package fr.graynaud.logbackelasticclientappender.event;

public class ECLHost {

    private final String hostname;

    public ECLHost(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }
}
