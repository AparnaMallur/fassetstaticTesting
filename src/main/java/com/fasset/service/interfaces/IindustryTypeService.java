/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;
import java.util.List;
import com.fasset.entities.IndustryType;
import com.fasset.entities.Suppliers;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IindustryTypeService extends IGenericService<IndustryType>{

	public String saveIndustryType(IndustryType type);
	public List<IndustryType> findAll();
	public String deleteByIdValue(Long entityId);
	public String deleteSubLedger(Long induId, Long subId);
	List<IndustryType> findAllactive();
	List<IndustryType> findAllListing();
	public IndustryType findOneWithAll(Long Id);

}
