package com.example.assistant.model;


import com.example.assistant.model.enums.OperationName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue(value = "CLICK")
public class Click extends Operation {

    private String selector;
    @Builder.Default
    private OperationName type = OperationName.CLICK;

}
