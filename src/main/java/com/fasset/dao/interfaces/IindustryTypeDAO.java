/**
 * mayur suramwar
 */
package com.fasset.dao.interfaces;
import java.util.List;
import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.IndustryType;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
public interface IindustryTypeDAO extends IGenericDao<IndustryType>{

	Long saveindustryTypedao(IndustryType type);
	public String deleteByIdValue(Long entityId);
	public String deleteSubLedger(Long induId , Long subId);
	List<IndustryType> findAllactive();
	List<IndustryType> findAllListing();
	public IndustryType findOneWithAll(Long Id);
}
