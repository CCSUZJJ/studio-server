package com.mob.utils;

import com.lamfire.hydra.Message;
import com.lamfire.hydra.MessageFactory;
import com.lamfire.hydra.Session;
import com.lamfire.jspp.*;
import com.lamfire.utils.Bytes;
import org.apache.log4j.Logger;

/**
 * 工具类-返回响应
 * User: panal
 * Date: 2016/3/24
 * Time: 12:13
 */
public class ResponseHelper {

    private static final Logger LOGGER = Logger.getLogger(ResponseHelper.class);

    /**
     * 发送错误信息
     * @param session
     * @param jspp
     * @param errorCode
     * @param errorBody
     */
    public static void sendError(Session session, JSPP jspp, int errorCode, String errorBody) {
        ERROR error = new ERROR();
        error.setCode(errorCode);
        error.setBody(errorBody);
        jspp.setType("error");
        jspp.setError(error);
        Message message = MessageFactory.message(0, 0, JSPPUtils.encode(jspp));
        session.send(message);
        LOGGER.info("Return error message: " + Bytes.toString(message.content()));
    }

    /**
     * 发送错误信息
     * @param session
     * @param jspp
     * @param error
     */
    public static void sendError(Session session, JSPP jspp, ERROR error) {
        jspp.setType("error");
        jspp.setError(error);
        Message message = MessageFactory.message(0, 0, JSPPUtils.encode(jspp));
        session.send(message);
        LOGGER.info("Return error message: " + Bytes.toString(message.content()));
    }

    /**
     * 主播已离开，此房间没有被订阅
     * @param session
     * @param fromPid
     * @param toGid
     */
    public static void sendUnsubscribedRoomMsg(Session session, String fromPid, String toGid) {
        PRESENCE presence = new PRESENCE();
        presence.setFrom(fromPid);
        presence.setTo(toGid);
        presence.setType(PRESENCE.TYPE_UNSUBSCRIBED);
        Message msg = MessageFactory.message(0, 0, JSPPUtils.encode(presence));
        session.send(msg);
        LOGGER.info("Return unsubscribe message : " + Bytes.toString(msg.content()));
    }
}
