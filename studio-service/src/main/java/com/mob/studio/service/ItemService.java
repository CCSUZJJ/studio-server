package com.mob.studio.service;

import com.mob.studio.domain.Item;

import java.util.List;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public interface ItemService {
    Item getItemById(String id);

    List<Item> getItemAll();

    List<Item> refreshItemAll();
}
