package memo.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.text.*;
/**
 * ------------------------------------
 * @author 임규빈
 * 작성일: 2023-04-05
 * 버전: 1.1
 * 한줄 메모장 애플리케이션의 화면을 담당하는 클래스
 * ------------------------------------
 * 
 *MFC(c++),JFC(java swing)
 * MVC 패턴===> 웹 mvc패턴 도입
 * 
 * 모델1 방식 : mvc패턴을 적용하지 않을 때
 * 모델2 방식 : mvc패턴 적용
 * 
 * Model  :  DB접근 로직(DB처리 로직을 갖는다. CRUD) [Persistence Layer-영속성 계층]
 * 			VO(Value Object), DAO(Data Transfer Object), DAO(Data Access Object): DB에 접근해서 CRUD로직을 수행함
 * View   :  화면단 (Presentatioin Layer) - Swing, HTML(JSP)
 * 
 * Controller: Model View사이에서 제어하는 역할을 담당하는 계층. Event Handler,  Servlet/SpringFramework Controller
 * 
 * MemoAppView:=> GUI /View 담당 [Presentatioin Layer]
 * XXXDAO: DB접근 로직(DB처리 로직을 갖는다. CRUD)
 * 		   Data Access Object  [Persistence Layer-영속성 계층]
 * XXXVO/XXXDTO [Domain Layer]
 *  Value Object/ Data Transfer Object
 * 	: 사용자가 입력한 값을 담거나 DB에서 가져온 값을 갖고 있는
 *    객체
 * 
 * */
public class MemoAppView extends JFrame {

	Container cp;
	JPanel pN = new JPanel(new GridLayout(2,1));//2행1열 북쪽
	JPanel pS = new JPanel();//FlowLayout 남쪽
	
	JPanel pN_sub=new JPanel(new GridLayout(2,1));
	
	JPanel pN_sub_1=new JPanel();
	JPanel pN_sub_2=new JPanel();
	
	JTextArea ta;//중앙
	JButton btAdd, btList,btDel,btEdit,btEditEnd,btFind;
	JLabel lbTitle, lbName, lbDate, lbNo, lbMsg;
	JTextField tfName, tfDate, tfNo, tfMsg;
	
	MemoHandler handler;//Controller

	public MemoAppView() {
		super("::MemoAppView::");
		cp = this.getContentPane();
		
		ta=new JTextArea("::한줄 메모장::");
		JScrollPane sp=new JScrollPane(ta);
		
		cp.add(sp, "Center");
		cp.add(pN,"North");
		cp.add(pS,"South");
		ta.setEditable(false);//편집 불가. 읽기전용
		
		lbTitle=new JLabel("♥♥ 한줄 메모장 ♥♥", JLabel.CENTER);
		pN.add(lbTitle);
		pN.add(pN_sub);
		
		pN_sub.add(pN_sub_1);
		pN_sub.add(pN_sub_2);
		
		pN_sub_1.setLayout(new FlowLayout(FlowLayout.LEFT));
		pN_sub_1.add(lbName=new JLabel("작성자: "));
		pN_sub_1.add(tfName=new JTextField(15));
		
		pN_sub_1.add(lbDate=new JLabel("작성일: "));
		pN_sub_1.add(tfDate=new JTextField(15));
		
		pN_sub_1.add(lbNo=new JLabel("글번호: "));
		pN_sub_1.add(tfNo=new JTextField(15));
		
		pN_sub_2.setLayout(new FlowLayout(FlowLayout.LEFT));
		pN_sub_2.add(lbMsg=new JLabel("메모내용: "));
		pN_sub_2.add(tfMsg=new JTextField(40));
		pN_sub_2.add(btAdd=new JButton("글등록"));
		pN_sub_2.add(btList=new JButton("글목록"));
		
		
		tfDate.setEditable(false);
		tfNo.setEditable(false);
		tfDate.setForeground(Color.blue);
		String date=this.getSysDate();
		tfDate.setText(date);
		tfDate.setFont(new Font("Serif",Font.BOLD,14));
		tfDate.setHorizontalAlignment(JTextField.CENTER);
		
		lbTitle.setFont(new Font("Serif",Font.BOLD,28));
		
		pS.add(btDel=new JButton("글삭제"));
		pS.add(btEdit=new JButton("글수정"));
		pS.add(btEditEnd=new JButton("글수정 처리"));
		pS.add(btFind=new JButton("글검색"));
		//리스너 부착
		//****MemoHandler와 MemoAppView가 연동하기 위해서는 생성자에서 this를 넘겨준다
		handler=new MemoHandler(this);
		btAdd.addActionListener(handler);
		btList.addActionListener(handler);
		btDel.addActionListener(handler);
		btEdit.addActionListener(handler);
		btEditEnd.addActionListener(handler);
		btFind.addActionListener(handler);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}//생성자-------------
	
	/**
	 * 메시지를 대화창에 보여주는 메서드
	 **/
	public void showMessage(String str) {
		JOptionPane.showMessageDialog(this, str);
	}
	
	/**입력필드를 모두 빈문자열로 초기화하는 메서드
	 */
	public void clearInput() {
		tfNo.setText("");
		tfName.setText("");
		tfMsg.setText("");
		tfName.requestFocus();
	}
	
	/**현재 날짜를 yyyy-MM-dd 포맷의 문자열로 변환하여 반환하는 메서드*/
	public String getSysDate() {
		Date today=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String str=sdf.format(today);
		return str;
	}

	/** 전체 메모글을 텍스트에리어에 출력해주는 메서드*/
	public void showTextArea(List<MemoVO> arr) {
		if(arr==null||arr.size()==0) {
			ta.setText("데이터가 없습니다");
		}else {
			ta.setText("");
			ta.append("==================================================================================\n");
			ta.append("글번호\t작성자\t메모내용\t\t\t\t\t작성일\n");
			ta.append("==================================================================================\n");
			for(MemoVO vo:arr) {
				ta.append(vo.getNo()+"\t"+vo.getName()+"\t"+vo.getMsg()+"\t"+vo.getWdate()+"\n");
			}			
		}
	}

	public String showInputDialog(String str) {
		String res=JOptionPane.showInputDialog(str);
		return res;
	}//---------------------------------
	
	public void setText(MemoVO vo) {
		if(vo!=null) {
			tfNo.setText(String.valueOf(vo.getNo()));
			tfName.setText(vo.getName());
			tfMsg.setText(vo.getMsg());
			tfDate.setText(vo.getWdate().toString());
		}
	}
	
	public static void main(String[] args) {
		MemoAppView my = new MemoAppView();
		my.setSize(900, 500);
		my.setVisible(true);
	}

}

