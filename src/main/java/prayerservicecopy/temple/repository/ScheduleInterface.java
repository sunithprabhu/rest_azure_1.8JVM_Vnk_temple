package prayerservicecopy.temple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prayerservicecopy.temple.model.Schedule;

@Repository
public interface ScheduleInterface extends JpaRepository<Schedule, String>{
	
	Schedule findBydayName(String dayName);

}
