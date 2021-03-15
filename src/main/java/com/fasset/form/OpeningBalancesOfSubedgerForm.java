package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.SubLedger;

public class OpeningBalancesOfSubedgerForm {
private String AccountGroupName;
private List<String> AccountSubGroupNameList;
private Double Totaldebit_balance;
private Double Totalcredit_balance;
private Long posting_id;
List<LedgerForm>  ledgerformlist;
private AccountGroup group ;
private Integer isReceipt;
private Integer isPayment;

public String getAccountGroupName() {
	return AccountGroupName;
}
public void setAccountGroupName(String accountGroupName) {
	AccountGroupName = accountGroupName;
}
public Double getTotaldebit_balance() {
	return Totaldebit_balance;
}
public void setTotaldebit_balance(Double totaldebit_balance) {
	Totaldebit_balance = totaldebit_balance;
}
public Double getTotalcredit_balance() {
	return Totalcredit_balance;
}
public void setTotalcredit_balance(Double totalcredit_balance) {
	Totalcredit_balance = totalcredit_balance;
}

public List<String> getAccountSubGroupNameList() {
	return AccountSubGroupNameList;
}
public void setAccountSubGroupNameList(List<String> accountSubGroupNameList) {
	AccountSubGroupNameList = accountSubGroupNameList;
}
public Long getPosting_id() {
	return posting_id;
}
public void setPosting_id(Long posting_id) {
	this.posting_id = posting_id;
}
public AccountGroup getGroup() {
	return group;
}
public void setGroup(AccountGroup group) {
	this.group = group;
}
public List<LedgerForm> getLedgerformlist() {
	return ledgerformlist;
}
public void setLedgerformlist(List<LedgerForm> ledgerformlist) {
	this.ledgerformlist = ledgerformlist;
}
public Integer getIsReceipt() {
	return isReceipt;
}
public void setIsReceipt(Integer isReceipt) {
	this.isReceipt = isReceipt;
}
public Integer getIsPayment() {
	return isPayment;
}
public void setIsPayment(Integer isPayment) {
	this.isPayment = isPayment;
}

}
