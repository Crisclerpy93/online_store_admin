<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="model.User" import="java.util.List" import="java.util.Iterator"%>
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
                <a href="index.jsp" class="js-logo-clone">Alispeed</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>
    <div class="bg-light py-3">
      <div class="container">
        <div class="row">
          <div class="col-md-12 mb-0"><a href="initPage.jsp">Admnistrator</a> <span class="mx-2 mb-0">/</span> <strong class="text-black">Users List</strong></div>
        </div>
      </div>
    </div>

    <div class="site-section">
      <div class="container">
        <div class="row mb-7">
        	<form class="col-md-12"  action="/online_store_admin/admController"method="post">
          <% List <User> ulist=(List<User>) session.getAttribute("ulist");
        %>
            <div class="site-blocks-table">
              <table class="table table-bordered">
                <thead>
                  <tr>
                    <th class="user-thumbnail">Image</th>
                    <th class="user-name">Name</th>
                    <th class="user-number">Phone number</th>
                    <th class="user-address">Seller</th>
                    <th class="user-remove">Remove</th>
                    <th class="product-modify">Modify</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>

                  <% for(User newUser : ulist){
                  if(!newUser.getMail().equals("admin@admin.com")){%>

                    <td class="user-thumbnail">
                      <img  style="width:100px; weight:100px"class="img-fluid"src="<% StringBuilder sb = new StringBuilder();
                  sb.append("data:image/png;base64,");
                  sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(newUser.getImage(), false)));
                  out.print(sb.toString()); %>">
                  </p>

                     </td>
                    <td class="user-name">
                      <h2 class="h5 text-black"><%=newUser.getName() %></h2>
                    </td>
                    <td>
                      <h2 class="h5 text-black"><%=newUser.getPhone() %></h2>
                    </td>
                    <td><%=newUser.getIsSeller()%></td>
                    <td>
 					     <button  class="btn btn-primary btn-sm" value="<%=newUser.getMail()%>" name="wButton" >
 					     X</button>
                    </td>
                     <td>
 					     <button class="btn btn-primary btn-sm" value="<%=newUser.getMail()%>" name="mButton">
 					     O</button>
                    </td>


                  </tr>
              <% } }%>
                </tbody>
              </table>
            </div>
          </form>
        </div>
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
