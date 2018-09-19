<%@page import="dao.BoardDAO"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import = "java.sql.Timestamp" %>

<%
	request.setCharacterEncoding("UTF-8");
%>

<jsp:useBean id="article" scope="page" class="vo.BoardVO">
   <jsp:setProperty name="article" property="*"/>
</jsp:useBean>
 
<%
 	article.setReg_date(new Timestamp(System.currentTimeMillis()) );
 	article.setIp(request.getRemoteAddr());

     BoardDAO dbPro = BoardDAO.getInstance();
     dbPro.insertArticle(article);

     response.sendRedirect("list.jsp");
 %>