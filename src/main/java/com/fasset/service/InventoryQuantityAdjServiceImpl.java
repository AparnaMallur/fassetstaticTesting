package com.fasset.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.IAccountingYearDAO;
import com.fasset.dao.interfaces.IInventoryQuantityAdjDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.entities.InventoryQuantityAdjustment;
import com.fasset.entities.Product;
import com.fasset.entities.Stock;
import com.fasset.exceptions.MyWebException;
import com.fasset.service.interfaces.IInventoryQuantityAdjService;
import com.fasset.service.interfaces.IStockService;


@Transactional
@Service
public class InventoryQuantityAdjServiceImpl implements IInventoryQuantityAdjService{
	
	@Autowired
	private IInventoryQuantityAdjDAO InventoryQuantityDao;
	
	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private IAccountingYearDAO accountingYearDao;
	
	@Autowired
	private IStockService stockService;
	
	@Override
	public void add(InventoryQuantityAdjustment entity) throws MyWebException {
		try{
			Stock stock = null;
			stock=stockService.isstockexist((long)entity.getCompany().getCompany_id(), (long)entity.getProductId());
			
			Product product = productDao.findOne(entity.getProductId());
			entity.setProduct(product);
			entity.setYearId(entity.getYearId());
			entity.setAccounting_year_id(accountingYearDao.findOne(entity.getYearId()));
			entity.setCreated_date(new LocalDate());
			entity.setDate(entity.getDate());
			entity.setIs_addition(entity.getIs_addition());
			
			if(stock==null) // If user added first time products in to to the stock
			{
				Stock stockdata = new Stock();
				stockdata.setCompany_id(entity.getCompany().getCompany_id());
				stockdata.setProduct_id(entity.getProductId());
				stockdata.setQuantity(entity.getQuantity());
				stockdata.setAmount(entity.getValue()*entity.getQuantity());
				Long id =stockService.saveStock(stockdata);
				
				if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
				{
			    stockService.addStockInfoOfProduct(id, entity.getCompany().getCompany_id(), entity.getProductId(),entity.getQuantity(), (double)entity.getValue());
	       		}
				entity.setStock(stockService.isstockexist((long)entity.getCompany().getCompany_id(), (long)entity.getProductId()));
			}
			else // If user modified stock i.e addition or subtract
			{
				if(entity.getIs_addition()==true) // addition
				{
					stock.setQuantity(stock.getQuantity()+entity.getQuantity());
					stock.setAmount(stock.getAmount()+(entity.getValue()*entity.getQuantity()));
					if(!product.getProduct_name().contains("Non-"))// for blocking Non-Inventory products, it will always start with Non-
					{
		    	    stockService.addStockInfoOfProduct(stock.getStock_id(), entity.getCompany().getCompany_id(), entity.getProductId(),entity.getQuantity(),(double)entity.getValue());
	           		}
				}
				else //subtract
				{
					double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), entity.getCompany().getCompany_id(), entity.getProductId(), entity.getQuantity());
					stock.setQuantity(stock.getQuantity()-entity.getQuantity());
					stock.setAmount(stock.getAmount()-amount);
				}
				stockService.updateStock(stock);
				entity.setStock(stock);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		  //entity.setValue(stock);
		InventoryQuantityDao.create(entity);
	}

	@Override
	public void update(InventoryQuantityAdjustment entity) throws MyWebException {
		InventoryQuantityAdjustment adjustment = new InventoryQuantityAdjustment();
		try{
			Stock stock = null;
			adjustment = InventoryQuantityDao.findOne(entity.getInventory_adj_id());
			adjustment.setProduct(productDao.findOne(entity.getProductId()));
			adjustment.setRemark(entity.getRemark());
			adjustment.setAccounting_year_id(accountingYearDao.findOne(entity.getYearId()));
			adjustment.setUpdated_date(new LocalDate());
			adjustment.setStatus(entity.getStatus());
			adjustment.setDate(entity.getDate());
			adjustment.setIs_addition(entity.getIs_addition());	
			
			stock=stockService.isstockexist((long)adjustment.getCompany().getCompany_id(), (long)adjustment.getProduct().getProduct_id());
			if(stock==null)
			{
				Stock stockdata = new Stock();
				stockdata.setCompany_id(adjustment.getCompany().getCompany_id());
				stockdata.setProduct_id(adjustment.getProduct().getProduct_id());
				stockdata.setQuantity(entity.getQuantity());
				stockdata.setAmount(entity.getValue()*entity.getQuantity());
				stockService.saveStock(stockdata);
				adjustment.setStock(stockService.isstockexist((long)adjustment.getCompany().getCompany_id(), (long)adjustment.getProduct().getProduct_id()));
			}
			else
			{
				
						if(entity.getIs_addition()==true)
						{
							stock.setQuantity((stock.getQuantity()+entity.getQuantity())-adjustment.getQuantity());
							stock.setAmount(stock.getAmount()+(entity.getValue()*entity.getQuantity())-(adjustment.getValue()*adjustment.getQuantity()));
		
						}
						else
						{
							stock.setQuantity((stock.getQuantity()-entity.getQuantity())+adjustment.getQuantity());
							stock.setAmount(stock.getAmount()-(entity.getValue()*entity.getQuantity())+(adjustment.getValue()*adjustment.getQuantity()));
						}
						stockService.updateStock(stock);
				
				adjustment.setStock(stock);
			}
			//adjustment.setDate(new LocalDate(entity.getDateString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		adjustment.setQuantity(entity.getQuantity());
		adjustment.setValue(entity.getValue());
		InventoryQuantityDao.update(adjustment);
	}

	@Override
	public List<InventoryQuantityAdjustment> list() {
		return InventoryQuantityDao.findAll();
	}
	
	@Override
	public List<InventoryQuantityAdjustment> findAllInventoryOfCompany(Long CompanyId) {
		return InventoryQuantityDao.findAllInventoryOfCompany(CompanyId);
	}

	@Override
	public InventoryQuantityAdjustment getById(Long id) throws MyWebException {
		return InventoryQuantityDao.findOne(id);
	}

	@Override
	public InventoryQuantityAdjustment getById(String id) throws MyWebException {
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
	public void remove(InventoryQuantityAdjustment entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(InventoryQuantityAdjustment entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InventoryQuantityAdjustment isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		InventoryQuantityAdjustment adjustment = new InventoryQuantityAdjustment();
		Stock stock=new Stock();
		try {
			adjustment = InventoryQuantityDao.findOne(entityId);
			stock=stockService.isstockexist((long)adjustment.getCompany().getCompany_id(), (long)adjustment.getProduct().getProduct_id());
			
			if(stock!=null)
			{	
						if(adjustment.getIs_addition()==true)
						{
							double amount = stockService.substractStockInfoOfProduct(stock.getStock_id(), adjustment.getCompany().getCompany_id(), adjustment.getProduct().getProduct_id(), adjustment.getQuantity());
							stock.setQuantity(stock.getQuantity()-adjustment.getQuantity());
							stock.setAmount(stock.getAmount()-amount);
						}
						else
						{
							
							stock.setQuantity(stock.getQuantity()+adjustment.getQuantity());
							stock.setAmount(stock.getAmount()+(adjustment.getValue()*adjustment.getQuantity()));
							stockService.addStockInfoOfProduct(stock.getStock_id(), adjustment.getCompany().getCompany_id(), adjustment.getProduct().getProduct_id(),adjustment.getQuantity(),(double)adjustment.getValue());
						}
						stockService.updateStock(stock);				
			}
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return InventoryQuantityDao.deleteByIdValue(entityId);
	}

}
