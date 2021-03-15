package com.fasset.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IContraDAO;
import com.fasset.dao.interfaces.ISubLedgerDAO;
import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IBankDAO;
import com.fasset.entities.SubLedger;
import com.fasset.entities.Contra;
import com.fasset.service.interfaces.IContraService;
import com.fasset.service.interfaces.IBankService;
import com.fasset.service.interfaces.ISubLedgerService;
import com.fasset.exceptions.MyWebException;

@Service
@Transactional
public class ContraServiceImpl implements IContraService {	
		@Autowired
		private IContraDAO contraDao;
		
		@Autowired
		private ISubLedgerDAO subLedgerDAO ;
		
		@Autowired
		private IAccountingYearDAO accountYearDAO;
		
		@Autowired
		private IBankDAO bankDao;
		
		@Autowired
		private IBankService bankService;
		
		@Autowired
		private ISubLedgerService subledgerService;
		
		@Override
		public void add(Contra entity) throws MyWebException {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void update(Contra entity) throws MyWebException {
			Contra contra = contraDao.findOneWithAll(entity.getTransaction_id());
			
			Contra preContra = new Contra();
			if(contra.getAmount()!=null)
			{
			preContra.setAmount(contra.getAmount());
			}
			if(contra.getCompany()!=null)
			{
			preContra.setCompany(contra.getCompany());
			}
			if(contra.getType()!=null)
			{
			preContra.setType(contra.getType());
			}
			if(contra.getDate()!=null)
			{
			preContra.setDate(contra.getDate());
			}
			
			if(contra.getAccounting_year_id()!=null)
			{
			preContra.setAccounting_year_id(contra.getAccounting_year_id());
			}
			if(contra.getDeposite_to() != null){
				preContra.setDeposite_to(contra.getDeposite_to());
			}
			if(contra.getWithdraw_from() != null){	
				preContra.setWithdraw_from(contra.getWithdraw_from());
			}
			
			SubLedger cashSubledger = subLedgerDAO.findOne("Cash In Hand",contra.getCompany().getCompany_id());
			
			
			contra.setUpdate_date(new LocalDate());
			if(entity.getAmount()!=null)
			{
			contra.setAmount(entity.getAmount());
			}
			if(entity.getOther_remark()!=null)
			{
			contra.setOther_remark(entity.getOther_remark());
			}
			if(entity.getType()!=null)
			{
			contra.setType(entity.getType());
			}
			
			if(entity.getDate()!=null)
			{
			contra.setDate(entity.getDate());
			}
			if(entity.getType()!=null && entity.getDepositeTo()!=null && entity.getType() == MyAbstractController.CONTRA_DEPOSITE){	
				contra.setDeposite_to(bankDao.findOne(entity.getDepositeTo()));
				contra.setWithdraw_from(null);
			}
			else if(entity.getType()!=null && entity.getWithdrawFrom()!=null && entity.getType() == MyAbstractController.CONTRA_WITHDRAW){	
				contra.setWithdraw_from(bankDao.findOne(entity.getWithdrawFrom()));
				contra.setDeposite_to(null);
			}
			else if(entity.getType()!=null && entity.getWithdrawFrom()!=null && entity.getDepositeTo()!=null && entity.getType() == MyAbstractController.CONTRA_TRANSFER){	
				contra.setDeposite_to(bankDao.findOne(entity.getDepositeTo()));
				contra.setWithdraw_from(bankDao.findOne(entity.getWithdrawFrom()));
			}
			if(entity.getUpdated_by()!=null)
			{
				contra.setUpdated_by(entity.getUpdated_by());
			}
			contraDao.update(contra);
		
			try {
				if(preContra.getType()!=null && contra.getType()!=null && preContra.getType() == contra.getType()) {
					
					if(entity.getType()!=null && entity.getType() == MyAbstractController.CONTRA_DEPOSITE){
						
						if((entity.getAmount()!=null &&  preContra.getAmount()!=null && entity.getAmount() != preContra.getAmount()) || (entity.getDepositeTo()!=null && preContra.getDeposite_to()!=null && entity.getDepositeTo() != preContra.getDeposite_to().getBank_id())){
							if((entity.getAmount() != preContra.getAmount()) && (entity.getDepositeTo() != preContra.getDeposite_to().getBank_id())){
								
								try{
								
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
									
									if(cashSubledger!=null){
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double)preContra.getAmount(),(double) 0,(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double)entity.getAmount(),(double) 0,(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
									}
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
							else if(entity.getAmount() != preContra.getAmount()){
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
									if(cashSubledger!=null){
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double)preContra.getAmount(),(double) 0,(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double)entity.getAmount(),(double) 0,(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
									}
								}
								catch(Exception e){
									e.printStackTrace();
								}
								
							}
						}
					}
					else if(entity.getType() == MyAbstractController.CONTRA_WITHDRAW){
						if(entity.getAmount() != preContra.getAmount() || entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id()){
							if((entity.getAmount() != preContra.getAmount()) && (entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id())){
		
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									if(cashSubledger!=null){
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double) 0,(double)preContra.getAmount(), (long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double) 0,(double)entity.getAmount(),(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
								    }
								}
								catch(Exception e){
									e.printStackTrace();
								}
								
							}
							else if(entity.getAmount() != preContra.getAmount()){
								
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									if(cashSubledger!=null){
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double) 0,(double)preContra.getAmount(), (long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
										subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),cashSubledger.getSubledger_Id(),(long) 2,(double) 0,(double)entity.getAmount(),(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
									}
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
						}
					}
					else if(entity.getType() == MyAbstractController.CONTRA_TRANSFER){
						if(entity.getAmount() != preContra.getAmount() || entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id() || entity.getDepositeTo() != preContra.getDeposite_to().getBank_id()){
							if((entity.getAmount() != preContra.getAmount()) && entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id() && entity.getDepositeTo() != preContra.getDeposite_to().getBank_id()){
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
								}
								catch(Exception e){
									e.printStackTrace();
								}
										
							}
							else if(entity.getAmount() != preContra.getAmount() && entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id()){
		
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
							
							else if(entity.getAmount() != preContra.getAmount() && entity.getDepositeTo() != preContra.getDeposite_to().getBank_id()){
							
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
		                        }
								catch(Exception e){
									e.printStackTrace();
								}
							}
		
							else if(entity.getWithdrawFrom() != preContra.getWithdraw_from().getBank_id()){	
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
							else if(entity.getDepositeTo() != preContra.getDeposite_to().getBank_id()){						
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
							else if(entity.getAmount() != preContra.getAmount()){	
								try{
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getWithdrawFrom(),(long) 3,(double)entity.getAmount(),(double) 0,(long) 1);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
									bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),entity.getDate(),preContra.getCompany().getCompany_id(),entity.getDepositeTo(),(long) 3,(double) 0,(double)entity.getAmount(),(long) 1);
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
						}
					}
				}
				else {
					//Rollback previous contra affection
					if(preContra.getType() == MyAbstractController.CONTRA_DEPOSITE){
						bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double)preContra.getAmount(),(double) 0,(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
						}
					}
					else if(preContra.getType() == MyAbstractController.CONTRA_WITHDRAW){
						bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double) 0,(double)preContra.getAmount(),(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
						}
						
					}
					else{
						bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)preContra.getAmount(),(long) 0);
						bankService.addbanktransactionbalance(preContra.getAccounting_year_id().getYear_id(),preContra.getDate(),preContra.getCompany().getCompany_id(),preContra.getWithdraw_from().getBank_id(),(long) 3,(double)preContra.getAmount(),(double) 0,(long) 0);
					}
					
					//New contra affection
					if(contra.getType() == MyAbstractController.CONTRA_DEPOSITE){
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(),contra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 1);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double)contra.getAmount(),(double) 0,(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
						}
					}
					else if(contra.getType() == MyAbstractController.CONTRA_WITHDRAW){
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(),contra.getWithdraw_from().getBank_id(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 1);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double) 0,(double)contra.getAmount(),(long) 1,null,null,null,null,null,null,contra,null,null,null,null,null);
						}
						
					}
					else{
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(),contra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 1);
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),entity.getDate(),contra.getCompany().getCompany_id(),contra.getWithdraw_from().getBank_id(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 1);
					}
				}
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public List<Contra> list() {
			return null;
		}
		
		@Override
		public Contra getById(Long id) throws MyWebException {
			return contraDao.findOne(id);
		}
		@Override
		public Contra getById(String id) throws MyWebException {
			return null;
		}
		
		@Override
		public void removeById(Long id) throws MyWebException {
						
		}
		
		@Override
		public void removeById(String id) throws MyWebException {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void remove(Contra entity) throws MyWebException {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public long countRegs() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void merge(Contra entity) throws MyWebException {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public String saveContra(Contra contra) {
			try {
				contra.setCreated_date(new LocalDate());
				contra.setAccounting_year_id(accountYearDAO.findOne(contra.getAccountYearId()));			
				if(contra.getType() == MyAbstractController.CONTRA_DEPOSITE){
					contra.setDeposite_to(bankDao.findOne(contra.getDepositeTo()));
				}
				else if(contra.getType() == MyAbstractController.CONTRA_WITHDRAW){
					contra.setWithdraw_from(bankDao.findOne(contra.getWithdrawFrom()));
				}
				else{
					contra.setDeposite_to(bankDao.findOne(contra.getDepositeTo()));
					contra.setWithdraw_from(bankDao.findOne(contra.getWithdrawFrom()));
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			contra.setLocal_time(new LocalTime());
			Long id=contraDao.saveContradao(contra);
			
			Contra contra1 = null;
			try {
				contra1 = contraDao.findOne(id);
			} catch (MyWebException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(id!=null){
				try {
					SubLedger cashSubledger = subLedgerDAO.findOne("Cash In Hand",contra.getCompany().getCompany_id());				
					
					if(contra.getType() == MyAbstractController.CONTRA_DEPOSITE){
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getDepositeTo(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 1);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double)contra.getAmount(),(double) 0,(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
						}
					}
					else if(contra.getType() == MyAbstractController.CONTRA_WITHDRAW){
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getWithdrawFrom(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 1);
						if(cashSubledger!=null){
							subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2, (double) 0,(double)contra.getAmount(),(long) 1,null,null,null,null,null,null,contra1,null,null,null,null,null);
						}
						
					}
					else{
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getDepositeTo(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 1);
						bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getWithdrawFrom(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return " Contra saved successfully";
			}
			else{
				return "Please try again ";
			}
		}
		
		@Override
		public List<Contra> findAll() {			
			return contraDao.findAll();
		}
		
		@Override
		public String deleteByIdValue(Long entityId) {
			try {
				Contra contra = contraDao.findOne(entityId);
				SubLedger cashSubledger = subLedgerDAO.findOne("Cash In Hand",contra.getCompany().getCompany_id());		
				
				if(contra.getType() !=null && contra.getType() == MyAbstractController.CONTRA_DEPOSITE){
					
					if(contra.getDeposite_to()!=null && contra.getAmount()!=null)
					{
					bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 0);
					}
					if(cashSubledger!=null && contra.getAmount()!=null){
						subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2,(double)contra.getAmount(),(double) 0,(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
				    }
				}
				else if(contra.getType() !=null && contra.getType() == MyAbstractController.CONTRA_WITHDRAW){
					if(contra.getWithdraw_from()!=null && contra.getAmount()!=null)
					{
					bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getWithdraw_from().getBank_id(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 0);
					}
					
					if(cashSubledger!=null && contra.getAmount()!=null){
						subledgerService.addsubledgertransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(), cashSubledger.getSubledger_Id(), (long) 2,(double) 0,(double)contra.getAmount(),(long) 0,null,null,null,null,null,null,contra,null,null,null,null,null);
				    }
				}
				else{
					if(contra.getDeposite_to()!=null && contra.getWithdraw_from()!=null && contra.getAmount()!=null)
					{
					bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getDeposite_to().getBank_id(),(long) 3,(double) 0,(double)contra.getAmount(),(long) 0);
					bankService.addbanktransactionbalance(contra.getAccounting_year_id().getYear_id(),contra.getDate(),contra.getCompany().getCompany_id(),contra.getWithdraw_from().getBank_id(),(long) 3,(double)contra.getAmount(),(double) 0,(long) 0);
				    }
				}
			} catch (MyWebException e) {
				e.printStackTrace();
			}
			return contraDao.deleteByIdValue(entityId);
		}

		@Override
		public List<SubLedger> getsubledger() {
			return contraDao.getsubledger();
		}

		@Override
		public Contra isExists(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Contra> findAllContraEntry(Long CompanyId,Boolean flag) {
			return contraDao.findAllContraEntry(CompanyId,flag);
		}

		@Override
		public Contra findOneWithAll(Long conId) {
			
			return contraDao.findOneWithAll(conId);
		}

		@Override
		public Long saveContraThroughExcel(Contra contra) {
			return contraDao.saveContraThroughExcel(contra);
			
		}

		@Override
		public List<Contra> findAllContraEntries(Long CompanyId) {
			return contraDao.findAllContraEntries(CompanyId);
		}

		@Override
		public List<Contra> getCashBookBankBookReport(LocalDate from_date, LocalDate to_date, Long companyId, Integer type) {
			return contraDao.getCashBookBankBookReport(from_date, to_date, companyId, type);
		}

		@Override
		public List<Contra> getDayBookReport(LocalDate from_date,LocalDate to_date, Long companyId) {
			return contraDao.getDayBookReport(from_date,to_date, companyId);
		}

		@Override
		public List<Contra> getContraListForLedgerReport(LocalDate from_date, LocalDate to_date, Long companyId,Long bank_id) {
			// TODO Auto-generated method stub
			return contraDao.getContraListForLedgerReport(from_date, to_date, companyId,bank_id);
		}

		@Override
		public Contra isExcelVocherNumberExist(String vocherNo, Long companyId) {
			// TODO Auto-generated method stub
			return contraDao.isExcelVocherNumberExist(vocherNo, companyId);
		}
		
}