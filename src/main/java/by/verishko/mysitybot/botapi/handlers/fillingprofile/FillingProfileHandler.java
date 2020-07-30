package by.verishko.mysitybot.botapi.handlers.fillingprofile;

import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.InputMessageHandler;
import by.verishko.mysitybot.cache.UserDataCache;
import by.verishko.mysitybot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


/**
 * Формирует данные о городе.
 */

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAMECITY);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        City profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAMECITY)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askCity");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DESCRIPTION);
        }

        if (botState.equals(BotState.ASK_DESCRIPTION)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askDescription");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setDescription(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DESTINY);
            replyToUser = new SendMessage(chatId, String.format("%s %s", "Данные по вашему добавлению", profileData));
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }


}



