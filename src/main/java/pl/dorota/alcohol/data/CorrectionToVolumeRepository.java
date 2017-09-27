package pl.dorota.alcohol.data;

import org.springframework.data.repository.CrudRepository;

import pl.dorota.alcohol.model.CorrectionToVolume;

public interface CorrectionToVolumeRepository extends CrudRepository<CorrectionToVolume, Integer> {

	public CorrectionToVolume findByTemperatureTankAndPowerMeasuredInt(double temperatureTank, int powerMeasuredInt);
	

}
