package pl.dorota.alcohol.data;

import org.springframework.data.repository.CrudRepository;

import pl.dorota.alcohol.model.VolumeAlcohol;


public interface VolumeAlcoholRepository extends CrudRepository<VolumeAlcohol, Integer> {

	public VolumeAlcohol findByGivenWeightAndPowerMeasuredD(int givenWeight, double powerMeasuredD);
	

}
