package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;import java.util.*;
@WebServlet("/ViewQuestionsServlet") public class ViewQuestionsServlet extends HttpServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{if(!WebUtil.admin(q)){p.sendRedirect(q.getContextPath()+"/admin-login.jsp");return;}
  List<Map<String,Object>> rows=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement("SELECT id,title,category,difficulty,xp_reward FROM missions ORDER BY id DESC");ResultSet r=s.executeQuery()){
   while(r.next()){Map<String,Object> m=new HashMap<>();m.put("id",r.getLong("id"));m.put("title",r.getString("title"));m.put("category",r.getString("category"));m.put("difficulty",r.getString("difficulty"));m.put("xp",r.getInt("xp_reward"));rows.add(m);}
  }catch(SQLException ex){getServletContext().log("View questions failed",ex);q.setAttribute("error","Unable to load questions.");}
  q.setAttribute("questions",rows);q.getRequestDispatcher("/view-questions.jsp").forward(q,p);
 }
}
