<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java"%>
<!DOCTYPE html>

<html lang="en">
<head>
<title>Alispeed &mdash; Administrator mode</title>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Mukta:300,400,700">
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
						<%
							String logged = (String) session.getAttribute("userLogged");
						%>
						<%
							if (session.getAttribute("message") != null) {
						%>
						<p><%=session.getAttribute("message")%></p>
						<%
							session.setAttribute("message", null);
							}
						%>
						<div class="col-12 text-center">
							<div class="site-logo">
								<a href="index.jsp" class="js-logo-clone">Alispeed</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</header>

		<div class="site-blocks-cover"
			style="background-image: url(images/hero_1.jpg);" data-aos="fade">
			<div class="container">
				<div
					class="row align-items-start align-items-md-center justify-content-end">
					<div class="col-md-5 text-center text-md-left pt-5 pt-md-0">
						<h1 class="mb-2">Welcome Administrator</h1>
						<div class="intro-text text-center text-md-left">
							<p class="mb-4">Please, introduce your credentials</p>
							<p>
								<a href="login.jsp" class="btn btn-sm btn-primary">Log in</a>
							</p>
						</div>
					</div>
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
