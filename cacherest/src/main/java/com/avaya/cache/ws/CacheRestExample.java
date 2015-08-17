package com.avaya.cache.ws;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.hibernate.stat.Statistics;

import com.avaya.csi.callback.cache.CallbackCacheAdministration;
import com.avaya.csi.callback.datalayer.ConfigurationDao;
import com.avaya.csi.callback.datalayer.HibernateSessionFactory;
import com.avaya.csi.callback.datalayer.IGeneralDao;
import com.avaya.csi.callback.datalayer.imp.DAOFactory;
import com.avaya.csi.callback.domain.CallbackConfig;

@Path("cache")
public class CacheRestExample {
	private static Logger LOGGER = Logger.getLogger(CacheRestExample.class);
	private final IGeneralDao generalDao = DAOFactory.getGeneralDao();
	private final ConfigurationDao configDao = DAOFactory.getConfigurationDao();

	@GET
    @Path("{configId}")
	public String testCache(@PathParam("configId") String configId) throws Exception {
		long configIdParam = Long.valueOf(configId);
		long totalDuration=this.readTimeTaken(configIdParam);
		StringBuilder stringBuilder = new StringBuilder("\n");
        stringBuilder.append("<p>******************************************************************Time to read: " +totalDuration+" ms *********************************************************************</p>");
        this.printStats(HibernateSessionFactory.getStatistics(), stringBuilder);
        return stringBuilder.toString();
    }

	private long readTimeTaken(long configId) throws Exception{
		long totalDuration=0;
		long start = System.currentTimeMillis();
		
			CallbackConfig config = this.generalDao.getCallbackConfigurationById(configId);
			LOGGER.info(config); 
			if(config!=null){
				LOGGER.info(config.getCalendarConfig() !=null ? config.getCalendarConfig().getDayConfigurations() : "Day configurations: NULL"); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.ENTRY_GENERAL_ENGINE_CALLBACK_RECOVERY_TIME)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.ENTRY_ENABLE_VP_SIGNALING_PORT_USAGE)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.DELAY_BEFORE_MERGE_ON_OFFERING)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.RETRIES_BEFORE_MERGE_ON_OFFERING)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.SEND_REFER_TO_AGENT)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.ENTRY_MEDIA_ENCRYPTION)); 
				LOGGER.info(configDao.getConfigurationByKey(com.avaya.csi.callback.domain.Configuration.CUSTOMER_FIRST_ENABLED)); 
			}
			
		
		totalDuration = System.currentTimeMillis() - start; 
		return totalDuration;
	}
	
	protected void printStats(Statistics stats,StringBuilder stringBuilder) {
		LOGGER.info("***** Hibernate Statistics *****");
		LOGGER.info("Fetch Count=" + stats.getEntityFetchCount());
		LOGGER.info("Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
		LOGGER.info("Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
		LOGGER.info("Second Level Put Count=" + stats.getSecondLevelCachePutCount());
		
		stringBuilder.append("<p>***** Hibernate Statistics *****");
		stringBuilder.append("<br>Fetch Count=" + stats.getEntityFetchCount());
		stringBuilder.append("<br>Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
		stringBuilder.append("<br>Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
		stringBuilder.append("<br>Second Level Put Count=" + stats.getSecondLevelCachePutCount());
		
		List<String> statistics = CallbackCacheAdministration.getCacheStatisticsWithNotNullSize();
		for (String string : statistics) {
			LOGGER.info(string);
			stringBuilder.append("\n"+string);
		}
		
		
	}	

}
