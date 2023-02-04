package prayerservicecopy.temple.model;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Lob;
//import jakarta.persistence.Table;

@javax.persistence.Entity
@javax.persistence.Table(name="Believer")
public class Believers {
	
	@javax.persistence.Id
	private char[] profileNumber;
	
	//@Column(name = "believerName")
	private String believerName;
	
	//@Column(name = "believerPhone")
	private String believerPhone;
	
	//@Column(name = "believerLocation")
	private String believerLocation;
	
	public Believers()
	{
		
	}
	
	public Believers(String believerName, String believerPhone, String believerLocation,
			char[] profileNumber) {
		
		super();
		this.believerName = believerName;
		this.believerPhone = believerPhone;
		this.believerLocation = believerLocation;
		this.profileNumber = profileNumber;
	}

	public String getBelieverName() {
		return believerName;
	}

	public void setBelieverName(String believerName) {
		this.believerName = believerName;
	}

	public String getBelieverPhone() {
		return believerPhone;
	}

	public void setBelieverPhone(String believerPhone) {
		this.believerPhone = believerPhone;
	}

	public String getBelieverLocation() {
		return believerLocation;
	}

	public void setBelieverLocation(String believerLocation) {
		this.believerLocation = believerLocation;
	}

	public char[] getProfileNumber() {
		return profileNumber;
	}

	public void setProfileNumber(char[] profileNumber) {
		this.profileNumber = profileNumber;
	}

}

