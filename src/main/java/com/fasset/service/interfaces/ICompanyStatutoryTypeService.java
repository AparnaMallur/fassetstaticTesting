/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;


import java.util.List;

import com.fasset.entities.CompanyStatutoryType;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICompanyStatutoryTypeService extends IGenericService<CompanyStatutoryType>{

	public String saveCompanyStatutoryType(CompanyStatutoryType type);
	public List<CompanyStatutoryType> findAll();
	public String deleteByIdValue(Long entityId);
	List<CompanyStatutoryType> findAllactive();
	public List<CompanyStatutoryType> findAllListing();

}
