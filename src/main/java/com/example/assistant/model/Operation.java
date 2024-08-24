package com.example.assistant.model;


import com.example.assistant.model.emp_type.Tracking;
import com.example.assistant.model.enums.OperationName;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "operation")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "emp_type")
public class Operation {
    @Id
    @SequenceGenerator(allocationSize = 1, name = "operation_seq", sequenceName = "operation_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_seq")
    private Long id;

    private String name;

    private int timeout = 2;

    private OperationName type;

    @Column(name = "order_operation")
    private int orderOperation;

    @ManyToOne
    private Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "tracking_id")
    private Tracking tracking;

    @Column(name = "end_point")
    private boolean endPoint = false;


    @Override
    public String toString() {
        return "Operation{" +
               "id=" + id +
               ", timeOut='" + timeout + '\'' +
               ", orderOperation=" + orderOperation +
               ", scenario=" + scenario +
               ", tracking=" + tracking +
               '}';
    }
}
