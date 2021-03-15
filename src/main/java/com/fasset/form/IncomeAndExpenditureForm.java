package com.fasset.form;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.AccountGroup;
import com.fasset.entities.Company;
import com.fasset.entities.SubLedger;

public class IncomeAndExpenditureForm {
	private Double surplus;
	private Double deficit;
	private Double totalExpense;
	private Double totalIncome;
	private List<IncomeOrExpense> incomes = new ArrayList<IncomeOrExpense>();
	private List<IncomeOrExpense> expenses = new ArrayList<IncomeOrExpense>();
	private Company company ;
	
	private LocalDate fromDate;
	private LocalDate toDate;

	
	public class IncomeOrExpense{		
		private AccountGroup group;		
		private Double totalAmount;
		private List<Details> list = new ArrayList<Details>();
		
		public class Details{
			private SubLedger subLedger;
			private Double amount;
			
			public SubLedger getSubLedger() {
				return subLedger;
			}
			public void setSubLedger(SubLedger subLedger) {
				this.subLedger = subLedger;
			}
			public Double getAmount() {
				return amount;
			}
			public void setAmount(Double amount) {
				this.amount = amount;
			}
			
		}		

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public List<Details> getList() {
			return list;
		}

		public void setList(List<Details> list) {
			this.list = list;
		}

		public AccountGroup getGroup() {
			return group;
		}

		public void setGroup(AccountGroup group) {
			this.group = group;
		}
		
		public Details getDetailsClass(){
			return new Details();
		}
		
	}

	public Double getSurplus() {
		return surplus;
	}

	public void setSurplus(Double surplus) {
		this.surplus = surplus;
	}

	public Double getDeficit() {
		return deficit;
	}

	public void setDeficit(Double deficit) {
		this.deficit = deficit;
	}

	public List<IncomeOrExpense> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<IncomeOrExpense> incomes) {
		this.incomes = incomes;
	}

	public List<IncomeOrExpense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<IncomeOrExpense> expenses) {
		this.expenses = expenses;
	}
	
	public IncomeOrExpense getIncomeOrExpense(){
		return new IncomeOrExpense();
	}

	public Double getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(Double totalExpense) {
		this.totalExpense = totalExpense;
	}

	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
}
