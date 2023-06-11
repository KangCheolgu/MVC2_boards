package sec02.ex02;

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo(); // URL에서 요청명을 가져옵니다.
		System.out.println("action : " + action);

		// 최초 요청이거나 action 값이 /memberList.do면 회원 목록을 출력합니다.
		if (action == null || action.equals("/listMembers.do")) {

			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test03/listMembers.jsp"; // test2 폴더의 listMember.jsp로 포워딩합니다.

			// action 값이 /addMember.do 면 전송된 회원 정보를 가져와서 테이블에 추가합니다.
		} else if (action.equals("/addMember.do")) {

			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do"; // 회원 등록후 다시 회원 목록을 출력합니다.

			// action 값이 /memberForm.do 면 회원 가입창을 화면에 출력합니다.
		} else if (action.equals("/memberForm.do")) {

			nextPage = "/test03/memberForm.jsp"; // test2 폴더의 memberForm.jsp 로 포워딩합니다.

			// 회원 수정창 요청시 id로 회원 정보를 조회한 후 수정창으로 포워딩합니다.
		} else if (action.equals("/modMemberForm.do")) {

			String id = request.getParameter("id");
			// 회원 정보 수정창을 요청하면서 전송된 id를 이용해 수정 전 회원 정보를 조회합니다.
			MemberVO memInfo = memberDAO.findMember(id);
			// request에 바인딩 하여 회원 정보 수정창에 수정하기 전 회원 정보를 전달합니다.
			request.setAttribute("memInfo", memInfo);
			nextPage = "/test03/modMemberForm.jsp";

			// 회원 정보 수정
		} else if (action.equals("/modMember.do")) {

			// 회원 정보 수정창에서 전송된 수정 회원 정보를 가져온 후 MemberVO 객체 속성에 설정합니다.
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.modMember(memberVO);
			// 회원 목록창으로 수정 작업 완료 메세지를 전달합니다.
			request.setAttribute("msg", "modified");
			nextPage = "/member/listMembers.do";

		// 회원 삭제
		} else if (action.equals("/delMember.do")) {
			// 받아오는 회원 아이디
			String id = request.getParameter("id");
			memberDAO.delMember(id);
			request.setAttribute("msg", "deleted");
			nextPage = "/member/listMembers.do";

		} else { // 그 외 다른 action 값은 회원 목록을 출력합니다.

			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test03/listMembers.jsp";

		}

		// 컨트롤러에서 표시하고자 하는 JSP로 포워딩합니다.
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);

	}
}
