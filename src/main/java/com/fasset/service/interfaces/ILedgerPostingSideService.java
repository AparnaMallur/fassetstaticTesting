/**
 * mayur suramwar
 */
package com.fasset.service.interfaces;
import java.util.List;
import com.fasset.entities.LedgerPostingSide;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */

public interface ILedgerPostingSideService extends IGenericService<LedgerPostingSide>{

	public List<LedgerPostingSide> findAll();
	
}
