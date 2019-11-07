package onlineJavaCode;

	import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.ArrayList;
	//import java.util.Set;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;

	import javax.servlet.ServletConfig;
	import javax.servlet.ServletContext;
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import javax.servlet.http.HttpSession;

import com.sun.tools.ws.wsdl.document.Output;

	//import javafx.util.Pair;


	/**
	 * Servlet implementation class admController
	 */
	@WebServlet("/admController")
	public class admController extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       public ServletConfig config;
	       public ServletContext context;
	       dataManager DM;
	       HttpSession session;
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public admController() {
	        super();
	        // TODO Auto-generated constructor stub
	    }

		/**
		 * @see Servlet#init(ServletConfig)
		 */
		public void init(ServletConfig config) throws ServletException {
			this.config = config;
			context=config.getServletContext();
			//	Iniciazile database
			DM=new dataManager();
		}

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
			context.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			session = request.getSession();
			// TODO Auto-generated method stub
				String path=request.getHeader("referer");
				  String [] arr=path.split("/");
				  path=(arr[arr.length-1]);
					String message=null;
					session.setAttribute("message",message);
					System.out.println(path);
					
					  if(path.compareTo("login.jsp")==0) {
						  String regex = "^(.+)@(.+)$";
						  Pattern pattern = Pattern.compile(regex);
						  String admin=request.getParameter("c_email");
						  String pass=request.getParameter("c_password");
						  if(admin==null) {
							  response.sendRedirect("/online_store_admin/error.jsp");
						  }
						  Matcher matcher = pattern.matcher(admin);
						  if(matcher.matches()==false) {
							  System.out.println(admin);
							  	message="BAD FORMAT EMAIL";
								session.setAttribute("message",message);
								response.sendRedirect("/online_store_admin/login.jsp");
						  }
						  else {
							  administrator act=DM.getAdmin(admin); //Check name in database
							  if(act==null) {
								  message="USER IS NOT REGISTERED";
								  session.setAttribute("message",message);
								  response.sendRedirect("/online_store_admin/login.jsp");
							  }else {
								  boolean check= DM.getAdmin(admin).checkPass(pass); //Check if it exists and good password
								  if(check==false) {
									  message="INCORRECT PASSWORD";
									  session.setAttribute("message",message);
									  response.sendRedirect("/online_store_admin/login.jsp");
								  }
								  else {
									  //Keep data in the session
									  session.setAttribute("admin", act); 
									  session.setAttribute("adminLogged", act.getMail()); 
									  session.setAttribute("plist", act.getProducts());
									  session.setAttribute("ulist", act.getUsers());
									  response.sendRedirect("/online_store_admin/initPage.jsp");
								  }
							  }
						  }
					  }else if(path.compareTo("productList.jsp")==0) {
						  String admin = (String) session.getAttribute("adminLogged");
						  String act = request.getParameter("wButton");
						  String act2 =  request.getParameter("mButton");
						  if((admin==null || act==null) && (admin==null || act2==null)){
							  request.getRequestDispatcher("/error.jsp").forward(request, response); //error checking
						  }
						  else if(act2!=null) {
							  String [] values = act2.split("\\+");
							  String product = values[0].trim();
							  float price = Float.parseFloat(values[1]);
							  product p = DM.getProduct(product, price);
							  session.setAttribute("product", p);
							  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
							  response.sendRedirect("/online_store_admin/modifyProduct.jsp");
						  }else {
							  String [] values = act.split("\\+");
							  String product = values[0].trim();
							  float price = Float.parseFloat(values[1]);
							  product p = DM.getProduct(product, price);
							  ArrayList<product> plist = DM.getAdmin(admin).getProducts();
							  if(plist==null) {
								  message="THERE ARE NO AVAILABLE PRODUCTS";
								  session.setAttribute("message", message);
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }else if(p.getStock()>1){
								  p.doSale();
								  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
								  response.sendRedirect("/online_store_admin/productList.jsp");
							  }else{
								  DM.getAdmin(admin).deleteProduct(p);
								  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
								  response.sendRedirect("/online_store_admin/productList.jsp");
							  }
						  }
					  }else if(path.compareTo("modifyProduct.jsp")==0) {
						  String act = request.getParameter("modifyProduct");
						  String admin = (String) session.getAttribute("adminLogged");
						  product p = (product) session.getAttribute("product");
						  if(act==null || admin==null || p==null) {
							  request.getRequestDispatcher("/error.jsp").forward(request, response);
						  }else{
							  //String name = request.getParameter("c_name");
							  //if(name.isEmpty()) name = p.getName();
						  	  String name = p.getName();
							  String longDesc = request.getParameter("c_longDesc");
							  if(longDesc.isEmpty()) longDesc= p.getLongDesc();
							  String shortDesc = request.getParameter("c_shortDesc");
							  if(shortDesc.isEmpty()) shortDesc = p.getShortDesc();
							  //String price = request.getParameter("c_price");
							  //float fprice = Float.parseFloat(price);
							  //if(fprice==0.00) fprice= p.getPrice();
							  float fprice= p.getPrice();
							  String stock = request.getParameter("c_stock");
							  int fstock = Integer.parseInt(stock);
							  if(fstock==0) fstock = p.getStock();
							  String seller = request.getParameter("c_seller");
							  user newSeller;
							  if(!seller.isEmpty()) {
								  newSeller = DM.getUser(seller);
								  if(newSeller==null) {
									  newSeller = p.getSeller();
								  }
							  }else {
								 newSeller = p.getSeller();
							  }
							  String categories = request.getParameter("c_categories");
							  String [] posibleCategories;
							  String [] newCategories = new String [3];
							  if(!categories.isEmpty()) {
								  posibleCategories = categories.split(" ");
								  int pos = 0;
								  for(String category: posibleCategories) {
									  newCategories[pos] = category;
									  pos++;
									  }
								  while(pos<newCategories.length){
									  newCategories[pos] = "";
									  pos++;
								  }
							  }else {
								  newCategories = p.getCategories();
							  }
							  String image = request.getParameter("c_image");
							  if(image==null) image = p.getImagePath();
							  product updated=new product(name, fprice, shortDesc, longDesc, image, newCategories[0], newCategories[1], newCategories[2], fstock, newSeller);
							  DM.forceProductInsert(updated);
							  //p =DM.getProduct(name, fprice);
							  //session.setAttribute("product", p);
							  ArrayList<product> plist = DM.getAdmin(admin).getProducts();
							  if(plist==null) {
								  message="SOMETHING UNEXPECTED HAPPENED";
								  session.setAttribute("message", message);
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }	  
							  session.setAttribute("plist", plist);
							  response.sendRedirect("/online_store_admin/productList.jsp");
							  }
						  
						  /*CAMBIAR A PARTIR DE AQUI!!*/
						  }else if(path.compareTo("userList.jsp")==0) {
							  String admin = (String) session.getAttribute("adminLogged");
							  String act = request.getParameter("wButton");
							  String act2 =  request.getParameter("mButton");
							  if((admin==null || act==null) && (admin==null || act2==null)){
								  request.getRequestDispatcher("/error.jsp").forward(request, response); //error checking
							  }
							  else if(act2!=null) {
								  String [] values = act2.split("\\+");
								  String product = values[0].trim();
								  float price = Float.parseFloat(values[1]);
								  product p = DM.getProduct(product, price);
								  session.setAttribute("product", p);
								  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
								  response.sendRedirect("/online_store_admin/modifyProduct.jsp");
							  }else {
								  String [] values = act.split("\\+");
								  String product = values[0].trim();
								  float price = Float.parseFloat(values[1]);
								  product p = DM.getProduct(product, price);
								  ArrayList<product> plist = DM.getAdmin(admin).getProducts();
								  if(plist==null) {
									  message="THERE ARE NO AVAILABLE PRODUCTS";
									  session.setAttribute("message", message);
									  request.getRequestDispatcher("/error.jsp").forward(request, response);
								  }else if(p.getStock()>1){
									  p.doSale();
									  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
									  response.sendRedirect("/online_store_admin/productList.jsp");
								  }else{
									  DM.getAdmin(admin).deleteProduct(p);
									  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
									  response.sendRedirect("/online_store_admin/productList.jsp");
								  }
							  }
						  }else if(path.compareTo("modifyUser.jsp")==0) {
							  String act = request.getParameter("modifyProduct");
							  String admin = (String) session.getAttribute("adminLogged");
							  product p = (product) session.getAttribute("product");
							  if(act==null || admin==null || p==null) {
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }else{
								  //String name = request.getParameter("c_name");
								  //if(name.isEmpty()) name = p.getName();
							  	  String name = p.getName();
								  String longDesc = request.getParameter("c_longDesc");
								  if(longDesc.isEmpty()) longDesc= p.getLongDesc();
								  String shortDesc = request.getParameter("c_shortDesc");
								  if(shortDesc.isEmpty()) shortDesc = p.getShortDesc();
								  //String price = request.getParameter("c_price");
								  //float fprice = Float.parseFloat(price);
								  //if(fprice==0.00) fprice= p.getPrice();
								  float fprice= p.getPrice();
								  String stock = request.getParameter("c_stock");
								  int fstock = Integer.parseInt(stock);
								  if(fstock==0) fstock = p.getStock();
								  String seller = request.getParameter("c_seller");
								  user newSeller;
								  if(!seller.isEmpty()) {
									  newSeller = DM.getUser(seller);
									  if(newSeller==null) {
										  newSeller = p.getSeller();
									  }
								  }else {
									 newSeller = p.getSeller();
								  }
								  String categories = request.getParameter("c_categories");
								  String [] posibleCategories;
								  String [] newCategories = new String [3];
								  if(!categories.isEmpty()) {
									  posibleCategories = categories.split(" ");
									  int pos = 0;
									  for(String category: posibleCategories) {
										  newCategories[pos] = category;
										  pos++;
										  }
									  while(pos<newCategories.length){
										  newCategories[pos] = "";
										  pos++;
									  }
								  }else {
									  newCategories = p.getCategories();
								  }
								  String image = request.getParameter("c_image");
								  if(image==null) image = p.getImagePath();
								  product updated=new product(name, fprice, shortDesc, longDesc, image, newCategories[0], newCategories[1], newCategories[2], fstock, newSeller);
								  DM.forceProductInsert(updated);
								  //p =DM.getProduct(name, fprice);
								  //session.setAttribute("product", p);
								  ArrayList<product> plist = DM.getAdmin(admin).getProducts();
								  if(plist==null) {
									  message="SOMETHING UNEXPECTED HAPPENED";
									  session.setAttribute("message", message);
									  request.getRequestDispatcher("/error.jsp").forward(request, response);
								  }	  
								  session.setAttribute("plist", plist);
								  response.sendRedirect("/online_store_admin/productList.jsp");
								  }
							  }
					  
					  
					  
					  }
					  /*else if(path.compareTo("userList.jsp")==0) {
						  String admin = (String) session.getAttribute("adminLogged");
						  String act = request.getParameter("wButton");
						  String act2 = request.getParameter("mButton");
						  if(admin==null || act==null || act2==null) {
							  request.getRequestDispatcher("/error.jsp").forward(request, response); //error checking
						  }else {
							  String [] values = act.split("\\+");
							  String product = values[0].trim();
							  float price = Float.parseFloat(values[1]);
							  product p = DM.getProduct(product, price);
							  ArrayList<product> plist = DM.getAdmin(admin).getProducts();
							  if(plist==null) {
								  message="THERE ARE NO AVAILABLE PRODUCTS";
								  session.setAttribute("message", message);
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }else {
								  DM.getAdmin(admin).deleteProduct(p);
								  session.setAttribute("plist", DM.getAdmin(admin).getProducts());
								  response.sendRedirect("/online_store_admin/productList.jsp");
							  }
						  }
					  }*/
						
			  	

	}
