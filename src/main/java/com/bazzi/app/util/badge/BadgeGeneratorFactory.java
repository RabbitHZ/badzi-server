package com.bazzi.app.util.badge;

import com.bazzi.app.util.badge.generator.BadgeGenerator;
import com.bazzi.app.util.badge.generator.BasicBadgeGenerator;
import com.bazzi.app.util.badge.generator.MapleBadgeGenerator;
import com.bazzi.app.util.badge.generator.RabbitBadgeGenerator;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class BadgeGeneratorFactory {

    private final Map<BadgeStyleType, BadgeGenerator> generators;

    public BadgeGeneratorFactory(
            BasicBadgeGenerator basicBadgeGenerator,
            MapleBadgeGenerator mapleBadgeGenerator,
            RabbitBadgeGenerator rabbitBadgeGenerator
    ) {
        this.generators = new EnumMap<>(BadgeStyleType.class);
        this.generators.put(BadgeStyleType.BASIC, basicBadgeGenerator);
        this.generators.put(BadgeStyleType.MAPLE, mapleBadgeGenerator);
        this.generators.put(BadgeStyleType.RABBIT, rabbitBadgeGenerator);
    }

    public BadgeGenerator getGenerator(BadgeStyleType styleType) {
        return generators.getOrDefault(styleType, generators.get(BadgeStyleType.BASIC));
    }

    public BadgeGenerator getGenerator(String styleTypeCode) {
        return getGenerator(BadgeStyleType.fromCode(styleTypeCode));
    }

    public String generateBadge(String styleType, String color, String label, long today, long total) {
        return getGenerator(styleType).generate(color, label, today, total);
    }
}
