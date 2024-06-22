package io.github.pbl32024.model.news;


import lombok.Data;

import java.net.URI;

@Data
public class RSSSource {

	private String id;

	private URI url;

}
