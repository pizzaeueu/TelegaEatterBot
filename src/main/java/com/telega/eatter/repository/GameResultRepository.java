package com.telega.eatter.repository;

import com.telega.eatter.entity.GameResults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GameResultRepository extends CrudRepository<GameResults, Long> {

    List<GameResults> findBySubscriberId(String subscriber);
}
