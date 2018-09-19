package svc;


import static db.JdbcUtil.*;
import java.sql.Connection;
import java.util.List;

import dao.BoardDAO;
import vo.BoardVO;

public class BoardListService {

	public int getArticleCount() throws Exception{
	
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		int aritcleCount = boardDAO.selectArticleCount();
		close(con);
		
		return aritcleCount;
	}

	public List<BoardVO> getArticles(int startRow, int pageSize) throws Exception{
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		List<BoardVO> articleList = boardDAO.selectArticleList(startRow,pageSize);
		close(con);
		return articleList;
	}

}
