package com.neurotech.credit_api.repository;

import com.neurotech.credit_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByAgeBetweenAndIncomeBetween(int minAge, int maxAge, double minIncome, double maxIncome);
}

