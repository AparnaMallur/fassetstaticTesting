package com.fasset.form;

import java.util.List;

import com.fasset.entities.Payment;
import com.fasset.entities.PaymentDetails;

/**
 * @author "Vishwajeet"
 *
 */
public class PaymentForm {
	private Payment payment;
	private List<PaymentDetails> paymentDetailList;	
	private Double closingbalance ;
	
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public Double getClosingbalance() {
		return closingbalance;
	}
	public void setClosingbalance(Double closingbalance) {
		this.closingbalance = closingbalance;
	}
	public List<PaymentDetails> getPaymentDetailList() {
		return paymentDetailList;
	}
	public void setPaymentDetailList(List<PaymentDetails> paymentDetailList) {
		this.paymentDetailList = paymentDetailList;
	}
	
	

}
