package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.InventoryQuantityAdjustment;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IInventoryQuantityAdjService extends IGenericService<InventoryQuantityAdjustment>{
	public String deleteByIdValue(Long entityId);
	List<InventoryQuantityAdjustment> findAllInventoryOfCompany(Long CompanyId);
}
