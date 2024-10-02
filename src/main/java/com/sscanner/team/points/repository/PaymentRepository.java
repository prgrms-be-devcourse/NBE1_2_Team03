package com.sscanner.team.points.repository;

import com.sscanner.team.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentRecord, String> {
}
