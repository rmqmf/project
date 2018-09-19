package dao;

import static db.JdbcUtil.*;
import static db.JdbcUtil.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vo.BoardVO;

public class BoardDAO {
	private static BoardDAO boardDAO;
	private static Connection con;

	private BoardDAO() {

	}

	public static BoardDAO getInstance() {
		if (boardDAO == null) {
			// 외부에서 MemberDAO객체를 처음 사용한 것이면..
			boardDAO = new BoardDAO();
		}
		return boardDAO;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

	public int insertArticle(BoardVO article) throws Exception {
		int insertCount = 0;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 작성한 글이 답변글일때 답변글 처리를 위해서 필요한 값들

		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();

		int number = 0;
		// 새 관련글 번호

		String sql = "";

		try {

			pstmt = con.prepareStatement("select max(num) from board");
			rs = pstmt.executeQuery();

			if (rs.next())
				// 이전에 작성된 글이 하나라도 있으면

				number = rs.getInt(1) + 1;
			else
				number = 1;

			if (num != 0) // 답변글이면
			{
				sql = "update board set re_step=re_step+1 " + "where ref= ? and re_step> ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, re_step);
				pstmt.executeUpdate();
				re_step = re_step + 1;
				re_level = re_level + 1;
			} else {
				ref = number;
				re_step = 0;
				re_level = 0;
			}
			// ������ �ۼ�
			sql = "insert into board" + "(num,writer,email,subject,passwd,reg_date,";
			sql += "ref,re_step,re_level,content,ip) " + "values(board_seq.nextval,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPasswd());
			pstmt.setTimestamp(5, article.getReg_date());
			pstmt.setInt(6, ref);
			pstmt.setInt(7, re_step);
			pstmt.setInt(8, re_level);
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getIp());

			insertCount = pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return insertCount;
	}

	public int selectArticleCount() throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int articleCount = 0;

		try {

			pstmt = con.prepareStatement("select count(*) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				articleCount = rs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return articleCount;
	}

	public List<BoardVO> selectArticleList(int start, int end) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;
		PreparedStatement pstmt = null;
		try {

			pstmt = con.prepareStatement("select list2.* from(select rownum r, list1.*  "
					+ "from(select *  from board order by ref desc, re_step asc)list1) "
					+ "list2 where r between ? and ?");
			pstmt.setInt(1, start);
			pstmt.setInt(2, start + end - 1);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int i = 0;
				articleList = new ArrayList(end);
				do {
					BoardVO article = new BoardVO();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPasswd(rs.getString("passwd"));
					article.setReg_date(rs.getTimestamp("reg_date"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setRe_step(rs.getInt("re_step"));
					article.setRe_level(rs.getInt("re_level"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));

					articleList.add(article);
					i++;
				} while (rs.next() && i < end);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return articleList;
	}

	/*
	 * 
	 * public BoardVO getArticle(int num) throws Exception { Connection conn = null;
	 * PreparedStatement pstmt = null; ResultSet rs = null; BoardVO article=null;
	 * try { conn = getConnection();
	 * 
	 * pstmt = conn.prepareStatement(
	 * "update board set readcount=readcount+1 where num = ?"); pstmt.setInt(1,
	 * num); pstmt.executeUpdate();
	 * 
	 * pstmt = conn.prepareStatement( "select * from board where num = ?");
	 * pstmt.setInt(1, num); rs = pstmt.executeQuery();
	 * 
	 * if (rs.next()) { article = new BoardVO(); article.setNum(rs.getInt("num"));
	 * article.setWriter(rs.getString("writer"));
	 * article.setEmail(rs.getString("email"));
	 * article.setSubject(rs.getString("subject"));
	 * article.setPasswd(rs.getString("passwd"));
	 * article.setReg_date(rs.getTimestamp("reg_date"));
	 * article.setReadcount(rs.getInt("readcount"));
	 * article.setRef(rs.getInt("ref")); article.setRe_step(rs.getInt("re_step"));
	 * article.setRe_level(rs.getInt("re_level"));
	 * article.setContent(rs.getString("content"));
	 * article.setIp(rs.getString("ip")); } } catch(Exception ex) {
	 * ex.printStackTrace(); } finally { if (rs != null) try { rs.close(); }
	 * catch(SQLException ex) {} if (pstmt != null) try { pstmt.close(); }
	 * catch(SQLException ex) {} if (conn != null) try { conn.close(); }
	 * catch(SQLException ex) {} } return article; }
	 * 
	 * public BoardVO updateGetArticle(int num) throws Exception { Connection conn =
	 * null; PreparedStatement pstmt = null; ResultSet rs = null; BoardVO
	 * article=null; try { conn = getConnection();
	 * 
	 * pstmt = conn.prepareStatement( "select * from board where num = ?");
	 * pstmt.setInt(1, num); rs = pstmt.executeQuery();
	 * 
	 * if (rs.next()) { article = new BoardVO(); article.setNum(rs.getInt("num"));
	 * article.setWriter(rs.getString("writer"));
	 * article.setEmail(rs.getString("email"));
	 * article.setSubject(rs.getString("subject"));
	 * article.setPasswd(rs.getString("passwd"));
	 * article.setReg_date(rs.getTimestamp("reg_date"));
	 * article.setReadcount(rs.getInt("readcount"));
	 * article.setRef(rs.getInt("ref")); article.setRe_step(rs.getInt("re_step"));
	 * article.setRe_level(rs.getInt("re_level"));
	 * article.setContent(rs.getString("content"));
	 * article.setIp(rs.getString("ip")); } } catch(Exception ex) {
	 * ex.printStackTrace(); } finally { if (rs != null) try { rs.close(); }
	 * catch(SQLException ex) {} if (pstmt != null) try { pstmt.close(); }
	 * catch(SQLException ex) {} if (conn != null) try { conn.close(); }
	 * catch(SQLException ex) {} } return article; }
	 * 
	 * public int updateArticle(BoardVO article) throws Exception { Connection conn
	 * = null; PreparedStatement pstmt = null; ResultSet rs= null;
	 * 
	 * String dbpasswd=""; String sql=""; int x=-1; try { conn = getConnection();
	 * 
	 * pstmt = conn.prepareStatement( "select passwd from board where num = ?");
	 * pstmt.setInt(1, article.getNum()); rs = pstmt.executeQuery();
	 * 
	 * if(rs.next()){ dbpasswd= rs.getString("passwd");
	 * if(dbpasswd.equals(article.getPasswd())){
	 * sql="update board set writer=?,email=?,subject=?,passwd=?";
	 * sql+=",content=? where num=?"; pstmt = conn.prepareStatement(sql);
	 * 
	 * pstmt.setString(1, article.getWriter()); pstmt.setString(2,
	 * article.getEmail()); pstmt.setString(3, article.getSubject());
	 * pstmt.setString(4, article.getPasswd()); pstmt.setString(5,
	 * article.getContent()); pstmt.setInt(6, article.getNum());
	 * pstmt.executeUpdate(); x= 1; }else{ x= 0; } } } catch(Exception ex) {
	 * ex.printStackTrace(); } finally { if (rs != null) try { rs.close(); }
	 * catch(SQLException ex) {} if (pstmt != null) try { pstmt.close(); }
	 * catch(SQLException ex) {} if (conn != null) try { conn.close(); }
	 * catch(SQLException ex) {} } return x; }
	 * 
	 * public int deleteArticle(int num, String passwd) throws Exception {
	 * Connection conn = null; PreparedStatement pstmt = null; ResultSet rs= null;
	 * String dbpasswd=""; int x=-1; try { conn = getConnection();
	 * 
	 * pstmt = conn.prepareStatement( "select passwd from board where num = ?");
	 * pstmt.setInt(1, num); rs = pstmt.executeQuery();
	 * 
	 * if(rs.next()){ dbpasswd= rs.getString("passwd"); if(dbpasswd.equals(passwd)){
	 * pstmt = conn.prepareStatement( "delete from board where num=?");
	 * pstmt.setInt(1, num); pstmt.executeUpdate(); x= 1; //�ۻ��� ���� }else x= 0;
	 * //��й�ȣ Ʋ�� } } catch(Exception ex) { ex.printStackTrace(); } finally { if
	 * (rs != null) try { rs.close(); } catch(SQLException ex) {} if (pstmt != null)
	 * try { pstmt.close(); } catch(SQLException ex) {} if (conn != null) try {
	 * conn.close(); } catch(SQLException ex) {} } return x; }
	 */
}
