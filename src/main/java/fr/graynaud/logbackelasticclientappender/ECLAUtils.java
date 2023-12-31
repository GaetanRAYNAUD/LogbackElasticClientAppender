package fr.graynaud.logbackelasticclientappender;

public class ECLAUtils {

    private ECLAUtils() {
    }

    public static String cleanStringSetting(String setting) {
        if (setting == null) {
            return null;
        }

        setting = setting.trim();

        return setting.isEmpty() ? null : setting;
    }
}
