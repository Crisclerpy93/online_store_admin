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
						  }else if(act2!=null) {
							  String [] values = act2.split("\\+");
							  String product = values[0].trim();
							  float price = Float.parseFloat(values[1]);
							  product p = DM.getProduct(product, price);
							  session.setAttribute("product", p);
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

	}
