package com.mob.groupchats;

import com.lamfire.hydra.*;
import com.lamfire.hydra.netty.NettySessionMgr;
import com.lamfire.jspp.*;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.mob.services.ChatService;
import com.mob.utils.ResponseHelper;
import com.mob.utils.ErrorMsg;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 16-2-16
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */
public class GroupChat implements SessionClosedListener{
    private static final Logger LOGGER = Logger.getLogger(GroupChat.class);
    private static final String MEMBER_KEY = "SESSION_MEMBER";
    private final SessionMgr sessionMgr = new NettySessionMgr();
    private String id;
    private String anchorId;
    private boolean isOnline;

    public GroupChat (String id, String anchorId){
        this.id = id;
        this.anchorId = anchorId;
        this.isOnline = true;
    }

    public void addGroupMember(Session session,GroupMember member){
        session.attr(MEMBER_KEY,member);
        session.addCloseListener(this);
        sessionMgr.add(session);
    }

    public Collection<GroupMember> getGroupMembers(){
        List<GroupMember> members = Lists.newArrayList();
        Collection<Session> sessions = sessionMgr.all();
        for(Session s : sessions){
            GroupMember member = (GroupMember)s.attr(MEMBER_KEY);
            members.add(member);
        }
        return members;
    }

    public boolean exists(String memberId){
        Collection<Session> sessions = sessionMgr.all();
        for(Session s : sessions){
            GroupMember member = (GroupMember)s.attr(MEMBER_KEY);
            if(memberId.equalsIgnoreCase(member.getId())){
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyMembers(){
        return sessionMgr.isEmpty();
    }

    public void pushDataFormatInvalidError(Session session) {
        PRESENCE p = new PRESENCE();
        p.setFrom(id);
        p.setType(PRESENCE.TYPE_ERROR);
        p.put("body","Your send data format invalid");

        Message sendMessage = MessageFactory.message(0,0, JSPPUtils.encode(p));
        session.send(sendMessage);
    }

    public void pushMemberRepeatEnterGroupError(Session session,String memberId){
        PRESENCE p = new PRESENCE();
        p.setFrom(id);
        p.setTo(memberId);
        p.setType(PRESENCE.TYPE_ERROR);
        p.put("body","Refuse to repeat join the group");

        Message sendMessage = MessageFactory.message(0,0, JSPPUtils.encode(p));
        session.send(sendMessage);
    }

    public void pushGroupMembers(Session session,String memberId){
        PRESENCE p = new PRESENCE();
        p.setFrom(id);
        p.setTo(memberId);
        p.setType(PRESENCE.TYPE_AVAILABLE);
        p.put("members", getGroupMembers());
//        p.put("giftPoint",  ChatService.getPlayerGift(this.anchorId)); //推送直播间的映票数
        Message sendMessage = MessageFactory.message(0,0, JSPPUtils.encode(p));
        session.send(sendMessage);
        LOGGER.info("pushGroupMembers : " + p);
    }

    /**
     * 更新直播间鲜花数量
     * @param message
     * @return 主播现有礼物值
     */
    public long sendGift(MESSAGE message){
        Long giftPoint = ChatService.getItemPoint(message.getBody());
        Long result = ChatService.sendGift(message.getFrom(), this.anchorId, giftPoint);
        return result;
    }


    public void onReceivedMessage(Session session ,MESSAGE message){
        try{
            message.put("profile",session.attr(MEMBER_KEY));
            Collection<Session> sessions = sessionMgr.all();
            for(Session s : sessions){
                Message sendMessage = MessageFactory.message(0,0,JSPPUtils.encode(message));
                s.send(sendMessage);
            }
            LOGGER.info("Return message: " + message);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public void onMemberEnterGroup(Session session ,GroupMember member){
        PRESENCE p = new PRESENCE();
        p.setFrom(member.getId());
        p.setTo(id);
        p.setType(PRESENCE.TYPE_SUBSCRIBED);
        p.put("profile",member);
        Collection<Session> sessions = sessionMgr.all();
        for(Session s : sessions){
            Message sendMessage = MessageFactory.message(0,0,JSPPUtils.encode(p));
            s.send(sendMessage);
            LOGGER.info("onMemberEnterGroup: " + p);
        }
        LOGGER.info("[" + member.getId() +"] joined group : " + id);
    }

    /**
     * 用户离开房间
     * @param session
     */
    public void onMemberUnsubscribe(Session session) {
        session.removeCloseListener(this);
        session.close();
        unsubscribeHandler(session);
        LOGGER.info("[onMemberUnsubscribe] : member has left the room");
    }

    /**
     * 广播某某离开房间消息
     * @param memberId
     */
    public void onMemberLeaveGroup(String memberId){
        PRESENCE p = new PRESENCE();
        p.setFrom(memberId);
        p.setTo(id);
        p.setType(PRESENCE.TYPE_UNSUBSCRIBED);
        Collection<Session> sessions = sessionMgr.all();
        for(Session s : sessions){
            Message sendMessage = MessageFactory.message(0,0,JSPPUtils.encode(p));
            s.send(sendMessage);
        }
        LOGGER.info("[" + memberId + "] leaved group : " + id);
    }

    /**
     * 用户离开房间处理
     * @param session
     */
    private void unsubscribeHandler(Session session) {
        GroupMember member = (GroupMember)session.attr(MEMBER_KEY);
        if (member.getId().equalsIgnoreCase(this.anchorId)) {
            isOnline = false;
            onMemberLeaveGroup(member.getId());
            LOGGER.info("anchor[" + this.anchorId +  "] has left room[" + this.id + "]");
            Collection<Session> sessions = sessionMgr.all();
            for (Session s : sessions) {//服务器主动断掉房间其他成员的session会话
                if (!s.equals(session)) {
//                    LOGGER.info("close session , seesion-id : " + s.getRemoteAddress());
                    s.removeCloseListener(this);
                    s.close();
                }
            }
            //同步房间相关的信息
            ChatService.syncOnAnchorLeave(this.anchorId, this.id);

        } else if (isOnline) {
            onMemberLeaveGroup(member.getId());
            ChatService.updateRoomViewers(this.id, -1d);
        }
    }


    @Override
    public void onClosed(Session session) {
        unsubscribeHandler(session);
        LOGGER.info("[onClosed] : member has left the room");
    }

    public String getId() {
        return id;
    }

    public String getAnchorId() { return anchorId; }
}
