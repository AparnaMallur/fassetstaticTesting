package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.InventoryQuantityAdjustment;

public interface IInventoryQuantityAdjDAO extends IGenericDao<InventoryQuantityAdjustment>{
	public String deleteByIdValue(Long entityId);
	List<InventoryQuantityAdjustment> findAllInventoryOfCompany(Long CompanyId);

}
