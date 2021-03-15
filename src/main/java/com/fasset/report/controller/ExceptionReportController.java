package com.fasset.report.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.entities.ActivityLog;
import com.fasset.entities.Bank;
import com.fasset.entities.Company;
import com.fasset.entities.Contra;
import com.fasset.entities.Customer;
import com.fasset.entities.GstTaxMaster;
import com.fasset.entities.LoginLog;
import com.fasset.entities.OpeningBalances;
import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;
import com.fasset.entities.Product;
import com.fasset.entities.PurchaseEntry;
import com.fasset.entities.PurchaseEntryProductEntityClass;
import com.fasset.entities.Receipt;
import com.fasset.entities.Receipt_product_details;
import com.fasset.entities.SalesEntry;
import com.fasset.entities.SalesEntryProductEntityClass;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.DateForm;
import com.fasset.form.ExceptionReport1Form;
import com.fasset.form.ExceptionReport2form;
import com.fasset.form.ExceptionReport3Form;
import com.fasset.form.ExceptionReport4Form;
import com.fasset.form.ExceptionReport5Form;
import com.fasset.form.ExceptionReport6Form;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.SupplierCustomerLedgerForm;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ICompanyService;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.ICustomerService;
import com.fasset.service.interfaces.ILoginLogService;
import com.fasset.service.interfaces.IOpeningBalancesService;
import com.fasset.service.interfaces.IPaymentService;
import com.fasset.service.interfaces.IPurchaseEntryService;
import com.fasset.service.interfaces.IReceiptService;
import com.fasset.service.interfaces.ISalesEntryService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.service.interfaces.IUserService;
import com.fasset.service.interfaces.IYearEndingService;

