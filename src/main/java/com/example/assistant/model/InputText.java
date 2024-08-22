package com.example.assistant.model;


import com.example.assistant.model.enums.OperationName;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@DiscriminatorValue(value = "INSERT")
public class InputText extends Operation {

    @Column(name = "input_text")
    private String inputText;

    private String selector;

    @Builder.Default
    private boolean isPush = false;

    @Builder.Default
    private OperationName type = OperationName.INSERT;

}
