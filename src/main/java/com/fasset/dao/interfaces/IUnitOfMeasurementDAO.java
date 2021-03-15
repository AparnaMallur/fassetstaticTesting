package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.UnitOfMeasurement;

public interface IUnitOfMeasurementDAO extends IGenericDao<UnitOfMeasurement>{
	public String deleteByIdValue(Long entityId);

	List<UnitOfMeasurement> findAllListing();
}
