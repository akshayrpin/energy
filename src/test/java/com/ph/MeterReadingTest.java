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

import com.ph.model.MeterReading;
import com.ph.model.Month;
import com.ph.model.vo.Status;
import com.ph.repository.MeterReadingRepository;
import com.ph.service.MeterReadingParse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeterReadingTest {

	final RestTemplate template = new RestTemplate();

	@Autowired
	private MeterReadingRepository meterReadingRepository;

	@Autowired
	private MeterReadingParse meterReadingParse;

	@Before
	public void beforeEachTest() {
		meterReadingRepository.deleteAll();
	}
	
	@Test
	public void getAllFractions() {
		Object obj = template.getForEntity("http://localhost:9090/api/meterreadings", Object.class);
		ResponseEntity<List<MeterReading>> readingResponse = (ResponseEntity<List<MeterReading>>) obj;
		HttpStatus getAllStatusCode = readingResponse.getStatusCode();
		Assert.assertEquals(HttpStatus.OK, getAllStatusCode);
	}
	
	@Test
	public void meterSuccessTest() {

		List<List<String>> meter = new ArrayList<List<String>>();
		int reading = -12;
		for (Month month : Month.values()) {
			List<String> values = Arrays.asList("1","A", month.toString(), (month.toString().equals("JAN") ? "0"
					: (month.toString().equals("FEB") ? "3" : reading+"")));
			meter.add(values);
			reading += 12;
		}

		List<Status> readingResponse = meterReadingParse.uploadMeterReadings(meter);
		Assert.assertEquals("A", readingResponse.get(0).getProfile());
		Assert.assertEquals("SUCCESS", readingResponse.get(0).getStatus());
	}
	
	@Test
	public void meterFailureTest() {

		List<List<String>> meter = new ArrayList<List<String>>();
		int reading = -12;
		for (Month month : Month.values()) {
			List<String> values = Arrays.asList("2","B", month.toString(), reading+"");
			meter.add(values);
			reading += 12;
		}

		List<Status> readingResponse = meterReadingParse.uploadMeterReadings(meter);
		Assert.assertEquals("B", readingResponse.get(0).getProfile());
		Assert.assertEquals(" - ERROR, Profile does not exist", readingResponse.get(0).getStatus());
	}
}
