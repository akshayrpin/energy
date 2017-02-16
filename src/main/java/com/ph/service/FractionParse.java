package com.ph.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ph.model.Fraction;
import com.ph.model.Month;
import com.ph.model.vo.Status;
import com.ph.repository.FractionRepository;

/**
 * @author Akshay
 *
 */
@Service
public class FractionParse extends ParseCsv{

	@Autowired
	private FractionRepository fractionRepository;
	
	public static final Logger log = LoggerFactory.getLogger(FractionParse.class);
		
	//formatting to 2 decimal places.
	private DecimalFormat df2 = new DecimalFormat(".##"); 
	/**
	 * @param record List of data which was read from csv file
	 * @return List of error status if encountered while processing the data else SUCCESS 
	 */
	public List<Status> uploadFractions(List<List<String>> record){	
		List<Status> res = new ArrayList<Status>();
		Map<String, HashSet<Fraction>> grpFractions = record.stream()
				.map(reading -> new Fraction(reading.get(0), Month.valueOf(reading.get(1)), Float.valueOf(reading.get(2))))
				.collect(Collectors.groupingBy(Fraction::getProfile, Collectors.toCollection(HashSet::new)));
		
		for (String name : grpFractions.keySet()) {
			boolean isValid = true;
			List<Fraction> savedFraction = new ArrayList<>();
			Set<Fraction> fractions = grpFractions.get(name);

			double totalvalue = 0;
			for(Fraction fraction : fractions){
				totalvalue += fraction.getValue();
			}
			
			for (Fraction fraction : fractions) {
				savedFraction.add(fraction);
			}
			
			if(fractions.size() != 12){
				log.error(name + " - ERROR, Data does not exist for all the months");
				res.add(new Status(name, "ERROR, Data does not exist for all the months"));
				isValid = false;
			}
			if(!df2.format(totalvalue).equals("1.0")){
				log.error("ERROR Total fraction should not exceed more than 1");
				res.add(new Status(name, "ERROR Total fraction should not exceed more than 1"));
				isValid = false;
			}
			
			if(isValid){
				res.add(new Status(name, "SUCCESS"));
				log.debug(name + " Fraction file Processed and saved data Successfully");
				fractionRepository.save(savedFraction);				
			}
		}
		return res;
	}

}
