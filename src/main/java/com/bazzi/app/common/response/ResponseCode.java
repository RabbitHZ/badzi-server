package com.bazzi.app.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResponseCode {

    VIEW_COUNT_READ_SUCCESS(HttpStatus.OK, true, "조회수 조회 성공"),
    VIEW_COUNT_INCREASE_SUCCESS(HttpStatus.OK, true, "조회수 증가 성공"),
    VIEW_COUNT_RESET_SUCCESS(HttpStatus.OK, true, "조회수 초기화 성공"),
    BADGE_CREATE_SUCCESS(HttpStatus.OK, true, "뱃지 생성 및 조회수 증가 성공"),
    BADGE_PREVIEW_CREATE_SUCCESS(HttpStatus.OK, true, "미리보기 뱃지 생성 성공");

    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;
}
