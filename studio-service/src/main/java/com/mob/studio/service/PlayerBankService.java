package com.mob.studio.service;

import com.mob.studio.domain.Item;
import com.mob.studio.domain.PlayerBank;

import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/22
 * @version: 1.7
 */
public interface PlayerBankService {
    void initBank(Long id);
    Long getPlayerPointById(Long id);
    Long getPlayerCoinById(Long id);
    Long getPlayerSendById(Long id);
    /**
     * 为demo设计的用户持有的鲜花数据接口
     * */
    Long getPlayersFlowersById(Long id);

    List<Map<String,Object>> findGiftRankByPlayerId(Long playerId, Integer pageNo, Integer pageSize);

    //送礼
    void insertOrUpdatePlayerCoin(PlayerBank playerBank);
    void insertOrUpdatePlayerPoint(PlayerBank playerBank);
    void insertOrUpdatePlayerStatistic(PlayerBank playerBank);
    void useItem(Long playerId, Long targetId, Item item, Long quantity);
}
