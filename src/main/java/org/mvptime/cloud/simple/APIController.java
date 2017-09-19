package org.mvptime.cloud.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class APIController {
	private static final Logger LOG = LoggerFactory.getLogger(APIController.class);

	@Autowired
	private ApplicationConfig appConfig;

	@RequestMapping("/hello")
	public String hello() {
		final ObjectMapper mapper = appConfig.mapper();
		final ObjectNode jsonObj = mapper.createObjectNode().put("greeting", appConfig.getGreeting() + " World!");
		try {
			return mapper.writeValueAsString(jsonObj);
		} catch (JsonProcessingException e) {
			LOG.error("Json generation error", e);
			return "{error: \"" + e.getMessage() + "\"}";
		}
	}
}
