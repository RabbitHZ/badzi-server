package com.bazzi.app.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeRequestDto {
    private String url = "";
    private String color = "blue";
    private String label = "Views";
    private int fontSize = 12;
}
