package com.fasset.dao.interfaces.generic;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasset.entities.abstracts.AbstractEntity;
import com.fasset.exceptions.MyWebException;
import com.google.common.base.Preconditions;

@Repository
public abstract class AbstractHibernateDao<T extends AbstractEntity> implements IGenericDao<T> {

	private Class<T> clazz;

	@Autowired
	SessionFactory sessionFactory;

	public final void setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void close()
	{
		sessionFactory.close();
	}

	@SuppressWarnings("unchecked")
	public T getById(final Long id) throws MyWebException {
		try {
			Preconditions.checkArgument(id != null);
			return (T) this.getCurrentSession().get(this.clazz, id);
		} catch (IllegalArgumentException e) {
			throw new MyWebException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T getById(final String id) throws MyWebException {
		try {
			Preconditions.checkArgument(id != null);
			return (T) this.getCurrentSession().get(this.clazz, id);
		} catch (IllegalArgumentException e) {
			throw new MyWebException(e);
		}
	}

	public void delete(T entity) throws MyWebException {
		try {
			validatePreTransaction(entity);
			getCurrentSession().delete(entity);
		} catch (MyWebException e) {
			throw new MyWebException(e);
		}
	}

	public void deleteById(final Long entityId) throws MyWebException {
		try {
			final T entity = this.getById(entityId);
			Preconditions.checkState(entity != null);
			this.delete(entity);
		} catch (MyWebException e) {
			throw new MyWebException(e);
		}
	}

	public void deleteById(final String entityId) throws MyWebException {

		try {
			final T entity = this.getById(entityId);
			Preconditions.checkState(entity != null);
			this.delete(entity);

		} catch (MyWebException e) {
			throw new MyWebException(e);
		}

	}

	public void update(T entity) throws MyWebException {
		try {
			validatePreTransaction(entity);
			getCurrentSession().saveOrUpdate(entity);
		} catch (MyWebException e) {
			throw new MyWebException(e);
		}
		// getCurrentSession().merge(entity);
	}

	public void merge(T entity) throws MyWebException {
		try {
			validatePreTransaction(entity);
			getCurrentSession().merge(entity);
		} catch (MyWebException e) {
			throw new MyWebException(e);
		}
	}

	public void create(T entity) throws MyWebException {
		try {
			validatePreTransaction(entity);
			getCurrentSession().save(entity);
		} catch (MyWebException e) {
			throw new MyWebException(e);
		}
	}

	public long count() {
		long count;
		count = (long) getCurrentSession().createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
		return count;
	}

	@SuppressWarnings("unchecked")
	public T findOne(final Long id) throws MyWebException {
		try {
			Preconditions.checkArgument(id != null);
			return (T) getCurrentSession().get(clazz, id);
		} catch (IllegalArgumentException e) {
			throw new MyWebException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T findOne(final String id) throws MyWebException {
		try {
			Preconditions.checkArgument(id != null);
			return (T) getCurrentSession().get(clazz, id);
		} catch (IllegalArgumentException e) {
			throw new MyWebException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getCurrentSession().createQuery("from " + clazz.getName()).list();
	}

	public boolean validatePreTransaction(T entity) throws MyWebException {
		try {
			Preconditions.checkNotNull(entity);
		} catch (Exception e) {
			throw new MyWebException("Element is Null", e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public T findOne(Criteria crt) throws MyWebException {
		try {
			Preconditions.checkArgument(crt != null);
			return (T) crt.uniqueResult();
		} catch (IllegalArgumentException e) {
			throw new MyWebException(e);
		}
	}

	public void refresh(T entity) {
		getCurrentSession().refresh(entity);
	}
}
