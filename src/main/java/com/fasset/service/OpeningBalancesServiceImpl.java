package com.fasset.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.dao.interfaces.IOpeningBalancesDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.ILedgerDAO;
import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.PurchaseEntryDAOImpl;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.ICustomerDAO;
import com.fasset.dao.interfaces.ISupplierDAO;
@Service
@Transactional
public class OpeningBalancesServiceImpl implements IOpeningBalancesService{
	@Autowired
	private IOpeningBalancesDAO opbalanceDAO;	

	@Autowired
	private ILedgerDAO ledgerDAO;	

	@Autowired
	private ICompanyDAO companyDAO;
	
	@Autowired
	private IAccountingYearDAO yearDAO;
	
	@Autowired
	private ISubLedgerDAO subledgerDAO;
	
	@Autowired
	private IBankDAO bankDAO;
	
	@Autowired
	private ICustomerDAO customerDAO;
	
	@Autowired
	private ISupplierDAO supplierDAO;
	
	@Override
	public void add(OpeningBalances entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(OpeningBalances entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<OpeningBalances> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public OpeningBalances getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return opbalanceDAO.findexists(id);
	}

	@Transactional
	@Override
	public OpeningBalances getById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void removeById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public void removeById(String id) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(OpeningBalances entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(OpeningBalances entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OpeningBalances isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Transactional
	@Override
	public void updateStock(OpeningBalances opbalance) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public Long findledgerbalance(Long company_Id, Long year_Id, Long ledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexist(company_Id,year_Id,ledger_Id,balanceType);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;	
	}

	@Transactional
	@Override
	public Long findsubledgerbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexist(company_Id,year_Id,subledger_Id,balanceType);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
	}

	@Transactional
	@Override
	public Long findbankbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexist(company_Id,year_Id,subledger_Id,balanceType);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
	}

	@Transactional
	@Override
	public Long findsupplierbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexist(company_Id,year_Id,subledger_Id,balanceType);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
		
	}

