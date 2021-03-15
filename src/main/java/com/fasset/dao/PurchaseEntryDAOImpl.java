/**
 * mayur suramwar
 */
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Company;
import com.fasset.entities.Payment;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.Stock;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProductInformation;
import com.fasset.service.interfaces.IStockService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.ISuplliersService;
import com.sun.prism.RTTexture;

/**
 * @author mayur suramwar
 *
 *         deven infotech pvt ltd.
 */
@Transactional
@Repository
public class PurchaseEntryDAOImpl extends AbstractHibernateDao<PurchaseEntry> implements IPurchaseEntryDAO {

	@Autowired
	private IStockService stockService;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private IProductDAO productDAO;

	@Autowired
	ISuplliersService supplierService;

	@Autowired
	ISubLedgerService subledgerService;

	@Autowired
	private IOpeningBalancesDAO balanceDao;
	
	@Override
	public Company findCompanyDao(Long user_id) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", user_id));
		User user = (User) criteria.uniqueResult();
		Company company = user.getCompany();
		return company;
	}

	@Override
	public List<Suppliers> findAllSuppliers(Long company_id) {
		return null;
	}
	
	@Override
	public Long savePurchaseEntryDao(PurchaseEntry sup) {
		Session session = getCurrentSession();
		List<ProductInformation> informationList = new ArrayList<ProductInformation>();
		informationList = sup.getInformationList();
		sup.setLocal_time(new LocalTime());
		Long id = (Long) session.save(sup);
		
		
		try {
			PurchaseEntry newId=findOne(id);
			
		} catch (MyWebException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		session.flush();
		session.clear();
		Long company_id = sup.getCompany().getCompany_id();
		for (int i = 0; i < informationList.size(); i++) {
			ProductInformation information = informationList.get(i);
			Query query = session.createQuery(
					"update PurchaseEntryProductEntityClass set quantity=:quantity,rate=:rate,discount=:discount,"
							+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
							+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,product_name=:product_name,VAT=:VAT,VATCST=:VATCST,Excise=:Excise,is_gst=:is_gst "
							+ "where purchase_id =:pid and product_id=:proid");
			query.setParameter("pid", id);
			query.setParameter("proid", Long.parseLong(information.getProductId()));
			query.setParameter("quantity", Double.parseDouble(information.getQuantity()));
			query.setParameter("rate", Double.parseDouble(information.getRate()));
			query.setParameter("discount", Double.parseDouble(information.getDiscount()));
			query.setParameter("CGST", Double.parseDouble(information.getCGST()));
			query.setParameter("IGST", Double.parseDouble(information.getIGST()));
			query.setParameter("SGST", Double.parseDouble(information.getSGST()));
			query.setParameter("SCT", Double.parseDouble(information.getSCT()));
			query.setParameter("labourCharge", Double.parseDouble(information.getLabourCharge()));
			query.setParameter("freight", Double.parseDouble(information.getFreightCharges()));
			query.setParameter("transaction_amount", Double.parseDouble(information.getTransaction_amount()));
			query.setParameter("Others", Double.parseDouble(information.getOthers()));
			query.setParameter("UOM", information.getUnit());
			query.setParameter("HSNCode", information.getHsncode());
			query.setParameter("product_name", information.getProductname());
			query.setParameter("VAT", Double.parseDouble(information.getVAT()));
			query.setParameter("VATCST", Double.parseDouble(information.getVATCST()));
			query.setParameter("Excise", Double.parseDouble(information.getExcise()));
			query.setParameter("is_gst", Long.parseLong(information.getIs_gst()));

			try {
				query.executeUpdate();
				
			} catch (Exception e) {
				e.printStackTrace();
				

			}
			if(!information.getProductname().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
			{
			Integer pflag = productDAO.checktype(company_id, Long.parseLong(information.getProductId()));
			if (pflag == 1) {
				Stock stock = null;
				
				stock = stockService.isstockexist(company_id, Long.parseLong(information.getProductId()));
			    double amt=Double.parseDouble(information.getQuantity())*Double.parseDouble(information.getRate());
				if (stock == null) {
					Stock stockdata = new Stock();
					stockdata.setCompany_id(company_id);
					stockdata.setProduct_id(Long.parseLong(information.getProductId()));
					stockdata.setQuantity(Double.parseDouble(information.getQuantity()));
					stockdata.setAmount(amt);
					Long stockid = stockService.saveStock(stockdata);
					stockService.addStockInfoOfProduct(stockid, company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()), Double.parseDouble(information.getRate()));
				} else {
					stock.setAmount(stock.getAmount()+amt);
					stock.setQuantity(stock.getQuantity() + Double.parseDouble(information.getQuantity()));
					stockService.updateStock(stock);
					stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()), Double.parseDouble(information.getRate()));
				}
			}
			}
		}
		return id;
	}

	@Override
	public void update(PurchaseEntry sup) throws MyWebException {
		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("purchase_id", sup.getPurchase_id()));
		criteria.setFetchMode("products", FetchMode.JOIN);
		PurchaseEntry entity = (PurchaseEntry) criteria.uniqueResult();
		
		Long company_id = entity.getCompany().getCompany_id();
		
		if(sup.getUpdated_by()!=null)
		{
			entity.setUpdated_by(sup.getUpdated_by());
		}
		if(sup.getSupplier()!=null)
		{
		entity.setSupplier(sup.getSupplier());
		}
		if(sup.getSupplier_bill_date()!=null)
		{
			entity.setSupplier_bill_date(sup.getSupplier_bill_date());
		}
		if(sup.getSupplier_bill_no()!=null)
		{
			entity.setSupplier_bill_no(sup.getSupplier_bill_no());
		}
		if(sup.getGrn_date()!=null)
		{
			entity.setGrn_date(sup.getGrn_date());
		}
		if(sup.getSubledger()!=null)
		{
			entity.setSubledger(sup.getSubledger());
		}
		if(sup.getVoucher_date()!=null)
		{
			entity.setVoucher_date(sup.getVoucher_date());
		}
		if(sup.getRemark()!=null)
		{
			entity.setRemark(sup.getRemark());
		}
		if(sup.getEntrytype()!=null)
		{
			entity.setEntrytype(sup.getEntrytype());
		}
		if(sup.getEntry_status()!=null)
		{
			entity.setEntry_status(sup.getEntry_status());
		}
		if(sup.getAdvpayment()!=null)
		{
			entity.setAdvpayment(sup.getAdvpayment());
		}
		
		
		
		entity.setFlag(true);
		
		if (sup.getEntrytype() == 2) {
			
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
		} else {
			entity.setShipping_bill_no(null);
			entity.setExport_type(null);
			entity.setShipping_bill_date(null);
			entity.setPort_code(null);
		}

		if (sup.getInformationList() != null && sup.getInformationList().size()>0) {
			Set<Product> products = new HashSet<Product>();
			Set<Product> newproducts = new HashSet<Product>();
			List<ProductInformation> informationList = new ArrayList<ProductInformation>();
			informationList = sup.getInformationList();
			products = entity.getProducts();
			newproducts = sup.getProducts();
			Boolean flag = false;
			for (Product product : newproducts) {

				for (Product lastproducts : products) {
					if (product.equals(lastproducts)) {
						flag = true;
					}
				}

				if (flag == false) {
					products.add(product);
					entity.setTransaction_value(sup.getTransaction_value());
					entity.setCgst(sup.getCgst());
					entity.setSgst(sup.getSgst());
					entity.setIgst(sup.getIgst());
					entity.setState_compansation_tax(sup.getState_compansation_tax());
					entity.setRound_off(sup.getRound_off());
					if(sup.getTds_amount()!=null)
					{
					entity.setTds_amount(sup.getTds_amount());
					}
					entity.setTotal_vat(sup.getTotal_vat());
					entity.setTotal_excise(sup.getTotal_excise());
					entity.setTotal_vatcst(sup.getTotal_vatcst());
				}
			}
			entity.setProducts(products);
			entity.setUpdate_date(new LocalDateTime());
			session.merge(entity);
			session.flush();
			session.clear();
			if (flag == false) {
				for (int i = 0; i < informationList.size(); i++) {
					ProductInformation information = informationList.get(i);
					Query query = session.createQuery(
							"update PurchaseEntryProductEntityClass set quantity=:quantity,rate=:rate,discount=:discount,"
									+ "CGST=:CGST,IGST=:IGST,SGST=:SGST,state_com_tax=:SCT,labour_charges=:labourCharge,"
									+ "freight=:freight,transaction_amount=:transaction_amount,Others=:Others,UOM=:UOM,HSNCode=:HSNCode,product_name=:product_name,VAT=:VAT,VATCST=:VATCST,Excise=:Excise,is_gst=:is_gst "
									+ "where purchase_id =:pid and product_id=:proid");
					
					query.setParameter("pid", sup.getPurchase_id());
					query.setParameter("proid", Long.parseLong(information.getProductId()));
					query.setParameter("quantity", Double.parseDouble(information.getQuantity()));
					query.setParameter("rate", Double.parseDouble(information.getRate()));
					query.setParameter("discount", Double.parseDouble(information.getDiscount()));
					query.setParameter("CGST", Double.parseDouble(information.getCGST()));
					query.setParameter("IGST", Double.parseDouble(information.getIGST()));
					query.setParameter("SGST", Double.parseDouble(information.getSGST()));
					query.setParameter("SCT", Double.parseDouble(information.getSCT()));
					query.setParameter("labourCharge", Double.parseDouble(information.getLabourCharge()));
					query.setParameter("freight", Double.parseDouble(information.getFreightCharges()));
					query.setParameter("transaction_amount", Double.parseDouble(information.getTransaction_amount()));
					query.setParameter("Others", Double.parseDouble(information.getOthers()));
					query.setParameter("UOM", information.getUnit());
					query.setParameter("HSNCode", information.getHsncode());
					query.setParameter("product_name", information.getProductname());
				      query.setParameter("is_gst",Long.parseLong(information.getIs_gst()));
				      query.setParameter("VAT", Double.parseDouble(information.getVAT()));
				      query.setParameter("VATCST", Double.parseDouble(information.getVATCST()));
				      query.setParameter("Excise", Double.parseDouble(information.getExcise()));

					try {
						query.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();

					}
					
					if(!information.getProductname().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					{
					Integer pflag = productDAO.checktype(company_id, Long.parseLong(information.getProductId()));
					if (pflag == 1) {
						Stock stock = null;
						stock = stockService.isstockexist(company_id, Long.parseLong(information.getProductId()));
					    double amt=Double.parseDouble(information.getQuantity())*Double.parseDouble(information.getRate());
						if (stock == null) {
							Stock stockdata = new Stock();
							stockdata.setCompany_id(company_id);
							stockdata.setProduct_id(Long.parseLong(information.getProductId()));
							stockdata.setQuantity(Double.parseDouble(information.getQuantity()));
							stockdata.setAmount(amt);
							Long stockid = stockService.saveStock(stockdata);
							stockService.addStockInfoOfProduct(stockid, company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()), Double.parseDouble(information.getRate()));
						} else {
							stock.setAmount(stock.getAmount()+amt);
							stock.setQuantity(stock.getQuantity() + Double.parseDouble(information.getQuantity()));
							stockService.updateStock(stock);
							stockService.addStockInfoOfProduct(stock.getStock_id(), company_id, Long.parseLong(information.getProductId()), Double.parseDouble(information.getQuantity()), Double.parseDouble(information.getRate()));
						}
					}
					}
				}

			}

		} else {
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
	public PurchaseEntry findOne(Long id) throws MyWebException {
		System.out.println("The findone id is"+id);
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("purchase_id", id));
		return (PurchaseEntry) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from PurchaseEntry order by purchase_id desc");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntryProductEntityClass> findAllPurchaseEntryProductEntityList(Long entryId) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from PurchaseEntryProductEntityClass where purchase_id=:entryId");
		query.setParameter("entryId", entryId);
		return query.list();
	}

	@Override
	public String deletePurchaseEntryProduct(Long PEId, Long purId, Long Company_id) {

		Double transaction_value = (double)0;
		Double cgst = (double)0;
		Double igst = (double)0;
		Double sgst = (double)0;
		Double vat = (double)0;
		Double vatcst = (double)0;
		Double tds=(double)0;
		Double excise = (double)0;
		Double state_comp_tax = (double)0;
		Double round_off = (double)0;
		Double CGST = (double)0;
		Double IGST = (double)0;
		Double SGST = (double)0;
		Double state_com_tax = (double)0;
		Double transaction_amount = (double)0;
		Double roundamount = (double)0;
		Double VAT = (double)0;
		Double VATCST = (double)0;
		Double Excise = (double)0;
		Double tdsnew=(double)0;
		Suppliers supplier1 = new Suppliers();
		SubLedger subledger1 = new SubLedger();

		Session session = getCurrentSession();

		Criteria criteria = session.createCriteria(PurchaseEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("purchase_detail_id", PEId));
		PurchaseEntryProductEntityClass purchasedetails = (PurchaseEntryProductEntityClass) criteria.uniqueResult();

		Criteria criteria1 = session.createCriteria(PurchaseEntry.class);
		criteria1.add(Restrictions.eq("purchase_id", purId));
		PurchaseEntry purchase = (PurchaseEntry) criteria1.uniqueResult();
		Integer tdsapply=purchase.getSupplier().getTds_applicable();
		Double tdsrate=(double)purchase.getSupplier().getTds_rate();
		if (purchase.getSupplier() != null) {
			supplier1 = purchase.getSupplier();
		}
		if (purchase.getSubledger() != null) {
			subledger1 = purchase.getSubledger();
		}

		if (purchase.getTransaction_value() != null) {
			transaction_value = purchase.getTransaction_value();
		}
		if (purchase.getRound_off() != null) {
			round_off = purchase.getRound_off();
		}
		if (purchase.getCgst() != null) {
			cgst = purchase.getCgst();
		}
		if (purchase.getIgst() != null) {
			igst = purchase.getIgst();
		}
		if (purchase.getSgst() != null) {
			sgst = purchase.getSgst();
		}
		if (purchase.getState_compansation_tax() != null) {
			state_comp_tax = purchase.getState_compansation_tax();
		}

		if (purchase.getTotal_vat() != null) {
			vat = purchase.getTotal_vat();
		}

		if (purchase.getTotal_vatcst() != null) {
			vatcst = purchase.getTotal_vatcst();
		}
		if (purchase.getTotal_excise() != null) {
			excise = purchase.getTotal_excise();
		}
		if(purchase.getTds_amount()!=null)
		{
			tds=purchase.getTds_amount();
		}
		if (purchasedetails.getCGST() != null) {
			CGST = purchasedetails.getCGST();
		}
		if (purchasedetails.getIGST() != null) {
			IGST = purchasedetails.getIGST();
		}
		if (purchasedetails.getSGST() != null) {
			SGST = purchasedetails.getSGST();
		}
		if (purchasedetails.getState_com_tax() != null) {
			state_com_tax = purchasedetails.getState_com_tax();
		}
		if (purchasedetails.getTransaction_amount() != null) {
			transaction_amount = purchasedetails.getTransaction_amount();
		}

		if (purchasedetails.getVAT() != null) {
			VAT = purchasedetails.getVAT();
		}
		if (purchasedetails.getVATCST() != null) {
			VATCST = purchasedetails.getVATCST();
		}
		if (purchasedetails.getExcise() != null) {
			Excise = purchasedetails.getExcise();
		}

		if (VAT == 0 && VATCST == 0 && Excise == 0) {
			cgst = cgst - CGST;
			igst = igst - IGST;
			sgst = sgst - SGST;
			state_comp_tax = state_comp_tax - state_com_tax;
			if(tdsapply==1)
			{
				tdsnew=(transaction_amount*tdsrate)/100;
			}
			else
				tdsnew=(double)0;
			
			transaction_value = transaction_value - transaction_amount;
			roundamount = CGST + IGST + SGST + state_com_tax + transaction_amount;
			
			tds=tds-tdsnew;
			round_off = round_off - roundamount + tdsnew;
			
			purchase.setCgst(cgst);
			purchase.setIgst(igst);
			purchase.setSgst(sgst);
			purchase.setState_compansation_tax(state_comp_tax);
			purchase.setTransaction_value(transaction_value);
			purchase.setTds_amount(tds);
			purchase.setRound_off(round_off);

		} else {
			vat = vat - VAT;
			vatcst = vatcst - VATCST;
			excise = excise - Excise;
			
			if(tdsapply==1)
			{
				tdsnew=(transaction_amount*tdsrate)/100;
			}
			else
				tdsnew=(double)0;
			
			// calculate transaction and round off value,
			transaction_value = transaction_value - transaction_amount;
			roundamount = VAT + VATCST + Excise + transaction_amount;
		
			tds=tds-tdsnew;
			round_off = round_off - roundamount + tdsnew;
			purchase.setTotal_vat(vat);
			purchase.setTotal_vatcst(vatcst);
			purchase.setTotal_excise(excise);
			purchase.setTransaction_value(transaction_value);
			purchase.setTds_amount(tds);
			purchase.setRound_off(round_off);
		}
	

         if(purchase.getAgainst_advance_Payment()!=null && purchase.getAgainst_advance_Payment()==false)
         {
        	SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", purchase.getCompany().getCompany_id());
        	if(subledgertds!=null)
        	{
			 subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)tdsnew, (double)0, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
        	}
         }
		if (VAT == 0 && VATCST == 0 && Excise == 0) {
			try {

				if (supplier1 != null) {
					supplierService.addsuppliertrsansactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, supplier1.getSupplier_id(), (long) 4, (double)(roundamount-tdsnew), (double)0, (long) 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				if (subledger1 != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledger1.getSubledger_Id(), (long) 2, (double)0, (double)transaction_amount, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (CGST != null && CGST > 0) {
					SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST", Company_id);

					if (subledgercgst != null) {
						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)CGST, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
					}
				}

				if (SGST != null && SGST > 0) {
					SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST", Company_id);

					if (subledgersgst != null) {
						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)SGST, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);

					}
				}

				if (IGST != null && IGST > 0) {
					SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST", Company_id);

					if (subledgerigst != null) {
						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)IGST, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);

					}
				}

				if (state_com_tax != null && state_com_tax > 0) {
					SubLedger subledgercess = subLedgerDAO.findOne("Input CESS", Company_id);

					if (subledgercess != null) {

						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgercess.getSubledger_Id(), (long) 2, (double)0, (double)state_com_tax, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {

				if (VAT != null && VAT > 0) {
					SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", Company_id);
					if (subledgervat != null) {

						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)VAT, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
					}
				}

				if (VATCST != null && VATCST > 0) {
					SubLedger subledgercst = subLedgerDAO.findOne("Input CST", Company_id);

					if (subledgercst != null) {
						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)VATCST, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
					}
				}
				if (Excise != null && Excise > 0) {
					SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE", Company_id);

					if (subledgerexcise != null) {

						subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),Company_id, subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)Excise, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		if(!purchasedetails.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
		{
	Integer pflag = productDAO.checktype(purchase.getCompany().getCompany_id(),
			purchasedetails.getProduct_id());
	if (pflag == 1) {
		Stock stock = null;
		stock = stockService.isstockexist(purchase.getCompany().getCompany_id(),
				purchasedetails.getProduct_id());
		if (stock != null) {
			
			double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), purchase.getCompany().getCompany_id(), purchasedetails.getProduct_id(), purchasedetails.getQuantity());
			stock.setAmount(stock.getAmount()-amount);
			stock.setQuantity(stock.getQuantity() - purchasedetails.getQuantity());
			stockService.updateStock(stock);
		}
	       }
		}
		
		Query query = session.createQuery("delete from PurchaseEntryProductEntityClass where purchase_detail_id =:PEId");
		query.setParameter("PEId", PEId);
		String msg = "";
		try {
			query.executeUpdate();
			session.update(purchase);
			msg = "Product Deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Product";
		}
		return msg;
	}

	@Override
	public PurchaseEntry isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CopyOnWriteArrayList<PurchaseEntry> getReport(Long Id, LocalDate from_date, LocalDate to_date, Long comId) {

		CopyOnWriteArrayList<PurchaseEntry> list = new CopyOnWriteArrayList<>();
		/*Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.ge("supplier_bill_date", from_date));
		criteria.add(Restrictions.le("supplier_bill_date", to_date));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", comId));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		list1 =  criteria.list();*/
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		purchaselist.clear();
					Session session = getCurrentSession();
					Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
					query.setParameter("company_id", comId);
					query.setParameter("flag", true);
					query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
					query.setParameter("date1", from_date);
					query.setParameter("date2", to_date);
					ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
					
					  while (scrollableResults.next()) {
						  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
					   session.evict(scrollableResults.get()[0]);
				         }
					  session.clear();
		
		for(PurchaseEntry entry : purchaselist)
		{
			list.add(entry);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> findAllPurchaseEntryOfCompany(Long CompanyId, Boolean flag) {
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase LEFT JOIN FETCH purchase.company company "
				+"WHERE purchase.company.company_id =:company_id and purchase.flag =:flag ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
		query.setParameter("company_id", CompanyId);
		/* query.setParameter("yearID", yearID); */ /* and purchase.accountingYear.year_id=:yearID */
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((PurchaseEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
		  }
		  session.clear();
		  return list;
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
		Double round_off = (double)0;
		//Double tdsnew=(double)0;
		Double tds=(double)0;
		Suppliers supplier1 = null;
		SubLedger subledger1 = null;

		Session session = getCurrentSession();
		Criteria criteria1 = session.createCriteria(PurchaseEntry.class);
		criteria1.add(Restrictions.eq("purchase_id", entityId));
		criteria1.setFetchMode("company", FetchMode.JOIN);
		PurchaseEntry purchase = (PurchaseEntry) criteria1.uniqueResult();
		//Integer tdsapply=purchase.getSupplier().getTds_applicable();
		//Double tdsrate=purchase.getSupplier().getTds_rate();
		Criteria criteria = session.createCriteria(PurchaseEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("purchase_id", entityId));
		@SuppressWarnings("unchecked")
		List<PurchaseEntryProductEntityClass> purchasedetails = criteria.list();
		
		for (PurchaseEntryProductEntityClass purchaseproduct : purchasedetails) {
			
			if(purchaseproduct!=null)
			{
				if(!purchaseproduct.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
				{
			Integer pflag = productDAO.checktype(purchase.getCompany().getCompany_id(),
					purchaseproduct.getProduct_id());
			if (pflag == 1) {
				Stock stock = null;
				stock = stockService.isstockexist(purchase.getCompany().getCompany_id(),
						purchaseproduct.getProduct_id());
				if (stock != null) {
					
					double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), purchase.getCompany().getCompany_id(), purchaseproduct.getProduct_id(), purchaseproduct.getQuantity());
					stock.setAmount(stock.getAmount()-amount);
					stock.setQuantity(stock.getQuantity() - purchaseproduct.getQuantity());
					stockService.updateStock(stock);
				}
			       }
				}
			
		  }
		}
		
		
		if (purchase.getSupplier() != null) {
			supplier1 = purchase.getSupplier();
		}
		if (purchase.getSubledger() != null) {
			subledger1 = purchase.getSubledger();
		}

		if (purchase.getTransaction_value() != null) {
			transaction_value = purchase.getTransaction_value();
		}
		if (purchase.getRound_off() != null) {
			round_off = purchase.getRound_off();
		}
		if(tds!=null)
		{
			tds=purchase.getTds_amount();
		}
		if (purchase.getCgst() != null && purchase.getCgst() > 0) {
			cgst = purchase.getCgst();
		}
		if (purchase.getIgst() != null && purchase.getIgst() > 0) {
			igst = purchase.getIgst();
		}
		if (purchase.getSgst() != null && purchase.getSgst() > 0) {
			sgst = purchase.getSgst();
		}
		if (purchase.getState_compansation_tax() != null && purchase.getState_compansation_tax() > 0) {
			state_comp_tax = purchase.getState_compansation_tax();
		}
		if (purchase.getTotal_vat() != null && purchase.getTotal_vat() > 0) {
			vat = purchase.getTotal_vat();
		}
		if (purchase.getTotal_vatcst() != null && purchase.getTotal_vatcst() > 0) {
			vatcst = purchase.getTotal_vatcst();
		}
		if (purchase.getTotal_excise() != null && purchase.getTotal_excise() > 0) {
			excise = purchase.getTotal_excise();
		}

		try {

			if (supplier1 != null) {
				supplierService.addsuppliertrsansactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
						supplier1.getSupplier_id(), (long) 4, (double)round_off, (double)0, (long) 0);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			if (subledger1 != null) {
				subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(), subledger1.getSubledger_Id(), (long) 2, (double)0, (double)transaction_value, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (purchase.getAgainst_advance_Payment()!=null && purchase.getAgainst_advance_Payment()==false) {
				SubLedger subledgertds = subLedgerDAO.findOne("TDS Payable", purchase.getCompany().getCompany_id());
				subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(), subledgertds.getSubledger_Id(), (long) 2, (double)tds, (double)0, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
			}
			if (cgst != null && cgst > 0) {
				SubLedger subledgercgst = subLedgerDAO.findOne("Input CGST", purchase.getCompany().getCompany_id());
				if (subledgercgst != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgercgst.getSubledger_Id(), (long) 2, (double)0, (double)cgst, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

			if (sgst != null && sgst > 0) {
				SubLedger subledgersgst = subLedgerDAO.findOne("Input SGST", purchase.getCompany().getCompany_id());
				if (subledgersgst != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgersgst.getSubledger_Id(), (long) 2, (double)0, (double)sgst, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

			if (igst != null && igst > 0) {
				SubLedger subledgerigst = subLedgerDAO.findOne("Input IGST", purchase.getCompany().getCompany_id());
				if (subledgerigst != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgerigst.getSubledger_Id(), (long) 2, (double)0, (double)igst, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

			if (state_comp_tax != null && state_comp_tax > 0) {
				SubLedger subledgercess = subLedgerDAO.findOne("Input CESS", purchase.getCompany().getCompany_id());
				if (subledgercess != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgercess.getSubledger_Id(), (long) 2, (double)0, (double)state_comp_tax, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

			if (vat != null && vat > 0) {
				SubLedger subledgervat = subLedgerDAO.findOne("Input VAT", purchase.getCompany().getCompany_id());
				if (subledgervat != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgervat.getSubledger_Id(), (long) 2, (double)0, (double)vat, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

			if (vatcst != null && vatcst > 0) {
				SubLedger subledgercst = subLedgerDAO.findOne("Input CST", purchase.getCompany().getCompany_id());
				if (subledgercst != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgercst.getSubledger_Id(), (long) 2, (double)0, (double)vatcst, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}
			if (excise != null && excise > 0) {
				SubLedger subledgerexcise = subLedgerDAO.findOne("Input EXCISE", purchase.getCompany().getCompany_id());
				if (subledgerexcise != null) {
					subledgerService.addsubledgertransactionbalance(purchase.getAccountingYear().getYear_id(),purchase.getSupplier_bill_date(),purchase.getCompany().getCompany_id(),
							subledgerexcise.getSubledger_Id(), (long) 2, (double)0, (double)excise, (long) 0,null,null,purchase,null,null,null,null,null,null,null,null,null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Query query1 = session.createQuery("delete from PurchaseEntryProductEntityClass where purchase_id =:purchase_id");
		query1.setParameter("purchase_id", entityId);
		query1.executeUpdate();

		balanceDao.deleteOpeningBalance(null,null, null, entityId, null, null,null,null, null,null,null,null);
		Query query = session.createQuery("delete from PurchaseEntry where purchase_id =:id");
		query.setParameter("id", entityId);
		String msg = "";
		try {
			query.executeUpdate();
			msg = "Purchase entry deleted successfully";
		} catch (Exception e) {
			msg = "You can't delete Purchase Entry";
		}

		return msg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> findAllBySuppliers(Long supplierId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("supplier.supplier_id", supplierId),
				Restrictions.eq("company.company_id", companyId)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> getBillsPayable(Long supplierId, LocalDate fromDate, LocalDate toDate, Long companyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.ge("supplier_bill_date", fromDate));
		criteria.add(Restrictions.le("supplier_bill_date", toDate));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		if (supplierId > 0) {
			criteria.add(Restrictions.eq("supplier.supplier_id", supplierId));
		}
		return criteria.list();*/
		//Theis is metod
		System.out.println("The frm dt is "+fromDate);
		System.out.println("The to dt is "+toDate);
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		purchaselist.clear();
		Session session = getCurrentSession();
		Query query =null;
		  if(supplierId!=null) {
				
			    if(supplierId > 0)
				{
					
					query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.againstOpeningBalnce =:againstOpeningBalnce and purchase.company.company_id =:company_id and purchase.supplier.supplier_id=:supplier_id and purchase.entry_status =:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
					query.setParameter("supplier_id", supplierId);
				}
				else
				{
					query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.againstOpeningBalnce =:againstOpeningBalnce and purchase.company.company_id =:company_id and purchase.supplier.supplier_id IS NOT NULL and purchase.entry_status =:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
				}
			    query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("againstOpeningBalnce", false);
			    query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
				query.setParameter("date1", fromDate);
				query.setParameter("date2", toDate);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		  return purchaselist;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> getB2BList(Integer month, Long yearId, Long companyId) {

		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.createAlias("supplier", "sup");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("sup.gst_applicable", true), Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> getB2CLList(Integer month, Long yearId, Long companyId, Long stateId) {

		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.createAlias("supplier", "sup");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.not(Restrictions.eq("sup.state.state_id", stateId)),
				Restrictions.gt("round_off", (double) 250000), 
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> getB2CSList(Integer month, Long yearId, Long companyId, Long stateId) {

		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.createAlias("supplier", "sup");
		Criterion c1 = Restrictions.not(Restrictions.eq("sup.state.state_id", stateId));
		Criterion c2 = Restrictions.lt("round_off", (double) 250000);
		Criterion c3 = Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month);
		Criterion c4 = Restrictions.eq("accountingYear.year_id", yearId);
		Criterion c5 = Restrictions.eq("company.company_id", companyId);
		Criterion c6 = Restrictions.and(c1, c2, c3, c4, c5,Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));

		Criterion c7 = Restrictions.eq("company.company_id", companyId);
		Criterion c8 = Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month);
		Criterion c9 = Restrictions.eq("accountingYear.year_id", yearId);
		Criterion c10 = Restrictions.eq("sup.state.state_id", stateId);
		Criterion c11 = Restrictions.and(c7, c8, c9, c10,Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));

		criteria.add(Restrictions.or(c11, c6));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseEntry> getExpList(Integer month, Long yearId, Long companyId, Long countryId) {

		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.createAlias("supplier", "sup");
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.not(Restrictions.eq("sup.country.country_id", countryId)),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		return criteria.list();
	}

	public Integer getCountByYear(Long yearId, Long companyId, String range) {
		Session session = getCurrentSession();
		Query query = session.createSQLQuery("select count(purchase_id)from purchase_Entry where company_id =:company_id and accounting_year_id =:year_id and voucher_no LIKE :term");
		query.setParameter("company_id", companyId);
		query.setParameter("year_id", yearId);
		query.setParameter("term", range + "%");
		
		if (query.list() == null || query.list().isEmpty()) {
			return 0;
		} else {
			
			if (query.list().get(0) == null)
				return 0;
			else {
				Integer vid = ((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}
	}

	@Override
	public PurchaseEntry findOneWithAll(Long purId) {

		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("purchase_id", purId));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("accountingYear", FetchMode.JOIN);
		criteria.setFetchMode("products", FetchMode.JOIN);
		return (PurchaseEntry) criteria.uniqueResult();
	}

	@Override
	public PurchaseEntry findOneWithSuppilerAndSublegder(Long purId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("purchase_id", purId));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("advpayment", FetchMode.JOIN);
		return (PurchaseEntry) criteria.uniqueResult();
	}

	@Override
	public Long savePurchaseEntryThroughExcel(PurchaseEntry entry) {
		Session session = getCurrentSession();
		Long id=(Long) session.save(entry);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public Long savePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry) {

		Session session = getCurrentSession();
		Long id=(Long) session.save(entry);
		session.flush();
		session.clear();
		return id;
	}

	@Override
	public void updatePurchaseEntryThroughExcel(PurchaseEntry entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();
	}

	@Override
	public void updatePurchaseEntryProductEntityClassThroughExcel(PurchaseEntryProductEntityClass entry) {
		Session session = getCurrentSession();
		session.save(entry);
		session.flush();
		session.clear();

	}

	@Override
	public List<PurchaseEntry> findAllPurchaseEntriesOfCompany(Long CompanyId,Boolean importFunction) {
		
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		if(importFunction==true) /**for import we need only that records which are having excel_voucher_no IS NOT NULL to skip all entries which are added through system*/
		{
		Query query = session.createQuery("SELECT pentry from PurchaseEntry pentry LEFT JOIN FETCH pentry.company company "
				+"WHERE pentry.company.company_id =:company_id and excel_voucher_no IS NOT NULL");
		query.setParameter("company_id", CompanyId);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((PurchaseEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		}
		else if(importFunction==false)/**for other cases we need all records with flag=true i.e it should be from success list or it should be added through system not by import.*/
		{
			Query query = session.createQuery("SELECT pentry from PurchaseEntry pentry LEFT JOIN FETCH pentry.company company "
					+"WHERE pentry.company.company_id =:company_id and pentry.flag =:flag");
			query.setParameter("company_id", CompanyId);
			query.setParameter("flag", true);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PurchaseEntry)scrollableResults.get()[0]);
			   session.evict(scrollableResults.get()[0]);
		         }
		}
		  Collections.sort(list, new Comparator<PurchaseEntry>() {
	            public int compare(PurchaseEntry purc1, PurchaseEntry purc2) {
	                Long purchase_id1 = new Long(purc1.getPurchase_id());
	                Long purchase_id2 = new Long(purc2.getPurchase_id());
	                return purchase_id2.compareTo(purchase_id1);
	            }
	        });
		  return list;
	}

	public int getCountByDate(Long companyId, String range, LocalDate date) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("supplier_bill_date", date));
		criteria.addOrder(Order.desc("purchase_id"));
		criteria.setMaxResults(1);
		PurchaseEntry entry = (PurchaseEntry) criteria.uniqueResult();
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
		
		
		/*Query query = session.createSQLQuery(
				"select count(purchase_id)from purchase_Entry where company_id =:company_id and supplier_bill_date ='"
						+ date + "'");
		query.setParameter("company_id", companyId);
		// query.setParameter("supplier_bill_date", date);

		if (query.list() == null || query.list().isEmpty()) {
			return 0;
		} else {
			System.out.println(query.list().get(0));
			if (query.list().get(0) == null)
				return 0;
			else {
				Integer vid = ((BigInteger) query.list().get(0)).intValue();
				System.out.println(vid);
				return vid;
			}
		}*/
	}

	@Override
	public PurchaseEntryProductEntityClass editproductdetailforPurchaseEntry(Long entryId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntryProductEntityClass.class);
		criteria.add(Restrictions.eq("purchase_detail_id", entryId));

		return (PurchaseEntryProductEntityClass) criteria.uniqueResult();
	}

	@Override
	public void updatePurchaseEntry(PurchaseEntry entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();

	}

	@Override
	public void updatePurchaseEntryProductEntityClass(PurchaseEntryProductEntityClass entry) {
		Session session = getCurrentSession();
		session.merge(entry);
		session.flush();
		session.clear();
	}

	@Override
	public List<PurchaseEntry> getDayBookReport(LocalDate fromDate, LocalDate toDate, Long companyId) {
		/*Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.ge("supplier_bill_date", fromDate));
		criteria.add(Restrictions.le("supplier_bill_date", toDate));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.addOrder(Order.desc("supplier_bill_date"));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("state", FetchMode.JOIN);
		criteria.setFetchMode("subledger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();*/
	List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
	purchaselist.clear();
				Session session = getCurrentSession();
				Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
						+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 order by purchase.supplier_bill_date desc");
				query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("date1", fromDate);
				query.setParameter("date2", toDate);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
			
	
	  return purchaselist;
	}

	@Override
	public List<PurchaseEntry> findAllactivePurchaseEntryOfCompany(long company_id, boolean b) {
		/*Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("company.company_id", company_id));
		criteria.addOrder(Order.desc("purchase_id"));
		criteria.add(Restrictions.eq("flag", b));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_PENDING));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setFetchMode("payments", FetchMode.JOIN);
		criteria.setFetchMode("company", FetchMode.JOIN);
		criteria.setFetchMode("debitNote", FetchMode.JOIN);
		criteria.setFetchMode("debitNote.debitDetails", FetchMode.JOIN);
		return criteria.list();*/
		
		
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT pentry from PurchaseEntry pentry LEFT JOIN FETCH pentry.company company "
				+"WHERE pentry.company.company_id =:company_id and flag =:flag and entry_status =:entry_status");
		query.setParameter("company_id", company_id);
		query.setParameter("flag", b);
		query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
			 

		   list.add((PurchaseEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  session.clear();
		 
		  return list;
	}

	@Override
	public void ChangeStatusOfPurchaseEntryThroughExcel(PurchaseEntry entry) {
		Session session = getCurrentSession();
		entry.setFlag(false);
		session.merge(entry);
		session.flush();
		session.clear();
	}

	@Override
	public List<PurchaseEntry> getHsnList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> getExempList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.add(Restrictions.eq("entrytype", 2)); //2 = import
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> getNilList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.add(Restrictions.eqOrIsNull("cgst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("igst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("sgst",(double)0)); 
		criteria.add(Restrictions.eqOrIsNull("state_compansation_tax", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("total_vat", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("total_vatcst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("total_excise", (double)0)); 
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> getNonGstList(Integer month, Long yearId, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.and(Restrictions.eq("company.company_id", companyId),
				Restrictions.sqlRestriction("MONTH(this_.supplier_bill_date) = " + month),
				Restrictions.eq("accountingYear.year_id", yearId),
				Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL)));
		criteria.add(Restrictions.eqOrIsNull("cgst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("igst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("sgst", (double)0)); 
		criteria.add(Restrictions.eqOrIsNull("state_compansation_tax", (double)0)); 
		criteria.add(Restrictions.or(Restrictions.neOrIsNotNull("total_vat", (double)0),
				Restrictions.neOrIsNotNull("total_vatcst", (double)0),
				Restrictions.neOrIsNotNull("total_excise", (double)0)));
		criteria.setFetchMode("supplier", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> supplirPurchaseHavingGST0(LocalDate fromDate, LocalDate toDate, Long company_id) {
		

		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase LEFT JOIN FETCH purchase.company company "
					+"WHERE purchase.company.company_id =:company_id and purchase.flag =:flag and purchase.supplier_bill_date>=:from_date and purchase.supplier_bill_date<=:to_date "
					+ "and purchase.supplier.gst_applicable=:gst_applicable and purchase.entry_status !=:entry_status and purchase.cgst=:cgst and purchase.igst=:igst and purchase.sgst=:sgst");
			query.setParameter("from_date", fromDate);
			query.setParameter("to_date", toDate);
			query.setParameter("company_id", company_id);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", true);
			query.setParameter("cgst", (double)0);
			query.setParameter("igst", (double)0);
			query.setParameter("sgst", (double)0);
			query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PurchaseEntry)scrollableResults.get()[0]);
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
	public List<PurchaseEntry> supplierHavingGSTApplicbleAsNOAndExceedingZERO(LocalDate fromDate, LocalDate toDate,
			Long company_id) {
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase LEFT JOIN FETCH purchase.company company "
					+"WHERE purchase.company.company_id =:company_id and purchase.flag =:flag and purchase.supplier_bill_date>=:from_date and purchase.supplier_bill_date<=:to_date "
					+ "and purchase.supplier.gst_applicable=:gst_applicable and (purchase.cgst>0 or purchase.igst>0 or purchase.sgst>0) ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
			query.setParameter("from_date", fromDate);
			query.setParameter("to_date", toDate);
			query.setParameter("company_id", company_id);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PurchaseEntry)scrollableResults.get()[0]);
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
	public List<PurchaseEntry> supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(LocalDate from_date,
			LocalDate to_date, Long companyId) {
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase LEFT JOIN FETCH purchase.company company "
					+"WHERE purchase.company.company_id =:company_id and purchase.flag =:flag and purchase.supplier_bill_date>=:from_date and purchase.supplier_bill_date<=:to_date "
					+ "and purchase.transaction_value>=:transaction_value and purchase.tds_amount=:tds_amount and (purchase.subledger.subledger_name LIKE '%Legal%' or purchase.subledger.subledger_name LIKE '%Professional%' or purchase.subledger.subledger_name LIKE '%Repairs & Maintenance%')");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("transaction_value",(double)30000);
			query.setParameter("tds_amount",(double)0);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PurchaseEntry)scrollableResults.get()[0]);
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
	public List<PurchaseEntry> getPurchaseForLedgerReport(LocalDate from_date, LocalDate to_date, Long suppilerId,
			Long companyId) {
		
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
	 	Query query =null;
	 	System.out.println("The purchase fr dt "+from_date);
	 	System.out.println("The purchase en dt "+to_date);
	 	System.out.println("The suppId "+suppilerId);
	 	System.out.println("The cmpId "+companyId);
		  if(suppilerId!=null) {
				
			    if(suppilerId.equals((long)-2)|| suppilerId==0)
				{
			    	query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.supplier.supplier_id IS NOT NULL and purchase.entry_status !=:entry_status  and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ");
				
				}
				else
				{
		    		query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.supplier.supplier_id=:supplier_id and purchase.entry_status !=:entry_status and purchase.supplier_bill_date >=:date1  and purchase.supplier_bill_date <=:date2 ");
					query.setParameter("supplier_id", suppilerId);
					
					
				}
			    query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				query.setParameter("date1", from_date);
				query.setParameter("date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		  System.out.println("tot rec " + purchaselist.size());
		  return purchaselist;
	}

	@Override
	public List<PurchaseEntry> getInwardSupplyList(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		
		try {
			Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
					+"WHERE purchase.company.company_id =:company_id and purchase.supplier.gst_applicable =:gst_applicable and purchase.flag =:flag and purchase.supplier_bill_date>=:from_date and purchase.supplier_bill_date<=:to_date "
					+ "order by purchase.purchase_id asc");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("flag", true);
			query.setParameter("gst_applicable", false);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((PurchaseEntry)scrollableResults.get()[0]);
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
	public Integer getCancelInwardSupplyList(LocalDate from_date, LocalDate to_date, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.createAlias("supplier", "sup");
        criteria.add(Restrictions.ge("supplier_bill_date", from_date));
		criteria.add(Restrictions.le("supplier_bill_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("entry_status", MyAbstractController.ENTRY_CANCEL));
		criteria.add(Restrictions.eq("flag", true));
		criteria.add(Restrictions.eq("sup.gst_applicable", false));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria .setProjection(Projections.rowCount());
		return (int)(long) criteria.uniqueResult();
	}

	@Override
	public PurchaseEntry isExcelVocherNumberExist(String vocherNo, Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		criteria.add(Restrictions.eq("excel_voucher_no", vocherNo));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (PurchaseEntry)criteria.uniqueResult();
	}
	
	
	@Override
	public List<PurchaseEntry>  getAllOpeningBalanceAgainstPurchase(Long supplierId,Long companyId) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		
		Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	

		criteria.add(Restrictions.and(criterion1,criterion2,criterion3));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> findAllPurchaseEntryOfCompanyForMobile(Long CompanyId, Boolean flag, Long yearID) {
		List<PurchaseEntry> list =  new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT purchase from PurchaseEntry purchase LEFT JOIN FETCH purchase.company company "
				+"WHERE purchase.company.company_id =:company_id and purchase.flag =:flag and purchase.accountingYear.year_id=:yearID ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
		query.setParameter("company_id", CompanyId);
		query.setParameter("yearID", yearID);
		query.setParameter("flag", flag);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		  while (scrollableResults.next()) {
		   list.add((PurchaseEntry)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
		  }
		  session.clear();
		  return list;
	}

	@Override
	public List<PurchaseEntry> getBillsPayableForPurchase(Long supplierId, LocalDate fromDate, LocalDate toDate,
			Long companyId) {
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		purchaselist.clear();
		Session session = getCurrentSession();
		Query query =null;
		  if(supplierId!=null) {
				
			    if(supplierId > 0)
				{
					
					query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.againstOpeningBalnce =:againstOpeningBalnce and purchase.company.company_id =:company_id and purchase.supplier.supplier_id=:supplier_id and purchase.entry_status =:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
					query.setParameter("supplier_id", supplierId);
				}
				else
				{
					query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag  and purchase.company.company_id =:company_id and purchase.supplier.supplier_id IS NOT NULL and purchase.entry_status =:entry_status and purchase.supplier_bill_date >=:date1 and purchase.supplier_bill_date <=:date2 ORDER by purchase.supplier_bill_date DESC,purchase.purchase_id DESC");
				}
			    query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_PENDING);
				query.setParameter("date1", fromDate);
				query.setParameter("date2", toDate);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		  
		  return purchaselist;
	}

	@Override
	public List<PurchaseEntry> getAllPurchaseAmount(Long supplierId, Long companyId, LocalDate toDate) {
		Criteria criteria = getCurrentSession().createCriteria(PurchaseEntry.class);
		//System.out.println("customerId" +customerId);
		//System.out.println("company_id" +companyId);
		//Criterion criterion1 = Restrictions.eq("againstOpeningBalnce", true);
		Criterion criterion2 = Restrictions.eq("supplier.supplier_id",supplierId);
		Criterion criterion3 = Restrictions.eq("company.company_id",companyId);	
		Criterion criterion4 = Restrictions.le("created_date",toDate);	
		Criterion criterion5= Restrictions.ne("entry_status", MyAbstractController.ENTRY_CANCEL);
		Criterion criterion6= Restrictions.eq("flag", true);
		criteria.add(Restrictions.and(criterion2,criterion3,criterion4,criterion5,criterion6));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	//	System.out.println("salesentry results"+criteria.list().size());
		return criteria.list();
	}

	@Override
	public List<PurchaseEntry> getPurchaseForLedgerReport1(LocalDate to_date, Long suppilerId, Long companyId) {
		List<PurchaseEntry> purchaselist = new ArrayList<PurchaseEntry>();
		Session session = getCurrentSession();
	 	Query query =null;
	 	
	 	
		  if(suppilerId!=null) {
				
			    if(suppilerId.equals((long)-2)|| suppilerId==0)
				{
			    	query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.supplier.supplier_id IS NOT NULL and purchase.entry_status !=:entry_status  and  purchase.supplier_bill_date <=:date2 ");
				
				}
				else
				{
		    		query = session.createQuery("SELECT purchase from PurchaseEntry purchase "
							+ "WHERE purchase.flag=:flag and purchase.company.company_id =:company_id and purchase.supplier.supplier_id=:supplier_id and purchase.entry_status !=:entry_status  and purchase.supplier_bill_date <=:date2 ");
					query.setParameter("supplier_id", suppilerId);
					
					
				}
			    query.setParameter("company_id", companyId);
				query.setParameter("flag", true);
				query.setParameter("entry_status", MyAbstractController.ENTRY_CANCEL);
				//query.setParameter("date1", from_date);
				query.setParameter("date2", to_date);
				ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
				
				  while (scrollableResults.next()) {
					  purchaselist.add((PurchaseEntry)scrollableResults.get()[0]);
				   session.evict(scrollableResults.get()[0]);
			         }
				  session.clear();
		  }
		 
		  return purchaselist;
	}

}