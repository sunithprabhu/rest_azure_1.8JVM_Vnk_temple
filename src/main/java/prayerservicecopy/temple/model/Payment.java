package prayerservicecopy.temple.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="paymentVnkTemple")
public class Payment {
	
	@Id
	private String orderId ;
	
	private String personName;
	
	private String amount;
	
	private String paymentId;
	
	private String paymentSignature;
	
	private String paymentStatus;
	
	public Payment()
	{
		
	}
	public Payment(String orderId, String personName, String amount, String paymentId, String paymentSignature,
			String paymentStatus) {
		super();
		this.orderId = orderId;
		this.personName = personName;
		this.amount = amount;
		this.paymentId = paymentId;
		this.paymentSignature = paymentSignature;
		this.paymentStatus = paymentStatus;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentSignature() {
		return paymentSignature;
	}

	public void setPaymentSignature(String paymentSignature) {
		this.paymentSignature = paymentSignature;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
