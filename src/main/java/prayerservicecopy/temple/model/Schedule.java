package prayerservicecopy.temple.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduleVnkTemple")
public class Schedule {
	
	
	@Id
	private String dayName;
	
	private String scheduleDetails;
	
	private Date date;
	
	public Schedule() {
		
	}
	
	public Schedule(String dayName, String scheduleDetails, Date date) {
		super();
		this.dayName = dayName;
		this.scheduleDetails = scheduleDetails;
		this.date = date;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public String getScheduleDetails() {
		return scheduleDetails;
	}

	public void setScheduleDetails(String scheduleDetails) {
		this.scheduleDetails = scheduleDetails;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}	
	

}

