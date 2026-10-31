package com.codequest.servlet;
import com.codequest.util.*;
import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;
import java.io.IOException;import java.sql.*;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  String id=req.getParameter("identity"),pw=req.getParameter("password");String error="Invalid username/email or password.";
  if(id!=null&&pw!=null)try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT id,username,password_hash FROM users WHERE username=? OR email=?")){
   p.setString(1,id.trim());p.setString(2,id.trim());try(ResultSet r=p.executeQuery()){if(r.next()&&PasswordUtil.verify(pw,r.getString("password_hash"))){
    HttpSession s=req.getSession();s.setAttribute("studentId",r.getLong("id"));s.setAttribute("username",r.getString("username"));s.setMaxInactiveInterval(30*60);
    resp.sendRedirect(req.getContextPath()+"/DashboardServlet");return;}}
  }catch(SQLException ex){getServletContext().log("Login failed",ex);error="Database connection failed.";}
  req.setAttribute("error",error);req.getRequestDispatcher("/login.jsp").forward(req,resp);
 }
}
