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
	//Obliczania alkoholu metodą objętościową
	@RequestMapping(value = "/calculatePower")
	public String calculatePower(Model model,
			@RequestParam(value = "powerMeasured") String powerMeasured,
			@RequestParam(value = "temperature") String temperature,
			@RequestParam(value = "givenVolume") String givenVolume,
			@RequestParam(value = "temperatureTank") String temperatureTank) {
		//tablica 7c
		double realPower = 0.0;  //moc rzeczywista alkoholu
		List<String> errors = new LinkedList<>();
		double temperatureDouble = 0.0; // temperatura odczytana
		try {
			temperatureDouble = Double.valueOf(temperature.trim().replaceAll(",", "."));
			//temperatura ma być zaokrąglona do 0,5
			temperatureDouble = roundingUpTemeperature(temperatureDouble);

		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość temperatury, podaj w przedziale -10 do +30  co 0,5");
		}
		model.addAttribute("temperatureDouble", temperatureDouble);
		double powerMeasuredDouble = 0.0;
		try {
			powerMeasuredDouble = Double.valueOf(powerMeasured.trim().replaceAll(",", "."));
			//moc odczytana z alkoholomierza 
			powerMeasuredDouble = roundingUpPower(powerMeasuredDouble, 80, 101.9);
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość mocy, podaj w przedziale 80-101,9  co 0,1");
		}
		model.addAttribute("powerMeasured", powerMeasured);
		if (errors.isEmpty()) {			
			AlcoholPower alcoholPower = alcoholPowerRepository.findByTemperatureAndPowerMeasured(temperatureDouble, powerMeasuredDouble);
			model.addAttribute("powerCalculated", (alcoholPower != null) ? alcoholPower.powerCalculated : "brak wartości");
			//zaokrąglenie mocy rzeczywistej(z tablicy) do 0,1
			realPower = alcoholPower.powerCalculated * 10;
			realPower = Math.round(realPower);
			realPower /= 10;
			model.addAttribute("realPower", realPower);
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		//----------------------------------------------------tablica 8d
		double temperatureTankDouble = 0.0;
		try {
			temperatureTankDouble = Double.valueOf(temperatureTank.trim().replaceAll(",", "."));
			temperatureTankDouble = roundingUpTemeperature(temperatureTankDouble);
		}
		catch (NumberFormatException e) {
			errors.add("Błędna wartość temperatury zbiornika , podaj w przedziale -10º do +30º  co 0,5");
		}
		model.addAttribute("temperatureTankDouble", temperatureTankDouble);
		int givenVolumeInt = 0;
		try {
			givenVolumeInt = Integer.valueOf(givenVolume.trim());
			if (givenVolumeInt<0) {
				givenVolumeInt = 0;
			}
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość objętości podaj liczbe całkowitą wiekszą od 0");
		}
		model.addAttribute("givenVolumeInt", givenVolumeInt);
		// pobieramy moc rzeczywista obliczoną z tablicy 7c i zaokrągloną do całości
		int powerMeasuredInteger = (int)Math.round(realPower);
		
		
		double correction = 0.0;
		String correctionCalculatedTxt = "";
		if (errors.isEmpty()) {
			CorrectionToVolume correctionToVolume = correctionToVolumeRepository.findByTemperatureTankAndPowerMeasuredInt(temperatureTankDouble, powerMeasuredInteger);
			model.addAttribute("correctionCalculated", (correctionToVolume != null) ? correctionToVolume.correctionCalculated : "brak wartości");
			// poprawka 0,07 do temperatury 20
			correction = correctionToVolume.correctionCalculated - 0.07;
			correctionCalculatedTxt = correctionToVolume.correctionCalculated + " - 0.07 = " + correction;
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		model.addAttribute("correctionCalculatedTxt", correctionCalculatedTxt);
		//------------------------------------- z tablicy 9b
		int givenWeightInt = 1000;
		// pobieramy moc rzeczywista obliczoną z tablicy 7c
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
	//Obliczania alkoholu metodą wagową
	@RequestMapping(value = "/calculateWeigth")
	public String calculateWeigth(Model model,
			@RequestParam(value = "powerMeasuredWeigth") String powerMeasuredWeigth,
			@RequestParam(value = "temperatureWeigth") String temperatureWeigth,
			@RequestParam(value = "grossWeight") String grossWeight,
			@RequestParam(value = "tareWeigth") String tareWeigth) {

		List<String> errors = new LinkedList<>();

		double realPowerWeigth = 0.0;  //moc rzeczywista alkoholu

		double temperatureDoubleWeigth = 0.0; //temperatura odczytana
		try {
			temperatureDoubleWeigth = Double.valueOf(temperatureWeigth.trim().replaceAll(",", "."));
			temperatureDoubleWeigth = roundingUpTemeperature(temperatureDoubleWeigth);
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość temperatury, podaj w przedziale -10 do +30  co 0,5");
		}
		model.addAttribute("temperatureDoubleWeigth", temperatureDoubleWeigth);
		double powerMeasuredDoubleWeigth = 0.0; // moc odczytana z alkoholomierza
		try {
			powerMeasuredDoubleWeigth = Double.valueOf(powerMeasuredWeigth.trim().replaceAll(",", "."));
		}
		catch (NumberFormatException e) {
			errors.add("Niepoprawna wartość mocy, podaj w przedziale 70-101,9  co 0,1");
		}
		model.addAttribute("powerMeasuredWeigth", powerMeasuredWeigth);
		if (errors.isEmpty()) {
			AlcoholPower alcoholPowerWeigth = alcoholPowerRepository.findByTemperatureAndPowerMeasured(temperatureDoubleWeigth, powerMeasuredDoubleWeigth);
			model.addAttribute("powerCalculatedWeigth", (alcoholPowerWeigth != null) ? alcoholPowerWeigth.powerCalculated : "brak wartości");
			realPowerWeigth = alcoholPowerWeigth.powerCalculated * 10;
			realPowerWeigth = Math.round(realPowerWeigth);
			realPowerWeigth /= 10;
		} else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		model.addAttribute("realPowerWeigth", realPowerWeigth);

		//  pobrana waga brutto tara i obliczona netto
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
		int auxiliaryVariable = 0; // zmienna pomocnicza do obliczeń
		int temporary = calculatedNetWeight; 
		int[] array = new int[6]; // tablica przchowująca wagę natto rozbitą na wartości, aby odczytać z z tablicy 100 do 1000kg	
		double volumeOf100PercentWegth = 0.0;
		if (errors.isEmpty()) {
			if (temporary>=100000 && temporary < 1000000) {
				auxiliaryVariable = temporary / 10000;
				array[0] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[0], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 1000 ;			
			}
			temporary = temporary - array[0] * 1000;
			if (temporary>=10000) {
				auxiliaryVariable = temporary / 1000;
				array[1] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[1], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 100 ;			
			}
			temporary = temporary - array[1] * 100;
			if (temporary>=1000) {
				auxiliaryVariable = temporary / 100;
				array[2] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[2], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 10 ;			
			}
			temporary = temporary - array[2] * 10;
			if (temporary>=100) {
				auxiliaryVariable = temporary / 10;
				array[3] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[3], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 1 ;			
			}
			temporary = temporary - array[3] * 1;
			if (temporary>=10) {
				auxiliaryVariable = temporary / 1;
				array[4] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[4], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 0.1 ;			
			}
			temporary = temporary - (int)(array[4] * 0.1); //to jest liczba całkowita
			if (temporary>=1) {
				auxiliaryVariable = temporary * 10;
				array[5] = 10 * auxiliaryVariable;
				VolumeAlcohol volumeAlcoholWeight = volumeAlcoholRepository.findByGivenWeightAndPowerMeasuredD(array[5], realPowerWeigth);
				model.addAttribute("volumeCalculated", (volumeAlcoholWeight != null) ? volumeAlcoholWeight.volumeCalculated : "brak wartości");
				volumeOf100PercentWegth = volumeOf100PercentWegth + volumeAlcoholWeight.volumeCalculated * 0.01 ;			
			}
			//temporary = temporary - (int)(array[5] * 0.01); // 0
			
			
		}else {
			model.addAttribute("errors", errors);
			return "calculatePowerError";
		}
		
		
		
		// wyliczenie objętości spirytusu 100% i objęjetości w 20 stopniach 
		int volumeOf100PercentWegthInt = (int)Math.round(volumeOf100PercentWegth);
		model.addAttribute("volumeOf100PercentWegthInt", volumeOf100PercentWegthInt);
		double volumeIn20DegreesWegth = Math.round(10000.0 * volumeOf100PercentWegthInt/realPowerWeigth);
		volumeIn20DegreesWegth /= 100;
		model.addAttribute("volumeIn20DegreesWegth", volumeIn20DegreesWegth);

		model.addAttribute("errors", errors);
		return "start";
	}
	
	public double roundingUpTemeperature (double temperature) {
		Double result = null;
		if (temperature>=-10 && temperature<=30) {
			int temp = (int)Math.round(temperature * 10);
			
			int restOfDivision = 0;
			int modulo = temp % 10;
			if (modulo >= 0 && modulo <= 2) {
				restOfDivision = 0;
			}else if (modulo >= 3 && modulo <= 7) {
				restOfDivision = 5;
			} else {
				restOfDivision = 10;
			}
			result = temp / 10 + restOfDivision / 10.0;
		}
		return result;
	}
	public double roundingUpPower (double power, double min, double max) {
		Double result = null;
		if (power >= min && power <=max) {
			power *= 10;
			power = Math.round(power);
			result = power / 10;
		}
		return result;
	}

}
