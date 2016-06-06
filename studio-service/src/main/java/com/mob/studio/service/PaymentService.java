package com.mob.studio.service;

import com.mob.studio.domain.Payment;
import com.mob.studio.domain.PaymentItem;

/**
 * @author: Zhang.Min
 * @since: 2016/4/19
 * @version: 1.7
 */
public interface PaymentService {
    //insert Payment,insert PaymentItem
    void addPayment(Payment payment,PaymentItem paymentItem);

    //insert PaymentEscrow
    boolean checkoutPaymentEscrow(String transactionId);

    boolean updatePayment(String transactionId);
}
