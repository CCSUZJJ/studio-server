package com.lamfire.test;

import com.lamfire.json.JSON;
import com.mob.groupchats.GroupMember;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 16-2-16
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        GroupMember member = new GroupMember();
        member.setId("member001");
        member.setNick("lamfire");
        member.setAvatar("http://www.lamfire.com/avatar.png");
        member.setSex(1);

        System.out.println(JSON.toJSONString(member));
    }
}
