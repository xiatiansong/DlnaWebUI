<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DLNA视频播放</title>
<%@include file="/common/head.jsp"%>
<script>
	document.createElement('video');document.createElement('audio');document.createElement('track');
	// 页面加载完成  才加载VideoJS的标签
    videojs.setupAllWhenReady();
    videojs.options.flash.swf = "/css/video-js.swf";
  </script>
</head>
<body>
  <nav >
  <video id="dlna_video" class="video-js vjs-default-skin vjs-big-play-centered"
  		width="${WIDTH}" height="${HEIGHT}"
  		style="margin:20px auto;"
      	poster="/image/oceans-clip.png"
      	data-setup='{ "controls": true, "autoplay": true, "preload": "auto" }'>
    <source src="${URL}" type='video/mp4' />
    <track kind="captions" src="/css/demo.captions.vtt" srclang="en" label="English"></track>
    <track kind="subtitles" src="/css/demo.captions.vtt" srclang="en" label="English"></track>
    <p class="vjs-no-js">To view this video please enable JavaScript, and consider upgrading to a web browser that <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a></p>
  </video>
  </nav>
</body>
</html>