package com.tecnotree.dclm.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("PaymentConfirmation")
public class PaymentConfirmationDetails  implements Serializable{
	
	private static final long serialVersionUID = 1643813092356878725L;
	
	@Id
	private String id;
	private String vendorname;   
	private String emailId;
	private String mobilno;
	private String amount;
	private String description;
	private String status;
	private String orgId;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobilno() {
		return mobilno;
	}
	public void setMobilno(String mobilno) {
		this.mobilno = mobilno;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
