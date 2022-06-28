package com.viner.site.service.dto;

import lombok.Data;

@Data
public class TodoDto {
    private Long id;
    private String title;
    private Boolean completed;
}
