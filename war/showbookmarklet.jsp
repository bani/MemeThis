<%@ page info="memethis" %>


<html>
<head><title>MemeThis</title>
 	<style type="text/css" media="screen">
		@import url( css/style.css );
	</style>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-23048726-1']);
  _gaq.push(['_setDomainName', '.memethis.com']);
  _gaq.push(['_trackPageview', '/success']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
 </head>
 
<body>


<div id="header"><img src="img/memethis-header-title.png"
	alt="MemeThis" /></div>
<div id="content" class="content">

<div class="container">
<h2>Success!</h2> 

<p>Here is your bookmark: <a onclick="return false;" href="<%=request.getAttribute("bookmarklet")%>"><img src="img/bookmarklet.png" alt="MemeThis" width="100" height="28" border="0" style="vertical-align:middle"></a></p>

<h4>A few things you should know:</h4>
<ul>
<li>The first time you use the bookmarklet it may take a few seconds to load</li>
<li>Trying to meme anything on the memethis.com domain (like this page) will <em>not</em> work.</li>
</ul>

<h4>Installation instructions:</h4>

<h5>Firefox / Safari</h5>
<ul><li>Drag the button above to your Bookmarks Toolbar.</li></ul>

<h5>Chrome</h5>
<ul><li>Drag this link &lt;<a onclick="return false;" href="<%=request.getAttribute("bookmarklet")%>">MemeThis</a>&gt; to the Bookmarks Bar.</li></ul>

<h5>Opera</h5>
<ul><li>Drag the button above to your Personal Bar.</li></ul>

<h5>Internet Explorer</h5>
<ul><li>Right-click on the button above and select "Add to Favorites...". After that, you can add it to your Favorites Bar. You'll be prompted with a few security warnings because the bookmark uses JavaScript.</li></ul>

</div>
</div>

</body>
</html>
