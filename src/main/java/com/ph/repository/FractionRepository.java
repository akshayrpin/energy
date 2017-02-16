package com.ph.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ph.model.Fraction;

/**
 * @author Akshay
 *
 */
public interface FractionRepository extends MongoRepository<Fraction, String> {
	 
	 List<Fraction> findByProfile(String profile);
	 List<Fraction> deleteByProfile(String profile);

}
