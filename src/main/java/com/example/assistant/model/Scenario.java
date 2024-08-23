package com.example.assistant.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "scenario")
public class Scenario {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "scenario_seq", sequenceName = "scenario_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scenario_seq")
    private Long id;

    private String name;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Operation> operations;

    @Builder.Default
    private boolean isRun = false;

}
