package com.ph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ph.model.Fraction;
import com.ph.model.Month;
import com.ph.model.vo.Status;
import com.ph.repository.FractionRepository;
import com.ph.service.FractionParse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FractionTest {

	final RestTemplate template = new RestTemplate();

	@Autowired
	private FractionRepository fractionRepository;

	@Autowired
	private FractionParse fractionParse;

	@Before
	public void beforeEachTest() {
		fractionRepository.deleteAll();
	}

	@Test
	public void getAllFractions() {
		Object obj = template.getForEntity("http://localhost:9090/api/fractions", Object.class);
		ResponseEntity<List<Fraction>> readingResponse = (ResponseEntity<List<Fraction>>) obj;
		HttpStatus getAllStatusCode = readingResponse.getStatusCode();
		Assert.assertEquals(HttpStatus.OK, getAllStatusCode);
	}

	@Test
	public void fractionSuccessTest() {

		List<List<String>> fractions = new ArrayList<List<String>>();
		for (Month month : Month.values()) {
			List<String> values = Arrays.asList("A", month.toString(), (month.toString().equals("JAN") ? "0"
					: (month.toString().equals("FEB") ? "0.03" : (month.toString().equals("MAR") ? "0.07" : "0.1"))));
			fractions.add(values);
		}

		List<Status> readingResponse = fractionParse.uploadFractions(fractions);
		Assert.assertEquals("A", readingResponse.get(0).getProfile());
		Assert.assertEquals("SUCCESS", readingResponse.get(0).getStatus());
	}

	@Test
	public void fractionFailureTest() {
		List<List<String>> fractions = new ArrayList<List<String>>();
		for (Month month : Month.values()) {
			List<String> values = Arrays.asList("B", month.toString(), "0.1");
			fractions.add(values);
		}

		List<Status> readingResponse = fractionParse.uploadFractions(fractions);
		Assert.assertEquals("B", readingResponse.get(0).getProfile());
		Assert.assertEquals("ERROR Total fraction should not exceed more than 1", readingResponse.get(0).getStatus());
	}

}
