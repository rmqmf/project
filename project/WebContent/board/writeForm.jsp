<%@page import="vo.ReplyInfo"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/view/color.jsp"%>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="script.js"></script>
<style>
#writeFormArea{
	width : 600px;
	margin: auto;
	border : 1px solid red;
	
}
table{
	width: 580px;
	margin: auto;
	text-align: center;
}
.td_left{
	width: 200px;
}
.td_right{
	width: 380px;
	text-align: left;
}
h1{
	text-align: center;
}
#commandArea{
	padding-top: 20px;
}

</style>
</head>

<% 
	ReplyInfo replyInfo = (ReplyInfo)request.getAttribute("replyInfo");
	int num = replyInfo.getNum();
	int ref = replyInfo.getRef();
	int re_level = replyInfo.getRe_level();
	int re_step = replyInfo.getRe_step();
%>
   
<body>  
<section id = "writeFormArea">
<h1>글쓰기</h1>
<br>
<form method="post" name="writeform" action="boardWritePro.bo">
<input type="hidden" name="num" value="<%=num%>">
<input type="hidden" name="ref" value="<%=ref%>">
<input type="hidden" name="re_step" value="<%=re_step%>">
<input type="hidden" name="re_level" value="<%=re_level%>">

<table>
  
   <tr>
    <td  >이 름</td>
    <td class= "td_right">
       <input type="text" size="10" maxlength="10" name="writer" required="required"></td>
  </tr>
  <tr>
    <td class = "td_left">제 목</td>
    <td class= "td_right">
    <%if(request.getParameter("num")==null){%>
       <input type="text" size="40" maxlength="50" name="subject"></td>
	<%}else{%>
	   <input type="text" size="40" maxlength="50" name="subject" 
	   value="[답변]"></td>
	<%}%>
  </tr>
  <tr>
    <td>Email</td>
    <td class= "td_right">
       <input type="email" size="40" maxlength="30" name="email" ></td>
  </tr>
  <tr>
    <td>내 용</td>
    <td class= "td_right" >
     <textarea name="content" rows="13" cols="40"></textarea> </td>
  </tr>
  <tr>
    <td>비밀번호</td>
    <td class= "td_right">
     <input type="password" size="8" maxlength="12" name="passwd"> 
	 </td>
  </tr>
<tr >      
 <td colspan=2 id="commandArea"> 
  <input type="submit" value="글쓰기" >  
  <input type="reset" value="다시작성">
  <input type="button" value="목록보기" 
  OnClick="window.location='list.jsp'">
</td></tr></table>    
   
</form>      
</section>
</body>
</html>      
