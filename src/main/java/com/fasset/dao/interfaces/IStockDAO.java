package com.fasset.dao.interfaces;

import java.util.List;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.Company;
import com.fasset.entities.Product;
import com.fasset.entities.Stock;
import com.fasset.form.StockReportForm;

public interface IStockDAO extends IGenericDao<Stock>{
	public String deleteByIdValue(Long entityId);
	Stock isstockexist(Long company_Id,Long product_Id);
	Long saveStock(Stock stock);
	void updateStock(Stock stock);
	public List<StockReportForm> getStockDetails(Long company_Id,Long product_Id);
	public void addStockInfoOfProduct(Stock stock,Company company,Product product,Double quantity,Double rate);
	public Double substractStockInfoOfProduct(Long stock_id, Long company_id, Long product_id,Double quantity);
}
