package net.rotmcemojis;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import java.util.Arrays;


public class ROTMCEmojiHandlerChat {
    private final ProxyServer server;
    private final String langPrefix;
    private final String langNoPermission;
    private final String langInvalidCharPrefix;
    private final String langInvalidCharSuffix;
    private final String[] allowedCharacters = {"1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "@",
            " ", "!", "#","$", "%","^", "&","*", "(",")", "_","-", "=" ,"+" ,
            "{","}", "[","]", "\\", "|", "~","`", ":", ".", "/", "<", ">", "?", ";", "'", "\"", ","};

    public ROTMCEmojiHandlerChat(ProxyServer server, Toml toml) {
        this.server = server;
        // Localization
        Toml lang = toml.getTable("lang");
        this.langPrefix = lang.getString("prefix");
        this.langNoPermission = lang.getString("nopermission");
        this.langInvalidCharPrefix = lang.getString("invalidcharprefix");
        this.langInvalidCharSuffix = lang.getString("invalidcharsuffix");

    }
    private boolean messageContainsEmoji(String message){
        for (listOfEmoji x: listOfEmoji.values()){
            if (message.contains(x.identifier)){
                return true;
            }
        }
        return false;
    }
    private String replaceMessageEmojiForASCII(String message){
        for (listOfEmoji x: listOfEmoji.values()){
            if (message.contains(x.identifier)){
                message = message.replace(x.identifier, x.asciiversion);
            }
        }
        return message;
    }
    private String checkForBlacklistedCharacters(String message){
        String[] messageArray = message.split("(?!^)");
        for(String m : messageArray){
            if (Arrays.stream(allowedCharacters).noneMatch(m::contains)){
                return m;
            }
        }
        return null;
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onChat(PlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        String blacklistedChar = checkForBlacklistedCharacters(message);
        if (blacklistedChar != null){
            // they have a blacklisted character in their message so, the message shouldn't be passed to the backend.
            e.setResult(PlayerChatEvent.ChatResult.denied());
            player.sendMessage(Component.text()
                    .content(langPrefix + langInvalidCharPrefix + "§c" + blacklistedChar + langInvalidCharSuffix)
            );
            return;
        }
        if (!player.hasPermission("emojichat") && messageContainsEmoji(message)) {
            // they don't have permission, and the message contains an emoji so it shouldn't be passed to the backend.
            e.setResult(PlayerChatEvent.ChatResult.denied());
            player.sendMessage(Component.text()
                    .content(langPrefix + langNoPermission)
            );
            return;
        }
        if (player.hasPermission("emojichat") && messageContainsEmoji(message)) {
            // they don't have permission, and the message contains an emoji so it shouldn't be passed to the backend.
            //e.setResult(PlayerChatEvent.ChatResult.denied());
            String newMessage = replaceMessageEmojiForASCII(message);
            e.setResult(PlayerChatEvent.ChatResult.message(newMessage));
            return;
        }
    }
}
