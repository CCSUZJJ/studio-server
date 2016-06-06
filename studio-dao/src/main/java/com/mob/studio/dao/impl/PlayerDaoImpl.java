package com.mob.studio.dao.impl;

import com.mob.studio.dao.PlayerDao;
import com.mob.studio.domain.Player;
import com.mob.studio.mapper.PlayerMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public class PlayerDaoImpl implements PlayerDao {

    @Override
    public Player insertPlayer(SqlSession sqlSession, Player player) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Long num_record_inserted =  mapper.insertPlayer(player);
        return player;
    }

    @Override
    public void updatePlayer(SqlSession sqlSession, Player player) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        mapper.updatePlayer(player);
    }

    @Override
    public Player getPlayerByAliasId(SqlSession sqlSession, String aliasId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("aliasId", aliasId);
        return mapper.getPlayerByAliasId(map);
    }

    @Override
    public Player getPlayerById(SqlSession sqlSession, Long id) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
//        Map<String,Object> map = new HashMap<>();
//        map.put("playerId",id);
        return mapper.getPlayerById(id);
    }

    @Override
    public List<Player> findPlayerByFuzzyNick(SqlSession sqlSession, String nick, Integer pageNo, Integer pageSize) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Integer start = (pageNo.intValue() - 1) * pageSize.intValue();
        Map<String,Object> map = new HashMap<>();

        map.put("nick",nick);
        map.put("start",start);
        map.put("pageSize",pageSize);
        return mapper.findPlayerByFuzzyNick(map);
    }

    @Override
    public List<Long> getFollowersIdById(SqlSession sqlSession, Long playerId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        return mapper.getFollowersIdById(playerId);
    }

    @Override
    public List<Long> getFansIdById(SqlSession sqlSession, Long playerId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        return mapper.getFansIdById(playerId);
    }

    @Override
    public List<Long> getBlackListById(SqlSession sqlSession, Long playerId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        return mapper.getBlackListById(playerId);
    }

    @Override
    public void insertFollower(SqlSession sqlSession, Long playerId, Long followerId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Map<String,Long> map = new HashMap<String,Long>();
        map.put("playerId", playerId);
        map.put("followerId", followerId);
        mapper.insertFollower(map);
    }

    @Override
    public void insertBlackList(SqlSession sqlSession, Long playerId, Long blackId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Map<String,Long> map = new HashMap<String,Long>();
        map.put("playerId", playerId);
        map.put("blackId", blackId);
        mapper.insertBlackList(map);
    }

    @Override
    public void deleteFollower(SqlSession sqlSession, Long playerId, Long followerId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Map<String,Long> map = new HashMap<String,Long>();
        map.put("playerId", playerId);
        map.put("followerId", followerId);
        mapper.deleteFollower(map);
    }

    @Override
    public void deleteBlackList(SqlSession sqlSession, Long playerId, Long blackId) {
        PlayerMapper mapper = sqlSession.getMapper(PlayerMapper.class);
        Map<String,Long> map = new HashMap<String,Long>();
        map.put("playerId", playerId);
        map.put("blackId", blackId);
        mapper.deleteBlackList(map);
    }
}
