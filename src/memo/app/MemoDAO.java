package memo.app;
/**
 * 
 * @author User
 * 영속성 계층(Persistant Layer에 속함)
 * Dao(Data Access Object): 데이터베이스에 접근해서 CRUD의 로직을 수행하는 객체
 * => Model에 속함
 *
 */
import java.util.*;
import common.util.*;
import java.sql.*;

public class MemoDAO {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	
	/**
	 * 한줄 메모장에 insert문을 수행하는 메서드
	 */
	public int insertMemo(MemoVO memo)throws SQLException{
		try {
			con=DButil.getCon();
//			String sql="insert into";
//					sql+="memo values(";
//					sql+=")";
			//StringBuffer, StringBuilder: 문자열 편집작업이 가능한 클래스
			//								문자열을 메모리 버퍼에 넣고 수정, 삽입, 삭제 등을 수행함

			StringBuilder buf=new StringBuilder("insert into memo(no, name, msg, wdate)")
							.append(" values(memo_seq.nextval,?,?,sysdate)");
			String sql=buf.toString();
			
			ps=con.prepareStatement(sql);
			ps.setString(1, memo.getName());
			ps.setString(2, memo.getMsg());
			
			int n=ps.executeUpdate();
			return n;
		}finally {
			close();
		}
	}
	/**
	 * 전체 메모글을 가져오는 메소드
	 */
	public List<MemoVO> listMemo() throws SQLException{
		try {
			con=DButil.getCon();
			StringBuilder buf=new StringBuilder("SELECT rpad(no,10,' ')no, rpad(name, 16,' ') name,")
							.append("rpad(msg,100,' ')msg, wdate FROM memo ORDER BY wdate DESC");
			String sql=buf.toString();
			
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			
			List<MemoVO> arr=makeList(rs);
			return arr;
		}finally {
			close();
		}
	}
	
	public List<MemoVO> makeList(ResultSet rs) throws SQLException{
		List<MemoVO> arr=new ArrayList<>();
		while(rs.next()) {
			int no=rs.getInt("no");
			String name=rs.getString("name");
			String msg=rs.getString("msg");
			java.sql.Date wdate=rs.getDate("wdate");
			
			MemoVO memo=new MemoVO(no,name,msg,wdate);
			arr.add(memo);
		}
		return arr;
	}
	
	/**
	 * 글번호(PK)로 메모글을 가져오는 메서드
	 */
	public MemoVO selectMemo(int no)throws SQLException{
		try {
			con=DButil.getCon();
			String sql="select no,name,msg,wdate from memo where no=?";
			ps=con.prepareStatement(sql);
			ps.setInt(1, no);
			rs=ps.executeQuery();
			List<MemoVO> arr=makeList(rs);
			if(arr!=null && arr.size()==1) {
//				MemoVO memo=arr.get(0);
//				return memo;
				return arr.get(0);
			}
			return null;
		}finally {
			close();
		}
	}
	
	/**
	 * 키워드로 메모 글 내용을 검색하는 메서드
	 */
	public List<MemoVO> findMemo(String keyword)throws SQLException {
		try {
			con=DButil.getCon();
			StringBuilder buf=new StringBuilder("select rpad(no,10,' ') no,")
						.append(" rpad(name,16,' ')name,rpad(msg,100,' ') msg, wdate from memo")
						.append(" where msg like ?");
			String sql=buf.toString();
			ps=con.prepareStatement(sql);
			ps.setString(1, "%"+keyword+"%");
			rs=ps.executeQuery();
			List<MemoVO> arr=makeList(rs);
			return arr;
		}finally {
			close();
		}
	}
	
	/**
	 * 메모 글, 내용, 작성자를 수정하는 메서드
	 */
	public int updateMemo(MemoVO memo) throws SQLException {
		try {
			con=DButil.getCon();
			String sql="update memo set name=?, msg=? where no=?";
			ps=con.prepareStatement(sql);
			ps.setString(1, memo.getName());
			ps.setString(2, memo.getMsg());
			ps.setInt(3, memo.getNo());
			
			return ps.executeUpdate();
		}finally {
			close();
		}
	}
	
	/**
	 * 글번호로 메모글을 삭제하는 메서드
	 * */
	public int deleteMemo(int no)throws SQLException{
		try {
			con=DButil.getCon();
			String sql="delete from memo where no=?";
			
			ps=con.prepareStatement(sql);
			ps.setInt(1, no);
			return ps.executeUpdate();
		}finally {
			close();
		}	
	}
	
	
	/**
	 * DB관련한 자원들을 반납하는 메서드
	 */
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(con!=null) con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
