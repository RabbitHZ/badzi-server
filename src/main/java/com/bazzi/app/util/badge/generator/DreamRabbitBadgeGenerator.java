package com.bazzi.app.util.badge.generator;

import org.springframework.stereotype.Component;

@Component
public class DreamRabbitBadgeGenerator extends AbstractTemplateBadgeGenerator {

    public DreamRabbitBadgeGenerator() {
        super("badge-templates/dream_rabbit.svg");
    }
}
