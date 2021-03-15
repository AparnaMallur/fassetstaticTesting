package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import com.fasset.entities.AccountSubGroup;
import com.fasset.entities.Ledger;
import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;

public class LedgerForm {

	private String LedgerName;
	private String SubgroupName;
	private Double ledgerdebit_balance;
	private Double ledgercredit_balance;
	
	private List<OpeningBalancesForm>  subledgerList;
	public String getLedgerName() {
		return LedgerName;
	}
	public void setLedgerName(String ledgerName) {
		LedgerName = ledgerName;
	}
	public List<OpeningBalancesForm> getSubledgerList() {
		return subledgerList;
	}
	public void setSubledgerList(List<OpeningBalancesForm> subledgerList) {
		this.subledgerList = subledgerList;
	}
	public Double getLedgerdebit_balance() {
		return ledgerdebit_balance;
	}
	public void setLedgerdebit_balance(Double ledgerdebit_balance) {
		this.ledgerdebit_balance = ledgerdebit_balance;
	}
	public Double getLedgercredit_balance() {
		return ledgercredit_balance;
	}
	public void setLedgercredit_balance(Double ledgercredit_balance) {
		this.ledgercredit_balance = ledgercredit_balance;
	}
	public String getSubgroupName() {
		return SubgroupName;
	}
	public void setSubgroupName(String subgroupName) {
		SubgroupName = subgroupName;
	}
}
