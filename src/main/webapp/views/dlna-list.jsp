<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DLNA播放列表</title>
<%@include file="/common/head.jsp"%>
<script src="/javascript/dlna-list.js" type="text/javascript"></script>
</head>
<body>
	<table class="table table-hover table-condensed">
		<caption class="text-center">DLNA文件列表</caption>
		<thead>
			<tr>
				<th>ID</th>
				<th>名称</th>
				<th>打开</th>
				<th>文件类型</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${retList}">
				<tr>
					<th scope="row">${item.ID}</th>
					<td>${item.NAME}</td>
					<td>
						<c:choose>
							<c:when test="${item.CLASS == 'container.storageFolder'}">
								<a href="/index?parentId=${item.OBJECT_ID}&fileType=${item.CLASS}" target="_self">
							</c:when>
							<c:when test="${item.CLASS == 'item.videoItem'}">
								<a href="/index?fileType=${item.CLASS}&url=${item.URL}&width=${item.WIDTH}&height=${item.HEIGHT}" target="_blank">
							</c:when>
							<c:when test="${item.CLASS == 'item.imageItem.photo'}">
								<a href="/index?parentId=${item.OBJECT_ID}&fileType=${item.CLASS}&url=${item.URL}" target="_blank">
							</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> 
							${item.NAME}
						</a>
					</td>
					<td>
						<c:choose>
							<c:when test="${item.CLASS == 'container.storageFolder'}">目录</c:when>
							<c:when test="${item.CLASS == 'item.videoItem'}">视频</c:when>
							<c:when test="${item.CLASS == 'item.imageItem.photo'}">图片</c:when>
							<c:otherwise>文件</c:otherwise>
						</c:choose> 
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<nav class="text-center">
		<ul class="pagination">
			<c:choose>
				<c:when test="${pageNo != 1}">
				<li>
					<a href="/index?parentId=${parentId}&fileType=${fileType}&pageNo=${pageNo-1}" aria-label="Previous"> 
						<span aria-hidden="true">«</span>
					</a>
				</li>
				</c:when>
				<c:otherwise>
				<li class="disabled">
					<a href="#" aria-label="Previous"> 
						<span aria-hidden="true">«</span>
					</a>
				</li>
				</c:otherwise>
			</c:choose> 
				
			<c:forEach var="index" begin="1" end="${totalPage}">
				<c:choose>
					<c:when test="${pageNo == index}">
						<li class="active"><a href="/index?parentId=${parentId}&fileType=${fileType}&pageNo=${index}">${index}<span class="sr-only">(current)</span></a></li>
					</c:when>
					<c:otherwise>
						<li><a href="/index?parentId=${parentId}&fileType=${fileType}&pageNo=${index}">${index}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${pageNo != totalPage}">
				<li>
					<a href="/index?parentId=${parentId}&fileType=${fileType}&pageNo=${pageNo+1}" aria-label="Next"> 
						<span aria-hidden="true">»</span>
					</a>
				</li>
				</c:when>
				<c:otherwise>
				<li class="disabled">
					<a href="#" aria-label="Next"> 
						<span aria-hidden="true">»</span>
					</a>
				</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</nav>
</body>
</html>