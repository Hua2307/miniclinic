package tw.edu.fju.miniclinic.model;

import java.time.LocalDate;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        return date == null ? null : date.toString();  // LocalDate → "2026-05-01"
    }

    @Override
    public LocalDate convertToEntityAttribute(String s) {
        return s == null ? null : LocalDate.parse(s);  // "2026-05-01" → LocalDate
    }
}