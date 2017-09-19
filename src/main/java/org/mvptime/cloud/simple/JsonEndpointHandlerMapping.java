package org.mvptime.cloud.simple;

import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.endpoint.mvc.ActuatorMediaTypes;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMappingCustomizer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public final class JsonEndpointHandlerMapping implements EndpointHandlerMappingCustomizer {

	static class JsonFix extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
				final Object handler) throws Exception {
			final Object attribute = request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
			if (attribute instanceof LinkedHashSet) {
				@SuppressWarnings("unchecked")
				LinkedHashSet<MediaType> lhs = (LinkedHashSet<MediaType>) attribute;
				if (lhs.remove(ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON)) {
					lhs.add(ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON);
				}
			}
			return true;
		}

	}

	@Override
	public void customize(final EndpointHandlerMapping mapping) {
		mapping.setInterceptors(new Object[] { new JsonFix() });
	}
}
