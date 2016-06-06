package controllers;

import actions.AuthAction;
import actions.CheckBodyAnnotation;
import com.mob.studio.domain.Room;
import com.mob.studio.service.LiveService;
import com.mob.studio.service.PlayerService;
import com.mob.studio.service.SearchService;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/21
 * @version: 1.7
 */
@With(AuthAction.class)

public class Home extends Controller {
    private static final Logger logger = Logger.getLogger(Studio.class);

    private static LiveService liveService = ServiceHelper.getLiveService();
    private static PlayerService playerService = ServiceHelper.getPlayerService();
    private static SearchService searchService = ServiceHelper.getSearchService();


    @CheckBodyAnnotation({"playerId"})
    public static Result hot(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        List<Room> list = liveService.hotRank(playerId, pageNo, pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rooms",list);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result follower(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        List<Room> list = liveService.followerLive(playerId, pageNo, pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rooms",list);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result talent(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);

        List<Map<String,Object>> list = playerService.talentRank(playerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("players",list);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId","key"})
    public static Result search(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        String key = RequestHelper.getBodyParam(request(), "key", String.class);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        List<Map<String,Object>> list = searchService.search(playerId, key, pageNo, pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }
}
