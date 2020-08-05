package by.verishko.mysitybot.botapi.handlers.fillingprofile;

import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.InputMessageHandler;
import by.verishko.mysitybot.cache.UserDataCache;
import by.verishko.mysitybot.service.CityService;
import by.verishko.mysitybot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Формирует данные о городе.
 */

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private CityService cityService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
/*        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARСH_PROFILE)){
            Long chatId = message.getChatId();
            String city = message.getText();
            List<City> citiesDB = cityService.getNameInList(city);
            if (citiesDB.size() > 1) {
                return messagesService.getReplyMessage(chatId, specifyRequest(citiesDB));
            }
            else if (citiesDB.size() == 1) {
                return getInlineKeyboard(chatId, googlePlayGames.get(0));
            }
            else {
                log.error("Game {} doesn't exist in library", title);
                return replyMessageService.getTextMessage(chatId, "Такой игры в библиотеке нет!");
            }
        }*/

        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAMECITY);
        }
        return processUsersInput(message);
    }

    private String specifyRequest(List<City> cityes) {
        return String.join("\n\n"
                , "Найденные совпадения:"
                , getCityesNames(cityes)
                , "Уточните ваш запрос!");
    }

    private String getCityesNames(List<City> cityes) {
        return cityes.stream()
                .map(City::getName)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        City profileData = userDataCache.getCityesProfileData(userId);
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
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
        }

        userDataCache.saveCityesProfileData(userId, profileData);
        return replyToUser;
    }
}



