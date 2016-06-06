package com.mob.groupchats;

import com.lamfire.hydra.*;
import com.lamfire.json.JSON;
import com.lamfire.jspp.*;
import com.lamfire.logger.Logger;
import com.lamfire.utils.*;
import com.mob.services.ChatService;
import com.mob.utils.ResponseHelper;
import com.mob.utils.ErrorMsg;
import com.sun.org.apache.bcel.internal.generic.I2F;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 16-2-16
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class GroupServer implements MessageReceivedListener,SessionCreatedListener,SessionClosedListener {
    private static final Logger LOGGER = Logger.getLogger(GroupServer.class);
    private final Map<String,GroupChat> groupChats = Maps.newLinkedHashMap();
    private final ScheduledExecutorService service = Threads.newSingleThreadScheduledExecutor(new ThreadFactory("emptyGroupClean"));
    private static ChatService chatService = new ChatService();
    private final String host;
    private final int port;
    private Hydra hydra;
    private static final String SERVICE_KEY = "ENTER_ROOM_VALIDATE";

    public GroupServer(String host,int port){
        this.host = host;
        this.port = port;
    }

    public synchronized void startup(){
        if(hydra != null){
            return;
        }

        HydraBuilder builder = new HydraBuilder();
        builder.bind(host).port(port).messageReceivedListener(this).threads(8);
        hydra = builder.build();
        hydra.startup();
        service.scheduleWithFixedDelay(emptyGroupClean, 1, 1, TimeUnit.MINUTES);
        LOGGER.info("startup on - " + host +":" + port);
    }

    public synchronized void shutdown(){
        if(hydra == null){
            return;
        }
        hydra.shutdown();
        hydra = null;
        LOGGER.info("shutdown on - " + host +":" + port);
    }

    private void validate (String groupId, String memberId, Session session, SERVICE message) {
        if (message.getNs().equals(SERVICE_KEY)) {
            ERROR error = null;
            GroupChat groupChat = groupChats.get(groupId);
            if (!ChatService.existRoom(groupId)) {
                error = new ERROR(ErrorMsg.s405, String.format(ErrorMsg.getMsg(405), groupId));
            } else if (groupChat != null && groupChat.exists(memberId)) {
                error = new ERROR(ErrorMsg.s407, String.format(ErrorMsg.getMsg(407), groupId));
            }

            if (error != null) {
                ResponseHelper.sendError(session, message, error);
                session.close();
                LOGGER.info("validate error : " + error.toJSONString());
            } else {
                message.setType("ok");
                Message msg = MessageFactory.message(0, 0, JSPPUtils.encode(message));
                session.send(msg);
                LOGGER.info("validate ok : " + Bytes.toString(msg.content()));
            }
        }
    }

    private void handleMessage(String groupId,Session session,MESSAGE message){
        if (ChatService.existRoom(groupId)){
            GroupChat groupChat = groupChats.get(groupId);
            if(groupChat == null){
                LOGGER.error("group["+groupId+"] not found,send message failed - " +message);
                ERROR error = new ERROR(ErrorMsg.s401, String.format(ErrorMsg.getMsg(401), groupId));
                ResponseHelper.sendError(session, message, error);
//                ResponseHelper.sendError(session, message, ErrorMsg.s401, String.format(ErrorMsg.getMsg(401), groupId));
                return;
            }
            LOGGER.info("receive Message : " + message);
            groupChat.onReceivedMessage(session,message);
        } else {
            ResponseHelper.sendError(session, message, ErrorMsg.s405, String.format(ErrorMsg.getMsg(405), groupId));
            session.close();
        }
    }

    private void handlePresence(String groupId, Session session, PRESENCE presence){
        if (ChatService.existRoom(groupId)) {
            GroupChat groupChat = groupChats.get(groupId);
            //离开房间
            if (presence.getType().equalsIgnoreCase(PRESENCE.TYPE_UNSUBSCRIBE) && groupChat != null) {
                groupChat.onMemberUnsubscribe(session);
                return;
            }
            //进入房间
            if (presence.getType().equalsIgnoreCase(PRESENCE.TYPE_SUBSCRIBE)) {
                handleSubscribe(groupId, session, groupChat, presence);
                return;
            }
        } else {
            ResponseHelper.sendError(session, presence, ErrorMsg.s405, String.format(ErrorMsg.getMsg(405), groupId));
            session.close();
        }
    }

    /**
     * 用户进入房间
     * @param groupId
     * @param session
     * @param groupChat
     * @param presence
     */
    private void handleSubscribe(String groupId, Session session, GroupChat groupChat, PRESENCE presence) {
        JSON json = presence.getJSONObject("profile");
        GroupMember member = json.toJavaObject(GroupMember.class);
        if (groupChat == null) {
            groupChat = new GroupChat(groupId, member.getId());
            groupChats.put(groupId,groupChat);
            chatService.initRoom(groupId, member.getId());
        }
        try {
            if(!groupChat.exists(member.getId())){
                chatService.updateRoomViewers(groupId, 1d); //进入房间，房间的在线观看人数相应加1
                groupChat.addGroupMember(session,member);
                groupChat.onMemberEnterGroup(session,member);
                groupChat.pushGroupMembers(session, member.getId());
            } else {
                ResponseHelper.sendError(session, presence, ErrorMsg.s407, String.format(ErrorMsg.getMsg(407), groupId));
                session.close();
            }
            return;
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /*private void handlePresence2(String groupId,Session session,PRESENCE presence){
        JSON json = presence.getJSONObject("profile");
        GroupMember member = json.toJavaObject(GroupMember.class);
        if (ChatService.existRoom(groupId)){
            GroupChat groupChat = groupChats.get(groupId);
            if(groupChat == null){
                groupChat = new GroupChat(groupId, member.getId());
                groupChats.put(groupId,groupChat);
                chatService.initRoom(groupId, member.getId());
            }

            try{
                //进入房间
                if(PRESENCE.TYPE_SUBSCRIBE.equalsIgnoreCase(presence.getType())){
                    if(!groupChat.exists(member.getId())){
                        chatService.updateRoomViewers(groupId, 1d); //进入房间，房间的在线观看人数相应加1
                        groupChat.addGroupMember(session,member);
                        groupChat.onMemberEnterGroup(session,member);
                        groupChat.pushGroupMembers(session, member.getId());
                    }else{
//                        groupChat.pushMemberRepeatEnterGroupError(session,member.getId());
                        ResponseHelper.sendError(session, presence, ErrorMsg.s407, String.format(ErrorMsg.getMsg(407), groupId));
                        session.close();
                    }
                    return;
                }
            }catch (Exception e){
                groupChat.pushDataFormatInvalidError(session);
                LOGGER.error(e);
            }
        } else {
            ResponseHelper.sendError(session, presence, ErrorMsg.s405, String.format(ErrorMsg.getMsg(405), groupId));
            session.close();
        }

    }*/


    @Override
    public void onMessageReceived(Session session, Message message) {
        byte[] bytes = message.content();
        JSPP jspp = JSPPUtils.decode(bytes);
        if(JSPPUtils.getProtocolType(jspp) == ProtocolType.MESSAGE){
            MESSAGE msg = (MESSAGE) jspp;
            String to = msg.getTo();
            handleMessage(to,session,msg);
            return;
        }

        if(JSPPUtils.getProtocolType(jspp) == ProtocolType.PRESENCE){
            PRESENCE msg = (PRESENCE) jspp;
            String to = msg.getTo();
            handlePresence(to,session,msg);
            return;
        }

        if(JSPPUtils.getProtocolType(jspp) == ProtocolType.SERVICE){
            SERVICE msg = (SERVICE) jspp;
            String to = msg.getTo();
            String from = msg.getFrom();
            validate(to, from, session, msg);
        }
    }

    private Runnable emptyGroupClean = new Runnable() {
        @Override
        public void run() {
            List<String> emptyList = Lists.newArrayList();
            for(Map.Entry<String,GroupChat> e : groupChats.entrySet()){
                if(e.getValue().isEmptyMembers()){
//                    ChatService.syncOnAnchorLeave(e.getValue().getAnchorId(), e.getValue().getId());
                    emptyList.add(e.getKey());
                }
            }
            for(String id: emptyList ){
                groupChats.remove(id);
                LOGGER.info("[RemoveEmptyGroup] : " + id);
            }
        }
    };

    @Override
    public void onCreated(Session session) {
        System.out.println("[Created] - " + session);
    }

    @Override
    public void onClosed(Session session) {
        System.out.println("[Closed] - " + session);
    }

}
