package com.example.assistant.model.emp_type;


import com.example.assistant.model.Operation;
import com.example.assistant.model.enums.OperationName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value = "TRACKING")
public class Tracking extends Operation {

    private String selector;

    @Column(name = "number_of_iteration")
    private int numberOfIterations;

    private boolean isClicked;

    private OperationName type = OperationName.TRACKING;

    @OneToMany(mappedBy = "tracking", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Operation> operations = new ArrayList<>();

}
