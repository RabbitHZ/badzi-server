package com.bazzi.app.util.badge.generator;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTemplateBadgeGenerator implements BadgeGenerator {

    private final String template;

    protected AbstractTemplateBadgeGenerator(String templatePath) {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            this.template = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Badge template not found: " + templatePath, e);
        }
    }

    @Override
    public String generate(String color, String label, long today, long total) {
        return template
                .replace("{{color}}", resolveColor(color))
                .replace("{{label}}", label != null ? label : "views")
                .replace("{{today}}", formatViews(today))
                .replace("{{total}}", formatViews(total));
    }

    protected String resolveColor(String color) {
        return color;
    }

    private String formatViews(long views) {
        if (views >= 1_000_000) return String.format("%.1fM", views / 1_000_000.0);
        if (views >= 1_000) return String.format("%.1fK", views / 1_000.0);
        return String.valueOf(views);
    }
}
