/**
 * 
 */
package utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import play.Application;
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.SimpleResult;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {

	private static final Logger logger = Logger.getLogger(Global.class);

	private static boolean isDebug;

	/**
	 * 
	 */
	public Global() {
	}
	
	
	
	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		
		logger.info("Application is starting...");
		InitHelper.start();

		
	}

	@Override
	public void onStop(Application app) {
		logger.info("Application shutdown...");
	}

	@Override
	public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {
		String error = "Error occured" + "\r\n\r\n";
		error = error + "host: " + request.host();
		error = error + "uri: " + request.uri() + "\r\n";
		error = error + "params: " + request.queryString() + "\r\n";
		error = error + "methods: " + request.method() + "\r\n";
		error = error + "path: " + request.path() + "\r\n\r\n";
		error = error + "headers: " + "\r\n";
		for (String key : request.headers().keySet()) {
			error = error + key + " = ";
			String[] values = request.headers().get(key);
			if (values != null && values.length > 0) {
				for (String value : values) {
					error = error + value + ",";
				}
			}
			error = error + "\r\n";
		}
		error = error + "\r\n";
		error = error + ExceptionUtils.getStackTrace(t);
		logger.error(error);
		return super.onError(request, t);
	}

	

	
	public static void cleanCache() {
		//Cache.remove();
	}

	@Override
	public Action onRequest(Request request, Method arg1) {
		// memory kanshi
		logger.debug("total memory: " + Runtime.getRuntime().totalMemory()
				/ 1024 + "KB");
		logger.debug("free memory: " + Runtime.getRuntime().freeMemory() / 1024
				+ "KB");
		logger.info("request url: " + request.uri());
		return super.onRequest(request, arg1);
	}


	
	
	public static boolean isDebug() {
		isDebug = true;
		return isDebug;
	}

}
