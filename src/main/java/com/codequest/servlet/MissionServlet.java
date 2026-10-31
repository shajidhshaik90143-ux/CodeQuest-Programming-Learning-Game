package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;import java.util.*;
@WebServlet("/MissionServlet") public class MissionServlet extends HttpServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{if(!WebUtil.student(q)){p.sendRedirect(q.getContextPath()+"/login.jsp");return;}
  List<Map<String,Object>> list=new ArrayList<>();String sql="SELECT m.id,m.title,m.description,m.category,m.difficulty,m.option_a,m.option_b,m.option_c,m.option_d,m.xp_reward, s.id attempted,s.is_correct FROM missions m LEFT JOIN mission_submissions s ON s.mission_id=m.id AND s.user_id=? ORDER BY m.id DESC";
  try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,(Long)q.getSession().getAttribute("studentId"));try(ResultSet r=s.executeQuery()){while(r.next()){Map<String,Object> m=new HashMap<>();for(String k:new String[]{"id","title","description","category","difficulty","option_a","option_b","option_c","option_d","xp_reward","attempted","is_correct"})m.put(k,r.getObject(k));list.add(m);}}}
  catch(SQLException ex){getServletContext().log("Missions failed",ex);q.setAttribute("error","Unable to load missions.");}
  q.setAttribute("missions",list);q.getRequestDispatcher("/missions.jsp").forward(q,p);
 }
}
