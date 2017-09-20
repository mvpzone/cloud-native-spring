package org.mvptime.cloud.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.CloudScan;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ServiceScan
@CloudScan
public class ApplicationConfig extends AbstractCloudConfig {
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

	@SuppressWarnings("rawtypes")
	private static final Map EMPTY_MAP = new LinkedHashMap<>();

	@Value("${VCAP_APPLICATION:{}}")
	private String application;

	@Value("${VCAP_SERVICES:{}}")
	private String services;

	@Value("${CF_INSTANCE_ADDR:0.0.0.0:0}")
	private String extIpPort;

	@Value("${PORT:0}")
	private int port;

	@Value("${greeting:Hola}")
	private String _greeting;

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

	public String getIPPort() {
		return this.extIpPort;
	}

	public int getPort() {
		return this.port;
	}

	public String getGreeting() {
		return this._greeting;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getVCAPAppMap() {
		try {
			return mapper().readValue(application, LinkedHashMap.class);
		} catch (IOException e) {
			LOG.error("Error parsing app props", e);
			return EMPTY_MAP;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> getServiceProps() {
		try {
			return mapper().readValue(getServices(), LinkedHashMap.class);
		} catch (IOException e) {
			LOG.error("Error parsing service props", e);
			return EMPTY_MAP;
		}
	}

	public List<String> getCFServices() {
		if (Objects.isNull(cloud())) {
			return Collections.emptyList();
		}

		if (Objects.isNull(cloud().getServiceInfos())) {
			return Collections.emptyList();
		}

		final List<ServiceInfo> serviceInfos = cloud().getServiceInfos();
		final List<String> services = new ArrayList<String>(serviceInfos.size());
		for (final ServiceInfo si : serviceInfos) {
			final String sName = si.getClass().getSimpleName();
			if (sName.indexOf("ServiceInfo") != -1) {
				services.add(si.getId() + " / " + sName.substring(0, sName.indexOf("ServiceInfo")));
			}
		}

		return services;
	}

	public ObjectMapper mapper() {
		return this.json;
	}
}
