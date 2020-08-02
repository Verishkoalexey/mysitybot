package by.verishko.mysitybot.botapi.handlers.menu;

import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.InputMessageHandler;
import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;
import by.verishko.mysitybot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final City profileData = userDataCache.getCityesProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(message.getChatId(), String.format("%s%n -------------------%nНазвание города: %s%nОписание: %s%n", "Данные по вашей анкете",
                profileData.getName(), profileData.getDescription()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
