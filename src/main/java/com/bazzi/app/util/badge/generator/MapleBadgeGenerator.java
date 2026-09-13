package com.bazzi.app.util.badge.generator;

import org.springframework.stereotype.Component;

@Component
public class MapleBadgeGenerator extends AbstractTemplateBadgeGenerator {

    public MapleBadgeGenerator() {
        super("badge-templates/maple.svg");
    }
}
