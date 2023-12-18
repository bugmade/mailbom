<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<head>
    <title>매일봄 관리플랫폼</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../layout/header.jsp" %>
        <div class="container_inner">
            <div class="main_wrap">
                <div class="main_bg"></div>
                <!-- //main_bg -->
                <div class="main_box">
                    <h3>
                        ${webUtils.getLogin().getAdm().getAdmNm()}(${webUtils.getLogin().getAdm().getAdmId()})</span>님, 반갑습니다.
                    </h3>
                </div>
            </div>
        </div>
        <%@ include file="../layout/footer.jsp" %>
    </div>
    <!-- //container -->
    <button class="btn_top">
        <span>맨 위로</span>
    </button>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function(){

    })
</script>