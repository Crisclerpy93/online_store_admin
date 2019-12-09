package onlineJavaCode;

import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.codec.digest.DigestUtils;

//import com.sun.tools.ws.wsdl.document.Output;

import model.Category;
import model.Product;
import model.User;
import javafx.util.Pair;

/**
 * Servlet implementation class admController
 */
@WebServlet("/admController")
@MultipartConfig
public class admController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Global variables
	public ServletConfig config;
	public ServletContext context;
	dataManager DM;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	// Constructor of the class
	public admController() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Global variables that represent the factory and the queues of communication
	private ConnectionFactory projectFactory;
	private Queue messages;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	// Init method executed when the server is activated
	public void init(ServletConfig config) throws ServletException {
		// Configuration of the server
		this.config = config;
		// The context of the server is obtained
		context = config.getServletContext();
		// Initialize the data manager that connects with the database
		DM = new dataManager();
		// The resources of the factory and the queues are obtained from the server
		try {
			Context icontext = (Context) new InitialContext();
			projectFactory = (ConnectionFactory) icontext.lookup("projectFactory");
			messages = (Queue) icontext.lookup("messages");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// doGet method that is executed when the GET method is invoked, it is going to
	// redirect to the index page
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		context.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// doPost method that is executed when the POST method is invoked
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// The session obtained from the request
		session = request.getSession();
		// Getting information of the url from request
		String path = request.getHeader("referer");
		String[] arr = path.split("/");
		path = (arr[arr.length - 1]);
		// Preparing the information message that is sent in case of errors o
		// successfulness
		String message = null;
		session.setAttribute("message", message);
		// Once done, look where the request comes from
		if (path.compareTo("login.jsp") == 0) {
			// This is used to check if the email has the correct format
			String regex = "^(.+)@(.+)$";
			Pattern pattern = Pattern.compile(regex);
			// Getting parameters from the request
			String admin = request.getParameter("c_email");
			String pass = request.getParameter("c_password");
			// Error checking
			if (admin == null) {
				response.sendRedirect("/online_store_admin/error.jsp");
			}
			// Matcher is used to see if the email has the correct format
			Matcher matcher = pattern.matcher(admin);
			// Case where email is wrong
			if (matcher.matches() == false) {
				// Error message
				message = "BAD FORMAT EMAIL";
				session.setAttribute("message", message);
				// Forward to login again to display the message
				response.sendRedirect("/online_store_admin/login.jsp");
			} else {// If the email has correct format
					// Check if the user exists in database
				User act = DM.getUser(admin); // Check name in database
				// If admin is not found in the database
				if (act == null) {
					// Error message
					message = "USER IS NOT REGISTERED";
					session.setAttribute("message", message);
					// Forward to login again to display the message
					response.sendRedirect("/online_store_admin/login.jsp");
					// If the user is not an administrator
				} else if (!act.getMail().contains("@admin.com")) {
					// Error message
					message = "SORRY, YOU ARE NOT AN ADMINISTRATOR";
					session.setAttribute("message", message);
					// Forward to login again to display the message
					response.sendRedirect("/online_store_admin/login.jsp");
				} else {// If the user exists in the database
						// Check if the introduced password is equal to the database
					String hash = DigestUtils.sha256Hex(pass);// Check if it exists and good password
					// Case the password is not the same
					if (!hash.equals(DM.getUser(admin).getPassHash())) {
						// Error message
						message = "INCORRECT PASSWORD";
						session.setAttribute("message", message);
						// Forward to login again to display the message
						response.sendRedirect("/online_store_admin/login.jsp");
					} else {// If the password is correct
							// Saving the logged admin information in the session
						session.setAttribute("admin", act);
						session.setAttribute("adminLogged", act.getMail());
						session.setAttribute("plist", DM.getAllProducts());
						session.setAttribute("ulist", DM.getAllUsers());
						// Redirecting to the user profile
						response.sendRedirect("/online_store_admin/initPage.jsp");
					}
				}
			}
		//Look where the request comes from
		} else if (path.compareTo("productList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); //Obtain the actual logged administrator
			//Obtain buttons values to know which has been pressed
			String act = request.getParameter("wButton"); 
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) { //Error checking
				request.getRequestDispatcher("/error.jsp").forward(request, response); 
			} else if (act2 != null) { //If administrator has pressed modify button
				Product p = DM.getProduct(Integer.parseInt(act2)); //Obtain the product to modify
				session.setAttribute("product", p); //Keep it in session
				session.setAttribute("plist", DM.getAllProducts()); //Keep the whole products list in session
				response.sendRedirect("/online_store_admin/modifyProduct.jsp"); //Redirect administrator to modify page
			} else { //If administrator has pressed delete button
				Product p = DM.getProduct(Integer.parseInt(act)); //Obtain the product to delete
				List<Product> plist = DM.getAllProducts(); //Obtain the whole products list
				if (plist == null) { //Checking if there are products
					message = "THERE ARE NO AVAILABLE PRODUCTS";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				} else {
					DM.deleteProduct(p); //Delete product
					session.setAttribute("plist", DM.getAllProducts()); //update product list and keep it in session
					response.sendRedirect("/online_store_admin/productList.jsp"); //Go back to product list
				}
			}
		//Look where the request comes from
		} else if (path.compareTo("modifyProduct.jsp") == 0) {
			String act = request.getParameter("modifyProduct"); //Obtain button value
			String admin = (String) session.getAttribute("adminLogged"); //Obtain logged administrator
			Product p = (Product) session.getAttribute("product"); //Obtain product to modify
			if (act == null || admin == null || p == null) { //Errors checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else { //Getting values from the modification form and giving format to those in need
				String name = request.getParameter("c_name");
				String price = request.getParameter("c_price");
				float priceF = Float.parseFloat(price);
				String shortD = request.getParameter("c_shortDesc");
				String longD = request.getParameter("c_longDesc");
				Part filePart2 = request.getPart("c_image");
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
				String stock = request.getParameter("c_stock");
				int stockI = Integer.parseInt(stock);
				String cat = request.getParameter("c_categories");
				String scat = request.getParameter("c_subCategories");
				String sscat = request.getParameter("c_speCategories");
				// The new product object is defined
				boolean check = false;
				List<Category> par = DM.getParentCategories();
				Category parent = null;
				for (int i = 0; i < par.size(); i++) {
					if (par.get(i).getName().equals(cat)) {
						parent = par.get(i);
						break;
					}
				}
				if (parent != null) {
					List<Category> s = DM.getSubCategories(parent);
					Category son = null;
					for (int i = 0; i < s.size(); i++) {
						if (s.get(i).getName().equals(scat)) {
							son = s.get(i);
							break;
						}
					}
					if (son != null) {
						List<Category> sub = DM.getSubCategories(son);
						Category subson = null;
						for (int i = 0; i < sub.size(); i++) {
							if (sub.get(i).getName().equals(sscat)) {
								subson = sub.get(i);
								break;
							}
						}
						if (subson != null) {
							p.setName(name);
							p.setPrice(priceF);
							p.setShortDesc(shortD);
							p.setLongDesc(longD);
							p.setImage(data2);
							p.setStock(stockI);
							p.setCategoryBean(subson);
							check = true;
						}
					}
				}
				// If the format is bad
				if (check == false) {
					message = "BAD FORMAT";
					session.setAttribute("message", message);
					response.sendRedirect("/online_store_admin/modifyProduct.jsp");
				} else {
					// The product is updated in the database
					DM.updateProduct(p);
					session.setAttribute("plist", DM.getAllProducts());
					response.sendRedirect("/online_store_admin/productList.jsp");
				}
			}
		//Look where the request comes from
		} else if (path.compareTo("userList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); //Obtain the actual logged administrator
			//Obtain buttons values to know which has been pressed
			String act = request.getParameter("wButton");
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) { //Error checking
				request.getRequestDispatcher("/error.jsp").forward(request, response); 
			} else if (act2 != null) { //If administrator has pressed modify button
				User u = DM.getUser(act2); //Obtain the user to modify
				session.setAttribute("user", u); //Keep it in session
				session.setAttribute("ulist", DM.getAllUsers()); //Keep the whole users list in session
				response.sendRedirect("/online_store_admin/userProfile.jsp"); //Redirect administrator to modify page
			} else { //If administrator has pressed delete button
				User u = DM.getUser(act); //Obtain the user to delete
				List<User> ulist = DM.getAllUsers(); //Obtain the whole users list
				if (ulist == null) { //Checking if there are users
					message = "THERE ARE NO USERS";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				} else {
					if(u.getIsSeller() && u.getProducts1().size()!=0) { //Checking if user is a seller
						message =  "A SELLER WITH AVAILABLE PRODUCTS CANNOT BE DELETED";
						session.setAttribute("message", message);
						response.sendRedirect("/online_store_admin/userList.jsp");
					}else {
						//Deleting user and updating users list
						DM.deleteUser(u);
						session.setAttribute("ulist", DM.getAllUsers());
						//Go back to users list
						response.sendRedirect("/online_store_admin/userList.jsp");
					}
				}
			}
		//Look where the request comes from
		} else if (path.compareTo("userProfile.jsp") == 0) {
			String act = request.getParameter("modifyUser"); //Obtain button value
			String admin = (String) session.getAttribute("adminLogged"); //Obtain logged administrator
			User u = (User) session.getAttribute("user"); //Obtain user to modify
			if (act == null || admin == null || u == null) { //errors checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else { //Getting values from the modification form and giving format to those in need
				String name = request.getParameter("c_name");
				String surname = request.getParameter("c_surname");
				String phone = request.getParameter("c_phone");
				String address = request.getParameter("c_address");
				Part filePart2 = request.getPart("c_image");
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
				String seller = request.getParameter("c_seller");
				// The new user object is defined
				boolean bseller;
				if (seller == null)
					bseller = false;
				else
					bseller = true;
				u.setName(name);
				u.setPhone(phone);
				u.setSurname(surname);
				u.setAddress(address);
				u.setImage(data2);
				u.setIsSeller(bseller);
				DM.updateUser(u);
				List<User> ulist = DM.getAllUsers();
				// Checking errors
				if (ulist == null) {
					message = "SOMETHING UNEXPECTED HAPPENED";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
				// The user is updated in the database
				session.setAttribute("ulist", ulist);
				response.sendRedirect("/online_store_admin/userList.jsp");
			}
		//Look where the request comes from
		} else if (path.compareTo("initPage.jsp") == 0) {
			String act = request.getParameter("mailbox");
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null || act == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				try {
					// First create a connection using the connectionFactory
					Connection c = projectFactory.createConnection();
					// Next create the session. Indicate that transaction will not be supported
					Session s = c.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);
					// Start connection
					c.start();
					// The filter that takes only the message where the destination is the admnistrator
					// email is defined
					String filter = "dest = '" + admin + "'";
					// Use the session to create a consumer linked to the message queue and the
					// filter of the user destinated messages
					MessageConsumer co = s.createConsumer(messages, filter);
					// A pair where the message list and the sender of the messages are kept is
					// defined
					Pair<ArrayList<String>, ArrayList<String>> listMessage = new Pair<ArrayList<String>, ArrayList<String>>(
							new ArrayList<String>(), new ArrayList<String>());
					// The message is defined
					Message mensaje = null;
					// Infinite loop until all the messages are received
					while (true) {
						// Use the message consumer to try to retrieve a message. Timing 500
						mensaje = co.receive(500);
						// If a message is received
						if (mensaje != null) {
							// We transform the message in a text message
							TextMessage t = (TextMessage) mensaje;
							// The text of the message and the sender (orig property) is kept in the list
							listMessage.getKey().add(t.getText());
							listMessage.getValue().add(t.getStringProperty("orig"));
						} else
							break;
					}
					// The session and the connection are closed
					s.close();
					c.close();
					// The list of messages is put in the session and the administrator is redirected to the
					// mailbox
					session.setAttribute("messages", listMessage);
					response.sendRedirect("/online_store_admin/MailBox.jsp");

				} catch (Exception e) { // If an exception is produced, the user is redirected to error
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
			}
		//Look where the request comes from
		} else if (path.compareTo("MailBox.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				String messageR = request.getParameter("message");
				String dest = request.getParameter("dest");
				try {
					// - In the following steps we write the message and send it
					// First create a connection using the connectionFactory
					Connection c = projectFactory.createConnection();

					// Next create the session. Indicate that transaction will not be supported
					Session s = c.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);
					// Now use the session to create a message producer associated to the queue
					MessageProducer producer = s.createProducer(messages);
					// Now use the session to create a text message
					TextMessage m = s.createTextMessage();
					m.setStringProperty("orig", admin);
					m.setStringProperty("dest", dest);
					// We retrieve the parameter 'message' from the request, and use it as text of
					// our message
					m.setText(messageR);
					// Use the message producer to send the message
					producer.send(m);
					// Close the producer
					producer.close();
					// Close the session
					s.close();
					// Close the connection
					c.close();
					//The administrator is redirected to the mailbox
					response.sendRedirect("/online_store_admin/MailBox.jsp");
				} catch (Exception e) {// If an exception is produced, the administrator is redirected to error
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
			}
		}
	}
}