	@Transactional
	@Override
	public Long findcustomerbalance(Long company_Id, Long year_Id, Long subledger_Id, Long balanceType) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexist(company_Id,year_Id,subledger_Id,balanceType);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
	}

	@Transactional
	@Override
	public Long saveOpeningBalances(Long companyId, Long year_id, Long id, Long type, Double credit, Double debit) {
		// TODO Auto-generated method stub
		OpeningBalances balance= new OpeningBalances();
		
		balance.setCredit_opening_balance(credit);
		balance.setDebit_opening_balance(debit);
		balance.setCredit_balance(credit);
		balance.setDebit_balance(debit);
		balance.setBalanceType(type);
		balance.setCreated_date(new LocalDate());
		balance.setOpeningType((long) 1);
		try {
			if(type==1)
			balance.setLedger(ledgerDAO.findOne(id));
			else if(type==2)
				balance.setSubLedger(subledgerDAO.findOne(id));
			else if(type==3)
				balance.setBank(bankDAO.findOne(id));
			else if(type==4)
				balance.setSupplier(supplierDAO.findOne(id));
			else if(type==5)
				balance.setCustomer(customerDAO.findOne(id));
			balance.setAccountingYear(yearDAO.findOne(year_id));
			balance.setCompany(companyDAO.findOne(companyId));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opbalanceDAO.saveOpeningBalances(balance);
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public Long updatepeningbalance(Long existid, Double creditval, Double debitval) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findexists(existid);
		balance.setCredit_opening_balance(creditval.doubleValue());
		balance.setDebit_opening_balance(debitval.doubleValue());
		balance.setCredit_balance(creditval.doubleValue());
		balance.setDebit_balance(debitval.doubleValue());
		return opbalanceDAO.updateOpeningBalances(balance);
	}
	@Transactional
	@Override
	public Long updateCDbalance(Long existid, Double creditval, Double debitval, Long flag) {
		// TODO Auto-generated method stub
		/*OpeningBalances balance=opbalanceDAO.findexists(existid);
		//balance.setCredit_opening_balance(creditval);
		//balance.setDebit_opening_balance(debitval);
		if(flag==1)
		{
		balance.setCredit_balance(balance.getCredit_balance()+creditval);
		balance.setDebit_balance(balance.getDebit_balance()+debitval);
		}
		else
		{
			balance.setCredit_balance(balance.getCredit_balance()-creditval);
			balance.setDebit_balance(balance.getDebit_balance()-debitval);
		}*/
		return opbalanceDAO.updateOpeningBalances(existid,new Double(round(creditval,2)),new Double(round(debitval,2)),flag);
	}

	@Override
	public List<OpeningBalances> getOpeningBalances(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		return opbalanceDAO.getOpeningBalances(from_date, to_date, companyId);
	}

	@Override
	public List<Ledger> getOpeningBalancesOfLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		
		return opbalanceDAO.getOpeningBalancesOfLedger(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances> getOpeningBalances(Long companyId,Long customerId,Long supplierId,Long bankId)  {
		// TODO Auto-generated method stub
		return opbalanceDAO.getOpeningBalances(companyId,customerId,supplierId,bankId);
	}

	@Transactional
	@Override
	public Long saveOpeningBalancesnew(Long company_id, Long year, Long id, long type, Double credit, Double debit,
			String date) {
		// TODO Auto-generated method stub
		OpeningBalances balance= new OpeningBalances();
		
		balance.setCredit_opening_balance(credit);
		balance.setDebit_opening_balance(debit);
		balance.setCredit_balance(credit);
		balance.setDebit_balance(debit);
		balance.setBalanceType(type);
		balance.setCreated_date(new LocalDate(date));
		balance.setOpeningType((long) 1);

		try {
			if(type==1)
			balance.setLedger(ledgerDAO.findOne(id));
			else if(type==2)
				balance.setSubLedger(subledgerDAO.findOne(id));
			else if(type==3)
				balance.setBank(bankDAO.findOne(id));
			else if(type==4)
				balance.setSupplier(supplierDAO.findOne(id));
			else if(type==5)
				balance.setCustomer(customerDAO.findOne(id));
			balance.setAccountingYear(yearDAO.findOne(year));
			balance.setCompany(companyDAO.findOne(company_id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opbalanceDAO.saveOpeningBalances(balance);
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public Long updatepeningbalancenew(Long existid, Double creditval, Double debitval, String date, Long year) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findexists(existid);
		balance.setCredit_opening_balance(creditval.doubleValue());
		balance.setDebit_opening_balance(debitval.doubleValue());
		balance.setCredit_balance(creditval.doubleValue());
		balance.setDebit_balance(debitval.doubleValue());
		balance.setCreated_date(new LocalDate(date));
		try {
			balance.setAccountingYear(yearDAO.findOne(year));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opbalanceDAO.updateOpeningBalances(balance);
	}

	@Override
	public Long findcustomerbalancebydate(Long company_id, Long year_id, Long sids, long balanceType, LocalDate date) {
		// TODO Auto-generated method stub
				OpeningBalances balance=opbalanceDAO.findifexistbydate(company_id,year_id,sids,balanceType,date,null,null,null,null,null,null,null,null,null,null,null,null);
				if(balance!=null)
					return balance.getOpening_id();
				else
				return (long) 0;
	}

	@Override
	public Long saveOpeningBalancesbydate(LocalDate date, Long company_id, Long year_id, Long id, long type,
			Double credit, Double debit,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit1,DebitNote debit1,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV) {
		OpeningBalances balance= new OpeningBalances();
		/*balance.setCredit_opening_balance(credit);
		balance.setDebit_opening_balance(debit);*/
		balance.setCredit_balance(new Double(round(credit,2)));
		balance.setDebit_balance(new Double(round(debit,2)));
		balance.setBalanceType(type);
		balance.setCreated_date(date);
		balance.setOpeningType((long) 2);

		try {
			if(type==1)
			{
			balance.setLedger(ledgerDAO.findOne(id));
			}
			else if(type==2)
			{
				balance.setSubLedger(subledgerDAO.findOne(id));
				if(sales!=null)
				{
					balance.setSales(sales);
				}
				if(receipt!=null)
				{
					balance.setReceipt(receipt);
				}
				if(purchase!=null)
				{
					balance.setPurchase(purchase);
				}
				if(payment!=null)
				{
					balance.setPayment(payment);
				}
				if(credit1!=null)
				{
					balance.setCredit(credit1);
				}
				if(debit1!=null)
				{
					balance.setDebit(debit1);
				}
				if(contra !=null)
				{
					balance.setContra(contra);
				}
				if(mjv !=null)
				{
					balance.setMjv(mjv);
				
				}
				if(payAutoJv !=null)
				{
					balance.setPayAutoJv(payAutoJv);
				}
				if(gstAutoJV !=null)
				{
					balance.setGstAutoJV(gstAutoJV);
				}
				if(depriAutoJV !=null)
				{
					balance.setDepriAutoJV(depriAutoJV);
				}
				if(yearEndJV!=null)
				{
					balance.setYearEndJV(yearEndJV);
				}
			}
			else if(type==3)
			{
				balance.setBank(bankDAO.findOne(id));
			}
			else if(type==4)
			{
				balance.setSupplier(supplierDAO.findOne(id));
			}
			else if(type==5)
			{
				balance.setCustomer(customerDAO.findOne(id));
			}
			balance.setAccountingYear(yearDAO.findOne(year_id));
			balance.setCompany(companyDAO.findOne(company_id));
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opbalanceDAO.saveOpeningBalances(balance);
		// TODO Auto-generated method stub
	}

	@Override
	public Long findsupplierbalancebydate(Long company_id, Long year_id, Long subledger_Id, long balanceType, LocalDate date) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexistbydate(company_id,year_id,subledger_Id,balanceType,date,null,null,null,null,null,null,null,null,null,null,null,null);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
		
	}

	@Override
	public Long findbankbalancebydate(Long company_id, long year_id, Long bank_id, long balanceType, LocalDate date) {
		// TODO Auto-generated method stub
		OpeningBalances balance=opbalanceDAO.findifexistbydate(company_id,year_id,bank_id,balanceType,date,null,null,null,null,null,null,null,null,null,null,null,null);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
	}

	@Override
	public Long findsubledgerbalancebydate(Long company_id, Long year_id, Long sub_id, long balanceType, LocalDate date,SalesEntry sales,Receipt receipt,PurchaseEntry purchase,Payment payment,CreditNote credit,DebitNote debit,Contra contra,ManualJournalVoucher mjv,PayrollAutoJV payAutoJv,GSTAutoJV gstAutoJV,DepreciationAutoJV depriAutoJV,YearEndJV yearEndJV) {
		OpeningBalances balance=opbalanceDAO.findifexistbydate(company_id,year_id,sub_id,balanceType,date,sales,receipt,purchase,payment,credit,debit,contra,mjv,payAutoJv,gstAutoJV,depriAutoJV,yearEndJV);
		if(balance!=null)
			return balance.getOpening_id();
		else
		return (long) 0;
	}

	@Override
	public double getclosingbalance(Long company_id,Long year_id, LocalDate created_date, Long subledger_Id, long type) {
		// TODO Auto-generated method stub
		return opbalanceDAO.getclosingbalance(company_id,year_id,created_date,subledger_Id,type);
	}

	@Override
	public List<OpeningBalances> findAllOPbalancesofdata(long company_id, long flag, long year, long openingtype) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesofdata(company_id,flag,year,openingtype);
	}
	
	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSubledger(LocalDate from_date, LocalDate to_date, Long companyId,Boolean flag) {
		// TODO Auto-generated method stub
		
		return opbalanceDAO.findAllOPbalancesforSubledger(from_date, to_date, companyId,flag);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforLedger(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforCustomer(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforCustomer(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSupplier(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforSupplier(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforBank(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforBank(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSubLedger(LocalDate from_date, LocalDate to_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforSubLedger(from_date, to_date, companyId);
	}
	
	@Override
	public List<OpeningBalances[]> tdsReceivableAndTdsPayableReports(LocalDate from_date, LocalDate to_date,
			Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.tdsReceivableAndTdsPayableReports(from_date, to_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforCustomerBeforeFromDate(LocalDate from_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforCustomerBeforeFromDate(from_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforSupplierBeforeFromDate(LocalDate from_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforSupplierBeforeFromDate(from_date, companyId);
	}

	@Override
	public List<OpeningBalances[]> findAllOPbalancesforBankBeforeFromDate(LocalDate from_date, Long companyId) {
		// TODO Auto-generated method stub
		return opbalanceDAO.findAllOPbalancesforBankBeforeFromDate(from_date, companyId);
	}
	@Override
	public CopyOnWriteArrayList<OpeningBalances> findAllOPbalancesforSubledger(LocalDate from_date, LocalDate to_date, Long subledgerId,
			Long companyId) {
		// TODO Auto-generated method stub
		
		return opbalanceDAO.findAllOPbalancesforSubledger(from_date, to_date, subledgerId, companyId);
	}
	public String round(Double debit_balance, int decimalPlace) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(decimalPlace);
		return df.format(debit_balance);
	}

	@Override
	public OpeningBalances isOpeningBalancePresentPayment(LocalDate date, Long companyId,Long supplier_id) {

		return opbalanceDAO.isOpeningBalancePresentPayment(date, companyId, supplier_id);
	}
	
	@Override
	public OpeningBalances isOpeningBalancePresentReceipt(LocalDate date, Long companyId,Long customer_id) {

		return opbalanceDAO.isOpeningBalancePresentReceipt(date, companyId, customer_id);
	}
	

	@Override
	public OpeningBalances isOpeningBalancePresentPurchase(LocalDate date, Long companyId,Long supplier_id) {

		return opbalanceDAO.isOpeningBalancePresentPurchase(date, companyId, supplier_id);
	}
	
	
	@Override
	public OpeningBalances isOpeningBalancePresentSales(LocalDate date, Long companyId,Long customer_id) {

		return opbalanceDAO.isOpeningBalancePresentSales(date, companyId, customer_id);
				
	}

	@Override
	public OpeningBalances isOpeningBalancePresentSalesForPeriod(LocalDate fromdate, Long companyId, Long customerId,
			LocalDate toDate) {
		// TODO Auto-generated method stub
		return opbalanceDAO.isOpeningBalancePresentSalesForPeriod(fromdate, companyId, customerId, toDate);
	}

	
	
	
	
	
}