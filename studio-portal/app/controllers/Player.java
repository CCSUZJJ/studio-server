package controllers;

import actions.AuthAction;
import actions.CheckBodyAnnotation;
import com.mob.studio.service.PlayerBankService;
import com.mob.studio.service.PlayerService;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Constant;
import utils.RequestHelper;
import utils.ResponseHelper;
import utils.ServiceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/22
 * @version: 1.7
 */
@With(AuthAction.class)
public class Player extends Controller {
    private static final Logger logger = Logger.getLogger(Player.class);

    private static PlayerService playerService = ServiceHelper.getPlayerService();
    private static PlayerBankService playerBankService = ServiceHelper.getPlayerBankService();

    @CheckBodyAnnotation({"playerId"})
    public static Result portrait(Long targetId){
        Long currentPlayerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Map<String,Object> detail = playerService.getPortrait(targetId, currentPlayerId);
        return ok(ResponseHelper.writeSuccessResponse(detail));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result home(Long targetId){
        Long currentPlayerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Map<String,Object> detail = playerService.getProfileDetail(targetId, currentPlayerId);
        return ok(ResponseHelper.writeSuccessResponse(detail));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result follow(Long targetId) {
        Long currentPlayerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);

        List<Map<String,Object>> playerMaps = playerService.findFollowerByPlayerId(targetId, currentPlayerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("list", playerMaps);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result fans(Long targetId) {
        Long currentPlayerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);

        List<Map<String,Object>> playerMaps = playerService.findFansByPlayerId(targetId, currentPlayerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("list", playerMaps);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    public static Result gift_rank_list(Long targetId){
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        List<Map<String,Object>> giftRankList = playerBankService.findGiftRankByPlayerId(targetId, pageNo, pageSize);
        Long point = playerBankService.getPlayerPointById(targetId);
//        Long point = playerBankService.getPlayersFlowersById(targetId);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        resultMap.put("giftTop", giftRankList);
        resultMap.put("point", point);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

}
