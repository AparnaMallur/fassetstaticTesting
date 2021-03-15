package com.fasset.service.interfaces.generic;

import java.util.List;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.exceptions.MyWebException;

public interface IGenericService<T extends AbstractEntity> {

	public void add(T entity) throws MyWebException;

	public void update(T entity) throws MyWebException;

	public List<T> list();

	public T getById(Long id) throws MyWebException;

	public T getById(String id) throws MyWebException;

	public void removeById(Long id) throws MyWebException;

	public void removeById(String id) throws MyWebException;

	public void remove(T entity) throws MyWebException;

	public long countRegs();

	void merge(T entity) throws MyWebException;
	
	public T isExists(String name);

}
