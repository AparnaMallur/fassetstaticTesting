package com.fasset.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpSession;

import org.jfree.data.time.Year;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IPayrollAutoJVServiceDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.dao.interfaces.IYearEndingDAO;
import com.fasset.entities.AccountingYear;
import com.fasset.entities.DepreciationAutoJV;
import com.fasset.entities.DepreciationSubledgerDetails;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.ManualJVDetails;
import com.fasset.entities.ManualJournalVoucher;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.entities.SubLedger;
import com.fasset.entities.User;
import com.fasset.entities.VoucherSeries;
import com.fasset.entities.YearEndJV;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.LedgerForm;
import com.fasset.form.OpeningBalancesForm;
import com.fasset.form.OpeningBalancesOfSubedgerForm;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.service.interfaces.ICommonService;
import com.fasset.service.interfaces.IPayrollAutoJVService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfCell;

@Service
@Transactional
public class PayrollAutoJVServiceImpl  extends MyAbstractController implements IPayrollAutoJVService {

	@Autowired
	private IPayrollAutoJVServiceDAO dao;

	@Autowired
	private IAccountingYearDAO accountYearDAO;

	@Autowired
	private ISubLedgerDAO subLedgerDAO;

	@Autowired
	ISubLedgerService subledgerService;
	@Autowired
	private ICommonService commonService;

	@Autowired
	private IYearEndingDAO yearendingdao ;
	
	@Override
	public void add(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PayrollAutoJV entity) throws MyWebException {
		dao.update(entity);
	}

	@Override
	public List<PayrollAutoJV> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayrollAutoJV getById(Long id) throws MyWebException {
		return dao.getById(id);
	}

