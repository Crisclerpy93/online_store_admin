package onlineJavaCode;

	import java.io.IOException;
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

	//import javafx.util.Pair;


	/**
	 * Servlet implementation class UserController
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
			context.getRequestDispatcher("/login.jsp").forward(request, response);
		}
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				session = request.getSession();
			// TODO Auto-generated method stub
					String path=request.getServletPath();
					String message;
					
					  if(path.compareTo("/login.jsp")==0) {
						  String regex = "^(.+)@(.+)$";
						  Pattern pattern = Pattern.compile(regex);
						  String user=request.getParameter("c_email");
						  String pass=request.getParameter("c_password");
						  if(user==null) {
							  request.getRequestDispatcher("/error.jsp").forward(request, response);
						  }
						  Matcher matcher = pattern.matcher(user);
						  if(matcher.matches()==false) {
							  	message="BAD FORMAT EMAIL";
								request.setAttribute("message",message);
								request.getRequestDispatcher("/login.jsp").forward(request, response);
						  }
						  else {
							  user act=DM.getUser(user); //Check name in database
							  if(act==null) {
								  message="NOT EXISTS USER";
								  request.setAttribute("message",message);
								  request.getRequestDispatcher("/login.jsp").forward(request, response);
							  }
							  boolean check= act.checkPass(pass); //Check if it exists and good password
							  if(check==false) {
								  
								  message="INCORRECT PASSWORD";
								  request.setAttribute("message",message);
								  request.getRequestDispatcher("/login.jsp").forward(request, response);
							  }
							  else {
								  session.setAttribute("user", act); //Keep data in the request
								  session.setAttribute("userLogged", act.getMail()); //Keep data in the session
								  session.setAttribute("wlist", act.getWhish()); //Keep data in the session
								  //session.setAttribute("cart", act.getShopping()); //Keep data in the session
								  request.getRequestDispatcher("/initPage.jsp").forward(request, response);
						  }
					  }
				  }
				/*else if(path.compareTo("/register.jsp")==0){
					 String mail=request.getParameter("c_email");
					 String name=request.getParameter("c_name");
					 String surname=request.getParameter("c_surname");
					 String password=request.getParameter("c_password");
					 String passwordr=request.getParameter("c_passwordconfirm");
					 String phone=request.getParameter("c_phone");
					 String address=request.getParameter("c_adrress");
					 String pathPhoto=request.getParameter("c_photo");
					 String seller=(String) request.getParameter("c_seller");
					 boolean se;
					 if(seller==null) {
						 se=false;
					 }
					 else {
						 se=true;
					 }
					 
					 if(mail==null) {
						  request.getRequestDispatcher("/error.jsp").forward(request, response);
					  }
					 
					  else {
						  //Put in the database 
						  if(checkFormat(mail,name,surname,password,phone,address)==false) {
							  message="BAD FORMAT";
							  request.setAttribute("message",message);
							  request.getRequestDispatcher("/register.jsp").forward(request, response);
						  }
						  else if(password.compareTo(passwordr)!=0) {
							  message="NOT MATCHED PASSWORD";
							  request.setAttribute("message",message);
							  request.getRequestDispatcher("/login.jsp").forward(request, response);
						  }
						  else {
							  user us=new user(name, surname, phone, address, mail, password, pathPhoto, se);
							  if(!DM.putUser(us)) {
								  message="EXISTED USER";
								  request.setAttribute("message",message);
								  request.getRequestDispatcher("/login.jsp").forward(request, response);
							  }
							  else {
								  user act=DM.getUser(mail);
								  session.setAttribute("user", act); //Keep data in the request
								  session.setAttribute("wlist", act.getWhish()); //Keep data in the session
								  session.setAttribute("userLogged", act.getMail()); //Keep data in the session
								  session.setAttribute("cart", act.getShopping()); //Keep data in the session
								  request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
							  }
					  }
				}
			}
				else if(path.compareTo("/userProfileModify.jsp")==0) {
					String act = request.getParameter("modifyButton");
					if (act == null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					else if(act.equals("DeleteAccount")) {
						String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user
						if(user==null) {
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
						else {
						session.setAttribute("userLogged",null);
						session.setAttribute("user",null);
						session.setAttribute("wlist", null); //Keep data in the session
						session.setAttribute("cart", null); //Keep data in the session
						DM.deleteUser(user);
						request.getRequestDispatcher("/index.jsp").forward(request, response);
						}
						
					}
					else {
						//if null, in database we do not update
						String mail=(String) session.getAttribute("userLogged"); //obtain the actual logged user
						 String name=request.getParameter("c_name");
						 String surname=request.getParameter("c_surname");
						 String password=request.getParameter("c_password");
						 String phone=request.getParameter("c_phone");
						 String address=request.getParameter("c_adrress");
						  if(checkFormat(mail,name,surname,password,phone,address)==false) {
							  message="BAD FORMAT";
							  request.setAttribute("message",message);
							  request.getRequestDispatcher("/userProfileModify.jsp").forward(request, response);
						  }
						 else {
							 user ac=DM.getUser(mail);
							 if(ac==null) {
								  request.getRequestDispatcher("/error.jsp").forward(request, response);
							 }
							 else {
								 user u=new user(name, surname, phone, address, mail, password, DM.getUser(name).getImagePath(), DM.getUser(name).isSeller());
								 DM.forceUserInsert(u);
								 ac=DM.getUser(mail);
								 session.setAttribute("user", ac); //Keep data in the request
								 request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
							 }
						 }
					}
				}
				else if(path.compareTo("/index.jsp")==0) {
					String act = request.getParameter("indexButton");
					if (act == null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					else if(act.equals("cart")) {
						String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user
						if(user==null) {
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
						else {
						ArrayList<Pair<product,Integer>> list=DM.getUser(user).getShopping();
						 session.setAttribute("cart", list); //Keep data in the request
						request.getRequestDispatcher("/displayCart.jsp").forward(request, response);
						}
					}
					else if(act.equals("wlist")){
						String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user
						if(user==null) {
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
						else {
							Set<product> wlist=DM.getUser(user).getWhish();
							session.setAttribute("wlist", wlist); //Keep data in the request
							request.getRequestDispatcher("/wishList.jsp").forward(request, response);
						}
					}
					else if(act.equals("productsDisplay")) {
						ArrayList<product> list=DM.getAllProducts();
						request.setAttribute("product", list);
						request.getRequestDispatcher("/shop.jsp").forward(request, response);
					}
				}
				else if(path.compareTo("/wishList.jsp")==0){
					String act = request.getParameter("xButton");
					
					if (act == null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}		
					else if(act.equals("addToCart")) {
						Object cart;
						//ArrayList<Pair<product,Integer>> cart= DB.changeLists(session.getAttribute("wlist")); //Keep data in the session
						session.setAttribute("cart", cart);
						request.getRequestDispatcher("/cart.jsp").forward(request, response);
					}
					//Obtain modified data
					else {
					String [] values=act.split("+");
					String product=values[0];
					float price=Float.parseFloat(values[1]); //TODO: arreglar esto desde wlist
					String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user//obtain the actual logged user
					if(product==null || user==null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					else if(DM.getProduct(product,price) ==null) {
						message="THE PRODUCT DOES NOT EXIST";
						request.setAttribute("message",message);
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					Set<product> wlist=DM.getUser(user).getWhish();
					if(wlist==null) {
						message="THE WLIST IS DISABLED";
						request.setAttribute("message",message);
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					//Delete a product
					//send updated data to JSP
					Set<product> wlistnew=DM.getUser(user).deleteWlist(product,price);
					session.setAttribute("wlist",wlistnew);
					request.getRequestDispatcher("/wishList.jsp").forward(request, response);
					}	
					}
					
				else if(path.compareTo("/cart.jsp")==0) {
					String act = request.getParameter("xButton");
					String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user
					if (act == null || user==null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}		
					else if(act.equals("buyButton")) {
						session.setAttribute("cart", DM.getUser(user).getShopping());
						request.getRequestDispatcher("/checkout.jsp").forward(request, response);
					}
					else {
						String [] values=act.split("+");
						String product=values[0];
						float price=Float.parseFloat(values[1]);
						if(DM.getProduct(act,price)==null) {
						message="THE PRODUCT DOES NOT EXIST";
						request.setAttribute("message",message);
						request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
					ArrayList<Pair<product,Integer>>  cart=DM.getUser(user).getShopping();
					if(cart==null) {
						message="THE CART IS DISABLED";
						request.setAttribute("message",message);
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					//Delete a product
						ArrayList<Pair<product,Integer>> cartnew=DM.getUser(user).deleteCart(product,price);
						session.setAttribute("cart",cartnew);
						request.getRequestDispatcher("/displayCart.jsp").forward(request, response);
					}
				}
//				
				//LOG OUT
				else if(path.compareTo("/userProfile.jsp")==0) {
					String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user//obtain the actual logged user
					if(user==null) {
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
					else {	
						session.setAttribute("userLogged",null);
						session.setAttribute("user",null);
						session.setAttribute("wlist", null); //Keep data in the session
						session.setAttribute("cart", null); //Keep data in the session
						request.getRequestDispatcher("/index.jsp").forward(request, response);
						
					}
				}
//				else if(path.compareTo("/sellerProfile.jsp")==0) {
//					//Modify catalogue
//					String product=request.getParameter("product");
//					String user=(String) session.getAttribute("userLogged"); //obtain the actual logged user//obtain the actual logged user
//					if(product==null || user==null) {
//						request.getRequestDispatcher("/error.jsp").forward(request, response);
//					}
//					else if(DB.retrieveUserByMail(user).isSeller()==false) {
//						message="NOT SELLER";
//						request.setAttribute("message",message);
//						request.getRequestDispatcher("/error.jsp").forward(request, response);
//					}
//					else if(DB.getProduct(product)==false) {
//						message="THE PRODUCT DOES NOT EXIST";
//						request.setAttribute("message",message);
//						request.getRequestDispatcher("/error.jsp").forward(request, response);
//					}
//					ArrayList<product> catalogue=DB.getCatalogue(user);
//					if(catalogue==null) {
//						message="THE CATALOGUE IS DISABLED";
//						request.setAttribute("message",message);
//						request.getRequestDispatcher("/error.jsp").forward(request, response);
//					}
//					if(request.getParameter("action")=="m") {
//						//if something is null is not update
//						String price=request.getParameter("price");
//						String quantity = request.getParameter("quatity");
//						String shortD=request.getParameter("shortD");
//						String longD = request.getParameter("longD");
//						String image=request.getParameter("imageURL");
//						List newC=DB.modify(catalogue,product,price,quantity,shortD,longD,image);
//						//modify data in database
//						//send data to JSP
//						request.setParameter("catalogue",newC);
//						request.getRequestDispatcher("/sellerProfile.jsp").forward(request, response);
//						
//					}
//					//Delete a product
//					else if(request.getParameter("action")=="d") {
//						//delete data in database
//						//send updated data to JSP
//						List newC=DB.delete(catalogue,name);
//						request.setParameter("catalogue",newC);
//						request.getRequestDispatcher("/sellerProfile.jsp").forward(request, response);
//						
//					}
//					//Add a product
//					else {
//						//add data in database
//						//send updated data to JSP
//						List newC=DB.add(catalogue,name,price,quantity,shortD,longD,image);
//						request.setParameter("catalogue",newC);
//						request.getRequestDispatcher("/sellerProfile.jsp").forward(request, response);
//						
//					}
//				}
//				*/
				else if(path.compareTo("/checkout.jsp")==0) {
					message="SHOP DEVELOPED";
					request.setAttribute("message",message);
					request.getRequestDispatcher("/thankyou.jsp").forward(request, response);
				}
			  }

		/*private boolean checkFormat(String email,String name, String surname, String password, String phone,
				String address) {
			  String regex = "^(.+)@(.+)$";
			  Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(email);
			if(name==null || surname==null || address==null || password==null || phone==null || password.length()<8 || phone.length()!=9 || matcher.matches()) {
				return false;
			}
			return true;
		}*/
}
