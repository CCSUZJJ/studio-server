package com.mob.studio.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;

public class MybatisUtil  { 
	private static Logger logger = Logger.getLogger(MybatisUtil.class);
	
    private static SqlSessionFactory sqlSessionFactory_Studio_W = null;

  
    private final static String CONF_STUDIO_W = "db_studio_w/mybatis-conf.xml";

    
    static {
    	logger.info(">>>>>>>>>>>>>>>>>INIT com.mob.studio.util.MybatisUtil BEGIN<<<<<<<<<<<<<<<<<<<<<");
    	Reader rdr;

    	
    	try {
			rdr = Resources.getResourceAsReader(CONF_STUDIO_W);
			sqlSessionFactory_Studio_W = new SqlSessionFactoryBuilder().build(rdr);
			rdr.close();
    	}catch (IOException e) {
			e.printStackTrace();
		}
    	
		logger.info(">>>>>>>>>>>>>>>>>INIT com.mob.studio.util.MybatisUtil FINISH<<<<<<<<<<<<<<<<<<<<<");
    }
    
    public static SqlSessionFactory getSqlSessionFactory_Studio_W() {
		return sqlSessionFactory_Studio_W;
	}
}