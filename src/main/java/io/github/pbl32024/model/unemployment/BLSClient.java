package io.github.pbl32024.model.unemployment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface BLSClient {

	@PostExchange(url = "/v1/timeseries/data/")
	BLSSeriesResponse fetchBLSSeries(@RequestBody BLSSeriesRequest query);

}
