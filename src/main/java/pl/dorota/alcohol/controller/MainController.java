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

		if (errors.isEmpty()) {			
			AlcoholPower alcoholPower = alcoholPowerRepository.findByTemperatureAndPowerMeasured(temperatureDouble, powerMeasuredDouble);
			model.addAttribute("powerCalculated", (alcoholPower != null) ? alcoholPower.powerCalculated : "brak wartości");
			realPower = alcoholPower.powerCalculated * 10;
			realPower = Math.round(realPower);
			realPower /= 10;
			model.addAttribute("realPower", realPower);
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		//----------------------------------------------------
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
		double correction = 0.0;
		if (errors.isEmpty()) {
			CorrectionToVolume correctionToVolume = correctionToVolumeRepository.findByTemperatureTankAndPowerMeasuredInt(temperatureTankDouble, powerMeasuredInteger);
			model.addAttribute("correctionCalculated", (correctionToVolume != null) ? correctionToVolume.correctionCalculated : "brak wartości");
			correction = correctionToVolume.correctionCalculated - 0.07;
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		//---------------------------------------------
		int givenWeightInt = 1000;
		// z tablicy 7c
		powerMeasuredDouble = realPower;
		double volumeCalc = 0.0;
		if (errors.isEmpty()) {
			VolumeAlcohol volumeAlcohol = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(givenWeightInt, powerMeasuredDouble);
			model.addAttribute("volumeCalculated", (volumeAlcohol != null) ? volumeAlcohol.volumeCalculated : "brak wartości");
			volumeCalc = volumeAlcohol.volumeCalculated;
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}	
		//		double correction = correctionToVolume.correctionCalculated - 0.07;
		double volumeIn20Degrees = (100 + correction) * givenVolumeInt / 100;
		double volumeIn20DegreesView = volumeIn20Degrees *100;
		volumeIn20DegreesView = Math.round(volumeIn20DegreesView);
		volumeIn20DegreesView = volumeIn20DegreesView / 100;
		model.addAttribute("volumeIn20DegreesView", volumeIn20DegreesView);
		int volumeOf100Percent = (int)Math.round(volumeIn20Degrees * realPower / 100);
		model.addAttribute("volumeOf100Percent", volumeOf100Percent);
		double netWeight = (100000 / volumeCalc) * volumeOf100Percent;
		netWeight = Math.round(netWeight);
		netWeight = netWeight / 100;
		model.addAttribute("netWeight", netWeight);

		model.addAttribute("errors", errors);
		return "start";
	}
	@RequestMapping(value = "/calculateWeigth")
	public String calculateWeigth(Model model,
			@RequestParam(value = "powerMeasuredWeigth") String powerMeasuredWeigth,
			@RequestParam(value = "temperatureWeigth") String temperatureWeigth,
			@RequestParam(value = "grossWeight") String grossWeight,
			@RequestParam(value = "tareWeigth") String tareWeigth) {


		List<String> errors = new LinkedList<>();

		double realPowerWeigth = 0.0;  //moc rzeczywista alkoholu

		double temperatureDoubleWeigth = 0.0;
		try {
			temperatureDoubleWeigth = Double.valueOf(temperatureWeigth.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość temperatury");
		}
		model.addAttribute("temperatureDoubleWeigth", temperatureDoubleWeigth);
		double powerMeasuredDoubleWeigth = 0.0;
		try {
			powerMeasuredDoubleWeigth = Double.valueOf(powerMeasuredWeigth.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość mocy");
		}
		model.addAttribute("powerMeasuredWeigth", powerMeasuredWeigth);
		AlcoholPower alcoholPowerWeigth = alcoholPowerRepository.findByTemperatureAndPowerMeasured(temperatureDoubleWeigth, powerMeasuredDoubleWeigth);
		model.addAttribute("powerCalculatedWeigth", (alcoholPowerWeigth != null) ? alcoholPowerWeigth.powerCalculated : "brak wartości");
		realPowerWeigth = alcoholPowerWeigth.powerCalculated * 10;
		realPowerWeigth = Math.round(realPowerWeigth);
		realPowerWeigth /= 10;

		model.addAttribute("realPowerWeigth", realPowerWeigth);


		int grossWeightInt = 0;
		try {
			grossWeightInt = Integer.valueOf(grossWeight.trim());
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość wagi brutto");
		}
		model.addAttribute("grossWeightInt", grossWeightInt);
		int tareWeigthInt = 0;
		try {
			tareWeigthInt = Integer.valueOf(tareWeigth.trim());
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość wagi tara");
		}
		model.addAttribute("tareWeigthInt", tareWeigthInt);
		int calculatedNetWeight = grossWeightInt - tareWeigthInt;
		model.addAttribute("calculatedNetWeight", calculatedNetWeight);
		//------------------------------------------
		//29987 
		int auxiliaryVariable1 = 0; //*100
		int auxiliaryVariable2 = 0; //*1
		int auxiliaryVariable3 = 0; // /100
		double volumeOf100PercentWegth = 0.0;
		if (calculatedNetWeight>10000) {
			auxiliaryVariable1 = calculatedNetWeight / 1000; // = 29
			auxiliaryVariable1 = auxiliaryVariable1 * 10;	//290
			VolumeAlcohol volumeAlcoholWeight1 = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(auxiliaryVariable1, realPowerWeigth);
			model.addAttribute("volumeCalculated1", (volumeAlcoholWeight1 != null) ? volumeAlcoholWeight1.volumeCalculated : "brak wartości");
			volumeOf100PercentWegth = volumeAlcoholWeight1.volumeCalculated * 100 ;
		}
		auxiliaryVariable2 = calculatedNetWeight - auxiliaryVariable1*100; //29987-29000=987
		if (auxiliaryVariable2>100) {
			auxiliaryVariable2 = auxiliaryVariable2 / 10; // 98
			auxiliaryVariable2 = auxiliaryVariable2 * 10; //980
			VolumeAlcohol volumeAlcoholWeight2 = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(auxiliaryVariable2, realPowerWeigth);
			model.addAttribute("volumeCalculated1", (volumeAlcoholWeight2 != null) ? volumeAlcoholWeight2.volumeCalculated : "brak wartości");
			volumeOf100PercentWegth += volumeAlcoholWeight2.volumeCalculated ;
		}
		auxiliaryVariable3 = calculatedNetWeight - auxiliaryVariable1*100 - auxiliaryVariable2; //29987 - 290*100 - 980 = 7
		if (auxiliaryVariable3>0) {
			auxiliaryVariable3 = auxiliaryVariable3 * 100; // 700
			VolumeAlcohol volumeAlcoholWeight3 = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(auxiliaryVariable3, realPowerWeigth);
			model.addAttribute("volumeCalculated1", (volumeAlcoholWeight3 != null) ? volumeAlcoholWeight3.volumeCalculated : "brak wartości");
			volumeOf100PercentWegth = volumeAlcoholWeight3.volumeCalculated / 100;
		}
		int volumeOf100PercentWegthInt = (int)Math.round(volumeOf100PercentWegth);
		model.addAttribute("volumeOf100PercentWegthInt", volumeOf100PercentWegthInt);
		int volumeIn20DegreesWegth = (int)Math.round(100.0 * volumeOf100PercentWegthInt/realPowerWeigth);
		model.addAttribute("volumeIn20DegreesWegth", volumeIn20DegreesWegth);




		model.addAttribute("errors", errors);
		return "start";
	}


}
