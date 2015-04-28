package com.sunshine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunshine.service.DlnaService;
import com.sunshine.util.SQLiteUtil;
import com.sunshine.util.Validation;

/**
 * dlna处理请求控制器
 * @author zyj
 *
 */
public class DlnaServlet extends HttpServlet {

	private static final long serialVersionUID = 6161057395281119778L;
	
	/**视频文件类型**/
	private static final String VIDEO_TYPE = "item.videoItem";
	
	/**目录文件类型**/
	private static final String FOLDER_TYPE = "container.storageFolder";
	
	/**图片文件类型**/
	private static final String PHOTO_TYPE = "item.imageItem.photo";
	
	/**参数：分页页数**/
	private static final String PARAMS_PAGE_NO = "pageNo";
	
	/**参数：父目录ID**/
	private static final String PARAMS_PARENT_ID = "parentId";
	
	/**参数：文件类型**/
	private static final String PARAMS_FILE_TYPE = "fileType";
	
	/**参数：URL**/
	private static final String PARAMS_URL = "url";
	
	/**参数：文件宽度**/
	private static final String PARAMS_WIDTH = "width";
	
	/**参数：文件高度**/
	private static final String PARAMS_HEIGHT = "height";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//分页数
		int pageNo = Validation.toInteger(req.getParameter(PARAMS_PAGE_NO));
		if(pageNo == 0){pageNo = 1;}
		//父目录ID
		String parentId = Validation.toString(req.getParameter(PARAMS_PARENT_ID));
		//文件类型
		String fileType = Validation.toString(req.getParameter(PARAMS_FILE_TYPE));
		
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		int totalRecord = 1;
		DlnaService ds = new DlnaService();
		if(Validation.isNULL(parentId)){
			retList.addAll(ds.getRootPageList(pageNo));
			totalRecord = ds.getRootCount();
		}else if(!Validation.isNULL(parentId)){
			boolean isPage = true;
			if(PHOTO_TYPE.equals(fileType)){
				parentId = parentId.substring(0, parentId.lastIndexOf("$"));
				isPage = false;
			}
			retList.addAll(ds.getPageList(parentId, pageNo, isPage));
			totalRecord = ds.getCount(parentId);
			//遍历返回列表，处理图片和视频展现地址和高宽度
			for(Map<String, Object> map : retList){
				 String cla = Validation.toString(map.get("CLASS"));
				 String detailId = Validation.toString(map.get("DETAIL_ID"));
				 String resolu = Validation.toString(map.get("RESOLUTION"));
				 String path = Validation.toString(map.get("PATH"));
				 String oriUrl = SQLiteUtil.getResource().getDlnaUrl();
				 int index = path.lastIndexOf(".");
				 String suffix = path.substring(index > 0 ? index : 0);
				 if(VIDEO_TYPE.equals(cla)){
					 map.put("URL", oriUrl + detailId + suffix);
					 String[] resoluArray = resolu.split("x");
					 int width = Validation.toInteger(resoluArray[0]);
					 int height = Validation.toInteger(resoluArray[1]);
					 map.put("WIDTH", width > 950 ? 950 : width);
					 map.put("HEIGHT", height > 600 ? 600 : height);
				 }else if(PHOTO_TYPE.equals(cla)){
					map.put("URL", oriUrl + detailId + suffix);
				 }
			}
		}
		
		if(!Validation.isNULL(parentId) && parentId.indexOf("$") != -1 && FOLDER_TYPE.equals(fileType)){
			//添加父目录连接
			Map<String, Object> parentMap = new HashMap<String, Object>();
			parentMap.put("CLASS", FOLDER_TYPE);
			parentMap.put("ID", "0");
			parentMap.put("NAME", "上级目录");
			int index = parentId.lastIndexOf("$");
			int countDollar = parentId.split("$").length;
			if(countDollar > 2){
				parentMap.put("OBJECT_ID", index > 0 ? parentId.substring(0, index) : "");
			}else{
				parentMap.put("OBJECT_ID", "");
			}
			
			retList.add(0, parentMap);
		}
		
		//组装返回值
		int totalPageNum = (totalRecord  +  SQLiteUtil.getResource().getPageSize()  - 1) / SQLiteUtil.getResource().getPageSize();  
		req.setAttribute("retList", retList);
		req.setAttribute("totalPage", totalPageNum);
		req.setAttribute(PARAMS_PAGE_NO, pageNo);
		req.setAttribute(PARAMS_PARENT_ID, parentId);
		req.setAttribute(PARAMS_FILE_TYPE, fileType);
		//返回指定页面
		if(FOLDER_TYPE.equals(fileType) || Validation.isNULL(fileType)){
			RequestDispatcher dispatcher = req.getRequestDispatcher("/views/dlna-list.jsp");
			dispatcher.forward(req, resp); 
		}else if(VIDEO_TYPE.equals(fileType)){
			req.setAttribute("URL", Validation.toString(req.getParameter(PARAMS_URL)));
			req.setAttribute("WIDTH", Validation.toString(req.getParameter(PARAMS_WIDTH)));
			req.setAttribute("HEIGHT", Validation.toString(req.getParameter(PARAMS_HEIGHT)));
			RequestDispatcher dispatcher = req.getRequestDispatcher("/views/dlna-view-video.jsp");
			dispatcher.forward(req, resp); 
		}else if(PHOTO_TYPE.equals(fileType)){
			RequestDispatcher dispatcher = req.getRequestDispatcher("/views/dlna-view-photo.jsp"); 
			dispatcher.forward(req, resp); 
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

}