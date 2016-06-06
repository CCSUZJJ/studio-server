package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.log4j.Logger;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
public class RequestHelper {
    private static final Logger logger = Logger.getLogger(RequestHelper.class);

    public static Map<String, Object>  parseBody(Http.Request request) {
        Map<String, Object> bodyMap = new HashMap<>();
        try {
            String isPlain = request.getHeader("is_plain");
            String text = request.body().asText();
            if (isPlain.equals("1") && text != null && !text.trim().isEmpty()){
                bodyMap = JSON.parseObject(text, new TypeReference<Map<String, Object>>() {
                });
                return bodyMap;
            }else {
                if (text != null && !text.trim().isEmpty()) {
                    String plainText = new String(EncodeHelper.AES128Decrypt(Constant.AES_KEY, EncodeHelper.Base64Decode(text)),"UTF-8");
                    bodyMap = JSON.parseObject(plainText, new TypeReference<Map<String, Object>>() {
                    });
                    return bodyMap;
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return bodyMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBodyParam(Http.Request request,String key, Class<T> clazz, T defaultObj) {
        Map<String, Object> bodyMap = parseBody(request);
        if (bodyMap.containsKey(key)){
            if (Integer.TYPE == clazz){
                return (T) Integer.valueOf(bodyMap.get(key).toString());
            } else if (Long.TYPE == clazz){
                return (T) Long.valueOf(bodyMap.get(key).toString());
            } else if (String.class == clazz){
                return (T) bodyMap.get(key).toString();
            }
        }else {
            return defaultObj;
        }
        return defaultObj;
    }

    public static <T> T getBodyParam(Http.Request request,String key, Class<T> clazz) {
        return getBodyParam(request,key, clazz, null);
    }
}
