package io.github.pbl32024.model.occupation;


import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OccupationTrie {

	private final Trie<String, Occupation> trie = new PatriciaTrie<>();

	public Occupation getBySocCode(String socCode) {
		return trie.get(socCode);
	}

	public void addAll(Collection<Occupation> occupations) {
		trie.putAll(occupations.stream()
				.collect(
						Collectors.toMap(Occupation::getSocCode, Function.identity())));
	}

	public List<Occupation> getWithPrefix(String prefix) {
		return trie.prefixMap(prefix).values().stream().toList();
	}

	public List<Occupation> getAll() {
		return trie.values().stream().toList();
	}

}
