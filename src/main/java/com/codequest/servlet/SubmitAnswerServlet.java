package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;
@WebServlet("/SubmitAnswerServlet") public class SubmitAnswerServlet extends HttpServlet{
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{if(!WebUtil.student(q)){p.sendRedirect(q.getContextPath()+"/login.jsp");return;}
  long uid=(Long)q.getSession().getAttribute("studentId");int mid;try{mid=Integer.parseInt(q.getParameter("missionId"));}catch(Exception e){q.getSession().setAttribute("result","Invalid mission.");p.sendRedirect(q.getContextPath()+"/MissionServlet");return;}
  String selected=q.getParameter("answer");if(!("A".equals(selected)||"B".equals(selected)||"C".equals(selected)||"D".equals(selected))){q.getSession().setAttribute("result","Select an answer.");p.sendRedirect(q.getContextPath()+"/MissionServlet");return;}
  String message;
  try(Connection c=DBConnection.getConnection()){c.setAutoCommit(false);try{
   String correct;int reward;try(PreparedStatement s=c.prepareStatement("SELECT correct_answer,xp_reward FROM missions WHERE id=? FOR UPDATE")){s.setInt(1,mid);try(ResultSet r=s.executeQuery()){if(!r.next())throw new SQLException("Mission not found.");correct=r.getString(1);reward=r.getInt(2);}}
   boolean ok=correct.equals(selected);
   try(PreparedStatement s=c.prepareStatement("INSERT INTO mission_submissions(user_id,mission_id,selected_answer,is_correct) VALUES(?,?,?,?)")){s.setLong(1,uid);s.setInt(2,mid);s.setString(3,selected);s.setBoolean(4,ok);s.executeUpdate();}
   if(ok)try(PreparedStatement s=c.prepareStatement("UPDATE users SET xp=xp+?, level=1+FLOOR((xp+?)/100) WHERE id=?")){s.setInt(1,reward);s.setInt(2,reward);s.setLong(3,uid);s.executeUpdate();}
   c.commit();message=ok?"Correct answer! You earned "+reward+" XP.":"Incorrect answer. No XP awarded.";
  }catch(SQLException ex){c.rollback();if(ex.getErrorCode()==1062)message="You have already attempted this mission. One attempt is allowed.";else{getServletContext().log("Answer submission failed",ex);message="Could not submit answer. Please try again.";}}finally{c.setAutoCommit(true);}
  }catch(SQLException ex){getServletContext().log("Database failure",ex);message="Database connection failed.";}
  q.getSession().setAttribute("result",message);p.sendRedirect(q.getContextPath()+"/MissionServlet");
 }
}
