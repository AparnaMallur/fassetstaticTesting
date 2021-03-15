package com.fasset.report.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.controller.validators.CashFlowSummaryReportValidator;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.AccountGroup;
import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Customer;
import com.fasset.entities.Ledger;
import com.fasset.entities.Payment;
import com.fasset.entities.Receipt;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Suppliers;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.CashFlowSummaryForm;
import com.fasset.form.CustomerBalanceForm;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.SupplierBalanceForm;
import com.fasset.service.interfaces.IAccountGroupService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILedgerService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISuplliersService;

@Controller
@SessionAttributes("user")
public class CashFlowSummaryReportController extends MyAbstractController{
	
	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IOpeningBalancesService openingBalancesService;
	@Autowired
	private CashFlowSummaryReportValidator cashFlowSummaryReportValidator;
	@Autowired
	private IBankService bankService;
	@Autowired
	private ISubLedgerDAO subLedgerDAO;
	@Autowired 
	private ISalesEntryService salesEntryService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IReceiptService receiptService;
	@Autowired
	private ILedgerService ledgerService;
	@Autowired
	private IAccountGroupService accountGroupService ;
	@Autowired
	private ICustomerService customerService ;
	@Autowired
	private ISuplliersService suplliersService ;
	@Autowired
	private ICommonService commonservice;
	@Autowired
	private IOpeningBalancesService openingbalances;

	
	@Autowired
	private IBankDAO bankDao;
	
	List<OpeningBalancesForm>   bankOpeningBalanceBeforeStartDate =new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>  subledgerListbeforestartDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>  subledgerListBetweenstartDateandEndDate = new ArrayList<OpeningBalancesForm>();
	private List<OpeningBalancesForm>   bankOpeningBalanceList =new ArrayList<OpeningBalancesForm>();
	private List<Payment> paymentList = new ArrayList<Payment>();
	private List<Receipt> receiptList = new ArrayList<Receipt>();
	private List<SalesEntry> salesList = new ArrayList<SalesEntry>();
	private List<AccountGroup> accList = new ArrayList<AccountGroup>();
	private List<Ledger> legderList = new ArrayList<Ledger>();
	private List<OpeningBalancesOfSubedgerForm> subledgerList = new ArrayList<OpeningBalancesOfSubedgerForm>();
	
	private List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	
	@RequestMapping(value = "cashFlowSummaryReport", method = RequestMethod.GET)
	public ModelAndView cashFlowSummaryReport(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		Long role=(long)session.getAttribute("role");
		User user  = (User) session.getAttribute("user");
		CashFlowSummaryForm cashFlowSummaryForm = new CashFlowSummaryForm();
		Double openingBalance =0.0;
		if (role.equals(ROLE_SUPERUSER))
		{
			companyList = companyService.getAllCompaniesOnly();
			
			model.addObject("form", cashFlowSummaryForm);
			model.addObject("companyList",companyList);
			model.setViewName("/report/cashFlowSummaryReportForKpo");
		}
		else if(role.equals(ROLE_EXECUTIVE) || role.equals(ROLE_MANAGER)){ 
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", cashFlowSummaryForm);
			model.addObject("companyList",companyList);
			model.setViewName("/report/cashFlowSummaryReportForKpo");
		} 
		else {
			model.addObject("cashFlowSummaryForm", cashFlowSummaryForm);
			model.setViewName("/report/cashFlowSummaryReport");
		}
		return model;
	}
	
	@RequestMapping(value = "cashFlowSummaryReport", method = RequestMethod.POST)
	public ModelAndView cashFlowSummaryReport(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("cashFlowSummaryForm") CashFlowSummaryForm cashFlowSummaryForm, BindingResult result) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long company_id=(long)session.getAttribute("company_id");
		
