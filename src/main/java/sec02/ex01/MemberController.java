package sec02.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;
       

	public void init(ServletConfig config) throws ServletException {
		memberDAO = new MemberDAO();
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo(); // URL에서 요청명을 가져옵니다.
		System.out.println("action : " + action);
		
		// 최초 요청이거나 action 값이 /memberList.do면 회원 목록을 출력합니다.
		if (action == null || action.equals("/listMembers.do")) { 

			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test02/listMembers.jsp"; //test2 폴더의 listMember.jsp로 포워딩합니다.
			
		// action 값이 /addMember.do 면 전송된 회원 정보를 가져와서 테이블에 추가합니다.
		} else if (action.equals("/addMember.do")) { 

			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do"; //회원 등록후 다시 회원 목록을 출력합니다.

		// action 값이 /memberForm.do 면 회원 가입창을 화면에 출력합니다.
		} else if (action.equals("/memberForm.do")) {

			nextPage = "/test02/memberForm.jsp"; //test2 폴더의 memberForm.jsp 로 포워딩합니다.

		} else { // 그 외 다른 action 값은 회원 목록을 출력합니다.

			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test02/listMembers.jsp";
			
		}
		
		//컨트롤러에서 표시하고자 하는 JSP로 포워딩합니다.
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
		
	}
}
