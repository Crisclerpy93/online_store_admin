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

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	// Constructor of the class
	public admController() {
		super();

	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// doGet method that is executed when the GET method is invoked, it is going to
	// redirect to the index page
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
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
				// setting microservice path
				webtarget = client.target("http://localhost:15205");
				// setting call
				webtargetPath = webtarget.path("admin/users/" + admin);
				// making call
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// result
				responsews = invocationBuilder.get();
				// status returned by call
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
						// invoquing service
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting result
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						User[] ac = responsews.readEntity(User[].class);
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							List<User> users = Arrays.asList(ac);
							// setting port of microservice
							webtarget = client.target("http://localhost:15205");
							// setting path to make the call
							webtargetPath = webtarget.path("admin/products");
							// invoquing microservice
							invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
							// getting response
							responsews = invocationBuilder.get();
							status = responsews.getStatus();
							Product[] list = responsews.readEntity(Product[].class);
							if (status == 404) {
								// Forward to error to display the message
								response.sendRedirect("/online_store_admin/error.jsp");
							} else {
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
				webtarget = client.target("http://localhost:15205");
				webtargetPath = webtarget.path("admin/products/getById/" + Integer.parseInt(act2));
				// invoquing microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Product p = responsews.readEntity(Product.class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					session.setAttribute("product", p); // Keep it in session
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/products");
					// invoquing microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					Product[] list = responsews.readEntity(Product[].class);
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
				webtarget = client.target("http://localhost:15205");
				webtargetPath = webtarget.path("admin/products/delete/" + Integer.parseInt(act));
				// invoquing microservice
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.delete();
				int status = responsews.getStatus();
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/products");
					// invoquing microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					Product[] list = responsews.readEntity(Product[].class);
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
				byte[] data2 = new byte[(int) filePart2.getSize()];
				filePart2.getInputStream().read(data2, 0, data2.length);
				String stock = request.getParameter("c_stock");
				int stockI = Integer.parseInt(stock);
				String cat = request.getParameter("c_categories");
				String scat = request.getParameter("c_subCategories");
				String sscat = request.getParameter("c_speCategories");
				// The new product object is defined
				boolean check = false;
				// setting the port to make the call
				webtarget = client.target("http://localhost:15205");
				webtargetPath = webtarget.path("/admin/categories/parents");
				// invoquing
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Category[] catlist = responsews.readEntity(Category[].class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					List<Category> par = Arrays.asList(catlist);
					Category parent = null;
					for (int i = 0; i < par.size(); i++) {
						if (par.get(i).getName().equals(cat)) {
							parent = par.get(i);
							break;
						}
					}
					if (parent != null) {
						// setting the port to make the call
						webtarget = client.target("http://localhost:15205");
						webtargetPath = webtarget.path("/admin/categories/sons/" + parent.getCatID());
						// invoquing
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						Category[] catlist2 = responsews.readEntity(Category[].class);
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							List<Category> s = Arrays.asList(catlist2);
							Category son = null;
							for (int i = 0; i < s.size(); i++) {
								if (s.get(i).getName().equals(scat)) {
									son = s.get(i);
									break;
								}
							}
							if (son != null) {
								// setting the port to make the call
								webtarget = client.target("http://localhost:15205");
								webtargetPath = webtarget.path("/admin/categories/sons/" + son.getCatID());
								// invoquing
								invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
								// getting response
								responsews = invocationBuilder.get();
								status = responsews.getStatus();
								Category[] catlist3 = responsews.readEntity(Category[].class);
								if (status == 404) {
									// Forward to error to display the message
									response.sendRedirect("/online_store_admin/error.jsp");
								} else {
									List<Category> sub = Arrays.asList(catlist3);
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
					webtarget = client.target("http://localhost:15205");
					webtargetPath = webtarget.path("/admin/products/update/" + p.getId());
					// invoquing
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					responsews = invocationBuilder.put(Entity.entity(p, MediaType.APPLICATION_JSON));
					status = responsews.getStatus();
					p = responsews.readEntity(Product.class);
					if (status == 404) {
						// Forward to error to display the message
						response.sendRedirect("/online_store_admin/error.jsp");
					} else {
						// setting port of microservice
						webtarget = client.target("http://localhost:15205");
						// setting path to make the call
						webtargetPath = webtarget.path("admin/products");
						// invoquing microservice
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.get();
						status = responsews.getStatus();
						Product[] list = responsews.readEntity(Product[].class);
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
				webtarget = client.target("http://localhost:15205");
				// setting call
				webtargetPath = webtarget.path("/admin/users/" + act2);
				// making call
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// result
				responsews = invocationBuilder.get();
				// status returned by call
				int status = responsews.getStatus();
				User ac = responsews.readEntity(User.class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					session.setAttribute("user", ac); // Keep it in session
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/users");
					// invoquing microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User[] list = responsews.readEntity(User[].class);
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
				webtarget = client.target("http://localhost:15205");
				// setting call
				webtargetPath = webtarget.path("/admin/users/" + act);
				// making call
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// result
				responsews = invocationBuilder.get();
				// status returned by call
				int status = responsews.getStatus();
				User u = responsews.readEntity(User.class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					if (u.getIsSeller() && u.getProducts1().size() != 0) { // Checking if user is a seller
						message = "A SELLER WITH AVAILABLE PRODUCTS CANNOT BE DELETED";
						session.setAttribute("message", message);
						response.sendRedirect("/online_store_admin/userList.jsp");
					} else {
						// Deleting user and updating users list
						webtarget = client.target("http://localhost:15205");
						webtargetPath = webtarget.path("admin/users/" + u.getMail());
						// invoquing microservice
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.delete();
						status = responsews.getStatus();
						if (status == 404) {
							// Forward to error to display the message
							response.sendRedirect("/online_store_admin/error.jsp");
						} else {
							// setting port of microservice
							webtarget = client.target("http://localhost:15205");
							// setting path to make the call
							webtargetPath = webtarget.path("admin/users");
							// invoquing microservice
							invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
							// getting response
							responsews = invocationBuilder.get();
							status = responsews.getStatus();
							User[] list = responsews.readEntity(User[].class);
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
				// setting port of microservice
				webtarget = client.target("http://localhost:15205");
				// setting path plus email as variable
				webtargetPath = webtarget.path("admin/users/" + u.getMail());
				// calling service
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.put(Entity.entity(u, MediaType.APPLICATION_JSON));
				int status = responsews.getStatus();
				// User ac2 = responsews.readEntity(User.class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port of microservice
					webtarget = client.target("http://localhost:15205");
					// setting path to make the call
					webtargetPath = webtarget.path("admin/users");
					// invoquing microservice
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User[] list = responsews.readEntity(User[].class);
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
						// The user is updated in the database
						session.setAttribute("ulist", ulist);
						response.sendRedirect("/online_store_admin/userList.jsp");
					}
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("initPage.jsp") == 0) {
			String act = request.getParameter("mailbox");
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null || act == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				Pair<ArrayList<String>, ArrayList<String>> listMessage = new Pair<ArrayList<String>, ArrayList<String>>(
						new ArrayList<String>(), new ArrayList<String>());
				// setting port to call microservice
				webtarget = client.target("http://localhost:15203");
				webtargetPath = webtarget.path("chat/receive/messages/" + admin);
				// invoquing
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				Message[] messages = responsews.readEntity(Message[].class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// The list of messages is put in the session and the administrator is
					// redirected to the
					// mailbox
					for (int i = 0; i < messages.length; i++) {
						listMessage.getValue().add(messages[i].getUser1().getMail());
						listMessage.getKey().add(messages[i].getText());
					}
					session.setAttribute("messages", listMessage);
					response.sendRedirect("/online_store_admin/MailBox.jsp");
				}
			}
			// Look where the request comes from
		} else if (path.compareTo("MailBox.jsp") == 0) {
			String admin = (String) session.getAttribute("adminLogged"); // obtain the actual logged administrator
			if (admin == null) {
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				String messageR = request.getParameter("message");
				String dest = request.getParameter("dest");
				Message m = new Message();
				// setting port for the microservice
				webtarget = client.target("http://localhost:15205");
				// path plus the user variable
				webtargetPath = webtarget.path("admin/users/" + admin);
				// making the call
				invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
				// getting response
				responsews = invocationBuilder.get();
				int status = responsews.getStatus();
				User ac = responsews.readEntity(User.class);
				if (status == 404) {
					// Forward to error to display the message
					response.sendRedirect("/online_store_admin/error.jsp");
				} else {
					// setting port microservice
					webtarget = client.target("http://localhost:15205");
					// path plus the destination user for making the call
					webtargetPath = webtarget.path("admin/users/" + dest);
					// performing call
					invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
					// getting response
					responsews = invocationBuilder.get();
					status = responsews.getStatus();
					User ac2 = responsews.readEntity(User.class);
					if (status == 404) {
						// Forward to mailbox to display the message
						message="NOT EXISTS THIS RECEIVER";
						session.setAttribute("message", message);
						response.sendRedirect("/online_store_admin/MailBox.jsp");
					} else {
						m.setUser1(ac);
						m.setUser2(ac2);
						m.setText(messageR);
						m.setBroadcast(false);
						// setting port of the microservice
						webtarget = client.target("http://localhost:15203");
						// path to call
						webtargetPath = webtarget.path("chat/send");
						// invoquing service
						invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
						// getting response
						responsews = invocationBuilder.post(Entity.entity(m, MediaType.APPLICATION_JSON));
						status = responsews.getStatus();
						// Message correct = responsews.readEntity(Message.class);
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
