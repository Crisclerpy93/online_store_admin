<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="onlineJavaCode.administrator"%>
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
						<div
							class="col-12 mb-3 mb-md-0 col-md-4 order-1 order-md-2 text-center">
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
					<div class="col-md-12 mb-0">
						<a href="index.jsp">Administrator</a> <span class="mx-2 mb-0">/</span> <strong
							class="text-black">Initial Page</strong>
					</div>
				</div>
			</div>
		</div>

		<div class="site-section">
		<form action="/admController" method="post">
			<div class="container">
				<div class="row">
				<%administrator admin = (onlineJavaCode.administrator)session.getAttribute("admin");%>
					<div class="col-md-12">
						<h2 class="h3 mb-3 text-black">Welcome <%= admin.getName() %>, what would you want to manage?</h2>
					</div>
					<div class="col-md-4">
							<div class="p-3 p-lg-5 border">
								<div class="form-group row">
									<div class="col-lg-12">
										<a href="userList.jsp" ><input type="button" class="btn btn-primary btn-lg btn-block"
											name="userList" value="usersList"></a>
									</div>
								</div>
							</div>
					</div>
					<div class="col-md-4 ml-auto">
						<div class="p-3 p-lg-5 border">
								<div class="form-group row">
									<div class="col-lg-12">
										<a href="productList.jsp" ><input type="button" class="btn btn-primary btn-lg btn-block"
											name="productList" value="productsList"></a>
									</div>
								</div>
						</div>
					</div>
					<div class="col-md-4 ml-auto">
						<div class="p-3 p-lg-5 border">
								<div class="form-group row">
									<div class="col-lg-12">
									<!-- CAMBIAR CUANDO TENGAMOS EL CORREO CONFIGURADO -->
										<a href="error.jsp" ><input type="button" class="btn btn-primary btn-lg btn-block"
											name="mailBox" value="mailBox"></a>
									</div>
								</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		</div>

		<footer class="site-footer border-top">
			<div class="container">
				<div class="row pt-5 mt-5 text-center">
					<div class="col-md-12">
						<p>
							<!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
							Copyright &copy;
							<script data-cfasync="false"
								src="/cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script>
							<script>
								document.write(new Date().getFullYear());
							</script>
							All rights reserved | This template is made with <i
								class="icon-heart" aria-hidden="true"></i> by <a
								href="https://colorlib.com" target="_blank" class="text-primary">Colorlib</a>
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
