package by.verishko.mysitybot.cache;


import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    City getCityesProfileData(int userId);

    void saveCityesProfileData(int userId, City userProfileData);
}
