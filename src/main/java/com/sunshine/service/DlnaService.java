package com.sunshine.service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sunshine.util.SQLiteUtil;

public class DlnaService {

	private static JdbcTemplate template = null;

	static {
		DataSource ds = SQLiteUtil.getResource().getDataSource();
		template = new JdbcTemplate(ds);
	}

	/**
	 * 获取根目录
	 * @param pageNo
	 * @return
	 */
	public List<Map<String, Object>> getRootPageList(int pageNo) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * from OBJECTS t where t.CLASS = 'container.storageFolder' ")
		  .append(" and t.NAME not like '%$%'  and t.PARENT_ID = (SELECT t.OBJECT_ID from OBJECTS t where t.CLASS = 'container.storageFolder'")
		  .append(" and t.NAME = 'Browse Folders')  limit ? offset ? ");
		// 设置参数
		Object[] args = new Object[] { SQLiteUtil.getResource().getPageSize(),
				(pageNo - 1) * SQLiteUtil.getResource().getPageSize() };
		// 查询数据
		return template.query(sb.toString(), args, new ColumnMapRowMapper());
	}
	
	/**
	 * 获取根目录
	 * @return
	 */
	public int getRootCount() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(1) from OBJECTS t where t.CLASS = 'container.storageFolder' ")
		  .append(" and t.NAME not like '%$%'  and t.PARENT_ID = (SELECT t.OBJECT_ID from OBJECTS t where t.CLASS = 'container.storageFolder'")
		  .append(" and t.NAME = 'Browse Folders') ");
		// 设置参数
		Object[] args = new Object[] {};
		// 查询数据
		return template.queryForObject(sb.toString(), args, Integer.class);
	}

	/**
	 * 获取除根目录以外的目录
	 * @param parentId
	 * @param pageNo
	 * @param isPage-是否分页
	 * @return
	 */
	public List<Map<String, Object>> getPageList(String parentId, int pageNo, boolean isPage) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT t.ID,t.OBJECT_ID,t.PARENT_ID,t.REF_ID,t.CLASS,t.DETAIL_ID,t.NAME, ")
		  .append("d.MIME,d.TITLE,d.RESOLUTION,d.DURATION,d.PATH from OBJECTS t ")
		  .append("left join DETAILS d on t.DETAIL_ID = d.ID where t.PARENT_ID = ? ");
		// 设置参数
		Object[] args = null;
		//需要分页（图片查看不需要分页）
		if(isPage){
			sb.append(" limit ? offset ? ");
			args = new Object[] { parentId, SQLiteUtil.getResource().getPageSize(),
					(pageNo - 1) * SQLiteUtil.getResource().getPageSize() };
		}else{
			args = new Object[] { parentId };
		}
		// 查询数据
		return template.query(sb.toString(), args, new ColumnMapRowMapper());
	}
	
	/**
	 * 获取除根目录以外的目录
	 * @param parentId
	 * @return
	 */
	public int getCount(String parentId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(1) from OBJECTS t ")
		  .append("left join DETAILS d on t.DETAIL_ID = d.ID where t.PARENT_ID = ? ");
		// 设置参数
		Object[] args = new Object[] { parentId };
		// 查询数据
		return template.queryForObject(sb.toString(), args, Integer.class);
	}
	
	public static void main(String[] args) {
		DlnaService dl = new DlnaService();
		System.out.println(dl.getPageList("2$15$D", 1, true));
		System.out.println(dl.getCount("2$15$D"));
	}
}
