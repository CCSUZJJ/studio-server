package com.mob.studio.domain;

/**
 * @author: Zhang.Min
 * @since: 2016/4/8
 * @version: 1.7
 */
public class PlayerBank {
    private Long playerId;
    private Long coin;
    private Long point;
    private Long coinIn;
    private Long coinOut;
    private Long pointIn;
    private Long pointOut;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Long getCoinIn() {
        return coinIn;
    }

    public void setCoinIn(Long coinIn) {
        this.coinIn = coinIn;
    }

    public Long getCoinOut() {
        return coinOut;
    }

    public void setCoinOut(Long coinOut) {
        this.coinOut = coinOut;
    }

    public Long getPointIn() {
        return pointIn;
    }

    public void setPointIn(Long pointIn) {
        this.pointIn = pointIn;
    }

    public Long getPointOut() {
        return pointOut;
    }

    public void setPointOut(Long pointOut) {
        this.pointOut = pointOut;
    }

    public PlayerBank(){}

    //constructor for default object
    public PlayerBank(Long playerId){
        this.playerId = playerId;
        this.coin = 10000L;
        this.coinIn = 0L;
        this.coinOut = 0L;
        this.point = 0L;
        this.pointIn = 0L;
        this.pointOut = 0L;
    }
}
