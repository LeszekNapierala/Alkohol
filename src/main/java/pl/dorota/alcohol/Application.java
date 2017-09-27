package pl.dorota.alcohol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import pl.dorota.alcohol.data.AlcoholPowerRepository;
import pl.dorota.alcohol.data.CorrectionToVolumeRepository;
import pl.dorota.alcohol.data.VolumeAlcoholRepository;
import pl.dorota.alcohol.model.AlcoholPower;
import pl.dorota.alcohol.model.CorrectionToVolume;
import pl.dorota.alcohol.model.VolumeAlcohol;


@SpringBootApplication
public class Application {

	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(AlcoholPowerRepository alcoholPowerRepository, CorrectionToVolumeRepository correctionToVolumeRepository, VolumeAlcoholRepository volumeAlcoholRepository) {
		return (args) -> {
			Resource resource = resourceLoader.getResource("classpath:tablice7c.csv");
			InputStream alcoholPowerData = resource.getInputStream();
			String line;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(alcoholPowerData))) {
				List<Double> powerMeasuredValues = new ArrayList<>();
				//odczyt z pliku pierwszego wiersza jako nagłówka
				if ((line = br.readLine()) != null) {
					String[] words = line.split(";");
					for (int i = 1; i < words.length; ++i) {
						powerMeasuredValues.add(Double.valueOf(words[i]));
					}
				}
				// zapis danych do tabeli 4-kolumny: id,  powerMeasured, temperature, powerCalculated
				while ((line = br.readLine()) != null) {
					if (line.isEmpty()) {
						break;
					}
					String[] words = line.split(";");
					for (int i = 1; i < words.length; ++i) {
						AlcoholPower alcoholPower = new AlcoholPower();
						alcoholPower.powerMeasured = powerMeasuredValues.get(i - 1);
						alcoholPower.temperature = Double.valueOf(words[0]);
						alcoholPower.powerCalculated = Double.valueOf(words[i]);
						alcoholPowerRepository.save(alcoholPower);
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			resource = resourceLoader.getResource("classpath:tablice8d.csv");
			InputStream correctionToVolumeData = resource.getInputStream();
			String line2;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(correctionToVolumeData))) {
				List<Integer> correctionToVolumeValues = new ArrayList<>();
				//odczyt z pliku pierwszego wiersza jako nagłówka
				if ((line2 = br.readLine()) != null) {
					String[] words = line2.split(";");
					for (int i = 1; i < words.length; ++i) {
						correctionToVolumeValues.add(Integer.valueOf(words[i]));
					}
				}
				// zapis danych do tabeli 4-kolumny: id,  powerMeasured, temperature, powerCalculated
				while ((line2 = br.readLine()) != null) {
					if (line2.isEmpty()) {
						break;
					}
					String[] words = line2.split(";");
					for (int i = 1; i < words.length; ++i) {
						CorrectionToVolume correctionToVolume = new CorrectionToVolume();
						correctionToVolume.powerMeasuredInt = correctionToVolumeValues.get(i - 1);
						correctionToVolume.temperatureTank = Double.valueOf(words[0]);
						correctionToVolume.correctionCalculated = Double.valueOf(words[i]);
						correctionToVolumeRepository.save(correctionToVolume);
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			resource = resourceLoader.getResource("classpath:tablice9b.csv");
			InputStream volumeAlcoholData = resource.getInputStream();
			String line3;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(volumeAlcoholData))) {
				List<Double> volumeAlcoholValues = new ArrayList<>();
				//odczyt z pliku pierwszego wiersza jako nagłówka
				if ((line3 = br.readLine()) != null) {
					String[] words = line3.split(";");
					for (int i = 1; i < words.length; ++i) {
						volumeAlcoholValues.add(Double.valueOf(words[i]));
					}
				}
				// zapis danych do tabeli 4-kolumny: id,  powerMeasured, temperature, powerCalculated
				while ((line2 = br.readLine()) != null) {
					if (line2.isEmpty()) {
						break;
					}
					String[] words = line2.split(";");
					for (int i = 1; i < words.length; ++i) {
						VolumeAlcohol volumeAlcohol = new VolumeAlcohol();
						volumeAlcohol.powerMeasuredD = volumeAlcoholValues.get(i - 1);
						volumeAlcohol.givenWeight = Integer.valueOf(words[0]);
						volumeAlcohol.volumeCalculated = Double.valueOf(words[i]);
						volumeAlcoholRepository.save(volumeAlcohol);
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			};



		}

	}




