package com.telega.eatter.Utils;

import com.telega.eatter.exceptions.AlreadySubscribedException;
import com.telega.eatter.service.GameService;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotUtils {

    public static final String SUBSCRIBE_MESSAGE = "Subscribe on games";
    public static final String START_GAME_MESSAGE = "Start game";

    @Autowired
    private GameService gameService;

    public SendMessage getSubsribttionMessage(Long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, "");
        setDefaultButtons(sendMessage);
        try {
            gameService.subscribeNewUser(chatId);
        } catch (AlreadySubscribedException ex) {
            sendMessage.setText("U have already subscribed");
            return sendMessage;
        }
        sendMessage.setText("Done!");
        return sendMessage;
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
}
