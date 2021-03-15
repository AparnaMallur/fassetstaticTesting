package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.ActivityLog;
import com.fasset.entities.YearEndJvSubledgerDetails;
import com.fasset.entities.YearEnding;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IYearEndingService extends IGenericService<YearEnding>{
	
	List<YearEnding> findAllYearEnding(Long comapany_id);
	List<YearEnding> findAllYearEnding();
	YearEnding findYearEnd(Long yearId,Long company_id);
	List<ActivityLog> findAllActivityLog(Long user_id,LocalDate from_date, LocalDate to_date);
	void saveActivityLogForm(Long user_id,Long customer_id,
			Long supplier_id,Long product_id,Long ledger_id,
			Long subLedger_id,Long bank_id,Long quotation_id,Long primary_approval,Long secondary_approval,Long rejection);
	List<YearEndJvSubledgerDetails> findAllYearEndJVdtls(Long id); // method for YearEndJv not shown in ledger issue
}
