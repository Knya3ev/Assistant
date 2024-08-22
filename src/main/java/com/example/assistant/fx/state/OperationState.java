package com.example.assistant.fx.state;


import com.example.assistant.model.Operation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class OperationState {
    private Operation operation;
}
