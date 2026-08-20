package com.opxl.noblepay.repository;

import com.opxl.noblepay.model.WebPayRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebPayRequestRepository extends JpaRepository<WebPayRequest, Long> {
    Optional<WebPayRequest> firstByPaymentId(String paymentId);
    Optional<WebPayRequest> findByMd(String md);

}
