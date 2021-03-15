package com.fasset.dao.interfaces.generic;

import java.util.List;

import org.hibernate.Criteria;
import org.joda.time.LocalDate;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.exceptions.MyWebException;

public interface IGenericDao<T extends AbstractEntity> {

	T findOne(final Long id) throws MyWebException;

	T findOne(Criteria crt) throws MyWebException;

	T findOne(final String id) throws MyWebException;

	List<T> findAll();

	void create(T entity) throws MyWebException;

	void update(final T entity) throws MyWebException;

	void merge(final T entity) throws MyWebException;
	
	void delete(final T entity) throws MyWebException;

	void deleteById(final Long id) throws MyWebException;

	void deleteById(final String id) throws MyWebException;

	long count();

	boolean validatePreTransaction(final T entity) throws MyWebException;

	public void refresh(T entity);
	
	public T isExists(String name);

	

}
