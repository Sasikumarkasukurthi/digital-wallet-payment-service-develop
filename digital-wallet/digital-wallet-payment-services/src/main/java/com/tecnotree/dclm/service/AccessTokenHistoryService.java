package com.tecnotree.dclm.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tecnotree.dclm.model.AccessTokenHistory;
import com.tecnotree.dclm.util.RandomIdGenerator;

@Component
public class AccessTokenHistoryService {
private static final Logger LOG = LogManager.getLogger(AccessTokenHistoryService.class);

	
	protected RandomIdGenerator queryUtil;


	/*
	 * public Optional<AccessTokenHistory> create(AccessTokenHistory
	 * accessTokenHistory) {
	 * 
	 * AccessTokenHistory paymentconformation = repo.insertOne(accessTokenHistory,
	 * AccessTokenHistory.class); return paymentconformation == null ?
	 * Optional.ofNullable(paymentconformation) :
	 * Optional.ofNullable((paymentconformation)); }
	 */


	/*
	 * public Optional<AccessTokenHistory> save(AccessTokenHistory
	 * accessTokenHistory) { // TODO Auto-generated method stub return null; }
	 */

}
