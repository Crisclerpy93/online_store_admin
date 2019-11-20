<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page language="java" import="model.Product"%>
<%@ page import="java.util.List,java.util.ArrayList,model.*,org.apache.commons.codec.binary.StringUtils,org.apache.commons.codec.binary.Base64;" %>

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
					<div class="col-md-12 mb-0">
						<a href="initPage.jsp">Administrator</a> <span class="mx-2 mb-0">/</span>
						<a href="productList.jsp">Products List</a> <span
							class="mx-2 mb-0">/</span><strong class="text-black">Modify
							Product</strong>
					</div>
				</div>
			</div>
		</div>

		<div class="site-section">
			<div class="container">
				<form action="/online_store_admin/admController" enctype="multipart/form-data" method="post">
					<%
						Product product = (Product) session.getAttribute("product");
					%>
					<div class="row">
						<div class="col-6">
							<div class="col-lg-12">
								<label for="c_fname" class="text-black">New Name </label> <input
									type="text" class="form-control" id="c_name" name="c_name" required>
							</div>
							<div class="col-lg-12">
								<label for="c_longDesc" class="text-black">New Long
									Description </label> <input type="text" class="form-control"
									id="c_longDesc" name="c_longDesc" required>
							</div>
							<div class="col-lg-12">
								<label for="c_shortDesc" class="text-black">New Short
									Description </label> <input type="text" class="form-control"
									id="c_shortDesc" name="c_shortDesc" required>
							</div>
							<div class="col-lg-12">
								<label for="c_fname" class="text-black">New Price </label> <input
									type="number" min=0  value=0 class="form-control" id="c_price" name="c_price" required>
							</div>
							<div class="col-lg-12">
								<label for="c_stock" class="text-black">New Stock </label> <span
									class="text-danger">Positive integer number</span> <input
									type="number" class="form-control" id="c_stock" name="c_stock"
									min=0 value=0 required>
							</div>
							<div class="col-lg-12">
								<label for="c_seller" class="text-black">New Seller </label> <span
									class="text-danger">Previous will be set if not found</span> <input
									type="email" class="form-control" id="c_seller" name="c_seller" required>
							</div>
							<div class="col-lg-12">
								<label for="c_fname" class="text-black">New General
									Category </label> <input type="text" class="form-control"
									id="c_categories" name="c_categories" required>
							</div>
							<div class="col-lg-12">
								<label for="c_fname" class="text-black">New SubCategory
									Category </label> <input type="text" class="form-control"
									id="c_subCategories" name="c_subCategories" required>
							</div>
							<div class="col-lg-12">
								<label for="c_fname" class="text-black">New specific
									Category </label> <input type="text" class="form-control"
									id="c_speCategories" name="c_speCategories" required>
							</div>
							<div class="col-lg-12">
								<label for="c_image" class="text-black">New Image </label> <input
									type="file" class="form-control" id="c_image" name="c_image" >
							</div>
							<div class="col-lg-12">
								<input type="submit" class="btn btn-primary btn-lg btn-block"
									name="modifyProduct" value="send">
							</div>
						</div>
						<div class="col-6">
							<h2 class="text-black"><%=product.getName()%></h2>
							<p>
								<img class="img-fluid"src="<% StringBuilder sb = new StringBuilder();
						sb.append("data:image/png;base64,");
						sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(product.getImage(), false)));
						out.print(sb.toString()); %>">
						</p>

							</p>
							<p>
								<strong class="text-primary h6">Long Description:</strong>
								<%=product.getLongDesc()%></p>
							<p>
								<strong class="text-primary h6">Short Description:</strong>
								<%=product.getShortDesc()%></p>
							<p>
								<strong class="text-primary h6">Price:</strong>
								<%=product.getPrice()%></p>
							<p>
								<strong class="text-primary h6">Stock:</strong>
								<%=product.getStock()%></p>
							<p>
								<strong class="text-primary h6">Seller:</strong>
								<%=product.getUser().getMail()%></p>
							<h6>
								<strong class="text-primary h6">Categories:</strong>
							</h6>
							<p><%=product.getCategoryBean().getName()%></p>

						</div>
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
