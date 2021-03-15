package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICustomerDAO;// if tax rate changed in deductee master changes to be reflectd in supplier and customer
import com.fasset.dao.interfaces.IDeducteeDao;
import com.fasset.dao.interfaces.ISupplierDAO;
import com.fasset.dao.interfaces.IindustryTypeDAO;
import com.fasset.entities.Deductee;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IDeducteeService;

@Service
@Transactional
public class DeducteeServiceImpl implements IDeducteeService{

	@Autowired
	private IDeducteeDao deducteeDao;
	@Autowired
	private ICustomerDAO customerDao;
	@Autowired
	private ISupplierDAO supplierDao;
	
	@Autowired
	private IindustryTypeDAO industryTypeDAO ;

	@Override
	public void add(Deductee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Deductee entity) throws MyWebException {
		
		try {
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		entity.setValue(Float.parseFloat(entity.getValue1()));
		deducteeDao.update(entity);
		long result=customerDao.updateTaxRate(entity.getDeductee_id(), entity.getValue());
		long result1=supplierDao.updateTaxRate(entity.getDeductee_id(), entity.getValue());
		
	}

	@Override
	public List<Deductee> list() {
		return deducteeDao.findAllDeductee();
	}

	@Override
	public Deductee getById(Long id) throws MyWebException {
		return deducteeDao.findOne(id);
	}

	@Override
	public Deductee getById(String id) throws MyWebException {
		return null;
	}

	@Override
	public void removeById(Long id) throws MyWebException {		
		
	}

	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Deductee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Deductee entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Deductee isExists(String name) {		
		return deducteeDao.isExists(name);
	}

	@Override
	public List<Deductee> findAllDeducteeOfCompany(Long CompanyId) {		
		return deducteeDao.findAllDeducteeOfCompany(CompanyId);
	}

	@Override
	public String addDeductee(Deductee entity) {
		try {
			entity.setIndustryType(industryTypeDAO.findOne(entity.getIndustry_id()));
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		entity.setCreated_date(new LocalDateTime());
		entity.setValue(Float.parseFloat(entity.getValue1()));
		Long id = deducteeDao.saveDeductee(entity);
		if(id > 0){
			return "TDS saved successfully";
		}
		else{
			return "Please try again ";
		}
	}

	@Override
	public List<Deductee> findAllDeducteeListing() {
		return deducteeDao.findAllDeducteeListing();
	}

	@Override
	public String deleteDeducteeByIdValue(Long id) {		
		return deducteeDao.deleteDeducteeByIdValue(id);
	}

	

	@Override
	public Float getTDSRate(LocalDate effectiveDate, Long tdsTypeId) {
		// TODO Auto-generated method stub
		return deducteeDao.getTDSRate(effectiveDate, tdsTypeId);
	}

	@Override
	public Float getTDSRateByTdsType(Long tdsTypeId) {
		return deducteeDao.getTDSRateByTdsType(tdsTypeId);
	}

	@Override
	public Deductee isExists1(String name, LocalDate effectiveDate) {
		// TODO Auto-generated method stub
		return deducteeDao.isExists1(name, effectiveDate);
	}
	
}
