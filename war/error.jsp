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
    <h3>&nbsp; &nbsp; Error</h3> 
	<p><%=request.getAttribute("errormsg")%></p>
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
