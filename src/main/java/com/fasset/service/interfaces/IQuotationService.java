package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IQuotationService extends IGenericService<Quotation>{
	String deleteByIdValue(Long entityId);
	List<Quotation> findAll();
	Quotation getQuotation(String mailId, String quotationNumber);
	String savequote(Quotation quotation);
	void sendquotemail(Quotation quotation);
	public List <QuotationDetails> findAllDetailsList(Long entryId);
	public String updateQuatationDetails(Quotation quotation);
	public String deleteQuotationDetails (Long quoatId);
	public Quotation getQuotation(String mailId);
	public void updateQuatationDetailsAfterSaveAndBack(Quotation quotation);
	List<QuotationDetails> findAllimportDetailsList();
	public List<QuotationDetails> findAllTransactioimportDetailsList();
	public void saveupdatedservices(Long entryId, Boolean status);
	public Boolean findAllimportDetailsUser(String email, Long quote_id);
	public Boolean findAllTransactionimportDetailsUser(String email, Long quote_id);
	Integer findAllDashboard();
	List<Quotation> findAllListing();
	public CopyOnWriteArrayList<Quotation> findAllQuotations();
	List<ClientSubscriptionMaster> findAllSubscriptionList();
	public String deleteService(long  serviceid);
	
}
