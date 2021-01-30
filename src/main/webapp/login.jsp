<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Login</title>
    <link href="css/style.css" rel="stylesheet" type="text/css">
    <script src="js/validateForm.js"></script>
</head>
<body>
<form class="modal-content" action="<%=request.getContextPath()%>/login" method="post">
    <div id="myDIV" class="header">
        <h2>Login</h2>
    </div>
    <div class="container">
        <label for="email"><b>Email</b></label>
        <input type="email" placeholder="Enter Email" name="email" id="email">
        <span id="resultCheckEmail" style="color: #ff0000;"></span>
        <c:if test="${error != null}"> <span style="color: #ff0000;"><c:out value="${error}"/></span> </c:if>
        <br>
        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" id="password">
        <span id="resultCheckPassword" style="color: #ff0000;"></span>
        <br>
        <div class="clearfix">
            <button type="submit" class="signupbtn" onclick="return validateLogin()">Login</button>
        </div>
        <a href="<%=request.getContextPath()%>/signup.jsp"> Don't have an Account? Create one now.</a>
    </div>
</form>
</body>
</html>
