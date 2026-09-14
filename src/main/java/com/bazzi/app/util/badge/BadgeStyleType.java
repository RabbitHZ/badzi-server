package com.bazzi.app.util.badge;

/**
 * 뱃지 스타일 타입 정의
 * 새로운 스타일 추가 시 여기에 enum 값을 추가하고
 * 해당 Generator 클래스를 구현하면 됩니다.
 */
public enum BadgeStyleType {
    BASIC("basic", "기본 스타일"),
    MAPLE("maple", "메이플스토리 스타일"),
    RABBIT("rabbit", "래빗 스타일"),
    DREAM_RABBIT("dream_rabbit", "드림래빗 스타일");

    private final String code;
    private final String description;

    BadgeStyleType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static BadgeStyleType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return BASIC;
        }
        for (BadgeStyleType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return BASIC;
    }
}
