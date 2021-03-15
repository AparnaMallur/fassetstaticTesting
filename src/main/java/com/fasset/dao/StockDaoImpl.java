package com.fasset.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasset.controller.abstracts.MyAbstractController;
import com.fasset.dao.interfaces.IStockDAO;
import com.fasset.dao.interfaces.generic.AbstractHibernateDao;
import com.fasset.entities.Company;
import com.fasset.entities.Ledger;
import com.fasset.entities.Product;
import com.fasset.entities.State;
import com.fasset.entities.Stock;
import com.fasset.entities.StockInfoOfProduct;
import com.fasset.entities.Suppliers;
import com.fasset.entities.YearEnding;
import com.fasset.exceptions.MyWebException;
import com.fasset.form.StockReportForm;

@Repository
@Transactional
public class StockDaoImpl extends AbstractHibernateDao<Stock> implements IStockDAO{

	@Override
	public Stock isExists(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByIdValue(Long entityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stock findOne(Long id) throws MyWebException {
		Criteria criteria = getCurrentSession().createCriteria(Stock.class);
		criteria.add(Restrictions.eq("stock_id", id));
		return (Stock) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> findAll() {
		Session session = getCurrentSession();
		Query query = session.createQuery("from stock order by stock_id ASC ");
		return query.list();
		}

	@Override
	@Transactional
	public Stock isstockexist(Long company_Id, Long product_Id) {
		Stock stock = new Stock();
		Session session = getCurrentSession();
		Query stockquery = session.createQuery("FROM Stock WHERE company_Id = :company_Id and product_Id= :product_Id");
		stockquery.setParameter("company_Id", company_Id);
		stockquery.setParameter("product_Id", product_Id);
		try
		{
		stock = (Stock) stockquery.uniqueResult();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}
	
	@Override
	public Long saveStock(Stock stock) {
		Session session = getCurrentSession();
		stock.setCreated_date(new LocalDate());
		Long id = (Long) session.save(stock);
		session.flush();
	    session.clear();
		return id;
	}
	

	@Override
	public void updateStock(Stock stock) {
		Session session = getCurrentSession();	
		stock.setUpdated_date(new LocalDate());
		session.merge(stock);
		session.flush();
	    session.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockReportForm> getStockDetails(Long company_Id, Long product_Id) {
		List<Stock> stockList = new ArrayList<Stock>();
		List<StockReportForm> stockinfoList = new ArrayList<StockReportForm>();
		/*Session session = getCurrentSession();	
		Criteria criteria = session.createCriteria(Stock.class);
		criteria.add(Restrictions.eq("company_id", company_Id));
		if(product_Id>0)
		{
			criteria.add(Restrictions.eq("product_id", product_Id));
		}
		stockList = criteria.list();*/
		
		Session session = getCurrentSession();
		Query query =null;
		if(product_Id>0)
		{
			query = session.createQuery("SELECT stock from Stock stock "
					+ "WHERE stock.company_id =:company_id and stock.product_id =:product_id");
			
			query.setParameter("product_id", product_Id);
		}
		else
		{
			query = session.createQuery("SELECT stock from Stock stock "
					+ "WHERE stock.company_id =:company_id");
		}
		query.setParameter("company_id", company_Id);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
			  stockList.add((Stock)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }	
		
		for(Stock stock : stockList)
		{
			if(stock.getProduct_id()!=null && stock.getQuantity()!=null)
			{
				StockReportForm form = new StockReportForm();
				
				if(getProduct(stock.getProduct_id(),session)!=null)
				{
					if(getProduct(stock.getProduct_id(),session).getProduct_name()!=null)
					{
					form.setProductName(getProduct(stock.getProduct_id(),session).getProduct_name());
					form.setQuantity(stock.getQuantity());
					if(stock.getAmount()!=null)
					{
					form.setAmount(stock.getAmount());
					}
					else
					{
						form.setAmount((double)0);
					}
					}
				}
				
				stockinfoList.add(form);
				
			}
		}
		
		return stockinfoList;
	}
	
	@Transactional
	public Product getProduct(Long proId, Session session) {

		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("product_id", proId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Product) criteria.uniqueResult();

	}

	@Override
	public void addStockInfoOfProduct(Stock stock, Company company, Product product, Double quantity, Double rate) {
		Session session = getCurrentSession();	
		StockInfoOfProduct info = new StockInfoOfProduct();
		info.setStock(stock);
		info.setCompany(company);
		info.setProduct(product);
		info.setProduct_name(product.getProduct_name());
		info.setRate(rate);
		info.setQuantity(quantity);
		info.setDispatch(0.0);
		info.setStatus(true);
		info.setPurchase_date(new LocalDate());
		session.save(info);
	  /*  if ( i % 20 == 0 ) { //20, same as the JDBC batch size
	        //flush a batch of inserts and release memory:
	        session.flush();
	        session.clear();
	    }*/
		
	}

	@Override
	public Double substractStockInfoOfProduct(Long stock_id, Long company_id, Long product_id, Double quantity) {
		
		double amount = 0;
		
		CopyOnWriteArrayList<StockInfoOfProduct> list =  new CopyOnWriteArrayList<StockInfoOfProduct>();
		Session session = getCurrentSession();
		Query query = session.createQuery("SELECT stkinfo from StockInfoOfProduct stkinfo "
				+ "WHERE stkinfo.company.company_id =:company_id and stkinfo.stock.stock_id=:stock_id and stkinfo.product.product_id=:product_id "
				+ "and stkinfo.purchase_date<=:purchase_date and stkinfo.status =:status order by stkinfo.stockinfo_id asc");
		query.setParameter("company_id", company_id);
		query.setParameter("stock_id", stock_id);
		query.setParameter("product_id", product_id);
		query.setParameter("purchase_date", new LocalDate());
		query.setParameter("status", true);
		ScrollableResults scrollableResults = query.setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
		
		  while (scrollableResults.next()) {
		   list.add((StockInfoOfProduct)scrollableResults.get()[0]);
		   session.evict(scrollableResults.get()[0]);
	         }
		  
		  for(StockInfoOfProduct stockinfo : list)
		  {
			  if(!quantity.equals(0.0))
			  {
			  double dispatchQuanty = 0.0;
			  double totalQuanty = 0.0;
			  if(!stockinfo.getQuantity().equals(stockinfo.getDispatch()))
			  {
				  totalQuanty = stockinfo.getQuantity()-stockinfo.getDispatch();
				  
				  if(totalQuanty>=quantity)
				  {
					  amount = amount +(quantity*stockinfo.getRate());
					  dispatchQuanty = stockinfo.getDispatch()+quantity;
					  quantity =quantity-quantity;
				  }
				  else if(quantity>totalQuanty)
				  {
					  amount = amount +(totalQuanty*stockinfo.getRate());
					  dispatchQuanty = stockinfo.getDispatch()+totalQuanty;
					  quantity =quantity-totalQuanty;
				  }
				  Query query1 = null;
				  if(stockinfo.getQuantity().equals(dispatchQuanty))
				  {
					  query1 = session.createSQLQuery("update stock_Info_Of_Product set quantityDispatch=:quantityDispatch,status=:status where stockinfo_id=:stockinfo_id");
					  query1.setParameter("status", false);
				  }
				  else
				  {
					  query1 = session.createSQLQuery("update stock_Info_Of_Product set quantityDispatch=:quantityDispatch where stockinfo_id=:stockinfo_id");
				  }
				  query1.setParameter("quantityDispatch", dispatchQuanty);
				  query1.setParameter("stockinfo_id", stockinfo.getStockinfo_id());
				  query1.executeUpdate();
			  }
			  }
		  }
		  session.clear();
		  return amount;
	}
	
	public void deleteStockInfoOfProduct(List<Long>infoIdList,Session session)
	{
		
		Query query = session.createQuery("delete from StockInfoOfProduct where stockinfo_id in (:stockinfo_id)");
		query.setParameterList("stockinfo_id", infoIdList);
		query.executeUpdate();
		
	}
}
