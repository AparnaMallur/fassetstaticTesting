package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICreditNoteDAO;
import com.fasset.dao.interfaces.IDebitNoteDAO;
import com.fasset.dao.interfaces.IPaymentDAO;
import com.fasset.dao.interfaces.IPurchaseEntryDAO;
import com.fasset.dao.interfaces.IReceiptDAO;
import com.fasset.dao.interfaces.ISalesEntryDAO;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.Company;
import com.fasset.entities.CreditNote;
import com.fasset.entities.DebitNote;
import com.fasset.entities.Payment;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.SubLedger;
import com.fasset.form.GSTReport1Form;
import com.fasset.form.GSTdocsForm;
import com.fasset.form.HSNReportForm;
import com.fasset.form.IncomeAndExpenditureForm;
import com.fasset.form.IncomeAndExpenditureForm.IncomeOrExpense;
import com.fasset.form.IncomeAndExpenditureForm.IncomeOrExpense.Details;
import com.fasset.service.interfaces.IReportService;

@Transactional
@Service
public class ReportServiceImpl implements IReportService{
	
	@Autowired
	private IPaymentDAO paymentDao;
	
	@Autowired
	private IReceiptDAO receiptDao;
	
	@Autowired
	private ISalesEntryDAO salesEntryDao;
	
	@Autowired
	private ICreditNoteDAO creditNoteDao;

	@Autowired
	private IDebitNoteDAO debitnotedao ;
	
	@Autowired
	private IPurchaseEntryDAO pururchaseEntryDAO;
	
	@Override
	public IncomeAndExpenditureForm getIncomeAndExpenditure(Long yearId, Long companyId) {
		IncomeAndExpenditureForm incomeAndExpenditure = new IncomeAndExpenditureForm();
		
		Double surplus = new Double(0);
		Double deficit = new Double(0);
		Double totalIncome = new Double(0);
		Double totalExpense = new Double(0);
		List<IncomeOrExpense> incomes= new ArrayList<IncomeOrExpense>();
		List<IncomeOrExpense> expenses = new ArrayList<IncomeOrExpense>();
		
		List<Payment> paymentList = paymentDao.getExpenditureByYearId(yearId, companyId);
		List<Receipt> receiptList = receiptDao.getIncomeByYearId(yearId, companyId);
		
		AccountGroup accountGroup = new AccountGroup();
		SubLedger subLedger = new SubLedger(); 
		
		for (Receipt receipt : receiptList) {			
			if(receipt.getSales_bill_id() != null){
				accountGroup = receipt.getSales_bill_id().getSubledger().getLedger().getAccsubgroup().getAccountGroup();
				subLedger = receipt.getSales_bill_id().getSubledger();
			}

			else if(receipt.getSubLedger() != null){
				accountGroup = receipt.getSubLedger().getLedger().getAccsubgroup().getAccountGroup();
				subLedger = receipt.getSubLedger();	
			}
			
			boolean isExistsGroup = false;
			if(incomes.size() > 0){
				for(IncomeOrExpense income : incomes){					
					Long groupId = accountGroup.getGroup_Id();
					if(income.getGroup().getGroup_Id() == groupId){
						isExistsGroup = true;
						boolean isExistsSubledger = false;
						if(income.getList().size() > 0){
							for(Details details:income.getList()){
								if(details.getSubLedger().getSubledger_Id() == subLedger.getSubledger_Id()){
									isExistsSubledger = true;
									details.setAmount(details.getAmount()+receipt.getAmount());
								}
							}
						}
						if(!isExistsSubledger){
							Details details = income.getDetailsClass();
							details.setSubLedger(subLedger);
							details.setAmount(receipt.getAmount());
							income.getList().add(details);
						}
						income.setTotalAmount(income.getTotalAmount() + receipt.getAmount());
					}
				}
			}
			if(!isExistsGroup){
				IncomeOrExpense income = incomeAndExpenditure.getIncomeOrExpense();
				income.setGroup(accountGroup);
				Details detail = income.getDetailsClass();
				detail.setAmount(receipt.getAmount());
				detail.setSubLedger(subLedger);
				income.getList().add(detail);
				income.setTotalAmount(receipt.getAmount());
				incomes.add(income);
			}
			totalIncome += receipt.getAmount();
		}
		
		for (Payment payment : paymentList) {			
			if(payment.getSupplier_bill_no() != null){
				accountGroup = payment.getSupplier_bill_no().getSubledger().getLedger().getAccsubgroup().getAccountGroup();
				subLedger = payment.getSupplier_bill_no().getSubledger();
			}
			else if(payment.getSubLedger() != null){
				accountGroup = payment.getSubLedger().getLedger().getAccsubgroup().getAccountGroup();
				subLedger = payment.getSubLedger();
			}
			
			boolean isExistsGroup = false;
			if(expenses.size() > 0){
				for(IncomeOrExpense expense : expenses){					
					Long groupId = accountGroup.getGroup_Id();
					if(expense.getGroup().getGroup_Id() == groupId){
						isExistsGroup = true;
						boolean isExistsSubledger = false;
						if(expense.getList().size() > 0){
							for(Details details:expense.getList()){
								if(details.getSubLedger().getSubledger_Id() == subLedger.getSubledger_Id()){
									isExistsSubledger = true;
									details.setAmount(details.getAmount()+payment.getAmount());
								}
							}
						}
						if(!isExistsSubledger){
							Details details = expense.getDetailsClass();
							details.setSubLedger(subLedger);
							details.setAmount(payment.getAmount());
							expense.getList().add(details);
						}
						expense.setTotalAmount(expense.getTotalAmount() + payment.getAmount());
					}
				}
			}
			if(!isExistsGroup){
				IncomeOrExpense expense = incomeAndExpenditure.getIncomeOrExpense();
				expense.setGroup(accountGroup);
				Details detail = expense.getDetailsClass();
				detail.setAmount(payment.getAmount());
				detail.setSubLedger(subLedger);
				expense.getList().add(detail);
				expense.setTotalAmount(payment.getAmount());
				expenses.add(expense);
			}
			totalExpense += payment.getAmount();
		}
		
		incomeAndExpenditure.setIncomes(incomes);
		incomeAndExpenditure.setExpenses(expenses);
		
		if(totalIncome > totalExpense){
			surplus = totalIncome - totalExpense;
			incomeAndExpenditure.setSurplus(surplus);
			incomeAndExpenditure.setTotalExpense(totalExpense + surplus);
			incomeAndExpenditure.setTotalIncome(totalIncome);
		}
		else{
			deficit = totalExpense - totalIncome;
			incomeAndExpenditure.setDeficit(deficit);
			incomeAndExpenditure.setTotalIncome(totalIncome + deficit);
			incomeAndExpenditure.setTotalExpense(totalExpense);
		}
		return incomeAndExpenditure;
	}
	
