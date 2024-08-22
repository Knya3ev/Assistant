package com.example.assistant.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConverterOperationName implements AttributeConverter<OperationName, String> {
    @Override
    public String convertToDatabaseColumn(OperationName operationCode) {
        if (operationCode == null) {
            return null;
        }
        return operationCode.getCode();
    }

    @Override
    public OperationName convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return OperationName.fromCode(code);
    }
}
