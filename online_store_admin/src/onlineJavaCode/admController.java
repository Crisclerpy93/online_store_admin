package onlineJavaCode;

import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.jersey.client.ClientConfig;

//import com.sun.tools.ws.wsdl.document.Output;

import model.Category;
import model.Product;
import model.User;
import model.Message;
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
	ClientConfig configclient;
	Client client;
	WebTarget webtarget;
	WebTarget webtargetPath;
	Invocation.Builder invocationBuilder;
	Response responsews;
	HttpSession session;

	// Constructor of the class
	public admController() {
		super();

	}

	// Init method executed when the server is activated
	public void init(ServletConfig config) throws ServletException {
		// Configuration of the server
		this.config = config;
		// The context of the server is obtained
		context = config.getServletContext();
		// The configuration and the client to connect with the different web services
		// is developed
		configclient = new ClientConfig();
		client = ClientBuilder.newClient(configclient);

	}

	// doGet method that is executed when the GET method is invoked, it is going to
	// redirect to the index page
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		context.getRequestDispatcher("/index.jsp").forward(request, response);
	}

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
				// setting the port to make the call
				webtarget = client.target("http://localhost:15205");
				// setting the path to make the call
				webtargetPath = webtarget.path("admin/users/" + admin);
				// invoking the microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting the result
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				User act = responsews.readEntity(User.class);
				// If admin is not found in the database
				if (status == 404) {
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
					if (!hash.equals(act.getPassHash())) {
						// Error message
						message = "INCORRECT PASSWORD";
						session.setAttribute("message", message);
						// Forward to login again to display the message
						response.sendRedirect("/online_store_admin/login.jsp");
					} else {// If the password is correct
							// Saving the logged admin information in the session
						session.setAttribute("admin", act);
						session.setAttribute("adminLogged", act.getMail());
						// seeting port for the microservice
						webtarget = client.target("http://localhost:15205");
						// setting path to call
						webtargetPath = webtarget.path("admin/users");
						// invoking service
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting result
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						User[] ac = responsews.readEntity(User[].class);
						// If the list of users is not found, the user is redirected to error
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							// The list of user is obtained
							List<User> users = Arrays.asList(ac);
							// setting port of microservice
							webtarget = client.target("http://localhost:15205");
							// setting path to make the call
							webtargetPath = webtarget.path("admin/products");
							// invoking microservice
							invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
							// getting response
							responsews = invocationBuilder.get();
							status = responsews.getStatus();
							// If the list of products is not found, the user is redirected to error
							Product[] list = responsews.readEntity(Product[].class);
							if (status == 404) {
								// Forward to error to display the message
								response.sendRedirect("/online_store_admin/error.jsp");
							} else {
								// The two lists are put in the session
								List<Product> articles = Arrays.asList(list);
								session.setAttribute("plist", articles);
								session.setAttribute("ulist", users);
								// Redirecting to the user profile
								response.sendRedirect("/online_store_admin/initPage.jsp");
							}
						}
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("productList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // Obtain the actual logged administrator
			// Obtain buttons values to know which has been pressed
			String act = request.getParameter("wButton");
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) { // Error checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else if (act2 != null) { // If administrator has pressed modify button
				// setting the port for the microservice
				webtarget = client.target("http://localhost:15205");
				// setting the path for the microservice
				webtargetPath = webtarget.path("admin/products/" + Integer.parseInt(act2));
				// invoking microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Product p = responsews.readEntity(Product.class);
				// If the product is not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					session.setAttribute("product", p); // Keep it in session
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/products");
					// invoking microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					Product[] list = responsews.readEntity(Product[].class);
					// If the list of products is not found, the user is redirected to error
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						List<Product> articles = Arrays.asList(list);
						session.setAttribute("plist", articles); // Keep the whole products list in session
						response.sendRedirect("/online_store_admin/modifyProduct.jsp"); // Redirect administrator to
																						// modify page
					}
				}
			} else { // If administrator has pressed delete button
				// setting the port to make the call
				webtarget = client.target("http://localhost:15205");
				// setting the path to make the call
				webtargetPath = webtarget.path("admin/products/" + Integer.parseInt(act));
				// invoking microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.delete();
				int status = responsews.getStatus();
				// If the product is not deleted, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/products");
					// invoking microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					Product[] list = responsews.readEntity(Product[].class);
					// If the product list is not found, the user is redirected to error
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						List<Product> articles = Arrays.asList(list);
						session.setAttribute("plist", articles); // update product list and keep it session
						response.sendRedirect("/online_store_admin/productList.jsp"); // Go back to product list
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("modifyProduct.jsp") == 0) {
			String act = request.getParameter("modifyProduct"); // Obtain button value
			String admin = (String) session.getAttribute("adminLogged"); // Obtain logged administrator
			Product p = (Product) session.getAttribute("product"); // Obtain product to modify
			if (act == null || admin == null || p == null) { // Errors checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else { // Getting values from the modification form and giving format to those in need
				String name = request.getParameter("c_name");
				String price = request.getParameter("c_price");
				float priceF = Float.parseFloat(price);
				String shortD = request.getParameter("c_shortDesc");
				String longD = request.getParameter("c_longDesc");
				Part filePart2 = request.getPart("c_image");
				// The image is transformed
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
				String stock = request.getParameter("c_stock");
				int stockI = Integer.parseInt(stock);
				String cat = request.getParameter("c_categories");
				String scat = request.getParameter("c_subCategories");
				String sscat = request.getParameter("c_speCategories");
				// The category is going to be checked
				boolean check = false;
				// setting the port to make the call
				webtarget = client.target("http://localhost:15205");
				// setting the path to make the call
				webtargetPath = webtarget.path("admin/categories/parents");
				// invoking microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Category[] catlist = responsews.readEntity(Category[].class);
				// If the category is not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// The parent category is searched
					List<Category> par = Arrays.asList(catlist);
					Category parent = null;
					for (int i = 0; i < par.size(); i++) {
						if (par.get(i).getName().equals(cat)) {
							parent = par.get(i);
							break;
						}
					}
					// If the parent category is correct
					if (parent != null) {
						// setting the port to make the call
						webtarget = client.target("http://localhost:15205");
						// setting the path to make the call
						webtargetPath = webtarget.path("admin/categories/sons/" + parent.getCatID());
						// invoking microservice
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						Category[] catlist2 = responsews.readEntity(Category[].class);
						// If the category is not found, the user is redirected to error
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							// The son category is searched
							List<Category> s = Arrays.asList(catlist2);
							Category son = null;
							for (int i = 0; i < s.size(); i++) {
								if (s.get(i).getName().equals(scat)) {
									son = s.get(i);
									break;
								}
							}
							// If the son category is correct
							if (son != null) {
								// setting the port to make the call
								webtarget = client.target("http://localhost:15205");
								// setting the path to make the call
								webtargetPath = webtarget.path("admin/categories/sons/" + son.getCatID());
								// invoking microservice
								invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
								// getting response
								responsews = invocationBuilder.get();
								status = responsews.getStatus();
								Category[] catlist3 = responsews.readEntity(Category[].class);
								// If the categories are not found, the user is redirected to error
								if (status == 404) {
									// Forward to error to display the message
									response.sendRedirect("/online_store_admin/error.jsp");
								} else {
									// The grandson category is searched
									List<Category> sub = Arrays.asList(catlist3);
									Category subson = null;
									for (int i = 0; i < sub.size(); i++) {
										if (sub.get(i).getName().equals(sscat)) {
											subson = sub.get(i);
											break;
										}
									}
									// If the grandson category exists, the product is modified
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
					// setting port of the microservice
					webtarget = client.target("http://localhost:15205");
					// setting path of the microservice
					webtargetPath = webtarget.path("admin/products/" + p.getId());
					// invoking microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.put(Entity.entity(p, MediaType.APPLICATION_JSON));
					status = responsews.getStatus();
					p = responsews.readEntity(Product.class);
					// If the product is not updated, the user is redirected to error
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						// setting port of microservice
						webtarget = client.target("http://localhost:15205");
						// setting path to make the call
						webtargetPath = webtarget.path("admin/products");
						// invoking microservice
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						Product[] list = responsews.readEntity(Product[].class);
						// If the product list is not found, the user is redirected to error
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							List<Product> articles = Arrays.asList(list);
							session.setAttribute("plist", articles); // update product list and keep it session
							response.sendRedirect("/online_store_admin/productList.jsp"); // Go back to product list
						}
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("userList.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // Obtain the actual logged administrator
			// Obtain buttons values to know which has been pressed
			String act = request.getParameter("wButton");
			String act2 = request.getParameter("mButton");
			if ((admin == null || act == null) && (admin == null || act2 == null)) { // Error checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else if (act2 != null) { // If administrator has pressed modify button
				// setting port of microservice
				webtarget = client.target("http://localhost:15205");
				// setting path to make the call
				webtargetPath = webtarget.path("admin/users/" + act2);
				// Invoking microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				User ac = responsews.readEntity(User.class);
				// If the user is not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					session.setAttribute("user", ac); // Keep it in session
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/users");
					// invoking microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User[] list = responsews.readEntity(User[].class);
					// If the list of user is not found, the user is redirected to error
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						List<User> users = Arrays.asList(list);
						session.setAttribute("ulist", users); // Keep the whole users list in session
						response.sendRedirect("/online_store_admin/userProfile.jsp"); // Redirect administrator to
																						// modify page
					}
				}
			} else { // If administrator has pressed delete button
				// setting port of microservice
				webtarget = client.target("http://localhost:15205");
				// setting path to make the call
				webtargetPath = webtarget.path("admin/users/" + act);
				// invoking microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				User u = responsews.readEntity(User.class);
				// If the user is not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					if (u.getIsSeller() && u.getProducts1().size() != 0) { // Checking if user is a seller and the list
																			// of product is empty
						message = "A SELLER WITH AVAILABLE PRODUCTS CANNOT BE DELETED";
						session.setAttribute("message", message);
						response.sendRedirect("/online_store_admin/userList.jsp");
					} else {
						// Deleting user and updating users list
						// setting port of microservice
						webtarget = client.target("http://localhost:15205");
						// setting path to make the call
						webtargetPath = webtarget.path("admin/users/" + u.getMail());
						// invoking microservice
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.delete();
						status = responsews.getStatus();
						// If the user is not deleted, the user is redirected to error
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							// setting port of microservice
							webtarget = client.target("http://localhost:15205");
							// setting path to make the call
							webtargetPath = webtarget.path("admin/users");
							// invoking microservice
							invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
							// getting response
							responsews = invocationBuilder.get();
							status = responsews.getStatus();
							User[] list = responsews.readEntity(User[].class);
							// If the list of users is not found, the user is redirected to error
							if (status == 404) {
								// Forward to error to display the message
								response.sendRedirect("/online_store_admin/error.jsp");
							} else {
								List<User> users = Arrays.asList(list);
								// Go back to users list
								session.setAttribute("ulist", users); // Keep the whole users list in session
								response.sendRedirect("/online_store_admin/userList.jsp");
							}
						}
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("userProfile.jsp") == 0) {
			String act = request.getParameter("modifyUser"); // Obtain button value
			String admin = (String) session.getAttribute("adminLogged"); // Obtain logged administrator
			User u = (User) session.getAttribute("user"); // Obtain user to modify
			if (act == null || admin == null || u == null) { // errors checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else { // Getting values from the modification form and giving format to those in need
				String name = request.getParameter("c_name");
				String surname = request.getParameter("c_surname");
				String phone = request.getParameter("c_phone");
				String address = request.getParameter("c_address");
				Part filePart2 = request.getPart("c_image");
				// The image is transformed
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
				String seller = request.getParameter("c_seller");
				// The seller option is transformed to boolean
				boolean bseller;
				if (seller == null)
					bseller = false;
				else
					bseller = true;
				// The values are setting in the user
				u.setName(name);
				u.setPhone(phone);
				u.setSurname(surname);
				u.setAddress(address);
				u.setImage(data2);
				u.setIsSeller(bseller);
				// setting port of microservice
				webtarget = client.target("http://localhost:15205");
				// setting path plus email as variable
				webtargetPath = webtarget.path("admin/users/" + u.getMail());
				// calling service
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.put(Entity.entity(u, MediaType.APPLICATION_JSON));
				int status = responsews.getStatus();
				// If the user is not updated, it is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/users");
					// invoking microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User[] list = responsews.readEntity(User[].class);
					// If the list of users is not found, the user is redirected to error
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						List<User> ulist = Arrays.asList(list);
						// Checking errors
						if (ulist == null) {
							message = "SOMETHING UNEXPECTED HAPPENED";
							session.setAttribute("message", message);
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
						// The list of user is put in the session and the user is redirected to the user
						// list page
						session.setAttribute("ulist", ulist);
						response.sendRedirect("/online_store_admin/userList.jsp");
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("initPage.jsp") == 0) {
			// The button is captured
			String act = request.getParameter("mailbox");
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null || act == null) { // Error checking
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				// The pair where the text and the sender is kept is declared
				Pair<ArrayList<String>, ArrayList<String>> listMessage = new Pair<ArrayList<String>, ArrayList<String>>(
						new ArrayList<String>(), new ArrayList<String>());
				// The message are obtained
				// setting port to call microservice
				webtarget = client.target("http://localhost:15203");
				// setting path to call microservice
				webtargetPath = webtarget.path("chat/messages/" + admin);
				// invoking the microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Message[] messages = responsews.readEntity(Message[].class);
				// If the message are not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// The pair is completed with the text and the seller
					for (int i = 0; i < messages.length; i++) {
						listMessage.getValue().add(messages[i].getUser1().getMail());
						listMessage.getKey().add(messages[i].getText());
					}
					// The list of messages is put in the session and the administrator is
					// redirected to the
					// mailbox
					session.setAttribute("messages", listMessage);
					response.sendRedirect("/online_store_admin/MailBox.jsp");
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("MailBox.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null) { // if the admin is not registered
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				// The text and the destination are obtained
				String messageR = request.getParameter("message");
				String dest = request.getParameter("dest");
				// The message is created
				Message m = new Message();
				// setting port for the microservice
				webtarget = client.target("http://localhost:15205");
				// setting path for the microservice
				webtargetPath = webtarget.path("admin/users/" + admin);
				// making the call
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				User ac = responsews.readEntity(User.class);
				// If the sender is not found, the user is redirected to error
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port microservice
					webtarget = client.target("http://localhost:15205");
					// setting path for the microservice
					webtargetPath = webtarget.path("admin/users/" + dest);
					// performing call
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User ac2 = responsews.readEntity(User.class);
					// If the user is not found, the user is redirected to mailbox to see a message
					// saying that the destination does not exist
					if (status == 404) {
						// create and set the message in the session
						message = "NOT EXISTS THIS RECEIVER";
						session.setAttribute("message", message);
						// Forward to mailbox to display the message
						response.sendRedirect("/online_store_admin/MailBox.jsp");
					} else {
						// put the parameters in the message
						m.setUser1(ac);
						m.setUser2(ac2);
						m.setText(messageR);
						m.setBroadcast(false);
						// setting port of the microservice
						webtarget = client.target("http://localhost:15203");
						// setting path for the microservice
						webtargetPath = webtarget.path("chat");
						// invoking service
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.post(Entity.entity(m, MediaType.APPLICATION_JSON));
						status = responsews.getStatus();
						// If the message is not sent, the user is redirected to error
						if (status == 200) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							// The user is redirected to mailbox in order to he/she can continue sending
							// messages
							response.sendRedirect("/online_store_admin/MailBox.jsp");
						}
					}
				}
			}
		}
	}
}
