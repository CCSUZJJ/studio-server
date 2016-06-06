package jobs;

import com.mob.studio.domain.Player;
import com.mob.studio.redis.RedisDao;
import com.mob.studio.service.PlayerService;
import com.mob.studio.util.RedisKey;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import utils.InitHelper;
import utils.ServiceHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: Zhang.Min
 * @since: 2016/4/26
 * @version: 1.7
 */
public class TalentDataInstallJob {
    private static final Logger logger = Logger.getLogger(TalentDataInstallJob.class);
    private static final PlayerService playerService = ServiceHelper.getPlayerService();

    private static RedisDao redisDao = (RedisDao) InitHelper.getBean("redisDao");



    public static void init(){
        Akka.system().scheduler().scheduleOnce(
                Duration.create(1000, TimeUnit.MILLISECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StopWatch watch = new StopWatch("talent init data job");
                            watch.start();
                            logger.debug("talent install job start ... ...");
                            //do job
                            List<Player> list = playerService.findPlayerByFuzzyNick("", 1, 100);
                            for (Player player : list) {
                                List l = playerService.findFansByPlayerId(player.getId());
                                if (l != null && !l.isEmpty()){
                                    redisDao.zAdd(RedisKey.TALENT_RANK, player.getId().toString(), l.size());
                                }else {
                                    redisDao.zAdd(RedisKey.TALENT_RANK, player.getId().toString(), 0);
                                }
                            }
                            watch.stop();
                            logger.debug(watch.prettyPrint());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                Akka.system().dispatcher()
        );
    }
}
