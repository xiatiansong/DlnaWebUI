<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DLNA图片查看</title>
<%@include file="/common/head.jsp"%>
<link rel="stylesheet" type="text/css" href="/css/demo.css" />
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<link rel="stylesheet" type="text/css" href="/css/elastislide.css" />
<noscript>
	<style>
.es-carousel ul {
	display: block;
}
</style>
</noscript>
<script id="img-wrapper-tmpl" type="text/x-jquery-tmpl">
	<div class="rg-image-wrapper">
	{{if itemsCount > 1}}
	<div class="rg-image-nav">
		<a href="#" class="rg-image-nav-prev">Previous Image</a>
		<a href="#" class="rg-image-nav-next">Next Image</a>
	</div>
	{{/if}}
	<div class="rg-image"></div>
	<div class="rg-loading"></div>
	<div class="rg-caption-wrapper">
	<div class="rg-caption" style="display:none;">
		<p></p>
	</div>
	</div>
	</div>
</script>
</head>
<body>
	<div class="container">
		<div class="content">
			<h1>
				图片查看 <span> 一个针对MiniDLNA的web端查看ui</span>
			</h1>
			<div id="rg-gallery" class="rg-gallery">
				<div class="rg-thumbs">
					<div class="es-carousel-wrapper">
						<div class="es-nav">
							<span class="es-nav-prev">Previous</span>
							<span class="es-nav-next">Next</span>
						</div>
						<div class="es-carousel">
							<ul>
								<c:forEach var="item" items="${retList}">
								<li>
									<a href="#">
									<img src="${item.URL}"
										data-large="${item.URL}" alt="${item.NAME}"
										data-description="${item.NAME}" />
									</a>
								</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<!-- End Elastislide Carousel Thumbnail Viewer -->
				</div>
				<!-- rg-thumbs -->
			</div>
			<!-- rg-gallery -->
		</div>
		<!-- content -->
	</div>
	<!-- container -->


	<script type="text/javascript" src="/js/jquery.tmpl.min.js"></script>
	<script type="text/javascript" src="/s/jquery.easing.1.3.js"></script>
	<script type="text/javascript" src="/js/jquery.elastislide.js"></script>
	<script type="text/javascript" src="/js/gallery.js"></script>
</body>
</html>