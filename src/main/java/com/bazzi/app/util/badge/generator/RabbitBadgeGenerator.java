package com.bazzi.app.util.badge.generator;

import org.springframework.stereotype.Component;

@Component
public class RabbitBadgeGenerator extends AbstractTemplateBadgeGenerator {

    public RabbitBadgeGenerator() {
        super("badge-templates/rabbit.svg");
    }
}
