package com.fasset.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IReceiptDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Company;
import com.fasset.entities.Product;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.ISubLedgerService;

@Transactional
@Repository
public class ReceiptDAOImpl extends AbstractHibernateDao<Receipt> implements IReceiptDAO{
	
	@Autowired
	private ISubLedgerDAO subledgerDAO ;

	@Autowired
	private ISubLedgerService subledgerService;
	
	@Autowired
	private IBankService bankService;
	
	@Autowired
	private ICustomerService customerService;
	
	/*@Autowired
	private ISalesEntryDAO salesEntryDao;*/
	
	@Autowired
	private IOpeningBalancesDAO balanceDao;
	
	@Override
	public Receipt findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("receipt_id", id));
		return (Receipt) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Receipt> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Receipt order by receipt_id desc");
		return query.list();
	}
	
	@Override
	public Long saveReceiptDao(Receipt sup) {
		Session session = getCurrentSession();
		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		if(sup.getGst_applied()==1){
			informationList = sup.getInformationList();
		}
		sup.setLocal_time(new LocalTime());
		Long id = (Long) session.save(sup);
		session.flush();
		session.clear();
		if(sup.getGst_applied()==1){
			for(int i = 0;i<informationList.size();i++){
				ProductInformation information =  informationList.get(i);
				Query query = session.createQuery("update Receipt_product_details set quantity=:quantity,rate=:rate,discount=:discount,"
			      		+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
			      		+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,product_name=:product_name "
			      		+ "where receipt_header_id =:pid and product_id=:proid");
			 
				query.setParameter("pid", id);
				query.setParameter("proid", Long.parseLong(information.getProductId()));
				query.setParameter("quantity", Double.parseDouble(information.getQuantity()));
				query.setParameter("rate", Double.parseDouble(information.getRate()));
				query.setParameter("discount", Double.parseDouble(information.getDiscount()));
				query.setParameter("CGST", Double.parseDouble(information.getCGST()));
				query.setParameter("IGST", 	Double.parseDouble(information.getIGST()));
				query.setParameter("SGST", Double.parseDouble(information.getSGST()));
				query.setParameter("SCT", Double.parseDouble(information.getSCT()));
				query.setParameter("labourCharge", Double.parseDouble(information.getLabourCharge()));
				query.setParameter("freight", Double.parseDouble(information.getFreightCharges()));
				query.setParameter("transaction_amount",Double.parseDouble(information.getTransaction_amount()));
				query.setParameter("Others", Double.parseDouble(information.getOthers()));
				query.setParameter("UOM", information.getUnit());
				query.setParameter("HSNCode", information.getHsncode());
				query.setParameter("product_name", information.getProductname());
		      
				try{
					query.executeUpdate();		    	 
				}
				catch (Exception e){
					e.printStackTrace();		    	 
				}		
			}	
		}			
		return id;
	}
	@Override
	public Long saveReceiptDaoagainstbill(Receipt sup) {
		Session session = getCurrentSession();
		Long id=(Long) session.save(sup);
		session.flush();
		session.clear();
		return id;
	}
	
	@Override
	public void update(Receipt sup) throws MyWebException {
	   
		Session session = getCurrentSession();
		
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("receipt_id", sup.getReceipt_id()));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setFetchMode("products", FetchMode.JOIN);
		Receipt entity = (Receipt)criteria.uniqueResult();
		if(sup.getUpdated_by()!=null)
		{
			entity.setUpdated_by(sup.getUpdated_by());
		}
	    entity.setDate(sup.getDate());
	    
	    if((entity.getCustomer()!=null)&&(sup.getCustomer_id()!=null && sup.getCustomer_id()==-1)&&(sup.getSubledgerId()!=null && sup.getSubledgerId()>0)){
	    	if(sup.getCustomer()!=null)
	    	{
	    	entity.setCustomer(sup.getCustomer());
	    	}
	    	if(sup.getSales_bill_id()!=null)
	    	{
	    		 entity.setSales_bill_id(sup.getSales_bill_id());
	    	}
		   
	    	if(sup.getSubLedger()!=null)
	    	{
	    		 entity.setSubLedger(sup.getSubLedger());
	    	}
		   
	    }
	    else if((entity.getSubLedger()!=null)&&(sup.getCustomer_id()!=null && sup.getCustomer_id()>0)&&(sup.getSalesEntryId()!=null && sup.getSalesEntryId()>0)){
	    	
	    	if(sup.getCustomer()!=null)
	    	{
	    		entity.setCustomer(sup.getCustomer());
	    	}
	    	if(sup.getSales_bill_id()!=null)
	    	{
	    		entity.setSales_bill_id(sup.getSales_bill_id());
	    	}
	    	if(sup.getSubLedger()!=null)
	    	{
	    		entity.setSubLedger(sup.getSubLedger());
	    	}
		    
	    }
	    else if(sup.getCustomer_id() != null && sup.getCustomer_id() > 0)
	    {
	    	if(sup.getCustomer()!=null)
	    	{
	    		entity.setCustomer(sup.getCustomer());
	    	}
	    	
	    	if(sup.getSalesEntryId() != null && sup.getSalesEntryId()>0)
	    	{
		    entity.setSales_bill_id(sup.getSales_bill_id());
	    	}
	    }
	    else if(sup.getSubledgerId() != null && sup.getSubledgerId()>0)
	    {
	    	 entity.setSubLedger(sup.getSubLedger());
	    }
		
	    if(sup.getBankId() !=null && sup.getBankId() > 0)
	    {
	    entity.setBank(sup.getBank());
	    }
	    
	    if(sup.getCheque_no()!=null)
    	{
	    	entity.setCheque_no(sup.getCheque_no());
    	}
	  
	    if(sup.getCheque_date()!=null)
    	{
	    	 entity.setCheque_date(sup.getCheque_date());
    	}
	    if(sup.getAdvance_payment()!=null)
    	{
	    	entity.setAdvance_payment(sup.getAdvance_payment());
    	}
	    if(sup.getGst_applied()!=null)
    	{
	    	entity.setGst_applied(sup.getGst_applied());
    	}
	    if(sup.getAmount()!=null)
    	{
	    	entity.setAmount(sup.getAmount());
    	}
	    entity.setFlag(true);
		entity.setTds_paid(sup.getTds_paid());
		if(entity.getTds_paid()==true)
		{
			entity.setTds_amount(sup.getTds_amount());
		}
	    if(sup.getOther_remark()!=null)
    	{
	    	 entity.setOther_remark(sup.getOther_remark());
    	}
	    if(sup.getStatus()!=null)
    	{
	    	entity.setStatus(sup.getStatus());	
    	}
	    if(sup.getPayment_type()!=null)
    	{
	    	entity.setPayment_type(sup.getPayment_type());
    	}
		
		
		
		if(sup.getInformationList()!=null && sup.getInformationList().size()!=0){
			Set<Product> products = new HashSet<Product>();
			Set<Product> newproducts = new HashSet<Product>();
			List<ProductInformation> informationList = new ArrayList<ProductInformation>();
			informationList = sup.getInformationList();
			
			products = entity.getProducts();
			newproducts=sup.getProducts();
			Boolean flag = false;
			for(Product product :newproducts){				
				for(Product lastproducts:products){
					if(product.equals(lastproducts)){
						flag=true;
					}
				}
				
				
				if(flag==false){
					products.add(product);
					entity.setTransaction_value(sup.getTransaction_value());
					entity.setCgst(sup.getCgst());
					entity.setSgst(sup.getSgst());
					entity.setIgst(sup.getIgst());
					entity.setState_compansation_tax(sup.getState_compansation_tax());
					entity.setRound_off(sup.getRound_off());
				}
			}
			entity.setProducts(products);
			entity.setUpdate_date(new LocalDateTime());
			session.merge(entity);
			session.flush();
			session.clear();
			if(flag==false){
				for(int i = 0;i<informationList.size();i++){
					ProductInformation information =  informationList.get(i);
					Query query = session.createQuery("update Receipt_product_details set quantity=:quantity,rate=:rate,discount=:discount,"
				      		+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
				      		+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,product_name=:product_name "
				      		+ "where receipt_header_id =:pid and product_id=:proid");
				
					query.setParameter("pid", sup.getReceipt_id());
					query.setParameter("proid", Long.parseLong(information.getProductId()));
					query.setParameter("quantity", Double.parseDouble(information.getQuantity()));
					query.setParameter("rate", Double.parseDouble(information.getRate()));
					query.setParameter("discount", Double.parseDouble(information.getDiscount()));
					query.setParameter("CGST", Double.parseDouble(information.getCGST()));
					query.setParameter("IGST", 	Double.parseDouble(information.getIGST()));
					query.setParameter("SGST", Double.parseDouble(information.getSGST()));
					query.setParameter("SCT", Double.parseDouble(information.getSCT()));
					query.setParameter("labourCharge", Double.parseDouble(information.getLabourCharge()));
					query.setParameter("freight", Double.parseDouble(information.getFreightCharges()));
					query.setParameter("transaction_amount",Double.parseDouble(information.getTransaction_amount()));
					query.setParameter("Others", Double.parseDouble(information.getOthers()));
					query.setParameter("UOM", information.getUnit());
					query.setParameter("HSNCode", information.getHsncode());
					query.setParameter("product_name", information.getProductname());
			      
					try{
						query.executeUpdate();
					}
					catch (Exception e){
						e.printStackTrace();		    	 
					}		
				}		
			}
	}
		else{
			if(sup.getGst_applied()==1){
				Set<Product> products = new HashSet<Product>();
				products = entity.getProducts();
				entity.setProducts(products);
				entity.setTransaction_value(sup.getTransaction_value());
				entity.setCgst(sup.getCgst());
				entity.setSgst(sup.getSgst());
				entity.setIgst(sup.getIgst());
				entity.setState_compansation_tax(sup.getState_compansation_tax());
				entity.setRound_off(sup.getRound_off());
			}
			entity.setUpdate_date(new LocalDateTime());
			session.merge(entity);
			session.flush();
			session.clear();
		}
	}

	@Override
	public Company findCompany(Long user_id) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", user_id));
		User user =(User) criteria.uniqueResult();
		Company company = user.getCompany();
		return company;
	}

	@SuppressWarnings("unchecked")
	@Override	public List<Receipt_product_details> findAllReceiptProductEntityList(Long entryId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Receipt_product_details where receipt_receipt_id=:entryId");
		query.setParameter("entryId", entryId);
		return query.list();
	}

	@Override
	public String deleteReceiptProduct(Long REId,Long receipt_id,Long company_id) {		
		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0 ;
		Double sgst = (double)0;
		Double state_comp_tax = (double)0;
		Double round_off = (double)0;
		/*Double tds=(double)0;*/
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double CGST = (double)0;
		Double IGST = (double)0; 
		Double SGST = (double)0;
		Double state_com_tax = (double)0;
		Double transaction_amount=(double)0;
		Double roundamount=(double)0;
		Double VAT = (double)0;
		Double VATCST = (double)0;
		Double Excise = (double)0;
		
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(Receipt_product_details.class);
		criteria.add(Restrictions.eq("receipt_detail_id", REId));
		Receipt_product_details receiptdetails =(Receipt_product_details) criteria.uniqueResult();
		
		Criteria criteria1 = session.createCriteria(Receipt.class);
		criteria1.add(Restrictions.eq("receipt_id", receipt_id));
		criteria.setFetchMode("bank", FetchMode.JOIN);
		Receipt receipt =(Receipt) criteria1.uniqueResult();
		
		if(receipt.getTransaction_value()!=null) 
		{
		transaction_value= receipt.getTransaction_value();
		}
		if(receipt.getCgst()!=null) 
		{
		cgst=receipt.getCgst();
		}
		if(receipt.getIgst()!=null) 
		{
		igst=receipt.getIgst();
		}
		if(receipt.getSgst()!=null) 
		{
		sgst=receipt.getSgst();
		}
		if(receipt.getState_compansation_tax()!=null) 
		{
		state_comp_tax=receipt.getState_compansation_tax();
		}		
		if(receipt.getAmount()!=null) 
		{
			round_off=receipt.getAmount();
		}
	/*	if(receipt.getTds_amount()!=null) 
		{
			tds=receipt.getTds_amount();
		}*/	
		if(receipt.getTotal_vat()!=null) 
		{
		vat=receipt.getTotal_vat();
		}
		if(receipt.getTotal_vatcst()!=null) 
		{
		vatcst=receipt.getTotal_vatcst();
		}
		if(receipt.getTotal_excise()!=null) 
		{
		excise=receipt.getTotal_excise();
		}
		
		if(receiptdetails.getCGST()!=null)
		{
		CGST=receiptdetails.getCGST();
		}
		
		if(receiptdetails.getIGST()!=null) 
		{
		IGST=receiptdetails.getIGST();
		}
		if(receiptdetails.getSGST()!=null)
		{
		SGST=receiptdetails.getSGST();
		}
		if(receiptdetails.getState_com_tax()!=null)
		{
		state_com_tax=receiptdetails.getState_com_tax();
		}
		if(receiptdetails.getTransaction_amount()!=null)
		{
		transaction_amount=receiptdetails.getTransaction_amount();
		}
		
		if(receiptdetails.getVAT()!=null)
		{
		VAT=receiptdetails.getVAT();
		}
		if(receiptdetails.getVATCST()!=null)
		{
		VATCST=receiptdetails.getVATCST();
		}
		if(receiptdetails.getExcise()!=null) 
		{
		Excise=receiptdetails.getExcise();
		}
		
		if(VAT==0 && VATCST==0 && Excise==0)
		{
		
		cgst=cgst-CGST;
		igst=igst-IGST;
		sgst=sgst-SGST;
		state_comp_tax= state_comp_tax-state_com_tax;
		transaction_value=transaction_value-transaction_amount;
		roundamount=CGST+IGST+SGST+state_com_tax+transaction_amount;
		
		round_off=round_off-roundamount;
		receipt.setCgst(cgst);
		receipt.setIgst(igst);
		receipt.setSgst(sgst);
		receipt.setState_compansation_tax(state_comp_tax);
		receipt.setTransaction_value(transaction_value);
		receipt.setRound_off(round_off);
		receipt.setAmount(round_off);
		
		}
		else
		{
			/*vat=vat-VAT;
			vatcst=vatcst-VATCST;
			excise=excise-Excise;
			//calculate transaction and round off value,
			transaction_value=transaction_value-transaction_amount;
			roundamount=VAT+VATCST+Excise+transaction_amount;
			round_off=round_off-roundamount;
			
			receipt.setTotal_vat(vat);
			receipt.setTotal_vatcst(vatcst);
			receipt.setTotal_excise(excise);
			receipt.setTransaction_value(transaction_value);
			receipt.setRound_off(round_off);
			receipt.setAmount(round_off);*/
		}
		
		
		
			if ((receipt.getCustomer() != null)) {
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,
						receipt.getCustomer().getCustomer_id(), (long) 5, (double)(roundamount), (double)0, (long) 0);
				
				
			}  
	
			if(receipt.getGst_applied()!=null && receipt.getPayment_type()!=null)
			{
			if (receipt.getGst_applied()==1)
			 {	
		    try {
			 if(receipt.getPayment_type()==1)
			 {
				    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",company_id);
	        	 
		 			if(subledgercash!=null)
		 			{
		 				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)transaction_amount,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
		 				
		 			}
			 }
			 else
			 {
				 if(receipt.getBank()!=null)
				 {
				 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,receipt.getBank().getBank_id(),(long) 3,(double)0,(double)transaction_amount,(long) 0);
					
				 }
				 
			 }	 
			 
			 if(CGST!=null && CGST>0)
				{
					SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",company_id);
					
					if(subledgercgst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)CGST,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(SGST!=null && SGST>0)
				{
					SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",company_id);
					
					if(subledgersgst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)SGST,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(IGST!=null && IGST>0)
				{
					SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",company_id);
					
					if(subledgerigst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)IGST,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(state_com_tax!=null && state_com_tax>0)
				{
					SubLedger subledgercess = subledgerDAO.findOne("Output CESS",company_id);
					
					if(subledgercess!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_com_tax,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}	
				}
				if(VAT!=null && VAT>0)
				{
					SubLedger subledgervat = subledgerDAO.findOne("Output VAT",company_id);
					
					if(subledgervat!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)VAT,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(VATCST!=null && VATCST>0)
				{
					SubLedger subledgercst = subledgerDAO.findOne("Output CST",company_id);
					
					if(subledgercst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)VATCST,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(Excise!=null && Excise>0)
				{
					SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",company_id);
					
					if(subledgerexcise!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),company_id,subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)Excise,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
	 }
	 catch (Exception e) {
			e.printStackTrace();
		}
 }
			
	}
		
		
		Query query = session.createQuery("delete from Receipt_product_details where receipt_detail_id =:REId");
		query.setParameter("REId", REId);
		String msg="";
		try{
			query.executeUpdate();
			session.update(receipt);
			msg= "Product Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Product";			
		}
		return msg;	
	}

	@Override
	public Receipt isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CopyOnWriteArrayList<Receipt>  getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		
		CopyOnWriteArrayList<Receipt> list = new CopyOnWriteArrayList<>();
		/*Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", comId));
		criteria.setFetchMode("customer",  FetchMode.JOIN);
		criteria.setFetchMode("subLedger",  FetchMode.JOIN);
		criteria.setFetchMode("sales_bill_id",  FetchMode.JOIN);
		list1 =  criteria.list();*/
		List<Receipt> list1 =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.sales_bill_id sales_bill_id "
				+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date >=:from_date and receipt.date <=:to_date and receipt.entry_status !=:entry_status ORDER by receipt.date DESC,receipt.receipt_id DESC");
		query.setParameter("company_id", comId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
			  list1.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		 
		  
		
		for (Receipt rec : list1)
		{
			list.add(rec);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Receipt> findAllRecepitOfCompany(Long company_id,Boolean flag) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
				+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag  ORDER by receipt.date DESC, receipt.receipt_id DESC");
	
//		receipt.accountingYear.year_id=:yearID  //query.setParameter("yearID",yearID);
		query.setParameter("company_id", company_id);
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<Receipt> findAllRecepitOfCompany(Long
	 * company_id,Boolean flag,Long yearID) { List<Receipt> list = new
	 * ArrayList<Receipt>(); Session session = getCurrentSession(); Query query =
	 * session.
	 * createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
	 * +"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.accountingYear.year_id=:yearID ORDER by receipt.date DESC, receipt.receipt_id DESC"
	 * ); query.setParameter("company_id", company_id);
	 * query.setParameter("yearID",yearID); query.setParameter("flag", flag);
	 * ScrollableResults scrollableResults =
	 * query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY); while
	 * (scrollableResults.next()) { list.add((Receipt)scrollableResults.get()[0]);
	 * session.evict(scrollableResults.get()[0]); } session.clear(); return list; }
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntry> findAllSalesEntryWithDate(LocalDate from_date, LocalDate to_date, Long company_id) {
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", company_id));
		return criteria.list();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double state_comp_tax = (double)0;
		Double amount= (double)0;
		Double tds= (double)0;
        Session session = getCurrentSession();
        Criteria criteria1 = session.createCriteria(Receipt.class);
		criteria1.add(Restrictions.eq("receipt_id", entityId));
		criteria1.setFetchMode("bank", FetchMode.JOIN);
		criteria1.setFetchMode("customer", FetchMode.JOIN);
		criteria1.setFetchMode("subLedger", FetchMode.JOIN);
		criteria1.setFetchMode("company", FetchMode.JOIN);
		criteria1.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria1.setFetchMode("advreceipt", FetchMode.JOIN);

		Receipt receipt =(Receipt) criteria1.uniqueResult();
		int status= MyAbstractController.ENTRY_CANCEL;
		if(receipt.getAmount()!=null)
		{
		amount=receipt.getAmount();
		}
		
		if(receipt.getTransaction_value()!=null && receipt.getTransaction_value()>0)
		{
		transaction_value= receipt.getTransaction_value();
		}
	
		if(receipt.getCgst()!=null && receipt.getCgst()>0)
		{
			cgst=receipt.getCgst();
		}
		if(receipt.getIgst()!=null && receipt.getIgst()>0)
		{
			igst=receipt.getIgst();
		}
		if(receipt.getSgst()!=null && receipt.getSgst()>0)
		{
			sgst=receipt.getSgst();
		}
		if(receipt.getState_compansation_tax()!=null && receipt.getState_compansation_tax()>0)
		{
			state_comp_tax=receipt.getState_compansation_tax();
		}
		if(receipt.getTotal_vat()!=null && receipt.getTotal_vat()>0)
		{
			vat=receipt.getTotal_vat();
		}
		if(receipt.getTotal_vatcst()!=null && receipt.getTotal_vatcst()>0 )
		{
			vatcst=receipt.getTotal_vatcst();
		}
		if(receipt.getTotal_excise()!=null && receipt.getTotal_excise()>0)
		{
			excise=receipt.getTotal_excise();
		}
		if(receipt.getTds_amount()!=null)
		{
			tds=receipt.getTds_amount();
		}
		
		if(receipt.getTds_paid()!=null && receipt.getTds_paid()==true)
		{
			SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", receipt.getCompany().getCompany_id());
			if (subledgerTds != null && receipt.getTds_amount()!=null) {
				subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tds, (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
			}
		}
		try {

			if(receipt.getCustomer() != null){
				
				if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true) {
				customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5,( (double)amount+(double)tds ),(double)0,(long) 0);
			    }
				else
				{
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5,( (double)amount),(double)0,(long) 0);
				}
			}
		 if(receipt.getSubLedger() != null){
			 
			 subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getSubLedger().getSubledger_Id(),(long) 2,((double)amount + (double)receipt.getTds_amount()),(double)0,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
		 }
		 
		 if(receipt.getGst_applied()!=null && receipt.getPayment_type()!=null)
		 {
		 if(receipt.getGst_applied()==2)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
					if(subledgercash!=null)
					{
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)amount,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
			
					}
			 }
			 else
			 {
				 if(receipt.getBank()!=null)
				 {
				 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)amount,(long) 0);
				 }
			 }
		 }
		 else if(receipt.getGst_applied()==1)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
						
						if(subledgercash!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)(transaction_value),(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}	
						
					
			     }
		
			 }
			 else
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 if(receipt.getBank()!=null)
					 {
					 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)(transaction_value),(long) 0);
					 }
					
				 }
				
			 }	 
			 
			 if(cgst!=null && cgst>0)
				{
					SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",receipt.getCompany().getCompany_id());
					
					if(subledgercgst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if(sgst!=null && sgst>0)
				{
					SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",receipt.getCompany().getCompany_id());
					
					if(subledgersgst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if(igst!=null && igst>0)
				{
					SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",receipt.getCompany().getCompany_id());
					
					if(subledgerigst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(state_comp_tax!=null && state_comp_tax>0)
				{
					SubLedger subledgercess = subledgerDAO.findOne("Output CESS",receipt.getCompany().getCompany_id());
					
					if(subledgercess!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(vat!=null && vat>0)
				{
					SubLedger subledgervat = subledgerDAO.findOne("Output VAT",receipt.getCompany().getCompany_id());
					
					if(subledgervat!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(vatcst!=null && vatcst>0)
				{
					SubLedger subledgercst = subledgerDAO.findOne("Output CST",receipt.getCompany().getCompany_id());
					
					if(subledgercst!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
				if(excise!=null && excise>0)
				{
					SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",receipt.getCompany().getCompany_id());
					
					if(subledgerexcise!=null)
					{
						subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						
					}
				}
		 }
		 
		}
	 }
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(receipt.getCustomer()!=null)
		{
			
				if(receipt.getSales_bill_id()!=null)
				{
					//receipt.getSales_bill_id().getSales_id()
					Query querysales = session.createQuery("update SalesEntry set entry_status=:entry_status where sales_id=:sales_id");
					querysales.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
					querysales.setParameter("sales_id", receipt.getSales_bill_id().getSales_id());
					querysales.executeUpdate();
				}
		}
		if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true)
		{
			if(receipt.getAdvreceipt()!=null)
			{
				Criteria criteria = session.createCriteria(Receipt.class);
				criteria.add(Restrictions.eq("receipt_id", receipt.getAdvreceipt().getReceipt_id()));
				criteria.setFetchMode("bank", FetchMode.JOIN);
				criteria.setFetchMode("customer", FetchMode.JOIN);
				criteria.setFetchMode("subLedger", FetchMode.JOIN);
				criteria.setFetchMode("company", FetchMode.JOIN);
				criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
				criteria.setFetchMode("advreceipt", FetchMode.JOIN);

				Receipt advreceipt =(Receipt) criteria.uniqueResult();				
				if(advreceipt!=null)
				{
					if(advreceipt.getSales_bill_id()==null)
					{
						if(receipt.getAmount()>advreceipt.getRound_off())
						{
							status= MyAbstractController.ENTRY_REVERT;
						}
						else
						{
							advreceipt.setAmount(advreceipt.getAmount()+receipt.getAmount());
							advreceipt.setTransaction_value(advreceipt.getTransaction_value()+receipt.getTransaction_value());
							advreceipt.setTds_amount(advreceipt.getTds_amount()+receipt.getTds_amount());						//add aamount
							//advreceipt
							session.update(advreceipt);
						}
					}
					else
					{
						//change status
						status= MyAbstractController.ENTRY_REVERT;
					}
				}
				else
				{
					status= MyAbstractController.ENTRY_REVERT;
				}
			}
			
		}
		if(status==4)
		{
			Query query = session.createQuery("update Receipt set entry_status=:entry_status,sales_bill_id= null where receipt_id =:id");
			query.setParameter("receipt_id", entityId);
			query.setParameter("entry_status",status);
			query.executeUpdate();
		}
		else
		{
			Query query1 = session.createSQLQuery("delete from receipt_product where receipt_receipt_id =:receipt_id");
			query1.setParameter("receipt_id", entityId);
			query1.executeUpdate();
		}
		
		balanceDao.deleteOpeningBalance(null,entityId, null, null, null, null,null,null, null,null,null,null);
		Query query = session.createQuery("delete from Receipt where receipt_id =:id");
		query.setParameter("id", entityId);
		String msg="";
		try{
			query.executeUpdate();
			msg= "Receipt entry deleted successfully";
			
		}
		catch(Exception e){
			msg= "You can't delete Receipt Entry";			
		}
		return msg;
	}

	@Override
	public List<Receipt> getAdvanceReceiptList(List<Long> receiptIds, Long customerId, Long companyId, Long yid) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		Criterion criterion1 = Restrictions.eq("advance_payment", true);
		Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);
		criteria.add(Restrictions.le("accountingYear.year_id",yid));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_PENDING));
		/*if(receiptIds.size() > 0){
			Criterion criterion4 = Restrictions.not(Restrictions.in("receipt_id", receiptIds));
			criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4));
		}*/
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void updateAdvanceReceipt(Receipt receipt) {
		Session session = getCurrentSession();
		session.merge(receipt);
		session.flush();
		session.clear();
	}

	@Override
	public List<Receipt> getIncomeByYearId(Long yearId, Long companyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("advance_payment", false));
		criteria.add(Restrictions.eq("company.company_id",companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id",yearId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt "
				+"WHERE receipt.accountingYear.year_id =:year_id and receipt.advance_payment =:advance_payment and receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.entry_status !=:entry_status");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("advance_payment", false);
		query.setParameter("year_id", yearId);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Receipt> getATList(LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.add(Restrictions.eq("company.company_id",companyId));
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.addOrder(Order.asc("date"));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public List<Receipt> getATAdjList(LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.add(Restrictions.eq("company.company_id",companyId));
		criteria.add(Restrictions.ge("date", from_date.minusDays(31))); 
		criteria.add(Restrictions.le("date", from_date.minusDays(1)));
		criteria.addOrder(Order.asc("date"));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public void updateReceipt(Receipt sup, Receipt entity) {		
	    
	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		/*Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("accountingYear.year_id", yearId));
		criteria.setProjection(Projections.rowCount());
		return Integer.parseInt(criteria.uniqueResult().toString());*/
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select count(receipt_id)from receipt where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term",  range + "%");
		
		if(query.list()==null || query.list().isEmpty())
		{
			return 0;
		}
		else
		{
			
			if(query.list().get(0)==null)
				return 0;
			else 
			{
			Integer vid=((BigInteger) query.list().get(0)).intValue();
			
			return vid;
			}
		}
	}

	@Override
	public Receipt findOneWithAll(Long rId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("receipt_id", rId));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("bank", FetchMode.JOIN);
		return (Receipt) criteria.uniqueResult();
	}

	@Override
	public Long saveReceiptThroughExcel(Receipt receipt) {
		Session session = getCurrentSession();
		Long id=(Long) session.save(receipt);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public void saveReceipt_product_detailsThroughExcel(Receipt_product_details entity) {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}

	@Override
	public void updateReceiptThroughExcel(Receipt receipt) {
		Session session = getCurrentSession();
		session.merge(receipt);
		session.flush();
		session.clear();
		
	}

	@Override
	public void updateReceipt_product_detailsThroughExcel(Receipt_product_details entity) {
		Session session = getCurrentSession();
		session.save(entity);
		session.flush();
		session.clear();
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Receipt> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
/*	Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
	criteria.add(Restrictions.ge("date", from_date));
	criteria.add(Restrictions.le("date", to_date));
	criteria.add(Restrictions.eq("company.company_id", companyId));
	criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
	criteria.add(Restrictions.eq("flag", true));
	criteria.addOrder(Order.desc("date"));
	criteria.setFetchMode("customer", FetchMode.JOIN);
	criteria.setFetchMode("subLedger", FetchMode.JOIN);
	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	return criteria.list();*/
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt "
				+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date >=:from_date and receipt.date <=:to_date and receipt.entry_status !=:entry_status order by receipt.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Receipt> findAllRecepitsOfCompany(Long company_id) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
				+"WHERE receipt.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", company_id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  
		  Collections.sort(list, new Comparator<Receipt>() {
	            public int compare(Receipt rec1, Receipt rec2) {
	                Long receipt_id1 = new Long(rec1.getReceipt_id());
	                Long receipt_id2 = new Long(rec2.getReceipt_id());
	                return receipt_id2.compareTo(receipt_id1);
	            }
	        });
		  return list;
		
	}

	@Override
	public List<Receipt> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Integer type) {
      /*  Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		
        criteria.add(Restrictions.ge("date", from_date));
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt "
				+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date >=:from_date and receipt.date <=:to_date and receipt.entry_status !=:entry_status order by receipt.date desc");
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		 
	}

	@Override
	public int getCountByDate(Long companyId, String range, LocalDate date) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("date", date));
		criteria.addOrder(Order.desc("receipt_id"));
		criteria.setMaxResults(1);
		Receipt entry = (Receipt) criteria.uniqueResult();
		Integer i= null;
		if(entry!=null)
		{
			
			String vocher_no = entry.getVoucher_no().trim();
	    	String[] vochers = vocher_no.split("/");
			String vocher = vochers[vochers.length-1];
			i = Integer.parseInt(vocher.trim());
		}
		else
		{
			i=0;
			
		}
		return i;
		
	/*	Query query = session.createSQLQuery("select count(receipt_id)from receipt where company_id =:company_id and date ='"+date+"'");
		query.setParameter("company_id", companyId);
		
		if(query.list()==null || query.list().isEmpty())
		{
			return 0;
		}
		else
		{
			System.out.println(query.list().get(0));
			if(query.list().get(0)==null)
				return 0;
			else 
			{
			Integer vid=((BigInteger) query.list().get(0)).intValue();
			System.out.println(vid);
			return vid;
			}
		}*/
	}

	@Override
	public Receipt_product_details editproductdetailforReceipt(Long entryId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt_product_details.class);
		criteria.add(Restrictions.eq("receipt_detail_id", entryId));
		
		return (Receipt_product_details) criteria.uniqueResult();
	}

	@Override
	public void updateReceipt(Receipt entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();
	}

	@Override
	public void updateReceipt_product_details(Receipt_product_details entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();
	}

	@Override
	public Double findpaidtds(Long sales_id) {
		Session session = getCurrentSession();
		Double tds=0.0;
		Query query = session.createSQLQuery("select ROUND(SUM(tds_amount), 2) as tds_amount from receipt where sales_bill_id =:sales_bill_id and ((entry_status = 0) || (entry_status = 1) || (entry_status IS NULL))");
		query.setParameter("sales_bill_id", sales_id);
		if(query.list()==null || query.list().isEmpty())
		{
			tds=(double)0;
		}
		else
		{
			if(query.list().get(0)==null || query.list().isEmpty())
				tds=(double)0;
			else
				tds= Double.parseDouble(query.list().get(0).toString());	
		}
		return tds;
	}
	@Override
	public void diactivateByIdValue(Long entityId,Boolean isDelete) {		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double state_comp_tax = (double)0;
		Double amount= (double)0;
		
        Session session = getCurrentSession();
        Criteria criteria1 = session.createCriteria(Receipt.class);
		criteria1.add(Restrictions.eq("receipt_id", entityId));
		criteria1.setFetchMode("bank", FetchMode.JOIN);
		criteria1.setFetchMode("customer", FetchMode.JOIN);
		criteria1.setFetchMode("subLedger", FetchMode.JOIN);
		criteria1.setFetchMode("company", FetchMode.JOIN);
		criteria1.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria1.setFetchMode("advreceipt", FetchMode.JOIN);

		Receipt receipt =(Receipt) criteria1.uniqueResult();
		int status= MyAbstractController.ENTRY_CANCEL;
		if(receipt.getAmount()!=null)
		{
		amount=receipt.getAmount();
		}
		
		if(receipt.getTransaction_value()!=null && receipt.getTransaction_value()>0)
		{
		transaction_value= receipt.getTransaction_value();
		}
	
		if(receipt.getCgst()!=null && receipt.getCgst()>0)
		{
			cgst=receipt.getCgst();
		}
		if(receipt.getIgst()!=null && receipt.getIgst()>0)
		{
			igst=receipt.getIgst();
		}
		if(receipt.getSgst()!=null && receipt.getSgst()>0)
		{
			sgst=receipt.getSgst();
		}
		if(receipt.getState_compansation_tax()!=null && receipt.getState_compansation_tax()>0)
		{
			state_comp_tax=receipt.getState_compansation_tax();
		}
		if(receipt.getTotal_vat()!=null && receipt.getTotal_vat()>0)
		{
			vat=receipt.getTotal_vat();
		}
		if(receipt.getTotal_vatcst()!=null && receipt.getTotal_vatcst()>0 )
		{
			vatcst=receipt.getTotal_vatcst();
		}
		if(receipt.getTotal_excise()!=null && receipt.getTotal_excise()>0)
		{
			excise=receipt.getTotal_excise();
		}
		
		try {
			if(receipt.getAdvreceipt()!=null)
			{
				
			}
			else
			{
				if(receipt.getCustomer() != null){
					
					if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true) {
					customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5, ((double)amount + (double)receipt.getTds_amount()),(double)0,(long) 0);
				}
					else
					{
						customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5, ((double)amount),(double)0,(long) 0);
					}
				}
			}
			
		 if(receipt.getSubLedger() != null){
			 
			 subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getSubLedger().getSubledger_Id(),(long) 2,((double)amount + (double)receipt.getTds_amount()),(double)0,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
		 }
		 
		 if(receipt.getTds_paid()!=null && receipt.getTds_paid()==true)
			{
				SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", receipt.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)receipt.getTds_amount(), (long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
				}
			}
		
		if(receipt.getGst_applied()!=null && receipt.getPayment_type()!=null)
		{
		 if(receipt.getGst_applied()==2)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
					if(subledgercash!=null)
					{
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)amount,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
			
					}
			 }
			 else
			 {
				 if(receipt.getBank()!=null)
				 {
				 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)amount,(long) 0);
				 }
			 }
		 }
		 
		 else if(receipt.getGst_applied()==1)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
						
						if(subledgercash!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)transaction_value,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}	
						
					
			     }
					if(cgst!=null && cgst>0)
					{
						SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",receipt.getCompany().getCompany_id());
						
						if(subledgercgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(sgst!=null && sgst>0)
					{
						SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",receipt.getCompany().getCompany_id());
						
						if(subledgersgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(igst!=null && igst>0)
					{
						SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",receipt.getCompany().getCompany_id());
						
						if(subledgerigst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(state_comp_tax!=null && state_comp_tax>0)
					{
						SubLedger subledgercess = subledgerDAO.findOne("Output CESS",receipt.getCompany().getCompany_id());
						
						if(subledgercess!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(vat!=null && vat>0)
					{
						SubLedger subledgervat = subledgerDAO.findOne("Output VAT",receipt.getCompany().getCompany_id());
						
						if(subledgervat!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(vatcst!=null && vatcst>0)
					{
						SubLedger subledgercst = subledgerDAO.findOne("Output CST",receipt.getCompany().getCompany_id());
						
						if(subledgercst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(excise!=null && excise>0)
					{
						SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",receipt.getCompany().getCompany_id());
						
						if(subledgerexcise!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					
			 }
			 else
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 if(receipt.getBank()!=null)
					 {
					 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)transaction_value,(long) 0);
					 }
					
				 }
				 if(cgst!=null && cgst>0)
					{
						SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",receipt.getCompany().getCompany_id());
						
						if(subledgercgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(sgst!=null && sgst>0)
					{
						SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",receipt.getCompany().getCompany_id());
						
						if(subledgersgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(igst!=null && igst>0)
					{
						SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",receipt.getCompany().getCompany_id());
						
						if(subledgerigst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(state_comp_tax!=null && state_comp_tax>0)
					{
						SubLedger subledgercess = subledgerDAO.findOne("Output CESS",receipt.getCompany().getCompany_id());
						
						if(subledgercess!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(vat!=null && vat>0)
					{
						SubLedger subledgervat = subledgerDAO.findOne("Output VAT",receipt.getCompany().getCompany_id());
						
						if(subledgervat!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(vatcst!=null && vatcst>0)
					{
						SubLedger subledgercst = subledgerDAO.findOne("Output CST",receipt.getCompany().getCompany_id());
						
						if(subledgercst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(excise!=null && excise>0)
					{
						SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",receipt.getCompany().getCompany_id());
						
						if(subledgerexcise!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 0,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
			 }	 
		 }
		 
		 
		}
		
			if(receipt.getAdvance_payment()==true)
				{
					if(receipt.getAdvreceipt()!=null)
					{
						Criteria criteria = session.createCriteria(Receipt.class);
						criteria.add(Restrictions.eq("receipt_id", receipt.getAdvreceipt().getReceipt_id()));
						criteria.setFetchMode("bank", FetchMode.JOIN);
						criteria.setFetchMode("customer", FetchMode.JOIN);
						criteria.setFetchMode("subLedger", FetchMode.JOIN);
						criteria.setFetchMode("company", FetchMode.JOIN);
						criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
						criteria.setFetchMode("advreceipt", FetchMode.JOIN);

						Receipt advreceipt =(Receipt) criteria.uniqueResult();				
						if(advreceipt!=null)
						{
							if(advreceipt.getSales_bill_id()==null)
							{
								if(receipt.getAmount()>advreceipt.getRound_off())
								{
									status= MyAbstractController.ENTRY_REVERT;
								}
								else
								{
									advreceipt.setAmount(advreceipt.getAmount()+receipt.getAmount());
									advreceipt.setTransaction_value(advreceipt.getTransaction_value()+receipt.getTransaction_value());
									advreceipt.setTds_amount(advreceipt.getTds_amount()+receipt.getTds_amount());						//add aamount
									//advreceipt
									session.update(advreceipt);
								}
							}
							else
							{
								//change status
								status= MyAbstractController.ENTRY_REVERT;
							}
						}
						else
						{
							status= MyAbstractController.ENTRY_REVERT;
						}
					}
					
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(isDelete)
		{
			balanceDao.deleteOpeningBalance(null,entityId, null, null, null, null,null,null, null,null,null,null);
			
			Query query = session.createQuery("delete from Receipt where receipt_id =:id");
			query.setParameter("id", entityId);
			query.executeUpdate();
			
		
		}
		else
		{
			Query query = session.createQuery("update Receipt set entry_status=:entry_status where receipt_id =:id");
			query.setParameter("id", entityId);
			query.setParameter("entry_status",status);
			query.executeUpdate();
		}

	}

	@Override
	public List<Receipt> findallreceiptentryofsales(long id) {
		// TODO Auto-generated method stub
		Session session = getCurrentSession();
		Query query = session.createQuery("from Receipt where sales_bill_id.sales_id =:sales_id");
		query.setParameter("sales_id", id);
				
		return query.list();			
	}
	@Override
	public void activateByIdValue(Long entityId) {		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double state_comp_tax = (double)0;
		Double amount= (double)0;
		
        Session session = getCurrentSession();
        Criteria criteria1 = session.createCriteria(Receipt.class);
		criteria1.add(Restrictions.eq("receipt_id", entityId));
		criteria1.setFetchMode("bank", FetchMode.JOIN);
		criteria1.setFetchMode("customer", FetchMode.JOIN);
		criteria1.setFetchMode("subLedger", FetchMode.JOIN);
		criteria1.setFetchMode("company", FetchMode.JOIN);
		criteria1.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria1.setFetchMode("advreceipt", FetchMode.JOIN);
		Receipt receipt =(Receipt) criteria1.uniqueResult();
		int status= MyAbstractController.ENTRY_PENDING;
		if(receipt.getAmount()!=null)
		{
		amount=receipt.getAmount();
		}
		
		if(receipt.getTransaction_value()!=null && receipt.getTransaction_value()>0)
		{
		transaction_value= receipt.getTransaction_value();
		}
	
		if(receipt.getCgst()!=null && receipt.getCgst()>0)
		{
			cgst=receipt.getCgst();
		}
		if(receipt.getIgst()!=null && receipt.getIgst()>0)
		{
			igst=receipt.getIgst();
		}
		if(receipt.getSgst()!=null && receipt.getSgst()>0)
		{
			sgst=receipt.getSgst();
		}
		if(receipt.getState_compansation_tax()!=null && receipt.getState_compansation_tax()>0)
		{
			state_comp_tax=receipt.getState_compansation_tax();
		}
		if(receipt.getTotal_vat()!=null && receipt.getTotal_vat()>0)
		{
			vat=receipt.getTotal_vat();
		}
		if(receipt.getTotal_vatcst()!=null && receipt.getTotal_vatcst()>0 )
		{
			vatcst=receipt.getTotal_vatcst();
		}
		if(receipt.getTotal_excise()!=null && receipt.getTotal_excise()>0)
		{
			excise=receipt.getTotal_excise();
		}
		
		try {
			if(receipt.getAdvreceipt()!=null)
			{
				
			}
			else
			{
					if(receipt.getCustomer() != null){
						
						if(receipt.getAdvance_payment()!=null && receipt.getAdvance_payment()==true) {
						customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5, ((double)amount + (double)receipt.getTds_amount()),(double)0,(long) 1);
					}
						else
						{
							customerService.addcustomertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(), receipt.getCustomer().getCustomer_id(), (long) 5, ((double)amount),(double)0,(long) 1);
						}
					}
					
			}
		 if(receipt.getSubLedger() != null){
			 
			 subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getSubLedger().getSubledger_Id(),(long) 2,((double)amount + (double)receipt.getTds_amount()),(double)0,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
		 }

		 if(receipt.getTds_paid()!=null && receipt.getTds_paid()==true)
			{
				SubLedger subledgerTds = subledgerDAO.findOne("TDS Receivable", receipt.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)receipt.getTds_amount(), (long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
				}
			}

			
		if(receipt.getGst_applied()!=null && receipt.getPayment_type()!=null)	
		{
		 if(receipt.getGst_applied()==2)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				    SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
					if(subledgercash!=null)
					{
					subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)amount,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
			
					}
			 }
			 else
			 {
				 if(receipt.getBank()!=null)
				 {
				 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)amount,(long) 1);
				 }
			 }
		 }
		 else if(receipt.getGst_applied()==1)
		 {
			 if(receipt.getPayment_type()==1)
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 SubLedger subledgercash = subledgerDAO.findOne("Cash In Hand",receipt.getCompany().getCompany_id());
						
						if(subledgercash!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercash.getSubledger_Id(),(long) 2,(double)0,(double)transaction_value,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}	
						
					
			     }
					if(cgst!=null && cgst>0)
					{
						SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",receipt.getCompany().getCompany_id());
						
						if(subledgercgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(sgst!=null && sgst>0)
					{
						SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",receipt.getCompany().getCompany_id());
						
						if(subledgersgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(igst!=null && igst>0)
					{
						SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",receipt.getCompany().getCompany_id());
						
						if(subledgerigst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(state_comp_tax!=null && state_comp_tax>0)
					{
						SubLedger subledgercess = subledgerDAO.findOne("Output CESS",receipt.getCompany().getCompany_id());
						
						if(subledgercess!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(vat!=null && vat>0)
					{
						SubLedger subledgervat = subledgerDAO.findOne("Output VAT",receipt.getCompany().getCompany_id());
						
						if(subledgervat!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(vatcst!=null && vatcst>0)
					{
						SubLedger subledgercst = subledgerDAO.findOne("Output CST",receipt.getCompany().getCompany_id());
						
						if(subledgercst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(excise!=null && excise>0)
					{
						SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",receipt.getCompany().getCompany_id());
						
						if(subledgerexcise!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					
			 }
			 else
			 {
				 if(transaction_value!=null && transaction_value>0)
				 {
					 if(receipt.getBank()!=null)
					 {
					 bankService.addbanktransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),receipt.getBank().getBank_id(),(long) 3,(double)0,(double)transaction_value,(long) 1);
					 }
					
				 }
				 if(cgst!=null && cgst>0)
					{
						SubLedger subledgercgst = subledgerDAO.findOne("Output CGST",receipt.getCompany().getCompany_id());
						
						if(subledgercgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercgst.getSubledger_Id(),(long) 2,(double)0,(double)cgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(sgst!=null && sgst>0)
					{
						SubLedger subledgersgst = subledgerDAO.findOne("Output SGST",receipt.getCompany().getCompany_id());
						
						if(subledgersgst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgersgst.getSubledger_Id(),(long) 2,(double)0,(double)sgst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					if(igst!=null && igst>0)
					{
						SubLedger subledgerigst = subledgerDAO.findOne("Output IGST",receipt.getCompany().getCompany_id());
						
						if(subledgerigst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerigst.getSubledger_Id(),(long) 2,(double)0,(double)igst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(state_comp_tax!=null && state_comp_tax>0)
					{
						SubLedger subledgercess = subledgerDAO.findOne("Output CESS",receipt.getCompany().getCompany_id());
						
						if(subledgercess!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercess.getSubledger_Id(),(long) 2,(double)0,(double)state_comp_tax,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
						}
					}
					
					if(vat!=null && vat>0)
					{
						SubLedger subledgervat = subledgerDAO.findOne("Output VAT",receipt.getCompany().getCompany_id());
						
						if(subledgervat!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgervat.getSubledger_Id(),(long) 2,(double)0,(double)vat,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(vatcst!=null && vatcst>0)
					{
						SubLedger subledgercst = subledgerDAO.findOne("Output CST",receipt.getCompany().getCompany_id());
						
						if(subledgercst!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgercst.getSubledger_Id(),(long) 2,(double)0,(double)vatcst,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
					if(excise!=null && excise>0)
					{
						SubLedger subledgerexcise = subledgerDAO.findOne("Output EXCISE",receipt.getCompany().getCompany_id());
						
						if(subledgerexcise!=null)
						{
							subledgerService.addsubledgertransactionbalance(receipt.getAccountingYear().getYear_id(),receipt.getDate(),receipt.getCompany().getCompany_id(),subledgerexcise.getSubledger_Id(),(long) 2,(double)0,(double)excise,(long) 1,null,receipt,null,null,null,null,null,null,null,null,null,null);
							
						}
					}
			 }	 
		 }
		 
		}
		if(receipt.getAdvance_payment()==true)
		{
			if(receipt.getAdvreceipt()!=null)
			{
				Criteria criteria = session.createCriteria(Receipt.class);
				criteria.add(Restrictions.eq("receipt_id", receipt.getAdvreceipt().getReceipt_id()));
				criteria.setFetchMode("bank", FetchMode.JOIN);
				criteria.setFetchMode("customer", FetchMode.JOIN);
				criteria.setFetchMode("subLedger", FetchMode.JOIN);
				criteria.setFetchMode("company", FetchMode.JOIN);
				criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
				criteria.setFetchMode("advreceipt", FetchMode.JOIN);
				Receipt advreceipt =(Receipt) criteria.uniqueResult();				
				if(advreceipt!=null)
				{
					if(advreceipt.getSales_bill_id()==null)
					{
						if(receipt.getAmount()>advreceipt.getRound_off())
						{
							status= MyAbstractController.ENTRY_PENDING;
						}
						else
						{
							advreceipt.setAmount(advreceipt.getAmount()-receipt.getAmount());
							advreceipt.setTransaction_value(advreceipt.getTransaction_value()-receipt.getTransaction_value());
							advreceipt.setTds_amount(advreceipt.getTds_amount()-receipt.getTds_amount());						//add aamount
							//advreceipt
							session.update(advreceipt);
						}
					}
					else
					{
						//change status
						status= MyAbstractController.ENTRY_PENDING;
					}
				}
				else
				{
					status= MyAbstractController.ENTRY_PENDING;
				}
			}
			
		}
	 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Query query = session.createQuery("update Receipt set entry_status=:entry_status where receipt_id =:id");
		query.setParameter("id", entityId);
		query.setParameter("entry_status",status);
		query.executeUpdate();

	}

	@Override
	public void changeStatusOfReceiptThroughExcel(Receipt receipt) {
		Session session = getCurrentSession();
		receipt.setFlag(false);
		session.merge(receipt);
		session.flush();
		session.clear();
		
	}

	/*@Override
	public List<Receipt> getAdvanceReceiptListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		List<Receipt> list = new ArrayList<>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.sales_bill_id billno "
				+ "WHERE receipt.advance_payment=:advance_payment and receipt.flag=:flag and receipt.company.company_id =:company_id and receipt.entry_status !=:entry_status and receipt.date >=:date1 and receipt.date <=:date2 and receipt.sales_bill_id.sales_id IS NULL order by receipt.date ASC,receipt.receipt_id ASC");
		query.setParameter("advance_payment", true);
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		query.setParameter("date1", from_date);
		query.setParameter("date2", to_date);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.ge("date", from_date)); 
		criteria.add(Restrictions.le("date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("advance_payment", true));
		criteria.setFetchMode("sales_bill_id", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
*/
	@Override
	public List<Receipt> getReceiptListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query =null;
		if(bank_id.equals((long)-4))
		{
		query = session.createQuery("SELECT receipt from Receipt receipt "
				+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date>=:from_date and receipt.date<=:to_date and receipt.entry_status !=:entry_status and receipt.bank.bank_id IS NOT NULL order by receipt.date ASC,receipt.receipt_id ASC");
		}
		else
		{
  		query = session.createQuery("SELECT receipt from Receipt receipt "
					+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date>=:from_date and receipt.date<=:to_date and receipt.entry_status !=:entry_status and receipt.bank.bank_id=:bank_id order by receipt.date ASC,receipt.receipt_id ASC");
		query.setParameter("bank_id",bank_id);
     	}
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Receipt> CashReceiptOfMoreThanRS10000AndAbove(LocalDate from_date,LocalDate to_date,Long companyId) {

		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt "
				+"WHERE receipt.flag =:flag and receipt.entry_status !=:entry_status and receipt.company.company_id =:company_id and amount+tds_amount>=10000 and payment_type=1 and date>=:from_date and date<=:to_date");
		query.setParameter("company_id", companyId);
		query.setParameter("from_date", from_date);
		query.setParameter("to_date", to_date);
		query.setParameter("flag", true); 
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Receipt> customerReceiptHavingUnadjustedUnadjusted(LocalDate from_date, LocalDate to_date,Long companyId) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
				+"WHERE receipt.company.company_id =:company_id and receipt.date<:from_date "
				+"and receipt.sales_bill_id.sales_id IS NULL and receipt.entry_status =:entry_status and receipt.flag =:flag and receipt.advance_payment=:advance_payment");
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true); 
		query.setParameter("from_date", from_date);
		query.setParameter("advance_payment", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
			  Receipt rec = (Receipt)scrollableResults.get()[0];
			  rec.setAgingDays(Days.daysBetween(rec.getDate(), from_date).getDays());
			 
		      list.add(rec);
		   
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public void deleteReceipt(Long entityId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("delete from Receipt where receipt_id =:id");
		query.setParameter("id", entityId);
		
		try{
			query.executeUpdate();
			}
		catch(Exception e){	
		}
		
	}

	@Override
	public List<Receipt> getReceiptForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		List<Receipt> list = new ArrayList<>();
		Query query = null; 
		Session session = getCurrentSession();
		  if(customerId!=null) {
				
				if(customerId.equals((long)-1))
				{
					query = session.createQuery("SELECT receipt from Receipt receipt "
				+ "WHERE receipt.flag=:flag and receipt.company.company_id =:company_id and receipt.subLedger.subledger_Id IS NULL and receipt.entry_status !=:entry_status and receipt.date >=:date1 and receipt.date <=:date2 order by receipt.date ASC,receipt.receipt_id ASC");
		
				}
				else
				{
					query = session.createQuery("SELECT receipt from Receipt receipt "
							+ "WHERE receipt.flag=:flag and receipt.customer.customer_id =:customer_id and receipt.subLedger.subledger_Id IS NULL and receipt.company.company_id =:company_id and receipt.entry_status !=:entry_status and receipt.date >=:date1 and receipt.date <=:date2 order by receipt.date ASC,receipt.receipt_id ASC");
					query.setParameter("customer_id", customerId);
					
				}
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("date1", from_date);
				query.setParameter("date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  list.add((Receipt)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
			       }
		  return list;
	}

	@Override
	public List<Receipt> customerReceiptHavingGST0(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
					+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date>=:from_date and receipt.date<=:to_date "
					+ "and receipt.gst_applied=:gst_applied and receipt.entry_status !=:entry_status and receipt.customer.gst_applicable=:gst_applicable and receipt.cgst=:cgst and receipt.igst=:igst and receipt.sgst=:sgst");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", true);
			query.setParameter("gst_applied", 1);
			query.setParameter("cgst", (double)0);
			query.setParameter("igst", (double)0);
			query.setParameter("sgst", (double)0);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Receipt)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	}
	
	@Override
	public List<Receipt> customerReceiptWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
				List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		try {
			Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
					+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date>=:from_date and receipt.date<=:to_date and receipt.customer IS NULL "
					+ "and receipt.amount>=:amount and receipt.tds_amount=:tds_amount and (receipt.subLedger.subledger_name LIKE '%Legal%' or receipt.subLedger.subledger_name LIKE '%Professional%' or receipt.subLedger.subledger_name LIKE '%Repairs & Maintenance%')");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("amount",(double)30000);
			query.setParameter("tds_amount",(double)0);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Receipt)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	}
	@Override
	public List<Receipt> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
					+"WHERE receipt.company.company_id =:company_id and receipt.flag =:flag and receipt.date>=:from_date and receipt.date<=:to_date "
					+ "and receipt.customer.gst_applicable=:gst_applicable and (receipt.cgst>0 or receipt.igst>0 or receipt.sgst>0)");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((Receipt)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
              session.clear();
			 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		 
		 return list;
	}

	@Override
	public Receipt isExcelVocherNumberExist(String vocherNo, Long companyId) {
		
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Receipt)criteria.uniqueResult();
	}

	@Override
	public List<Receipt> getAllReceiptsAgainstAdvanceReceipt(Long advanceReceiptId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		criteria.add(Restrictions.eq("advreceipt.receipt_id", advanceReceiptId));
		return criteria.list();
	}
	
	public List<Receipt> getAllOpeningBalanceAgainstReceipt(Long customerId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

		
	}
	public List<Receipt> getReceiptForSales(Long customerId, Long companyId,LocalDate toDate) {
		Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		if(customerId !=null){
			if(customerId.equals((long)-1)){
				//Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
				Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
				Criterion criterion4=Restrictions.ne("againstOpeningBalnce", true);
				Criterion criterion5=Restrictions.le("date", toDate);
				criteria.add(Restrictions.and(criterion3,criterion5));
				}
			else{
				Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
				Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
				Criterion criterion4=Restrictions.ne("againstOpeningBalnce", true);
				Criterion criterion5=Restrictions.le("date", toDate);
				criteria.add(Restrictions.and(criterion2,criterion3,criterion5));
				
			}
			}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

		
	}
	@Override
	public List<Receipt> getAllAdvanceReceiptsAgainstCustomer(Long companyId,Long customer_id) {
		List<Receipt> list =  new ArrayList<Receipt>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT receipt from Receipt receipt LEFT JOIN FETCH receipt.company company "
				+"WHERE receipt.company.company_id =:company_id and receipt.advance_payment=:advance_payment and receipt.sales_bill_id.sales_id IS NULL and receipt.flag =:flag and receipt.customer.customer_id=:customer_id ORDER by receipt.date DESC, receipt.receipt_id DESC");
		query.setParameter("company_id", companyId);
		query.setParameter("customer_id", customer_id);
		query.setParameter("advance_payment", true);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((Receipt)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
	}

	@Override
	public List<Receipt> getAllOpeningBalanceAgainstReceiptForPeriod(Long customerId, Long companyId,
			LocalDate toDate) {
		// TODO Auto-generated method stub
Criteria criteria = getCurrentSession().createCriteria(Receipt.class);
		
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2=Restrictions.eq("flag", true);
		Criterion criterion3 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion4 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion5=Restrictions.neOrIsNotNull("entry_status", MyAbstractController.ENTRY_CANCEL);
		Criterion criterion6 = Restrictions.le("date",toDate);	
		criteria.add(Restrictions.and(criterion1,criterion2,criterion3,criterion4,criterion5,criterion6));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

	}
}