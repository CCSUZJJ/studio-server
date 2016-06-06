package controllers;

import actions.AuthAction;
import actions.CheckBodyAnnotation;
import com.sig.utils.SignatureUtils;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.RequestHelper;



/**
 * @author: Zhang.Min
 * @since: 2016/3/31
 * @version: 1.7
 */
@With(AuthAction.class)
public class Tencent extends Controller {
    private static final Logger logger = Logger.getLogger(Tencent.class);

    @CheckBodyAnnotation({"playerId"})
    public static Result sign(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        logger.info("sever 2 server ask sign..." + playerId);
        String sig = SignatureUtils.getSignature(playerId);
        logger.info("sever 2 server get sign...\n" + sig);
        return ok(sig);
    }
}
