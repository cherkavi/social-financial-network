package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;

public class RatingsUser  implements Serializable{
	private static final long serialVersionUID = -6085757128698439757L;
	
	private int ratingNumber;
	private String fullName;
	private String cardNumber;
	
	
	public int getRatingNumber() {
		return ratingNumber;
	}

	public void setRatingNumber(int ratingNumber) {
		this.ratingNumber = ratingNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getSumBon() {
		return sumBon;
	}

	public void setSumBon(double sumBon) {
		this.sumBon = sumBon;
	}

	private double sumBon;
	
	public RatingsUser(){
	}

	
}
