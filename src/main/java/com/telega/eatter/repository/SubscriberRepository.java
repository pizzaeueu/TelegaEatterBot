package com.telega.eatter.repository;

import com.telega.eatter.entity.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {
}
