package io.github.pbl32024.model.jobposting.usajobs;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface USAJobs {
    @GetExchange(url = "/api/Search")
    SearchResult fetchJobPostings(@RequestParam(name = "Keyword") String keyword);
}
