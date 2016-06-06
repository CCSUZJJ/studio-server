/**
 * 
 */
package utils;

import com.mob.studio.service.*;

/**
 * @author zhangmin
 *
 * @since 2016年3月15日
 */
public class ServiceHelper {
	
	public static PlayerService getPlayerService() {
		return (PlayerService) InitHelper.getBean("playerService");
	}

	public static LiveService getLiveService() {
		return (LiveService) InitHelper.getBean("liveService");
	}

	public static SearchService getSearchService() {
		return (SearchService) InitHelper.getBean("searchService");
	}

	public static PlayerBankService getPlayerBankService() {
		return (PlayerBankService) InitHelper.getBean("playerBankService");
	}

	public static ItemService getItemService() {
		return (ItemService) InitHelper.getBean("itemService");
	}

	public static BankService getBankService() {
		return (BankService) InitHelper.getBean("bankService");
	}

}
