package io.github.pbl32024.model.news;

import lombok.Data;

import java.util.List;

@Data
public class RSSChannel {
    private List<RSSArticle> item;
}
