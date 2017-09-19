package org.mvptime.cloud.simple;

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

@Controller
public class EnvController {
	private static final Logger LOG = LoggerFactory.getLogger(EnvController.class);

	@Autowired
	private EnvConfig envConfig;

	@RequestMapping(value = { "/" }, produces = { "text/html" })
	public String index(Model model) {
		try {
			final ApplicationInstanceInfo appInfo = envConfig.cloud().getApplicationInstanceInfo();
			LOG.info("App properties : {}", appInfo);
			model.addAttribute("cfapp", appInfo.getProperties());

			final Map<String, List<Object>> cfservices = envConfig.getServiceProps();
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
}
