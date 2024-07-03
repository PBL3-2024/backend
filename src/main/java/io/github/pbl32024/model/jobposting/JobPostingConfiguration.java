package io.github.pbl32024.model.jobposting;


import io.github.pbl32024.model.jobposting.usajobs.USAJobs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class JobPostingConfiguration {

    @Bean
    public USAJobs usaJobs(@Value("${backend.usajobs.api.url}") String baseUrl,
                           @Value("${backend.usajobs.api.key}") String apiKey,
                           @Value("${backend.usajobs.api.email}") String email) {
        RestClient restClient = RestClient.builder()
                .defaultHeader("Authorization-Key", apiKey)
                .defaultHeader("User-Agent", email)
                .baseUrl(baseUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(USAJobs.class);
    }

}
