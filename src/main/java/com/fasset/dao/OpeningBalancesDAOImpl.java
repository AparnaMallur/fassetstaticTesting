package com.fasset.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Contra;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.Ledger;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.YearEndJV;

@Repository
@Transactional
public class OpeningBalancesDAOImpl extends AbstractHibernateDao<OpeningBalances> implements IOpeningBalancesDAO{

	@Transactional
	@Override
	public OpeningBalances isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Transactional
	public OpeningBalances findexists(Long id)
	{
		Criteria criteria = getCurrentSession().createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.eq("opening_id", id));
		return (OpeningBalances) criteria.uniqueResult();
	}
	
	@Transactional
	@Override
	public Long saveOpeningBalances(OpeningBalances opbalance) {
		// TODO Auto-generated method stub
		
		Session session = getCurrentSession();
		Long id = (Long) session.save(opbalance);
		
		session.flush();
		session.clear();
		return id ;
	}

	@Transactional
	@Override
	public Long updateOpeningBalances(Long existid, Double creditval, Double debitval, Long flag) {
		Session session = getCurrentSession();
		Query query=null;
		if(flag.equals((long)1))
		{
		query = session.createQuery("update OpeningBalances set credit_balance=credit_balance+:credit_balance1,debit_balance=debit_balance+:debit_balance1 where opening_id =:existid");
		}
		else
		{
	     query = session.createQuery("update OpeningBalances set credit_balance=credit_balance-:credit_balance1,debit_balance=debit_balance-:debit_balance1 where opening_id =:existid");
		}
		query.setParameter("credit_balance1", creditval);
		query.setParameter("debit_balance1", debitval);
		query.setParameter("existid", existid);
		try{
			query.executeUpdate();
		}
		catch (Exception e){
			e.printStackTrace();		    	 
		}	
		return existid;
	}

	@Transactional
	@Override
	public Long findledgerbalance(Long company_Id, Long year_Id, Long ledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long findsubledgerbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		return null;
	}

	@Override
	public Long findbankbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long findsupplierbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long findcustomerbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateStock(OpeningBalances opbalance) {
		// TODO Auto-generated method stub
		
	}
	@Transactional
	@Override
	public OpeningBalances findifexist(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.eq("company.company_id", company_Id));
		criteria.add(Restrictions.eq("accountingYear.year_id", year_Id));
		if(balanceType==1)
			criteria.add(Restrictions.eq("ledger.ledger_id", subledger_Id));
		else if(balanceType==2)		
			criteria.add(Restrictions.eq("subLedger.subledger_Id", subledger_Id));
		else if(balanceType==3)
			criteria.add(Restrictions.eq("bank.bank_id", subledger_Id));
		else if(balanceType==4)
			criteria.add(Restrictions.eq("supplier.supplier_id", subledger_Id));
		else if(balanceType==5)
			criteria.add(Restrictions.eq("customer.customer_id", subledger_Id));

		criteria.add(Restrictions.eq("balanceType", balanceType));
		return (OpeningBalances) criteria.uniqueResult();	
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances> getOpeningBalances(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.ge("created_date", from_date));
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Ledger> getOpeningBalancesOfLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		Session session = getCurrentSession();
		List<Ledger> ledgerlist = new ArrayList<>();
		List<Ledger> ledgerlistwithsubledgers = new ArrayList<>();
		List<OpeningBalances>  openingBalancesList = new ArrayList<OpeningBalances>();
		
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.ge("created_date", from_date));
		criteria.add(Restrictions.le("created_date", to_date));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		openingBalancesList = criteria.list();
		for(OpeningBalances balance : openingBalancesList)
		{
			if(balance.getLedger()!=null)
			{
			ledgerlist.add(balance.getLedger());
			}
		}
		
		if(ledgerlist!=null && ledgerlist.size()>0)
		{
			for(Ledger ledger : ledgerlist)
			{			
					ledgerlistwithsubledgers.add(getLedger(ledger, session));
			}
		}
		
		return ledgerlistwithsubledgers;
	}
	
	
	@Transactional
	public Ledger getLedger(Ledger ledger, Session session) {

		Criteria criteria = session.createCriteria(Ledger.class);
		criteria.add(Restrictions.eq("ledger_id", ledger.getLedger_id()));
		criteria.setFetchMode("subLedger", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Ledger) criteria.uniqueResult();

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances> getOpeningBalances(Long companyId,Long customerId,Long supplierId,Long bankId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.eq("company.company_id", companyId));
		if(customerId!=null) {
			
			if(customerId==-1)
			{
				/*criteria.add(Restrictions.eq("subLedger.subledger_Id", null));
				criteria.add(Restrictions.eq("ledger.ledger_id", null));
				criteria.add(Restrictions.eq("bank.bank_id", null));
				criteria.add(Restrictions.eq("supplier.supplier_id", null));
				criteria.add(Restrictions.isNotNull("customer.customer_id"));*/
			}
			if(customerId!=-1)
			{
				criteria.add(Restrictions.eq("customer.customer_id", customerId));
				
			}
		}
		
           if(supplierId!=null) {
			
			if(supplierId==-2)
			{
				/*criteria.add(Restrictions.eq("subLedger.subledger_Id", null));
				criteria.add(Restrictions.eq("ledger.ledger_id", null));
				criteria.add(Restrictions.eq("bank.bank_id", null));
				criteria.add(Restrictions.eq("customer.customer_id", null));
				criteria.add(Restrictions.isNotNull("supplier.supplier_id"));*/
			}
			if(supplierId!=-2)
			{
				criteria.add(Restrictions.eq("supplier.supplier_id", supplierId));
			}
		}
           
           if(bankId!=null) {
   			
   			if(bankId==-4)
   			{
   				/*criteria.add(Restrictions.eq("subLedger.subledger_Id", null));
   				criteria.add(Restrictions.eq("ledger.ledger_id", null));
   				criteria.add(Restrictions.eq("supplier.supplier_id", null));
   				criteria.add(Restrictions.eq("customer.customer_id", null));
   				criteria.add(Restrictions.isNotNull("bank.bank_id"));*/
   			}
   			if(bankId!=-4)
   			{
   				criteria.add(Restrictions.eq("bank.bank_id", bankId));
   			}
   		}

           criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	@Override
	public OpeningBalances findifexistbydate(Long company_Id, Long year_Id, Long subledger_Id, long balanceType,
			LocalDate date,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,
			PayrollAutoJV payAutoJv,GSTAutoJV gSTAutoJV, DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV) 
	{
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.eq("created_date", date)); 
		criteria.add(Restrictions.eq("company.company_id", company_Id));
		criteria.add(Restrictions.eq("accountingYear.year_id", year_Id));
		if(balanceType==1)
		{
			criteria.add(Restrictions.eq("ledger.ledger_id", subledger_Id));
		}
		else if(balanceType==2)	
		{
			criteria.add(Restrictions.eq("subLedger.subledger_Id", subledger_Id));
			if(sales!=null)
			{
				criteria.add(Restrictions.eq("sales.sales_id", sales.getSales_id()));
			}
			if(receipt!=null)
			{
				criteria.add(Restrictions.eq("receipt.receipt_id", receipt.getReceipt_id()));
			}
			if(purchase!=null)
			{
				criteria.add(Restrictions.eq("purchase.purchase_id", purchase.getPurchase_id()));
			}
			if(payment!=null)
			{
				criteria.add(Restrictions.eq("payment.payment_id", payment.getPayment_id()));
			}
			if(credit!=null)
			{
				criteria.add(Restrictions.eq("credit.credit_no_id", credit.getCredit_no_id()));
			}
			if(debit!=null)
			{
				criteria.add(Restrictions.eq("debit.debit_no_id", debit.getDebit_no_id()));
			}
			if(contra!=null)
			{
				criteria.add(Restrictions.eq("contra.transaction_id", contra.getTransaction_id()));
			}
			if(mjv!=null)
			{
				criteria.add(Restrictions.eq("mjv.journal_id", mjv.getJournal_id()));
			}
			
			if(payAutoJv!=null)
			{
				criteria.add(Restrictions.eq("payAutoJv.payroll_id", payAutoJv.getPayroll_id()));
			}
			
			if(gSTAutoJV!=null)
			{
				criteria.add(Restrictions.eq("gstAutoJV.gst_id", gSTAutoJV.getGst_id()));
			}
			
			if(depriAutoJV!=null)
			{
				criteria.add(Restrictions.eq("depriAutoJV.depreciation_id", depriAutoJV.getDepreciation_id()));
			}
			
			if(yearEndJV!=null)
			{
				criteria.add(Restrictions.eq("yearEndJV.year_end_jVId", yearEndJV.getYear_end_jVId()));
			}
			
			
		}
		else if(balanceType==3)
		{
			criteria.add(Restrictions.eq("bank.bank_id", subledger_Id));
		}
		else if(balanceType==4)
		{
			criteria.add(Restrictions.eq("supplier.supplier_id", subledger_Id));
		}
		else if(balanceType==5)
		{
			criteria.add(Restrictions.eq("customer.customer_id", subledger_Id));
		}

		criteria.add(Restrictions.eq("balanceType", balanceType));
		criteria .setMaxResults(1);
		List result = criteria.setProjection(Projections.projectionList()
		                 .add(Projections.groupProperty("opening_id"))
		         ).list();
		
		if(result.size()>0)
		{
		OpeningBalances balance = new OpeningBalances();
		balance.setOpening_id((long)result.get(0));
		return balance	;
		}
		else
		{
			return null	;
		}
		
	}
	@Override
	public double getclosingbalance(Long company_Id,Long year_Id, LocalDate date, Long subledger_Id, long balanceType) {
		// TODO Auto-generated method stub
		Criteria criteria = getCurrentSession().createCriteria(OpeningBalances.class);
		criteria.add(Restrictions.le("created_date", date)); 
		criteria.add(Restrictions.eq("company.company_id", company_Id));
		criteria.add(Restrictions.eq("accountingYear.year_id", year_Id));
		if(balanceType==1)
			criteria.add(Restrictions.eq("ledger.ledger_id", subledger_Id));
		else if(balanceType==2)		
			criteria.add(Restrictions.eq("subLedger.subledger_Id", subledger_Id));
		else if(balanceType==3)
			criteria.add(Restrictions.eq("bank.bank_id", subledger_Id));
		else if(balanceType==4)
			criteria.add(Restrictions.eq("supplier.supplier_id", subledger_Id));
		else if(balanceType==5)
			criteria.add(Restrictions.eq("customer.customer_id", subledger_Id));
		
		criteria.add(Restrictions.eq("balanceType", balanceType));
		List<OpeningBalances>  op = new ArrayList<OpeningBalances>();
		op=criteria.list();
		double closingbalance=0;
		if(op!=null)
		{
			for(OpeningBalances balance : op)
			{
				System.out.println(balance.getDebit_balance());
			closingbalance+=balance.getDebit_balance()-balance.getCredit_balance();
			}
		}
		return closingbalance;
	}
	@Override
	public List<OpeningBalances> findAllOPbalancesofdata(long company_id, long flag, long year,long openingtype) {
		List result = null;
			if(openingtype==1)
			{
				if(flag==2)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
					
					.add(Restrictions.eq("openingType", openingtype))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("subLedger.subledger_Id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();					
				}
				else if(flag==3)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
					.add(Restrictions.eq("openingType", openingtype))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("bank.bank_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();
				}
				else if(flag==4)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
					.add(Restrictions.eq("openingType", openingtype))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("supplier.supplier_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();
				}
				else if(flag==5)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
					.add(Restrictions.eq("openingType", openingtype))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("customer.customer_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();					
				}
			}
			else
			{
				if(flag==2)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
			        .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("subLedger.subledger_Id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();					
				}
				else if(flag==3)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("bank.bank_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();
				}
				else if(flag==4)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("supplier.supplier_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();
				}
				else if(flag==5)
				{
					result = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", company_id))   
					.add(Restrictions.eq("balanceType", flag))
	                .setProjection(Projections.projectionList()
	                        .add(Projections.groupProperty("customer.customer_id"))
	                        .add(Projections.sum("debit_balance"))       
	                        .add(Projections.sum("credit_balance"))
	                ).list();					
				}
			}
				return result;


	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSubledger(LocalDate from_date, LocalDate to_date, Long companyId,Boolean flag) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
	System.out.println("The  created from date in TB "+from_date);
	System.out.println("The  created to date in TB "+to_date);
	System.out.println("The cmp Id TB "+companyId);

		if(flag==true)
	 {
		
	 list = getCurrentSession().createCriteria(OpeningBalances.class)       
				.add(Restrictions.eq("company.company_id", companyId))   
				.add(Restrictions.ge("created_date", from_date))
				.add(Restrictions.le("created_date", to_date))
             .setProjection(Projections.projectionList()
                     .add(Projections.groupProperty("subLedger.subledger_Id"))
                     .add(Projections.sum("debit_balance"))       
                     .add(Projections.sum("credit_balance"))
             ).list();	
	 
		return list;
	 }
	 else
	 {
			System.out.println("The from date is 16th jan "+from_date);
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.le("created_date", from_date.minusDays(1)))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("subLedger.subledger_Id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();	
		 
			return list;
	 }
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.ge("created_date", from_date))
					.add(Restrictions.le("created_date", to_date))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("ledger.ledger_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();		
			return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforCustomer(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.ge("created_date", from_date))
					.add(Restrictions.le("created_date", to_date))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("customer.customer_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();		
			return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSupplier(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.ge("created_date", from_date))
					.add(Restrictions.le("created_date", to_date))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("supplier.supplier_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();		
			return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforBank(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		if(from_date!=null && to_date!=null && companyId!=null)
		{
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.ge("created_date", from_date))
					.add(Restrictions.le("created_date", to_date))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("bank.bank_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();	
		}
			return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSubLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		if(from_date!=null && to_date!=null && companyId!=null)
		{
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.ge("created_date", from_date))
					.add(Restrictions.le("created_date", to_date))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("subLedger.subledger_Id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();	
		}
			return list;
	}
	
	@Override
	public List<OpeningBalances[]> tdsReceivableAndTdsPayableReports(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforCustomerBeforeFromDate(LocalDate from_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.le("created_date", from_date.minusDays(1)))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("customer.customer_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();		
			return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSupplierBeforeFromDate(LocalDate from_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.le("created_date", from_date.minusDays(1)))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("supplier.supplier_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();		
			return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforBankBeforeFromDate(LocalDate from_date, Long companyId) {
		List<OpeningBalances[]>  list = new ArrayList<OpeningBalances[]>();
	System.out.println("The date is "+from_date.minusDays(1) );
	System.out.println("The cmp Id is "+companyId );
		 list = getCurrentSession().createCriteria(OpeningBalances.class)       
					.add(Restrictions.eq("company.company_id", companyId))   
					.add(Restrictions.le("created_date", from_date.minusDays(1)))
	             .setProjection(Projections.projectionList()
	                     .add(Projections.groupProperty("bank.bank_id"))
	                     .add(Projections.sum("debit_balance"))       
	                     .add(Projections.sum("credit_balance"))
	             ).list();	
			return list;
	}
	@Override
	public CopyOnWriteArrayList<OpeningBalances> findAllOPbalancesforSubledger(LocalDate from_date, LocalDate to_date, Long subledgerId,
			Long companyId) {
		
		CopyOnWriteArrayList<OpeningBalances> list =  new CopyOnWriteArrayList<OpeningBalances>();
		Session session = getCurrentSession();
		
		
		try {
			Query query = session.createQuery("SELECT balance from OpeningBalances balance LEFT JOIN FETCH balance.sales sales "
					+"LEFT JOIN FETCH balance.receipt receipt "
					+"LEFT JOIN FETCH balance.payment payment "
					+"LEFT JOIN FETCH balance.purchase purchase "
					+"LEFT JOIN FETCH balance.debit debit "
					+"LEFT JOIN FETCH balance.credit credit "
					+"LEFT JOIN FETCH balance.contra contra "
					+"LEFT JOIN FETCH balance.mjv mjv "
					+"LEFT JOIN FETCH balance.payAutoJv payAutoJv "
					+"LEFT JOIN FETCH balance.gstAutoJV gstAutoJV "
					+"LEFT JOIN FETCH balance.depriAutoJV depriAutoJV "
					+"LEFT JOIN FETCH balance.yearEndJV yearEndJV "
					+"WHERE balance.company.company_id =:company_id and balance.created_date>=:from_date and balance.created_date<=:to_date "
					+ "and balance.subLedger.subledger_Id=:subledger_Id "
					+ "and (balance.credit_balance !=:credit_balance OR balance.debit_balance !=:debit_balance) order by balance.created_date ASC,balance.opening_id ASC");
			query.setParameter("from_date", from_date);
			query.setParameter("to_date", to_date);
			query.setParameter("company_id", companyId);
			query.setParameter("subledger_Id", subledgerId);
			query.setParameter("credit_balance", (double)0);
			query.setParameter("debit_balance", (double)0);
			ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
			  while (scrollableResults.next()) {
			   list.add((OpeningBalances)scrollableResults.get()[0]);
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
	public void deleteOpeningBalance(Long salesId,Long receiptId,Long creditId,Long purchaseId,Long paymentId,Long debitId,Long contra_id,Long mj_id,Long payroll_id, Long gst_id, Long depri_id,Long yearId)  {
		Session session = getCurrentSession();
		Query query =null ;
		
		if(salesId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where sales.sales_id =:id");
			query.setParameter("id", salesId);
		}
		if(receiptId!=null)
		{
			System.out.println("The receipt id getting deleted is "+receiptId);
			query = session.createQuery("delete from OpeningBalances where receipt.receipt_id =:id");
			query.setParameter("id", receiptId);
		}
		if(creditId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where credit.credit_no_id =:id");
			query.setParameter("id", creditId);
		}
		if(purchaseId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where purchase.purchase_id =:id");
			query.setParameter("id", purchaseId);
		}
		if(paymentId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where payment.payment_id =:id");
			query.setParameter("id", paymentId);
		}
		if(debitId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where debit.debit_no_id =:id");
			query.setParameter("id", debitId);
		}
		if(contra_id!=null)
		{
			query = session.createQuery("delete from OpeningBalances where contra.transaction_id =:id");
			query.setParameter("id", contra_id);
		}
		if(mj_id!=null)
		{
			query = session.createQuery("delete from OpeningBalances where mjv.journal_id =:id");
			query.setParameter("id", mj_id);
		}
		if(payroll_id!=null)
		{
			query = session.createQuery("delete from OpeningBalances where payAutoJv.payroll_id =:id");
			query.setParameter("id", payroll_id);
		}
		
		if(gst_id!=null)
		{
			query = session.createQuery("delete from OpeningBalances where gstAutoJV.gst_id =:id");
			query.setParameter("id", gst_id);
		}
		
		if(depri_id!=null)
		{
			query = session.createQuery("delete from OpeningBalances where depriAutoJV.depreciation_id =:id");
			query.setParameter("id", depri_id);
		}
		if(yearId!=null)
		{
			query = session.createQuery("delete from OpeningBalances where yearEndJV.year_end_jVId =:id");
			query.setParameter("id", yearId);
		}
		
		
		try{
			query.executeUpdate();
			
			
		}
		catch(Exception e){
		}
		
	}
	@Override
	public Long updateOpeningBalances(OpeningBalances balance) {
        Session session = getCurrentSession();	
		session.merge(balance);	
		session.flush();
		session.clear();
		return balance.getOpening_id();
	}
	@Override
	public OpeningBalances isOpeningBalancePresentPayment(LocalDate date, Long companyId,Long supplier_id) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		
		criteria.add(Restrictions.ge("created_date", (date)));
		criteria.add(Restrictions.le("created_date", (date)));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("supplier.supplier_id", supplier_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (OpeningBalances) criteria.uniqueResult();

	}
	
	@Override
	public OpeningBalances isOpeningBalancePresentReceipt(LocalDate date, Long companyId,Long customer_id) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		
		criteria.add(Restrictions.ge("created_date", (date)));
		criteria.add(Restrictions.le("created_date", (date)));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("customer.customer_id", customer_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (OpeningBalances) criteria.uniqueResult();

	}
	public OpeningBalances isOpeningBalancePresentPurchase(LocalDate date, Long companyId,Long supplier_id)
	{
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		
		criteria.add(Restrictions.eq("created_date", (date)));
		//criteria.add(Restrictions.le("created_date", (date)));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("supplier.supplier_id", supplier_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (OpeningBalances) criteria.uniqueResult();
	}
	
	public OpeningBalances isOpeningBalancePresentSales(LocalDate date, Long companyId,Long customer_id)
	{
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		
		criteria.add(Restrictions.eq("created_date", (date)));
		criteria.add(Restrictions.le("created_date", (date)));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("customer.customer_id", customer_id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (OpeningBalances) criteria.uniqueResult();
	}
	@Override
	public OpeningBalances isOpeningBalancePresentSalesForPeriod(LocalDate fromdate, Long companyId, Long customerId,
			LocalDate toDate) {
		Session session = getCurrentSession();
		
		Criteria criteria = session.createCriteria(OpeningBalances.class);
		
		criteria.add(Restrictions.eq("created_date", (fromdate)));
		//criteria.add(Restrictions.le("created_date", (toDate)));
		criteria.add(Restrictions.eq("company.company_id", companyId));
		criteria.add(Restrictions.eq("customer.customer_id", customerId));
		criteria.add(Restrictions.eq("openingType",(long)1));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (OpeningBalances) criteria.uniqueResult();
		// TODO Auto-generated method stub
		
	}
	private Double result(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
}