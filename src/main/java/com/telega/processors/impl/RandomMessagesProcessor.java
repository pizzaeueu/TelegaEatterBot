package com.telega.processors.impl;

import com.telega.eatter.Utils.BotUtils;
import com.telega.processors.MessageProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RandomMessagesProcessor implements MessageProcessor {

    @Override
    public String process(Update update) {
        String message = update.getMessage().getText();
        if (message.equals("/Артем")) {
            return "Крест бля";
        } else if (message.equals("/ГруппаКрови")) {
            return "На руковееее";
        } else if (message.equals("/ООО")) {
            return "ПОШЛИ ВЫ ВСЕ НАХУЙ";
        } else if (message.equals(BotUtils.HELP_MESSAGE)) {
            String rules = "";
            rules += "Сообщение " + BotUtils.START_GAME_MESSAGE + " - начинает игру \n";
            rules += "Что бы внести свой ответ напиши - " + BotUtils.ANSWER_MESSAGE + " и цену(не забудь пробел) пример - " + BotUtils.ANSWER_MESSAGE + " 34.4 \n";
            rules += "Когда ведущий желает закончить игру - пишет " + BotUtils.FINISH_GAME_MESSAGE + " и цену, пример - " + BotUtils.FINISH_GAME_MESSAGE + " 25.5 \n";
            return rules;
        } else if (message.equals("/Саня")) {
            return "Ты в порядке?";
        } else if (message.equals("/OmaeWaMouShindeiru")) {
            return "NANIIII????!!!!!";
        }
        return "Да проще тебе ебало набить";
    }
}
