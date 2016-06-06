package controllers;

import actions.AuthAction;
import actions.CheckBodyAnnotation;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.mob.studio.domain.Item;
import com.mob.studio.service.BankService;
import com.mob.studio.service.ItemService;
import com.mob.studio.service.PlayerBankService;
import org.apache.log4j.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.RequestHelper;
import utils.ResponseHelper;
import utils.ServiceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: Zhang.Min
 * @since: 2016/4/13
 * @version: 1.7
 */
//@With(AuthAction.class)
public class Bank extends Controller {
    private static final Logger logger = Logger.getLogger(Bank.class);
    private static final ItemService itemService = ServiceHelper.getItemService();
    private static final PlayerBankService playerBankService = ServiceHelper.getPlayerBankService();
    private static final BankService bankService = ServiceHelper.getBankService();

    @With(AuthAction.class)
    @CheckBodyAnnotation({"playerId"})
    public static Result inventory(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        List<Item> list = itemService.getItemAll();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    @With(AuthAction.class)
    @CheckBodyAnnotation({"playerId"})
    public static Result asset(){
        Long playerId = RequestHelper.getBodyParam(request(), "playerId", Long.TYPE);
        Long coin = playerBankService.getPlayerCoinById(playerId);
        Long point = playerBankService.getPlayerPointById(playerId);
//        Long point = playerBankService.getPlayersFlowersById(playerId);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("coin",coin);
        resultMap.put("point", point);
        return ok(ResponseHelper.writeSuccessResponse(resultMap));
    }

    public static Result debit_post() {
        logger.debug("POST : /bank/debit");
        JsonNode jsonNode = request().body().asJson();
        logger.debug("body json " + jsonNode.toString());
        List<String> errorList = new ArrayList<String>();
        String tx = bankService.createTransaction(jsonNode.toString(), errorList);
        if (!errorList.isEmpty()){
            logger.debug("error>>>>>>badRequest:" + JSON.toJSONString(errorList));
            return badRequest(JSON.toJSONString(errorList));
        }
        return ok(tx);
    }

    public static Result debit_put() {
        logger.debug("PUT : /bank/debit");
        JsonNode jsonNode = request().body().asJson();
        logger.debug("body json " + jsonNode.toString());
        String transactionId = jsonNode.findValue("transactionId").asText();
        String status = jsonNode.findValue("status").asText();
        List<String> errorList = new ArrayList<String>();
        switch (status){
            case "open":
                bankService.openTransaction(transactionId, errorList);
                if (!errorList.isEmpty()){
                    logger.debug("error>>>>>>badRequest:" + JSON.toJSONString(errorList));
                    return badRequest(JSON.toJSONString(errorList));
                }
                break;
            case "close":
                bankService.closeTransaction(transactionId, errorList);
                if (!errorList.isEmpty()){
                    logger.debug("error>>>>>>badRequest:" + JSON.toJSONString(errorList));
                    return badRequest(JSON.toJSONString(errorList));
                }
                break;
        }
        logger.debug("transaction status change to " + status);
        return ok();
    }

}
