package com.telega.eatter.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

@Component
@Data
public final class GameStatusInfo {
    private volatile boolean isActiveGame = false;
    private Integer gameOwnerId = null;
    private Document document = null;
    private Double price = -2.0;
    private boolean gameIsReady = false;
    private boolean isPhotoSent;

    public void flush() {
        isActiveGame = false;
        gameOwnerId = null;
        document = null;
        price = -2.0;
        gameIsReady = false;
    }
}