	@Override
	public PayrollAutoJV getById(String id) throws MyWebException {
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
	public void remove(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(PayrollAutoJV entity) throws MyWebException {
		// TODO Auto-generated method stub

	}

	@Override
	public PayrollAutoJV isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayrollAutoJV savePayrollAutoJv(PayrollAutoJV autojv) {
		// TODO Auto-generated method stub
		try {
			autojv.setAccounting_year_id(accountYearDAO.findOne(autojv.getAccountYearId()));
			if (autojv.getEmployeeDetails() != "") {
				JSONArray jsonArray = new JSONArray(autojv.getEmployeeDetails());
				Set<PayrollEmployeeDetails> employeeDetailsList = new HashSet<PayrollEmployeeDetails>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					PayrollEmployeeDetails empDetails = new PayrollEmployeeDetails();
					empDetails.setCode(json.getString("empId"));
					empDetails.setName(json.getString("employeeName"));
					empDetails.setPan(json.getString("PAN"));
					empDetails.setTotaldays(Integer.parseInt(json.getString("totaldays")));
					empDetails.setBasicSalary((Double.parseDouble(json.getString("basicSalary"))));
					empDetails.setDA((Double.parseDouble(json.getString("DA"))));
					empDetails.setConveyanceAllowance((Double.parseDouble(json.getString("conveyanceAllowance"))));
					empDetails.setOtherAllowances((Double.parseDouble(json.getString("otherAllowances"))));
					empDetails.setGrossSalary((Double.parseDouble(json.getString("grossSalary"))));
					empDetails
							.setPfEmployeeContribution((Double.parseDouble(json.getString("pfEmployeeContribution"))));
					empDetails.seteSICEmployeeContribution(
							(Double.parseDouble(json.getString("eSICEmployeeContribution"))));
					empDetails.setProfessionTax((Double.parseDouble(json.getString("professionTax"))));
					empDetails.setlWF((Double.parseDouble(json.getString("lWF"))));
					empDetails.settDS((Double.parseDouble(json.getString("tDS"))));
					empDetails.setOtherDeductions((Double.parseDouble(json.getString("otherDeductions"))));
					empDetails.setAdvanceAdjustment((Double.parseDouble(json.getString("advanceAdjustment"))));
					empDetails.setTotalDeductions((Double.parseDouble(json.getString("totalDeductions"))));
					empDetails.setNetSalary((Double.parseDouble(json.getString("netSalary"))));
					empDetails
							.setPfEmployerContribution((Double.parseDouble(json.getString("pfEmployerContribution"))));
					empDetails.seteSICEmployerContribution(
							(Double.parseDouble(json.getString("eSICEmployerContribution"))));
					empDetails.setpFAdminCharges((Double.parseDouble(json.getString("pFAdminCharges"))));
					empDetails.setPayrollAutoJV(autojv);
					employeeDetailsList.add(empDetails);
				}
				//series.setVoucher_id(autojv.getVoucherSeries().getVoucher_id());
				
				//autojv.setVoucherSeries(accountYearDAO.findOneVoucherForAutoJV(autojv.getVoucherSeries().getVoucher_id()));
			//	 autojv.setVoucherSeries(dao.getVoucherS(autojv.getCompany().getCompany_id(), "AutoJV", autojv.getDate()));
				autojv.setPayrollEmployeeDetails(employeeDetailsList);
			}

			if (autojv.getSubledgerDetails() != "") {
				JSONArray jsonArray = new JSONArray(autojv.getSubledgerDetails());
				Set<PayrollSubledgerDetails> payrollSubledgerDetailsList = new HashSet<PayrollSubledgerDetails>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);

					if (json.getString("salaryandWages").equals("Salary and Wages")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("Salary and Wages");
						subDetails.setDrAmount((Double.parseDouble(json.getString("salaryandWagesDrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("ePFPayable").equals("PF Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("PF Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("ePFPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("eSICPayable").equals("ESIC Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("ESIC Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("eSICPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					
					if (json.getString("eSICEmployerContribution").equals("ESIC")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("ESIC");
						subDetails.setDrAmount((Double.parseDouble(json.getString("eSICDrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("pTPayable").equals("PT Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("PT Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("pTPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("lWFPayable").equals("LWF Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("LWF Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("lWFPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("tDS192BPayable").equals("TDS 192B Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("TDS 192B Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("tDS192BPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					//	"otherSalaryDeductions":otherSalaryDeductions, 		"salaryAdvance":salaryAdvance,
					if (json.getString("otherSalaryDeductions").equals("Other Salary Deductions")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("Other Salary Deductions");
						subDetails.setCrAmount((Double.parseDouble(json.getString("otherSalaryDeductionsCramount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("salaryAdvance").equals("Salary Advance")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("Salary Advance");
						subDetails.setCrAmount((Double.parseDouble(json.getString("salaryAdvanceCramount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					
					if (json.getString("salaryPayable").equals("Salary Payable")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("Salary Payable");
						subDetails.setCrAmount((Double.parseDouble(json.getString("salaryPayableCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("providentFund").equals("Provident Fund")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("Provident Fund");
						subDetails.setDrAmount((Double.parseDouble(json.getString("providentFundDrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}
					if (json.getString("administrativeExpense").equals("PF administrative charges")) {
						PayrollSubledgerDetails subDetails = new PayrollSubledgerDetails();
						subDetails.setSubledgerName("PF administrative charges");
						subDetails.setDrAmount((Double.parseDouble(json.getString("administrativeExpenseCrAmount"))));
						subDetails.setPayrollAutoJV(autojv);
						payrollSubledgerDetailsList.add(subDetails);
					}

				}
				autojv.setPayrollSubledgerDetails(payrollSubledgerDetailsList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return autojv;

	}

	@Override
	public List<PayrollAutoJV> getAllPayrollJVReletedToCompany(Long company_id) {

		return dao.getAllPayrollJVReletedToCompany(company_id);
	}

	@Override
	public String deleteByIdValue(Long payroll_id) {
		PayrollAutoJV payAutoJv=null;
		Long payrollEmpId=0l;
		String msg="";
		SubLedger salaryAndWages=null;
		SubLedger esic=null;
		SubLedger providentFund=null;
		SubLedger administrativeExpense=null;
		SubLedger tds192BPayable=null;
		SubLedger lwfPayable=null;
		SubLedger pfPayable=null;
		SubLedger esicPayable=null;
		SubLedger salaryPayable=null;
		SubLedger ptPayable=null;
		SubLedger otherSalaryDeductions=null;
		SubLedger salaryAdvance=null;
		
		try {
			payAutoJv = dao.findOne(payroll_id);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long company_id=payAutoJv.getCompany().getCompany_id();
		try {
			otherSalaryDeductions=subLedgerDAO.findOne("Other Salary Deductions", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			salaryAdvance=subLedgerDAO.findOne("Salary Advance", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			 salaryAndWages=subLedgerDAO.findOne("Salary and Wages", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			esic=subLedgerDAO.findOne("ESIC", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			providentFund=subLedgerDAO.findOne("Provident Fund", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			administrativeExpense=subLedgerDAO.findOne("PF administrative charges", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			tds192BPayable=subLedgerDAO.findOne("TDS 192B Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			lwfPayable=subLedgerDAO.findOne("LWF Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			pfPayable=subLedgerDAO.findOne("PF Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			esicPayable=subLedgerDAO.findOne("ESIC Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			salaryPayable=subLedgerDAO.findOne("Salary Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ptPayable=subLedgerDAO.findOne("PT Payable", company_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		Set<PayrollSubledgerDetails> payrollSubledgerDetails=new HashSet<PayrollSubledgerDetails>();;
		 payrollSubledgerDetails=payAutoJv.getPayrollSubledgerDetails();
		 
		 // Removed subLedger Details for that particular payroll id
			for(PayrollSubledgerDetails payrollautojvdetails:payrollSubledgerDetails)
			{
				
			
				try
				{
					if(payrollautojvdetails.getCrAmount()!=null && otherSalaryDeductions!=null )
					{
						subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
								otherSalaryDeductions.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
								(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
						
					
					}
					if(payrollautojvdetails.getCrAmount()!=null && salaryAdvance!=null )
					{
						subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
								salaryAdvance.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
								(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
						
					
					}
			if(payrollautojvdetails.getCrAmount()!=null && tds192BPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						tds192BPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
			
			if(payrollautojvdetails.getCrAmount()!=null && lwfPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						lwfPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
			
			if(payrollautojvdetails.getCrAmount()!=null && pfPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						pfPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
			
			if(payrollautojvdetails.getCrAmount()!=null && esicPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						esicPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
			
			
			
			if(payrollautojvdetails.getCrAmount()!=null && salaryPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						salaryPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
			
			if(payrollautojvdetails.getCrAmount()!=null && ptPayable!=null )
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						ptPayable.getSubledger_Id(), (long) 2, (double)payrollautojvdetails.getCrAmount(), (double) 0,
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
			
			}
				
			 if(payrollautojvdetails.getDrAmount()!=null  && salaryAndWages!=null)
			{
				subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
						salaryAndWages.getSubledger_Id(), (long) 2, (double) 0, (double)payrollautojvdetails.getDrAmount(),
						(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
				
				
			}
			 
			 if(payrollautojvdetails.getDrAmount()!=null  && esic!=null)
				{
					subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
							esic.getSubledger_Id(), (long) 2, (double) 0, (double)payrollautojvdetails.getDrAmount(),
							(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
					
					
				}
			 
			 if(payrollautojvdetails.getDrAmount()!=null  && providentFund!=null)
				{
					subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
							providentFund.getSubledger_Id(), (long) 2, (double) 0, (double)payrollautojvdetails.getDrAmount(),
							(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
					
					
				}
			 
			 if(payrollautojvdetails.getDrAmount()!=null  && administrativeExpense!=null)
				{
					subledgerService.addsubledgertransactionbalance(payAutoJv.getAccounting_year_id().getYear_id(),payAutoJv.getDate(),payAutoJv.getCompany().getCompany_id(),
							administrativeExpense.getSubledger_Id(), (long) 2, (double) 0, (double)payrollautojvdetails.getDrAmount(),
							(long) 0,null,null,null,null,null,null,null,null,payAutoJv,null,null,null);
					
					
				}
			 
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			
			try {
				dao.deleteByPayrollSubledgerDetails(payrollautojvdetails.getSubledgerdetails_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}

			}
			
			
			
			
		
			// Removed Payroll Employee Details for that particular payroll id
			
			Set<PayrollEmployeeDetails> payrollEmpDetails = new HashSet<PayrollEmployeeDetails>();
			
			payrollEmpDetails=payAutoJv.getPayrollEmployeeDetails();
			
			 // Removed subLedger Details for that particular payroll id
		for (PayrollEmployeeDetails payrollEmpdetails : payrollEmpDetails) {
			if (payrollEmpdetails != null) {
				payrollEmpId = payrollEmpdetails.getEmployeeDetails_id();
				dao.deletePayrollEmployeeId(payrollEmpId);

			}
		}
		msg=	dao.deletePayroll(payroll_id);
	
		return msg;
			
	}
	
	@Override
	public List<PayrollAutoJV> findAllListing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long saveGstAutoJv(GSTAutoJV autojv) {
		// TODO Auto-generated method stub
		return dao.saveGstAutoJv(autojv);
	}

	@Override
	public GSTAutoJV findGstAutojv(Long gstId) {
		// TODO Auto-generated method stub
		return dao.findGstAutojv(gstId);
	}

	@Override
	public PayrollAutoJV findOneView(long payroll_id) {
		return  dao.findOneView(payroll_id);
	
	}

	@Override
	public List<PayrollEmployeeDetails> findAllPayrollEmployeeList(Long entryId) {
	
		return dao.findAllPayrollEmployeeList(entryId);
	}

	@Override
	public List<GSTAutoJV> getAllGstAutoJVReletedToCompany(Long company_id) {
		// TODO Auto-generated method stub
		return dao.getAllGstAutoJVReletedToCompany(company_id);
	}

	@Override
	public String deleteGSTAutoJVByIdValue(Long gstauto_id,Long company_id) {
		
		GSTAutoJV entity = getGstAutoJvById(gstauto_id);
		
		SubLedger outputcgst = subLedgerDAO.findOne("Output CGST", company_id);

		SubLedger outputsgst = subLedgerDAO.findOne("Output SGST", company_id);

		SubLedger outputigst = subLedgerDAO.findOne("Output IGST", company_id);

		SubLedger inputcgst = subLedgerDAO.findOne("Input CGST", company_id);

		SubLedger inputsgst = subLedgerDAO.findOne("Input SGST", company_id);

		SubLedger inputigst = subLedgerDAO.findOne("Input IGST", company_id);
		
	//	SubLedger gstpayble = subLedgerDAO.findOne("GST Payable", company_id);
		
		if (outputcgst != null && entity.getTotalOutputCgstCrbalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					outputcgst.getSubledger_Id(), (long) 2, (double) 0,(double) entity.getTotalOutputCgstCrbalance(), (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}

		if (inputcgst != null && entity.getTotalInputCgstDrBalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					inputcgst.getSubledger_Id(), (long) 2, (double) entity.getTotalInputCgstDrBalance(),(double) 0, (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}
		
		if (outputsgst != null && entity.getTotalOutputSgstCrBalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					outputsgst.getSubledger_Id(), (long) 2, (double) 0,(double) entity.getTotalOutputSgstCrBalance(), (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}
		
		if (inputsgst != null && entity.getTotalInputSgstDrBalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					inputsgst.getSubledger_Id(), (long) 2, (double) entity.getTotalInputSgstDrBalance(),(double) 0, (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}
		

		if (outputigst != null && entity.getTotalOutputIgstCrBalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					outputigst.getSubledger_Id(), (long) 2, (double) 0,(double) entity.getTotalOutputIgstCrBalance(), (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}
		
		if (inputigst != null && entity.getTotalInputIgstDrBalance()>0) {
			subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().getYear_id(),entity.getCreated_date(),company_id,
					inputigst.getSubledger_Id(), (long) 2, (double) entity.getTotalInputIgstDrBalance(),(double) 0, (long) 0,null,null,null,null,null,null,null,null,null,entity,null,null);
		}
		
		/*
		 * if (gstpayble != null && entity.getTotalgstPaybleDrBalance() !=null &&
		 * entity.getTotalgstPaybleDrBalance()>0) {
		 * subledgerService.addsubledgertransactionbalance(entity.getAccountingYear().
		 * getYear_id(),entity.getCreated_date(),company_id,
		 * gstpayble.getSubledger_Id(), (long) 2,(double) 0, (double)
		 * entity.getTotalgstPaybleDrBalance(), (long)
		 * 0,null,null,null,null,null,null,null,null,null,entity,null,null); }
		 */
		
		// TODO Auto-generated method stub
		return dao.deleteGSTAutoJVByIdValue(gstauto_id);
	}

	@Override
	public GSTAutoJV getGstAutoJvById(Long id) {
		// TODO Auto-generated method stub
		return dao.getGstAutoJvById(id);
	}

	@Override
	public PayrollAutoJV findOneWithAll(Long payroll_id) {
		// TODO Auto-generated method stub
		return dao.findOneWithAll(payroll_id);
	}

	@Override
	public PayrollEmployeeDetails editEmployeeForPayroll(Long entryId) {
		
		return dao.editEmployeeForPayroll(entryId);
	}

	@Override
	public void updateSalesEntry(PayrollAutoJV entry) {
		dao.updateSalesEntry(entry);
	}

	@Override
	public Long saveYearEndAutoJV(ProfitAndLossReportForm form) {
	YearEndJV jv = new YearEndJV();
	
	String currentyear = new Year().toString();//CURRENT YEAR LIKE 2018	
	Integer year = Integer.parseInt(currentyear);
	year=year-1;
	String lastYear =year.toString();
	currentyear=lastYear+"-"+currentyear;
	System.out.println("The company id yearendautojv "+form.getCompany().getCompany_name());
	System.out.println("The year range is "+form.getCompany().getYearRange());
	String[] years=form.getCompany().getYearRange().split(",");
	List<OpeningBalancesOfSubedgerForm>  abc=form.getListForOpeningbalancesOfsubledger();
	
	List<Long> yearIds = new ArrayList<Long>();
	for (String yId : years) {
		System.out.println("The year id is" +yId);
		yearIds.add(new Long(yId));
	}
	AccountingYear accYear = new AccountingYear();
	AccountingYear accYearTobeClosed = new AccountingYear();
	for(Long id : yearIds)
	{
		try {
			accYear = accountYearDAO.findOne(id);
		} catch (MyWebException e) {
			e.printStackTrace();
		}
		if(currentyear.equalsIgnoreCase(accYear.getYear_range()))
		{
			jv.setAccountingYear(accYear);
			accYearTobeClosed=accYear;
			break;
		}
	}
	
	double totalDebitSideAmount = (double) 0;
	double totalCreditSideAmount = (double) 0;
	Set<YearEndJvSubledgerDetails> yearEndJvSubledgerDetailsList=new HashSet<YearEndJvSubledgerDetails>();
	jv.setCompany(form.getCompany());
	jv.setDate(form.getToDate());
	jv.setVoucher_no(commonService.getVoucherNumber("YEARENDJV", YEAR_AUTOJV, jv.getDate(),jv.getAccountYearId(), form.getCompany().getCompany_id()));
	
//	jv.setVoucher_no("YEARENDJV123");
	List<OpeningBalancesOfSubedgerForm> subOpeningList=form.getListForOpeningbalancesOfsubledger();
    List<OpeningBalancesOfSubedgerForm> subOpeningListBeforeStartDate =form.getListForOpeningbalancesbeforestartDate();
	System.out.println("The suopening list is "+subOpeningListBeforeStartDate.size());
    for(OpeningBalancesOfSubedgerForm obalance : subOpeningList)
	{
		if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
		{
			for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
			{
				if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
				{
					totalDebitSideAmount=totalDebitSideAmount+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
				}
			}
		}
		if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
		{
			for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
			{
				if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
				{
					totalCreditSideAmount=totalCreditSideAmount+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
				}
			}
		}
	}
    
    if (subOpeningList.size() > 0) 
    {

		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
			
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)1))
			{
				double closingBalance = (double) 0;
			
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						closingBalance=closingBalance+((obalanceBeStartDate.getTotaldebit_balance()-obalanceBeStartDate.getTotalcredit_balance())+(obalance.getTotaldebit_balance()-obalance.getTotalcredit_balance()));
					}
				}
				System.out.println("The clsing bal is posting id 1 "+closingBalance);
				if(closingBalance!=0)
				{
					
					if(obalance.getAccountSubGroupNameList().size()>0)
					{
						for(String accSubGroup : obalance.getAccountSubGroupNameList())
						{
							double closingBalanceOfSubgroup = (double) 0;
							double total_credit_Subgroup = (double) 0;
							double total_debit_Subgroup = (double) 0;
							double total_credit_Subgroup_beforeStartDate = (double) 0;
							double total_debit_Subgroup_beforeStartDate = (double) 0;
							
							for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
							{
								if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
								{
									for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
									{
										if(subGroupName.equals(accSubGroup))
										{
											if(obalanceBeStartDate.getLedgerformlist().size()>0)
											{
												for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
												{
													if(ledgerform1.getSubgroupName().equals(accSubGroup))
													{
														total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
														total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
													}
												}
											}
										}
									}
								}
							}
							if(obalance.getLedgerformlist().size()>0)
							{
								for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
								{
									if(ledgerform2.getSubgroupName().equals(accSubGroup))
									{
										total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
										total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
									}
								}
							}
							
							closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_debit_Subgroup_beforeStartDate-total_credit_Subgroup_beforeStartDate)+(total_debit_Subgroup-total_credit_Subgroup);
							if(closingBalanceOfSubgroup!=0)
							{
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
									{
										if(ledgerform3.getSubgroupName().equals(accSubGroup))
										{
											double openiBalanceOfLedger = (double) 0;
											double total_debitOfLedger = (double) 0;
											double total_creditOfLedger = (double) 0;
											for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
											{
												if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
												{
													for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
													{
														if(subGroupName.equals(accSubGroup))
														{
															if(obalanceBeStartDate.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																{
																	if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																	{
																		if(ledgerform4.getSubgroupName().equals(accSubGroup))
																		{
																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgerdebit_balance()-ledgerform4.getLedgercredit_balance());
																		}
																	}
																}
															}
														}
													}
												}
											}
											
											total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
											total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
											
										}
										
										if(ledgerform3.getSubledgerList().size()>0)
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
												{
													double openiBalanceOfSubLedger = (double) 0;
													double total_debitOfSubLedger = (double) 0;
													double total_creditOfSubLedger = (double) 0;
													for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
													{
														if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
														{
															for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
															{
																if(subGroupName.equals(accSubGroup))
																{
																	for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform5.getSubgroupName().equals(accSubGroup))
																		{
																			for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																			{
																				if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																				{
																					openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																				}
																			}
																		}
																	}
																}
															}
														}
													}
													
													total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
													total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
													
													if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
													{
														System.out.println("The crside is 1"+subledger.getSubledger().getSubledger_name());
														YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
														obj.setYearEndJV(jv);
														obj.setSubLedgerAmount((openiBalanceOfSubLedger+total_debitOfSubLedger-total_creditOfSubLedger));
														obj.setCrSide(1);
														obj.setSubledger(subledger.getSubledger());
														yearEndJvSubledgerDetailsList.add(obj);
													}
												}
											}
										}
									}
								}
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												
												for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
												{
													int isNotPresent = 0;
													int isPresent = 0;
													
													if(ledgerform6.getSubgroupName().equals(accSubGroup))
													{
														if(obalance.getLedgerformlist().size()>0)
														{
															for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
															{
																if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																{
																	isPresent=1;
																}
															}
															
															if(isPresent==0) 
															{
																if(obalance.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																	{
																		if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																		{
																			if(isNotPresent==0)
																			{
																				double openiBalanceOfLedger = (double) 0;
																				openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
																				
																				if(ledgerform6.getSubledgerList().size()>0)
																				{
																					for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																					{
																						double openiBalanceOfSubLedger = (double) 0;
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																						if(openiBalanceOfSubLedger!=0)
																						{
																							System.out.println("The crside is 2"+subledger1.getSubledger().getSubledger_name());
																							YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
																							obj.setYearEndJV(jv);
																							obj.setSubLedgerAmount((openiBalanceOfSubLedger));
																							obj.setCrSide(1);
																							obj.setSubledger(subledger1.getSubledger());
																							yearEndJvSubledgerDetailsList.add(obj);
																							
																						}
																					}
																				}
																				isNotPresent=1;
																			}
																		}
																	}
																}
															}
														}
														if(obalance.getLedgerformlist().size()==0)
														{
															double openiBalanceOfLedger = (double) 0;
															openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgerdebit_balance()-ledgerform6.getLedgercredit_balance());
															if(ledgerform6.getSubledgerList().size()>0)
															{
																for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																{
																	double openiBalanceOfSubLedger = (double) 0;
																	openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getDebit_balance()-subledger1.getCredit_balance();
																	if(openiBalanceOfSubLedger!=0)
																	{
																		System.out.println("The crside is 3"+subledger1.getSubledger().getSubledger_name());
																		YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
																		obj.setYearEndJV(jv);
																		obj.setSubLedgerAmount((openiBalanceOfSubLedger));
																		obj.setCrSide(1);
																		obj.setSubledger(subledger1.getSubledger());
																		yearEndJvSubledgerDetailsList.add(obj);
																	
																	}
																}
															}
														}
														
														
													}
												}
											}
										}
									}
								}
								
							}
						}
					}
				}
			
			}
		}
		double totalNetProfit = (double) 0;
		System.out.println("Total creditside amot "+totalCreditSideAmount);
		System.out.println("Total debitside amot "+totalDebitSideAmount);
		if(totalCreditSideAmount>totalDebitSideAmount)
		{
			totalNetProfit=totalNetProfit+(totalCreditSideAmount-totalDebitSideAmount);
			
		}
	
		for (OpeningBalancesOfSubedgerForm obalance : subOpeningList) {
			
			if(obalance.getGroup().getPostingSide().getPosting_id().equals((long)2))
			{
				double closingBalance = (double) 0;
			
				for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
				{
					if(obalanceBeStartDate.getAccountGroupName().equals(obalance.getAccountGroupName()))
					{
						closingBalance=closingBalance+((obalanceBeStartDate.getTotalcredit_balance()-obalanceBeStartDate.getTotaldebit_balance())+(obalance.getTotalcredit_balance()-obalance.getTotaldebit_balance()));
					}
				}
				System.out.println("The clsing bal is posting id 2 "+closingBalance);
				if(closingBalance!=0)
				{
				
					if(obalance.getAccountSubGroupNameList().size()>0)
					{
						for(String accSubGroup : obalance.getAccountSubGroupNameList())
						{
							double closingBalanceOfSubgroup = (double) 0;
							double total_credit_Subgroup = (double) 0;
							double total_debit_Subgroup = (double) 0;
							double total_credit_Subgroup_beforeStartDate = (double) 0;
							double total_debit_Subgroup_beforeStartDate = (double) 0;
							
							for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
							{
								if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
								{
									for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
									{
										if(subGroupName.equals(accSubGroup))
										{
											if(obalanceBeStartDate.getLedgerformlist().size()>0)
											{
												for(LedgerForm ledgerform1 : obalanceBeStartDate.getLedgerformlist())
												{
													if(ledgerform1.getSubgroupName().equals(accSubGroup))
													{
														total_credit_Subgroup_beforeStartDate=total_credit_Subgroup_beforeStartDate+ledgerform1.getLedgercredit_balance();
														total_debit_Subgroup_beforeStartDate=total_debit_Subgroup_beforeStartDate+ledgerform1.getLedgerdebit_balance();
													}
												}
											}
										}
									}
								}
							}
							if(obalance.getLedgerformlist().size()>0)
							{
								for(LedgerForm ledgerform2 : obalance.getLedgerformlist())
								{
									if(ledgerform2.getSubgroupName().equals(accSubGroup))
									{
										total_credit_Subgroup=total_credit_Subgroup+ledgerform2.getLedgercredit_balance();
										total_debit_Subgroup=total_debit_Subgroup+ledgerform2.getLedgerdebit_balance();
									}
								}
							}
							
							closingBalanceOfSubgroup=closingBalanceOfSubgroup+(total_credit_Subgroup_beforeStartDate-total_debit_Subgroup_beforeStartDate)+(total_credit_Subgroup-total_debit_Subgroup);
							if(closingBalanceOfSubgroup!=0)
							{
								if(obalance.getLedgerformlist().size()>0)
								{
									for(LedgerForm ledgerform3 : obalance.getLedgerformlist())
									{
										if(ledgerform3.getSubgroupName().equals(accSubGroup))
										{
											double openiBalanceOfLedger = (double) 0;
											double total_debitOfLedger = (double) 0;
											double total_creditOfLedger = (double) 0;
											for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
											{
												if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
												{
													for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
													{
														if(subGroupName.equals(accSubGroup))
														{
															if(obalanceBeStartDate.getLedgerformlist().size()>0)
															{
																for(LedgerForm ledgerform4 : obalanceBeStartDate.getLedgerformlist())
																{
																	if(ledgerform4.getLedgerName().equals(ledgerform3.getLedgerName()))
																	{
																		if(ledgerform4.getSubgroupName().equals(accSubGroup))
																		{
																			openiBalanceOfLedger=openiBalanceOfLedger+(ledgerform4.getLedgercredit_balance()-ledgerform4.getLedgerdebit_balance());
																		}
																	}
																}
															}
														}
													}
												}
											}
											
											total_debitOfLedger=total_debitOfLedger+ledgerform3.getLedgerdebit_balance();
											total_creditOfLedger=total_creditOfLedger+ledgerform3.getLedgercredit_balance();
										}
										
										if(ledgerform3.getSubledgerList().size()>0)
										{
											if(ledgerform3.getSubgroupName().equals(accSubGroup))
											{
												for(OpeningBalancesForm subledger : ledgerform3.getSubledgerList())
												{
													double openiBalanceOfSubLedger = (double) 0;
													double total_debitOfSubLedger = (double) 0;
													double total_creditOfSubLedger = (double) 0;
													for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
													{
														if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
														{
															for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
															{
																if(subGroupName.equals(accSubGroup))
																{
																	for(LedgerForm ledgerform5 : obalanceBeStartDate.getLedgerformlist())
																	{
																		if(ledgerform5.getSubgroupName().equals(accSubGroup))
																		{
																			for(OpeningBalancesForm subledger1 : ledgerform5.getSubledgerList())
																			{
																				if(subledger1.getSubledgerName().equals(subledger.getSubledgerName()))
																				{
																					openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																				}
																			}
																		}
																	}
																}
															}
														}
													}
													
													total_debitOfSubLedger=total_debitOfSubLedger+subledger.getDebit_balance();
													total_creditOfSubLedger=total_creditOfSubLedger+subledger.getCredit_balance();
													
													if(openiBalanceOfSubLedger!=0 || total_debitOfSubLedger!=0 || total_creditOfSubLedger!=0)
													{
														YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
														obj.setYearEndJV(jv);
														obj.setSubLedgerAmount((openiBalanceOfSubLedger+total_creditOfSubLedger-total_debitOfSubLedger));
														obj.setDrSide(1);
														obj.setSubledger(subledger.getSubledger());
														yearEndJvSubledgerDetailsList.add(obj);
													}
												}
											}
										}
									}
								}
								
								for(OpeningBalancesOfSubedgerForm obalanceBeStartDate : subOpeningListBeforeStartDate)
								{
									if(obalanceBeStartDate.getAccountSubGroupNameList().size()>0)
									{
										for(String subGroupName : obalanceBeStartDate.getAccountSubGroupNameList())
										{
											if(subGroupName.equals(accSubGroup))
											{
												
												for(LedgerForm ledgerform6 : obalanceBeStartDate.getLedgerformlist())
												{
													int isNotPresent = 0;
													int isPresent = 0;
													
													if(ledgerform6.getSubgroupName().equals(accSubGroup))
													{
														if(obalance.getLedgerformlist().size()>0)
														{
															for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
															{
																if(ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																{
																	isPresent=1;
																}
															}
															
															if(isPresent==0) 
															{
																if(obalance.getLedgerformlist().size()>0)
																{
																	for(LedgerForm ledgerform7 : obalance.getLedgerformlist())
																	{
																		if(!ledgerform7.getLedgerName().equals(ledgerform6.getLedgerName()))
																		{
																			if(isNotPresent==0)
																			{
																				double openiBalanceOfLedger = (double) 0;
																				openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
																				
																				if(ledgerform6.getSubledgerList().size()>0)
																				{
																					for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																					{
																						double openiBalanceOfSubLedger = (double) 0;
																						openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																						if(openiBalanceOfSubLedger!=0)
																						{
																							YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
																							obj.setYearEndJV(jv);
																							obj.setSubLedgerAmount((openiBalanceOfSubLedger));
																							obj.setDrSide(1);
																							obj.setSubledger(subledger1.getSubledger());
																							yearEndJvSubledgerDetailsList.add(obj);
																						}
																					}
																				}
																				isNotPresent=1;
																			}
																		}
																	}
																}
															}
														}
														if(obalance.getLedgerformlist().size()==0)
														{
															double openiBalanceOfLedger = (double) 0;
															openiBalanceOfLedger =openiBalanceOfLedger+(ledgerform6.getLedgercredit_balance()-ledgerform6.getLedgerdebit_balance());
															if(ledgerform6.getSubledgerList().size()>0)
															{
																for(OpeningBalancesForm subledger1 :ledgerform6.getSubledgerList())
																{
																	double openiBalanceOfSubLedger = (double) 0;
																	openiBalanceOfSubLedger=openiBalanceOfSubLedger+subledger1.getCredit_balance()-subledger1.getDebit_balance();
																	if(openiBalanceOfSubLedger!=0)
																	{
																		YearEndJvSubledgerDetails obj = new YearEndJvSubledgerDetails();
																		obj.setYearEndJV(jv);
																		obj.setSubLedgerAmount((openiBalanceOfSubLedger));
																		obj.setDrSide(1);
																		obj.setSubledger(subledger1.getSubledger());
																		yearEndJvSubledgerDetailsList.add(obj);
																	}
																}
															}
														}
														
														
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			
			}
		}
		double totalNetLoss = (double) 0;
		System.out.println("Total creditside loss "+totalCreditSideAmount);
		System.out.println("Total debitside loss "+totalDebitSideAmount);
		if(totalDebitSideAmount>totalCreditSideAmount)
		{
			totalNetLoss=totalNetLoss+(totalDebitSideAmount-totalCreditSideAmount);
		}
		jv.setYearEndJvSubledgerDetails(yearEndJvSubledgerDetailsList);
		jv.setNetProfit(totalNetProfit);
	    jv.setNetLoss(totalNetLoss);
	    Set<YearEndJvSubledgerDetails> abcd=jv.getYearEndJvSubledgerDetails();
		for(YearEndJvSubledgerDetails dd:abcd){
			
		}
		Long id = dao.saveYearEndAutoJv(jv);
		YearEndJV entry = dao.getYearEndJVById(id);
		
		if(entry.getYearEndJvSubledgerDetails()!=null && entry.getYearEndJvSubledgerDetails().size()>0)
		{
			
			for(YearEndJvSubledgerDetails details: entry.getYearEndJvSubledgerDetails())
			{
				
				
				if(details.getCrSide()!=null && details.getCrSide()!=-1)
				{
					System.out.println("getCrSide()  "+details.getCrSide());
					System.out.println("get subledger name "+details.getSubledger().getSubledger_name());
					System.out.println("The YearEndJvSubledgerDetails Id is "+details.getYear_endJvSubledgerDetailsId());
					System.out.println("The(entry.getAccountingYear().getYear_id() is "+entry.getAccountingYear().getYear_id());
					System.out.println("Theentry.getDate() is "+entry.getDate());
					System.out.println("entry.getCompany().getCompany_id() is "+entry.getCompany().getCompany_id());
					System.out.println("entry.getCompany().getCompany_id() is "+entry.getCompany().getCompany_id());
					subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
							entry.getCompany().getCompany_id(), details.getSubledger().getSubledger_Id(), (long) 2,
								(double) details.getSubLedgerAmount(), (double) 0,(long) 1, null, null, null, null, null, null,
							null, null, null, null, null,entry);
				}
				else
				{
					System.out.println("getDrSide()  ");
					subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
							entry.getCompany().getCompany_id(),details.getSubledger().getSubledger_Id(), (long) 2,(double) 0,
								(double)  details.getSubLedgerAmount(), (long) 1, null, null, null, null, null, null,
							null, null, null, null, null,entry);
				}
			}
			
			SubLedger profitAndLoss = subLedgerDAO.findOne("Profit & Loss A/C", entry.getCompany().getCompany_id());
			
			if(profitAndLoss!=null)
			{
			if(totalCreditSideAmount>totalDebitSideAmount)
			{
			System.out.println("creditside");
				subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
						entry.getCompany().getCompany_id(), profitAndLoss.getSubledger_Id(), (long) 2,
							(double) totalNetProfit, (double) 0,(long) 1, null, null, null, null, null, null,
						null, null, null, null, null,entry);
			}
			else
			{
				System.out.println("debittside");
				subledgerService.addsubledgertransactionbalance(entry.getAccountingYear().getYear_id(), entry.getDate(),
						entry.getCompany().getCompany_id(),profitAndLoss.getSubledger_Id(), (long) 2,(double) 0,
							(double)  totalNetLoss, (long) 1, null, null, null, null, null, null,
						null, null, null, null, null,entry);
			}
			}
			
		}
		
		// Locking year
		List<YearEnding> yearList = yearendingdao.findAllYearEnding(form.getCompany().getCompany_id());
		for(YearEnding yearend : yearList)
		{
			if(yearend.getAccountingYear()!=null && yearend.getAccountingYear().getYear_id().equals(accYearTobeClosed.getYear_id()))
			{
				yearend.setYearEndingstatus((long)0);
				try {
					yearendingdao.update(yearend);
				} catch (MyWebException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
    
	return  (long) 2;
	}

	
	@Override
	public CopyOnWriteArrayList<YearEndJV> findAllYearEndVoucherReletedToCompany() {
		// TODO Auto-generated method stub
		return dao.findAllYearEndVoucherReletedToCompany();
	}

	@Override
	public YearEndJV getYearEndJVById(Long id) {
		
			return dao.getYearEndJVById(id);

	}

	@Override
	public List<YearEndJvSubledgerDetails> getAllYearEndJvSubledgerDetails(Long entityId) {
		
		return dao.getAllYearEndJvSubledgerDetails(entityId);
	}
	
	@Override
	public String deleteByYearEndJVIdValue(Long entityId) {
		// TODO Auto-generated method stub
		try {
			
			YearEndJV yearendjv =dao.getYearEndJVById(entityId);
			Set<YearEndJvSubledgerDetails> yearjvdetails = new HashSet<YearEndJvSubledgerDetails>();
			yearjvdetails=yearendjv.getYearEndJvSubledgerDetails();
SubLedger profitAndLoss = subLedgerDAO.findOne("Profit & Loss A/C", yearendjv.getCompany().getCompany_id());
			
			if(profitAndLoss!=null)
			{
				if(yearendjv.getNetProfit()!=null && yearendjv.getNetProfit() >0)
				{
					subledgerService.addsubledgertransactionbalance(yearendjv.getAccountingYear().getYear_id(), yearendjv.getDate(),
							yearendjv.getCompany().getCompany_id(),profitAndLoss.getSubledger_Id(), (long) 2,(double) 0,
								(double)  yearendjv.getNetProfit(), (long) 0, null, null, null, null, null, null,
							null, null, null, null, null,yearendjv);
				
			}
			else
			{
				subledgerService.addsubledgertransactionbalance(yearendjv.getAccountingYear().getYear_id(), yearendjv.getDate(),
						yearendjv.getCompany().getCompany_id(), profitAndLoss.getSubledger_Id(), (long) 2,
							(double) yearendjv.getNetLoss(), (double) 0,(long) 0, null, null, null, null, null, null,
						null, null, null, null, null,yearendjv);
			}
			}
			
		

			for(YearEndJvSubledgerDetails yearjvdetail:yearjvdetails)
			{
				try {

					if (yearjvdetail.getSubLedgerAmount() != null && yearendjv != null) {
						subledgerService.addsubledgertransactionbalance(yearendjv.getAccountingYear().getYear_id(),
								yearendjv.getDate(), yearendjv.getCompany().getCompany_id(),
								yearjvdetail.getSubledger().getSubledger_Id(), (long) 2, (double) 0,
								(double) yearjvdetail.getSubLedgerAmount(), (long) 0, null, null, null, null, null, null,
								null, null, null, null, null,yearendjv);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			 
			// Locking year
			List<YearEnding> yearList = yearendingdao.findAllYearEnding(yearendjv.getCompany().getCompany_id());
			for(YearEnding yearend : yearList)
			{
				if(yearend.getAccountingYear()!=null && yearend.getAccountingYear().getYear_id().equals(yearendjv.getAccountingYear().getYear_id()))
				{
					yearend.setYearEndingstatus((long)1);
					try {
						yearendingdao.update(yearend);
					} catch (MyWebException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
				
		return dao.deleteByYearEndIdValue(entityId);

}
}