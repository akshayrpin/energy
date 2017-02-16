package com.ph.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ph.model.MeterReading;
import com.ph.model.Month;

/**
 * @author Akshay
 *
 */
public interface MeterReadingRepository extends MongoRepository<MeterReading, String> {
	 
	 List<MeterReading> findByProfile(String profile);
	 List<MeterReading> deleteByProfile(String profile);
	 MeterReading findByProfileAndMonth(String profile, Month month);
}
