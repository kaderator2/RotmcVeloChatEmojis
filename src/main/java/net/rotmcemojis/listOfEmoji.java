package net.rotmcemojis;

import java.util.HashMap;
import java.util.Map;

public enum listOfEmoji {
    VALORCRY("valorcry", "⌀"),
    VALORSALUTE("valorsalute", "⌱"),
    VALORSAD("valorsad", "⌁"),
    VALORANGRY("valorangry", "⌇"),
    VALORHAPPY("valorhappy", "⌰");
    private static final Map<String, listOfEmoji> IDENTIFIER = new HashMap<>();
    private static final Map<String, listOfEmoji> ASCIIVERSION = new HashMap<>();
    static {
        for (listOfEmoji e : values()) {
            IDENTIFIER.put(e.identifier, e);
            ASCIIVERSION.put(e.asciiversion, e);
        }
    }
    public final String identifier;
    public final String asciiversion;
    private listOfEmoji(String identifier, String asciiversion){
        this.identifier = identifier;
        this.asciiversion = asciiversion;
    }
    public static listOfEmoji valueOfIdentifier(String identifier) {
        return IDENTIFIER.get(identifier);
    }
    public static listOfEmoji valueOfAsciiVersion(String asciiversion) {
        return ASCIIVERSION.get(asciiversion);
    }
}
