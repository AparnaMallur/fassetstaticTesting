package com.fasset.dao.interfaces;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.GSTAutoJV;
import com.fasset.entities.PayrollAutoJV;
import com.fasset.entities.PayrollEmployeeDetails;
import com.fasset.entities.PayrollSubledgerDetails;
import com.fasset.entities.YearEndJV;
import com.fasset.entities.YearEndJvSubledgerDetails;

public interface IPayrollAutoJVServiceDAO extends IGenericDao<PayrollAutoJV>{
	

	public Long savePayrollAutoJv(PayrollAutoJV autojv);
	public List<PayrollAutoJV> getAllPayrollJVReletedToCompany(Long company_id);
	PayrollAutoJV getById(Long id);
	GSTAutoJV getGstAutoJvById(Long id);
	int getCountByDate(Long companyId, String range, LocalDate date);
	int getCountByDateAutoJV(Long companyId, String range, LocalDate date);
	int getCountByDategst(Long companyId, String range, LocalDate date);
	
	public Long saveGstAutoJv(GSTAutoJV autojv);
	public GSTAutoJV findGstAutojv(Long gstId);
	public void deletePayrollEmployeeId(Long entityId);
	public String deletePayroll(Long entityId);
	public String deleteGSTAutoJVByIdValue(Long gstauto_id);
	public PayrollAutoJV findOneView(long id);
	List<PayrollEmployeeDetails> findAllPayrollEmployeeList(Long entryId);
	List<PayrollSubledgerDetails> findAllPayrollSubledgerList(Long entryId);
	public List<GSTAutoJV> getAllGstAutoJVReletedToCompany(Long company_id);
	public void deleteByPayrollSubledgerDetails(Long entityId);
	public PayrollAutoJV findOneWithAll(Long payroll_id);
	PayrollEmployeeDetails editEmployeeForPayroll(Long entryId);
	void updateSalesEntry(PayrollAutoJV entry);
	public void deleteDetails(Long entityId);
	public Long saveYearEndAutoJv(YearEndJV autojv);
	
	public YearEndJV getYearEndJVById(Long id);
	public CopyOnWriteArrayList<YearEndJV> findAllYearEndVoucherReletedToCompany() ;
	public List<YearEndJvSubledgerDetails> getAllYearEndJvSubledgerDetails(Long entityId);
	public String deleteByYearEndIdValue(Long entityId) ;
	public int getCountByDateYearAuto(Long companyId, String range, LocalDate date);
	public int getVoucherS(Long companyId, String range, LocalDate date);
}
