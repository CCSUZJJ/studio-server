package com.mob.studio.dao;

import com.mob.studio.domain.Item;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public interface ItemDao {
    Item getItemById(SqlSession sqlSession, String id);
    void insertItem(SqlSession sqlSession, Item item);
    void updateItem(SqlSession sqlSession, Item item);

    List<Item> getItemAll(SqlSession sqlSession);
}
