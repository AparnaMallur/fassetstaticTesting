package com.fasset.dao.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.exceptions.MyWebException;

public interface IQuotationDAO extends IGenericDao<Quotation>{
	public String deleteByIdValue(Long entityId);
	public List<Quotation> findAll();
	Quotation getQuotation(String mailId, String quotationNumber);
	Long savequote(Quotation quotation);
	public List <QuotationDetails> findAllDetailsList(Long entryId);
	public String updateQuatationDetails(Quotation quotation);
	public String deleteQuotationDetails (Long quoatId);
	public Quotation getQuotation(String mailId);
	public void updateQuatationDetailsAfterSaveAndBack(Quotation quotation);
	public List<QuotationDetails> findAllimportDetailsList();
	public List<QuotationDetails> findAllTransactioimportDetailsList();
	public void saveupdatedservices(Long entryId, Boolean status);
	public List<QuotationDetails> findAllimportDetailsUser(String email, Long quote_id);
	public List<QuotationDetails> findAllTransactionimportDetailsUser(String email, Long quote_id);
	public Integer findAllDashboard();
	public List<Quotation> findAllListing();
	public CopyOnWriteArrayList<Quotation> findAllQuotations();
	public List<ClientSubscriptionMaster> findAllSubscriptionList();
	Long getLastQuotationId();
	public String deleteService(long  serviceid);
	
}
