package onlineJavaCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.codec.digest.DigestUtils;

import com.sun.tools.ws.wsdl.document.Output;

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
		} else if (path.compareTo("productList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged");
			String act = request.getParameter("wButton");
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) {
				request.getRequestDispatcher("/error.jsp").forward(request, response); // error checking
			} else if (act2 != null) {
				Product p = DM.getProduct(Integer.parseInt(act2));
				session.setAttribute("product", p);
				session.setAttribute("plist", DM.getAllProducts());
				response.sendRedirect("/online_store_admin/modifyProduct.jsp");
			} else {
				Product p = DM.getProduct(Integer.parseInt(act));
				List<Product> plist = DM.getAllProducts();
				if (plist == null) {
					message = "THERE ARE NO AVAILABLE PRODUCTS";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				} else {
					DM.deleteProduct(p);
					session.setAttribute("plist", DM.getAllProducts());
					response.sendRedirect("/online_store_admin/productList.jsp");
				}
			}
		} else if (path.compareTo("modifyProduct.jsp") == 0) {
			String act = request.getParameter("modifyProduct");
			String admin = (String) session.getAttribute("adminLogged");
			Product p = (Product) session.getAttribute("product");
			if (act == null || admin == null || p == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
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
				p.setName(name);
				p.setPrice(priceF);
				p.setShortDesc(shortD);
				p.setLongDesc(longD);
				p.setImage(data2);
				p.setStock(stockI);
				List<Category> par = DM.getParentCategories();
				Category parent = new Category();
				for (int i = 0; i < par.size(); i++) {
					if (par.get(i).getName().equals(cat)) {
						parent = par.get(i);
						break;
					}
				}
				List<Category> s = DM.getSubCategories(parent);
				Category son = new Category();
				for (int i = 0; i < s.size(); i++) {
					if (s.get(i).getName().equals(scat)) {
						son = s.get(i);
						break;
					}
				}
				List<Category> sub = DM.getSubCategories(son);
				Category subson = new Category();
				for (int i = 0; i < sub.size(); i++) {
					if (sub.get(i).getName().equals(sscat)) {
						subson = sub.get(i);
						break;
					}
				}
				p.setCategoryBean(subson);
				// The product is updated in the database
				DM.updateProduct(p);
				session.setAttribute("plist", DM.getAllProducts());
				response.sendRedirect("/online_store_admin/productList.jsp");
			}
		} else if (path.compareTo("userList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged");
			String act = request.getParameter("wButton");
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) {
				request.getRequestDispatcher("/error.jsp").forward(request, response); // error checking
			} else if (act2 != null) {
				String[] values = act2.split("\\+");
				String user = values[0].trim();
				User u = DM.getUser(user);
				session.setAttribute("user", u);
				session.setAttribute("ulist", DM.getAllUsers());
				response.sendRedirect("/online_store_admin/userProfile.jsp");
			} else {
				User u = DM.getUser(act);
				List<User> ulist = DM.getAllUsers();
				if (ulist == null) {
					message = "THERE ARE NO USERS";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				} else {
					DM.deleteUser(u);
					session.setAttribute("ulist", DM.getAllUsers());
					response.sendRedirect("/online_store_admin/userList.jsp");
				}
			}
		} else if (path.compareTo("userProfile.jsp") == 0) {
			String act = request.getParameter("modifyUser");
			String admin = (String) session.getAttribute("adminLogged");
			User u = (User) session.getAttribute("user");
			if (act == null || admin == null || u == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				String name = request.getParameter("c_name");
				if (name.isEmpty())
					name = u.getName();
				String surname = request.getParameter("c_surname");
				if (surname.isEmpty())
					surname = u.getSurname();
				// String email = request.getParameter("c_email");
				// if(email.isEmpty()) email = u.getMail();
				String phone = request.getParameter("c_phone");
				if (phone.equals("111111111"))
					phone = u.getPhone();
				String address = request.getParameter("c_address");
				if (address.isEmpty())
					address = u.getAddress();
				Part filePart2 = request.getPart("c_image");
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
//				if (image == null)
//					image = u.getImagePath();
				String seller = request.getParameter("c_seller");
				boolean bseller;
				if (seller == null)
					bseller = false;
				else
					bseller = true;
				// user(String uName, String uSurname, String uPhone, String uAddr, String
				// uMail, String uPath, boolean uSell)
				u.setName(name);
				u.setPhone(phone);
				u.setSurname(surname);
				u.setAddress(address);
				u.setImage(data2);
				u.setIsSeller(bseller);
				DM.updateUser(u);
				List<User> ulist = DM.getAllUsers();
				if (ulist == null) {
					message = "SOMETHING UNEXPECTED HAPPENED";
					session.setAttribute("message", message);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
				session.setAttribute("ulist", ulist);
				response.sendRedirect("/online_store_admin/userList.jsp");
			}
		} else if (path.compareTo("initPage.jsp") == 0) {
			String act = request.getParameter("mailbox");
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged user
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
					// The filter that takes only the message where the destination is the user
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
					// The list of messages is put in the session and the user is redirected to the
					// mailbox
					session.setAttribute("messages", listMessage);
					response.sendRedirect("/online_store_admin/MailBox.jsp");

				} catch (Exception e) { // If an exception is produced, the user is redirected to error
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
			}
		} else if (path.compareTo("MailBox.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged user
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
				} catch (Exception e) {
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}
				response.sendRedirect("/online_store_admin/MailBox.jsp");
			}

		}
	}
}
