package com.mob.studio.service.impl;

import com.mob.studio.domain.Player;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.PlayerService;
import com.mob.studio.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 11:36
 */
public class SearchServiceImpl implements SearchService {

    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;

    @Override
    public List<Map<String,Object>> search(Long playId, String nick, Integer pageNo, Integer pageSize) {
        List<Player> list = playerService.findPlayerByFuzzyNick(nick,pageNo,pageSize);

        List<Map<String,Object>> result = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for(Player player : list){
                if (player != null) {
                    Boolean isFollow = playerService.isFollower(playId, player.getId());
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("player", player);
                    resultMap.put("isFollow", isFollow);
                    result.add(resultMap);
                }
            }
        }
        return result;
    }
}
