package com.mob.studio.dao.impl;

import com.mob.studio.dao.PlayerBankDao;
import com.mob.studio.domain.PlayerBank;
import com.mob.studio.mapper.PlayerBankMapper;
import org.apache.ibatis.session.SqlSession;

/**
 * @author: Zhang.Min
 * @since: 2016/4/8
 * @version: 1.7
 */
public class PlayerBankDaoImpl implements PlayerBankDao {
    @Override
    public Long getPlayerPointById(SqlSession sqlSession, Long id) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        return mapper.getPlayerPointById(id);
    }

    @Override
    public Long getPlayerCoinById(SqlSession sqlSession, Long id) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        return mapper.getPlayerCoinById(id);
    }

    @Override
    public Long getPlayerCoinOutById(SqlSession sqlSession, Long id) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        return mapper.getPlayerCoinOutById(id);
    }

    @Override
    public void insertOrUpdatePlayerCoin(SqlSession sqlSession, PlayerBank playerBank) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        if (mapper.getPlayerCoinById(playerBank.getPlayerId()) != null){
            mapper.updatePlayerCoin(playerBank);
        }else {
            mapper.insertPlayerCoin(playerBank);
        }
    }

    @Override
    public void insertOrUpdatePlayerPoint(SqlSession sqlSession, PlayerBank playerBank) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        if (mapper.getPlayerPointById(playerBank.getPlayerId()) !=null){
            mapper.updatePlayerPoint(playerBank);
        } else {
            mapper.insertPlayerPoint(playerBank);
        }
    }

    @Override
    public void insertOrUpdatePlayerBankStatistic(SqlSession sqlSession, PlayerBank playerBank) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        if (mapper.getPlayerBankStatisticById(playerBank.getPlayerId()) !=null){
            mapper.updatePlayerBankStatistic(playerBank);
        } else {
            mapper.insertPlayerBankStatistic(playerBank);
        }
    }

    @Override
    public PlayerBank getPlayerBankStatisticById(SqlSession sqlSession, Long id) {
        PlayerBankMapper mapper = sqlSession.getMapper(PlayerBankMapper.class);
        return mapper.getPlayerBankStatisticById(id);
    }


}