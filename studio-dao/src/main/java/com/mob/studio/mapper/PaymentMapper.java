package com.mob.studio.mapper;

import com.mob.studio.domain.CoinHist;
import com.mob.studio.domain.Payment;
import com.mob.studio.domain.PaymentEscrow;
import com.mob.studio.domain.PaymentItem;

import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/4/19
 * @version: 1.7
 */
public interface PaymentMapper {
    void insertPayment(Payment payment);
    Payment getPaymentById(Long id);
    void updatePayment(Payment payment);
    void insertPaymentItem(PaymentItem paymentItem);

    void insertPaymentEscrow(PaymentEscrow paymentEscrow);
    PaymentEscrow getPaymentEscrowById(Long paymentId);
    void updatePaymentEscrow(PaymentEscrow paymentEscrow);
    void insertCoinHist(CoinHist coinHist);

    Payment getPaymentByUUId(Map<String,byte[]> map);
    PaymentItem getPaymentItemById(Long id);
}
