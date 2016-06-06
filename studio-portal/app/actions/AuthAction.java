package actions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import utils.Constant;
import utils.EncodeHelper;
import utils.ResponseHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/15
 * @version: 1.7
 */
public class AuthAction extends Action.Simple {
    private static final Logger logger = Logger.getLogger(AuthAction.class);


    @Override
    public F.Promise<SimpleResult> call(Http.Context context) throws Throwable {
        try {
            Http.Request req = context.request();
            String signature = req.getHeader("signature");
            String isPlain = req.getHeader("is_plain");
            if (isPlain != null && signature !=null) {
                String timestamp = getBody(isPlain, req.body(), "timestamp");
                String nonce = getBody(isPlain, req.body(), "nonce");
                String seed = getBody(isPlain, req.body(), "seed");
                if (!validateSignature(seed, signature, timestamp, nonce)) {
                    logger.info("signature is not correct.");
                    Map<String,Object> resultMap = new HashMap<>();
                    resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "signature is not correct.");
                    return F.Promise.<SimpleResult>pure(ok(ResponseHelper.writeFailureResponse(resultMap)));
                }
            } else {
                Map<String,Object> resultMap = new HashMap<>();
                resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "signature or is_plain is not exists");
                return F.Promise.<SimpleResult>pure(ok(ResponseHelper.writeFailureResponse(resultMap)));
            }
        }catch (Exception e) {
            return F.Promise.<SimpleResult>pure(badRequest(ExceptionUtils.getStackTrace(e)));
        }
        return delegate.call(context);
    }

    private String getBody(String isPlain,Http.RequestBody reqBody,String key) throws Throwable{
        if (isPlain.equals("1")) {
            Map<String,Object> bodyMap = JSON.parseObject(reqBody.asText().trim(), new TypeReference<Map<String,Object>>() {});
            return bodyMap.get(key).toString();

        }else {
            byte[] raw = EncodeHelper.Base64Decode(reqBody.asText().trim());
            byte[] decrypt_raw = EncodeHelper.AES128Decrypt(Constant.AES_KEY, raw);
            Map<String,Object> bodyMap = JSON.parseObject(decodeByte(decrypt_raw), new TypeReference<Map<String,Object>>() {});
            return bodyMap.get(key).toString();
        }
    }

    private String decodeByte(byte[] data) throws Throwable{
        return new String(data, "UTF-8").trim();
    }


    private boolean validateSignature(String seed,String signature,String timestamp,String nonce){
        boolean result = false;
        String[] str = { seed, timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String inputStr = str[0] + str[1] + str[2];
        logger.debug("before encode:" + inputStr);
        String encode = EncodeHelper.encode(EncodeHelper.ALGORITHM_SHA1, inputStr);
        logger.debug("after encode:" + encode);
        if (signature != null && signature.equals(encode)) {
            result = true;
        }
        return result;
    }
}
