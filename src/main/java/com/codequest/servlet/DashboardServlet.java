package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;
@WebServlet("/DashboardServlet") public class DashboardServlet extends HttpServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{if(!WebUtil.student(q)){p.sendRedirect(q.getContextPath()+"/login.jsp");return;}
  try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT u.username,u.xp,u.level,(SELECT COUNT(*) FROM mission_submissions ms WHERE ms.user_id=u.id AND ms.is_correct=1) completed FROM users u WHERE u.id=?")){
   s.setLong(1,(Long)q.getSession().getAttribute("studentId"));try(ResultSet r=s.executeQuery()){if(r.next()){q.setAttribute("username",r.getString("username"));q.setAttribute("xp",r.getInt("xp"));q.setAttribute("level",r.getInt("level"));q.setAttribute("completed",r.getInt("completed"));}}
  }catch(SQLException ex){getServletContext().log("Dashboard failed",ex);q.setAttribute("error","Could not load progress.");}
  q.getRequestDispatcher("/dashboard.jsp").forward(q,p);
 }
}
