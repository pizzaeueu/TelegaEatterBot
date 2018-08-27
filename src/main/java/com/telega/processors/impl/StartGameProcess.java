package com.telega.processors.impl;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.processors.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartGameProcess implements MessageProcessor {

    private final GameStatusInfo gameStatus;
    private MessageProcessor gameByOwnerMessageProcessor;

    @Autowired
    public StartGameProcess(GameStatusInfo gameStatus) {
        this.gameStatus = gameStatus;
        gameByOwnerMessageProcessor = new ActiveGameByOwnerProcessor(gameStatus);
    }


    @Override
    public String process(Update update) {
        String username = update.getMessage().getFrom().getFirstName();
        Integer userId = update.getMessage().getFrom().getId();
        String message;
        if (gameStatus.isActiveGame()) {
            return "Game has already started";
        }
        gameStatus.setActiveGame(true);
        gameStatus.setGameOwnerId(userId);
        message = "Игру начал " + username;
        message += "\n";
        message += gameByOwnerMessageProcessor.process(update);
        return message;
    }
}
