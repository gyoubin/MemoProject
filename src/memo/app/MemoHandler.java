package memo.app;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
/**
 * 
 * @author User
 * Controller
 * View쪽에서 이벤트가 발생하면 이벤트를 처리한다.==> DB관련작업이 있으면
 * DAO객체 통해 작업을 수행한다.==> 그 처리 결과를 View에 전달한다
 * Model과 View 사이에서 제어하는 역할을 수행한다
 * 
 */
public class MemoHandler implements ActionListener{
	MemoAppView app;
	MemoDAO dao=new MemoDAO();
	
	public MemoHandler(MemoAppView mav) {
		this.app=mav;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("event 처리 중...");
		//MemoAppView타이틀에 "event 처리 중..." 출력
		//app.setTitle("event 처리 중...");
		Object obj=e.getSource();
		if(obj==app.btAdd) {
			addMemo();
			app.clearInput();
			listMemo();
		}else if(obj==app.btList){
			listMemo();
		}else if(obj==app.btDel) {
			deleteMemo();
			listMemo();
		}else if(obj==app.btEdit) {
			editMemo();
		}else if(obj==app.btEditEnd) {
			editMemoEnd();
			app.clearInput();
			listMemo();
		}else if(obj==app.btFind) {
			findMemo();
		}
	}
	
	private void findMemo() {
		String keyword=app.showInputDialog("검색할 키워드를 입력하세요");
		if(keyword==null) return;
		if(keyword==null||keyword.trim().isEmpty()) {
			findMemo();
			return;
		}
		try {
			List<MemoVO> arr=dao.findMemo(keyword);
			if(arr==null||arr.size()==0) {
				app.showMessage(keyword+"로 검색할 결과 해당 글은 없어요");
				return;
		}
		app.showTextArea(arr);
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}
	
	public void editMemo() {
		String noStr=app.showInputDialog("수정할 글 번호를 입력하세요");
		if(noStr==null) return;
		try {
			MemoVO vo=dao.selectMemo(Integer.parseInt(noStr.trim()));
			if(vo==null) {
				app.showMessage(noStr+"번 글은 존재하지 않아요!!");
				return;
			}
			app.setText(vo);
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
	
	public void editMemoEnd() {
		String noStr=app.tfNo.getText();
		String name=app.tfName.getText();
		String msg=app.tfMsg.getText();
		
		if(noStr==null||name==null||noStr.trim().isEmpty()||name.trim().isEmpty()) {
			app.showMessage("작성자와 글번호는 반드시 입력해야 해요");
			return;
		}
		
		int no=Integer.parseInt(noStr.trim());
		MemoVO memo=new MemoVO(no,name,msg,null);
		
		try {
			int res=dao.updateMemo(memo);
		
			String str=(res>0)?"글 수정 성공":"글 수정 실패";
			app.showMessage(str);
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}
	
	public void deleteMemo() {
		String noStr=app.showInputDialog("삭제할 글 번호를 입력하세요");
		if(noStr==null) return;
		try {
			int n=dao.deleteMemo(Integer.parseInt(noStr.trim()));
			String res=(n>0)?"글 삭제 성공":"글 삭제 실패";
			app.showMessage(res);
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
	public void listMemo() {
		try {
			List<MemoVO> arr=dao.listMemo();
			app.setTitle("전체 글 개수: "+arr.size()+"개");
			app.showTextArea(arr);
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
	public void addMemo() {
		//[1] app의 tfName, tfMsg에 입력한 값 얻어오기
		String name = app.tfName.getText();
		String msg = app.tfMsg.getText();
		//[2] 유효성 검사
		if(name==null||msg==null||name.trim().isEmpty()) {
			app.showMessage("작성자와 메모내용을 입력해야 해요");
			app.tfName.requestFocus();
			return;
		}
		//[3] [1]번에서 받은 값을 MemoVO객체에 담아준다
		MemoVO memo=new MemoVO(0,name,msg,null);
		//[4] dao의 insertMemo()호출한다
		try {
			int result=dao.insertMemo(memo);
		//[5] 그 결과값에 따라 메시지 처리
			if(result>0) {
				app.setTitle("글 등록 성공");
			}else {
				app.showMessage("글 등록 실패");
			}
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}
}