		cashFlowSummaryReportValidator.validate(cashFlowSummaryForm, result);
		if(result.hasErrors()){
			model.setViewName("/report/cashFlowSummaryReport");
		}
		else {
			bankOpeningBalanceBeforeStartDate.clear();
			paymentList.clear();
			receiptList.clear();
			salesList.clear();
			bankOpeningBalanceList.clear();
			subledgerListbeforestartDate.clear();
			subledgerList.clear();
			subledgerListBetweenstartDateandEndDate.clear();
			
			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(company_id);
			
			paymentList = paymentService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), company_id,1);
			receiptList = receiptService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), company_id,1);
			salesList = salesEntryService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), company_id,1);
			
			fromDate =cashFlowSummaryForm.getFromDate();
			toDate =cashFlowSummaryForm.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(company_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(cashFlowSummaryForm.getFromDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setBankName(bank.getBank_name()+"--"+bank.getAccount_no());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceBeforeStartDate.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			SubLedger cashinhand = subLedgerDAO.findOne("Cash In Hand", company_id);
			try {
				cashinhand = subLedgerDAO.findOne(cashinhand.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", company_id);
			
			try {
				subledgercgst = subLedgerDAO.findOne(subledgercgst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", company_id);
			try {
				subledgersgst = subLedgerDAO.findOne(subledgersgst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", company_id);
			try {
				subledgerigst = subLedgerDAO.findOne(subledgerigst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", company_id);
			try {
				subledgercess = subLedgerDAO.findOne(subledgercess.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(),  company_id,false))
			{
				
				if(cashinhand!=null)
				{
					if(bal[0]!=null && bal[1]!=null && bal[2]!=null && cashinhand.getSubledger_Id().equals(new Long((long)bal[0])))
					{	
						if(cashinhand!=null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
						{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setSubledgerName(cashinhand.getSubledger_name());
							try {
								form.setSubledger(cashinhand);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						form.setCredit_balance((double)bal[2]);
						form.setDebit_balance((double)bal[1]);
						subledgerListbeforestartDate.add(form);
						} 
						
						
					}
				}
				
			}
			
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						CopyOnWriteArrayList<OpeningBalancesForm>  openingBalanceList = new CopyOnWriteArrayList<>();
						for(SalesEntry form : salesList)
						{
							
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								Boolean isSublegerExit= false;
								for(OpeningBalancesForm oldform :openingBalanceList)
								{
									if(oldform.getSubledgerName().equals(form.getSubledger().getSubledger_name()))
									{
										isSublegerExit=true;
										oldform.setCredit_balance(oldform.getCredit_balance()+form.getTransaction_value());
										
									}
								}
								if(isSublegerExit==false)
								{
									OpeningBalancesForm formbalance = new OpeningBalancesForm();
									formbalance.setSubledgerName(form.getSubledger().getSubledger_name());
									formbalance.setCredit_balance(form.getTransaction_value());
									formbalance.setDebit_balance(0.0d);
									formbalance.setSubledger(form.getSubledger());
									openingBalanceList.add(formbalance);
								}
								
								ledgerdebit_balance =ledgerdebit_balance+0.0d;
								ledgercredit_balance =ledgercredit_balance+form.getTransaction_value();
								Totaldebit_balance = Totaldebit_balance+0.0d;
								Totalcredit_balance = Totalcredit_balance+form.getTransaction_value();
								sublederform.setIsReceipt(1);
								
							}
							if(form.getCgst()>0)
							{
								
								
								if((subledgercgst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgercgst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgercgst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgercgst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getCgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgercgst.getSubledger_name());
												formbalance.setCredit_balance(form.getCgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
											
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getCgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getCgst();
											sublederform.setIsReceipt(1);
											
										}
								
							}
							if(form.getSgst()>0)
							{
								
								if((subledgersgst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgersgst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgersgst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgersgst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getSgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgersgst.getSubledger_name());
												formbalance.setCredit_balance(form.getSgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
		
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getSgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getSgst();
											sublederform.setIsReceipt(1);
											
										}
							}
							if(form.getIgst()>0)
							{
								
								if((subledgerigst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgerigst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgerigst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
									
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgerigst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getIgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgerigst.getSubledger_name());
												formbalance.setCredit_balance(form.getIgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
						
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getIgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getIgst();
											sublederform.setIsReceipt(1);
											
										}
							}
							if(form.getState_compansation_tax()>0)
							{
								
								if((subledgercess.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgercess.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgercess.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgercess.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getState_compansation_tax());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgercess.getSubledger_name());
												formbalance.setCredit_balance(form.getState_compansation_tax());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
									
											
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getState_compansation_tax();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getState_compansation_tax();
											sublederform.setIsReceipt(1);
											
										}
							}
							
							
							
							
						}
						for(Receipt form : receiptList)
						{
							if(form.getSubLedger()!=null)
							{
								if((form.getSubLedger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(form.getSubLedger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (form.getSubLedger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
									
									Boolean isSublegerExit= false;
									for(OpeningBalancesForm oldform :openingBalanceList)
									{
										if(oldform.getSubledgerName().equals(form.getSubLedger().getSubledger_name()))
										{
											isSublegerExit=true;
											oldform.setCredit_balance(oldform.getCredit_balance()+form.getAmount());
											
										}
									}
									if(isSublegerExit==false)
									{
										OpeningBalancesForm formbalance = new OpeningBalancesForm();
										formbalance.setSubledgerName(form.getSubLedger().getSubledger_name());
										formbalance.setCredit_balance(form.getAmount());
										formbalance.setDebit_balance(0.0d);
										formbalance.setSubledger(form.getSubLedger());
										openingBalanceList.add(formbalance);
									}
					
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getAmount();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getAmount();
											sublederform.setIsReceipt(1);
											
										}
							}
						}
						for(Payment form  : paymentList)
						{
							if(form.getSubLedger()!=null)
							{
								if((form.getSubLedger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(form.getSubLedger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (form.getSubLedger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(form.getSubLedger().getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setDebit_balance(oldform.getDebit_balance()+form.getAmount());
													
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(form.getSubLedger().getSubledger_name());
												formbalance.setCredit_balance(0.0d);
												formbalance.setDebit_balance(form.getAmount());
												formbalance.setSubledger(form.getSubLedger());
												openingBalanceList.add(formbalance);
											}
									
											ledgerdebit_balance =ledgerdebit_balance+form.getAmount();
											ledgercredit_balance =ledgercredit_balance+0.0d;
											Totaldebit_balance = Totaldebit_balance+form.getAmount();
											Totalcredit_balance = Totalcredit_balance+0.0d;
											sublederform.setIsPayment(1);
											
										}
							}
						}
						
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				subledgerList.add(sublederform);
			}
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(),  company_id,true))
			{
			
				if(cashinhand!=null)
				{
					if(bal[0]!=null && bal[1]!=null && bal[2]!=null && cashinhand.getSubledger_Id().equals(new Long((long)bal[0])))
					{	
						if(cashinhand!=null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
						{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setSubledgerName(cashinhand.getSubledger_name());
							try {
								form.setSubledger(cashinhand);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						form.setCredit_balance((double)bal[2]);
						form.setDebit_balance((double)bal[1]);
						subledgerListBetweenstartDateandEndDate.add(form);
						} 
						
						
					}
				}
				
			}
			Double openingBalance =0.0;
			Double openingcredit_amount=0.0;
			Double openingdebit_amount=0.0;
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), company_id))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setBankName(bank.getBank_name()+"--"+bank.getAccount_no());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceList.add(form);
					}
				}
			}
			openingBalance = openingBalance+(openingdebit_amount-openingcredit_amount);
		
			
			Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate );
			model.addObject("to_date", toDate);
			model.addObject("bankOpeningBalanceBeforeStartDate", bankOpeningBalanceBeforeStartDate);
			model.addObject("subledgerListbeforestartDate", subledgerListbeforestartDate);
			model.addObject("subledgerListBetweenstartDateandEndDate", subledgerListBetweenstartDateandEndDate);
			model.addObject("subOpeningList", subledgerList);
			model.addObject("bankOpeningBalanceList", bankOpeningBalanceList);
			model.addObject("receiptList", receiptList);
			model.addObject("paymentList", paymentList);
			model.addObject("group1", "Current Assets");
			model.addObject("subGroup2", "Trade Receivables");
			model.addObject("group4", "Current Liabilities");
			model.addObject("subGroup5", "Trade Payables");
			model.addObject("emptyString", new String("---"));
			model.setViewName("/report/cashFlowSummaryReportList");
		}
		return model;
	}
	
	@RequestMapping(value = "cashFlowSummaryReportForKpo", method = RequestMethod.POST)
	public ModelAndView cashFlowSummaryReportForKpo(@ModelAttribute("form") CashFlowSummaryForm cashFlowSummaryForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView();
		
		cashFlowSummaryReportValidator.validate(cashFlowSummaryForm, result);
		if(result.hasErrors()){
			model.addObject("companyList",companyList);
			model.setViewName("/report/cashFlowSummaryReportForKpo");
		}
		else
		{	
			bankOpeningBalanceBeforeStartDate.clear();
			paymentList.clear();
			receiptList.clear();
			salesList.clear();
			bankOpeningBalanceList.clear();
			subledgerListbeforestartDate.clear();
			subledgerList.clear();
			subledgerListBetweenstartDateandEndDate.clear();
			
			accList = accountGroupService.findAll();
			legderList = ledgerService.findLedgersOnlyOfComapany(cashFlowSummaryForm.getCompanyId());
			
			paymentList = paymentService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), cashFlowSummaryForm.getCompanyId(),1);
			receiptList = receiptService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), cashFlowSummaryForm.getCompanyId(),1);
			salesList = salesEntryService.getCashBookBankBookReport(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), cashFlowSummaryForm.getCompanyId(),1);
			
			fromDate =cashFlowSummaryForm.getFromDate();
			toDate =cashFlowSummaryForm.getToDate();
			try {
				company = companyService.getCompanyWithCompanyStautarType(cashFlowSummaryForm.getCompanyId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
                    form.setBankName(bank.getBank_name()+"--"+bank.getAccount_no());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceBeforeStartDate.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceBeforeStartDate, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			
			SubLedger cashinhand = subLedgerDAO.findOne("Cash In Hand", cashFlowSummaryForm.getCompanyId());
			try {
				cashinhand = subLedgerDAO.findOne(cashinhand.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgercgst = subLedgerDAO.findOne("Output CGST", cashFlowSummaryForm.getCompanyId());
			
			try {
				subledgercgst = subLedgerDAO.findOne(subledgercgst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgersgst = subLedgerDAO.findOne("Output SGST", cashFlowSummaryForm.getCompanyId());
			try {
				subledgersgst = subLedgerDAO.findOne(subledgersgst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgerigst = subLedgerDAO.findOne("Output IGST", cashFlowSummaryForm.getCompanyId());
			try {
				subledgerigst = subLedgerDAO.findOne(subledgerigst.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			SubLedger subledgercess = subLedgerDAO.findOne("Output CESS", cashFlowSummaryForm.getCompanyId());
			try {
				subledgercess = subLedgerDAO.findOne(subledgercess.getSubledger_Id());
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(),  cashFlowSummaryForm.getCompanyId(),false))
			{
				
				if(cashinhand!=null)
				{
					if(bal[0]!=null && bal[1]!=null && bal[2]!=null && cashinhand.getSubledger_Id().equals(new Long((long)bal[0])))
					{	
						if(cashinhand!=null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
						{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setSubledgerName(cashinhand.getSubledger_name());
							try {
								form.setSubledger(cashinhand);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						form.setCredit_balance((double)bal[2]);
						form.setDebit_balance((double)bal[1]);
						subledgerListbeforestartDate.add(form);
						} 
						
						
					}
				}
				
			}
			
			for(AccountGroup group : accList)
			{
				double Totaldebit_balance = 0;
				double Totalcredit_balance = 0;
				
				OpeningBalancesOfSubedgerForm sublederform = new OpeningBalancesOfSubedgerForm();
				List<LedgerForm>  ledgerFormList = new ArrayList<>();
				List<String> AccountSubGroupNameList = new ArrayList<>();
				sublederform.setAccountGroupName(group.getGroup_name());
				sublederform.setGroup(group);
				for(AccountSubGroup subgroup : group.getAccount_sub_group())
				{
					AccountSubGroupNameList.add(subgroup.getSubgroup_name());
					
					for(Ledger ledger : legderList)
					{
						
						LedgerForm ledgerform = new LedgerForm();
					    Double ledgerdebit_balance =0.0;
					    Double ledgercredit_balance = 0.0;
						CopyOnWriteArrayList<OpeningBalancesForm>  openingBalanceList = new CopyOnWriteArrayList<>();
						for(SalesEntry form : salesList)
						{
							
							if((form.getSubledger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
							(form.getSubledger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
							&& (form.getSubledger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
							{
								
								Boolean isSublegerExit= false;
								for(OpeningBalancesForm oldform :openingBalanceList)
								{
									if(oldform.getSubledgerName().equals(form.getSubledger().getSubledger_name()))
									{
										isSublegerExit=true;
										oldform.setCredit_balance(oldform.getCredit_balance()+form.getTransaction_value());
										
									}
								}
								if(isSublegerExit==false)
								{
									OpeningBalancesForm formbalance = new OpeningBalancesForm();
									formbalance.setSubledgerName(form.getSubledger().getSubledger_name());
									formbalance.setCredit_balance(form.getTransaction_value());
									formbalance.setDebit_balance(0.0d);
									formbalance.setSubledger(form.getSubledger());
									openingBalanceList.add(formbalance);
								}
								
								ledgerdebit_balance =ledgerdebit_balance+0.0d;
								ledgercredit_balance =ledgercredit_balance+form.getTransaction_value();
								Totaldebit_balance = Totaldebit_balance+0.0d;
								Totalcredit_balance = Totalcredit_balance+form.getTransaction_value();
								sublederform.setIsReceipt(1);
								
							}
							if(form.getCgst()>0)
							{
								
								
								if((subledgercgst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgercgst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgercgst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgercgst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getCgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgercgst.getSubledger_name());
												formbalance.setCredit_balance(form.getCgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
											
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getCgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getCgst();
											sublederform.setIsReceipt(1);
											
										}
								
							}
							if(form.getSgst()>0)
							{
								
								if((subledgersgst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgersgst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgersgst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgersgst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getSgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgersgst.getSubledger_name());
												formbalance.setCredit_balance(form.getSgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
		
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getSgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getSgst();
											sublederform.setIsReceipt(1);
											
										}
							}
							if(form.getIgst()>0)
							{
								
								if((subledgerigst.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgerigst.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgerigst.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
									
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgerigst.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getIgst());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgerigst.getSubledger_name());
												formbalance.setCredit_balance(form.getIgst());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
						
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getIgst();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getIgst();
											sublederform.setIsReceipt(1);
											
										}
							}
							if(form.getState_compansation_tax()>0)
							{
								
								if((subledgercess.getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(subledgercess.getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (subledgercess.getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(subledgercess.getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setCredit_balance(oldform.getCredit_balance()+form.getState_compansation_tax());
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(subledgercess.getSubledger_name());
												formbalance.setCredit_balance(form.getState_compansation_tax());
												formbalance.setDebit_balance(0.0d);
												formbalance.setSubledger(form.getSubledger());
												openingBalanceList.add(formbalance);
											}
									
											
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getState_compansation_tax();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getState_compansation_tax();
											sublederform.setIsReceipt(1);
											
										}
							}
							
							
							
							
						}
						for(Receipt form : receiptList)
						{
							if(form.getSubLedger()!=null)
							{
								if((form.getSubLedger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(form.getSubLedger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (form.getSubLedger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
									
									Boolean isSublegerExit= false;
									for(OpeningBalancesForm oldform :openingBalanceList)
									{
										if(oldform.getSubledgerName().equals(form.getSubLedger().getSubledger_name()))
										{
											isSublegerExit=true;
											oldform.setCredit_balance(oldform.getCredit_balance()+form.getAmount());
											
										}
									}
									if(isSublegerExit==false)
									{
										OpeningBalancesForm formbalance = new OpeningBalancesForm();
										formbalance.setSubledgerName(form.getSubLedger().getSubledger_name());
										formbalance.setCredit_balance(form.getAmount());
										formbalance.setDebit_balance(0.0d);
										formbalance.setSubledger(form.getSubLedger());
										openingBalanceList.add(formbalance);
									}
					
											ledgerdebit_balance =ledgerdebit_balance+0.0d;
											ledgercredit_balance =ledgercredit_balance+form.getAmount();
											Totaldebit_balance = Totaldebit_balance+0.0d;
											Totalcredit_balance = Totalcredit_balance+form.getAmount();
											sublederform.setIsReceipt(1);
											
										}
							}
						}
						for(Payment form  : paymentList)
						{
							if(form.getSubLedger()!=null)
							{
								if((form.getSubLedger().getLedger().getLedger_id().equals(ledger.getLedger_id()))&&
										(form.getSubLedger().getLedger().getAccsubgroup().getSubgroup_Id().equals(subgroup.getSubgroup_Id()))
										&& (form.getSubLedger().getLedger().getAccsubgroup().getAccountGroup().getGroup_Id().equals(group.getGroup_Id())))
										{
											
											Boolean isSublegerExit= false;
											for(OpeningBalancesForm oldform :openingBalanceList)
											{
												if(oldform.getSubledgerName().equals(form.getSubLedger().getSubledger_name()))
												{
													isSublegerExit=true;
													oldform.setDebit_balance(oldform.getDebit_balance()+form.getAmount());
													
													
												}
											}
											if(isSublegerExit==false)
											{
												OpeningBalancesForm formbalance = new OpeningBalancesForm();
												formbalance.setSubledgerName(form.getSubLedger().getSubledger_name());
												formbalance.setCredit_balance(0.0d);
												formbalance.setDebit_balance(form.getAmount());
												formbalance.setSubledger(form.getSubLedger());
												openingBalanceList.add(formbalance);
											}
									
											ledgerdebit_balance =ledgerdebit_balance+form.getAmount();
											ledgercredit_balance =ledgercredit_balance+0.0d;
											Totaldebit_balance = Totaldebit_balance+form.getAmount();
											Totalcredit_balance = Totalcredit_balance+0.0d;
											sublederform.setIsPayment(1);
											
										}
							}
						}
						
						if(!(ledgerdebit_balance.equals((double)0) && ledgercredit_balance.equals((double)0)))
						{
						ledgerform.setSubgroupName(subgroup.getSubgroup_name());
						ledgerform.setLedgerName(ledger.getLedger_name());
						ledgerform.setLedgercredit_balance(ledgercredit_balance);
						ledgerform.setLedgerdebit_balance(ledgerdebit_balance);
						ledgerform.setSubledgerList(openingBalanceList);
						ledgerFormList.add(ledgerform);
						}
						
					}
					
				}
				sublederform.setTotalcredit_balance(Totalcredit_balance);
				sublederform.setTotaldebit_balance(Totaldebit_balance);
				sublederform.setLedgerformlist(ledgerFormList);
				sublederform.setAccountSubGroupNameList(AccountSubGroupNameList);
				subledgerList.add(sublederform);
			}
			
			for(Object bal[] :openingBalancesService.findAllOPbalancesforSubledger(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(),  cashFlowSummaryForm.getCompanyId(),true))
			{
			
				if(cashinhand!=null)
				{
					if(bal[0]!=null && bal[1]!=null && bal[2]!=null && cashinhand.getSubledger_Id().equals(new Long((long)bal[0])))
					{	
						if(cashinhand!=null && cashinhand.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) // for removing subledgers which are not having status APPROVAL_STATUS_SECONDARY 
						{
							OpeningBalancesForm form = new OpeningBalancesForm();
							form.setSubledgerName(cashinhand.getSubledger_name());
							try {
								form.setSubledger(cashinhand);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						form.setCredit_balance((double)bal[2]);
						form.setDebit_balance((double)bal[1]);
						subledgerListBetweenstartDateandEndDate.add(form);
						} 
						
						
					}
				}
				
			}
				
     			for(Object bal[] :openingBalancesService.findAllOPbalancesforBank(cashFlowSummaryForm.getFromDate(), cashFlowSummaryForm.getToDate(), cashFlowSummaryForm.getCompanyId()))
			{
				if(bal[0]!=null && bal[1]!=null && bal[2]!=null)
				{
					Bank bank = null;
					try {
						bank = bankService.getById((long)bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(bank!=null && bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY))
					{
					OpeningBalancesForm form = new OpeningBalancesForm();
					 form.setBankName(bank.getBank_name()+"--"+bank.getAccount_no());
					form.setCredit_balance((double)bal[2]);
					form.setDebit_balance((double)bal[1]);
					bankOpeningBalanceList.add(form);
					}
				}
			}
			
			Collections.sort(bankOpeningBalanceList, new Comparator<OpeningBalancesForm>() {
	            public int compare(OpeningBalancesForm form1, OpeningBalancesForm form2) {
	                String bankName1 = form1.getBankName().trim();
	                String bankName2 = form2.getBankName().trim();
	                return bankName1.compareTo(bankName2);
	            }
	        });
			List<Customer>customerList =  customerService.findAllCustomersOfCompany(cashFlowSummaryForm.getCompanyId());
			List<Suppliers>supplierList = suplliersService.findAllSuppliersOfCompany(cashFlowSummaryForm.getCompanyId());
			List<CustomerBalanceForm>customerBalanceList = new ArrayList<>();
			List<SupplierBalanceForm>supplierBalanceList = new ArrayList<>();
			
			for(Customer cus : customerList)
			{
				CustomerBalanceForm from = new CustomerBalanceForm();
				double amount=0.0d;
				for(Receipt rec : receiptList)
				{
					if(rec.getCustomer()!=null)
					{
						if(cus.getCustomer_id().equals(rec.getCustomer().getCustomer_id()))
						{
							if(rec.getTds_amount()!=null)
							{
								amount=amount+(rec.getAmount()-rec.getTds_amount());
							}
							else
							{
								amount=amount+(rec.getAmount());
							}
						}
					}
				}
				if(amount>0)
				{
					from.setCustomer(cus);
					from.setCreditBalance(amount);
					customerBalanceList.add(from);
				}
			}
			
			for(Suppliers sup : supplierList)
			{
				SupplierBalanceForm from = new SupplierBalanceForm();
				double amount=0.0d;
				for(Payment pay : paymentList)
				{
					if(pay.getSupplier()!=null)
					{
						if(sup.getSupplier_id().equals(pay.getSupplier().getSupplier_id()))
						{
							if(pay.getTds_amount()!=null)
							{
								amount=amount+(pay.getAmount());
							}
							else
							{
								amount=amount+(pay.getAmount());
							}
						}
					}
				}
				if(amount>0)
				{
					from.setSupplier(sup);
					from.setDebitBalance(amount);
					supplierBalanceList.add(from);
				}
			}
			
			model.addObject("company",company);
			model.addObject("from_date",fromDate );
			model.addObject("to_date", toDate);
			model.addObject("bankOpeningBalanceBeforeStartDate", bankOpeningBalanceBeforeStartDate);
			model.addObject("subledgerListbeforestartDate", subledgerListbeforestartDate);
			model.addObject("subledgerListBetweenstartDateandEndDate", subledgerListBetweenstartDateandEndDate);
			model.addObject("subOpeningList", subledgerList);
			model.addObject("bankOpeningBalanceList", bankOpeningBalanceList);
			model.addObject("receiptList", receiptList);
			model.addObject("paymentList", paymentList);
			model.addObject("customerBalanceList", customerBalanceList);
			model.addObject("supplierBalanceList", supplierBalanceList);
			model.addObject("group1", "Current Assets");
			model.addObject("subGroup2", "Trade Receivables");
			model.addObject("group4", "Current Liabilities");
			model.addObject("subGroup5", "Trade Payables");
			model.addObject("emptyString", new String("---"));
			model.setViewName("/report/cashFlowSummaryReportList");
		}
		
		return model;
	}
	
	/*@RequestMapping(value = "pdfcashFlowSummaryReport", method = RequestMethod.GET)
	public ModelAndView downloadExcel() {
		
		CashFlowSummaryForm form = new CashFlowSummaryForm();
		form.setFromDate(fromDate);
		form.setToDate(toDate);
		form.setCompanyId(companyId);
		form.setLedgerList(ledgerList);
		form.setCompany(company);
		return new ModelAndView("CashFlowSummaryPdfView", "form", form);
	}*/

	
}
