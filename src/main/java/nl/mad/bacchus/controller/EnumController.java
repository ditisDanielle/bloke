/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.bacchus.controller;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import nl.mad.bacchus.model.Wine;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.Labeled;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link org.springframework.web.bind.annotation.RestController} for {@link nl.mad.bacchus.model.Cheese} stuff.
 */
@RestController
@RequestMapping("/enum")
public class EnumController {

    @RequestMapping(value = "/wine", method = RequestMethod.GET)
    public Map<String, Set<EnumValue>> getWineEnums() {
        return this.getAllEnumValues(Wine.getEnumValues());
    }

    @RequestMapping(value = "/cheese", method = RequestMethod.GET)
    public Map<String, Set<EnumValue>> getCheeseEnums() {
        return this.getAllEnumValues(Cheese.getEnumValues());
    }

    private <T extends Enum & Labeled> Map<String, Set<EnumValue>> getAllEnumValues(Map<String, T[]> values) {
        Map<String, Set<EnumValue>> result = new HashMap<>();  
        for(String key : values.keySet()) {
            result.put(key, createEnumValueObjects(values.get(key)));
        }
        return result;
    }

    private <T extends Enum & Labeled> Set<EnumValue> createEnumValueObjects(T[] values) {
        Set<EnumValue> valueList = new LinkedHashSet<>();
        for (T value : values) {
            valueList.add(new EnumValue(value.name(), value.getLabel()));
        }
        return valueList;
    }

    private class EnumValue {
        private String id;
        private String label;

        public EnumValue(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
