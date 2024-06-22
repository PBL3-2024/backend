package io.github.pbl32024.model.news;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Configuration
public class NewsConfiguration {

    @Bean
    public RSSWebClient rssWebClient() {
        RestClient restClient = RestClient.builder().messageConverters(c -> c.add(new Jaxb2RootElementHttpMessageConverter())).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(RSSWebClient.class);
    }

}
