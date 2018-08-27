package com.telega.processors.impl;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.eatter.service.GameService;
import com.telega.processors.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FinishProcessor implements MessageProcessor {

    private final GameStatusInfo gameStatus;

    private final GameService gameService;

    @Autowired
    public FinishProcessor(GameStatusInfo gameStatus, GameService gameService) {
        this.gameStatus = gameStatus;
        this.gameService = gameService;
    }


    @Override
    public String process(Update update) {
        Integer userId = update.getMessage().getFrom().getId();
        String message = update.getMessage().getText();

        if (!gameStatus.isGameIsReady()) {
            return "Тупо нету игр";
        }
        if (!userId.equals(gameStatus.getGameOwnerId())) {
            return "Не тебе заканчивать игру, лох";
        }

        Double realPrice = null;
        String normalizedMessage = message.substring(5);
        try {
            realPrice = Double.valueOf(normalizedMessage);
        } catch (Exception e) {
            return "Введи нормальную цену";
        }

        String results = "Игра закончена, ответ - " + realPrice;
        results += "\n";
        results += "победил - " + gameService.calculateWinner(realPrice);
        gameService.flushGame();
        return results;
    }
}
