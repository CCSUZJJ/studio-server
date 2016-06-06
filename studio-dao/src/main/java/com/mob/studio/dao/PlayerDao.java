package com.mob.studio.dao;

import com.mob.studio.domain.Player;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public interface PlayerDao {

    Player insertPlayer(SqlSession sqlSession, Player player);

    void updatePlayer(SqlSession sqlSession, Player player);

    Player getPlayerById(SqlSession sqlSession,Long id);

    List<Long> getFollowersIdById(SqlSession sqlSession, Long playerId);

    List<Long> getFansIdById(SqlSession sqlSession, Long playerId);

    List<Long> getBlackListById(SqlSession sqlSession, Long playerId);

    void insertFollower(SqlSession sqlSession, Long playerId, Long followerId);

    void insertBlackList(SqlSession sqlSession, Long playerId, Long blackId);

    void deleteFollower(SqlSession sqlSession, Long playerId, Long followerId);

    void deleteBlackList(SqlSession sqlSession, Long playerId, Long followerId);

    Player getPlayerByAliasId(SqlSession sqlSession,String aliasId);

    List<Player> findPlayerByFuzzyNick(SqlSession sqlSession,String nick,Integer pageNo,Integer pageSize);
}
