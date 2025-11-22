package com.payment_chat.project.repository;

import com.payment_chat.project.model.Payment;
import com.payment_chat.project.model.Payment.PaymentStatus;
import com.payment_chat.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByMercadoPagoPaymentId(String mercadoPagoPaymentId);

    @Query("SELECT p FROM Payment p WHERE p.requester = :user OR p.payer = :user ORDER BY p.createdAt DESC")
    List<Payment> findByUser(User user);

    List<Payment> findByPayerAndStatus(User payer, PaymentStatus status);
}