@Controller
@SessionAttributes("user")
public class ExceptionReportController extends MyAbstractController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IYearEndingService yearService;

	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private IReceiptService receiptService;

	@Autowired
	private IOpeningBalancesService openingBalancesService;

	@Autowired
	private ILoginLogService loginLogService;

	@Autowired
	private IBankService bankService;

	@Autowired
	private ISalesEntryService salesService;

	@Autowired
	private IPurchaseEntryService purchaseService;

	@Autowired
	private ICompanyService companyService;
	
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	private ISubLedgerService subLedgerService;
	@Autowired
	private ISalesEntryService salesEntryService;

	@Autowired
	private IContraService contraservice;

	public List<Company> companyList = new ArrayList<Company>();
	private LocalDate fromDate;
	private LocalDate toDate;
	private Company company;
	private List<OpeningBalancesForm> bankOpenBalanceList = new ArrayList<OpeningBalancesForm>();
	private List<SupplierCustomerLedgerForm> dayBookList = new ArrayList<SupplierCustomerLedgerForm>();
	private List<ExceptionReport2form> monthlyAvgBalanceList = new ArrayList<ExceptionReport2form>();
	private List<Contra> contraList = new ArrayList<Contra>();
	private List<Receipt> receiptList = new ArrayList<Receipt>();
	private List<Payment> paymenttList = new ArrayList<Payment>();
	private List<SalesEntry> salesEntryList = new ArrayList<SalesEntry>();

	// Executives and Managers Activity Log. By this, we mean that for a period
	// selected,
	// we should be able to get a list of things done by them, like approval of
	// masters, quotes given etc.
	@RequestMapping(value = "activityLogReport", method = RequestMethod.GET)
	public ModelAndView activityLogReport(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();

		if (role == ROLE_SUPERUSER) {

			List<User> kpoList = userService.getManagerAndExecutive();
			kpoList.add(user);
			Collections.sort(kpoList, new Comparator<User>() {
				public int compare(User user1, User user2) {
					return user1.getFirst_name().trim().toLowerCase()
							.compareTo(user2.getFirst_name().trim().toLowerCase());
				}
			});
			model.addObject("form", form);
			model.addObject("kpoList", kpoList);

		} else if (role == ROLE_MANAGER) {

			List<User> kpoList = userService.findAllExecutiveOfManager(user.getUser_id());
			kpoList.add(user);
			Collections.sort(kpoList, new Comparator<User>() {
				public int compare(User user1, User user2) {
					return user1.getFirst_name().trim().toLowerCase()
							.compareTo(user2.getFirst_name().trim().toLowerCase());
				}
			});
			model.addObject("form", form);
			model.addObject("kpoList", kpoList);

		} else if (role == ROLE_EXECUTIVE) {
			model.addObject("form", form);

		}
		model.setViewName("/report/activityLogReportForKpo");
		return model;
	}

	@RequestMapping(value = "activityLogReport", method = RequestMethod.POST)
	public ModelAndView showActivityLogReport(@ModelAttribute("form") DateForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER || role == ROLE_MANAGER) {
			List<ActivityLog> loglist = yearService.findAllActivityLog(form.getUserId(), form.getFromDate(),
					form.getToDate());
			List<LoginLog> loginLogList = loginLogService.Loginlog(form.getUserId(), form.getFromDate(),
					form.getToDate());
			try {
				model.addObject("Approveduser", userService.getById(form.getUserId()));
			} catch (MyWebException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addObject("loglist", loglist);
			model.addObject("loginLogList", loginLogList);

		} else if (role == ROLE_EXECUTIVE) {

			List<ActivityLog> loglist = yearService.findAllActivityLog(user.getUser_id(), form.getFromDate(),
					form.getToDate());
			List<LoginLog> loginLogList = loginLogService.Loginlog(user.getUser_id(), form.getFromDate(),
					form.getToDate());
			model.addObject("loglist", loglist);
			model.addObject("Approveduser", user);
			model.addObject("loginLogList", loginLogList);

		}
		model.setViewName("/report/activityLogReportList");
		return model;
	}

	/** Cash payment and receipt of more Rs.10000/- and above (For Client) */
	@RequestMapping(value = "exceptionReport1", method = RequestMethod.GET)
	public ModelAndView cashPaymentAndReceipt(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/CashPaymentAndReceiptForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/CashPaymentAndReceiptForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/cashPaymentAndReceipt");
		}
		return model;
	}

	/** Cash payment and receipt of more Rs.10000/- and above (For Client) */
	@RequestMapping(value = "exceptionReport1", method = RequestMethod.POST)
	public ModelAndView ShowCashPaymentAndReceipt(@ModelAttribute("form") DateForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport1Form> exceptionReport1FormsList = new ArrayList<ExceptionReport1Form>();
		List<Receipt> receiptList = receiptService.CashReceiptOfMoreThanRS10000AndAbove(form.getFromDate(),
				form.getToDate(), user.getCompany().getCompany_id());
		List<Payment> paymentList = paymentService.CashPaymentOfMoreThanRS10000AndAbove(form.getFromDate(),
				form.getToDate(), user.getCompany().getCompany_id());
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport1Form exception1Form = new ExceptionReport1Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}

				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				exception1Form.setPaymentType("Cash");
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport1FormsList.add(exception1Form);

			}
		}

		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport1Form exception1Form = new ExceptionReport1Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");
				exception1Form.setPaymentType("Cash");
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport1FormsList.add(exception1Form);

			}

		}

		Collections.sort(exceptionReport1FormsList, new Comparator<ExceptionReport1Form>() {

			@Override
			public int compare(ExceptionReport1Form o1, ExceptionReport1Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("exceptionReport1FormsList", exceptionReport1FormsList);

		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.setViewName("/report/cashPaymentAndReceiptList");
		return model;
	}

	/** Cash payment and receipt of more Rs.10000/- and above (For Client) */
	@RequestMapping(value = "exceptionReport1ForKPO", method = RequestMethod.POST)
	public ModelAndView ShowCashPaymentAndReceiptForKPO(@ModelAttribute("form") DateForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport1Form> exceptionReport1FormsList = new ArrayList<ExceptionReport1Form>();
		List<Receipt> receiptList = receiptService.CashReceiptOfMoreThanRS10000AndAbove(form.getFromDate(),
				form.getToDate(), form.getCompanyId());
		List<Payment> paymentList = paymentService.CashPaymentOfMoreThanRS10000AndAbove(form.getFromDate(),
				form.getToDate(), form.getCompanyId());
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport1Form exception1Form = new ExceptionReport1Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}
				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");
				exception1Form.setPaymentType("Cash");
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport1FormsList.add(exception1Form);

			}
		}

		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport1Form exception1Form = new ExceptionReport1Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");
				exception1Form.setPaymentType("Cash");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport1FormsList.add(exception1Form);

			}

		}

		Collections.sort(exceptionReport1FormsList, new Comparator<ExceptionReport1Form>() {

			@Override
			public int compare(ExceptionReport1Form o1, ExceptionReport1Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("exceptionReport1FormsList", exceptionReport1FormsList);

		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.setViewName("/report/cashPaymentAndReceiptList");
		return model;
	}

	/** debitBalanceinAllBankAccountExceedingRs500000 */
	@RequestMapping(value = "exceptionReport2", method = RequestMethod.GET)
	public ModelAndView debitBalanceinAllBankAccountExceedingRs100000(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/debitBalanceinAllBankAccountExceedingRs100000ForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/debitBalanceinAllBankAccountExceedingRs100000ForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/debitBalanceinAllBankAccountExceedingRs100000");
		}
		return model;
	}

	/** debitBalanceinAllBankAccountExceedingRs500000 */
	/*
	 * @RequestMapping(value = "exceptionReport2", method = RequestMethod.POST)
	 * public ModelAndView
	 * showDebitBalanceinAllBankAccountExceedingRs100000(@ModelAttribute("form")
	 * DateForm form1, HttpServletRequest request, HttpServletResponse response) {
	 * ModelAndView model = new ModelAndView(); HttpSession session =
	 * request.getSession(true); User user = (User) session.getAttribute("user");
	 * fromDate = form1.getFromDate(); toDate = form1.getToDate(); try { company =
	 * companyService.getCompanyWithCompanyStautarType(user.getCompany().
	 * getCompany_id()); } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * Integer countdays=null; countdays = Days.daysBetween(form1.getFromDate(),
	 * form1.getToDate()).getDays()+1; receiptList.clear(); paymenttList.clear();
	 * contraList.clear(); salesEntryList.clear(); bankOpenBalanceList.clear();
	 * dayBookList.clear(); monthlyAvgBalanceList.clear(); List<Bank> banklist =
	 * bankService.findAllBanksOfCompany(user.getCompany().getCompany_id());
	 * 
	 * for(Object bal[]
	 * :openingBalancesService.findAllOPbalancesforBankBeforeFromDate(form1.
	 * getFromDate(), user.getCompany().getCompany_id())) { if(bal[0]!=null &&
	 * bal[1]!=null && bal[2]!=null) { Bank bank = null; try { bank =
	 * bankService.getById((long)bal[0]); } catch (Exception e) {
	 * e.printStackTrace(); } if(bank!=null &&
	 * bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY
	 * )) { OpeningBalancesForm form = new OpeningBalancesForm();
	 * form.setBank_id((long)bal[0]); form.setCredit_balance((double)bal[2]);
	 * form.setDebit_balance((double)bal[1]); bankOpenBalanceList.add(form); } } }
	 * 
	 * for(Bank bank : banklist) { Double ruuningBalance = (double)0; Double
	 * monthlyAvgBalance = (double)0; dayBookList.clear(); LocalDate
	 * startdateOfMonth = form1.getFromDate(); paymenttList =
	 * paymentService.getPaymentListForLedgerReport(form1.getFromDate(),
	 * form1.getToDate(), user.getCompany().getCompany_id(),bank.getBank_id());
	 * receiptList =
	 * receiptService.getReceiptListForLedgerReport(form1.getFromDate(),
	 * form1.getToDate(), user.getCompany().getCompany_id(),bank.getBank_id());
	 * contraList = contraservice.getContraListForLedgerReport(form1.getFromDate(),
	 * form1.getToDate(), user.getCompany().getCompany_id(),bank.getBank_id());
	 * salesEntryList =
	 * salesEntryService.getCardSalesForLedgerReport(form1.getFromDate(),
	 * form1.getToDate(), user.getCompany().getCompany_id(),bank.getBank_id());
	 * 
	 * for(SalesEntry sales :salesEntryList) { if(sales.getLocal_time()!=null) {
	 * SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm(); Double
	 * amount = (double)0; if(sales.getCreated_date()!=null) {
	 * form.setDate(sales.getCreated_date()); } if(sales.getRound_off()!=null) {
	 * amount=amount+sales.getRound_off(); } form.setDebit(amount);
	 * form.setLocal_time(sales.getLocal_time()); form.setType(4);
	 * dayBookList.add(form); } }
	 * 
	 * for(Contra contra :contraList) { if(contra.getLocal_time()!=null) {
	 * if((contra.getDeposite_to()!=null &&
	 * !contra.getType().equals(3))||(contra.getWithdraw_from()!=null)) {
	 * SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm(); Double
	 * amount = (double)0; if(contra.getDate()!=null) {
	 * form.setDate(contra.getDate()); } form.setContratType(contra.getType());
	 * if(contra.getType().equals(2)) { if(contra.getAmount()!=null) {
	 * amount=amount+contra.getAmount(); } form.setCredit(amount);
	 * 
	 * } else if(contra.getType().equals(1)) { if(contra.getAmount()!=null) {
	 * amount=amount+contra.getAmount(); } form.setDebit(amount);
	 * 
	 * } else { if(contra.getAmount()!=null) { amount=amount+contra.getAmount(); }
	 * form.setCredit(amount); }
	 * 
	 * form.setLocal_time(contra.getLocal_time()); form.setType(7);
	 * dayBookList.add(form); } } }
	 * 
	 * for(Receipt receipt :receiptList) { if(receipt.getLocal_time()!=null) {
	 * SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm(); Double
	 * Tds = (double)0; Double Tax = (double)0; Double amount = (double)0;
	 * if(receipt.getDate()!=null) { form.setDate(receipt.getDate()); }
	 * if(receipt.getAdvance_payment()!=null &&
	 * receipt.getAdvance_payment().equals(new Boolean(true))) {
	 * if(receipt.getTds_amount()!=null) { Tds=Tds+receipt.getTds_amount(); }
	 * if(receipt.getAmount()!=null) { amount=amount+receipt.getAmount(); }
	 * 
	 * if(receipt.getCgst()!=null) { Tax=Tax+receipt.getCgst(); }
	 * if(receipt.getSgst()!=null) { Tax=Tax+receipt.getSgst(); }
	 * if(receipt.getIgst()!=null) { Tax=Tax+receipt.getIgst(); }
	 * if(receipt.getState_compansation_tax()!=null) {
	 * Tax=Tax+receipt.getState_compansation_tax(); }
	 * 
	 * form.setDebit(amount-Tax);
	 * 
	 * } if(receipt.getAdvance_payment()!=null &&
	 * receipt.getAdvance_payment().equals(new Boolean(false))) {
	 * if(receipt.getAmount()!=null) { amount=amount+receipt.getAmount(); }
	 * if(receipt.getCgst()!=null) { Tax=Tax+receipt.getCgst(); }
	 * if(receipt.getSgst()!=null) { Tax=Tax+receipt.getSgst(); }
	 * if(receipt.getIgst()!=null) { Tax=Tax+receipt.getIgst(); }
	 * if(receipt.getState_compansation_tax()!=null) {
	 * Tax=Tax+receipt.getState_compansation_tax(); }
	 * 
	 * form.setDebit(amount-Tax); }
	 * 
	 * form.setLocal_time(receipt.getLocal_time()); form.setType(2);
	 * dayBookList.add(form); } }
	 * 
	 * for(Payment payment :paymenttList) { if(payment.getLocal_time()!=null) {
	 * SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm(); Double
	 * Tds = (double)0; Double Tax = (double)0; Double amount = (double)0;
	 * if(payment.getDate()!=null) { form.setDate(payment.getDate()); }
	 * if(payment.getAdvance_payment()!=null &&
	 * payment.getAdvance_payment().equals(new Boolean(true))) {
	 * if(payment.getTds_amount()!=null) { Tds=Tds+payment.getTds_amount(); }
	 * if(payment.getAmount()!=null) { amount=amount+payment.getAmount(); }
	 * if(payment.getCGST_head()!=null) { Tax=Tax+payment.getCGST_head(); }
	 * if(payment.getSGST_head()!=null) { Tax=Tax+payment.getSGST_head(); }
	 * if(payment.getIGST_head()!=null) { Tax=Tax+payment.getIGST_head(); }
	 * if(payment.getSCT_head()!=null) { Tax=Tax+payment.getSCT_head(); }
	 * form.setCredit(amount-Tax);
	 * 
	 * 
	 * 
	 * } if(payment.getAdvance_payment()!=null &&
	 * payment.getAdvance_payment().equals(new Boolean(false))) {
	 * if(payment.getAmount()!=null) { amount=amount+payment.getAmount(); }
	 * if(payment.getCGST_head()!=null) { Tax=Tax+payment.getCGST_head(); }
	 * if(payment.getSGST_head()!=null) { Tax=Tax+payment.getSGST_head(); }
	 * if(payment.getIGST_head()!=null) { Tax=Tax+payment.getIGST_head(); }
	 * if(payment.getSCT_head()!=null) { Tax=Tax+payment.getSCT_head(); }
	 * form.setCredit(amount-Tax); }
	 * 
	 * form.setLocal_time(payment.getLocal_time()); form.setType(1);
	 * dayBookList.add(form); } } Collections.sort(dayBookList, new
	 * Comparator<SupplierCustomerLedgerForm>() { public int
	 * compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {
	 * 
	 * LocalDate Date1 = o1.getDate(); LocalDate Date2= o2.getDate(); int sComp =
	 * Date1.compareTo(Date2);
	 * 
	 * if (sComp != 0) { return sComp; }
	 * 
	 * LocalTime local_time1 = o1.getLocal_time(); LocalTime local_time2=
	 * o2.getLocal_time(); return local_time1.compareTo(local_time2); }});
	 * 
	 * for(OpeningBalancesForm form:bankOpenBalanceList) {
	 * if(bank.getBank_id().equals(form.getBank_id())) {
	 * ruuningBalance=form.getDebit_balance()-form.getCredit_balance(); } }
	 * 
	 * for(int i=1;i<=countdays;i++) { Boolean flag1 = false; Boolean flag2 = false;
	 * for(SupplierCustomerLedgerForm form:dayBookList) {
	 * if(startdateOfMonth.equals(form.getDate())) { flag1=true; flag2=true;
	 * if(form.getType().equals(4)) { ruuningBalance=ruuningBalance+form.getDebit();
	 * } if(form.getType().equals(2)) {
	 * ruuningBalance=ruuningBalance+form.getDebit(); } if(form.getType().equals(1))
	 * { ruuningBalance=ruuningBalance-form.getCredit(); }
	 * if(form.getType().equals(7)) { if(form.getContratType().equals(1)) {
	 * ruuningBalance=ruuningBalance+form.getDebit(); }
	 * if(form.getContratType().equals(2)) {
	 * ruuningBalance=ruuningBalance-form.getCredit(); }
	 * if(form.getContratType().equals(3)) {
	 * ruuningBalance=ruuningBalance-form.getCredit(); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * if(flag1==true && flag2==true) {
	 * monthlyAvgBalance=monthlyAvgBalance+ruuningBalance; flag2=false;
	 * 
	 * }
	 * 
	 * 
	 * } if(flag1==false) { monthlyAvgBalance=monthlyAvgBalance+ruuningBalance; }
	 * startdateOfMonth = form1.getFromDate().plusDays(i); }
	 * 
	 * monthlyAvgBalance = (monthlyAvgBalance/countdays);
	 * if(monthlyAvgBalance>500000) { ExceptionReport2form form= new
	 * ExceptionReport2form(); form.setMonthAvgBalance(monthlyAvgBalance);
	 * form.setBankName(bank.getBank_name()); monthlyAvgBalanceList.add(form); } }
	 * 
	 * model.addObject("company",company); model.addObject("from_date",fromDate);
	 * model.addObject("to_date",toDate);
	 * model.addObject("monthlyAvgBalanceList",monthlyAvgBalanceList);
	 * model.setViewName("/report/debitBalanceinAllBankAccountExceedingRs100000List"
	 * ); return model; }
	 */

	/** debitBalanceinAllBankAccountExceedingRs500000 */
	@RequestMapping(value = "exceptionReport2ForKPO", method = RequestMethod.POST)
	public ModelAndView showDebitBalanceinAllBankAccountExceedingRs100000ForKPO(@ModelAttribute("form") DateForm form1,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		fromDate = form1.getFromDate();
		toDate = form1.getToDate();
		companyList = companyService.getAllCompaniesOnly();
		monthlyAvgBalanceList.clear();
		for (Company comp : companyList) {
			Double monthlyAvgBalanceOfAllbanksOfOneCompany = (double) 0;
			Integer countdays = null;
			countdays = Days.daysBetween(form1.getFromDate(), form1.getToDate()).getDays() + 1;
			receiptList.clear();
			paymenttList.clear();
			contraList.clear();
			salesEntryList.clear();
			bankOpenBalanceList.clear();
			dayBookList.clear();

			List<Bank> banklist = bankService.findAllBanksOfCompany(comp.getCompany_id());

			for (Object bal[] : openingBalancesService.findAllOPbalancesforBankBeforeFromDate(form1.getFromDate(),
					comp.getCompany_id())) {
				if (bal[0] != null && bal[1] != null && bal[2] != null) {
					Bank bank = null;
					try {
						bank = bankService.getById((long) bal[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (bank != null
							&& bank.getBank_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) {
						OpeningBalancesForm form = new OpeningBalancesForm();
						form.setBank_id((long) bal[0]);
						form.setCredit_balance((double) bal[2]);
						form.setDebit_balance((double) bal[1]);
						bankOpenBalanceList.add(form);
					}
				}
			}

			for (Bank bank : banklist) {
				Double ruuningBalance = (double) 0;
				Double monthlyAvgBalance = (double) 0;
				dayBookList.clear();
				LocalDate startdateOfMonth = form1.getFromDate();
				paymenttList = paymentService.getPaymentListForLedgerReport(form1.getFromDate(), form1.getToDate(),
						comp.getCompany_id(), bank.getBank_id());
				receiptList = receiptService.getReceiptListForLedgerReport(form1.getFromDate(), form1.getToDate(),
						comp.getCompany_id(), bank.getBank_id());
				contraList = contraservice.getContraListForLedgerReport(form1.getFromDate(), form1.getToDate(),
						comp.getCompany_id(), bank.getBank_id());
				salesEntryList = salesEntryService.getCardSalesForLedgerReport(form1.getFromDate(), form1.getToDate(),
						comp.getCompany_id(), bank.getBank_id());

				for (SalesEntry sales : salesEntryList) {
					if (sales.getLocal_time() != null) {
						SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
						Double amount = (double) 0;
						if (sales.getCreated_date() != null) {
							form.setDate(sales.getCreated_date());
						}
						if (sales.getRound_off() != null) {
							amount = amount + sales.getRound_off();
						}
						form.setDebit(amount);
						form.setLocal_time(sales.getLocal_time());
						form.setType(4);
						dayBookList.add(form);
					}
				}

				for (Contra contra : contraList) {
					if (contra.getLocal_time() != null) {
						if ((contra.getDeposite_to() != null && !contra.getType().equals(3))
								|| (contra.getWithdraw_from() != null)) {
							SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
							Double amount = (double) 0;
							if (contra.getDate() != null) {
								form.setDate(contra.getDate());
							}
							form.setContratType(contra.getType());
							if (contra.getType().equals(2)) {
								if (contra.getAmount() != null) {
									amount = amount + contra.getAmount();
								}
								form.setCredit(amount);

							} else if (contra.getType().equals(1)) {
								if (contra.getAmount() != null) {
									amount = amount + contra.getAmount();
								}
								form.setDebit(amount);

							} else {
								if (contra.getAmount() != null) {
									amount = amount + contra.getAmount();
								}
								form.setCredit(amount);
							}

							form.setLocal_time(contra.getLocal_time());
							form.setType(7);
							dayBookList.add(form);
						}
					}
				}

				for (Receipt receipt : receiptList) {
					if (receipt.getLocal_time() != null) {
						SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
						Double Tds = (double) 0;
						Double Tax = (double) 0;
						Double amount = (double) 0;
						if (receipt.getDate() != null) {
							form.setDate(receipt.getDate());
						}
						if (receipt.getAdvance_payment() != null
								&& receipt.getAdvance_payment().equals(new Boolean(true))) {
							if (receipt.getTds_amount() != null) {
								Tds = Tds + receipt.getTds_amount();
							}
							if (receipt.getAmount() != null) {
								amount = amount + receipt.getAmount();
							}

							if (receipt.getCgst() != null) {
								Tax = Tax + receipt.getCgst();
							}
							if (receipt.getSgst() != null) {
								Tax = Tax + receipt.getSgst();
							}
							if (receipt.getIgst() != null) {
								Tax = Tax + receipt.getIgst();
							}
							if (receipt.getState_compansation_tax() != null) {
								Tax = Tax + receipt.getState_compansation_tax();
							}

							form.setDebit(amount - Tax);

						}
						if (receipt.getAdvance_payment() != null
								&& receipt.getAdvance_payment().equals(new Boolean(false))) {
							if (receipt.getAmount() != null) {
								amount = amount + receipt.getAmount();
							}
							if (receipt.getCgst() != null) {
								Tax = Tax + receipt.getCgst();
							}
							if (receipt.getSgst() != null) {
								Tax = Tax + receipt.getSgst();
							}
							if (receipt.getIgst() != null) {
								Tax = Tax + receipt.getIgst();
							}
							if (receipt.getState_compansation_tax() != null) {
								Tax = Tax + receipt.getState_compansation_tax();
							}

							form.setDebit(amount - Tax);
						}

						form.setLocal_time(receipt.getLocal_time());
						form.setType(2);
						dayBookList.add(form);
					}
				}

				for (Payment payment : paymenttList) {
					if (payment.getLocal_time() != null) {
						SupplierCustomerLedgerForm form = new SupplierCustomerLedgerForm();
						Double Tds = (double) 0;
						Double Tax = (double) 0;
						Double amount = (double) 0;
						if (payment.getDate() != null) {
							form.setDate(payment.getDate());
						}
						if (payment.getAdvance_payment() != null
								&& payment.getAdvance_payment().equals(new Boolean(true))) {
							if (payment.getTds_amount() != null) {
								Tds = Tds + payment.getTds_amount();
							}
							if (payment.getAmount() != null) {
								amount = amount + payment.getAmount();
							}
							if (payment.getCGST_head() != null) {
								Tax = Tax + payment.getCGST_head();
							}
							if (payment.getSGST_head() != null) {
								Tax = Tax + payment.getSGST_head();
							}
							if (payment.getIGST_head() != null) {
								Tax = Tax + payment.getIGST_head();
							}
							if (payment.getSCT_head() != null) {
								Tax = Tax + payment.getSCT_head();
							}
							form.setCredit(amount - Tax);

						}
						if (payment.getAdvance_payment() != null
								&& payment.getAdvance_payment().equals(new Boolean(false))) {
							if (payment.getAmount() != null) {
								amount = amount + payment.getAmount();
							}
							if (payment.getCGST_head() != null) {
								Tax = Tax + payment.getCGST_head();
							}
							if (payment.getSGST_head() != null) {
								Tax = Tax + payment.getSGST_head();
							}
							if (payment.getIGST_head() != null) {
								Tax = Tax + payment.getIGST_head();
							}
							if (payment.getSCT_head() != null) {
								Tax = Tax + payment.getSCT_head();
							}
							form.setCredit(amount - Tax);
						}

						form.setLocal_time(payment.getLocal_time());
						form.setType(1);
						dayBookList.add(form);
					}
				}
				Collections.sort(dayBookList, new Comparator<SupplierCustomerLedgerForm>() {
					public int compare(SupplierCustomerLedgerForm o1, SupplierCustomerLedgerForm o2) {

						LocalDate Date1 = o1.getDate();
						LocalDate Date2 = o2.getDate();
						int sComp = Date1.compareTo(Date2);

						if (sComp != 0) {
							return sComp;
						}

						LocalTime local_time1 = o1.getLocal_time();
						LocalTime local_time2 = o2.getLocal_time();
						return local_time1.compareTo(local_time2);
					}
				});

				for (OpeningBalancesForm form : bankOpenBalanceList) {
					if (bank.getBank_id().equals(form.getBank_id())) {
						ruuningBalance = form.getDebit_balance() - form.getCredit_balance();
					}
				}

				for (int i = 1; i <= countdays; i++) {
					Boolean flag1 = false;
					Boolean flag2 = false;
					for (SupplierCustomerLedgerForm form : dayBookList) {
						if (startdateOfMonth.equals(form.getDate())) {
							flag1 = true;
							flag2 = true;
							if (form.getType().equals(4)) {
								ruuningBalance = ruuningBalance + form.getDebit();
							}
							if (form.getType().equals(2)) {
								ruuningBalance = ruuningBalance + form.getDebit();
							}
							if (form.getType().equals(1)) {
								ruuningBalance = ruuningBalance - form.getCredit();
							}
							if (form.getType().equals(7)) {
								if (form.getContratType().equals(1)) {
									ruuningBalance = ruuningBalance + form.getDebit();
								}
								if (form.getContratType().equals(2)) {
									ruuningBalance = ruuningBalance - form.getCredit();
								}
								if (form.getContratType().equals(3)) {
									ruuningBalance = ruuningBalance - form.getCredit();
								}

							}

						}

						if (flag1 == true && flag2 == true) {
							monthlyAvgBalance = monthlyAvgBalance + ruuningBalance;
							flag2 = false;

						}

					}
					if (flag1 == false) {
						monthlyAvgBalance = monthlyAvgBalance + ruuningBalance;
					}
					startdateOfMonth = form1.getFromDate().plusDays(i);
				}

				monthlyAvgBalance = (monthlyAvgBalance / countdays);
				monthlyAvgBalanceOfAllbanksOfOneCompany = monthlyAvgBalanceOfAllbanksOfOneCompany + monthlyAvgBalance;

			}

			if (monthlyAvgBalanceOfAllbanksOfOneCompany > 500000) {
				ExceptionReport2form form = new ExceptionReport2form();
				form.setMonthAvgBalance(monthlyAvgBalanceOfAllbanksOfOneCompany);
				form.setCompanyName(comp.getCompany_name());
				monthlyAvgBalanceList.add(form);
			}

		}
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("monthlyAvgBalanceList", monthlyAvgBalanceList);
		model.setViewName("/report/debitBalanceinAllBankAccountExceedingRs100000List");
		return model;
	}

	/** Cash payment and receipt Unadjusted (For Client) */
	@RequestMapping(value = "exceptionReport3", method = RequestMethod.GET)
	public ModelAndView cashPaymentAndReceiptUnadjused(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");

		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashPaymentAndReceiptHavingAdvanceUnadjustedForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashPaymentAndReceiptHavingAdvanceUnadjustedForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/cashPaymentAndReceiptHavingAdvanceUnadjusted");
		}
		return model;
	}

	/**
	 * Cash payment and receipt Unadjusted (For Client)
	 */
	@RequestMapping(value = "exceptionReport3", method = RequestMethod.POST)
	public ModelAndView showCashPaymentAndReceiptUnadjusted(@ModelAttribute("form") DateForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ExceptionReport3Form> exceptionReport3FormsList = new ArrayList<ExceptionReport3Form>();

		List<Receipt> receiptList = receiptService.customerReceiptHavingUnadjustedUnadjusted(form.getFromDate(),
				form.getToDate(), user.getCompany().getCompany_id());
		List<Payment> paymentList = paymentService.supplierPaymentHavingUnadjusted(form.getFromDate(), form.getToDate(),
				user.getCompany().getCompany_id());

		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}

				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				Double amount = (double) 0;
				Integer days = 0;

				if (payment.getAmount() != null) {
					amount = amount + payment.getAmount();
				}
				exception1Form.setTotalAmount(amount);
				if (payment.getAgingDays() != null) {
					days = days + payment.getAgingDays();

				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport3FormsList.add(exception1Form);

			}
		}
		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}

				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}

				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");

				Double amount = (double) 0;
				Integer days = 0;
				if (receipt.getAmount() != null) {
					amount = amount + receipt.getAmount();
				}
				exception1Form.setTotalAmount(amount);

				if (receipt.getAgingDays() != null) {
					days = days + receipt.getAgingDays();
				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport3FormsList.add(exception1Form);

			}

		}

		Collections.sort(exceptionReport3FormsList, new Comparator<ExceptionReport3Form>() {

			@Override
			public int compare(ExceptionReport3Form o1, ExceptionReport3Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("exceptionReport3FormsList", exceptionReport3FormsList);
		model.setViewName("/report/cashPaymentAndReceiptHavingAdvanceUnadjustedList");
		return model;
	}

	@RequestMapping(value = "exceptionReport3ForKPO", method = RequestMethod.POST)
	public ModelAndView showCashPaymentAndReceiptUnadjustedForKPO(@ModelAttribute("form") DateForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
	//	toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport3Form> exceptionReport3FormsList = new ArrayList<ExceptionReport3Form>();
		List<Receipt> receiptList = receiptService.customerReceiptHavingUnadjustedUnadjusted(form.getFromDate(),
				form.getToDate(), form.getCompanyId());
		List<Payment> paymentList = paymentService.supplierPaymentHavingUnadjusted(form.getFromDate(), form.getToDate(),
				form.getCompanyId());
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}

				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				Double amount = (double) 0;
				Integer days = 0;

				if (payment.getAmount() != null) {
					amount = amount + payment.getAmount();
				}
				exception1Form.setTotalAmount(amount);
				if (payment.getAgingDays() != null) {
					days = days + payment.getAgingDays();

				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport3FormsList.add(exception1Form);

			}
		}
		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport3Form exception1Form = new ExceptionReport3Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}

				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");

				Double amount = (double) 0;
				Integer days = 0;
				if (receipt.getAmount() != null) {
					amount = amount + receipt.getAmount();
				}
				exception1Form.setTotalAmount(amount);

				if (receipt.getAgingDays() != null) {
					days = days + receipt.getAgingDays();
				}
				exception1Form.setNumberOfDays(days);
				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport3FormsList.add(exception1Form);

			}

		}
		Collections.sort(exceptionReport3FormsList, new Comparator<ExceptionReport3Form>() {

			@Override
			public int compare(ExceptionReport3Form o1, ExceptionReport3Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		//model.addObject("to_date", toDate);
		model.addObject("exceptionReport3FormsList", exceptionReport3FormsList);
		model.setViewName("/report/cashPaymentAndReceiptHavingAdvanceUnadjustedList");
		return model;
	}

	/** supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZero */
	@RequestMapping(value = "exceptionReport4", method = RequestMethod.GET)
	public ModelAndView supplierAndCustomerHavingGSTApplicbleAsYesAndTax0(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZeroForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZeroForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZero");
		}

		return model;
	}

	/** supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZero */
	@RequestMapping(value = "exceptionReport4", method = RequestMethod.POST)
	public ModelAndView showSupplierAndCustomerHavingGSTApplicbleAsYesAndTax0(@ModelAttribute("form") DateForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
System.out.println("excess exception report");
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport4Form> exceptionReport4FormsList = new ArrayList<ExceptionReport4Form>();
		List<SalesEntry> salesList = salesService.customerSalesHavingGST0(form.getFromDate(), form.getToDate(),
				user.getCompany().getCompany_id());
		List<PurchaseEntry> purchaseEntryList = purchaseService.supplirPurchaseHavingGST0(form.getFromDate(),
				form.getToDate(), user.getCompany().getCompany_id());
		List<Receipt> receiptList = receiptService.customerReceiptHavingGST0(form.getFromDate(), form.getToDate(),
				user.getCompany().getCompany_id());
		List<Payment> paymentList = paymentService.supplirPaymentHavingGST0(form.getFromDate(), form.getToDate(),
				user.getCompany().getCompany_id());
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}
				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);

				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}
		}

		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}

				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}

				exception1Form.setTotalAmount(amount + Tds);

				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}

		}

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Sales");
				if (sales.getVoucher_no() != null) {
					exception1Form.setInvoiceNo(sales.getVoucher_no());
				}

				Double amount = (double) 0;

				if (sales.getRound_off() != null) {
					amount = amount + sales.getRound_off();
				}
				exception1Form.setTotalAmount(amount);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}
				if (purchase.getSupplier().getCompany_name() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Purchase");
				// supplier_bill_no}
				if (purchase.getSupplier_bill_no() != null) {
					exception1Form.setInvoiceNo(purchase.getSupplier_bill_no());
				}

				Double amount = (double) 0;

				if (purchase.getRound_off() != null) {
					amount = amount + purchase.getRound_off();
				}
				exception1Form.setTotalAmount(amount);
				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		Collections.sort(exceptionReport4FormsList, new Comparator<ExceptionReport4Form>() {

			@Override
			public int compare(ExceptionReport4Form o1, ExceptionReport4Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("exceptionReport4FormsList", exceptionReport4FormsList);
		model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZeroList");
		return model;
	}

	/** supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZero */
	@RequestMapping(value = "exceptionReport4ForKPO", method = RequestMethod.POST)
	public ModelAndView showSupplierAndCustomerHavingGSTApplicbleAsYesAndTax0ForKPO(
			@ModelAttribute("form") DateForm form, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ExceptionReport4Form> exceptionReport4FormsList = new ArrayList<ExceptionReport4Form>();
		List<SalesEntry> salesList = salesService.customerSalesHavingGST0(form.getFromDate(), form.getToDate(),
				form.getCompanyId());
		List<PurchaseEntry> purchaseEntryList = purchaseService.supplirPurchaseHavingGST0(form.getFromDate(),
				form.getToDate(), form.getCompanyId());
		List<Receipt> receiptList = receiptService.customerReceiptHavingGST0(form.getFromDate(), form.getToDate(),
				form.getCompanyId());
		List<Payment> paymentList = paymentService.supplirPaymentHavingGST0(form.getFromDate(), form.getToDate(),
				form.getCompanyId());

		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (payment.getDate() != null) {
					exception1Form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					exception1Form.setSuppliers(payment.getSupplier().getCompany_name());
				}

				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers(payment.getSubLedger().getSubledger_name());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Payment");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);
				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}
		}

		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					exception1Form.setCustomer(receipt.getCustomer().getFirm_name());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer(receipt.getSubLedger().getSubledger_name());
				}

				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Receipt");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				exception1Form.setTotalAmount(amount + Tds);

				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);

			}

		}

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Sales");
				if (sales.getVoucher_no() != null) {
					exception1Form.setInvoiceNo(sales.getVoucher_no());
				}

				Double amount = (double) 0;

				if (sales.getRound_off() != null) {
					amount = amount + sales.getRound_off();
				}
				exception1Form.setTotalAmount(amount);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport4Form exception1Form = new ExceptionReport4Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}
				if (purchase.getSupplier().getCompany_name() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}
				exception1Form.setVoucher_Type("Purchase");
				// supplier_bill_no}
				if (purchase.getSupplier_bill_no() != null) {
					exception1Form.setInvoiceNo(purchase.getSupplier_bill_no());
				}

				Double amount = (double) 0;

				if (purchase.getRound_off() != null) {
					amount = amount + purchase.getRound_off();
				}
				exception1Form.setTotalAmount(amount);
				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport4FormsList.add(exception1Form);
			}

		}
		Collections.sort(exceptionReport4FormsList, new Comparator<ExceptionReport4Form>() {

			@Override
			public int compare(ExceptionReport4Form o1, ExceptionReport4Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("exceptionReport4FormsList", exceptionReport4FormsList);
		model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsYesAndTaxZeroList");
		return model;

	}

	/** supplierAndCustomerHaviPrevngGSTApplicbleAsNOAndTaxExceedingZero */
	@RequestMapping(value = "exceptionReport5", method = RequestMethod.GET)
	public ModelAndView SupplierHavingGSTApplicbleAsNOAndExceedingZERO(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZeroForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZeroForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZero");
		}

		return model;
	}

	/** supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZero */
	@RequestMapping(value = "exceptionReport5", method = RequestMethod.POST)
	public ModelAndView ShowsupplierHavingGSTApplicbleAsNOAndExceedingZERO(@ModelAttribute("form") DateForm form1,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
System.out.println("customer excess report");
		fromDate = form1.getFromDate();
		toDate = form1.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<SalesEntry> salesList = new ArrayList<>();
		List<ExceptionReport5Form> exceptionReport5List = new ArrayList<ExceptionReport5Form>();
		for (SalesEntry entry : salesService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), user.getCompany().getCompany_id())) {
			entry.setProductinfoList(salesService.findAllSalesEntryProductEntityList(entry.getSales_id()));
			salesList.add(entry);
		}
		List<PurchaseEntry> purchaseEntryList = new ArrayList<>();

		for (PurchaseEntry entry : purchaseService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), user.getCompany().getCompany_id())) {
			entry.setProductinfoList(purchaseService.findAllPurchaseEntryProductEntityList(entry.getPurchase_id()));
			purchaseEntryList.add(entry);
		}
		List<Receipt> receciptList = new ArrayList<>();
		for (Receipt receipt : receiptService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), user.getCompany().getCompany_id())) {
			receipt.setProductinfoList(receiptService.findAllReceiptProductEntityList(receipt.getReceipt_id()));
			receciptList.add(receipt);
		}
		List<Payment> paymentList = paymentService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), user.getCompany().getCompany_id());

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (sales.getCreated_date() != null) {
					form.setDate(sales.getCreated_date());
				}
				if (sales.getCustomer() != null) {
					form.setCusSupplierName(sales.getCustomer().getFirm_name());

				}
				if (sales.getVoucher_no() != null) {
					form.setVoucherNumber(sales.getVoucher_no());
				}
				if (sales.getVoucher_no() != null) {
					form.setInvocieNumber(sales.getVoucher_no());
				}
				for (SalesEntryProductEntityClass info : sales.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if (sales.getCgst() != null) {
					form.setCgst(sales.getCgst());

				}
				form.setVoucherType("Sales");
				if (sales.getSgst() != null) {
					form.setSgst(sales.getSgst());

				}
				if (sales.getIgst() != null) {
					form.setIgst(sales.getIgst());

				}
				if (sales.getRound_off() != null) {
					form.setInvoiceValue(sales.getRound_off());
				}
				form.setLocaltime(sales.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (purchase.getSupplier_bill_date() != null) {
					form.setDate(purchase.getSupplier_bill_date());
				}
				if (purchase.getSupplier() != null) {
					form.setCusSupplierName(purchase.getSupplier().getCompany_name());

				}
				if (purchase.getVoucher_no() != null) {
					form.setVoucherNumber(purchase.getVoucher_no());
				}
				if (purchase.getSupplier_bill_no() != null) {
					form.setInvocieNumber(purchase.getSupplier_bill_no());
				}
				for (PurchaseEntryProductEntityClass info : purchase.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if (purchase.getCgst() != null) {
					form.setCgst(purchase.getCgst());

				}
				form.setVoucherType("Purchase");
				if (purchase.getSgst() != null) {
					form.setSgst(purchase.getSgst());

				}
				if (purchase.getIgst() != null) {
					form.setIgst(purchase.getIgst());

				}
				if (purchase.getRound_off() != null) {
					form.setInvoiceValue(purchase.getRound_off());
				}
				form.setLocaltime(purchase.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (Receipt receipt : receciptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (receipt.getDate() != null) {
					form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					form.setCusSupplierName(receipt.getCustomer().getFirm_name());

				}
				if (receipt.getVoucher_no() != null) {
					form.setVoucherNumber(receipt.getVoucher_no());
				}
				if (receipt.getVoucher_no() != null) {
					form.setInvocieNumber(receipt.getVoucher_no());
				}
				for (Receipt_product_details info : receipt.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Receipt");
				if (receipt.getCgst() != null) {
					form.setCgst(receipt.getCgst());

				}
				if (receipt.getSgst() != null) {
					form.setSgst(receipt.getSgst());

				}
				if (receipt.getIgst() != null) {
					form.setIgst(receipt.getIgst());

				}
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getTds_amount() != null) {
					Tds = Tds + receipt.getTds_amount();
				}
				if (receipt.getAmount() != null) {
					amount = amount + receipt.getAmount();
				}
				form.setInvoiceValue(Tds + amount);

				form.setLocaltime(receipt.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (payment.getDate() != null) {
					form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					form.setCusSupplierName(payment.getSupplier().getCompany_name());

				}
				if (payment.getVoucher_no() != null) {
					form.setVoucherNumber(payment.getVoucher_no());
				}
				if (payment.getSupplier_bill_no() != null) {
					form.setInvocieNumber(payment.getVoucher_no());
				}
				for (PaymentDetails info : payment.getPaymentDetails()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFrieght();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Payment");
				if (payment.getCGST_head() != null) {
					form.setCgst(payment.getCGST_head());

				}
				if (payment.getSGST_head() != null) {
					form.setSgst(payment.getSGST_head());

				}
				if (payment.getIGST_head() != null) {
					form.setIgst(payment.getIGST_head());

				}
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getTds_amount() != null) {
					Tds = Tds + payment.getTds_amount();
				}
				if (payment.getAmount() != null) {
					amount = amount + payment.getAmount();
				}
				form.setInvoiceValue(Tds + amount);
				form.setLocaltime(payment.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		Collections.sort(exceptionReport5List, new Comparator<ExceptionReport5Form>() {
			public int compare(ExceptionReport5Form o1, ExceptionReport5Form o2) {

				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocaltime();
				LocalTime local_time2 = o2.getLocaltime();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("salesList", salesList);
		model.addObject("exceptionReport5List", exceptionReport5List);
		model.addObject("purchaseEntryList", purchaseEntryList);
		model.addObject("receciptList", receciptList);
		model.addObject("paymentList", paymentList);
		model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZeroList");
		return model;
	}

	@RequestMapping(value = "exceptionReport5ForKPO", method = RequestMethod.POST)
	public ModelAndView ShowsupplierHavingGSTApplicbleAsNOAndExceedingZEROForKPO(@ModelAttribute("form") DateForm form1,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DecimalFormat df = new DecimalFormat("#.##");
		fromDate = form1.getFromDate();
		toDate = form1.getToDate();
		System.out.println("ShowsupplierHavingGSTApplicbleAsNOAndExceedingZEROForKPO");
		try {
			company = companyService.getCompanyWithCompanyStautarType(form1.getCompanyId());
		} catch (Exception e) {

			e.printStackTrace();
		}
		List<SalesEntry> salesList = new ArrayList<>();
		List<ExceptionReport5Form> exceptionReport5List = new ArrayList<ExceptionReport5Form>();
		List<SalesEntry> saleList = salesService.salesEntryWithEditedGSTvalues(form1.getFromDate(),
				form1.getToDate(), form1.getCompanyId());
		for(SalesEntry entry:saleList){
			Long cusId=entry.getCustomer().getCustomer_id();
			//System.out.println("The customer sales id is "+cusId);
			System.out.println("The  sales id is "+entry.getSales_id());
			
			Set <Product>prodMaster =entry.getProducts();
			Customer customer = customerService.findOneWithAll(cusId);
			Long compStateId = customer.getCompany().getState().getState_id();
			Long custStateId= customer.getState().getState_id();
			Integer entryType=entry.getEntrytype();
			List<SalesEntryProductEntityClass> Sales_Product_Info=salesService.findAllSalesEntryProductEntityList(entry.getSales_id());
			//System.out.println("The product of sales "+Sales_Product_Info.size());
			entry.setProductinfoList(Sales_Product_Info);
			//System.out.println("custStateId" +custStateId);
			//System.out.println("compStateId"+compStateId);
			if(entryType==2){
				for(SalesEntryProductEntityClass ProdInfo:Sales_Product_Info){
					Double CGST=ProdInfo.getCGST();
					Double SGST=ProdInfo.getSGST();
					Long SEPProdId=ProdInfo.getProduct_id();
					System.out.println("The SEPProdId export"+SEPProdId);
						
					
					if(CGST>0 ||SGST>0 ){
						System.out.println("exception export");
						salesList.add(entry);
					}}
			} else{
			if (custStateId.equals(compStateId)){
				
				for(SalesEntryProductEntityClass ProdInfo:Sales_Product_Info){
					Double IGST=ProdInfo.getIGST();
					Long SEPProdId=ProdInfo.getProduct_id();
					System.out.println("The Igst is given for same state"+IGST);
					System.out.println("The SEPProdId"+SEPProdId);
					if(IGST>0){
						System.out.println("TIGST>0");
						salesList.add(entry);
					}else{
						System.out.println("TIGST>0 else in same state" );
						for(Product p1:prodMaster){
							Long pprodId=p1.getProduct_id();
							System.out.println("The produts product id is " + pprodId);
							if(SEPProdId.equals(pprodId) ){
							Double	Cgst=ProdInfo.getCGST();
							Double Sgst=ProdInfo.getSGST();
							System.out.println("Sales entry Igst amt" +Cgst );
							Double TranAmt=ProdInfo.getQuantity() * ProdInfo.getRate();
						System.out.println("Actual tran amount is" +TranAmt );
						GstTaxMaster gtm=	p1.getGst_id();
						Float ActCGST=gtm.getCgst();
						Float ActSGST=gtm.getSgst();
						ActCGST=(ActCGST/100);
						ActSGST=(ActSGST/100);
						Double CgstVal= (TranAmt*ActCGST);
						//CgstVal= (double) Math.round(CgstVal);
						
						CgstVal= Double.valueOf(df.format(CgstVal));
						Double SgstVal=(TranAmt*ActSGST);
						SgstVal=(double) Math.round(SgstVal);
						System.out.println(" Igst perc of master" +CgstVal );
						System.out.println("Actual Igst" +Cgst );
						if ((!CgstVal.equals(Cgst))||(!SgstVal.equals(Sgst))){salesList.add(entry);System.out.println("tax master gst"+gtm.getCgst()); }
						}
						}
					}
					
				}
				
			} else if(!custStateId.equals(compStateId)){
				for(SalesEntryProductEntityClass ProdInfo:Sales_Product_Info){
					
					Double CGST=ProdInfo.getCGST();
					Double SGST=ProdInfo.getSGST();
					Long SEPProdId=ProdInfo.getProduct_id();
					System.out.println("The SEPProdId else"+SEPProdId);
					if(CGST>0 ||SGST>0 ){
						System.out.println("exception");
						salesList.add(entry);
					}else{
						for(Product p1:prodMaster){
							Long pprodId=p1.getProduct_id();
							System.out.println("The produts product id is  else" + pprodId);
							if(SEPProdId.equals(pprodId) ){
							Double	Igst=ProdInfo.getIGST();
							
							System.out.println("Sales entry Igst amt" +Igst );
							Double TranAmt=ProdInfo.getQuantity() * ProdInfo.getRate();
						System.out.println("Actual tran amount is" +TranAmt );
						GstTaxMaster gtm=	p1.getGst_id();
						Float ActIGST=gtm.getIgst();
						
						ActIGST=(ActIGST/100);
						
						Double IgstVal= (TranAmt*ActIGST);
						//IgstVal= (double) Math.round(IgstVal);
						IgstVal= Double.valueOf(df.format(IgstVal));
						System.out.println(" Igst perc of master" +IgstVal );
						System.out.println("Actual Igst" +Igst );
						if ((!IgstVal.equals(Igst))){salesList.add(entry);System.out.println("tax master gst"+gtm.getCgst()); }
						}
						}
					}
					
				}
			}
			}
			 }
			
			//System.out.println("The sales cgst is "+x.getCgst());
			
			
		
		//salesEntryWithEditedGSTvalues
		/*for (SalesEntry entry : salesService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), form1.getCompanyId())) {
			entry.setProductinfoList(salesService.findAllSalesEntryProductEntityList(entry.getSales_id()));
			salesList.add(entry);
		}*/
		List<PurchaseEntry> purchaseEntryList = new ArrayList<>();

		for (PurchaseEntry entry : purchaseService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), form1.getCompanyId())) {
			entry.setProductinfoList(purchaseService.findAllPurchaseEntryProductEntityList(entry.getPurchase_id()));
			purchaseEntryList.add(entry);
		}
		List<Receipt> receciptList = new ArrayList<>();
		for (Receipt receipt : receiptService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), form1.getCompanyId())) {
			receipt.setProductinfoList(receiptService.findAllReceiptProductEntityList(receipt.getReceipt_id()));
			receciptList.add(receipt);
		}
		List<Payment> paymentList = paymentService.supplierHavingGSTApplicbleAsNOAndExceedingZERO(form1.getFromDate(),
				form1.getToDate(), form1.getCompanyId());

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (sales.getCreated_date() != null) {
					form.setDate(sales.getCreated_date());
				}
				if (sales.getCustomer() != null) {
					form.setCusSupplierName(sales.getCustomer().getFirm_name());

				}
				if (sales.getVoucher_no() != null) {
					form.setVoucherNumber(sales.getVoucher_no());
				}
				if (sales.getVoucher_no() != null) {
					form.setInvocieNumber(sales.getVoucher_no());
				}
				for (SalesEntryProductEntityClass info : sales.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if (sales.getCgst() != null) {
					form.setCgst(sales.getCgst());

				}
				form.setVoucherType("Sales");
				if (sales.getSgst() != null) {
					form.setSgst(sales.getSgst());

				}
				if (sales.getIgst() != null) {
					form.setIgst(sales.getIgst());

				}
				if (sales.getRound_off() != null) {
					form.setInvoiceValue(sales.getRound_off());
				}
				form.setLocaltime(sales.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (purchase.getSupplier_bill_date() != null) {
					form.setDate(purchase.getSupplier_bill_date());
				}
				if (purchase.getSupplier() != null) {
					form.setCusSupplierName(purchase.getSupplier().getCompany_name());

				}
				if (purchase.getVoucher_no() != null) {
					form.setVoucherNumber(purchase.getVoucher_no());
				}
				if (purchase.getSupplier_bill_no() != null) {
					form.setInvocieNumber(purchase.getSupplier_bill_no());
				}
				for (PurchaseEntryProductEntityClass info : purchase.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				if (purchase.getCgst() != null) {
					form.setCgst(purchase.getCgst());

				}
				form.setVoucherType("Purchase");
				if (purchase.getSgst() != null) {
					form.setSgst(purchase.getSgst());

				}
				if (purchase.getIgst() != null) {
					form.setIgst(purchase.getIgst());

				}
				if (purchase.getRound_off() != null) {
					form.setInvoiceValue(purchase.getRound_off());
				}
				form.setLocaltime(purchase.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (Receipt receipt : receciptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (receipt.getDate() != null) {
					form.setDate(receipt.getDate());
				}
				if (receipt.getCustomer() != null) {
					form.setCusSupplierName(receipt.getCustomer().getFirm_name());

				}
				if (receipt.getVoucher_no() != null) {
					form.setVoucherNumber(receipt.getVoucher_no());
				}
				if (receipt.getVoucher_no() != null) {
					form.setInvocieNumber(receipt.getVoucher_no());
				}
				for (Receipt_product_details info : receipt.getProductinfoList()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFreight();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Receipt");
				if (receipt.getCgst() != null) {
					form.setCgst(receipt.getCgst());

				}

				if (receipt.getSgst() != null) {
					form.setSgst(receipt.getSgst());

				}
				if (receipt.getIgst() != null) {
					form.setIgst(receipt.getIgst());

				}
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getTds_amount() != null) {
					Tds = Tds + receipt.getTds_amount();
				}
				if (receipt.getAmount() != null) {
					amount = amount + receipt.getAmount();
				}
				form.setInvoiceValue(Tds + amount);

				form.setLocaltime(receipt.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport5Form form = new ExceptionReport5Form();
				Double labourcharges = (double) 0;
				Double freight = (double) 0;
				Double others = (double) 0;

				if (payment.getDate() != null) {
					form.setDate(payment.getDate());
				}
				if (payment.getSupplier() != null) {
					form.setCusSupplierName(payment.getSupplier().getCompany_name());

				}
				if (payment.getVoucher_no() != null) {
					form.setVoucherNumber(payment.getVoucher_no());
				}
				if (payment.getSupplier_bill_no() != null) {
					form.setInvocieNumber(payment.getVoucher_no());
				}
				for (PaymentDetails info : payment.getPaymentDetails()) {
					labourcharges = labourcharges + info.getLabour_charges();
					freight = freight + info.getFrieght();
					others = others + info.getOthers();
				}
				form.setLabourcharges(labourcharges);
				form.setOthers(others);
				form.setFreight(freight);
				form.setVoucherType("Payment");
				if (payment.getCGST_head() != null) {
					form.setCgst(payment.getCGST_head());

				}
				if (payment.getSGST_head() != null) {
					form.setSgst(payment.getSGST_head());

				}
				if (payment.getIGST_head() != null) {
					form.setIgst(payment.getIGST_head());

				}
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getTds_amount() != null) {
					Tds = Tds + payment.getTds_amount();
				}
				if (payment.getAmount() != null) {
					amount = amount + payment.getAmount();
				}
				form.setInvoiceValue(Tds + amount);
				form.setLocaltime(payment.getLocal_time());
				exceptionReport5List.add(form);
			}
		}

		Collections.sort(exceptionReport5List, new Comparator<ExceptionReport5Form>() {
			public int compare(ExceptionReport5Form o1, ExceptionReport5Form o2) {

				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocaltime();
				LocalTime local_time2 = o2.getLocaltime();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("salesList", salesList);
		model.addObject("exceptionReport5List", exceptionReport5List);
		model.addObject("purchaseEntryList", purchaseEntryList);
		model.addObject("receciptList", receciptList);
		model.addObject("paymentList", paymentList);
		model.setViewName("/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZeroList");
		return model;
	}

	/**
	 * Supplier with sub-ledgers linked as Legal/ Professional/ Repairs &
	 * Maintenance and the TDS Applicable Flag is "N"; but the total of debit
	 * transactions or total of credit transactions exceed Rs 30000/-
	 */
	@RequestMapping(value = "exceptionReport6", method = RequestMethod.GET)
	public ModelAndView supplierandCustomerwithSubledgerTransactionexceedsRs300000(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierandCustomerwithSubledgerTransactionexceedsRs300000ForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/supplierandCustomerwithSubledgerTransactionexceedsRs300000ForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/supplierandCustomerwithSubledgerTransactionexceedsRs300000");
		}

		return model;
	}

	/**
	 * Supplier with sub-ledgers linked as Legal/ Professional/ Repairs &
	 * Maintenance and the TDS Applicable Flag is "N"; but the total of debit
	 * transactions or total of credit transactions exceed Rs 30000/-
	 */
	@RequestMapping(value = "exceptionReport6", method = RequestMethod.POST)
	public ModelAndView ShowsupplierandCustomerwithSubledgerTransactionexceedsRs300000(
			@ModelAttribute("form") DateForm form, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport6Form> exceptionReport6FormsList = new ArrayList<ExceptionReport6Form>();
		List<SalesEntry> salesList = salesService.customerSalesWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), user.getCompany().getCompany_id());
		List<PurchaseEntry> purchaseEntryList = purchaseService
				.supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(form.getFromDate(), form.getToDate(),
						user.getCompany().getCompany_id());
		List<Receipt> receiptList = receiptService.customerReceiptsWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), user.getCompany().getCompany_id());

		List<Payment> paymentList = paymentService.supplierPaymentWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), user.getCompany().getCompany_id());
		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}
				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer("Income");
					exception1Form.setSubledgerName(receipt.getSubLedger().getSubledger_name());
				}
				exception1Form.setVoucher_Type("Receipt");
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);

			}
		}
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (payment.getCreated_date() != null) {
					exception1Form.setDate(payment.getCreated_date());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}

				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers("Expense");
					exception1Form.setSubledgerName(payment.getSubLedger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Payment");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}

		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}

				if (sales.getSubledger() != null) {
					exception1Form.setSubledgerName(sales.getSubledger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Sales");

				Double amount = (double) 0;

				if (sales.getTransaction_value() != null) {
					amount = amount + sales.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}
		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}

				if (purchase.getSupplier() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}

				if (purchase.getSubledger() != null) {
					exception1Form.setSubledgerName(purchase.getSubledger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Purchase");

				Double amount = (double) 0;

				if (purchase.getTransaction_value() != null) {
					amount = amount + purchase.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}

		Collections.sort(exceptionReport6FormsList, new Comparator<ExceptionReport6Form>() {

			@Override
			public int compare(ExceptionReport6Form o1, ExceptionReport6Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("exceptionReport6FormsList", exceptionReport6FormsList);
		model.setViewName("/report/supplierandCustomerwithSubledgerTransactionexceedsRs300000List");
		return model;
	}

	@RequestMapping(value = "exceptionReport6ForKPO", method = RequestMethod.POST)
	public ModelAndView ShowsupplierandCustomerwithSubledgerTransactionexceedsRs300000ForKPO(
			@ModelAttribute("form") DateForm form, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		fromDate = form.getFromDate();
		toDate = form.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form.getCompanyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ExceptionReport6Form> exceptionReport6FormsList = new ArrayList<ExceptionReport6Form>();
		List<SalesEntry> salesList = salesService.customerSalesWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), form.getCompanyId());
		List<PurchaseEntry> purchaseEntryList = purchaseService
				.supplierPurchaseWithSubledgerTransactionsRs300000AndAbove(form.getFromDate(), form.getToDate(),
						form.getCompanyId());
		List<Receipt> receiptList = receiptService.customerReceiptsWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), form.getCompanyId());

		List<Payment> paymentList = paymentService.supplierPaymentWithSubledgerTransactionsRs300000AndAbove(
				form.getFromDate(), form.getToDate(), form.getCompanyId());
		for (Receipt receipt : receiptList) {
			if (receipt.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (receipt.getDate() != null) {
					exception1Form.setDate(receipt.getDate());
				}
				if (receipt.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(receipt.getVoucher_no());
				}

				if (receipt.getSubLedger() != null) {
					exception1Form.setCustomer("Income");
					exception1Form.setSubledgerName(receipt.getSubLedger().getSubledger_name());
				}
				exception1Form.setVoucher_Type("Receipt");
				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(true))) {
					if (receipt.getTds_amount() != null) {
						Tds = Tds + receipt.getTds_amount();
					}
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				if (receipt.getAdvance_payment() != null && receipt.getAdvance_payment().equals(new Boolean(false))) {
					if (receipt.getAmount() != null) {
						amount = amount + receipt.getAmount();
					}

				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(receipt.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);

			}
		}
		for (Payment payment : paymentList) {
			if (payment.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (payment.getCreated_date() != null) {
					exception1Form.setDate(payment.getCreated_date());
				}

				if (payment.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(payment.getVoucher_no());
				}
				if (payment.getSubLedger() != null) {
					exception1Form.setSuppliers("Expense");
					exception1Form.setSubledgerName(payment.getSubLedger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Payment");

				Double Tds = (double) 0;
				Double amount = (double) 0;
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(true))) {
					if (payment.getTds_amount() != null) {
						Tds = Tds + payment.getTds_amount();
					}
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				if (payment.getAdvance_payment() != null && payment.getAdvance_payment().equals(new Boolean(false))) {
					if (payment.getAmount() != null) {
						amount = amount + payment.getAmount();
					}

				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(payment.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}

		for (PurchaseEntry purchase : purchaseEntryList) {
			if (purchase.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (purchase.getCreated_date() != null) {
					exception1Form.setDate(purchase.getCreated_date());
				}

				if (purchase.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(purchase.getVoucher_no());
				}

				if (purchase.getSupplier() != null)

				{
					exception1Form.setCustomer(purchase.getSupplier().getCompany_name());
				}

				if (purchase.getSubledger() != null) {
					exception1Form.setSubledgerName(purchase.getSubledger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Purchase");

				Double amount = (double) 0;

				if (purchase.getTransaction_value() != null) {
					amount = amount + purchase.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(purchase.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}
		for (SalesEntry sales : salesList) {
			if (sales.getLocal_time() != null) {
				ExceptionReport6Form exception1Form = new ExceptionReport6Form();
				if (sales.getCreated_date() != null) {
					exception1Form.setDate(sales.getCreated_date());
				}
				if (sales.getVoucher_no() != null) {
					exception1Form.setVoucher_Number(sales.getVoucher_no());
				}
				if (sales.getCustomer() != null) {
					exception1Form.setCustomer(sales.getCustomer().getFirm_name());
				}

				if (sales.getSubledger() != null) {
					exception1Form.setSubledgerName(sales.getSubledger().getSubledger_name());
				}

				exception1Form.setVoucher_Type("Sales");

				Double amount = (double) 0;

				if (sales.getTransaction_value() != null) {
					amount = amount + sales.getTransaction_value();
				}
				exception1Form.setTransactionValue(amount);
				exception1Form.setTDS(0.0d);

				exception1Form.setLocal_time(sales.getLocal_time());
				exceptionReport6FormsList.add(exception1Form);
			}

		}
		Collections.sort(exceptionReport6FormsList, new Comparator<ExceptionReport6Form>() {

			@Override
			public int compare(ExceptionReport6Form o1, ExceptionReport6Form o2) {
				LocalDate Date1 = o1.getDate();
				LocalDate Date2 = o2.getDate();
				int sComp = Date2.compareTo(Date1);

				if (sComp != 0) {
					return sComp;
				}

				LocalTime local_time1 = o1.getLocal_time();
				LocalTime local_time2 = o2.getLocal_time();
				return local_time2.compareTo(local_time1);
			}
		});

		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.addObject("exceptionReport6FormsList", exceptionReport6FormsList);
		model.setViewName("/report/supplierandCustomerwithSubledgerTransactionexceedsRs300000List");
		return model;
	}

	/** cash in hand account should technically have debit or zero balance */
	@RequestMapping(value = "exceptionReport7", method = RequestMethod.GET)
	public ModelAndView cashInHandAcountShouldTechnicallyHaveDebitorZerobalance(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		DateForm form = new DateForm();
		HttpSession session = request.getSession(true);
		long role = (long) session.getAttribute("role");
		User user = (User) session.getAttribute("user");
		if (role == ROLE_SUPERUSER) {
			companyList = companyService.getAllCompaniesOnly();
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceForKPO");
		} else if (role == ROLE_EXECUTIVE || role == ROLE_MANAGER) {
			companyList = companyService.getAllCompaniesExe(user.getUser_id());
			model.addObject("form", form);
			model.addObject("companyList", companyList);
			model.setViewName("/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceForKPO");
		} else {
			model.addObject("form", form);
			model.setViewName("/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalance");
		}

		return model;
	}

	@RequestMapping(value = "exceptionReport7", method = RequestMethod.POST)
	public ModelAndView ShowcashInHandAcountShouldTechnicallyHaveDebitorZerobalance(
			@ModelAttribute("form") DateForm form1, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		fromDate = form1.getFromDate();
		toDate = form1.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(user.getCompany().getCompany_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<OpeningBalances> subledgerOPBalanceList = new ArrayList<OpeningBalances>();
		List<OpeningBalancesForm> subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
		SubLedger subledgercash = subLedgerDAO.findOne("Cash In Hand", user.getCompany().getCompany_id());

		for (Object bal[] : openingBalancesService.findAllOPbalancesforSubledger(form1.getFromDate(), form1.getToDate(),
				user.getCompany().getCompany_id(), false)) {
			if (bal[0] != null && bal[1] != null && bal[2] != null) {
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long) bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (sub != null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) {
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
					form.setSub_id((long) bal[0]);
					form.setCredit_balance((double) bal[2]);
					form.setDebit_balance((double) bal[1]);
					subledgerOpenBalanceList.add(form);
				}
			}
		}

		subledgerOPBalanceList = openingBalancesService.findAllOPbalancesforSubledger(form1.getFromDate(),
				form1.getToDate(), subledgercash.getSubledger_Id(), user.getCompany().getCompany_id());
		model.addObject("subledgerOPBalanceList", subledgerOPBalanceList);
		model.addObject("subledgerOpenBalanceList", subledgerOpenBalanceList);
		model.addObject("subledgerId", subledgercash.getSubledger_Id());
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.setViewName("/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceList");
		return model;
	}

	/** cash in hand account should technically have debit or zero balance */

	@RequestMapping(value = "exceptionReport7ForKPO", method = RequestMethod.POST)
	public ModelAndView ShowcashInHandAcountShouldTechnicallyHaveDebitorZerobalanceForKpo(
			@ModelAttribute("form") DateForm form1, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();

		fromDate = form1.getFromDate();
		toDate = form1.getToDate();
		try {
			company = companyService.getCompanyWithCompanyStautarType(form1.getCompanyId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<OpeningBalances> subledgerOPBalanceList = new ArrayList<OpeningBalances>();
		List<OpeningBalancesForm> subledgerOpenBalanceList = new ArrayList<OpeningBalancesForm>();
		SubLedger subledgercash = subLedgerDAO.findOne("Cash In Hand", form1.getCompanyId());

		for (Object bal[] : openingBalancesService.findAllOPbalancesforSubledger(form1.getFromDate(), form1.getToDate(),
				form1.getCompanyId(), false)) {
			if (bal[0] != null && bal[1] != null && bal[2] != null) {
				SubLedger sub = null;
				try {
					sub = subLedgerService.getById((long) bal[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (sub != null && sub.getSubledger_approval().equals(MyAbstractController.APPROVAL_STATUS_SECONDARY)) {
					OpeningBalancesForm form = new OpeningBalancesForm();
					form.setSubledgerName(sub.getSubledger_name());
					try {
						form.setSubledger(sub);
					} catch (Exception e) {
						e.printStackTrace();
					}
					form.setSub_id((long) bal[0]);
					form.setCredit_balance((double) bal[2]);
					form.setDebit_balance((double) bal[1]);
					subledgerOpenBalanceList.add(form);
				}
			}
		}

		subledgerOPBalanceList = openingBalancesService.findAllOPbalancesforSubledger(form1.getFromDate(),
				form1.getToDate(), subledgercash.getSubledger_Id(), form1.getCompanyId());
		model.addObject("subledgerOPBalanceList", subledgerOPBalanceList);
		model.addObject("subledgerOpenBalanceList", subledgerOpenBalanceList);
		model.addObject("subledgerId", subledgercash.getSubledger_Id());
		model.addObject("company", company);
		model.addObject("from_date", fromDate);
		model.addObject("to_date", toDate);
		model.setViewName("/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceList");
		return model;
	}

}