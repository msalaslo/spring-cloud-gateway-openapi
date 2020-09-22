package com.msl.gatewaydoc;

import org.springdoc.core.customizers.OpenApiCustomiser;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayApi extends SpecFilter implements OpenApiCustomiser {
    
    private final String contextPath;
    private final String version;
    
    public GatewayApi(String contextPath, String version) {
        this.contextPath = contextPath;
        this.version = version;
    }
    
    @Override
    public void customise(OpenAPI openApi) {
        Paths versionedPaths = new Paths();
        
        openApi.getPaths().entrySet().stream().filter(path -> path.getKey().startsWith(version)).forEach(path -> {
        	String key = path.getKey().substring(version.length());
        	String apiUrl = contextPath.concat(version);
        	LOGGER.info("Group API Key:" + key + ", url:" + apiUrl);
            versionedPaths.put(path.getKey().substring(version.length()),
                    path.getValue().addServersItem(new Server().url(apiUrl)));
        });
        
        openApi.setOpenapi("3.0.0");
        openApi.setPaths(versionedPaths);
        
        openApi.setInfo(new Info()
                .title("Gateway Application REST API")
                .description("Gateway Application manager REST API documentation.")
                .version(version)
                .contact(new Contact().email("faas@securitasdirect.es").name("FaaS Securitas Direct").url("https://faas.securitasdirect.local/"))
                .license(new License().name("License of API for YourCompany use only").url("License of API for YourCompany use only"))
        );
        
        super.removeBrokenReferenceDefinitions(openApi);
    }
    
}
