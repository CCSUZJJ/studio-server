package com.mob.config;

import com.mob.redis.ChatRedis;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * load spring applicationContext.xml
 * User: panal
 * Date: 2016/4/13
 * Time: 16:33
 */
public class AppContextConfig {

    private static ApplicationContext context;

    public static void init() {
        context = new ClassPathXmlApplicationContext("/applicationContext.xml");
    }

    public static ChatRedis getChatRedis(){
        return (ChatRedis)getBeans("chatRedis");
    }

    private static Object getBeans(String beanName){
        return context.getBean(beanName);
    }

}

