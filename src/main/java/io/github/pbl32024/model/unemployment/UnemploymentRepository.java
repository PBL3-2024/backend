package io.github.pbl32024.model.unemployment;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnemploymentRepository extends CrudRepository<Unemployment, String> {

    List<Unemployment> findBySocCodeStartsWith(String prefix);

}
