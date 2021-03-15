package com.fasset.dao.interfaces;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.ForgotPasswordLog;

public interface IForgotPasswordLogDAO extends IGenericDao<ForgotPasswordLog>{
	ForgotPasswordLog getLastPasswordUpdateDate(Long userId);
}
