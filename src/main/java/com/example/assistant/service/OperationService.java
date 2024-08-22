package com.example.assistant.service;

import com.example.assistant.model.Operation;
import com.example.assistant.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;

    public void delete(Operation operation) {
        operationRepository.delete(operation);
    }

    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    public Operation getById(Long id) {
        return operationRepository.findById(id).orElseThrow(() -> new RuntimeException("Operation not found"));
    }

    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }

    @Transactional
    public void updateTimeout(Operation operation, int timeout) {
        int oldValue = operation.getOrderOperation();

        if (oldValue != timeout) {
            operationRepository.updateTimeout(operation.getId(), timeout);
        }
    }

    @Transactional
    public void updateOrdering(Operation operation, int newOrdering) {
        int oldValue = operation.getOrderOperation();

        if (oldValue != newOrdering) {
            operationRepository.updateOrdering(operation.getId(), newOrdering);
        }
    }

    @Transactional
    public void updateEndPoint(Operation operation){
        if(operation.getId() != null){
            operationRepository.updateEndpoint(operation.getId(), operation.isEndPoint());
        }
    }


}
