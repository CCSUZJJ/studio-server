package controllers;

import actions.AuthAction;
import actions.CheckBodyAnnotation;
import com.mob.studio.service.LiveService;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/18
 * @version: 1.7
 */
@With(AuthAction.class)
@CheckBodyAnnotation({"playerId"})
public class Studio extends Controller {
    private static final Logger logger = Logger.getLogger(Studio.class);

    private static LiveService liveService = ServiceHelper.getLiveService();

    public static Result open(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        String title = RequestHelper.getBodyParam(request(), "title", String.class);
        ErrorMessage errorMessage = Validate.checkRoomOnline(playerId);
        if(errorMessage.hasError()){
            return ok(ResponseHelper.writeResponseWithErrorMessage(Constant.RESPONSE_NOT_MODIFIED, errorMessage));
        }
        Integer roomId = liveService.openRoom(playerId, title);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("roomId",roomId);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }



}
