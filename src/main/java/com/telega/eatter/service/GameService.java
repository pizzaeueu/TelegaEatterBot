package com.telega.eatter.service;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.eatter.entity.GameResults;
import com.telega.eatter.entity.Subscriber;
import com.telega.eatter.exceptions.AlreadySubscribedException;
import com.telega.eatter.repository.GameResultRepository;
import com.telega.eatter.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

@Component
public class GameService {

    private final SubscriberRepository subscriberRepository;
    private final GameResultRepository gameResultRepository;
    private final GameStatusInfo gameStatusInfo;

    @Autowired
    public GameService(SubscriberRepository subscriberRepository, GameResultRepository gameResultRepository, GameStatusInfo gameStatusInfo) {
        this.subscriberRepository = subscriberRepository;
        this.gameResultRepository = gameResultRepository;
        this.gameStatusInfo = gameStatusInfo;
    }

    public boolean subscribeNewUser(Long subscriberId) {
        if (subscriberRepository.findById(subscriberId).isPresent()) {
            throw new AlreadySubscribedException();
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setId(subscriberId);
        subscriberRepository.save(subscriber);
        return true;
    }

    public GameResults saveAnswer(GameResults result) {
        return gameResultRepository.save(result);
    }

    public boolean isAlreadyGiveAnswer(String username) {
        return !gameResultRepository.findBySubscriberId(username).isEmpty();
    }

    public String calculateWinner(Double realPrice) {
        final Double[] winnerAbs = {1000.0};
        AtomicReference<String> winner = new AtomicReference<>("NULL");

        StreamSupport.stream(gameResultRepository.findAll().spliterator(), false).forEach(
                result -> {
                    Double abs = Math.abs(realPrice - result.getAnswer());

                    if (abs < winnerAbs[0]) {
                        winnerAbs[0] = abs;
                        winner.set(result.getSubscriberId());
                    }
                }
        );

        return winner.get();

    }

    public void flushGame() {
        gameResultRepository.deleteAll();
        gameStatusInfo.flush();

    }
}
