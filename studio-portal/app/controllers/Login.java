package controllers;

import actions.AuthAction;
import com.mob.studio.domain.Player;
import com.mob.studio.service.PlayerService;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/15
 * @version: 1.7
 */
@With(AuthAction.class)
public class Login extends Controller {
    private static final Logger logger = Logger.getLogger(Login.class);

    private static PlayerService playerService = ServiceHelper.getPlayerService();

    public static Result login(){
        ErrorMessage errorMessage = Validate.login(RequestHelper.parseBody(request()));
        if(errorMessage.hasError()){
            return ok(ResponseHelper.writeResponseWithErrorMessage(errorMessage));
        }
        Player player = new Player();
        player.setAliasId(RequestHelper.getBodyParam(request(), "aliasId", String.class));
        player.setAliasType(RequestHelper.getBodyParam(request(), "aliasType", Integer.TYPE));
        player.setId(RequestHelper.getBodyParam(request(), "id", Long.TYPE));
        player.setNick(RequestHelper.getBodyParam(request(), "nick", String.class));
        player.setCity(RequestHelper.getBodyParam(request(), "city", String.class));
        player.setSex(RequestHelper.getBodyParam(request(), "sex", Integer.TYPE));
        player.setAvatar(RequestHelper.getBodyParam(request(), "avatar", String.class));
        player.setIntro(RequestHelper.getBodyParam(request(), "intro", String.class));
        Player p = playerService.login(player);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("player",p);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

}
