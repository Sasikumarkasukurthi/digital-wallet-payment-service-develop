package com.tecnotree.dclm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class ApplicationProperties {

	
	
	@Value("${CURRENCY}")
	private String CURRENCY;

	@Value("${STRIPE_SECRET_KEY}")
	private String STRIPE_SECRET_KEY;

	@Value("${protocol}")
	private String protocol;

	@Value("${hostidentifier}")
	private String hostidentifier;
	@Value("${schemaFile}")
	private String schemaFile;

	@Value("${servicename}")
	private String serviceName;

	public String getProtocol() {
		return protocol;
	}

	public String getHostidentifier() {
		return hostidentifier;
	}

	public String getSchemaFile() {
		return schemaFile;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getCURRENCY() {
		return CURRENCY;
	}

	public String getSTRIPE_SECRET_KEY() {
		return STRIPE_SECRET_KEY;
	}	

	
}
