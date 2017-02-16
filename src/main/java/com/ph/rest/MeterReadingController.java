package com.ph.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ph.model.MeterReading;
import com.ph.model.Month;
import com.ph.model.vo.Status;
import com.ph.repository.MeterReadingRepository;
import com.ph.service.MeterReadingParse;

/**
 * @author Akshay
 *
 */
@RestController
@RequestMapping(value = "/api")
public class MeterReadingController {

	@Autowired
	MeterReadingRepository meterReadingRepository;

	@Autowired
	MeterReadingParse meterReadingParse;

	@Value("${filepath.meterreading}")
	Resource file;

	private static final Logger log = LoggerFactory.getLogger(MeterReadingController.class);

	/**
	 * @return ResponseEntity with status 200 and the data Meter Reading
	 *         created, or with list error status if the fraction validation
	 *         fails
	 */
	@RequestMapping(value = "/uploadMeter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Status>> importMeterReadings() {
		List<List<String>> data;
		List<Status> readings = new ArrayList<Status>();
		try {
			data = meterReadingParse.parseCsvData(file);
			readings = meterReadingParse.uploadMeterReadings(data);
		} catch (IOException e) {
			log.error("ERROR!!! File Not Found");
			readings.add(new Status("", "ERROR!!! File Not Found"));
		}
		return new ResponseEntity<List<Status>>(readings, HttpStatus.OK);
	}

	/**
	 * @return ResponseEntity with status 200 and the data Meter Reading fetched
	 */
	@RequestMapping(value = "/meterreadings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MeterReading>> getAllMeterReadings() {
		List<MeterReading> reading = meterReadingRepository.findAll();
		return Optional.ofNullable(reading)
				.map(result -> new ResponseEntity<List<MeterReading>>(
                result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * @param profile Accepts profile name as an input
	 * @return ResponseEntity with status 200 and the data Meter Reading for a
	 *         given profile
	 */
	@RequestMapping(value = "/meterreadings/{profile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MeterReading>> getByMeterReadings(@PathVariable String profile) {
		List<MeterReading> reading = meterReadingRepository.findByProfile(profile);
		return Optional.ofNullable(reading)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * @return ResponseEntity with status 200
	 */
	@RequestMapping(value = "/meterreadings", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> deleteProfile() {
		meterReadingRepository.deleteAll();
		return new ResponseEntity<>(new Status("","Profile Deleted."), HttpStatus.OK);
	}
	
	/**
	 * @param profile
	 * @param month
	 * @return ResponseEntity with status 200 and the  Meter Reading for a given profile and Month, 
	 * If data not found return 404
	 */
	@RequestMapping(value = "/meterreading/{profile}/{month}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MeterReading> getConsumptions(@PathVariable("profile") String profile, @PathVariable("month") Month month){
		MeterReading reading = meterReadingRepository.findByProfileAndMonth(profile, month);
		return Optional.ofNullable(reading)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>( new MeterReading(0,"Data Not Found", null, 0),HttpStatus.NOT_FOUND));
	}
}
