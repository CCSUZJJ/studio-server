package com.mob.studio.domain;

/**
 * @author: Zhang.Min
 * @since: 2016/3/14
 * @version: 1.7
 */
public class Player {
    private Long id;
    private String nick;
    private String city;
    private Integer sex;
    private String avatar;
    private String intro;
    private String aliasId;
    private Integer aliasType;
    private Long createdOn;
    private Long updatedOn;

    public Player(){}

    public Player(String nick, String city, Integer sex, String avatar, String aliasId, Integer aliasType) {
        this.nick = nick;
        this.city = city;
        this.sex = sex;
        this.avatar = avatar;
        this.aliasId = aliasId;
        this.aliasType = aliasType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public Integer getAliasType() {
        return aliasType;
    }

    public void setAliasType(Integer aliasType) {
        this.aliasType = aliasType;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }


}
