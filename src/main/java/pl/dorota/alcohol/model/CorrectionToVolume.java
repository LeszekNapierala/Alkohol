package pl.dorota.alcohol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CorrectionToVolume {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	// odczytana objętość w dm3 
	public Integer givenVolume;
	// temperatura alkoholu w zbiorniku [°C]
	public Double temperatureTank;
	//moc spirytusu odczytana z AlkoholPower.powerCalculated zaokrąglona do liczby całkowitej
	public Integer powerMeasuredInt ; //= Math.round(AlcoholPower.powerCalculated);
	// moc spirytusu [% obj. w 20°C]
	public Double correctionCalculated;

	@Override
	public String toString() {
		return "CorrectionToVolume [id=" + id + ", temperatureTank=" + temperatureTank + ", powerMeasuredInt=" + powerMeasuredInt
				+ ", correctionCalculated=" + correctionCalculated + "]";
	}

}
