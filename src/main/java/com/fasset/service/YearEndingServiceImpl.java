package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IYearEndingDAO;
import com.fasset.entities.ActivityLog;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.DateForm;
import com.fasset.service.interfaces.IYearEndingService;

@Service
@Transactional
public class YearEndingServiceImpl implements IYearEndingService{

	@Autowired
	private IYearEndingDAO dao ;
	
	@Override
	public void add(YearEnding entity) throws MyWebException {
		// TODO Auto-generated method stub
		dao.create(entity);
	}

	@Override
	public void update(YearEnding entity) throws MyWebException {
		// TODO Auto-generated method stub
		dao.update(entity);
	}

	@Override
	public List<YearEnding> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YearEnding getById(Long id) throws MyWebException {
		
		return dao.findOne(id);
	}

	@Override
	public YearEnding getById(String id) throws MyWebException {
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
	public void remove(YearEnding entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(YearEnding entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public YearEnding isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<YearEnding> findAllYearEnding(Long comapany_id) {
		// TODO Auto-generated method stub
		return dao.findAllYearEnding(comapany_id);
	}

	@Override
	public List<YearEnding> findAllYearEnding() {
		// TODO Auto-generated method stub
		return dao.findAllYearEnding();
	}

	@Override
	public List<ActivityLog> findAllActivityLog(Long user_id,LocalDate from_date, LocalDate to_date) {
		// TODO Auto-generated method stub
		return dao.findAllActivityLog(user_id,from_date,to_date);
	}

	@Override
	public void saveActivityLogForm(Long user_id, Long customer_id, Long supplier_id, Long product_id, Long ledger_id,
			Long subLedger_id, Long bank_id, Long quotation_id, Long primary_approval, Long secondary_approval,
			Long rejection) {
		dao.saveActivityLogForm(user_id, customer_id, supplier_id, product_id, ledger_id, subLedger_id, bank_id, quotation_id, primary_approval, secondary_approval, rejection);
		
	}

	@Override
	public YearEnding findYearEnd(Long yearId, Long company_id) {
		// TODO Auto-generated method stub
		return dao.findYearEnd(yearId, company_id);
	}

	@Override
	public List<YearEndJvSubledgerDetails> findAllYearEndJVdtls(Long id) {
		// TODO Auto-generated method stub
		return dao.findAllYearEndJVdtls(id);
	}

}
