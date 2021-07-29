package net.rotmcemojis;

import com.moandjiezana.toml.Toml;
import net.kyori.adventure.text.Component;
import com.velocitypowered.api.command.RawCommand;

public class EmojiCommandManager implements RawCommand  {
    private final String langPrefix;
    private final String langNoPermission;
    private final String langEmojiListHeader;
    private final String langEmojiListTutorialText;

    public EmojiCommandManager(Toml toml) {
        // Localization
        Toml lang = toml.getTable("lang");
        this.langPrefix = lang.getString("prefix");
        this.langNoPermission = lang.getString("nopermission");
        this.langEmojiListHeader = lang.getString("emojilistheader");
        this.langEmojiListTutorialText = lang.getString("emojilisttutorialtext");
    }
    private String getFormattedListOfEmojis() {
        String list = "";
        //lol caveman programming
        String spacing = "                 ";
        for (listOfEmoji x : listOfEmoji.values()) {
            list = list.concat(spacing);
            list = list.concat("§6");
            list = list.concat(x.asciiversion);
            list = list.concat("§f = ");
            list = list.concat(x.identifier);
            list = list.concat("\n");
        }
        return list;
    }
    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("emojichat")) {
            invocation.source().sendMessage(Component.text()
                    .content(langPrefix + langNoPermission)
            );
            return;
        }
        else{
            invocation.source().sendMessage(Component.text()
                    .content(langEmojiListHeader + "\n" + getFormattedListOfEmojis() + langEmojiListTutorialText));
            return;
        }
    }
}