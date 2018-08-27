package com.telega.eatter.Utils;

import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.eatter.entity.GameResults;
import com.telega.eatter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotUtils {

    public static final String SUBSCRIBE_MESSAGE = "Subscribe on games";
    public static final String START_GAME_MESSAGE = "/понеслась";
    public static final String ANSWER_MESSAGE = "/ответ";
    public static final String FINISH_GAME_MESSAGE = "/цена";
    public static final String HELP_MESSAGE = "/правила";

    private GameService gameService;

    private GameStatusInfo gameStatus;

    @Autowired
    public BotUtils(GameService gameService, GameStatusInfo gameStatus) {
        this.gameService = gameService;
        this.gameStatus = gameStatus;
    }

    public synchronized void setDefaultButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(SUBSCRIBE_MESSAGE));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(START_GAME_MESSAGE));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public synchronized void setGameOwnerButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Stop Game"));

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public String processActiveGameByOwner(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "ERROR");
        String message = "ERROR";
        while (!gameStatus.isGameIsReady()) {
            setGameOwnerButtons(sendMessage);
            if(!gameStatus.isPhotoSent() && update.getMessage().getPhoto() == null) {
            //if (gameStatus.getDocument() == null && update.getMessage().getDocument() == null) {
                message = "Send me picture";
                return message;
            } else if (!gameStatus.isPhotoSent() && update.getMessage().getPhoto() != null) {
                //gameStatus.setDocument(update.getMessage().getDocument());
                gameStatus.setPhotoSent(true);
            } else if (gameStatus.isPhotoSent()) {
                message = "Game has started, Пидор";
                gameStatus.setGameIsReady(true);
                return message;
            }
        }

        return message;
    }

    public String processStartGame(Update update) {
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
        message += processActiveGameByOwner(update);
        return message;
    }

    public String processAnswers(Update update) {
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
        return "error from BotUils.processAnswers()";
    }

    public String processFinish(Update update) {
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
