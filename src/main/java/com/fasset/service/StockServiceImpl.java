package com.fasset.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.dao.interfaces.ICompanyDAO;
import com.fasset.dao.interfaces.IProductDAO;
import com.fasset.dao.interfaces.IStockDAO;
import com.fasset.entities.Stock;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.StockReportForm;
import com.fasset.service.interfaces.IStockService;

@Service
@Transactional
public class StockServiceImpl implements IStockService{

	@Autowired
	private IStockDAO stockDao;
	
	@Autowired
	private ICompanyDAO companyDao;
	
	@Autowired
	private IProductDAO productDao;
	
	
	@Override
	public void add(Stock entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Stock entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Stock> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock getById(Long id) throws MyWebException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock getById(String id) throws MyWebException {
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
	public void remove(Stock entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long countRegs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void merge(Stock entity) throws MyWebException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Stock isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Long saveStock(Stock stock) {
		// TODO Auto-generated method stub
		Long id= stockDao.saveStock(stock);
		return id;
	}

	@Override
	@Transactional
	public void updateStock(Stock stock) {
		// TODO Auto-generated method stub
		 stockDao.updateStock(stock);
	}

	@Override
	public List<Stock> findAll() {
		// TODO Auto-generated method stub
		return stockDao.findAll();
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Stock isstockexist(Long company_Id, Long product_Id) {
		// TODO Auto-generated method stub
		return stockDao.isstockexist(company_Id, product_Id);
	}

	@Override
	public List<StockReportForm> getStockDetails(Long company_Id, Long product_Id) {
		// TODO Auto-generated method stub
		return stockDao.getStockDetails(company_Id, product_Id);
	}

	@Override
	public void addStockInfoOfProduct(Long stock_id, Long company_id, Long product_id, Double quantity, Double rate) {
		
		try {
			stockDao.addStockInfoOfProduct(stockDao.findOne(stock_id), companyDao.findOne(company_id), productDao.findOne(product_id), quantity, rate);
		} catch (MyWebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Double substractStockInfoOfProduct(Long stock_id, Long company_id, Long product_id,Double quantity) {
		// TODO Auto-generated method stub
		double amount = 0;
		try {
			amount=amount+stockDao.substractStockInfoOfProduct(stock_id,company_id, product_id,quantity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amount;
	}

	

}
