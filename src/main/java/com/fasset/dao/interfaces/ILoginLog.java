package com.fasset.dao.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.dao.interfaces.generic.IGenericDao;
import com.fasset.entities.LoginLog;

public interface ILoginLog  extends IGenericDao<LoginLog>{

	List <LoginLog> Loginlog(Long user_id,LocalDate from_date, LocalDate to_date);
}
