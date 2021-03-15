package com.fasset.service.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.YearEndJV;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.ProfitAndLossReportForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IPayrollAutoJVService extends IGenericService<PayrollAutoJV>{

	public PayrollAutoJV savePayrollAutoJv(PayrollAutoJV autojv);
	
	public List<PayrollAutoJV> getAllPayrollJVReletedToCompany(Long company_id);
	
	public String deleteByIdValue(Long payroll_id);
	
	public PayrollAutoJV getById(Long payroll_id) throws MyWebException;
	List<PayrollAutoJV> findAllListing();
	
	public Long saveGstAutoJv(GSTAutoJV autojv);
	public GSTAutoJV findGstAutojv(Long gstId);

	public PayrollAutoJV findOneView(long id);
	List<PayrollEmployeeDetails> findAllPayrollEmployeeList(Long entryId);
	
	public List<GSTAutoJV> getAllGstAutoJVReletedToCompany(Long company_id);
	
	public String deleteGSTAutoJVByIdValue(Long gstauto_id,Long company_id);
	
	GSTAutoJV getGstAutoJvById(Long id);
	public PayrollAutoJV findOneWithAll(Long payroll_id);
	
	PayrollEmployeeDetails editEmployeeForPayroll(Long entryId);
	
	void updateSalesEntry(PayrollAutoJV entry);
	Long saveYearEndAutoJV(ProfitAndLossReportForm form);
	public CopyOnWriteArrayList<YearEndJV> findAllYearEndVoucherReletedToCompany();
	public YearEndJV getYearEndJVById(Long id);
	public List<YearEndJvSubledgerDetails> getAllYearEndJvSubledgerDetails(Long entityId);
	public String deleteByYearEndJVIdValue(Long entityId);

	
}