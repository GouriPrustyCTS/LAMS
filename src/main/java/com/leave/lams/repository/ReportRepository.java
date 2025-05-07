package com.leave.lams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leave.lams.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
