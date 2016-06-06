package com.mob.studio.service.impl;

import com.mob.studio.dao.ItemDao;
import com.mob.studio.domain.Item;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.ItemService;
import com.mob.studio.util.MybatisUtil;
import com.mob.studio.util.RedisKey;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = Logger.getLogger(ItemServiceImpl.class);

    private static final SqlSessionFactory sqlSessionFactory_Studio_W = MybatisUtil.getSqlSessionFactory_Studio_W();
    @Autowired
    @Qualifier("redisDao")
    private RedisDao redisDao;

    @Autowired
    @Qualifier("itemDao")
    private ItemDao itemDao;

    @Override
    public Item getItemById(String id) {
        Item item = null;
        Boolean isExists = redisDao.exists(RedisKey.ITEM + id);
        if (isExists) {
            item = installItem(id);
        }else {
            item = getItemByIdFromDB(id);
        }
        return item;
    }

    @Override
    public List<Item> getItemAll() {
        List<Item> list = new ArrayList<>();
        Boolean isExists = redisDao.exists(RedisKey.ITEM_ID_LIST);
        if (isExists) {
            Set<String> itemSet = redisDao.sMembers(RedisKey.ITEM_ID_LIST);
            if (itemSet != null && !itemSet.isEmpty()) {
                Iterator<String> it = itemSet.iterator();
                while (it.hasNext()) {
                    String itemId = it.next();
                    Item item = getItemById(itemId);
                    if (item != null) {
                        list.add(item);
                    }else {
                        logger.warn("[DB data not found]\t[item]\tid:" + itemId);
                    }
                }
            }
        } else {
            //get from DB;refresh Cache
            list = refreshItemAll();
        }
        return list;
    }

    @Override
    public List<Item> refreshItemAll() {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        List<Item> list = itemDao.getItemAll(sqlSession);
        sqlSession.close();
        //clean cache
        redisDao.delete(RedisKey.ITEM_ID_LIST);
        //reset
        for (Item item : list) {
            redisDao.sAdd(RedisKey.ITEM_ID_LIST, item.getId());
            redisDao.hset(RedisKey.ITEM + item.getId(), RedisKey.ITEM_NAME, item.getName());
            redisDao.hset(RedisKey.ITEM + item.getId(), RedisKey.ITEM_PRICE, item.getPrice().toString());
            redisDao.hset(RedisKey.ITEM + item.getId(), RedisKey.ITEM_POINT, item.getPoint().toString());
        }
        return list;
    }

    private Item installItem(String id) {
        Item item = null;
        String name = redisDao.hget(RedisKey.ITEM + id, RedisKey.ITEM_NAME);
        String priceStr = redisDao.hget(RedisKey.ITEM + id, RedisKey.ITEM_PRICE);
        String pointStr = redisDao.hget(RedisKey.ITEM + id, RedisKey.ITEM_POINT);
        if (name != null && priceStr != null && pointStr != null) {
            item = new Item();
            item.setId(id);
            item.setName(name);
            item.setPrice(Long.valueOf(priceStr));
            item.setPoint(Long.valueOf(pointStr));
        }
        return item;
    }

    private Item getItemByIdFromDB(String id) {
        SqlSession sqlSession = sqlSessionFactory_Studio_W.openSession();
        Item item = itemDao.getItemById(sqlSession, id);
        sqlSession.close();
        return item;
    }
}
