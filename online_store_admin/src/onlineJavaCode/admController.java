package onlineJavaCode;

	import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.ArrayList;
	//import java.util.Set;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
	    
	    private ConnectionFactory projectFactory;
	 	
	 	private Queue messages;
		private Queue conf;

		 
		/**
		 * @see Servlet#init(ServletConfig)
		 */
		public void init(ServletConfig config) throws ServletException {
			this.config = config;
			context=config.getServletContext();
			//	Iniciazile database
			DM=new dataManager();
			try {
				Context icontext = (Context) new InitialContext();
				projectFactory = (ConnectionFactory) icontext.lookup("projectFactory");
				messages = (Queue) icontext.lookup("messages");
				conf = (Queue) icontext.lookup("conf");
			} catch (NamingException e) {
				e.printStackTrace();
			}
			
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
									  //session.setAttribute("plist", act.getProducts());
									  //session.setAttribute("ulist", act.getUsers());
									  session.setAttribute("plist", DM.getAllProducts());
									  session.setAttribute("ulist", DM.getAllUsers());
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
							  session.setAttribute("plist", DM.getAllProducts());
							  response.sendRedirect("/online_store_admin/modifyProduct.jsp");
						  }else {
							  String [] values = act.split("\\+");
							  String product = values[0].trim();
							  float price = Float.parseFloat(values[1]);
							  product p = DM.getProduct(product, price);
							  ArrayList<product> plist = DM.getAllProducts();
							  if(plist==null) {
								  message="THERE ARE NO AVAILABLE PRODUCTS";
								  session.setAttribute("message", message);
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }else if(p.getStock()>1){
								  p.doSale();
								  session.setAttribute("plist", DM.getAllProducts());
								  response.sendRedirect("/online_store_admin/productList.jsp");
							  }else{
								  DM.deleteProduct(p.getName(), p.getPrice());
								  session.setAttribute("plist", DM.getAllProducts());
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
							  ArrayList<product> plist = DM.getAllProducts();
							  if(plist==null) {
								  message="SOMETHING UNEXPECTED HAPPENED";
								  session.setAttribute("message", message);
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }	  
							  session.setAttribute("plist", plist);
							  response.sendRedirect("/online_store_admin/productList.jsp");
							  }
						  }else if(path.compareTo("userList.jsp")==0) {
							  String admin = (String) session.getAttribute("adminLogged");
							  String act = request.getParameter("wButton");
							  String act2 =  request.getParameter("mButton");
							  if((admin==null || act==null) && (admin==null || act2==null)){
								  request.getRequestDispatcher("/error.jsp").forward(request, response); //error checking
							  }
							  else if(act2!=null) {
								  String [] values = act2.split("\\+");
								  String user = values[0].trim();
								  user u = DM.getUser(user);
								  session.setAttribute("user", u);
								  session.setAttribute("ulist", DM.getAllUsers());
								  response.sendRedirect("/online_store_admin/userProfile.jsp");
							  }else {
								  String [] values = act.split("\\+");
								  String user = values[0].trim();
								  user u = DM.getUser(user);
								  ArrayList<user> ulist = DM.getAllUsers();
								  if(ulist==null) {
									  message="THERE ARE NO USERS";
									  session.setAttribute("message", message);
									  request.getRequestDispatcher("/error.jsp").forward(request, response);
								  }else{
									  DM.deleteUser(u.getMail());
									  session.setAttribute("ulist", DM.getAllUsers());
									  response.sendRedirect("/online_store_admin/userList.jsp");
								  }
							  }
						  }else if(path.compareTo("userProfile.jsp")==0) {
							  String act = request.getParameter("modifyUser");
							  String admin = (String) session.getAttribute("adminLogged");
							  user u = (user) session.getAttribute("user");
							  if(act==null || admin==null || u==null) {
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							  }else{
								  String name = request.getParameter("c_name");
								  if(name.isEmpty()) name = u.getName();
								  String surname = request.getParameter("c_surname");
								  if(surname.isEmpty()) surname= u.getSurname();
								  //String email = request.getParameter("c_email");
								  //if(email.isEmpty()) email = u.getMail();
								  String phone= request.getParameter("c_phone");
								  if(phone.equals("111111111")) phone = u.getPhone();
								  String address= request.getParameter("c_address");
								  if(address.isEmpty()) address = u.getAddress();
								  String image = request.getParameter("c_image");
								  if(image==null) image = u.getImagePath();
								  String seller = request.getParameter("c_seller");
								  boolean bseller;
								  if(seller==null) bseller = false;
								  else bseller = true;
								  //user(String uName, String uSurname, String uPhone, String uAddr, String uMail, String uPath, boolean uSell)
								  user updated=new user(name, surname, phone, address, u.getMail(), image, bseller);
								  DM.forceUserInsert(updated);
								  ArrayList<user> ulist = DM.getAllUsers();
								  if(ulist==null) {
									  message="SOMETHING UNEXPECTED HAPPENED";
									  session.setAttribute("message", message);
									  request.getRequestDispatcher("/error.jsp").forward(request, response);
								  }	  
								  session.setAttribute("ulist", ulist);
								  response.sendRedirect("/online_store_admin/userList.jsp");
								  }
							  }else if(path.compareTo("MailBox.jsp")==0) {
									String admin=(String) session.getAttribute("adminLogged"); //obtain the actual logged user
									if (admin==null) {
										request.getRequestDispatcher("/error.jsp").forward(request, response);
									}
									else {
										String messageR=request.getParameter("message");
										String dest=request.getParameter("dest");
										try {
											// - In the following steps we write the message and send it
											// First create a connection using the connectionFactory
											Connection c=projectFactory.createConnection();

											// Next create the session. Indicate that transaction will not be supported
												Session s=c.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);
											// Now use the session to create a message producer associated to the queue
												MessageProducer producer=s.createProducer(messages);
											 // Now use the session to create a text message
												TextMessage m=s.createTextMessage();
												m.setStringProperty("orig", admin);
												m.setStringProperty("dest",dest);
										//	We retrieve the parameter 'message' from the request, and use it as text of our message
												m.setText(messageR);
											// Use the message producer to send the message
												producer.send(m);

											// Close the producer
												producer.close();
											// Close the session
											s.close();
											// Close the connection
											c.close();
										}
										catch(Exception e) {
											request.getRequestDispatcher("/error.jsp").forward(request, response);
										}
										response.sendRedirect("/online_store_admin/MailBox.jsp");
									}
							  }
					  
					  }
		/*private void cleanQueue(String admin) {
			try {
				// First create a connection using the connectionFactory
				Connection c = projectFactory.createConnection();
				// Next create the session. Indicate that transaction will not be supported
				Session s = c.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);
				// Start connection
				c.start();
				// The filter that takes only the notifications where the destination is the
				// user email is defined
				String filter = "dest = '" + admin + "'";
				// Use the session to create a consumer linked to the conf queue and the filter
				// from user confirmations
				MessageConsumer co = s.createConsumer(conf, filter);
				// The message is defined
				Message mensaje = null;
				// Infinite while loop until the message is received
				while (true) {
					// Use the message consumer to try to retrieve a message. Not timing (wait until
					// the message arrives)
					mensaje = co.receive(500);
					// If the message arrives
					if (mensaje != null) {

					} else {
						break;
					}
				}
				// The session and the connection is closed
				s.close();
				c.close();
			} catch (Exception e) { // In case of exception the user is redirected to error

			}
		}*/

	}
