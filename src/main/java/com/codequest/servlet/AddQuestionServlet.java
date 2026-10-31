package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;
@WebServlet("/AddQuestionServlet") public class AddQuestionServlet extends HttpServlet{
 protected void doPost(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{
  if(!WebUtil.admin(q)){p.sendRedirect(q.getContextPath()+"/admin-login.jsp");return;}
  String title=q.getParameter("title"),desc=q.getParameter("description"),cat=q.getParameter("category"),diff=q.getParameter("difficulty"),a=q.getParameter("a"),b=q.getParameter("b"),c=q.getParameter("c"),d=q.getParameter("d"),correct=q.getParameter("correct");
  int xp;try{xp=Integer.parseInt(q.getParameter("xp"));}catch(Exception ex){xp=0;}
  String err=null;if(title==null||title.isBlank()||desc==null||desc.isBlank()||a==null||b==null||c==null||d==null)err="Complete every field.";
  else if(!("Java".equals(cat)||"SQL".equals(cat)||"DSA".equals(cat)))err="Choose Java, SQL or DSA.";
  else if(!("Easy".equals(diff)||"Medium".equals(diff)||"Hard".equals(diff)))err="Choose a valid difficulty.";
  else if(!("A".equals(correct)||"B".equals(correct)||"C".equals(correct)||"D".equals(correct))||xp<1||xp>500)err="Choose a correct answer and XP from 1 to 500.";
  if(err==null)try(Connection con=DBConnection.getConnection();PreparedStatement s=con.prepareStatement("INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward) VALUES(?,?,?,?,?,?,?,?,?,?)")){
   s.setString(1,title.trim());s.setString(2,desc.trim());s.setString(3,cat);s.setString(4,diff);s.setString(5,a.trim());s.setString(6,b.trim());s.setString(7,c.trim());s.setString(8,d.trim());s.setString(9,correct);s.setInt(10,xp);s.executeUpdate();
  }catch(SQLException ex){getServletContext().log("Add question failed",ex);err="Could not save question. Check database connection.";}
  if(err!=null){q.setAttribute("error",err);q.getRequestDispatcher("/add-question.jsp").forward(q,p);}else{q.getSession().setAttribute("flash","Mission added successfully.");p.sendRedirect(q.getContextPath()+"/ViewQuestionsServlet");}
 }
}
