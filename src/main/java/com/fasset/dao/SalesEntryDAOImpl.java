/**
 * mayur suramwar
 */
package com.fasset.dao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;

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
import org.hibernate.transform.Transformers;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IGstTaxMasterDAO;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.Product;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.State;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.IClientSubscriptionMasterService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.IQuotationService;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;


/**
 * @author mayur suramwar
 *
 * deven infotech pvt ltd.
 */
@Transactional
@Repository
public class SalesEntryDAOImpl extends AbstractHibernateDao<SalesEntry> implements ISalesEntryDAO{

	@Autowired
	private IGstTaxMasterDAO GstTaxDao;
	
	@Autowired
	private IStockService stockService;
	
	@Autowired
	private IProductDAO productDAO;
		
	@Autowired
	private ISubLedgerDAO subLedgerDAO ;
	
	@Autowired
	ISubLedgerService subledgerService;

	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	IBankService bankService;
	
	@Autowired
	private IOpeningBalancesDAO balanceDao;
	
	@Override
	public Company findCompanyDao(Long user_id) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", user_id));
		User user =(User) criteria.uniqueResult();
		Company company = user.getCompany();
		return company;
	}

	@Override
	public Long saveSalesEntry(SalesEntry sup) {
		Session session = getCurrentSession();
		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		informationList = sup.getInformationList();
		sup.setLocal_time(new LocalTime());
		Long id = (Long) session.save(sup);
		session.flush();
		session.clear();
		Long company_id=sup.getCompany().getCompany_id();
		for(int i = 0;i<informationList.size();i++){
			ProductInformation information =  informationList.get(i);
			
			Query query = session.createQuery("update SalesEntryProductEntityClass set quantity=:quantity,rate=:rate,discount=:discount,"
					+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
					+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,"
					+ "product_name=:product_name,VAT=:VAT,VATCST=:VATCST,Excise=:Excise,is_gst=:is_gst where sales_id =:pid and product_id=:proid");
			
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
			query.setParameter("VAT", Double.parseDouble(information.getVAT()));
			query.setParameter("VATCST", Double.parseDouble(information.getVATCST()));
			query.setParameter("Excise", Double.parseDouble(information.getExcise()));
			query.setParameter("is_gst", Long.parseLong(information.getIs_gst()));
		      try{
		    	  query.executeUpdate();
		      }
		      
		      
		     catch (Exception e){
		    	 e.printStackTrace();		    	 
		     }	
		      
		    if(!information.getProductname().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
			{
		     Integer pflag=productDAO.checktype(company_id,Long.parseLong(information.getProductId()));
		     if(pflag==1)  
		     {
		      Stock stock = null;
		      stock=stockService.isstockexist(company_id,Long.parseLong(information.getProductId()));
				if(stock!=null)
				{
					double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()));
					stock.setAmount(stock.getAmount()-amount);
					stock.setQuantity(stock.getQuantity()-Double.parseDouble(information.getQuantity()));					
					stockService.updateStock(stock);
				}
				
		     }
			}
		}
		
		return id;
	}

	@Override
	public void update(SalesEntry sup) throws MyWebException {
	
        Session session = getCurrentSession();
        Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("sales_id", sup.getSales_id()));
		criteria.setFetchMode("products", FetchMode.JOIN);
		SalesEntry entity=(SalesEntry)criteria.uniqueResult();
		
		Long company_id=entity.getCompany().getCompany_id();
		if(sup.getUpdated_by()!=null)
		{
			entity.setUpdated_by(sup.getUpdated_by());
		}
		if(sup.getCustomer()!=null)
		{
		entity.setCustomer(sup.getCustomer());
		}
		if(sup.getCustomer_bill_date()!=null)
		{
			entity.setCustomer_bill_date(sup.getCustomer_bill_date());
		}
		if(sup.getCustomer_bill_no()!=null)
		{
			entity.setCustomer_bill_no(sup.getCustomer_bill_no());
		}
		if(sup.getSubledger()!=null)
		{
			entity.setSubledger(sup.getSubledger());
		}
		if(sup.getRemark()!=null)
		{
			entity.setRemark(sup.getRemark());
		}
		if(sup.getEntrytype()!=null)
		{
			entity.setEntrytype(sup.getEntrytype());
		}
		if(sup.getSale_type()!=null)
		{
			entity.setSale_type(sup.getSale_type());
		}
		if(sup.getBank()!=null)
		{
			entity.setBank(sup.getBank());
		}
		if(sup.getEntry_status()!=null)
		{
			entity.setEntry_status(sup.getEntry_status());
		}
		
		if(sup.getAdvreceipt()!=null)
		{
			entity.setAdvreceipt(sup.getAdvreceipt());
		}
		
		
		entity.setFlag(true);
		
		if(sup.getEntrytype()==2)
		{
			if(sup.getShipping_bill_no()!=null)
			{
				entity.setShipping_bill_no(sup.getShipping_bill_no());
			}
			if(sup.getExport_type()!=null)
			{
				entity.setExport_type(sup.getExport_type());
			}
			if(sup.getShipping_bill_date()!=null)
			{
				entity.setShipping_bill_date(sup.getShipping_bill_date());
			}
			if(sup.getPort_code()!=null)
			{
				entity.setPort_code(sup.getPort_code());
			}
			
		}
		else
		{
			entity.setShipping_bill_no(null);
			entity.setExport_type(null);
			entity.setShipping_bill_date(null);
			entity.setPort_code(null);
		}
		
		if(sup.getInformationList()!=null && sup.getInformationList().size()>0){
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
					entity.setTotal_vat(sup.getTotal_vat());
					entity.setTotal_vatcst(sup.getTotal_vatcst());
					entity.setTotal_excise(sup.getTotal_excise());
					entity.setState_compansation_tax(sup.getState_compansation_tax());
					entity.setRound_off(sup.getRound_off());
					if(sup.getTds_amount()!=null)
					{
					entity.setTds_amount(sup.getTds_amount());
					}
					
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
					Query query = session.createQuery("update SalesEntryProductEntityClass set quantity=:quantity,rate=:rate,discount=:discount,"
							+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
							+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,"
							+ "product_name=:product_name,VAT=:VAT,VATCST=:VATCST,Excise=:Excise,is_gst=:is_gst where sales_id =:pid and product_id=:proid");
		
					query.setParameter("pid", sup.getSales_id());
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
			         query.setParameter("is_gst",Long.parseLong(information.getIs_gst()));
				      query.setParameter("VAT", Double.parseDouble(information.getVAT()));
				      query.setParameter("VATCST", Double.parseDouble(information.getVATCST()));
				      query.setParameter("Excise", Double.parseDouble(information.getExcise()));
		      
					try{
						query.executeUpdate();
					}
					catch (Exception e){
						e.printStackTrace();		    	 
					}	
					
					  if(!information.getProductname().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
						{
					     Integer pflag=productDAO.checktype(company_id,Long.parseLong(information.getProductId()));
					     if(pflag==1)  
					     {
					      Stock stock = null;
					      stock=stockService.isstockexist(company_id,Long.parseLong(information.getProductId()));
							if(stock!=null)
							{
								double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()));
								stock.setAmount(stock.getAmount()-amount);
								stock.setQuantity(stock.getQuantity()-Double.parseDouble(information.getQuantity()));					
								stockService.updateStock(stock);
							}
							
					     }
						}
				}
			}		
		}
		
		else{
			Set<Product> products = new HashSet<Product>();
			products = entity.getProducts();
			entity.setProducts(products);
			entity.setUpdate_date(new LocalDateTime());
			session.merge(entity);
			session.flush();
			session.clear();
		}
		
	}
	
	
	@Override
	public SalesEntry findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("sales_id", id));
		return (SalesEntry) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntry> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from SalesEntry order by sales_id desc");
		return query.list();
	}	

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntryProductEntityClass> findAllSalesEntryProductEntityList(Long entryId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from SalesEntryProductEntityClass where sales_id=:entryId");
		query.setParameter("entryId", entryId);
		return query.list();
	}

	@Override
	public String deleteSalesEntryProduct(Long SEId,Long sale_id,Long company_id) {
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double state_comp_tax =(double)0;
		Double round_off = (double)0;
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
		Double tdsnew=(double)0;
		Double tdsrate = (double)0;
		Double tds=(double)0;
		Integer tdsapply=null;
		SubLedger subledger1 = new SubLedger();
		Customer customer1 = new Customer();
		
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(SalesEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("sales_detail_id", SEId));
		SalesEntryProductEntityClass salesdetails =(SalesEntryProductEntityClass) criteria.uniqueResult();
		
		Criteria criteria1 = session.createCriteria(SalesEntry.class);
		criteria1.add(Restrictions.eq("sales_id", sale_id));
		SalesEntry salesEntry =(SalesEntry) criteria1.uniqueResult();
		
		if(subledger1!=null)
		{
		 subledger1=salesEntry.getSubledger();
		}
		if(salesEntry.getCustomer()!=null)
		{
         customer1=salesEntry.getCustomer();
         tdsapply=salesEntry.getCustomer().getTds_applicable();
         tdsrate = (double)salesEntry.getCustomer().getTds_rate();
		}		
		if(salesEntry.getTransaction_value()!=null)
		{
		transaction_value= salesEntry.getTransaction_value();
		}
		if(salesEntry.getCgst()!=null)
		{
		cgst=salesEntry.getCgst();
		}
		if(salesEntry.getIgst()!=null)
		{
		igst=salesEntry.getIgst();
		}
		if(salesEntry.getSgst()!=null)
		{
		sgst=salesEntry.getSgst();
		}
		if(salesEntry.getState_compansation_tax()!=null)
		{
		state_comp_tax=salesEntry.getState_compansation_tax();
		}
		if(salesEntry.getRound_off()!=null)
		{
		round_off=salesEntry.getRound_off();
		}
		if(salesEntry.getTotal_vat()!=null)
		{
		vat=salesEntry.getTotal_vat();
		}
		if(salesEntry.getTotal_vatcst()!=null)
		{
		vatcst=salesEntry.getTotal_vatcst();
		}
		if(salesEntry.getTotal_excise()!=null)
		{
		excise=salesEntry.getTotal_excise();
		}
		if(salesEntry.getTds_amount()!=null)
		{
			tds=salesEntry.getTds_amount();
		}
		
		if(salesdetails.getCGST()!=null)
		{
		CGST=salesdetails.getCGST();
		}
		if(salesdetails.getIGST()!=null) {
		IGST=salesdetails.getIGST();
		}
		if(salesdetails.getSGST()!=null)
		{
		SGST=salesdetails.getSGST();
		}
		if(salesdetails.getState_com_tax()!=null)
		{
		state_com_tax=salesdetails.getState_com_tax();
		}
		if(salesdetails.getTransaction_amount()!=null)
		{
		transaction_amount=salesdetails.getTransaction_amount();
		}
		
		if(salesdetails.getVAT()!=null)
		{
		VAT=salesdetails.getVAT();
		}
		if(salesdetails.getVATCST()!=null)
		{
		VATCST=salesdetails.getVATCST();
		}
		if(salesdetails.getExcise()!=null) 
		{
		Excise=salesdetails.getExcise();
		}
		
		
		cgst=cgst-CGST;
		igst=igst-IGST;
		sgst=sgst-SGST;
		state_comp_tax= state_comp_tax-state_com_tax;
		vat=vat-VAT;
		vatcst=vatcst-VATCST;
		excise=excise-Excise;
		if(tdsapply!=null&&tdsapply==1)
		{
			tdsnew=(transaction_amount*tdsrate)/100;
		}
		else
			tdsnew=(double)0;
		transaction_value=transaction_value-transaction_amount;
		roundamount=CGST+IGST+SGST+state_com_tax+VAT+VATCST+Excise+transaction_amount;
		
		tds=tds-tdsnew;
		
		round_off = round_off - roundamount + tdsnew;
		
		salesEntry.setCgst(cgst);
		salesEntry.setIgst(igst);
		salesEntry.setSgst(sgst);
		salesEntry.setState_compansation_tax(state_comp_tax);
		salesEntry.setTotal_vat(vat);
		salesEntry.setTotal_vatcst(vatcst);
		salesEntry.setTotal_excise(excise);
		salesEntry.setTransaction_value(transaction_value);
		salesEntry.setRound_off(round_off);
		salesEntry.setTds_amount(tds);
		
		
		if(VAT==0 && VATCST==0 && Excise==0)
		{
			if((customer1!=null) || (salesEntry.getCustomer()!=null))
			{
				try {
					customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
							customer1.getCustomer_id(), (long) 5, (double)0, (double)(roundamount-tdsnew),
							(long) 0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				if(salesEntry.getSale_type()==1)
				{
				SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", salesEntry.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double)0, (double)(roundamount-tdsnew), (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
				else
				{
					bankService.addbanktransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), salesEntry.getBank().getBank_id(), (long) 3, (double)0,(double)(roundamount-tdsnew),(long) 0);
				}
				salesEntry.setEntry_status(2);
			}
			
			if(salesEntry.getAgainst_advance_receipt()!=null && salesEntry.getAgainst_advance_receipt()==false)
			{
				SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", salesEntry.getCompany().getCompany_id());
				if (subledgerTds != null) {
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)tdsnew, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}
			
			if(subledger1!=null)
			{
			try {
				
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
						subledger1.getSubledger_Id(), (long) 2, (double)transaction_amount, (double)0,
						(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
			catch (Exception e) {
				e.printStackTrace();
			}
			}
			
			try{
				if(CGST!=null && CGST>0)
				{
					SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST",
							salesEntry.getCompany().getCompany_id());
					if (subledgercgst != null) {
						
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgercgst.getSubledger_Id(), (long) 2, (double)CGST, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					
					}
				}
				
				if(SGST!=null && SGST>0)
				{
					SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgersgst != null) {
						
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgersgst.getSubledger_Id(), (long) 2,(double)SGST, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
							
					}
				}
				
				if(IGST!=null && IGST>0)
				{
					SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST",
							salesEntry.getCompany().getCompany_id());
					if (subledgerigst != null) {
						
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgerigst.getSubledger_Id(), (long) 2, (double)IGST, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
								}
				}
				
				if(state_com_tax!=null && state_com_tax>0)
				{
					SubLedger subledgercess = subLedgerDAO.findOne("Output CESS",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgercess != null) {
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgercess.getSubledger_Id(), (long) 2, (double)state_com_tax,
								(double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try{
				
				if(VAT!=null && VAT>0)
				{
					SubLedger subledgervat = subLedgerDAO.findOne("Output VAT",
							salesEntry.getCompany().getCompany_id());
					
					if (subledgervat != null) {
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgervat.getSubledger_Id(), (long) 2, (double)VAT, (double)0,
								(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
						
				}
				}
				
				if(VATCST!=null && VATCST>0)
				{
					SubLedger subledgercst = subLedgerDAO.findOne("Output CST",
							salesEntry.getCompany().getCompany_id());
					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgercst.getSubledger_Id(), (long) 2, (double)VATCST, (double)0,
								(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if(Excise!=null && Excise>0)
				{
					SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
							salesEntry.getCompany().getCompany_id());
					if (subledgerexcise != null) {
						subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),company_id,
								subledgerexcise.getSubledger_Id(), (long) 2, (double)Excise, (double)0,
								(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					
					}
				}
			
			}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
		
	    if(!salesdetails.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
	    {
		Integer pflag=productDAO.checktype(company_id,salesdetails.getProduct_id());
	     if(pflag==1)  
	     {
	      Stock stock = null;
	      stock=stockService.isstockexist(company_id,salesdetails.getProduct_id());
	      double amt=salesdetails.getQuantity()*salesdetails.getRate();
			if(stock!=null)
			{
				stock.setAmount(stock.getAmount()+amt);
				stock.setQuantity(stock.getQuantity()+salesdetails.getQuantity());					
				stockService.updateStock(stock);
				stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, salesdetails.getProduct_id(), salesdetails.getQuantity(), salesdetails.getRate());
			}
	     }	
	    }
		  
		Query query = session.createQuery("delete from SalesEntryProductEntityClass where sales_detail_id=:SEId");
		query.setParameter("SEId", SEId);
		String msg="";
		try{
			query.executeUpdate();
			session.update(salesEntry);
			msg= "Product Deleted successfully";
		}
		catch(Exception e){
			msg= "You can't delete Product";
			
		}
		return msg;
	}

	@Override
	public SalesEntry isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CopyOnWriteArrayList<SalesEntry> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {
		
		CopyOnWriteArrayList<SalesEntry> list = new CopyOnWriteArrayList<>();
		/*Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.ge("created_date", from_date)); 
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("company.company_id", comId));
		criteria.setFetchMode("customer",  FetchMode.JOIN);
		criteria.setFetchMode("subledger",  FetchMode.JOIN);
		criteria.setFetchMode("bank",  FetchMode.JOIN);
		list1= criteria.list();*/
		List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
		saleslist.clear();
					Session session = getCurrentSession();
					Query query = session.createQuery("SELECT sales from SalesEntry sales "
							+ "WHERE sales.flag=:flag and sales.company.company_id =:company_id and sales.entry_status !=:entry_status and sales.created_date >=:date1 and sales.created_date <=:date2 ORDER by sales.created_date DESC,sales.sales_id DESC");
					query.setParameter("company_id", comId);
					query.setParameter("flag", true);
					query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
					query.setParameter("date1", from_date);
					query.setParameter("date2", to_date);
					ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
					
					  while (scrollableResults.next()) {
						  saleslist.add((SalesEntry)scrollableResults.get()[0]);
					   session.evict(scrollableResults.get()[0]);
				         }
					  session.clear();	  
		
		for(SalesEntry entry :saleslist)
		{
			list.add(entry);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntry> findAllSalesEntryOfCompany(Long CompanyId,Boolean flag) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
					+"LEFT JOIN FETCH sales.bank bank "
					+"WHERE sales.company.company_id =:company_id and sales.flag =:flag   ORDER by sales.created_date DESC,sales.sales_id DESC");
			query.setParameter("company_id", CompanyId);
			query.setParameter("flag", flag);
			//query.setParameter("yearId", yearId);// and sales.accountingYear.year_id=:yearId
			/*
			 * query.setParameter("year_status_id", 1);
			 * System.out.println("query is:"+query);
			 */
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntry> findAllactiveSalesEntryOfCompany(Long CompanyId,Boolean flag) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
				+"WHERE sales.company.company_id =:company_id and flag =:flag and entry_status =:entry_status");
		query.setParameter("company_id", CompanyId);
		query.setParameter("flag", flag);
		query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((SalesEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		  return list;
		  
	}
	@Override
	public String deleteByIdValue(Long entityId,Boolean isDelete) {
		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double state_comp_tax =(double)0;
		Double round_off = (double)0;
		
		SubLedger subledger1 = null;
		Customer customer1 = null;
		
		Session session = getCurrentSession();
		Criteria criteria1 = session.createCriteria(SalesEntry.class);
		criteria1.add(Restrictions.eq("sales_id", entityId));
		criteria1.setFetchMode("company",  FetchMode.JOIN);
		SalesEntry salesEntry =(SalesEntry) criteria1.uniqueResult();
		
		Criteria criteria = session.createCriteria(SalesEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("sales_id", entityId));
		 @SuppressWarnings("unchecked")
		List<SalesEntryProductEntityClass> salesdetails =criteria.list();
		 
		for (SalesEntryProductEntityClass saleproduct : salesdetails) 
		{
			if(salesEntry.getCompany()!=null && saleproduct!=null)
			{
				
			Integer pflag=productDAO.checktype(salesEntry.getCompany().getCompany_id(),saleproduct.getProduct_id());
		     if(pflag==1)  
		     {
		      Stock stock = null;
		      stock=stockService.isstockexist(salesEntry.getCompany().getCompany_id(),saleproduct.getProduct_id());
		      double amt=saleproduct.getQuantity()*saleproduct.getRate();

		      if(stock!=null)
				{
		    	    stock.setAmount(stock.getAmount()+amt);
					stock.setQuantity(stock.getQuantity()+saleproduct.getQuantity());					
					stockService.updateStock(stock);
					stockService.addStockInfoOfProduct(stock.getStock_id(), salesEntry.getCompany().getCompany_id(), saleproduct.getProduct_id(), saleproduct.getQuantity(), saleproduct.getRate());
				}
			
		     }
		     
			}
		}
		
		if(salesEntry.getSubledger()!=null)
		{
			subledger1=salesEntry.getSubledger();
		}
		
		if((salesEntry.getCustomer()!=null))
		{
		customer1=salesEntry.getCustomer();
		}
		
		if(salesEntry.getTransaction_value()!=null)
		{
		transaction_value= salesEntry.getTransaction_value();
		}
		if(salesEntry.getRound_off()!=null)
		{
		round_off=salesEntry.getRound_off();
		}
		
		if(salesEntry.getCgst()!=null && salesEntry.getCgst()>0)
		{
			cgst=salesEntry.getCgst();
		}
		if(salesEntry.getIgst()!=null && salesEntry.getIgst()>0)
		{
			igst=salesEntry.getIgst();
		}
		if(salesEntry.getSgst()!=null && salesEntry.getSgst()>0)
		{
			sgst=salesEntry.getSgst();
		}
		if(salesEntry.getState_compansation_tax()!=null && salesEntry.getState_compansation_tax()>0)
		{
			state_comp_tax=salesEntry.getState_compansation_tax();
		}
		if(salesEntry.getTotal_vat()!=null && salesEntry.getTotal_vat()>0)
		{
			vat=salesEntry.getTotal_vat();
		}
		if(salesEntry.getTotal_vatcst()!=null && salesEntry.getTotal_vatcst()>0 )
		{
			vatcst=salesEntry.getTotal_vatcst();
		}
		if(salesEntry.getTotal_excise()!=null && salesEntry.getTotal_excise()>0)
		{
			excise=salesEntry.getTotal_excise();
		}
		
		
		if(customer1!=null)
		{
			try {
				customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						customer1.getCustomer_id(), (long) 5, (double)0, (double)round_off, (long) 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(salesEntry.getSale_type()!=null)
			{
			if(salesEntry.getSale_type()==1)
			{
			SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", salesEntry.getCompany().getCompany_id());
				if(subledgercgst!=null)
				{
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round_off, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
			}
			else
			{
				if(salesEntry.getBank()!=null)
				{
				bankService.addbanktransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), salesEntry.getBank().getBank_id(), (long) 3, (double)0,(double)round_off,(long) 0);
				}
			}
			}
		}
		
	
		if(salesEntry.getAgainst_advance_receipt()!=null && salesEntry.getAgainst_advance_receipt()==false && salesEntry.getTds_amount()!=null && salesEntry.getTds_amount()>0)
		{
			SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", salesEntry.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)salesEntry.getTds_amount(), (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
			}
		}
			
		if(subledger1!=null)
		{
			try {
				
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
			try{
				if(cgst!=null && cgst>0)
				{
					SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", salesEntry.getCompany().getCompany_id());
					if(subledgercgst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(sgst!=null && sgst>0)
				{
					SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", salesEntry.getCompany().getCompany_id());
					if(subledgersgst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(igst!=null && igst>0)
				{
					SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", salesEntry.getCompany().getCompany_id());
					if(subledgerigst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(state_comp_tax!=null && state_comp_tax>0)
				{
					SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", salesEntry.getCompany().getCompany_id());
					if(subledgercess!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double)0,
							(long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
			try{
				
				if(vat!=null && vat>0)
				{
					SubLedger subledgervat = subLedgerDAO.findOne("Output VAT", salesEntry.getCompany().getCompany_id());
					if(subledgervat!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(vatcst!=null && vatcst>0)
				{
					SubLedger subledgercst = subLedgerDAO.findOne("Output CST", salesEntry.getCompany().getCompany_id());
					if(subledgercst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if(excise!=null && excise>0)
				{
					SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
							salesEntry.getCompany().getCompany_id());
					
					if(subledgerexcise!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0, (long) 0,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			
			String msg="";
		
			if(isDelete)
			{
				Query query1 = session.createQuery("delete from SalesEntryProductEntityClass where sales_id =:sales_id");
				query1.setParameter("sales_id", entityId);
				query1.executeUpdate();

				balanceDao.deleteOpeningBalance(entityId,null, null, null, null, null,null,null, null,null,null,null);
				
				Query query = session.createQuery("delete from SalesEntry where sales_id =:id");
				query.setParameter("id", entityId);
				query.executeUpdate();
				
			
			}
			else
			{
				Query query = session.createQuery("update SalesEntry set entry_status=:entry_status where sales_id =:id");
				query.setParameter("id", entityId);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				
				try{
					query.executeUpdate();
					msg= "Sales Entry deactivated successfully";
				}
				catch(Exception e){
					msg= "You can't change status of Sales Entry";			
				}
			}
			
			return msg;
	}

	@Override
	public List<SalesEntry> findAllByCustomers(Long customerId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("customer.customer_id",customerId),
				Restrictions.eq("company.company_id", companyId)
				));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SalesEntry> getBillsReceivable(Long customerId, LocalDate fromDate, LocalDate toDate, Long companyId) {
	/*	Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.ge("created_date", fromDate)); 
		criteria.add(Restrictions.le("created_date", toDate));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		if(customerId > 0){
			criteria.add(Restrictions.eq("customer.customer_id",customerId));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		return criteria.list();*/
		
		List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
		saleslist.clear();
		Session session = getCurrentSession();
		Query query = null;
		
		  if(customerId!=null) {
				
				if(customerId > 0)
				{
					query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
							+ "WHERE salesEntry.flag=:flag and salesEntry.againstOpeningBalnce =:againstOpeningBalnce and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id =:customer_id and salesEntry.entry_status =:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date desc,salesEntry.sales_id desc");
					query.setParameter("customer_id", customerId);
					
				}
				else
				{
					System.out.println("The Query value");
					query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
							+ "WHERE salesEntry.flag=:flag and salesEntry.againstOpeningBalnce =:againstOpeningBalnce and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id IS NOT NULL and salesEntry.entry_status =:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date desc,salesEntry.sales_id desc");
					
				}
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("againstOpeningBalnce", false);
				query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
				query.setParameter("created_date1", fromDate);
				query.setParameter("created_date2", toDate);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		
		  return saleslist;
		
	}
	
	@Override
	public List<SalesEntry> getB2BList(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<SalesEntry> b2BList = new ArrayList<>();
		List<SalesEntry> b2BWithGSTrateList = new ArrayList<>();
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.eq("cust.gst_applicable",true),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)
				));
		criteria.addOrder(Order.asc("created_date"));
		criteria.setFetchMode("customer",  FetchMode.JOIN);
		criteria.setFetchMode("subledger",  FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		b2BList =  criteria.list();
		
		for(SalesEntry entry : b2BList)
		{
			List<SalesEntryProductEntityClass> productinfoList = new ArrayList<>();
			for(SalesEntryProductEntityClass entity : findAllSalesEntryProductEntityList(entry.getSales_id()))
			{
				GstTaxMaster tax = GstTaxDao.getHSNbyDate(entry.getCreated_date(),entity.getHSNCode());
				entity.setGstRate(tax.getIgst());
				productinfoList.add(entity);
			}
			entry.setProductinfoList(productinfoList);
			
			b2BWithGSTrateList.add(entry);
		}
		return b2BWithGSTrateList;
	}

	@Override
	public List<SalesEntry> getB2CLList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		System.out.println("getB2CLList");
		List<SalesEntry> b2CLList = new ArrayList<>();
		List<SalesEntry> b2CLWithGSTrateList = new ArrayList<>();
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable",false),
				Restrictions.gt("round_off",(double)250000),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)
				));
		criteria.addOrder(Order.asc("created_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		
		b2CLList =  criteria.list();
		
		for(SalesEntry entry : b2CLList)
		{
		//	System.out.println("The customer name and gst");
			List<SalesEntryProductEntityClass> productinfoList = new ArrayList<>();
			for(SalesEntryProductEntityClass entity : findAllSalesEntryProductEntityList(entry.getSales_id()))
			{
				GstTaxMaster tax = GstTaxDao.getHSNbyDate(entry.getCreated_date(),entity.getHSNCode());
				entity.setGstRate(tax.getIgst());
				productinfoList.add(entity);
				
			}
			entry.setProductinfoList(productinfoList);
			b2CLWithGSTrateList.add(entry);
		}
		return b2CLWithGSTrateList;
		
	}
	
	@Override
	public List<SalesEntry[]> getIntraRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
			    Restrictions.eq("cust.state.state_id", stateId),
				Restrictions.eq("cust.gst_applicable",true),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.eq("igst",0.0d),
				Restrictions.eq("cgst",0.0d),
				Restrictions.eq("sgst",0.0d)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}
	 
	

	@Override
	public List<SalesEntry[]> getIntraNonRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.eq("cust.state.state_id", stateId),
				Restrictions.eq("cust.gst_applicable",false),
				Restrictions.eq("igst",0.0d),
				Restrictions.eq("cgst",0.0d),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.eq("sgst",0.0d)
				)).setProjection(Projections.projectionList()
                        .add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}


	@Override
	public List<SalesEntry[]> getInterRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable",true),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.eq("igst",0.0d),
				Restrictions.eq("cgst",0.0d),
				Restrictions.eq("sgst",0.0d)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))      
                ).list();
		
		return result;
	}

	@Override
	public List<SalesEntry[]> getInterNonRegisterList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable",false),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.eq("igst",0.0d),
				Restrictions.eq("cgst",0.0d),
				Restrictions.eq("sgst",0.0d)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}
	
	@Override
	public List<SalesEntry[]> getIntraRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criterion c1 = Restrictions.ge("total_vat", 0.0d);
		Criterion c2 = Restrictions.ge("total_vatcst", 0.0d);
		Criterion c3 = Restrictions.ge("total_excise", 0.0d);
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.eq("cust.state.state_id", stateId),
				Restrictions.eq("cust.gst_applicable",true),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.or(c1, c2,c3)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}

	@Override
	public List<SalesEntry[]> getIntraNonRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criterion c1 = Restrictions.ge("total_vat", 0.0d);
		Criterion c2 = Restrictions.ge("total_vatcst", 0.0d);
		Criterion c3 = Restrictions.ge("total_excise", 0.0d);
		
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.eq("cust.state.state_id", stateId),
				Restrictions.eq("cust.gst_applicable",false),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.or(c1, c2,c3)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))     
                ).list();
		
		return result;
	}

	@Override
	public List<SalesEntry[]> getInterRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criterion c1 = Restrictions.ge("total_vat", 0.0d);
		Criterion c2 = Restrictions.ge("total_vatcst", 0.0d);
		Criterion c3 = Restrictions.ge("total_excise", 0.0d);
		
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable",true),
				Restrictions.or(c1, c2,c3)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}

	@Override
	public List<SalesEntry[]> getInterNonRegisterVATList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List result = null;
		Criterion c1 = Restrictions.ge("total_vat", 0.0d);
		Criterion c2 = Restrictions.ge("total_vatcst", 0.0d);
		Criterion c3 = Restrictions.ge("total_excise", 0.0d);
		
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.createAlias("customer", "cust");
		result=criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL),
				Restrictions.not(Restrictions.eq("cust.state.state_id", stateId)),
				Restrictions.eq("cust.gst_applicable",false),
				Restrictions.or(c1, c2,c3)
				)).setProjection(Projections.projectionList()
						.add(Projections.sum("round_off"))
                        .add(Projections.sum("tds_amount"))  
                ).list();
		
		return result;
	}
 
	

	

	@Override
	public List<SalesEntry> getB2CSList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		
		//Following code was earlier code. We have commented to show the b2cs records state wise  interest wise records
		
		/* 	List<SalesEntry> b2csList = new ArrayList<>(); 
		List<SalesEntry> cashSalesList = new ArrayList<>(); 
		List<SalesEntry> b2CSWithGSTrateList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		
		criteria.createAlias("customer", "cust");
		
		Criterion c1 = Restrictions.not(Restrictions.eq("cust.state.state_id", stateId));
		Criterion c2 = Restrictions.le("round_off", (double)250000);
		Criterion c3 = Restrictions.ge("created_date", from_date);
		Criterion c4 = Restrictions.le("created_date", to_date);
		Criterion c5 = Restrictions.eq("company.company_id", companyId);
	    Criterion c6 = Restrictions.and(c1, c2, c3, c5,Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		
		Criterion c7 = Restrictions.eq("company.company_id", companyId);
		Criterion c8 = Restrictions.ge("created_date", from_date);
		Criterion c9 = Restrictions.le("created_date", to_date);
		Criterion c10 = Restrictions.eq("cust.state.state_id", stateId);
		Criterion c11 = Restrictions.and(c7, c8, c10,Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		
		criteria.add(Restrictions.or(c11, c6));
		criteria.addOrder(Order.asc("created_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		b2csList =  criteria.list();
		
		
		
		List<Integer> salesTypes = new ArrayList<>(Arrays.asList(1,2));
		Criteria criteria1 = getCurrentSession().createCriteria(SalesEntry.class);
		criteria1.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.in("sale_type",salesTypes),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)
				));
		criteria1.addOrder(Order.asc("created_date"));
		criteria1.setFetchMode("bank", FetchMode.JOIN);
		criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		cashSalesList =  criteria1.list();
		
		for(SalesEntry entry : cashSalesList)
		{
			b2csList.add(entry);
		}
		

		Collections.sort(b2csList, new Comparator<SalesEntry>() {
	        public int compare(SalesEntry o1, SalesEntry o2) {

	        	LocalDate Date1 = o1.getCreated_date();
	        	LocalDate Date2= o2.getCreated_date();
	            int sComp = Date2.compareTo(Date1);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            LocalTime local_time1 = o1.getLocal_time();
	            LocalTime local_time2= o2.getLocal_time();
	            return local_time2.compareTo(local_time1);
	    }});
		
		for(SalesEntry entry : b2csList)
		{
			List<SalesEntryProductEntityClass> productinfoList = new ArrayList<>();
			for(SalesEntryProductEntityClass entity : findAllSalesEntryProductEntityList(entry.getSales_id()))
			{
				GstTaxMaster tax = GstTaxDao.getHSNbyDate(entry.getCreated_date(),entity.getHSNCode());
				entity.setGstRate(tax.getIgst());
				productinfoList.add(entity);
			}
			entry.setProductinfoList(productinfoList);
			b2CSWithGSTrateList.add(entry);
		}
		return b2CSWithGSTrateList;
		 */
		
		
		
		//New code on 24.11.2019
		List<SalesEntry> b2csList = new ArrayList<>(); 
		Long custId=(long) 1106;
		List<SalesEntry> b2CSWithGSTrateList = new ArrayList<>();
		Session session = getCurrentSession();
		
		String queryText ;
		double amt=(double)250000;
		queryText="SELECT st.state_id, st.state_name ,g.igst, sum(sep.transaction_value) FROM SalesEntry sep join sep.customer c join c.state st, SalesEntryProductEntityClass s,Product p,GstTaxMaster g "
				+"where sep.sales_id=s.sales_id and s.product_id=p.product_id and p.gst_id=g.tax_id and sep.company.company_id=:companyId and sep.created_date >=:fromDt and sep.created_date<=:todate " 
				+ " and ( st.state_id=:stateId or (st.state_id!=:stateId and sep.round_off < :amount ) ) and c.gst_applicable=:flag group by st.state_id,g.igst ";
		Query query = session.createQuery(queryText);
		query.setParameter("companyId", companyId);
		query.setParameter("fromDt", from_date);
		query.setParameter("todate", to_date);
		query.setParameter("stateId", stateId);
		query.setParameter("amount", amt);
		query.setParameter("flag", false);
		List<Object[]> rows = query.list();

