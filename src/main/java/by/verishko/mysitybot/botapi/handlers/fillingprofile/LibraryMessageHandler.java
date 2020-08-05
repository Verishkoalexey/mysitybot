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

@Slf4j
@Component
public class LibraryMessageHandler implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    @Autowired
    private CityService cityService;

    public LibraryMessageHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEARСH_PROFILE;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARСH_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.SEARСH_PROFILE);
        }
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        Long chatId = inputMsg.getChatId();
        if (inputMsg.getText().equals("Поиск информации о городе")) {
            return messagesService.getReplyMessage(chatId, "reply.askCity");
        }
        String inputNameCity = inputMsg.getText();
        List<City> citiesDB = cityService.getNameInList(inputNameCity);
        if (citiesDB.size() > 1) {
            return new SendMessage(chatId, specifyRequest(citiesDB));
        }
        else if (citiesDB.size() == 1) {
            return new SendMessage(chatId,"город: " +citiesDB.get(0).getName()+ " описание: " +citiesDB.get(0).getDescription());
        }
        else {
            return new SendMessage(chatId, "Такой игры в библиотеке нет!");
        }
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
}
