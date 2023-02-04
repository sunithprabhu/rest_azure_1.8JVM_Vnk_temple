package prayerservicecopy.temple.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prayerservicecopy.temple.model.Believers;

public interface BelieverRepository extends JpaRepository<Believers, Long>{
	
	Believers findByProfileNumber(char[] profileNumber);
}

