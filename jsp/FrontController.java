package com;

import com.common.AppService;
import com.common.ServiceForward;
import com.example.service.MainService;
import com.example.service.ResultService;
import com.example.service.SearchService;
import com.member.service.AjaxCheckIdService;
import com.member.service.JoinFormService;
import com.member.service.JoinService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// *.do는 FrontController가 모두 처리하겠다
@WebServlet("*.do") // tomcat기준 (/** :: 8080이후 나온 모든 경로를 받는다)
public class FrontController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // post :: 주소에 방식이 안나오는것
        doProcess(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get :: 주소에 방식이 나오는 것
        doProcess(request, response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 언어를 UTF-8로 바꾸어라.
        String command = request.getRequestURI();
        // URL :: http://torang.co.kr/user
        // URI :: http://torang.co.kr/user?id=107 <- id=107 :: Query String이라 한다.(key : id, value : 107)

        ServiceForward forward = null;

        try {
            if (command.equals("/main.do")) { // 만약 "/"로 요청하면
                AppService service = new MainService();
                forward = service.execute(request, response);

            } else if (command.equals("/result.do")) { // 아니고, "/result"로 요청하면
                // ResultService 객체 service를 선언한다.
                AppService service = new ResultService();
                forward = service.execute(request, response);
            } else if (command.equals("/search.do")) { // 아니고, "/search"로 요청하면
                // SearchService 객체 service를 선언한다.
                AppService service = new SearchService();
                forward = service.execute(request, response);
            } else if (command.equals("/member/join/form.do")) {
                // JoinFormService 객체 service를 선언한다.
                AppService service = new JoinFormService();
                forward = service.execute(request, response);
            } else if (command.equals("/member/check/id.do")) {
                // AjaxCheckIdService 객체 service를 선언한다.
                AppService service = new AjaxCheckIdService();
                forward = service.execute(request, response);
            } else if (command.equals("/member/join.do")) {
                // JoinService 객체 service를 선언한다.
                AppService service = new JoinService();
                forward = service.execute(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (forward != null) {
            if (forward.isRedirect()) {
                // 리다이렉트
            } else {
                // 포워드
                RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
                dispatcher.forward(request, response);
            }
        } else { // 검색에 숫자외 글자를 검색했을때
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('잘못된 접근입니다.');history.back();</script>");
            out.close();
        }

    }
}

// forwarding :: view로 접속하는 방식.주소가 변경되지 않음. forwarding방식은 request와 response가 유지된다.
// redirect :: 주소가 변경됨.
// WEB-INF파일 :: 주소를 직접 입력해서 접속을 하려고 해도 접속이 안되는 곳
// shift + F6 :: 이름바꾸기