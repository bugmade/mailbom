<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>매일봄 관리플랫폼</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
    <link rel="stylesheet" href="/resources/css/jquery/jquery-ui.css">
    <link rel="stylesheet" href="/resources/css/common.css">
    <!--script -->
    <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
    <script src="/resources/js/common.js"></script>
</head>
<body>
<div class="login_wrap">
    <div class="login_bg">
        <div class="login_box">
            <h1 class="login_logo">
                <span class="ir_so">매일봄 관리플랫폼</span>
            </h1>
            <form id="loginForm" name="loginForm" method="post">
                <div class="input_wrap">
                    <div class="loginform_wrap">
                        <input type="text" id="admId" class="fullformtype loginformtype" name="admId" placeholder="아이디를 입력하세요"/>
                    </div>
                    <div class="loginform_wrap">
                        <input type="password" id="admPw" class="fullformtype loginformtype"  name="admPw" placeholder="비밀번호를 입력하세요"/>
                    </div>
                </div>
                <div class="save_check">
                    <input type="checkbox" id="saveAdmId" name="saveAdmId" class="ir_so" value="Y">
                    <label for="saveAdmId">
                        <span class="ck_box"></span>
                        아이디 저장
                    </label>
                </div>
            </form>
            <div class="login_btnarea">
                <button id="btnLogin" class="login_btn">로그인 하기</button>
            </div>
        </div>
    </div>
</div>
<div id="openAlertPopup" class="pop_wrap view" style="display: none">
    <div class="sampleing_pop login_pop">
        <h3 class="pop_tit">확인</h3>
        <button class="pop_close" onclick="alertPopupClose();">
            <span class="ir_so">팝업 닫기 버튼</span>
        </button>
        <div id="alertPopupMsg" class="ta_c pop_txt">
        </div>
        <div class="btn_area mt20">
            <div class="ta_c">
                <button id="alertPopupAnchor" type="button" onclick="alertPopupClose();" class="btn round white reverse">확인</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<!-- -->
<script type="text/javascript">
    $(document).ready(function() {
        $("#admPw").keypress(function(e){
            if(e.keyCode==13) $('#btnLogin').trigger('click');
        });

        $("#admPw").on("keyup", (e) => {
            $("#admPw").val(e.target.value.trim());
        });

        let saveLoginIdYN = localStorage.getItem("saveLoginIdYN");

        if(saveLoginIdYN === "Y") {
            $("#saveAdmId").prop("checked", true);
            $("#loginForm input[name=admId]").val(localStorage.getItem("saveLoginId"));
        }

        $('#btnLogin').on('click', () => {
            saveLoginId();
            if(!validateLogin()) return;
            $('#loginForm').submit();
        });

        <c:if test="${not empty error}">
        alertPopupOpen("아이디 또는 비밀번호가 유효하지 않습니다.");
        </c:if>
        $('#saveAdmId').on('click', () => {
            saveLoginId();
        })

        $("#loginForm input[name=admPw]").on("keydown", (e) => {
            if(e.keyCode == 13) {
                $("#btnLogin").trigger("click");
                return false;
            }
        });
    });

    const saveLoginId = () => {
        let checked = $('#saveAdmId').is(":checked");
        if(checked) {
            localStorage.setItem("saveLoginIdYN", "Y")
            localStorage.setItem("saveLoginId", $("#admId").val());
        } else {
            localStorage.removeItem("saveLoginIdYN");
            localStorage.removeItem("saveLoginId");
        }
    }

    const validateLogin = () => {
        if ($("#loginForm input[name=admId]").val().trim() === "") {
            alertPopupOpen('아이디를 입력해주세요');
            return false;
        }
        if ($("#loginForm input[name=admPw]").val().trim() === "") {
            alertPopupOpen('비밀번호를 입력해주세요');
            return false;
        }
        return true;
    }
</script>
