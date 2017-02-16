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

import com.ph.model.Fraction;
import com.ph.model.vo.Status;
import com.ph.repository.FractionRepository;
import com.ph.service.FractionParse;

/**
 * @author Akshay
 *
 */
@RestController
@RequestMapping(value = "/api")
public class FractionsController {

	@Autowired
	private FractionRepository fractionRepository;

	@Autowired
	FractionParse fractionParse;

	@Value("${filepath.fraction}")
	Resource file;

	public static final Logger log = LoggerFactory.getLogger(FractionsController.class);

	/**
	 * @return ResponseEntity with status 200 and the data Fractions created, or
	 *         with list error status if the fraction validation fails
	 */
	@RequestMapping(value = "/uploadFractions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Status>> importFractions() {

		List<List<String>> data;
		List<Status> readings = new ArrayList<Status>();
		try {
			data = fractionParse.parseCsvData(file);
			readings = fractionParse.uploadFractions(data);
		} catch (IOException e) {
			log.error("ERROR!!! File Not Found");
			readings.add(new Status("", "ERROR!!! File Not Found"));
		}

		return new ResponseEntity<List<Status>>(readings, HttpStatus.OK);
	}

	/**
	 * @return ResponseEntity with status 200 and the data Fractions fetched
	 */
	@RequestMapping(value = "/fractions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Fraction>> getAllFraction() {
		List<Fraction> fractions = fractionRepository.findAll();		
		return Optional.ofNullable(fractions)
				.map(fraction -> new ResponseEntity<List<Fraction>>(fraction, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * @param profile Accepts profile name as an input
	 * @return ResponseEntity with status 200 and the data Fractions for a given
	 *         profile
	 */
	@RequestMapping(value = "/fraction/{profile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Fraction>> getProfile(@PathVariable String profile) {
		List<Fraction> fractions = fractionRepository.findByProfile(profile);
		return Optional.ofNullable(fractions)
				.map(fraction -> new ResponseEntity<List<Fraction>>(fraction, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * @return ResponseEntity with status 200
	 */
	@RequestMapping(value = "/fractions", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> deleteProfile() {
		fractionRepository.deleteAll();
		return new ResponseEntity<>(new Status("","Profile Deleted."), HttpStatus.OK);
	}
}
