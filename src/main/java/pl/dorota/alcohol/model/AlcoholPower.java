package pl.dorota.alcohol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AlcoholPower {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	// temperatura alkoholu  [°C]  -10 do +30  co 0,5
	//regex ((-\d[,\.][05])|(^-10)|(-\d))|([\+]?30)|([\+]?[12]\d[,\.][05])|([\+]?\d[,\.][05])
	public Double temperature;
	//moc pozorna - nominalne wskazanie aloholomierza [% obj] 70-101,9  co 0,1 
	//regex ([1][0][01][,\.]?\d?)|([789]\d[,\.]?\d?)
	public Double powerMeasured;
	// moc rzeczywista spirytusu [% obj. w 20°C] 
	public Double powerCalculated;

	@Override
	public String toString() {
		return "AlcoholPower [id=" + id + ", temperature=" + temperature + ", powerMeasured=" + powerMeasured
				+ ", powerCalculated=" + powerCalculated + "]";
	}

}
