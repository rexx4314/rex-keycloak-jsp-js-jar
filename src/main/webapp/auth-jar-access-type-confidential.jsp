<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>[JAR] Keycloak 인증 및 인가 테스트 - Access Type: Confidential</title>
</head>
<body>
<h1>[JAR] Keycloak 인증 및 인가 테스트 - Access Type: Confidential</h1>

<button onclick="requestToken()">토큰 요청</button>
<button onclick="goToIndex()">INDEX 페이지로 이동</button>
<p id="result"></p>

<script>
    function requestToken() {
        fetch("/get-token-jar-confidential", {
            method: "POST"
        })
            .then(response => response.text())
            .then(data => {
                try {
                    const jsonData = JSON.parse(data);
                    if (jsonData.access_token) {
                        alert("액세스 토큰: " + jsonData.access_token);
                        document.getElementById("result").innerText = "액세스 토큰: " + jsonData.access_token;
                    } else {
                        alert("응답 데이터: " + JSON.stringify(jsonData));
                        document.getElementById("result").innerText = JSON.stringify(jsonData);
                    }
                } catch (e) {
                    console.error("JSON 파싱 오류:", e);
                    document.getElementById("result").innerText = data;
                }
            })
            .catch(error => console.error("에러 발생:", error));
    }

    function goToIndex() {
        window.location.href = "index.jsp";
    }
</script>

</body>
</html>
