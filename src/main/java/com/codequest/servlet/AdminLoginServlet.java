package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;
@WebServlet("/AdminLoginServlet") public class AdminLoginServlet extends HttpServlet{
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{
  String id=q.getParameter("identity"),pw=q.getParameter("password");
  try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT id,username,password_hash FROM admins WHERE username=? OR email=?")){
   s.setString(1,id==null?"":id.trim());s.setString(2,id==null?"":id.trim());try(ResultSet r=s.executeQuery()){if(r.next()&&PasswordUtil.verify(pw==null?"":pw,r.getString("password_hash"))){
    HttpSession old=q.getSession(false);if(old!=null)old.invalidate();HttpSession session=q.getSession(true);session.setAttribute("adminId",r.getLong("id"));session.setAttribute("adminUsername",r.getString("username"));session.setMaxInactiveInterval(30*60);p.sendRedirect(q.getContextPath()+"/admin-dashboard.jsp");return;}}
  }catch(SQLException ex){getServletContext().log("Admin login failed",ex);q.setAttribute("error","Database connection failed.");}
  if(q.getAttribute("error")==null)q.setAttribute("error","Invalid administrator credentials.");q.getRequestDispatcher("/admin-login.jsp").forward(q,p);
 }
}
