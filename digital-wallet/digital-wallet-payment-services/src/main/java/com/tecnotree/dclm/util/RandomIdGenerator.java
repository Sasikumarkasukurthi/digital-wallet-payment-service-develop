package com.tecnotree.dclm.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Copyright (c) 2020 Tecnovos to Present. All rights reserved
 *
 * @author Roja (roja.r@tecnovos.com). Controller class: RandomIdGenerator.java
 *         Created date : 11/10/2020. Update date :
 *
 */
public class RandomIdGenerator {
	public static String getSaltString() {

		/*
		 * String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; StringBuilder salt
		 * = new StringBuilder(); Random rnd = new Random(); while (salt.length() < 8) {
		 * // length of the random string. int index = (int) (rnd.nextFloat() *
		 * SALTCHARS.length()); salt.append(SALTCHARS.charAt(index)); } String saltStr =
		 * salt.toString(); return saltStr;
		 */
		
		 String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	        StringBuilder salt = new StringBuilder();
	        float rnd = new SecureRandom().nextFloat();
	        while (salt.length() < 8) { 
	        	// length of the random string.
	            int index = (int) (rnd * SALTCHARS.length()); //NOSONAR
	            salt.append(SALTCHARS.charAt(index));
	        }
	        String saltStr = salt.toString();
	        return saltStr;

	}

}
