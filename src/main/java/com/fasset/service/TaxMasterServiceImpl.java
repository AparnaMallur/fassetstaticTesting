/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.entities.TaxMaster;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.ITaxMasterService;
import com.fasset.dao.interfaces.ITaxMasterDAO ;

/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class TaxMasterServiceImpl implements ITaxMasterService{

	@Autowired
	private ITaxMasterDAO ITaxMasterDAO ;
	
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#add(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void add(TaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#update(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void update(TaxMaster taxMaster) throws MyWebException {
		taxMaster.setTax_name(taxMaster.getTax_name());
		 taxMaster.setVat(Float.valueOf(taxMaster.getVat1()));
		 taxMaster.setCst(Float.valueOf(taxMaster.getCst1()));
		 taxMaster.setExcise(Float.valueOf(taxMaster.getExcise1()));
		 taxMaster.setUpdate_date(new LocalDateTime());
		 ITaxMasterDAO.update(taxMaster);
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#list()
	 */
	@Override
	public List<TaxMaster> list() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.Long)
	 */
	@Override
	public TaxMaster getById(Long id) throws MyWebException {
		return ITaxMasterDAO.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#getById(java.lang.String)
	 */
	@Override
	public TaxMaster getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#removeById(java.lang.Long)
	 */
	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#removeById(java.lang.String)
	 */
	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#remove(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void remove(TaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#countRegs()
	 */
	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#merge(com.fasset.entities.abstracts.AbstractEntity)
	 */
	@Override
	public void merge(TaxMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ITaxMasterService#saveTaxMaster(com.fasset.entities.TaxMaster)
	 */
	@Override
	public String saveTaxMaster(TaxMaster taxMaster) {
		taxMaster.setTax_name(taxMaster.getTax_name());
	    taxMaster.setVat(Float.valueOf(taxMaster.getVat1()));
	    taxMaster.setCst(Float.valueOf(taxMaster.getCst1()));
	    taxMaster.setExcise(Float.valueOf(taxMaster.getExcise1()));
	    taxMaster.setCreated_date(new LocalDateTime());
		Long id= ITaxMasterDAO.saveTaxMasterDao(taxMaster);
		if(id!=null){
			return " Tax saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ITaxMasterService#findAll()
	 */
	@Override
	public List<TaxMaster> findAll() {
		return ITaxMasterDAO.findAll();
	}

	@Override
	public List<TaxMaster> findAllListing() {
		return ITaxMasterDAO.findAllListing();
	}
	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.ITaxMasterService#deleteByIdValue(java.lang.Long)
	 */
	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return ITaxMasterDAO.deleteByIdValue(entityId);
	}

	/* (non-Javadoc)
	 * @see com.fasset.service.interfaces.generic.IGenericService#isExists(java.lang.String)
	 */
	@Override
	public TaxMaster isExists(String name) {
		// TODO Auto-generated method stub
		return ITaxMasterDAO.isExists(name);
	}

	
}
