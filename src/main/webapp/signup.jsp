<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Sign Up</title>
    <link href="css/style.css" rel="stylesheet" type="text/css">
    <script src="js/validateForm.js"></script>
</head>
<body>
<form class="modal-content" action="<%=request.getContextPath()%>/signup" method="post">
    <div id="myDIV" class="header">
        <h2>Sign Up</h2>
    </div>
    <div class="container">
        <label for="name"><b>Name</b></label>
        <input type="text" placeholder="Enter Name" name="name" id="name" required>
        <span id="resultCheckName" style="color: #ff0000;"></span>
        <br>
        <label for="email"><b>Email</b></label>
        <input type="email" placeholder="Enter Email" name="email" id="email">
        <span id="resultCheckEmail" style="color: #ff0000;"></span>
        <c:if test="${error != null}"> <span style="color: #ff0000;"><c:out value="${error}"/></span> </c:if>
        <br>
        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" id="password">
        <span id="resultCheckPassword" style="color: #ff0000;"></span>
        <br>
        <label for="repeatPassword"><b>Repeat Password</b></label>
        <input type="password" placeholder="Repeat Password" name="repeatPassword" id="repeatPassword">
        <span id="resultCheckRepeatPassword" style="color: #ff0000;"></span>
        <div class="clearfix">
            <button type="submit" class="signupbtn" onclick="return validateSignUp()">Sign Up</button>
        </div>
        <a href="<%=request.getContextPath()%>/login.jsp">Have an Account? Login one now.</a>
    </div>
</form>
</body>
</html>
