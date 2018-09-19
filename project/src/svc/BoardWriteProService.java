package svc;


import java.sql.Connection;
import static db.JdbcUtil.*;
import dao.BoardDAO;
import vo.BoardVO;
public class BoardWriteProService {

	public boolean writeArticle(BoardVO boardVO) throws Exception{
		
		boolean writeSuccess = false;
		
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);

		int inserCount = boardDAO.insertArticle(boardVO);
		if(inserCount>0) {
			writeSuccess=true;
			commit(con);
		}
		else {
			rollback(con);
		}
		close(con);
		return writeSuccess;
	}

}
