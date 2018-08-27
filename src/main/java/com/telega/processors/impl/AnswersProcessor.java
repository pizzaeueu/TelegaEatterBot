package com.telega.processors.impl;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.eatter.entity.GameResults;
import com.telega.eatter.service.GameService;
import com.telega.processors.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AnswersProcessor implements MessageProcessor {

    private final GameStatusInfo gameStatus;

    private final GameService gameService;

    @Autowired
    public AnswersProcessor(GameStatusInfo gameStatus, GameService gameService) {
        this.gameStatus = gameStatus;
        this.gameService = gameService;
    }


    @Override
    public String process(Update update) {
        String message = update.getMessage().getText();
        String username = update.getMessage().getFrom().getUserName();
        Integer userId = update.getMessage().getFrom().getId();
        if(!gameStatus.isActiveGame()) {
            return "Игр тупо нету";
        }
        if (gameStatus.isActiveGame() && !gameStatus.isGameIsReady()) {
            return "Игра еще не стартовала";
        }
        if (gameStatus.isGameIsReady() && !userId.equals(gameStatus.getGameOwnerId())) {

            if(gameService.isAlreadyGiveAnswer(update.getMessage().getFrom().getUserName())) {
                return "Ты уже ответил";
            }

            String answer = message.substring(6);
            Double nAnswer = null;
            try {
                nAnswer = Double.valueOf(answer);
            } catch (Exception e) {
                return "Введи нормальное число";
            }

            GameResults result = new GameResults();
            result.setAnswer(nAnswer);
            result.setGameId(1L);
            result.setSubscriberId(username);

            gameService.saveAnswer(result);
            return username + " поставил цену в " + nAnswer;
        }
        return "не лезь бляь";
    }
}
