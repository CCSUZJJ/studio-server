package actions;

import org.apache.log4j.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import utils.ErrorMessage;
import utils.RequestHelper;
import utils.ResponseHelper;
import utils.Validate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/30
 * @version: 1.7
 */
public class CheckBodyAction extends Action<CheckBodyAnnotation> {
    private static Logger logger = Logger.getLogger(CheckBodyAction.class);

    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        String[] values = configuration.value();
        Map<String,Object> bodyMap = RequestHelper.parseBody(ctx.request());
        List l = Arrays.asList(values);
        ErrorMessage errorMessage = null;
        if (l.contains("playerId")) {
            errorMessage = Validate.checkPlayerId(bodyMap);
        }
        else if (l.contains("key")) {
            errorMessage.add(Validate.checkSearchKey(bodyMap));
        }
        else if (l.contains("itemId")){
            errorMessage.add(Validate.checkItem(bodyMap));
        }
        else if (l.contains("quantity")){
            errorMessage.add(Validate.checkQuantity(bodyMap));
        }
        if (errorMessage != null && errorMessage.hasError()) {
            return F.Promise.<SimpleResult>pure(ok(ResponseHelper.writeResponseWithErrorMessage(errorMessage)));
        }
        return delegate.call(ctx);
    }
}
