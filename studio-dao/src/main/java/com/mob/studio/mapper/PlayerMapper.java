package com.mob.studio.mapper;

import com.mob.studio.domain.Player;

import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public interface PlayerMapper {


    /**
     * insert a player.
     *
     * @param player
     * @return Player
     */
    Long insertPlayer(Player player);


//    Player getPlayerById(Map map);

    void updatePlayer(Player player);

    Player getPlayerById(Long id);


    Player getPlayerByAliasId(Map map);

    List<Player> findPlayerByFuzzyNick(Map map);

    List<Long> getFollowersIdById(Long id);

    List<Long> getFansIdById(Long id);

    List<Long> getBlackListById(Long id);

    void insertFollower(Map<String,Long> map);

    void insertBlackList(Map<String,Long> map);

    void deleteFollower(Map<String,Long> map);

    void deleteBlackList(Map<String,Long> map);
}
