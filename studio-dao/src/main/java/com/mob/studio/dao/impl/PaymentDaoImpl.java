package com.mob.studio.dao.impl;

import com.mob.studio.dao.PaymentDao;
import com.mob.studio.domain.CoinHist;
import com.mob.studio.domain.Payment;
import com.mob.studio.domain.PaymentEscrow;
import com.mob.studio.domain.PaymentItem;
import com.mob.studio.mapper.PaymentMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/4/19
 * @version: 1.7
 */
public class PaymentDaoImpl implements PaymentDao {
    @Override
    public void insertPayment(SqlSession sqlSession, Payment payment) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.insertPayment(payment);
    }

    @Override
    public void updatePayment(SqlSession sqlSession, Payment payment) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.updatePayment(payment);
    }

    @Override
    public void insertPaymentItem(SqlSession sqlSession, PaymentItem paymentItem) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.insertPaymentItem(paymentItem);
    }

    @Override
    public void insertPaymentEscrow(SqlSession sqlSession, PaymentEscrow paymentEscrow) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.insertPaymentEscrow(paymentEscrow);
    }

    @Override
    public void updatePaymentEscrow(SqlSession sqlSession, PaymentEscrow paymentEscrow) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.updatePaymentEscrow(paymentEscrow);
    }

    @Override
    public void insertCoinHist(SqlSession sqlSession, CoinHist coinHist) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        mapper.insertCoinHist(coinHist);
    }

    @Override
    public Payment getPaymentById(SqlSession sqlSession, Long id) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        return mapper.getPaymentById(id);
    }

    @Override
    public Payment getPaymentByUUId(SqlSession sqlSession, Map<String,byte[]> map) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        return mapper.getPaymentByUUId(map);
    }

    @Override
    public PaymentEscrow getPaymentEscrowById(SqlSession sqlSession, Long paymentId) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        return mapper.getPaymentEscrowById(paymentId);
    }

    @Override
    public PaymentItem getPaymentItemById(SqlSession sqlSession, Long paymentId) {
        PaymentMapper mapper = sqlSession.getMapper(PaymentMapper.class);
        return mapper.getPaymentItemById(paymentId);
    }
}
