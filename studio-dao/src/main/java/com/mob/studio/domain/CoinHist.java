package com.mob.studio.domain;

/**
 * @author: Zhang.Min
 * @since: 2016/4/19
 * @version: 1.7
 */
public class CoinHist {
    private Long id;
    private Long userId;
    private Long payCoin;
    private Long freeCoin;
    private Long exDate;
    private Long logDate;
    private Long trackId;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPayCoin() {
        return payCoin;
    }

    public void setPayCoin(Long payCoin) {
        this.payCoin = payCoin;
    }

    public Long getFreeCoin() {
        return freeCoin;
    }

    public void setFreeCoin(Long freeCoin) {
        this.freeCoin = freeCoin;
    }

    public Long getExDate() {
        return exDate;
    }

    public void setExDate(Long exDate) {
        this.exDate = exDate;
    }

    public Long getLogDate() {
        return logDate;
    }

    public void setLogDate(Long logDate) {
        this.logDate = logDate;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
