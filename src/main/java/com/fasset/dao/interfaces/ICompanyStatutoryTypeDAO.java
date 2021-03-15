/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.CompanyStatutoryType;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface ICompanyStatutoryTypeDAO extends IGenericDao<CompanyStatutoryType>{
	Long saveCompanyStatutoryTypeDao(CompanyStatutoryType type);
	public String deleteByIdValue(Long entityId);
	List<CompanyStatutoryType> findAllactive();
	List<CompanyStatutoryType> findAllListing();
}
