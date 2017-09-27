package pl.dorota.alcohol.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.dorota.alcohol.data.AlcoholPowerRepository;
import pl.dorota.alcohol.data.CorrectionToVolumeRepository;
import pl.dorota.alcohol.data.VolumeAlcoholRepository;
//import pl.dorota.alcohol.data.CorrectionToVolumeRepository;
import pl.dorota.alcohol.model.AlcoholPower;
import pl.dorota.alcohol.model.CorrectionToVolume;
import pl.dorota.alcohol.model.VolumeAlcohol;
//import pl.dorota.alcohol.model.CorrectionToVolume;

@Controller
public class MainController {

	@Autowired
	private AlcoholPowerRepository alcoholPowerRepository;
	
	@Autowired
	private CorrectionToVolumeRepository correctionToVolumeRepository;
	
	@Autowired
	private VolumeAlcoholRepository volumeAlcoholRepository;

	@RequestMapping(value = "/")
	public String index(Model model) {
		return "start";
	}

	@RequestMapping(value = "/calculatePower")
	public String calculatePower(Model model,
			@RequestParam(value = "powerMeasured") String powerMeasured,
			@RequestParam(value = "temperature") String temperature,
			@RequestParam(value = "givenVolume") String givenVolume,
			@RequestParam(value = "temperatureTank") String temperatureTank) {
		//tablica 7c
		double realPower = 0.0;  //moc rzeczywista alkoholu
		List<String> errors = new LinkedList<>();
		double temperatureDouble = 0.0;
		try {
			temperatureDouble = Double.valueOf(temperature.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość temperatury");
		}
		model.addAttribute("temperatureDouble", temperatureDouble);
		double powerMeasuredDouble = 0.0;
		try {
			powerMeasuredDouble = Double.valueOf(powerMeasured.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość mocy");
		}
		model.addAttribute("powerMeasured", powerMeasured);
		AlcoholPower alcoholPower = alcoholPowerRepository.findByTemperatureAndPowerMeasured(temperatureDouble, powerMeasuredDouble);
		model.addAttribute("powerCalculated", (alcoholPower != null) ? alcoholPower.powerCalculated : "brak wartości");
		realPower = alcoholPower.powerCalculated;
		double temperatureTankDouble = 0.0;
		try {
			temperatureTankDouble = Double.valueOf(temperatureTank.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość temperatury, podaj w przedziale -10º do +30º  co 0,5");
		}
		model.addAttribute("temperatureTankDouble", temperatureTankDouble);
		int givenVolumeInt = 0;
		try {
			givenVolumeInt = Integer.valueOf(givenVolume.trim());
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość objętości");
		}
		model.addAttribute("givenVolumeInt", givenVolumeInt);
		// z tablicy 7c
		int powerMeasuredInteger = (int)Math.round(realPower);
		
		CorrectionToVolume correctionToVolume = correctionToVolumeRepository.findByTemperatureTankAndPowerMeasuredInt(temperatureTankDouble, powerMeasuredInteger);
		model.addAttribute("correctionCalculated", (correctionToVolume != null) ? correctionToVolume.correctionCalculated : "brak wartości");
		int givenWeightInt = 1000;
		// z tablicy 7c
		powerMeasuredDouble = realPower;
		VolumeAlcohol volumeAlcohol = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(givenWeightInt, powerMeasuredDouble);
		model.addAttribute("volumeCalculated", (volumeAlcohol != null) ? volumeAlcohol.volumeCalculated : "brak wartości");
		model.addAttribute("errors", errors);
		return "start";
	}

}
