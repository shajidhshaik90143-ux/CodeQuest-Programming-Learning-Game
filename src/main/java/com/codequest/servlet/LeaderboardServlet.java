package com.codequest.servlet;
import com.codequest.util.*;import jakarta.servlet.ServletException;import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import java.io.IOException;import java.sql.*;import java.util.*;
@WebServlet("/LeaderboardServlet") public class LeaderboardServlet extends HttpServlet{
 protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{
  List<Map<String,Object>> rows=new ArrayList<>();String sql="SELECT u.username,u.level,u.xp,COUNT(CASE WHEN s.is_correct=1 THEN 1 END) completed FROM users u LEFT JOIN mission_submissions s ON s.user_id=u.id GROUP BY u.id,u.username,u.level,u.xp ORDER BY u.xp DESC,u.username ASC";
  try(Connection c=DBConnection.getConnection();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){int rank=1;while(r.next()){Map<String,Object> m=new HashMap<>();m.put("rank",rank++);m.put("username",r.getString("username"));m.put("level",r.getInt("level"));m.put("xp",r.getInt("xp"));m.put("completed",r.getInt("completed"));rows.add(m);}}
  catch(SQLException ex){getServletContext().log("Leaderboard failed",ex);q.setAttribute("error","Unable to load leaderboard.");}
  q.setAttribute("players",rows);q.getRequestDispatcher("/leaderboard.jsp").forward(q,p);
 }
}
