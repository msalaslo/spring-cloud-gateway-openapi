package com.msl.gatewaydoc;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCloudGatewayOpenapiApplication {
     
	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGatewayOpenapiApplication.class, args);
	}

	@Autowired
	RouteDefinitionLocator locator;
	
	private static final String SERVICE_TO_REGISTER_IN_SWAGGER_SUFIX = "-service";

	@Bean
	public List<GroupedOpenApi> apis() {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*" + SERVICE_TO_REGISTER_IN_SWAGGER_SUFIX + "")).forEach(routeDefinition -> {
			String name = routeDefinition.getId().replaceAll(SERVICE_TO_REGISTER_IN_SWAGGER_SUFIX, "");
			groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").setGroup(name).build());
		});
		return groups;
	}
	
}
