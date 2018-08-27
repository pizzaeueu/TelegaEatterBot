package com.telega.processors;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageProcessor {

    String ACTIVE_GAME_BY_OWNER_PROCESSOR_BEAN = "ActiveGameByOwnerProcessor";
    String FINISH_PROCESSOR_BEAN = "FinishProcessor";
    String ANSWER_PROCESSOR_BEAN = "AnswerProcessor";
    String RANDOME_MESSAGES_PROCESSOR_BEAN = "RandomMessagesProcessor";
    String START_GAME_PROCESSOR_BEAN = "StartGameProcessor";

    String process(Update update);
}
