package com.fasset.ids;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.joda.time.LocalDate;

public class CreditNoteVoucherNoGenerator implements IdentifierGenerator{	

	String prefix = "CNV";
	Long id;
	int count = 0;
	LocalDate today = new LocalDate();
	Date date = Date.valueOf(today.toString());
	String voucherNo;
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		
		Connection con = session.connection();
		
		try {
			PreparedStatement pst = con.prepareStatement("select count(credit_no_id) as count from credit_note where created_date=?");
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
