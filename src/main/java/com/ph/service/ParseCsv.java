package com.ph.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;

/**
 * @author Akshay
 *
 */
public abstract class ParseCsv {
	
	private static final String SEPARATOR = ",";
	
	/**
	 * @param resource CSV file store in resource root folder.
	 * @return a List of Data read from csv file. 
	 * @throws IOException
	 */
	public List<List<String>> parseCsvData(Resource resource) throws IOException{
		List<List<String>> inputList = new ArrayList<List<String>>();
		InputStream is = resource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		inputList = reader.lines()
				.map(line -> Arrays.asList(line.split(SEPARATOR)))
				.collect(Collectors.toList());
		
		return inputList;
	}

}
