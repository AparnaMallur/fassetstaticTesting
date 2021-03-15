/**
 * 
 */
package com.fasset.service.interfaces;

import org.joda.time.LocalDate;

import com.fasset.entities.ForgotPasswordLog;
import com.fasset.service.interfaces.generic.IGenericService;

/**
 * @author "Vishwajeet"
 *
 */
public interface IForgotPasswordLogService extends IGenericService<ForgotPasswordLog>{
	LocalDate getLastPasswordUpdateDate(Long userId);

}
