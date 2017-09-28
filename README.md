
# Alkohol

Aplikacja stworzona za pomocą Spring Boota i zastosowana jest baza danych h2.

## Wprowadzenie

Służy do obliczeń alkoholowych dwoma metodami: objętościową i wagową.
Aplikacja wczytuje tablice alkoholometryczne do bazy danych h2.
Po wprowadzeniu danych w formularzu odczytuje z bazy te wartości i wyświetla je oraz używa do wykowywania potrzebnych obliczeń, które też są wyświetlone w tabeli.

## Przykład

Wprowadzenie do bazy danych jednej z tabeli z pliku tekstowego:

```Java
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

```
