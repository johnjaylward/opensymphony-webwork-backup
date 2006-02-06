<%-- 
	showcase.jsp
	
	@version $Date$ $Id$
--%>

<%@ taglib uri="/webwork" prefix="ww" %>
<html>
<head>
    <title>Showcase</title>
</head>

<body>
<h1>Showcase samples</h1>

<p>The given examples will demonstrate the usages of all WebWork tags as well as validations etc.</p>

<p>
	<ul>
        <!-- config-browser -->
        <li><ww:url id="url" namespace="/config-browser" action="index"/><ww:a href="%{url}">Configuration browser (Great for development!)</ww:a></li>

		<!-- continuation -->
		<li><ww:url id="url" namespace="/continuations" action="guess"/><ww:a href="%{url}">Continuations Example</ww:a></li>
		
		<!-- tags -->
		<li><ww:url id="url" value="/tags"/><ww:a href="%{url}">Tags Examples</ww:a></li>
		
		<!-- fileupload -->
		<li><ww:url id="url" namespace="/fileupload" action="upload" /><ww:a href="%{url}">File Upload Example</ww:a></li>

		<!-- crud -->
		<li><ww:url id="url" value="/empmanager"/><ww:a href="%{url}">CRUD Examples</ww:a></li>
		
		<!-- person manager sample -->
		<li><ww:url id="url" value="/person"/><ww:a href="%{url}">PersonManager Sample</ww:a></li>

        <!-- validation -->
        <li><ww:url id="url" value="/validation"/><ww:a href="%{url}">Validation Examples</ww:a></li>

        <!-- ajax -->
        <li><ww:url id="url" value="/ajax"/><ww:a href="%{url}">AJAX Examples</ww:a></li>
        
        <!-- action chaining -->
		<li><ww:url id="url" namespace="actionchaining" action="actionChain1" method="input" /><ww:a href="%{url}">Action Chaining Example</ww:a></li>
		
	</ul>
</p>

</body>
</html>