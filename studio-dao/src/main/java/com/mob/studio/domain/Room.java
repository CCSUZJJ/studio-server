package com.mob.studio.domain;

/**
 * @author: Zhang.Min
 * @since: 2016/3/21
 * @version: 1.7
 */
public class Room {
    private Long id;
    private String title;
    private Long viewers;
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getViewers() {
        return viewers;
    }

    public void setViewers(Long viewers) {
        this.viewers = viewers;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
