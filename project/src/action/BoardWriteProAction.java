package action;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardWriteProService;
import vo.ActionForward;
import vo.BoardVO;

public class BoardWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String writer = request.getParameter("writer");
		String subject = request.getParameter("subject");
		String passwd = request.getParameter("passwd");
		String email = request.getParameter("email");
		String content = request.getParameter("content");
		int ref = Integer.parseInt(request.getParameter("ref"));
		int num = Integer.parseInt(request.getParameter("num"));
		int re_step = Integer.parseInt(request.getParameter("re_step"));
		int re_level = Integer.parseInt(request.getParameter("re_level"));
		
		BoardVO boardVO = new BoardVO();
		boardVO.setContent(content);
		boardVO.setEmail(email);
		boardVO.setNum(num);
		boardVO.setPasswd(passwd);
		boardVO.setRe_level(re_level);
		boardVO.setRe_step(re_step);
		boardVO.setRef(ref);
		boardVO.setReg_date(new Timestamp(System.currentTimeMillis()));
		boardVO.setSubject(subject);
		boardVO.setWriter(writer);
		boardVO.setIp(request.getLocalAddr());
		
		BoardWriteProService boardWriteProService = new BoardWriteProService(); 
		
		boolean writeSuccess = boardWriteProService.writeArticle(boardVO);
		ActionForward forward = null;
		if(writeSuccess) {
			forward = new ActionForward();
			forward.setUrl("boardList.bo");
			forward.setRedirect(true);
		} 
		else {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('등록실패')");
			out.println("history.back()");
			out.println("<script>");
		}
		
		return forward;
	}

}