	@Transactional
	@Override
	public GSTReport1Form  getGSTReport1(LocalDate from_date, LocalDate to_date, Company company) {
		GSTReport1Form gstReport1Form = new GSTReport1Form(); 
		gstReport1Form.setAtAdjList(receiptDao.getATAdjList(from_date, to_date, company.getCompany_id()));
		gstReport1Form.setB2bList(salesEntryDao.getB2BList(from_date, to_date, company.getCompany_id()));
		gstReport1Form.setB2clList(salesEntryDao.getB2CLList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setB2csList(salesEntryDao.getB2CSList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setCdnrList(creditNoteDao.getCdnrList(from_date, to_date, company.getCompany_id()));
		gstReport1Form.setCdnurList(creditNoteDao.getCdnurList(from_date, to_date, company.getCompany_id(),company.getState().getState_id()));
		gstReport1Form.setExpList(salesEntryDao.getExpList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setAtList(receiptDao.getATList(from_date, to_date, company.getCompany_id()));
	/*	gstReport1Form.setCashSalesList(salesEntryDao.getCashSalesList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));*/
		gstReport1Form.setHsnReportList(getHsnList(from_date, to_date, company.getCompany_id()));
		gstReport1Form.setIntraRegisterList(salesEntryDao.getIntraRegisterList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setIntraNonRegisterList(salesEntryDao.getIntraNonRegisterList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setInterRegisterList(salesEntryDao.getInterRegisterList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setInterNonRegisterList(salesEntryDao.getInterNonRegisterList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setIntraRegisterVATList(salesEntryDao.getIntraRegisterVATList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setIntraNonRegisterVATList(salesEntryDao.getIntraNonRegisterVATList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setInterRegisterVATList(salesEntryDao.getInterRegisterVATList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setInterNonRegisterVATList(salesEntryDao.getInterNonRegisterVATList(from_date, to_date, company.getCompany_id(), company.getState().getState_id()));
		gstReport1Form.setOutwardSupplyList(getOutwardSupplyList(from_date, to_date,company));
		gstReport1Form.setInwardSupplyList(getInwardSupplyList(from_date,to_date, company));
		gstReport1Form.setCreditNoteList(getCreditNoteGstList(from_date, to_date,company));
		gstReport1Form.setDebitNoteList(getDebitNoteGstList(from_date, to_date,company));
		return gstReport1Form;
	}

	@Override
	public List<HSNReportForm> getHsnList(LocalDate from_date, LocalDate to_date, Long companyId) {
		List<HSNReportForm> hsnReportForms = new ArrayList<HSNReportForm>();
		List<SalesEntry> SalesEntries = salesEntryDao.getHsnList(from_date,to_date, companyId);
		for (SalesEntry SalesEntry : SalesEntries) {
			List<SalesEntryProductEntityClass> productDetails = salesEntryDao.findAllSalesEntryProductEntityList(SalesEntry.getSales_id());
			if(productDetails != null && productDetails.size() > 0) {
				for (SalesEntryProductEntityClass detail : productDetails) {
					Boolean flag = true;

					if(hsnReportForms.size() > 0) {
						if(detail.getHSNCode() != null && !detail.getHSNCode().trim().equals("")) {
							for(HSNReportForm hsnReport : hsnReportForms) {
								if(detail.getHSNCode().equals(hsnReport.getHsnCode())) {
									flag = false;									
									Double total = (double)0;
									
									if(detail.getTransaction_amount() != null && detail.getTransaction_amount() > 0) {
										hsnReport.setTaxableValue(hsnReport.getTaxableValue() + detail.getTransaction_amount());
										total += detail.getTransaction_amount();
									}
									if(detail.getCGST() != null && detail.getCGST() > 0) {
										hsnReport.setCgstAmount(hsnReport.getCgstAmount() + detail.getCGST());
										total += detail.getCGST();
									}
									if(detail.getSGST() != null && detail.getSGST() > 0) {
										hsnReport.setSgstAmount(hsnReport.getSgstAmount() + detail.getSGST());
										total += detail.getSGST();
									}
									if(detail.getIGST() != null && detail.getIGST() > 0) {
										hsnReport.setIgstAmount(hsnReport.getIgstAmount() + detail.getIGST());
										total += detail.getIGST();
									}
									if(detail.getState_com_tax() != null && detail.getState_com_tax() > 0) {
										hsnReport.setCessAmount(hsnReport.getCessAmount() + detail.getState_com_tax());
										total += detail.getState_com_tax();
									}
									if(detail.getQuantity() != null && detail.getQuantity() > 0) {
										hsnReport.setTotalQuantity(hsnReport.getTotalQuantity() + detail.getQuantity());
									}
									hsnReport.setTotalValue(hsnReport.getTotalValue() + total);
								}
							}
						}
						
					}
					if(flag) {
						if(detail.getHSNCode() != null && !detail.getHSNCode().trim().equals("")) {

							HSNReportForm hsnReport = new HSNReportForm();
							Double total = (double)0;
							
							hsnReport.setHsnCode(detail.getHSNCode());
							
							if(detail.getTransaction_amount() != null && detail.getTransaction_amount() > 0) {
								hsnReport.setTaxableValue(detail.getTransaction_amount());
								total += detail.getTransaction_amount();
							}
							else {
								hsnReport.setTaxableValue((double) 0);
							}
							
							if(detail.getCGST() != null && detail.getCGST() > 0) {
								hsnReport.setCgstAmount(detail.getCGST());
								total += detail.getCGST();
							}
							else {
								hsnReport.setCgstAmount((double) 0);
							}
							
							if(detail.getSGST() != null && detail.getSGST() > 0) {
								hsnReport.setSgstAmount(detail.getSGST());
								total += detail.getSGST();
							}
							else {
								hsnReport.setSgstAmount((double) 0);
							}
							if(detail.getIGST() != null && detail.getIGST() > 0) {
								hsnReport.setIgstAmount(detail.getIGST());
								total += detail.getIGST();
							}
							else {
								hsnReport.setIgstAmount((double) 0);
							}
							
							if(detail.getState_com_tax() != null && detail.getState_com_tax() > 0) {
								hsnReport.setCessAmount(detail.getState_com_tax());
								total += detail.getState_com_tax();
							}
							else {
								hsnReport.setCessAmount((double) 0);
							}
							
							if(detail.getQuantity() != null && detail.getQuantity() > 0) {
								hsnReport.setTotalQuantity(detail.getQuantity());
							}
							else {
								hsnReport.setTotalQuantity((double) 0);
							}
							
							hsnReport.setTotalValue(total);
							hsnReport.setUqc(detail.getUOM());
							
							hsnReportForms.add(hsnReport);
						}
					}
				}
			}
		}
		return hsnReportForms;
	}

	@Override
	public List<GSTdocsForm> getOutwardSupplyList(LocalDate from_date, LocalDate to_date, Company company) {
		List<GSTdocsForm> gSTdocsFormlist =  new ArrayList<GSTdocsForm>();
		String voucherRange = company.getVoucher_range();
		String[] range = voucherRange.split(",");
		for(String voucher:range)
		{
			voucher=voucher+"/%";
			GSTdocsForm form = new GSTdocsForm();
			List<SalesEntry> list = salesEntryDao.getOutwardSupplyListForGivenVocherRange(from_date, to_date, company.getCompany_id(), voucher);
			Integer count= salesEntryDao.getCancelOutwardSupplyListForGivenVocherRange(from_date, to_date, company.getCompany_id(), voucher);
			
			if(list.size()>0)
			{
			form.setSrnofrom(list.get(0).getVoucher_no());
			form.setSrnoto(list.get(list.size()-1).getVoucher_no());
			}
			else
			{
				form.setSrnofrom("0");
				form.setSrnoto("0");
			}
			form.setNatureOfdocument("Invoice for outward supply");
			form.setTotalNumber(list.size());
			form.setTotalCancel(count);
			gSTdocsFormlist.add(form);
		}
		return gSTdocsFormlist;
	}

	@Override
	public List<GSTdocsForm> getInwardSupplyList(LocalDate from_date, LocalDate to_date, Company company) {
		// TODO Auto-generated method stub
		List<GSTdocsForm> gSTdocsFormlist =  new ArrayList<GSTdocsForm>();
		GSTdocsForm form = new GSTdocsForm();
		List<PurchaseEntry> list  = pururchaseEntryDAO.getInwardSupplyList(from_date, to_date, company.getCompany_id());
		Integer count= pururchaseEntryDAO.getCancelInwardSupplyList(from_date, to_date, company.getCompany_id());
		
		
		
		if(list.size()>0)
		{
			form.setSrnofrom(list.get(0).getVoucher_no());
			form.setSrnoto(list.get(list.size()-1).getVoucher_no());
		}
		else
		{
			form.setSrnofrom("0");
			form.setSrnoto("0");
		}
		
		form.setNatureOfdocument("Invoice for inward supply from unregistered person");
		form.setTotalNumber(list.size());
		form.setTotalCancel(count);
		gSTdocsFormlist.add(form);
		return gSTdocsFormlist;
	}

	@Override
	public List<GSTdocsForm> getCreditNoteGstList(LocalDate from_date, LocalDate to_date, Company company) {
		List<GSTdocsForm> gSTdocsFormlist =  new ArrayList<GSTdocsForm>();
		GSTdocsForm form = new GSTdocsForm();
		List<CreditNote> list  = creditNoteDao.getCreditNoteGstList(from_date, to_date, company.getCompany_id());
		Integer count= creditNoteDao.getCancelCreditNoteGstList(from_date, to_date, company.getCompany_id());
	
		if(list.size()>0)
		{
			form.setSrnofrom(list.get(0).getVoucher_no());
			form.setSrnoto(list.get(list.size()-1).getVoucher_no());
		}
		else
		{
			form.setSrnofrom("0");
			form.setSrnoto("0");
		}
		
		form.setNatureOfdocument("Credit Note");
		form.setTotalNumber(list.size());
		form.setTotalCancel(count);
		gSTdocsFormlist.add(form);
		return gSTdocsFormlist;
	}

	@Override
	public List<GSTdocsForm> getDebitNoteGstList(LocalDate from_date, LocalDate to_date, Company company) {
		List<GSTdocsForm> gSTdocsFormlist =  new ArrayList<GSTdocsForm>();
		GSTdocsForm form = new GSTdocsForm();
		List<DebitNote> list  = debitnotedao.getDebitNoteGstList(from_date, to_date, company.getCompany_id());
		Integer count= debitnotedao.getCancelDebitNoteGstList(from_date, to_date, company.getCompany_id());
	
		if(list.size()>0)
		{
			form.setSrnofrom(list.get(0).getVoucher_no());
			form.setSrnoto(list.get(list.size()-1).getVoucher_no());
		}
		else
		{
			form.setSrnofrom("0");
			form.setSrnoto("0");
		}
		
		form.setNatureOfdocument("Debit Note");
		form.setTotalNumber(list.size());
		form.setTotalCancel(count);
		gSTdocsFormlist.add(form);
		return gSTdocsFormlist;
	}

}
