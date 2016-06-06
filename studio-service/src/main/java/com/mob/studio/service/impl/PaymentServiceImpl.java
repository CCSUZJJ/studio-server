package com.mob.studio.service.impl;

import com.mob.studio.dao.PaymentDao;
import com.mob.studio.domain.*;
import com.mob.studio.service.PaymentService;
import com.mob.studio.util.MybatisUtil;
import com.mob.studio.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/4/20
 * @version: 1.7
 */
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = Logger.getLogger(BankServiceImpl.class);

    private static final SqlSessionFactory sqlSessionFactory_Studio_W = MybatisUtil.getSqlSessionFactory_Studio_W();

    @Autowired
    @Qualifier("paymentDao")
    private PaymentDao paymentDao;

    @Override
    public void addPayment(Payment payment, PaymentItem paymentItem) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        paymentDao.insertPayment(sqlSession, payment);
        paymentDao.insertPaymentItem(sqlSession, paymentItem);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public boolean checkoutPaymentEscrow(String transactionId) {
        boolean result = false;
        byte[] uuid = StringUtil.asByteArray(StringUtil.fromString(transactionId));
        Map<String, byte[]> map = new HashMap<>();
        map.put("uuid", uuid);
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Payment payment = paymentDao.getPaymentByUUId(sqlSession, map);
        if (payment != null) {
            PaymentItem paymentItem = paymentDao.getPaymentItemById(sqlSession, payment.getId());
            if (paymentItem != null) {
                PaymentEscrow paymentEscrow = new PaymentEscrow();
                paymentEscrow.setPaymentId(payment.getId());
                paymentEscrow.setIsPaybacked(0);
                paymentEscrow.setRowId(0L);
                paymentEscrow.setPaybackedRowId(0L);
                paymentEscrow.setStatus(PaymentStatus.OPEN);
                Long price = paymentItem.getUnitPrice() * paymentItem.getAmount();
                paymentEscrow.setPrice(price);
                paymentEscrow.setComment(paymentItem.getDescription());
                paymentDao.insertPaymentEscrow(sqlSession, paymentEscrow);
                sqlSession.commit();
                result = true;
            }
        }
        sqlSession.close();
        return result;
    }

    @Override
    public boolean updatePayment(String transactionId) {
        boolean result = false;
        byte[] uuid = StringUtil.asByteArray(StringUtil.fromString(transactionId));
        Map<String, byte[]> map = new HashMap<>();
        map.put("uuid", uuid);
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Payment payment = paymentDao.getPaymentByUUId(sqlSession, map);
        if (payment != null) {
            PaymentEscrow paymentEscrow = paymentDao.getPaymentEscrowById(sqlSession, payment.getId());
            if (paymentEscrow != null) {
                // log coin history
                CoinHist hist = new CoinHist();
                hist.setUserId(payment.getUserId());
                hist.setPayCoin(paymentEscrow.getPrice());
                hist.setFreeCoin(0L);
                hist.setTrackId(payment.getId());
                hist.setTitle(paymentEscrow.getComment());
                paymentDao.insertCoinHist(sqlSession, hist);
                // update payment status
                payment.setStatus(10);
                paymentDao.updatePayment(sqlSession, payment);
                // update payment_escrow
                paymentEscrow.setRowId(hist.getId());
                paymentEscrow.setIsPaybacked(0);
                paymentEscrow.setStatus(PaymentStatus.CLOSED);
                paymentDao.updatePaymentEscrow(sqlSession, paymentEscrow);

                sqlSession.commit();
                result = true;
            }
        }
        sqlSession.close();
        return result;
    }
}
