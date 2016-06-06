package com.mob.studio.service;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/4/15
 * @version: 1.7
 */
public interface BankService {
    String createTransaction(String jsonReq,List<String> messageList);

    void openTransaction(String transactionId,List<String> messageList);

    void closeTransaction(String transactionId,List<String> messageList);
}
