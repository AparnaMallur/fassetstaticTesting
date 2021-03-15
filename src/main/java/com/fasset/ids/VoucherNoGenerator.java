/**
 * 
 */
package com.fasset.ids;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.joda.time.LocalDate;
/**
 * @author "Vishwajeet"
 *
 */
public class VoucherNoGenerator implements IdentifierGenerator{
	
	String prefix = "PV";
	Long id;
	LocalDate today = new LocalDate();
	Date date = Date.valueOf(today.toString());
	String voucherNo;
	int count = 0;

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {	
		
		Connection con = session.connection();

		try {
			PreparedStatement pst = con.prepareStatement("select count(payment_id) as count from payment where created_date=?");
			pst.setDate(1, date);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (count > 0) {
			if (count < 9) {
				voucherNo = prefix + today.getDayOfMonth() + today.getMonthOfYear() + today.getYear() + "-" + "00"
						+ (count + (long) 1);
			}

			else if (count >= 9 && count < 99) {
				voucherNo = prefix + today.getDayOfMonth() + today.getMonthOfYear() + today.getYear() + "-" + "0"
						+ (count + (long) 1);
			} else {
				voucherNo = prefix + today.getDayOfMonth() + today.getMonthOfYear() + today.getYear() + "-"
						+ (count + (long) 1);
			}
		} else {
			voucherNo = prefix + today.getDayOfMonth() + today.getMonthOfYear() + today.getYear() + "-" + "001";
		}
		return voucherNo;
	}
	
	

}
