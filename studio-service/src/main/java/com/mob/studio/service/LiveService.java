package com.mob.studio.service;

import com.mob.studio.domain.Room;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/3/17
 * @version: 1.7
 */
public interface LiveService {
    Integer openRoom(Long playerId, String title);

    List<Room> hotRank(Integer pageNo, Integer pageSize);

    List<Room> hotRank(Long playerId, Integer pageNo, Integer pageSize);

    List<Room> followerLive(Long playerId, Integer pageNo, Integer pageSize);

    Room findOnlineRoomByPlayerId(Long playerId);
}
