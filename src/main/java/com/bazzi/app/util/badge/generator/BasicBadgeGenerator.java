package com.bazzi.app.util.badge.generator;

import org.springframework.stereotype.Component;

@Component
public class BasicBadgeGenerator extends AbstractTemplateBadgeGenerator {

    public BasicBadgeGenerator() {
        super("badge-templates/basic.svg");
    }

    @Override
    protected String resolveColor(String color) {
        if (color == null || color.isEmpty()) return "#4CAF50";
        if (color.matches("[0-9A-Fa-f]{3,6}")) return "#" + color;
        return color;
    }
}
