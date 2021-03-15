package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IClientSubscriptionMasterDao;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IQuotationDAO;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Company;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ClientSubscriptionForm;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICompanyService;

@Transactional
@Service
public class ClientSubscriptionMasterServiceImpl implements IClientSubscriptionMasterService {
	
	@Autowired
	private IClientSubscriptionMasterDao clientSubscriptionMasterDao;
	
	@Autowired
	private IQuotationDAO quotationDao;
	
	@Autowired
	private ICompanyDAO companyDao;

	@Autowired
	private ICompanyService companyService;
	
	@Override
	public void add(ClientSubscriptionMaster entity) throws MyWebException {
		try{
			Quotation quotation = quotationDao.findOne(entity.getQuotationId());
			quotation.setFlag(true);
			entity.setQuotation_id(quotation);
			entity.setCompany(companyDao.findOne(quotation.getEmail()));
			entity.setCreated_date(new LocalDate());
			clientSubscriptionMasterDao.create(entity);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(ClientSubscriptionMaster entity) throws MyWebException {
		clientSubscriptionMasterDao.update(entity);
		
	}

	@Override
	public List<ClientSubscriptionMaster> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientSubscriptionMaster getById(Long id) throws MyWebException {
		return clientSubscriptionMasterDao.findOne(id);
	}

	@Override
	public ClientSubscriptionMaster getById(String id) throws MyWebException {
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
	public void remove(ClientSubscriptionMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(ClientSubscriptionMaster entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClientSubscriptionMaster isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientSubscriptionMaster getClientSubscriptionByCompanyId(Long CompanyId) {
		// TODO Auto-generated method stub
		return clientSubscriptionMasterDao.getClientSubscriptionByCompanyId(CompanyId);
	}

	@Override
	public Long getquoteofcompany(Long company_id) {
		// TODO Auto-generated method stub
		return clientSubscriptionMasterDao.getquoteofcompany(company_id);
	}

	@Override
	public List<ClientSubscriptionForm> getSubscriptionReport() {
		
		List<ClientSubscriptionForm> list = new ArrayList<ClientSubscriptionForm>();
	
		for(ClientSubscriptionMaster subscrip : clientSubscriptionMasterDao.getSubscriptionReport()) 
		{
			if(subscrip.getCompany()!=null && subscrip.getCompany().getStatus()==MyAbstractController.STATUS_SUBSCRIBED_USER)
			{
				if(subscrip.getQuotation_id()!=null)
				{
					Float amount = (float)0;
					for(QuotationDetails details:subscrip.getQuotation_id().getQuotationDetails() )
					{
						if(details.getAmount()!=null)
						{
							amount=amount+details.getAmount();
						}
					}
					ClientSubscriptionForm form = new ClientSubscriptionForm();
					form.setCompany(companyService.findOneWithAll(subscrip.getCompany().getCompany_id()));
					form.setAmount(amount);
					if(subscrip.getSubscription_from()!=null)
					{
					form.setSubscription_from(subscrip.getSubscription_from());
					}
					if(subscrip.getSubscription_to()!=null)
					{
					form.setSubscription_to(subscrip.getSubscription_to());
					}
					if(subscrip.getPayment_mode()!=null)
					{
					form.setPayment_mode(subscrip.getPayment_mode());
					}
					list.add(form);
				}
				
			}
		}
		return list;
	}

}
