package com.mob.studio.dao;

import com.mob.studio.domain.PlayerBank;
import org.apache.ibatis.session.SqlSession;

/**
 * @author: Zhang.Min
 * @since: 2016/4/8
 * @version: 1.7
 */
public interface PlayerBankDao {
    Long getPlayerPointById(SqlSession sqlSession,Long id);

    Long getPlayerCoinById(SqlSession sqlSession,Long id);

    Long getPlayerCoinOutById(SqlSession sqlSession,Long id);

    void insertOrUpdatePlayerCoin(SqlSession sqlSession,PlayerBank playerBank);

    void insertOrUpdatePlayerPoint(SqlSession sqlSession,PlayerBank playerBank);

    void insertOrUpdatePlayerBankStatistic(SqlSession sqlSession,PlayerBank playerBank);

    PlayerBank getPlayerBankStatisticById(SqlSession sqlSession,Long id);

}
