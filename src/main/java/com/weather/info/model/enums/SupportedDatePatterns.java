package com.weather.info.model.enums;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public enum SupportedDatePatterns {
    DD_MM_YYYY("dd-MM-yyyy"),
    DD_SLASH_MM_SLASH_YYYY("dd/MM/yyyy");

    private final String regex;

    SupportedDatePatterns(String regex) {
        this.regex = regex;
    }

    public static String getSupportedDateRegex() {
        List<String> supportedRegex = new ArrayList<>();
        for (SupportedDatePatterns pattern : SupportedDatePatterns.values()) {
            supportedRegex.add(pattern.regex);
        }
        return String.join("|", supportedRegex);
    }

    public static LocalDate parse(String date) {
        for (SupportedDatePatterns pattern : SupportedDatePatterns.values()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.regex);
            try {
                return LocalDate.parse(date.trim(), formatter);
            } catch (Exception e) {
                log.info("Given Date not matched with pattern: {}", pattern.regex);
            }
        }
        return null;
    }


}
