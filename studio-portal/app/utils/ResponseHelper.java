package utils;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
public class ResponseHelper {
    private static final Logger logger = Logger.getLogger(ResponseHelper.class);

    public static String writeSuccessResponse(Map<String, Object> resultMap){
        ResponseModel res = new ResponseModel(Constant.RESPONSE_OK, resultMap);
        String msg = JSON.toJSONString(res);
        logger.debug("result : \t" + msg);
        return msg;
    }

    public static String writeFailureResponse(Integer status, Map<String, Object> resultMap) {
        ResponseModel res = new ResponseModel(status, resultMap);
        String msg = JSON.toJSONString(res);
        logger.debug("result : \t" + msg);
        return msg;
    }

    public static String writeFailureResponse(Map<String, Object> resultMap) {
        ResponseModel res = new ResponseModel(Constant.RESPONSE_FAILED, resultMap);
        String msg = JSON.toJSONString(res);
        logger.debug("result : \t" + msg);
        return msg;
    }

    public static String writeResponseWithErrorMessage(ErrorMessage errorMessage) {
        Map<String, Object> resultMap = new HashMap<>();
        if (errorMessage.hasError()){
            for (Map.Entry err : errorMessage.entrySet()){
                resultMap.put(err.getKey().toString(), err.getValue());
            }
        }
        return writeFailureResponse(resultMap);
    }

    public static String writeResponseWithErrorMessage(Integer status, ErrorMessage errorMessage) {
        Map<String, Object> resultMap = new HashMap<>();
        if (errorMessage.hasError()){
            for (Map.Entry err : errorMessage.entrySet()){
                resultMap.put(err.getKey().toString(), err.getValue());
            }
        }
        return writeFailureResponse(status, resultMap);
    }
}
