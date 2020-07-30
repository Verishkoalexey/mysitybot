package by.verishko.mysitybot.cache;

import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.handlers.fillingprofile.City;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory cache.
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and cityes profile data.
 */

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, City> cityesProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.ASK_DESTINY;
        }

        return botState;
    }

    @Override
    public City getCityesProfileData(int userId) {
        City userProfileData = cityesProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new City();
        }
        return userProfileData;
    }

    @Override
    public void saveCityesProfileData(int userId, City userProfileData) {
        cityesProfileData.put(userId, userProfileData);
    }
}
