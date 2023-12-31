package fr.graynaud.logbackelasticclientappender;

import java.util.ArrayList;
import java.util.List;

public class ECLAProperties {

    private final List<ECLAProperty> properties = new ArrayList<>();

    public List<ECLAProperty> getProperties() {
        return properties;
    }

    public void addProp(ECLAProperty property) {
        this.properties.add(property);
    }
}
