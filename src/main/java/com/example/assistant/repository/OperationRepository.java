package com.example.assistant.repository;

import com.example.assistant.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Modifying
    @Query(value = """
            UPDATE Operation o
            SET o.timeout = :timeout
            WHERE o.id = :operationId
            """, nativeQuery = true)
    void updateTimeout(@Param("operationId") Long operationId, @Param("timeout") int timeout);


    @Modifying
    @Query(value = """
            UPDATE Operation 
            SET order_operation = :ordering
            WHERE id = :operationId
            """, nativeQuery = true)
    void updateOrdering(@Param("operationId") Long operationId,@Param("ordering") int ordering);



    @Modifying
    @Query(value = """
            UPDATE Operation
            SET end_point = :endPoint
            WHERE id = :operationId AND end_point != :endPoint
            """, nativeQuery = true)
    void updateEndpoint(@Param("operationId") Long operationId, @Param("endPoint") boolean isEndPoint);
}
