package utils;

import com.mob.studio.domain.Item;
import jobs.PaybackJobScheduler;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author: Zhang.Min
 * @since: 2016/4/12
 * @version: 1.7
 */
public class InitHelper {
    private static final Logger logger = Logger.getLogger(InitHelper.class);

    private static ApplicationContext ac;

    public static void start() {
        initSpring();
        initItem();
        initBankServerProp();
        PaybackJobScheduler.scheduleJobs();
//        TalentDataInstallJob.init();初始化数据，不需要再运行
    }

    private static void initSpring() {
        logger.info("start init spring config");
        ac = new FileSystemXmlApplicationContext(
                "classpath:META-INF/applicationContext.xml");
        logger.info("ac:" + ac);
    }

    public static Object getBean(String beanName) {
        return ac.getBean(beanName);
    }

    public static void initItem() {
        List<Item> itemList = ServiceHelper.getItemService().refreshItemAll();
        logger.info("items list init finish, size : " + itemList.size());
    }

    public static void initBankServerProp() {
        Properties prop = new Properties();
        InputStream in = InitHelper.class.getResourceAsStream("/bank.properties");
        try {
            prop.load(in);
        } catch(Exception e) {
        	logger.error(e);
        } finally {
        	try {
				in.close();
			} catch (IOException e) {
				logger.error(e);
			}
        }
        Constant.BANK_SERVER_HOST = prop.getProperty("server.host");
        logger.info("bank server host : " + Constant.BANK_SERVER_HOST);
        Constant.BANK_SERVER_PORT = prop.getProperty("server.port");
        logger.info("bank server port : " + Constant.BANK_SERVER_PORT);
    }
}
