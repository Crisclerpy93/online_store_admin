<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="onlineJavaCode.user" import="java.util.ArrayList" import="java.util.Iterator"%>
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
            <div class="col-12 mb-3 mb-md-0 col-md-4 order-1 order-md-2 text-center">
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
          <% ArrayList <user> ulist=(ArrayList<user>) session.getAttribute("ulist");
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

                  <% for(user newUser : ulist){ %>
                    <td class="user-thumbnail">
                   <img src=" <%=newUser.getImagePath() %>" alt="Image" class="img-fluid">
                     </td>
                    <td class="user-name">
                      <h2 class="h5 text-black"><%=newUser.getName() %></h2>
                    </td>
                    <td>
                      <div class="input-group mb-3" style="max-width: 120px;"> 
                        <input type="text" class="form-control text-center" value="<%=newUser.getPhone() %>" name="" id="<%=newUser.getName()%>">  
                      </div>
                    </td>
                    <td><%=newUser.isSeller() %></td>
                    <td>
 					     <button  class="btn btn-primary btn-sm" value="<%=newUser.getMail() %>" name="wButton" >
 					     X</button>
                    </td>
                     <td>
 					     <button class="btn btn-primary btn-sm" value="<%=newUser.getMail() %>" name="mButton">
 					     O</button>
                    </td>


                  </tr>
              <% } %>
                </tbody>
              </table>
            </div>
          </form>
        </div>
      </div>
    </div>

    <footer class="site-footer border-top">
      <div class="container">
        <div class="row pt-5 mt-5 text-center">
          <div class="col-md-12">
            <p>
            <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
            Copyright &copy;<script data-cfasync="false" src="/cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="icon-heart" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank" class="text-primary">Colorlib</a>
            <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
            </p>
          </div>

        </div>
      </div>
    </footer>
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
