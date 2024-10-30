<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>[JS] Keycloak 인증 및 인가 테스트</title>
</head>
<body>
<h1>[JS] Keycloak 인증 및 인가 테스트 - Access Type: Public</h1>

<button id="loginButton" disabled>로그인</button>
<button id="tokenButton" disabled>토큰 보기</button>
<button id="logoutButton" type="button">로그아웃</button>
<button onclick="goToIndex()">INDEX 페이지로 이동</button>
<p id="result"></p>

<script type="module">
    import Keycloak from 'http://localhost:18080/js/keycloak.js';

    const keycloak = new Keycloak({
        url: 'http://localhost:8080',
        realm: 'rex-realm',
        clientId: 'rex-client'
    });

    keycloak.init({
        onLoad: 'check-sso',
        // onLoad: 'login-required',
        checkLoginIframe: false
    }).then(authenticated => {
        document.getElementById('result').innerText = authenticated ? "로그인 상태입니다." : "로그인하지 않았습니다.";

        document.getElementById("loginButton").disabled = false;
        document.getElementById("tokenButton").disabled = false;

        document.getElementById("loginButton").addEventListener("click", keycloakLogin);
        document.getElementById("tokenButton").addEventListener("click", showToken);
        document.getElementById("logoutButton").addEventListener("click", logout);
    }).catch(() => {
        document.getElementById('result').innerText = "Keycloak 초기화 오류";
    });

    function keycloakLogin() {
        keycloak.login().then(() => {
            document.getElementById('result').innerText = "로그인 성공!";
        }).catch(() => {
            document.getElementById('result').innerText = "로그인 실패";
        });
    }

    function showToken() {
        if (keycloak.authenticated) {
            document.getElementById('result').innerText = "액세스 토큰: " + keycloak.token;
        } else {
            document.getElementById('result').innerText = "로그인 상태가 아닙니다.";
        }
    }

    function logout() {
        keycloak.logout().then(() => {
            document.getElementById('result').innerText = "로그아웃 되었습니다.";
        }).catch(() => {
            document.getElementById('result').innerText = "로그아웃 실패";
        });
    }
</script>

<script>
    function goToIndex() {
        window.location.href = "index.jsp";
    }
</script>
</body>
</html>
