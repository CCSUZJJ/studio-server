package controllers;

import actions.CheckBodyAnnotation;
import com.alibaba.fastjson.JSON;
import com.mob.studio.domain.Item;
import com.mob.studio.domain.Player;
import com.mob.studio.service.ItemService;
import com.mob.studio.service.PlayerBankService;
import com.mob.studio.service.PlayerService;
import org.apache.log4j.Logger;
import play.libs.F;
import play.libs.Json;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Constant;
import utils.RequestHelper;
import utils.ResponseHelper;
import utils.ServiceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/21
 * @version: 1.7
 */
//@With(AuthAction.class)
public class My extends Controller {
    private static final Logger logger = Logger.getLogger(My.class);

    private static PlayerService playerService = ServiceHelper.getPlayerService();
    private static PlayerBankService playerBankService = ServiceHelper.getPlayerBankService();
    private static ItemService itemService = ServiceHelper.getItemService();

    @CheckBodyAnnotation({"playerId"})
    public static Result follow(Long targetId) {
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Boolean isOk = playerService.doFollow(playerId, targetId);
        if (!isOk) {
            Map<String, Object> map = new HashMap<>();
            map.put("msg", "playerId:" + playerId + " blacked (or be blacked by) targetId:" + targetId);
            return ok(ResponseHelper.writeFailureResponse(map));
        }
        return ok(ResponseHelper.writeSuccessResponse(null));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result unfollow(Long targetId) {
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        playerService.doUnfollow(playerId, targetId);
        return ok(ResponseHelper.writeSuccessResponse(null));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result black(Long targetId) {
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        playerService.doBlack(playerId, targetId);
        return ok(ResponseHelper.writeSuccessResponse(null));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result unblack(Long targetId){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        playerService.doUnblack(playerId, targetId);
        return ok(ResponseHelper.writeSuccessResponse(null));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result home(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Map<String,Object> detail = playerService.getMyProfileDetail(playerId);
        return ok(ResponseHelper.writeSuccessResponse(detail));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result fans_list(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);

        List<Map<String,Object>> playerMaps = playerService.findFansByPlayerId(playerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", playerMaps);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result follower_list(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);

        List<Map<String,Object>> playerMaps = playerService.findFollowerByPlayerId(playerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", playerMaps);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result black_list(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        List<Player> blacklist = playerService.findBlackListByPlayerId(playerId,pageNo,pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("blacklist", blacklist);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result gift_rank_list(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Integer pageNo = RequestHelper.getBodyParam(request(), "pageNo", Integer.TYPE, Constant.DEFAULT_PAGE_NO);
        Integer pageSize = RequestHelper.getBodyParam(request(), "pageSize", Integer.TYPE, Constant.DEFAULT_PAGE_SIZE);
        Long point = playerBankService.getPlayerPointById(playerId);
//        Long point = playerBankService.getPlayersFlowersById(playerId);

        List<Map<String,Object>> giftRankList = playerBankService.findGiftRankByPlayerId(playerId, pageNo, pageSize);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        resultMap.put("giftTop", giftRankList);
        resultMap.put("point", point);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @CheckBodyAnnotation({"playerId"})
    public static Result update() {
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Player player = playerService.findPlayerInfoById(playerId);
        if (player == null) {
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "NOT FOUND player:\t" + playerId);
            return ok(ResponseHelper.writeFailureResponse(resultMap));
        }
        player = fillPlayer(player, RequestHelper.parseBody(request()));
        Player p = playerService.update(player);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("player",p);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    //使用道具送礼
    @CheckBodyAnnotation({"playerId","itemId","quantity"})
    public static F.Promise<Result> grant(final Long targetId) {
        final Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        final String itemId = RequestHelper.getBodyParam(request(), "itemId" ,String.class);
        final Long quantity = RequestHelper.getBodyParam(request(), "quantity", Long.TYPE);

//        DynamicForm requestData = Form.form().bindFromRequest();
//        final Long playerId = Long.valueOf(requestData.get("playerId"));
//        final String itemId = requestData.get("itemId");
//        final Long quantity = Long.valueOf(requestData.get("quantity"));

        //validate submit to Bank API
        Map<String,Object> itemDetail = new HashMap<>();
        itemDetail.put("playerId", playerId);
        itemDetail.put("itemId", itemId);
        itemDetail.put("quantity", quantity);
        final Map<String,Object> resultMap = new HashMap<>();
        F.Promise<Result> result = WS.url("http://" + Constant.BANK_SERVER_HOST + ":" + Constant.BANK_SERVER_PORT + "/bank/debit").post(Json.toJson(itemDetail)).flatMap(
                new F.Function<WS.Response, F.Promise<Result>>() {
                    @Override
                    public F.Promise<Result> apply(WS.Response response) throws Throwable {
                        if (response.getStatus() == Http.Status.OK) {
                            String txID = response.getBody();
                            final Map<String, String> transData = new HashMap<>();
                            transData.put("transactionId", txID);
                            transData.put("status", "open");
                            // open transaction
                            return WS.url("http://" + Constant.BANK_SERVER_HOST + ":" + Constant.BANK_SERVER_PORT + "/bank/debit").put(Json.toJson(transData)).flatMap(
                                    new F.Function<WS.Response, F.Promise<Result>>() {
                                        @Override
                                        public F.Promise<Result> apply(WS.Response response) throws Throwable {
                                            if (response.getStatus() == Http.Status.OK) {
                                                // deliver items to user
                                                final Item item = itemService.getItemById(itemId);
                                                if (item == null) {
                                                    resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "item not found\tid:" + itemId);
                                                    return F.Promise.<Result>pure(ok(ResponseHelper.writeFailureResponse(resultMap)));
                                                }
                                                playerBankService.useItem(playerId, targetId, item, quantity);
//                                                final Long targetPoint = playerBankService.getPlayerPointById(targetId);
                                                final Long targetPoint = playerBankService.getPlayersFlowersById(targetId);
                                                transData.put("status", "close");
                                                return WS.url("http://" + Constant.BANK_SERVER_HOST + ":" + Constant.BANK_SERVER_PORT + "/bank/debit").put(Json.toJson(transData)).map(
                                                        new F.Function<WS.Response, Result>() {
                                                            @Override
                                                            public Result apply(WS.Response response) throws Throwable {
                                                                if (response.getStatus() == Http.Status.OK) {
                                                                    resultMap.put("transactionId", transData.get("transactionId"));
                                                                    resultMap.put("item",item);
                                                                    resultMap.put("quantity",quantity);
                                                                    resultMap.put("status", transData.get("status"));
                                                                    resultMap.put("targetPoint",targetPoint);
                                                                    return ok(ResponseHelper.writeSuccessResponse(resultMap));
                                                                } else {
                                                                    resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "[transaction failed]\t:" + JSON.toJSONString(transData));
                                                                    return ok(ResponseHelper.writeFailureResponse(resultMap));
                                                                }

                                                            }
                                                        }
                                                );
                                            } else {
                                                resultMap.put(Constant.ERROR_DEFAULT_MESSAGE, "[transaction failed]\t:" + JSON.toJSONString(transData));
                                                return F.Promise.<Result>pure(ok(ResponseHelper.writeFailureResponse(resultMap)));
                                            }
                                        }
                                    }
                            );
                        }
                        return F.Promise.<Result>pure(badRequest(response.getBody()));
                    }
                }
        );
        return result;
    }

    private static Player fillPlayer(Player player, Map<String, Object> bodyMap) {
        if (bodyMap.get("nick") != null) {
            player.setNick(bodyMap.get("nick").toString());
        }
        if (bodyMap.get("avatar") != null) {
            player.setAvatar(bodyMap.get("avatar").toString());
        }
        if (bodyMap.get("sex") != null) {
            player.setSex(Integer.valueOf(bodyMap.get("sex").toString()));
        }
        if (bodyMap.get("city") != null) {
            player.setCity(bodyMap.get("city").toString());
        }
        if (bodyMap.get("intro") != null) {
            player.setIntro(bodyMap.get("intro").toString());
        }
        return player;
    }
}
