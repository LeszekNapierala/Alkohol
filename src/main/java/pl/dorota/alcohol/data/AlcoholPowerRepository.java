package pl.dorota.alcohol.data;

import org.springframework.data.repository.CrudRepository;

import pl.dorota.alcohol.model.AlcoholPower;

public interface AlcoholPowerRepository extends CrudRepository<AlcoholPower, Integer> {

	public AlcoholPower findByTemperatureAndPowerMeasured(double temperature, double powerMeasured);
	

}
