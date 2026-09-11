package br.com.devpree.shorturl.filter;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;

@ApplicationScoped
public class ApiKeyFilter {

	@ConfigProperty(name = "API_KEY")
	String apiKey;
	
    @ServerRequestFilter(preMatching = true)
	public void filterApiKey(ContainerRequestContext containerRequestContext) {
         String apiKeyHeader = containerRequestContext.getHeaderString("Api-Key");
         
         if (!apiKey.equals(apiKeyHeader)) {
             throw new UnauthorizedException();
         }
	}
}
