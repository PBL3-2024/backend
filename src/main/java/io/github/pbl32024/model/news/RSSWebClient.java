package io.github.pbl32024.model.news;

import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.net.URI;

@HttpExchange(accept = { MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE })
public interface RSSWebClient {

    @GetExchange
    RSSFeed getRSSFeed(URI uri);

}
