<%@ page info="memethis" %>


<html>
<head><title>MemeThis</title>
 	<style type="text/css" media="screen">
		@import url( css/style.css );
	</style>
 </head>
<body>
<div id="content">

  <b class="memethis">
  <b class="memethis1"><b></b></b>
  <b class="memethis2"><b></b></b>
  <b class="memethis3"></b>
  <b class="memethis4"></b>
  <b class="memethis5"></b></b>

  <div class="memethisfg">
	<br/>
<h3>&nbsp; &nbsp; Success!</h3> 

<p>Here is your bookmark: <a onclick="return false;" href="<%=request.getAttribute("bookmarklet")%>"><img src="img/bookmarklet.png" alt="MemeThis" width="100" height="28" border="0" style="vertical-align:middle"></a></p>

<h4>A few things you should know:</h4>
<ul>
<li>The first time you use the bookmarklet it may take a few seconds to load</li>
<li>Trying to meme anything on the memethis.com domain (like this page) will <em>not</em> work.</li>
</ul>

<h4>Installation instructions:</h4>

<h5>Firefox / Safari</h5>
<ul><li>Drag the button above to your Bookmarks Toolbar.</li></ul>

<h5>Opera</h5>
<ul><li>Drag the button above to your Personal Bar.</li></ul>

<h5>Internet Explorer</h5>
<ul><li>Right-click on the button above and select "Add to Favorites...". After that, you can add it to your Favorites Bar. You'll be prompted with a few security warnings because the bookmark uses JavaScript.</li></ul>
<!--
<h4>Learn how to use MemeThis:</h4>
	<div align="center">
	<object width="560" height="340"><param name="movie" value="http://www.youtube.com/v/p_4THzyxLe0&hl=en&fs=1&color1=0x402061&color2=0x9461ca"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="http://www.youtube.com/v/p_4THzyxLe0&hl=en&fs=1&color1=0x402061&color2=0x9461ca" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="560" height="340"></embed></object>
	</div>
-->
	<br/>
  </div>

  <b class="memethis">
  <b class="memethis5"></b>
  <b class="memethis4"></b>
  <b class="memethis3"></b>
  <b class="memethis2"><b></b></b>
  <b class="memethis1"><b></b></b></b>
</div>
</body>
</html>
