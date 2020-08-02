package by.verishko.mysitybot.botapi.handlers.menu;

import by.verishko.mysitybot.botapi.BotState;
import by.verishko.mysitybot.botapi.InputMessageHandler;
import by.verishko.mysitybot.service.MainMenuService;
import by.verishko.mysitybot.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class HelpMenuHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessagesService messagesService;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessagesService messagesService) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.showHelpMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
