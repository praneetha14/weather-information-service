package com.weather.info.model.enums;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
// Enum: represents a fixed set of constants (here, supported date formats)
public enum SupportedDatePatterns {

    // First enum constant: supports dates like "05-09-2025"
    DD_MM_YYYY("dd-MM-yyyy"),
    // Second enum constant: supports dates like "05/09/2025"
    DD_SLASH_MM_SLASH_YYYY("dd/MM/yyyy");

    // Variable to store the actual date format string (pattern)
    private final String regex;

    //Constructor for enum, automatically called for each constant above
    SupportedDatePatterns(String regex) {
    // Stores the provided date format string into the 'regex' variable
        this.regex = regex;
    }

    // Method to return all supported date formats in one string
    public static String getSupportedDateRegex() {
        // Create an empty list to hold formats
        List<String> supportedRegex = new ArrayList<>();
        // Loop through all enum constants (DD_MM_YYYY, DD_SLASH_MM_SLASH_YYYY)
        for (SupportedDatePatterns pattern : SupportedDatePatterns.values()) {
            // Add each format string (like "dd-MM-yyyy") to the list
            supportedRegex.add(pattern.regex);
        }
        // Join all formats with "|" symbol, result: "dd-MM-yyyy|dd/MM/yyyy"
        return String.join("|", supportedRegex);
    }

    // Method to convert a date string into LocalDate (if it matches any supported format)
    public static LocalDate parse(String date) {
        // Loop through all supported patterns
        for (SupportedDatePatterns pattern : SupportedDatePatterns.values()) {
            // Create formatter for the current pattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.regex);
            try {
                // Try to parse the given date string using the current pattern
                // If successful, immediately return the parsed LocalDate
                return LocalDate.parse(date.trim(), formatter);
            } catch (Exception e) {
                // If parsing fails, log info that the pattern didn’t match
                log.info("Given Date not matched with pattern: {}", pattern.regex);
            }
        }
        // If no patterns matched, return null (means unsupported format)
        return null;
    }
}
