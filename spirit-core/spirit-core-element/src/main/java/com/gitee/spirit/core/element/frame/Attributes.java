package com.gitee.spirit.core.element.frame;

import com.gitee.spirit.common.utils.ListUtils;

import java.util.Arrays;
import java.util.Map;

public abstract class Attributes {

    public Map<String, Object> attributes;

    public Attributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setAttr(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T attr(String attribute) {
        return (T) attributes.get(attribute);
    }

    @SuppressWarnings("unchecked")
    public <T> T attr(String... attributes) {
        String attribute = ListUtils.findOne(Arrays.asList(attributes), this.attributes::containsKey);
        return attribute != null ? (T) this.attributes.get(attribute) : null;
    }

    public boolean attr(String attribute, boolean defaultValue) {
        return (boolean) attributes.getOrDefault(attribute, defaultValue);
    }

}
