package com.telega.processors.impl;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.processors.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ActiveGameByOwnerProcessor implements MessageProcessor {

    private final GameStatusInfo gameStatus;

    @Autowired
    public ActiveGameByOwnerProcessor(GameStatusInfo gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override
    public String process(Update update) {
        String message = "ERROR";
        while (!gameStatus.isGameIsReady()) {
            if(!gameStatus.isPhotoSent() && update.getMessage().getPhoto() == null) {
                message = "Send me picture";
                return message;
            } else if (!gameStatus.isPhotoSent() && update.getMessage().getPhoto() != null) {
                gameStatus.setPhotoSent(true);
            } else if (gameStatus.isPhotoSent()) {
                message = "Game has started, Пидор";
                gameStatus.setGameIsReady(true);
                return message;
            }
        }

        return message;
    }
}
