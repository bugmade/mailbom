<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/resources/css/jquery/jquery-ui.css">
    <link rel="stylesheet" href="/resources/css/common.css">
    <!--script -->
    <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
    <script src="/resources/js/common.js"></script>
    <script src="/resources/js/jquery.twbsPagination.js"></script>

    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="https://jqueryui.com/resources/demos/style.css">
<%--    @@@ 아래것을 막으니 paging 과 datepicker 모두 정상 동작???--%>
<%--    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>--%>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <script>
        $( function() {
            $( "#datepicker" ).datepicker({
                //minDate: '+1D',
                // gotoCurrent: false,
                beforeShow: function (input, dpobj) {
                    var widget = $( "#" + input.id ).datepicker( "widget" );
                    setTimeout(function () {
                        widget.find('a.ui-state-highlight').removeClass('ui-state-highlight');
                        widget.find('a.ui-state-hover').removeClass('ui-state-hover');
                    }, 0);
                }
            });
        } );
    </script>
</head>
<header class="top">
    <div class="top_inner row">
        <div class="logo_wrap">
            <a href="/main">
                <h1 class="nav_logo">
                    <span class="ir_so">매일봄 관리플랫폼</span>
                </h1>
            </a>
        </div>
        <sec:authorize access="isAuthenticated()">
            <ul id="menu_depth01" class="menu_depth01">
                <li class="depth01_li">
                    <a href="/infoMng/product">정보관리</a>
                    <ul class="menu_depth02" style="display: none;">
                        <li class="depth02_li">
                            <a href="/infoMng/product" class="sub_d_link">제품정보관리</a>
                        </li>
                        <li class="depth02_li">
                            <a href="/infoMng/staff" class="sub_d_link">직원정보관리</a>
                        </li>
                        <li class="depth02_li">
                            <a href="/infoMng/consumer" class="sub_d_link">고객정보관리</a>
                        </li>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/pharmComp/list" class="sub_d_link">의약품 정보 관리</a>--%>
<%--                        </li>--%>
                    </ul>
                </li>
                <li class="depth01_li">
                    <a href="/inoutMng/stock">입출고관리</a>
                    <ul class="menu_depth02" style="display: none;">
                        <li class="depth02_li">
                            <a href="/inoutMng/stock" class="sub_d_link">제품입출고관리</a>
                        </li>
                        <li class="depth02_li">
                            <a href="/inoutMng/sales" class="sub_d_link">제품출고이력</a>
                        </li>
                    </ul>
                </li>
<%--                <li class="depth01_li">--%>
<%--                    <a href="/">메뉴3</a>--%>
<%--                    <ul class="menu_depth02" style="display: none;">--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴3-1</a>--%>
<%--                        </li>--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴3-2</a>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--                <li class="depth01_li">--%>
<%--                    <a href="/">메뉴4</a>--%>
<%--                    <ul class="menu_depth02" style="display: none;">--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴4-1</a>--%>
<%--                        </li>--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴4-2</a>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--                <li class="depth01_li">--%>
<%--                    <a href="/">메뉴5</a>--%>
<%--                    <ul class="menu_depth02" style="display: none;">--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴5-1</a>--%>
<%--                        </li>--%>
<%--                        <li class="depth02_li">--%>
<%--                            <a href="/" class="sub_d_link">메뉴5-2</a>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
            </ul>
        </sec:authorize>

        <div class="tool_wrap">
            <sec:authorize access="isAuthenticated()">
                <div class="my_infowrap">
                    <a href="javascript:openPopupUpdatePop('${webUtils.getLogin().getAdm().getAdmId()}');" class="ico_user" title="나의 정보 수정">
                        <span class="ir_so">나의 정보 수정</span>
                    </a>
                    <a href="/logout" class="ico_logout" title="로그아웃">
                        <span class="ir_so">로그아웃</span>
                    </a>
                </div><!--//myInfoWrap-->
                <div class="username_box">
                    <span>${webUtils.getLogin().getAdm().getAdmNm()}</span>님 환영합니다.
                </div>
            </sec:authorize>
        </div><!--//toolWrap-->
    </div><!--//top_inner-->
</header><!--//top-->

<!-- Alert POPUP:S -->
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
<!-- //openAlertPopup -->

<!-- openConfirmPopup -->
<div id="openConfirmPopup" class="pop_wrap view" style="display: none">
    <div class="sampleing_pop login_pop">
        <h3 class="pop_tit">확인</h3>
        <button class="pop_close" onclick="confirmPopupResult(false)">
            <span class="ir_so">팝업 닫기 버튼</span>
        </button>
        <div id="confirmPopupMsg" class="ta_c pop_txt">
        </div>
        <div class="btn_area mt20">
            <div class="ta_c">
                <button type="button" id="confirmCancel" onclick="confirmPopupResult(false)" class="btn round white reverse btn_close mr20" >취소</button>
                <button type="button" id="confirmOK" onclick="confirmPopupResult(true)" class="btn round white btn_check">확인</button>
            </div>
        </div>
    </div>
</div>
<!-- //openConfirmPopup -->

<div id="loadingBarPopup" class="loading" style="display: none;">
    <div class="loading_layer">
        <div class="spinner_icon">
            <div class="spinner_icon1 spinner_icon_item"></div>
            <div class="spinner_icon2 spinner_icon_item"></div>
            <div class="spinner_icon3 spinner_icon_item"></div>
            <div class="spinner_icon4 spinner_icon_item"></div>
            <div class="spinner_icon5 spinner_icon_item"></div>
            <div class="spinner_icon6 spinner_icon_item"></div>
            <div class="spinner_icon7 spinner_icon_item"></div>
            <div class="spinner_icon8 spinner_icon_item"></div>
            <div class="spinner_icon9 spinner_icon_item"></div>
        </div>
        <div class="loading_txt">
            <em><span id="loadingTxt">잠시만 기다려주세요.</span></em>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        //메뉴 영역 마우스 이벤트
        $(".depth01_li").mouseenter(function(e){
            $(this).addClass("on");
            $(this).children(".menu_depth02").stop().slideDown(200);
        }).mouseleave(function(){
            $(this).removeClass("on");
            $(this).children(".menu_depth02").stop().slideUp(200);
        });

        // 모달 엔터키가 눌린 경우 주의 모달, 확인 모달 누름 처리
        document.addEventListener("keydown", function (e) {
            if (e.key === "Enter" && $('#openAlertPopup').is(":visible")) {
                e.preventDefault();
                $("#alertPopupAnchor").trigger("click")
            }

            else if (e.key === "Enter" && $('#openConfirmPopup').is(":visible")) {
                e.preventDefault();
                $("#confirmOK").trigger("click")
            }
        });
    });
</script>
