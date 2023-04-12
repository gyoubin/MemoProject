package memo.app;

import java.sql.Date;

/**
 * 
 * @author User
 * Domain 객체
 * VO(Value Object): DB에서 가져온 값이나, UI를 통해 입력받은 값을 담고 있는 객체
 * memo테이블에 있는 컬럼들==> class의 member변수로 옮겨 구성
 * 
 */
public class MemoVO {
	private int no;
	private String name;
	private String msg;
	private java.sql.Date wdate;
	
	public MemoVO() {
		this(0,null,null,null);
	}
	
	public MemoVO(int no, String name, String msg, Date wdate) {
		super();
		this.no = no;
		this.name = name;
		this.msg = msg;
		this.wdate = wdate;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public java.sql.Date getWdate() {
		return wdate;
	}

	public void setWdate(java.sql.Date wdate) {
		this.wdate = wdate;
	}

	
}
