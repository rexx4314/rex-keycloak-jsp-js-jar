package main.java;

import com.acorn.authz.service.TokenService;
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

@WebServlet("/generate-token")
public class TokenServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(TokenServlet.class.getName());
    private final TokenService tokenService = new TokenService();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 Jackson ObjectMapper

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String tenantId = request.getParameter("tenantId");

        if (userId == null || tenantId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"userId와 tenantId를 입력하세요\"}");
            return;
        }

        try {
            // TokenService를 통해 토큰과 사용자 정의 클레임 데이터를 가져옴
            Map<String, Object> tokenData = tokenService.getTokenWithCustomClaims(userId, tenantId);

            // JSON 응답 작성
            String jsonResponse = objectMapper.writeValueAsString(tokenData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);

            logger.info("토큰 생성 성공: " + jsonResponse);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "토큰 생성 실패", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"토큰 생성 실패: " + e.getMessage() + "\"}");
        }
    }
}
