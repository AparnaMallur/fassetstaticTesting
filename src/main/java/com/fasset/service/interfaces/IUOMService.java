package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.UnitOfMeasurement;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IUOMService extends IGenericService<UnitOfMeasurement>{
	public String deleteByIdValue(Long entityId);

	public List<UnitOfMeasurement> findAllListing();
}
