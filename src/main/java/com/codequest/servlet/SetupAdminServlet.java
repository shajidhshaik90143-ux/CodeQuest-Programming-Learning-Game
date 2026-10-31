package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;
@WebServlet("/SetupAdminServlet") public class SetupAdminServlet extends HttpServlet{
 private static final String SETUP_SECRET="CHANGE_THIS_SETUP_SECRET";
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws IOException{
  p.setContentType("text/plain;charset=UTF-8");
  if(!SETUP_SECRET.equals(q.getParameter("secret"))){p.sendError(403,"Invalid setup secret.");return;}
  String u=q.getParameter("username"),e=q.getParameter("email"),pw=q.getParameter("password");
  if(u==null||e==null||pw==null||u.isBlank()||e.isBlank()||pw.length()<8){p.sendError(400,"Provide username, email and password (8+ characters).");return;}
  try(Connection c=DBConnection.getConnection();Statement st=c.createStatement();ResultSet r=st.executeQuery("SELECT COUNT(*) FROM admins")){
   r.next();if(r.getInt(1)>0){p.sendError(409,"An admin already exists.");return;}
   try(PreparedStatement x=c.prepareStatement("INSERT INTO admins(username,email,password_hash) VALUES(?,?,?)")){x.setString(1,u);x.setString(2,e);x.setString(3,PasswordUtil.hash(pw));x.executeUpdate();}
   p.getWriter().println("Admin created. Disable SetupAdminServlet now.");
  }catch(Exception ex){getServletContext().log("Admin setup failed",ex);p.sendError(500,"Admin setup failed. Check server log.");}
 }
}
