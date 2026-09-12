package com.bazzi.app.infrastructure.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySellerReference(String sellerReference);
    Optional<Payment> findByOrderNo(String orderNo);
    List<Payment> findAllByUserId(Long userId);
}
