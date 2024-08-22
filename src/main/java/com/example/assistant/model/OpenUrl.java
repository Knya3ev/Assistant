package com.example.assistant.model;

import com.example.assistant.model.enums.OperationName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue(value = "OPEN")
public class OpenUrl extends Operation {

    private String url;

    @Builder.Default
    private OperationName type = OperationName.OPEN;
}
