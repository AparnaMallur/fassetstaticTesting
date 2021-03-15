package com.fasset.service.interfaces;

import java.util.List;

import com.fasset.entities.Stock;
import com.fasset.form.StockReportForm;
import com.fasset.service.interfaces.generic.IGenericService;

public interface IStockService extends IGenericService<Stock>{
	public Long saveStock(Stock stock);
	public void updateStock(Stock stock);
	public List<Stock> findAll();
	public String deleteByIdValue(Long entityId);
	Stock isstockexist(Long company_Id,Long product_Id);
	public List<StockReportForm> getStockDetails(Long company_Id,Long product_Id);
	public void addStockInfoOfProduct(Long stock_id,Long company_id,Long product_id,Double quantity,Double rate);
	public Double substractStockInfoOfProduct(Long stock_id,Long company_id,Long product_id,Double quantity);
}
