package com.tecnotree.dclm.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;


@Document("paymentIntents")
public class PaymentIntents implements Serializable  {


	private String paymentId;
	private String walletId;
	private Double amount;
	private String currency;
	private String methodType;
	private Date createdDate;
	private String clientSecret;
	private String status;
	private String paymentMethodId;
	private String customerId;
	private String paymentType;
	private String  setup_future_usage;
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	
	public String getMethodType() {
		return methodType;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
	
	public String getPaymentMethodId() {
		return paymentMethodId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	

	public String getSetup_future_usage() {
		return setup_future_usage;
	}

	public void setSetup_future_usage(String setup_future_usage) {
		this.setup_future_usage = setup_future_usage;
	}

}
