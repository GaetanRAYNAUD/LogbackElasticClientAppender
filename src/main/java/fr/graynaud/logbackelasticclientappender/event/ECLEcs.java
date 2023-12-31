package fr.graynaud.logbackelasticclientappender.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ECLEcs {

    public static final ECLEcs INSTANCE = new ECLEcs();

    private static final String VERSION = "8.11.0";

    private ECLEcs() {
    }

    @JsonProperty("version")
    public String getVersion() {
        return VERSION;
    }
}
