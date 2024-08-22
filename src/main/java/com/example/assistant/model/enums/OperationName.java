package com.example.assistant.model.enums;

public enum OperationName {
    OPEN("OPEN"),
    TRACKING("TRACKING"),
    CLICK("CLICK"),
    INSERT("INSERT"),
    BACK("BACK");

    private final String code;

    OperationName(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OperationName fromCode(String code) {
        for (OperationName operationName : OperationName.values()) {
            if (operationName.getCode().equals(code)) return operationName;
        }
        throw new IllegalArgumentException("Неизвестный код:" + code);
    }

}
