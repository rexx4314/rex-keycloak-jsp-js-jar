package main.java;

import com.acorn.authz.service.AuthService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/check-authorization")
public class RoleAuthServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RoleAuthServlet.class.getName());
    private final AuthService authService = new AuthService(); // AuthService를 통한 권한 확인
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 Jackson ObjectMapper

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String systemDiv = request.getParameter("systemDiv");
        String userId = request.getParameter("userId");
        String menuId = request.getParameter("menuId");

        if (systemDiv == null || userId == null || menuId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"systemDiv, userId, menuId 모두 입력하세요\"}");
            return;
        }

        try {
            // AuthService를 통해 권한 확인
            String authorizationResult = authService.checkAuthorization(systemDiv, userId, menuId);

            // JSON 응답 작성
            String jsonResponse = objectMapper.writeValueAsString(Map.of("authorized", authorizationResult));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);

            logger.info("권한 확인 성공: " + jsonResponse);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "권한 확인 실패", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"권한 확인 실패: " + e.getMessage() + "\"}");
        }
    }
}