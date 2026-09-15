package com.bazzi.app.util.badge.generator;

import org.springframework.stereotype.Component;

@Component
public class CatBadgeGenerator extends AbstractTemplateBadgeGenerator {

    public CatBadgeGenerator() {
        super("badge-templates/cat.svg");
    }
}
