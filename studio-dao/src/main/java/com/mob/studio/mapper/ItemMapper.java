package com.mob.studio.mapper;

import com.mob.studio.domain.Item;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public interface ItemMapper {
    Item getItemById(String id);
    void insertItem(Item item);
    void updateItem(Item item);

    List<Item> getItemAll();
}
