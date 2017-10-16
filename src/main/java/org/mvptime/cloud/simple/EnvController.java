package org.mvptime.cloud.simple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EnvController {
	private static final Logger LOG = LoggerFactory.getLogger(EnvController.class);

	@Autowired
	private ApplicationConfig appConfig;

	@RequestMapping(value = { "/", "/info" }, produces = { "text/html" })
	public String index(Model model) {
		try {
			final ApplicationInstanceInfo appInfo = appConfig.cloud().getApplicationInstanceInfo();
			LOG.info("App properties : {}", appInfo);
			model.addAttribute("cfapp", appInfo.getProperties());

			final Map<String, List<Object>> cfservices = appConfig.getServiceProps();
			LOG.info("Service Props : {}", cfservices);
			model.addAttribute("cfservices", cfservices);

			final String cfservicename = cfservices.keySet().iterator().next();
			final Object cfservice = cfservices.get(cfservicename).get(0);
			model.addAttribute("cfservicename", cfservicename);
			model.addAttribute("cfservice", cfservice);
		} catch (Exception ex) {
			// No services
			model.addAttribute("cfservicename", "");
			model.addAttribute("cfservice", new LinkedHashMap<>());
		}
		return "index";
	}

	@RequestMapping(value = { "/environment", "/info/environment" }, method = RequestMethod.GET)
	public String env(Model model) throws Exception {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd'T'hh:mm:ss.S Z");
		final String serverTime = dateFormat.format(new Date());
		model.addAttribute("serverTime", serverTime);
		model.addAttribute("extIpPort", appConfig.getIPPort());

		final int port = appConfig.getPort();
		model.addAttribute("port", port);

		final Map<String, Object> vcapMap = appConfig.getVCAPAppMap();
		model.addAttribute("vcapApplication", vcapMap);
		model.addAttribute("vcapAppInfo", appConfig.getApplication());
		model.addAttribute("vcapServices", appConfig.getServices());
		model.addAttribute("serviceInfos", appConfig.getCFServices());

		LOG.info("Current date and time = [{}], port = [{}].", serverTime, port);

		return "environment";
	}
}