for (Object[] row: rows) {
	
	Long gstId;
	Float gstRate;
	//gstId=(Long) row[2];
	gstRate=(Float) row[2];
	Customer cust=new Customer();
	State state1=new State();
	state1.setState_id((Long) row[0]);
	state1.setState_name((String) row[1]);
//cust.setCustomer_id((Long) payment[5]);
//	cust.setFirm_name((String) payment[3]);
cust.setState(state1);
SalesEntry b2cs = new SalesEntry (); 
SalesEntryProductEntityClass prodInfo=new SalesEntryProductEntityClass();
List<SalesEntryProductEntityClass> prod=new ArrayList<>();
prodInfo.setGstRate( gstRate);


b2cs.setTransaction_value((Double) row[3]);
/*Double tranAmt=0.0;
tranAmt=(prodInfo.getGstRate())/100 * b2cs.getTransaction_value();*/
		
prodInfo.setTransaction_amount((Double) row[3]);
prod.add(prodInfo);
b2cs.setProductinfoList(prod);

b2cs.setCustomer(cust);
   System.out.println(" ------- ");
   System.out.println("State Id is : " + row[0]);
   System.out.println("State name is : " + row[1]);
   System.out.println("Tax  name is : " + row[2]);
   //System.out.println("sum is : " + row[2]);
   
   b2csList.add(b2cs);
}
		
	
	
		for(SalesEntry entry : b2csList)
		{
			//List<SalesEntryProductEntityClass> productinfoList = new ArrayList<>();
			/*for(SalesEntryProductEntityClass entity : findAllSalesEntryProductEntityList(entry.getSales_id()))
			{
				GstTaxMaster tax = GstTaxDao.getHSNbyDate(entry.getCreated_date(),entity.getHSNCode());
				entity.setGstRate(tax.getIgst());
				productinfoList.add(entity);
			}*/
			//entry.setProductinfoList(productinfoList);
			b2CSWithGSTrateList.add(entry);
		}
		return b2CSWithGSTrateList;
		
		
		
	}

	@Override
	public List<SalesEntry> getExpList(LocalDate from_date, LocalDate to_date, Long companyId, Long countryId) {
		List<SalesEntry> expList = new ArrayList<>();
		List<SalesEntry> expWithGSTrateList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.eq("entrytype", 2),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)
				));
		criteria.addOrder(Order.asc("created_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		expList =  criteria.list();
		
		for(SalesEntry entry : expList)
		{
			List<SalesEntryProductEntityClass> productinfoList = new ArrayList<>();
			for(SalesEntryProductEntityClass entity : findAllSalesEntryProductEntityList(entry.getSales_id()))
			{
				GstTaxMaster tax = GstTaxDao.getHSNbyDate(entry.getCreated_date(),entity.getHSNCode());
				entity.setGstRate(tax.getIgst());
				productinfoList.add(entity);
			}
			entry.setProductinfoList(productinfoList);
			expWithGSTrateList.add(entry);
		}
		return expWithGSTrateList;
	}

	@Override
	public void updateSalesEntry(SalesEntry sup, SalesEntry entity) {
		
	}

	@Override
	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		Session session = getCurrentSession();
		
		
		Criteria criteria = session.createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.like("voucher_no", range+"%"));
		criteria.addOrder(Order.desc("sales_id"));
		criteria.setMaxResults(1);
		SalesEntry entry = (SalesEntry) criteria.uniqueResult();
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
		
		
		/*Query query = session.createSQLQuery("select count(sales_id)from sales_Entry where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term",  range + "%");
		System.out.println(query.list());
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
	public SalesEntry findOneWithAll(Long salId) {
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("sales_id", salId));
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		criteria.setFetchMode("products", FetchMode.JOIN);
		criteria.setFetchMode("bank", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("company.company_statutory_type", FetchMode.JOIN);

		return (SalesEntry) criteria.uniqueResult();
	}

	@Override
	public SalesEntry findOneWithCustomerAndSublegder(Long salId) {
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("sales_id", salId));
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("advreceipt", FetchMode.JOIN);
		return (SalesEntry) criteria.uniqueResult();
	}

	@Override
	public Long saveSalesEntryThroughExcel(SalesEntry entry) {
		
		Session session = getCurrentSession();
		Long id=(Long) session.save(entry);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public Long saveSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry) {
		Session session = getCurrentSession();
		Long id=(Long) session.save(entry);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public void updateSalesEntryThroughExcel(SalesEntry entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
	    session.clear();
	}

	@Override
	public void updateSalesEntryProductEntityClassThroughExcel(SalesEntryProductEntityClass entry) {
		Session session = getCurrentSession();
		session.save(entry);
		session.flush();
	    session.clear();
	}

	@Override
	public List<SalesEntry> findAllSalesEntriesOfCompany(Long CompanyId ,Boolean importFunction) {
		
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		if(importFunction==true) /** for import we need only that records which are having excel_voucher_no IS NOT NULL to skip all entries which are added through system*/
		{
		
		Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
				+"WHERE sales.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((SalesEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		}
		else if(importFunction==false)/**for other cases we need all records with flag=true i.e it should be from success list or it should be added through system not by import.*/
		{
		
		Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
				+"WHERE sales.company.company_id =:company_id and sales.flag =:flag");
		query.setParameter("company_id", CompanyId);
		query.setParameter("flag", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((SalesEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		}
		 
		  Collections.sort(list, new Comparator<SalesEntry>() {
	            public int compare(SalesEntry sales1, SalesEntry sales2) {
	                Long sales_id1 = new Long(sales1.getSales_id());
	                Long sales_id2 = new Long(sales2.getSales_id());
	                return sales_id2.compareTo(sales_id1);
	            }
	        });
		  session.clear();
		  return list;
		
	}

	@Override
	public SalesEntryProductEntityClass editproductdetailforSalesEntry(Long entryId) {
		Criteria criteria = getCurrentSession().createCriteria(SalesEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("sales_detail_id", entryId));
		
		return (SalesEntryProductEntityClass) criteria.uniqueResult();
	}

	@Override
	public void updateSalesEntry(SalesEntry entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
	    session.clear();
	}

	@Override
	public void updateSalesEntryProductEntityClass(SalesEntryProductEntityClass entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
	    session.clear();
	}

	@Override
	public List<SalesEntry> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.ge("created_date", from_date));
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("created_date"));
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("bank", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<SalesEntry> purchaselist = new ArrayList<SalesEntry>();
		purchaselist.clear();
					Session session = getCurrentSession();
					Query query = session.createQuery("SELECT sales from SalesEntry sales "
							+ "WHERE sales.flag=:flag and sales.company.company_id =:company_id and sales.entry_status !=:entry_status and sales.created_date >=:date1 and sales.created_date <=:date2 order by sales.created_date desc");
					query.setParameter("company_id", companyId);
					query.setParameter("flag", true);
					query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
					query.setParameter("date1", from_date);
					query.setParameter("date2", to_date);
					ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
					
					  while (scrollableResults.next()) {
						  purchaselist.add((SalesEntry)scrollableResults.get()[0]);
					   session.evict(scrollableResults.get()[0]);
				         }
					  session.clear();
				
		
		  return purchaselist;
	}

	@Override
	public String activateByIdValue(long id) {
		
		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double excise = (double)0;
		Double state_comp_tax =(double)0;
		Double round_off = (double)0;
		
		SubLedger subledger1 = null;
		Customer customer1 = null;
		
		Session session = getCurrentSession();
		Criteria criteria1 = session.createCriteria(SalesEntry.class);
		criteria1.add(Restrictions.eq("sales_id", id));
		criteria1.setFetchMode("company",  FetchMode.JOIN);
		SalesEntry salesEntry =(SalesEntry) criteria1.uniqueResult();	
		Criteria criteria = session.createCriteria(SalesEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("sales_id", id));
		 @SuppressWarnings("unchecked")
		List<SalesEntryProductEntityClass> salesdetails =criteria.list();
		for (SalesEntryProductEntityClass saleproduct : salesdetails) 
		{
			if(saleproduct!=null)
			{
		    if(!saleproduct.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
			{	
			Integer pflag=productDAO.checktype(salesEntry.getCompany().getCompany_id(),saleproduct.getProduct_id());
		     if(pflag==1)  
		     {
		      Stock stock = null;
		      stock=stockService.isstockexist(salesEntry.getCompany().getCompany_id(),saleproduct.getProduct_id());
		      if(stock!=null)
				{
		    	    double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), salesEntry.getCompany().getCompany_id(),saleproduct.getProduct_id(), saleproduct.getQuantity());
		    	    stock.setAmount(stock.getAmount()-amount);
					stock.setQuantity(stock.getQuantity()-saleproduct.getQuantity());					
					stockService.updateStock(stock);
				}
		     }
			}
		     }
		}
		if(salesEntry.getSubledger()!=null)
		{
			subledger1=salesEntry.getSubledger();
		}
		if(salesEntry.getCustomer()!=null)
		{
		customer1=salesEntry.getCustomer();
		}
		
		if(salesEntry.getTransaction_value()!=null)
		{
		transaction_value= salesEntry.getTransaction_value();
		}
		if(salesEntry.getRound_off()!=null)
		{
		round_off=salesEntry.getRound_off();
		}
		
		if(salesEntry.getCgst()!=null && salesEntry.getCgst()>0)
		{
			cgst=salesEntry.getCgst();
		}
		if(salesEntry.getIgst()!=null && salesEntry.getIgst()>0)
		{
			igst=salesEntry.getIgst();
		}
		if(salesEntry.getSgst()!=null && salesEntry.getSgst()>0)
		{
			sgst=salesEntry.getSgst();
		}
		if(salesEntry.getState_compansation_tax()!=null && salesEntry.getState_compansation_tax()>0)
		{
			state_comp_tax=salesEntry.getState_compansation_tax();
		}
		if(salesEntry.getTotal_vat()!=null && salesEntry.getTotal_vat()>0)
		{
			vat=salesEntry.getTotal_vat();
		}
		if(salesEntry.getTotal_vatcst()!=null && salesEntry.getTotal_vatcst()>0 )
		{
			vatcst=salesEntry.getTotal_vatcst();
		}
		if(salesEntry.getTotal_excise()!=null && salesEntry.getTotal_excise()>0)
		{
			excise=salesEntry.getTotal_excise();
		}
		
		
		if(customer1!=null)
		{
			try {
				customerService.addcustomertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						customer1.getCustomer_id(), (long) 5, (double)0, (double)round_off, (long) 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(salesEntry.getSale_type()==1)
			{
			SubLedger subledgercgst = subLedgerDAO.findOne("Cash In Hand", salesEntry.getCompany().getCompany_id());
			if(subledgercgst!=null)
			{
			subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), subledgercgst.getSubledger_Id(), (long) 2,(double)0,(double)round_off, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
			}
			}
			else
			{
				if(salesEntry.getBank()!=null)
				{
				bankService.addbanktransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(), salesEntry.getBank().getBank_id(), (long) 3, (double)0,(double)round_off,(long) 1);
			    }
			}
		}
			
		if(salesEntry.getAgainst_advance_receipt()!=null && salesEntry.getAgainst_advance_receipt()==false && salesEntry.getTds_amount()!=null && salesEntry.getTds_amount()>0)
		{
			SubLedger subledgerTds = subLedgerDAO.findOne("TDS Receivable", salesEntry.getCompany().getCompany_id());
			if (subledgerTds != null) {
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledgerTds.getSubledger_Id(), (long) 2, (double)0, (double)salesEntry.getTds_amount(), (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
			}
		}
		
		if(subledger1!=null)
		{
			try {
				
				subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
						subledger1.getSubledger_Id(), (long) 2, (double)transaction_value, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
				}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
			try{
				if(cgst!=null && cgst>0)
				{
					SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", salesEntry.getCompany().getCompany_id());
					if(subledgercgst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercgst.getSubledger_Id(), (long) 2, (double)cgst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(sgst!=null && sgst>0)
				{
					SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", salesEntry.getCompany().getCompany_id());
					if(subledgersgst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgersgst.getSubledger_Id(), (long) 2, (double)sgst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(igst!=null && igst>0)
				{
					SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", salesEntry.getCompany().getCompany_id());
					if(subledgerigst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerigst.getSubledger_Id(), (long) 2, (double)igst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(state_comp_tax!=null && state_comp_tax>0)
				{
					SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", salesEntry.getCompany().getCompany_id());
					if(subledgercess!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercess.getSubledger_Id(), (long) 2, (double)state_comp_tax, (double)0,
							(long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
			try{
				
				if(vat!=null && vat>0)
				{
					SubLedger subledgervat = subLedgerDAO.findOne("Output VAT", salesEntry.getCompany().getCompany_id());
					if(subledgervat!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgervat.getSubledger_Id(), (long) 2, (double)vat, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				
				if(vatcst!=null && vatcst>0)
				{
					SubLedger subledgercst = subLedgerDAO.findOne("Output CST", salesEntry.getCompany().getCompany_id());
					if(subledgercst!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgercst.getSubledger_Id(), (long) 2, (double)vatcst, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				if(excise!=null && excise>0)
				{
					SubLedger subledgerexcise = subLedgerDAO.findOne("Output EXCISE",
							salesEntry.getCompany().getCompany_id());
					
					if(subledgerexcise!=null)
					{
					subledgerService.addsubledgertransactionbalance(salesEntry.getAccountingYear().getYear_id(),salesEntry.getCreated_date(),salesEntry.getCompany().getCompany_id(),
							subledgerexcise.getSubledger_Id(), (long) 2, (double)excise, (double)0, (long) 1,salesEntry,null,null,null,null,null,null,null,null,null,null,null);
					}
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			
			Query query = session.createQuery("update SalesEntry set entry_status=:entry_status where sales_id =:id");
			query.setParameter("id", id);
			if(salesEntry.getCustomer()==null)
			query.setParameter("entry_status", MyAbstractController.ENTRY_NIL);
			else
				query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
			String msg="";
			try{
				query.executeUpdate();
				msg= "Sales Entry Activated successfully";
			}
			catch(Exception e){
				msg= "You can't change status of Sales Entry";			
			}
			return msg;
	}

	@Override
	public void ChangeStatusofSalesEntryThroughExcel(SalesEntry entry) {
		Session session = getCurrentSession();
		entry.setFlag(false);
		session.merge(entry);
		session.flush();
	    session.clear();
		
	}

	@Override
	public List<SalesEntry> getCashSalesList(LocalDate from_date, LocalDate to_date, Long companyId, Long stateId) {
		List<Integer> salesTypes = new ArrayList<>(Arrays.asList(1,2));
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id",companyId),
				Restrictions.in("sale_type",salesTypes),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)
				));
		criteria.addOrder(Order.asc("created_date"));
		criteria.setFetchMode("bank", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Transactional
	@Override
	public List<SalesEntry> getHsnList(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.ge("created_date", from_date),
				Restrictions.le("created_date", to_date),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("created_date"));
		return criteria.list();
	}

	@Override
	public List<SalesEntry> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId,
			Integer type) {
		/*List<Integer> salesTypes = new ArrayList<>(Arrays.asList(1,2));
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
        criteria.add(Restrictions.ge("created_date", from_date));
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.in("sale_type",salesTypes));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("created_date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
		List<Integer> salestypelist = new ArrayList<>();
		salestypelist.add(1);
		salestypelist.add(2);
		List<SalesEntry> purchaselist = new ArrayList<SalesEntry>();
		purchaselist.clear();
					Session session = getCurrentSession();
					Query query = session.createQuery("SELECT sales from SalesEntry sales "
							+ "WHERE sales.flag=:flag and sales.company.company_id =:company_id and sales.sale_type in (:sale_type) and sales.entry_status !=:entry_status and sales.created_date >=:date1 and sales.created_date <=:date2 order by sales.created_date desc");
					query.setParameter("company_id", companyId);
					query.setParameter("flag", true);
					query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
					query.setParameter("date1", from_date);
					query.setParameter("date2", to_date);
					query.setParameterList("sale_type", salestypelist);
					ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
					
					  while (scrollableResults.next()) {
						  purchaselist.add((SalesEntry)scrollableResults.get()[0]);
					   session.evict(scrollableResults.get()[0]);
				         }
					  session.clear();
				
		
		  return purchaselist;
	}

	@Override
	public List<SalesEntry> customerSalesHavingGST0(LocalDate from_date, LocalDate to_date, Long companyId) 
	{
		
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT salesentry from SalesEntry salesentry LEFT JOIN FETCH salesentry.company company "
					+"WHERE salesentry.company.company_id =:company_id and salesentry.flag =:flag and salesentry.created_date>=:from_date and salesentry.created_date<=:to_date "
					+ "and salesentry.customer.gst_applicable=:gst_applicable and salesentry.entry_status !=:entry_status and salesentry.cgst=:cgst and salesentry.igst=:igst and salesentry.sgst=:sgst");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", true);
			query.setParameter("cgst", (double)0);
			query.setParameter("igst", (double)0);
			query.setParameter("sgst", (double)0);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	public List<SalesEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate from_date, LocalDate to_date,
			Long companyId) {

		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT salesentry from SalesEntry salesentry LEFT JOIN FETCH salesentry.company company "
					+"WHERE salesentry.company.company_id =:company_id and salesentry.flag =:flag and salesentry.created_date>=:from_date and salesentry.created_date<=:to_date "
					+ "and salesentry.customer.gst_applicable=:gst_applicable and (salesentry.cgst>0 or salesentry.igst>0 or salesentry.sgst>0) ORDER by salesentry.created_date DESC,salesentry.sales_id DESC ");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	public List<SalesEntry> customerSalesWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT salesentry from SalesEntry salesentry LEFT JOIN FETCH salesentry.company company "
					+"WHERE salesentry.company.company_id =:company_id and salesentry.flag =:flag and salesentry.created_date>=:from_date and salesentry.created_date<=:to_date "
					+ "and salesentry.transaction_value>=:transaction_value and salesentry.tds_amount=:tds_amount and (salesentry.subledger.subledger_name LIKE '%Legal%' or salesentry.subledger.subledger_name LIKE '%Professional%' or salesentry.subledger.subledger_name LIKE '%Repairs & Maintenance%')");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("transaction_value",(double)30000);
			query.setParameter("tds_amount",(double)0);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	public List<SalesEntry> getSalesForLedgerReport(LocalDate from_date, LocalDate to_date, Long customerId,
			Long companyId) {
		List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		Query query = null;
		  if(customerId!=null) {
				
				if(customerId.equals((long)-1))
				{
		    		query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
							+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id IS NOT NULL and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC ");
				}
				else
				{
					query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
							+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id =:customer_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC");
					query.setParameter("customer_id", customerId);
					
				}
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("created_date1", from_date);
				query.setParameter("created_date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  saleslist.add((SalesEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		  return saleslist;
	}

	@Override
	public List<SalesEntry> getCardSalesForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
		            List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
					Session session = getCurrentSession();
					Query query =null;
					
					if(bank_id.equals((long)-4))
					{
						query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
							+ "WHERE salesEntry.flag=:flag and salesEntry.sale_type=:sale_type and salesEntry.company.company_id =:company_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC ");
					}
					else
					{
						query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
								+ "WHERE salesEntry.flag=:flag and salesEntry.sale_type=:sale_type and salesEntry.bank.bank_id=:bank_id and salesEntry.company.company_id =:company_id and salesEntry.entry_status !=:entry_status and salesEntry.created_date >=:created_date1 and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC ");
						query.setParameter("bank_id", bank_id);
					}
					query.setParameter("company_id", companyId);
					query.setParameter("flag", true);
					query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
					query.setParameter("created_date1", from_date);
					query.setParameter("created_date2", to_date);
					query.setParameter("sale_type", (int)2);
					ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
					  while (scrollableResults.next()) {
						  saleslist.add((SalesEntry)scrollableResults.get()[0]);
					   session.evict(scrollableResults.get()[0]);
				         }
					  session.clear();
					  return saleslist;
					  
				}

	@Override
	public List<SalesEntry> getOutwardSupplyListForGivenVocherRange(LocalDate from_date, LocalDate to_date,
			Long companyId, String vocherRange) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT salesentry from SalesEntry salesentry "
					+"WHERE salesentry.company.company_id =:company_id and salesentry.flag =:flag and salesentry.created_date>=:from_date and salesentry.created_date<=:to_date "
					+ "and voucher_no LIKE :voucher_no order by salesentry.sales_id asc");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("voucher_no", vocherRange);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	public Integer getCancelOutwardSupplyListForGivenVocherRange(LocalDate from_date, LocalDate to_date,
			Long companyId, String vocherRange) {
		
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
        criteria.add(Restrictions.ge("created_date", from_date));
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.like("voucher_no", vocherRange));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public SalesEntry isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (SalesEntry)criteria.uniqueResult();
	}
	
	
	

	@Override
	public List<SalesEntry> findAllSalesEntryOfCompanyForMobile(Long companyId, Boolean flag,Long yearId) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
					+"LEFT JOIN FETCH sales.bank bank "
					+"WHERE sales.company.company_id =:company_id and sales.flag =:flag and sales.accountingYear.year_id=:yearId ORDER by sales.created_date DESC,sales.sales_id DESC");
			query.setParameter("company_id", companyId);
			query.setParameter("flag", flag);
			query.setParameter("yearId", yearId);
			query.setMaxResults(10);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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
	public List<SalesEntry> getAllOpeningBalanceAgainstSales(Long customerId, Long companyId) {
Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);
		
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	

		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	@Override
	public List<SalesEntry> getAllSalesAmount(Long customerId, Long companyId,LocalDate toDate) {
Criteria criteria = getCurrentSession().createCriteria(SalesEntry.class);

List<SalesEntry> saleslist = new ArrayList<SalesEntry>();
Session session = getCurrentSession();
Query query = null;
  if(customerId!=null) {
		
		if(customerId.equals((long)-1))
		{
    		query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
					+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id IS NOT NULL and salesEntry.entry_status !=:entry_status and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC ");
		}
		else
		{
			query = session.createQuery("SELECT salesEntry from SalesEntry salesEntry LEFT JOIN FETCH salesEntry.bank bank "
					+ "WHERE salesEntry.flag=:flag and salesEntry.company.company_id =:company_id and salesEntry.customer.customer_id =:customer_id and salesEntry.entry_status !=:entry_status  and salesEntry.created_date <=:created_date2 order by salesEntry.created_date ASC,salesEntry.sales_id ASC");
			query.setParameter("customer_id", customerId);
			
		}
		query.setParameter("company_id", companyId);
		query.setParameter("flag", true);
		query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		
		query.setParameter("created_date2", toDate);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  saleslist.add((SalesEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
  }
  return saleslist;

		//System.out.println("customerId" +customerId);
		//System.out.println("company_id" +companyId);
		//Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		/*Criterion criterion2 = Restrictions.eq("customer.customer_id",customerId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion4 = Restrictions.le("created_date",toDate);	
		Criterion criterion5= Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL);
		Criterion criterion6= Restrictions.eq("flag", true);
		criteria.add(Restrictions.and(criterion2,criterion3,criterion4,criterion5,criterion6));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);*/ // commented by Argents to get all sales record for option All customer and selected customer 
	//	System.out.println("salesentry results"+criteria.list().size());
	//	return criteria.list();
	}
	@Override
	public List<SalesEntry> findAllDisableSalesEntryOfCompanyAfterImport(Long CompanyId, Boolean flag) {
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT sales from SalesEntry sales LEFT JOIN FETCH sales.company company "
					+"LEFT JOIN FETCH sales.bank bank "
					+"WHERE sales.company.company_id =:company_id and sales.flag =:flag and sales.entry_status=:entry_status ORDER by sales.created_date DESC,sales.sales_id DESC");
			query.setParameter("company_id", CompanyId);
			query.setParameter("flag", flag);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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

	@SuppressWarnings("unchecked")
    @Override
    public List<SalesEntry> findExcelVoucherNumber(long companyid) {
        
        List<SalesEntry> list =  new ArrayList<SalesEntry>();
        Session session = getCurrentSession();
        
        Query query = session.createQuery("from SalesEntry WHERE company.company_id=:companyid");
              query.setParameter("companyid", companyid);
          list = query.list();
        
        
        return list;
    }

	@Override
	public List<SalesEntry> salesEntryWithEditedGSTvalues(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		List<SalesEntry> list =  new ArrayList<SalesEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT salesentry from SalesEntry salesentry  JOIN FETCH salesentry.products "
					+" WHERE salesentry.company.company_id =:company_id and salesentry.entry_status!=:entry_status and salesentry.flag =:flag and salesentry.created_date>=:from_date and salesentry.created_date<=:to_date "
					+ "  ");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
		//	query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((SalesEntry)scrollableResults.get()[0]);
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


}