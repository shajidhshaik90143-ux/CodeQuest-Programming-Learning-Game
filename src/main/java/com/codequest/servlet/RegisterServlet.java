package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  req.setCharacterEncoding("UTF-8");
  String u=req.getParameter("username"), e=req.getParameter("email"), p=req.getParameter("password"), c=req.getParameter("confirmPassword");
  String msg=null;
  if(u==null||e==null||p==null||c==null||u.isBlank()||e.isBlank()||p.isBlank()) msg="All fields are required.";
  else if(!u.matches("[A-Za-z0-9_]{3,40}")) msg="Username must be 3–40 characters (letters, numbers, underscore).";
  else if(!e.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) msg="Enter a valid email address.";
  else if(p.length()<8) msg="Password must contain at least 8 characters.";
  else if(!p.equals(c)) msg="Passwords do not match.";
  if(msg==null) try(Connection con=DBConnection.getConnection();
    PreparedStatement check=con.prepareStatement("SELECT id FROM users WHERE username=? OR email=?")){
    check.setString(1,u.trim());check.setString(2,e.trim());
    try(ResultSet rs=check.executeQuery()){if(rs.next())msg="Username or email already exists.";}
    if(msg==null) try(PreparedStatement ps=con.prepareStatement("INSERT INTO users(username,email,password_hash,xp,level) VALUES(?,?,?,0,1)")){
      ps.setString(1,u.trim());ps.setString(2,e.trim());ps.setString(3,PasswordUtil.hash(p));ps.executeUpdate();
    }
  }catch(SQLException ex){getServletContext().log("Registration failed",ex);msg="Database error. Check MySQL settings and try again.";}
  if(msg!=null){req.setAttribute("error",msg);req.getRequestDispatcher("/register.jsp").forward(req,resp);}
  else {req.getSession().setAttribute("flash","Registration successful. Please log in.");resp.sendRedirect(req.getContextPath()+"/login.jsp");}
 }
}
