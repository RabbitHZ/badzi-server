package com.bazzi.app.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ViewCountResponseDto {
    private long today;
    private long total;
}
