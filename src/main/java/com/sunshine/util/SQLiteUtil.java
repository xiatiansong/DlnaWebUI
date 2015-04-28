package com.sunshine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.sqlite.SQLiteDataSource;

/**
 * sqlite操作类
 * 
 * @author zyj
 * 
 */
public class SQLiteUtil {

	private static class ResourceHolder {
		public static SQLiteJdbc resource = new SQLiteJdbc();
	}

	public static SQLiteJdbc getResource() {
		return SQLiteUtil.ResourceHolder.resource;
	}

	public static class SQLiteJdbc {
		private final Logger log = Logger.getLogger(getClass());

		private SQLiteDataSource sds = null;
		
		/**
		 * 保存系统配置参数值
		 */
		private static Map<String, Object> paramMap = new HashMap<String, Object>();

		private SQLiteJdbc() {
			init();
			sds = new SQLiteDataSource();

			sds.setUrl(Validation.toString( paramMap.get("sqlfile") ));
		}

		private void init() {
			InputStream is = null;
			try {
				is = this.getClass().getClassLoader()
						.getResourceAsStream("init.properties");
				Properties prop = new Properties();
				prop.load(is);

				Map<String, String> map = PropertiesUtil.convertToMap(prop);
				if (map.size() > 0) {
					paramMap.putAll(map);
				}
			} catch (Exception e) {
				log.error("属性文件加载失败  init.properties caused by " + e.toString());
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭属性文件失败 caused by " + e.toString());
				}
			}
		}
		
		public int getPageSize(){
			return Validation.toInteger(paramMap.get("pagesize"));
		}
		
		public String getDlnaUrl(){
			return Validation.toString(paramMap.get("dlnaurl"));
		}
		
		public DataSource getDataSource(){
			return sds;
		}
		
	}

}
