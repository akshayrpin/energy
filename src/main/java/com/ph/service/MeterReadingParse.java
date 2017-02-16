package com.ph.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ph.model.Fraction;
import com.ph.model.MeterReading;
import com.ph.model.Month;
import com.ph.model.vo.Status;
import com.ph.repository.FractionRepository;
import com.ph.repository.MeterReadingRepository;

/**
 * @author Akshay
 *
 */
@Service
public class MeterReadingParse extends ParseCsv {

	@Autowired
	private FractionRepository fractionRepository;

	@Autowired
	private MeterReadingRepository meterReadingRepository;

	public static final Logger log = LoggerFactory.getLogger(MeterReadingParse.class);

	/**
	 * @param records
	 *            List of data which was read from csv file
	 * @return List of error status if encountered while processing the data
	 *         else SUCCESS
	 */
	public List<Status> uploadMeterReadings(List<List<String>> records) {
		List<Status> res = new ArrayList<Status>();
		Map<String, List<MeterReading>> record = records.stream()
				.map(reading -> new MeterReading(Long.parseLong(reading.get(0)), reading.get(1),
						Month.valueOf(reading.get(2)), Integer.parseInt(reading.get(3))))
				.collect(Collectors.groupingBy(MeterReading::getProfile, Collectors.toList()));

		record.forEach((profile, entry) -> {

			// Sorting the data based on Month
			List<MeterReading> store = entry.stream().sorted(MeterReading.MONTH_COMPARATOR)
					.collect(Collectors.toList());
			/*MeterReading preMeter = store.get(0);
			for(int i=1; i<= store.size()-1; i++){
				MeterReading meter = store.get(i);
				curReading = meter.getMeterReading();
				
				preMeter = 
			}*/

			// Validating to check if profile exists
			List<Fraction> fractions = fractionRepository.findByProfile(profile);
			if (fractions.isEmpty()) {
				log.error(profile + " - ERROR, Profile does not exist");
				res.add(new Status(profile, " - ERROR, Profile does not exist"));
				return;
			}

			// Validating if all the records exist or not
			if (entry.size() != 12) {
				log.error(profile + " - ERROR, Data does not exist for all the months");
				res.add(new Status(profile, " - ERROR, Data does not exist for all the months"));
				return;
			}

			// Fetching fraction values to calculate the tolerance of 25%
			List<Float> fraction = fractions.stream().sorted(Fraction.MONTH_COMPARATOR)
					.map(Fraction::getValue).collect(Collectors.toList());

			// Calculating the Consumption and checking if the value is in the
			// range of accepted values, consistent with the fraction with a tolerance of a 25%. 
			int preReading = 0;
			int yearConsumption = store.get(store.size() - 1).getMeterReading();
			boolean isValid = true;
			for (int i = 0; i < store.size(); i++) {
				int curReading = store.get(i).getMeterReading();
				
				if(preReading > curReading){
					isValid = false;
					log.error(profile + " - ERROR, Current month meter reading should always be greater than Previous month : " + store.get(i).getMonth());
					res.add(new Status(profile, " - ERROR, Current month meter reading should always be greater than Previous month : " + store.get(i).getMonth()));
					break;
				}
				
				int consumption = curReading - preReading;
				double fract = fraction.get(i);
				long percent = (long) ((fract * yearConsumption) * 0.25);
				long actual = (long) (fract * yearConsumption);
				long consumptionFrom = actual - percent;
				long consumptionTo = actual + percent;

				if (consumption < consumptionFrom || consumption > consumptionTo) {
					res.add(new Status(profile, String.format(" - consumption: %d, Allowed Consumption: %d to %d : for Month %s ",
							consumption, consumptionFrom, consumptionTo, store.get(i).getMonth())));
					log.debug(profile + String.format(" - consumption: %d, Allowed Consumption: %d to %d : for Month %s  ", 
							consumption, consumptionFrom, consumptionTo, store.get(i).getMonth()));
					isValid = false;
					break;
				}

				preReading = curReading;
				store.get(i).setConsumption(consumption);
			}
			if (isValid) {
				meterReadingRepository.save(store);
				log.debug(profile + " Meter Reading file Processed and save data Successfully");
				res.add(new Status(profile, "SUCCESS"));
			}
		});

		return res;
	}
}
