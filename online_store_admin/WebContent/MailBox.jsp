<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="java.util.ArrayList" import="java.util.Iterator" import="javafx.util.Pair"%>
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

<style>
.modal-backdrop {z-indez:9999 ;!important}
</style>
    <link rel="stylesheet" href="css/aos.css">

    <link rel="stylesheet" href="css/style.css">

  </head>
  <body>
 <div class="container">
<div style="z-index:1000000;" class="modal fade" id="myModal">
  <div class="modal-dialog modal-xl">
    <div class="modal-content" style="z-index:1000001;">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">New Message</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <form action="/online_store_admin/admController" method="POST">

          <div class="p-3 p-lg-5 border">

            <div class="form-group row">
              <div class="col-md-12">
                <label for="c_email" class="text-black">Message To <span
                  class="text-danger">*</span></label> <input type="email"
                  class="form-control" id="destination" name="dest"
                  placeholder="">
              </div>
            </div>

            <div class="form-group row">
              <div class="col-md-12">
                <label for="c_subject" class="text-black">Text </label> <input
                  type="text" class="form-control" id="message"
                  name="message">
              </div>
            </div>
            <div class="form-group row">
              <div class="col-lg-12">
                <input type="submit" class="btn btn-primary btn-lg btn-block"
                  value="Send" name="Send">
              </div>
            </div>
          </div>
        </form>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Dismiss</button>
      </div>

    </div>
  </div>
</div>
</div>
    
  <div class="site-wrap">
    <header class="site-navbar" role="banner">
			<div class="site-navbar-top">
				<div class="container">
					<div class="row align-items-center">
						<%String logged = (String) session.getAttribute("adminLogged");%>
						 <%if (session.getAttribute("message") != null) { %>
              				<p><%=session.getAttribute("message")%></p>
              				<%session.setAttribute("message",null);} %>
						<div class="col-12 text-center">
							<div class="site-logo">
								<a href="initPage.jsp" class="js-logo-clone">Alispeed</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</header>
	</div>
    <div class="bg-light py-3">
			<div class="container">
				<div class="row">
					<div class="col-md-12 mb-0">
						<a href="initPage.jsp">Administrator</a> <span class="mx-2 mb-0">/</span> <strong
							class="text-black">MailBox</strong>
					</div>
				</div>
			</div>
		</div>

    <div class="site-section">
      <div class="container">
        <div class="row mb-5">
          <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
            New message
          </button>
        <form class="col-md-12"  action="/online_store_admin/admController"method="post">
          <%Pair<ArrayList<String>, ArrayList<String>> Mess= (Pair<ArrayList<String>, ArrayList<String>>) session.getAttribute("messages");%>
            <div class="site-blocks-table">
              <table class="table table-bordered">
                <thead>
                  <tr>
                    <th class="product-thumbnail">From</th>
                    <th class="product-name">Message</th>
                      <th class="product-name">Reply</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                  <%for(int ii = 0 ;ii < Mess.getKey().size(); ii++){ %>
                    <td class="product-thumbnail">
                    <%=Mess.getValue().get(ii)%>
                     </td>
                    <td class="product-name">
                      <h2 class="h5 text-black"><%=Mess.getKey().get(ii) %></h2>
                    </td>
                    <td class="product-name">
                    	 <button type="button" class="btn btn-primary" onclick=reply(this.id)  
                    	 id="<%=Mess.getValue().get(ii)%>"  data-toggle="modal" data-target="#myModal"> 
                    	 Reply </button>
                    </td>
                    </tr>
              <% } %>
                </tbody>
              </table>
            </div>
          </form>

        </div>


      </div>
      <!-- The Modal -->
     

    <footer class="site-footer border-top">

    </footer>
  </div>

  <script src="js/jquery-3.3.1.min.js"></script>
  <script src="js/jquery-ui.js"></script>
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/owl.carousel.min.js"></script>
  <script src="js/jquery.magnific-popup.min.js"></script>
  <script src="js/aos.js"></script>
  <script src="js/reply.js"></script>
  <script src="js/main.js"></script>

  </body>
</html>
