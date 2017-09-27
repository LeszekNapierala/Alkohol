package pl.dorota.alcohol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VolumeAlcohol {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	// odczytana waga alkoholu w kg
	public Integer givenWeight;
	//moc spirytusu odczytana z AlkoholPower.powerCalculated zaokrąglona do liczby całkowitej
	public Double powerMeasuredD ; //= AlcoholPower.powerCalculated;
	// Objętość alkoholu w 20°C
	public Double volumeCalculated;

	@Override
	public String toString() {
		return "VolumeAlcohol [id=" + id + ", powerMeasuredD=" + powerMeasuredD + ", givenWeight=" + givenWeight
				+ ", volumeCalculated=" + volumeCalculated + "]";
	}

}