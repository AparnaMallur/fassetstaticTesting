package com.fasset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IQuotationDAO;
import com.fasset.entities.ClientSubscriptionMaster;
import com.fasset.entities.Quotation;
import com.fasset.entities.QuotationDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
import com.fasset.form.QuotationDetailsForm;
import com.fasset.service.interfaces.IMailService;
import com.fasset.service.interfaces.IQuotationService;


@Service
@Transactional
public class QuotationServiceImpl  implements  IQuotationService{
	
	
	@Autowired
	private IMailService mailService;

	@Autowired
	private IQuotationDAO quotationDAO;
	
	@Override
	public String savequote(Quotation entity) {
		Long id= quotationDAO.savequote(entity);	
		if(id!=null){
			return " Quotation saved successfully";
		}
		else{
			return "Quotation try again ";
		}
	}

	@Override
	public void update(Quotation entity) throws MyWebException {
		quotationDAO.update(entity);
	}

	@Override
	public List<Quotation> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quotation getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return quotationDAO.findOne(id);
	}

	@Override
	public Quotation getById(String id) throws MyWebException {
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
	public void remove(Quotation entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Quotation entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Quotation isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		String msg=quotationDAO.deleteByIdValue(entityId);
		return msg;
	}

	@Override
	public List<Quotation> findAll() {
		// TODO Auto-generated method stub
		return quotationDAO.findAll();
	}

	@Override
	public Quotation getQuotation(String mailId, String quotationNumber) {
		return quotationDAO.getQuotation(mailId, quotationNumber);
	}

	@Override
	public void add(Quotation entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<QuotationDetails> findAllDetailsList(Long entryId) {
		// TODO Auto-generated method stub
		return quotationDAO.findAllDetailsList(entryId);
	}

	@Override
	public List<QuotationDetails> findAllimportDetailsList() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllimportDetailsList();
	}
	
	@Override
	public Boolean findAllimportDetailsUser(String email,Long quote_id) {
		// TODO Auto-generated method stub
		List<QuotationDetails> quotedetails=quotationDAO.findAllimportDetailsUser(email,quote_id);
		 if(quotedetails.isEmpty())
			 return false;
		 else
			 return true;
	}
	
	public void sendquotemail(Quotation quotation) {
		try {
			/*mailService.sendQuotationMail(quotation);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String updateQuatationDetails(Quotation quotation) {
		 List<QuotationDetailsForm> quoteDetailsList = new ArrayList<QuotationDetailsForm>();
		 quotation.setStatus(quotation.getStatus());
		 System.out.println(quotation.getQuoteDetails());
		 if(quotation.getQuoteDetails()!=null && quotation.getQuoteDetails()!="")
		 {
		 JSONArray jsonArray = new JSONArray(quotation.getQuoteDetails());
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				QuotationDetailsForm quotationDetails = new QuotationDetailsForm();
				quotationDetails.setQuotation_detail_id(jsonObject.getString("quotation_detail_id"));
				quotationDetails.setAmount(jsonObject.getString("amount"));
				quoteDetailsList.add(quotationDetails);
			}
			
		 }
		 quotation.setQuoteDetailsList(quoteDetailsList);
		return quotationDAO.updateQuatationDetails(quotation);	
	}

	@Override
	public String deleteQuotationDetails(Long quoatId) {
		// TODO Auto-generated method stub
		return quotationDAO.deleteQuotationDetails(quoatId);
	}

	@Override
	public Quotation getQuotation(String mailId) {
		// TODO Auto-generated method stub
		return quotationDAO.getQuotation(mailId);
	}

	@Override
	public void updateQuatationDetailsAfterSaveAndBack(Quotation quotation) {
		
		List<QuotationDetailsForm> quoteDetailsList = new ArrayList<QuotationDetailsForm>();
		 if(quotation.getQuoteDetails()!=null && quotation.getQuoteDetails()!="")
		 {
		 JSONArray jsonArray = new JSONArray(quotation.getQuoteDetails());
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				QuotationDetailsForm quotationDetails = new QuotationDetailsForm();
				quotationDetails.setQuotation_detail_id(jsonObject.getString("quotation_detail_id"));
				quotationDetails.setAmount(jsonObject.getString("amount"));
				quoteDetailsList.add(quotationDetails);
			}
			quotation.setQuoteDetailsList(quoteDetailsList);
		quotationDAO.updateQuatationDetailsAfterSaveAndBack(quotation);
		 }
		
	}

	@Override
	public void saveupdatedservices(Long entryId, Boolean status) {
		 quotationDAO.saveupdatedservices(entryId, status);
	}

	@Override
	public Integer findAllDashboard() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllDashboard();
	}

	@Override
	public List<Quotation> findAllListing() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllListing();
	}

	@Override
	public List<ClientSubscriptionMaster> findAllSubscriptionList() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllSubscriptionList();
	}

	@Override
	public CopyOnWriteArrayList<Quotation> findAllQuotations() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllQuotations();
	}

	@Override
	public String deleteService(long serviceid) {
		
		return quotationDAO.deleteService(serviceid);
	}

	@Override
	public List<QuotationDetails> findAllTransactioimportDetailsList() {
		// TODO Auto-generated method stub
		return quotationDAO.findAllTransactioimportDetailsList();
	}

	@Override
	public Boolean findAllTransactionimportDetailsUser(String email, Long quote_id) {
		List<QuotationDetails> quotedetails=quotationDAO.findAllTransactionimportDetailsUser(email,quote_id);
		 if(quotedetails.isEmpty())
			 return false;
		 else
			 return true;
	}

}