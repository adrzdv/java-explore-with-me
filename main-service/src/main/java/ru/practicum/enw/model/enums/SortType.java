package ru.practicum.enw.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SortType {
    EVENT_DATE,
    VIEWS;

    @JsonCreator
    public static SortType fromString(String value) {
        return SortType.valueOf(value.toUpperCase());
    }
}
