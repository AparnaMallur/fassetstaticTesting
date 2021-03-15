/**
 * mayur suramwar
 */
package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.IndustryType;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IindustryTypeService;
/**
 * 
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Service
@Transactional
public class IndustryTypeSeviceImpl implements IindustryTypeService{
 
	@Autowired
	private IindustryTypeDAO typedao;
	
	@Override
	public void add(IndustryType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(IndustryType entity) throws MyWebException {
		typedao.update(entity);
		
	}
	
	@Override
	public List<IndustryType> list() {
		return typedao.findAll();
	}

	@Override
	public IndustryType getById(Long id) throws MyWebException {
		return typedao.findOne(id);
	}

	@Override
	public IndustryType getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(IndustryType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(IndustryType entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String saveIndustryType(IndustryType type) {
		Long id= typedao.saveindustryTypedao(type);
		if(id!=null){
			return " Industry Type saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<IndustryType> findAll() {
		// TODO Auto-generated method stub
		return typedao.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg = typedao.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public String deleteSubLedger(Long induId, Long subId) {
		
		String msg = typedao.deleteSubLedger(induId, subId);
		return msg;
	}

	@Override
	public IndustryType isExists(String name) {
		return typedao.isExists(name);
	}

	@Override
	public List<IndustryType> findAllactive() {
		return typedao.findAllactive();
	}

	@Override
	public List<IndustryType> findAllListing() {
		return typedao.findAllListing();
	}

	@Override
	public IndustryType findOneWithAll(Long Id) {		
		return typedao.findOneWithAll(Id);
	}

}
