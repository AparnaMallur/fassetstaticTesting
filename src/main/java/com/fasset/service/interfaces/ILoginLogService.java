package com.fasset.service.interfaces;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasset.entities.LoginLog;
import com.fasset.service.interfaces.generic.IGenericService;

public interface ILoginLogService extends IGenericService<LoginLog>{

	void create(Long user_id, int i, String remoteAddr);
	List <LoginLog> Loginlog(Long user_id,LocalDate from_date, LocalDate to_date);

}
