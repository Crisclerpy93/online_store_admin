<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="model.User"%>
<%@ page import="java.util.List,java.util.ArrayList,model.*,org.apache.commons.codec.binary.StringUtils,org.apache.commons.codec.binary.Base64;" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Alispeed &mdash; Administrator mode</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
    <link rel="stylesheet" href="fonts/icomoon/style.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/aos.css">
    <link rel="stylesheet" href="css/style.css">

  </head>
  <body>

  <div class="site-wrap">
    <header class="site-navbar" role="banner">
      <div class="site-navbar-top">
        <div class="container">
          <div class="row align-items-center">


            <div class="col-12 text-center">
              <div class="site-logo">
                <a href="initPage.jsp" class="js-logo-clone">Alispeed</a>
              </div>
            </div>

          </div>
        </div>
      </div>

    </header>
    <div class="bg-light py-3">
      <div class="container">
        <div class="row">
          <div class="col-md-12 mb-0"><a href="initPage.jsp">Administrator</a> <span class="mx-2 mb-0">/</span> <a href="userList.jsp">Users List</a> <span class="mx-2 mb-0">/</span><strong class="text-black">Modify User Profile</strong></div>
        </div>
      </div>
    </div>

    <div class="site-section">
      <div class="container">
        <form class="row" enctype="multipart/form-data"	 action="/online_store_admin/admController" method="post">
         <% User user = (User)session.getAttribute("user");
        %>
          <div class="col-md-6">
			<div class="col-lg-12">
				<label for="c_name" class="text-black">New Name </label>
					<input type="text" class="form-control" id="c_name" name="c_name" required>
			</div>
			<div class="col-lg-12">
				<label for="c_surname" class="text-black">New Surname </label>
					<input type="text" class="form-control" id="c_surname" name="c_surname" required>
			</div>
			<div class="col-lg-12">
				<label for="c_phone" class="text-black">New Phone Number </label>
					<input type="tel" class="form-control" id="c_phone" name="c_phone" value=111111111 required>
			</div>
			<div class="col-lg-12">
				<label for="c_address" class="text-black">New Address </label>
					<input type="text" class="form-control" id="c_address" name="c_address" required>
			</div>
			<div class="col-lg-12">
				<label for="c_image" class="text-black">New Image </label>
					<input type="file" class="form-control" id="c_image" name="c_image">
			</div>
			<div class="col-lg-12">
				<label for="c_seller" class="text-black">This user should be a seller? </label>
					<input type="checkbox" id="c_seller" name="c_seller" required>
			</div>
			<div class="col-lg-12">
				<input type="submit" class="btn btn-primary btn-lg btn-block" name="modifyUser" value="send">
			</div>
          </div>
          <div class="col-md-6">
            <h2 class="text-black"><%=user.getName()+" "+user.getSurname() %></h2>
            <p>						<img class="img-fluid"src="<% StringBuilder sb = new StringBuilder();
        						sb.append("data:image/png;base64,");
        						sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(user.getImage(), false)));
        						out.print(sb.toString()); %>">
        						</p>
</p>
            <p><strong class="text-primary h6">E-mail:</strong> <%=user.getMail()%></p>
            <p><strong class="text-primary h6">Addres:</strong> <%=user.getAddress()%></p>
            <p><strong class="text-primary h6">Phone number:</strong> <%=user.getPhone()%></p>
            <p><strong class="text-primary h6">Is this user a seller?:</strong> <%=user.getIsSeller()%></p>
          </div>
        </form>
      </div>
    </div>
  </div>

  <script src="js/jquery-3.3.1.min.js"></script>
  <script src="js/jquery-ui.js"></script>
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/owl.carousel.min.js"></script>
  <script src="js/jquery.magnific-popup.min.js"></script>
  <script src="js/aos.js"></script>

  <script src="js/main.js"></script>

  </body>
</html>
