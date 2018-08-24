package com.telega.eatter.service;

import com.telega.eatter.entity.Subscriber;
import com.telega.eatter.exceptions.AlreadySubscribedException;
import com.telega.eatter.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    public boolean subscribeNewUser(Long subscriberId) {
        if (subscriberRepository.findById(subscriberId).isPresent()) {
            throw new AlreadySubscribedException();
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setId(subscriberId);
        subscriberRepository.save(subscriber);
        return true;
    }
}
