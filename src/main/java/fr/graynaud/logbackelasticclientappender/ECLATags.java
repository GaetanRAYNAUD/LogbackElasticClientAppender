package fr.graynaud.logbackelasticclientappender;

import java.util.ArrayList;
import java.util.List;

public class ECLATags {

    private final List<String> tags = new ArrayList<>();

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}
