package com.mob.studio.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mob.studio.dao.ItemDao;
import com.mob.studio.domain.Item;
import com.mob.studio.domain.Payment;
import com.mob.studio.domain.PaymentItem;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.BankService;
import com.mob.studio.service.PaymentService;
import com.mob.studio.service.PlayerBankService;
import com.mob.studio.util.DateUtil;
import com.mob.studio.util.MybatisUtil;
import com.mob.studio.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author: Zhang.Min
 * @since: 2016/4/15
 * @version: 1.7
 */
public class BankServiceImpl implements BankService {

    private static final Logger logger = Logger.getLogger(BankServiceImpl.class);

    private static final SqlSessionFactory sqlSessionFactory_Studio_W = MybatisUtil.getSqlSessionFactory_Studio_W();
    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("itemDao")
    private ItemDao itemDao;

    @Autowired
    @Qualifier("playerBankService")
    private PlayerBankService playerBankService;

    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;


    @Override
    public String createTransaction(String reqJson,List<String> messageList) {
        String txID = null;
        HashMap<String,Object> reqMap = JSON.parseObject(reqJson, new TypeReference<HashMap<String, Object>>() {});
        Long playerId = Long.valueOf(reqMap.get("playerId").toString());
        String itemId = (String) reqMap.get("itemId");
        Long quantity = Long.valueOf(reqMap.get("quantity").toString());
        if (playerId != null && itemId != null && quantity != null){
            //check item validation
            SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
            Item item = itemDao.getItemById(sqlSession, itemId);
            sqlSession.close();
            if (item == null || item.getDisabled()){
                messageList.add("item is not found or disabled.\t>>>>>\t" + JSON.toJSON(item));
                return txID;
            }
            Long coin = playerBankService.getPlayerCoinById(playerId);
            Long priceTotal = item.getPrice() * quantity;
            if (priceTotal > coin){
                messageList.add("not enough coin.\t>>>>>\tprice total:" + priceTotal + "\tcoin:" + coin);
                return txID;
            }
            Payment payment = buildPayment(playerId);
            PaymentItem paymentItem = buildPaymentItem(payment.getId(), item, quantity);
            paymentService.addPayment(payment,paymentItem);
            txID = StringUtil.asUuid(payment.getUuid()).toString();
        } else{
            messageList.add("params missing,transaction failed.\t>>>>>\t" + JSON.toJSON(reqJson));
        }
        return txID;
    }

    @Override
    public void openTransaction(String transactionId, List<String> messageList) {
        boolean isok = paymentService.checkoutPaymentEscrow(transactionId);
        if (!isok){
            messageList.add("checkout payment_escrow failed.\t>>>>>transactionId:" + transactionId);
        }

    }

    @Override
    public void closeTransaction(String transactionId, List<String> messageList) {
        boolean isok = paymentService.updatePayment(transactionId);
        if (!isok){
            messageList.add("update payment failed.\t>>>>>transactionId:" + transactionId);
        }
    }

    private Payment buildPayment(Long playerId){
        Payment payment = new Payment();
        payment.setId(generateInnerTransactionId(playerId));
        payment.setUuid(StringUtil.asByteArray(UUID.randomUUID()));
        payment.setUserId(playerId);
        payment.setStatus(0);
        return payment;
    }

    private PaymentItem buildPaymentItem(Long paymentId,Item item,Long quantity){
        PaymentItem paymentItem = new PaymentItem();
        paymentItem.setPaymentId(paymentId);
        paymentItem.setItemId(item.getId());
        paymentItem.setUnitPrice(item.getPrice());
        paymentItem.setAmount(quantity);
        paymentItem.setImageUrl("");
        paymentItem.setDescription("");
        return paymentItem;
    }

    private Long generateInnerTransactionId(Long playerId) {
        String date = DateUtil.returnTimeStamp().toString();
        String dateLast3 = date.substring(10, 13);
        String dateFirst = String.valueOf(date.charAt(0));
        String dateOther = date.substring(1,10);
        String business = "0";
        String uidStr = String.valueOf(playerId);
        uidStr = (uidStr.length() < 5)? String.format("%05d",playerId): uidStr.substring(uidStr.length() - 5, uidStr.length());
        StringBuilder sb = new StringBuilder();
        sb.append(dateFirst).append(dateLast3).append(business).append(uidStr).append(dateOther);
        return Long.valueOf(sb.toString());
    }
}
