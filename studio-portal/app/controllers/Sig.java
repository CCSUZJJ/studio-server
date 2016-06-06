package controllers;

import com.sig.utils.SignatureUtils;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created with Intellij IDEA
 * User: panal
 * Date: 2016/3/31
 * Time: 12:21
 */
public class Sig extends Controller {
    public static Result getSig() {
        System.out.println("ok");
        return ok(SignatureUtils.getSignature(11L));
    }







}
