package com.tecnotree.dclm;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class Application {
	private static final Logger LOGGER = LogManager.getLogger(Application.class);
	private static final String VERSION = "v1";
	private static final Class<?>[] CLAZZ_LIST = {};
	@Autowired
	private ObjectMapper mapper;

	@Bean
	private static final CorsFilter corsFilter() {

		CorsConfiguration config = new CorsConfiguration();
		// config.setAllowCredentials(true); // you USUALLY want this
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@SuppressWarnings("squid:S4823")
	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
			//JsonSchemaUtility schemaUtility = context.getBean(JsonSchemaUtility.class);
			for (Class item : CLAZZ_LIST) {
			//	schemaUtility.saveJsonFile(item, VERSION);
			}
		} catch (RuntimeException  e) {
			LOGGER.error("Error ", e);

		}
		LOGGER.info(" Service is up !!");
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

}
