package org.mvptime.cloud.simple;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.CloudScan;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ServiceScan
@CloudScan
public class EnvConfig extends AbstractCloudConfig {
	private static final Logger LOG = LoggerFactory.getLogger(EnvConfig.class);

	@Value("${VCAP_APPLICATION:{}}")
	private String application;

	@Value("${VCAP_SERVICES:{}}")
	private String services;

	@Autowired
	private ObjectMapper json;

	@Bean
	public Properties cloudProperties() {
		return super.properties();
	}

	public String getApplication() {
		return this.application;
	}

	public String getServices() {
		return this.services;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> getServiceProps() {
		try {
			return mapper().readValue(getServices(), LinkedHashMap.class);
		} catch (IOException e) {
			LOG.error("Error parsing service props", e);
			return new LinkedHashMap<>();
		}
	}

	public ObjectMapper mapper() {
		return this.json;
	}
}
