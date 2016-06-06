package com.mob.studio.service;

import com.mob.studio.domain.Player;

import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public interface PlayerService {
    Player login(Player player);
    Player regist(Player player);
    Player update(Player player);
    Player getPlayerByAliasId(String aliasId);
//    Player findPlayerById(Long id);
    List<Player> findPlayerByFuzzyNick(String nick,Integer pageNo,Integer pageSize);
//    List<Player> findPlayerByNick(String nick);
    Player findPlayerInfoById(Long id);

    Boolean doFollow(Long playerId,Long followerId);
    Boolean doBlack(Long playerId, Long blackId);

    Boolean doUnfollow(Long playerId,Long followerId);
    Boolean doUnblack(Long playerId,Long followerId);

    Boolean isFollower(Long playerId, Long targetId);

    Boolean isBlack(Long playerId, Long targetId);

    Map<String,Object> getMyProfileDetail(Long playerId);

    List<Map<String,Object>> findFollowerByPlayerId(Long playerId,Integer pageNo,Integer pageSize);
    List<Map<String,Object>> findFollowerByPlayerId(Long playerId);

    List<Map<String,Object>> findFollowerByPlayerId(Long playerId, Long currentPlayerId,Integer pageNo,Integer pageSize);
    List<Map<String,Object>> findFollowerByPlayerId(Long playerId, Long currentPlayerId);

    List<Map<String,Object>> findFansByPlayerId(Long playerId, Long currentPlayerId,Integer pageNo,Integer pageSize);
    List<Map<String,Object>> findFansByPlayerId(Long playerId, Long currentPlayerId);

    List<Map<String,Object>> findFansByPlayerId(Long playerId,Integer pageNo,Integer pageSize);
    List<Map<String,Object>> findFansByPlayerId(Long playerId);
    List<Player> findBlackListByPlayerId(Long playerId);
    List<Player> findBlackListByPlayerId(Long playerId,Integer pageNo,Integer pageSize);

    Map<String,Object> getPortrait(Long playerId, Long currentPlayerId);
    Map<String,Object> getProfileDetail(Long playerId, Long currentPlayerId);

    List<Long> pageList(List<Long> list,Integer pageNo,Integer pageSize);
    /**
     *
     * @param
     * @return 达人榜
     */
    List<Map<String,Object>> talentRank(Long playerId, Integer pageNo,Integer pageSize);

}
