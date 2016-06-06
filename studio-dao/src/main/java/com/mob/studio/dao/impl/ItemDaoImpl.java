package com.mob.studio.dao.impl;

import com.mob.studio.dao.ItemDao;
import com.mob.studio.domain.Item;
import com.mob.studio.mapper.ItemMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public class ItemDaoImpl implements ItemDao {
    @Override
    public Item getItemById(SqlSession sqlSession, String id) {
        ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
        return mapper.getItemById(id);
    }

    @Override
    public void insertItem(SqlSession sqlSession, Item item) {
        ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
        mapper.insertItem(item);
    }

    @Override
    public void updateItem(SqlSession sqlSession, Item item) {
        ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
        mapper.updateItem(item);
    }

    @Override
    public List<Item> getItemAll(SqlSession sqlSession) {
        ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
        return mapper.getItemAll();
    }
}
