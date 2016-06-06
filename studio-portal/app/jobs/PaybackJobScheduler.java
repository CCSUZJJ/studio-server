package jobs;

import org.apache.log4j.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import utils.Constant;

import java.util.concurrent.TimeUnit;

/**
 * @author: Zhang.Min
 * @since: 2016/4/20
 * @version: 1.7
 */
public class PaybackJobScheduler {
    private static final Logger logger = Logger.getLogger(PaybackJobScheduler.class);
    public static void scheduleJobs() {
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(Constant.PAYBACK_JOB_INTERVAL, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            logger.debug("payback job start ... ...");
                            //do job
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                Akka.system().dispatcher()
        );
    }
}
