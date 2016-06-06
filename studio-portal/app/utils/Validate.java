package utils;


import com.mob.studio.domain.Room;
import com.mob.studio.service.LiveService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
public class Validate {
    private static final Logger logger = Logger.getLogger(Validate.class);
    private static final LiveService liveService = ServiceHelper.getLiveService();

    public static ErrorMessage login(Map<String,Object> bodyMap) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        if (!bodyMap.containsKey("aliasId")){
            error.add(Constant.ERROR_KEY_REQUIRED + "aliasId");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        if (!bodyMap.containsKey("aliasType")){
            error.add(Constant.ERROR_KEY_REQUIRED + "aliasType");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        if (!bodyMap.containsKey("nick")){
            error.add(Constant.ERROR_KEY_REQUIRED + "nick");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }

    public static ErrorMessage checkSearchKey(Map<String,Object> bodyMap) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        if (!bodyMap.containsKey("key")){
            error.add(Constant.ERROR_KEY_REQUIRED + "key");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }

    public static ErrorMessage checkPlayerId(Map<String,Object> bodyMap) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        if (!bodyMap.containsKey("playerId")){
            error.add(Constant.ERROR_KEY_REQUIRED + "playerId");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }

    public static ErrorMessage checkRoomOnline(Long playerId) {
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        Room room = liveService.findOnlineRoomByPlayerId(playerId);
        if (room != null){
            error.add(Constant.ERROR_KEY_EXISTS + room.getId());
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }

    public static ErrorMessage checkItem(Map<String,Object> bodyMap){
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        if (!bodyMap.containsKey("itemId")){
            error.add(Constant.ERROR_KEY_REQUIRED + "itemId");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }

    public static ErrorMessage checkQuantity(Map<String,Object> bodyMap){
        ErrorMessage errorMessage = new ErrorMessage();
        List<String> error = new ArrayList<>();
        if (!bodyMap.containsKey("quantity")){
            error.add(Constant.ERROR_KEY_REQUIRED + "quantity");
            errorMessage.put(Constant.ERROR_DEFAULT_MESSAGE , error);
        }
        return errorMessage;
    }



    private static boolean isLong(String str) {
        try {
            Long.parseLong(str);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
