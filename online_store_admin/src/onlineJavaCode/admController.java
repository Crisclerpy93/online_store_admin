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
								  /*String adminName = DM.getAdmin(admin).getName();
								  message="Welcome "+adminName;
								  session.setAttribute("message",message);*/
								  session.setAttribute("admin", act); //Keep data in the request
								  session.setAttribute("adminLogged", act.getMail()); //Keep data in the session
								  response.sendRedirect("/online_store_admin/initPage.jsp");
						  }
							  }
					  }
				  }
	
			  }

}
