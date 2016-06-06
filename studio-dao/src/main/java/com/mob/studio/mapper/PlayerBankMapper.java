package com.mob.studio.mapper;

import com.mob.studio.domain.PlayerBank;

/**
 * @author: Zhang.Min
 * @since: 2016/4/8
 * @version: 1.7
 */
public interface PlayerBankMapper {

    Long getPlayerPointById(Long id);

    Long getPlayerCoinById(Long id);

    Long getPlayerCoinOutById(Long id);

    void insertPlayerCoin(PlayerBank playerBank);

    void insertPlayerPoint(PlayerBank playerBank);

    void insertPlayerBankStatistic(PlayerBank playerBank);

    void updatePlayerCoin(PlayerBank playerBank);

    void updatePlayerPoint(PlayerBank playerBank);

    void updatePlayerBankStatistic(PlayerBank playerBank);

    PlayerBank getPlayerBankStatisticById(Long id);
}
