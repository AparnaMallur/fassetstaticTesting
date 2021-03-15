package com.fasset.form;

import java.util.List;
import java.util.Set;

import com.fasset.entities.AccountingYear;
import com.fasset.entities.ClientValidationChecklist;
import com.fasset.entities.ClientValidationChecklistStatus;
import com.fasset.entities.Company;
import com.fasset.entities.QuotationDetails;
import com.fasset.entities.User;

public class ClientValidationChecklistForm {

	private ClientValidationChecklistStatus status;
	private User user ;
	private Company company;
	private List<ClientValidationChecklist> checklist ;
	private List<AccountingYear> yearList;
	private Set<QuotationDetails> quotationDetails;
	
	public ClientValidationChecklistStatus getStatus() {
		return status;
	}
	public void setStatus(ClientValidationChecklistStatus status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<ClientValidationChecklist> getChecklist() {
		return checklist;
	}
	public void setChecklist(List<ClientValidationChecklist> checklist) {
		this.checklist = checklist;
	}
	public List<AccountingYear> getYearList() {
		return yearList;
	}
	public void setYearList(List<AccountingYear> yearList) {
		this.yearList = yearList;
	}
	public Set<QuotationDetails> getQuotationDetails() {
		return quotationDetails;
	}
	public void setQuotationDetails(Set<QuotationDetails> quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
}
