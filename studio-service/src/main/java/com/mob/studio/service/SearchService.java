package com.mob.studio.service;

import java.util.List;
import java.util.Map;

/**
 * User: jianwl
 * Date: 2016/3/22
 * Time: 10:58
 */
public interface SearchService {
    List<Map<String,Object>> search(Long playId,String nick,Integer pageNo,Integer pageSize);
}
