package com.example.assistant.exception;

import com.example.assistant.model.Tracking;

public class TrackingException extends RuntimeException{

    public TrackingException(String message, Throwable cause) {
        super(message, cause);
    }
}
