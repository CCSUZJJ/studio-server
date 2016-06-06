package com.mob.studio.dao;

import com.mob.studio.domain.CoinHist;
import com.mob.studio.domain.Payment;
import com.mob.studio.domain.PaymentEscrow;
import com.mob.studio.domain.PaymentItem;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/4/19
 * @version: 1.7
 */
public interface PaymentDao {
    void insertPayment(SqlSession sqlSession, Payment payment);
    void updatePayment(SqlSession sqlSession, Payment payment);
    void insertPaymentItem(SqlSession sqlSession, PaymentItem paymentItem);
    void insertPaymentEscrow(SqlSession sqlSession, PaymentEscrow paymentEscrow);
    void updatePaymentEscrow(SqlSession sqlSession, PaymentEscrow paymentEscrow);
    void insertCoinHist(SqlSession sqlSession, CoinHist coinHist);

    Payment getPaymentById(SqlSession sqlSession, Long id);
    Payment getPaymentByUUId(SqlSession sqlSession, Map<String,byte[]> map);
    PaymentEscrow getPaymentEscrowById(SqlSession sqlSession,Long paymentId);
    PaymentItem getPaymentItemById(SqlSession sqlSession,Long paymentId);
}
