package com.leave.lams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}

