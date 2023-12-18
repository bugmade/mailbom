<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<style>
    /* popup style 설정 vvv */

    /* popup table style 설정 vvv */
    .popup_table {
        border-collapse:collapse;
        width:100%;
        text-align:center;
    }
    .popup_table td { border:solid 1px #cccccc; height:30px; }
    .popup_table th { border:solid 1px #cccccc; height:30px; }
    /* table style 설정 ^^^ */

    /* 전체를 감싸고 있는 div에 fixed 부여 함으로 써 content의 길이가 길어져도 dimmed가 배경에 100% 깔릴 수 있게 함 */
    .layer_popup{
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
    }
    /* absolute / margin 을 통한 화면 정중앙 정렬 및 디자인*/
    .layer{
        width: 500px;
        height: 330px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        position: absolute;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        margin: auto;
        border-radius: 5px;
        box-shadow: 0 0 10px -4px;
    }
    /* 버튼 table로 넓이 조절 */
    .btn_area{
        padding: 30px;
        display: table;
        table-layout: fixed;
        width: 100%;
        box-sizing: border-box;
    }
    /* 버튼 디자인 및 table-cell로 넓이 조절 */
    .btn_area > button{
        border: 1px solid;
        padding: 13px 18px;
        color: #fff;
        font-size: 18px;
        background-color: gray;
        box-sizing: border-box;
        display: table-cell;
        width: 45%;
        margin-right: 2.5%;
        cursor: pointer;
        box-shadow: 0px 0px 8px -5px #000;
    }
    /* register 넓이 / 색상 */
    .btn_area > .btn_register{
        width: 25%;
        background-color: #00acee;
    }
    /* cancel 넓이 / 색상 */
    .btn_area > .btn_hide{
        width: 25%;
        background-color: #ff7b89;
        margin: 0;
    }
    /* 닫기 버튼  디자인 및 위치 조절*/
    .btn_close{
        border: none;
        font-size: 15px;
        width: 80px;
        height: 25px;
        background: transparent;
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
        font-weight: bold;
    }
    /* 딤드 처리 */
    .dimmed{
        background-color: rgba(0, 0, 0, 0.3);
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        z-index: -1;
    }
    /* popup style 설정 ^^^ */

</style>

<head>
    <title>제품입출고 관리</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    제품입출고 관리
                </h3>
                <div class="white_box bd_gr">
                    <form:form id="searchForm" name="searchForm" method="get" action="/inoutMng/stock" modelAttribute="stockListReq">
                        <table class="gray_table half_table">
                            <colgroup>
                                <col width="16.5%">
                                <col width="*">
                                <col width="16.5%">
                                <col width="*">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th>
                                        <label for="searchWord">검색조건</label>
                                    </th>
                                    <td colspan="3" class="fz0">
                                        <form:select path="inOut" style="width:170px;" id="inoutSelect" onchange="changeInoutSelect()" cssClass="basic_formtype select_form select_sch">
                                            <form:option value="ALL">전체(입출고)</form:option>
                                            <form:option value="1">입고</form:option>
                                            <form:option value="2">출고</form:option>
                                        </form:select>
                                        <form:select path="outWy" style="width:170px;" id="outwySelect" onchange="changeInoutSelect()" cssClass="basic_formtype select_form select_sch">
                                            <form:option value="ALL">전체(출고사유)</form:option>
                                            <form:option value="1">납품</form:option>
                                            <form:option value="2">증정</form:option>
                                            <form:option value="3">기타</form:option>
                                        </form:select>
                                        <form:input type="text" path="searchWord" value="" style="width:300px;" id="searchWord" cssClass="basic_formtype search_form" placeholder="특정 납품처 검색어를 입력하세요"/>
                                        <button id="searchBtn" class="search_btn">
                                            검색
                                            <span class="ir_so">검색버튼</span>
                                        </button>
                                        <button class="init_btn_j" onclick="doInit()">초기화</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form:form>
                </div>
                <!-- //white_box -->

                <c:set value="${result.getTotalElements()}" var="totalCount"/>
<%--                <div class="clgraph_info clearfix mb20">--%>
<%--                    <h4 class="wb_tit fl_l">--%>
<%--                        총 <span class="col_acc">${totalCount}</span> 건--%>
<%--                    </h4>--%>
<%--                </div>--%>
                    <div style="margin-bottom: 5px; margin-top: 5px;">
                        총 <span >${totalCount}</span> 건
                        <button class="input_btn_j" onclick="showStockPopup('CREATE_INPUT')">입고</button>
                        <button class="output_btn_j" onclick="showStockPopup('CREATE_OUTPUT')">출고</button>
                    </div>

                <table class="list_table">
                    <colgroup>
                        <col width="8%">
                        <col width="8%">
                        <col width="8%">
                        <col width="8%%">
                        <col width="8%">
                        <col width="8%">
                        <col width="20%">
                        <col width="5%">
                        <col width="10%">
                        <col width="*">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>입출고번호</th>
                        <th>제품명</th>
                        <th>구분</th>
                        <th>수량</th>
                        <th>출고사유</th>
                        <th>납품처</th>
                        <th>메모</th>
                        <th>등록자</th>
                        <th>등록일</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${totalCount == 0}">
                            <td colspan="9" class="list_none">등록된 내역이 없습니다.</td>
                        </c:when>
                        <c:otherwise>
                            <c:set var="idx" value="${totalCount - ((result.getNumber()) * result.getSize())}"/>
                            <c:forEach var="row" items="${result.getContent()}" varStatus="status">
                                <tr>
<%--                                    <td>${idx-status.index}</td>--%>
<%--                                    <td class="pl20 ta_c ellipsis">--%>
<%--                                        <a class="list_link ellipsis" href="">${row.proNm}</a>--%>
<%--                                    </td>--%>
                                    <td>${row.stoNo}</td>
                                    <td>${row.proNm}</td>
                                    <c:choose>
                                        <c:when test="${row.inOut == '1'}">
                                            <td>입고</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>출고</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${row.ioCnt}</td>
                                    <c:choose>
                                        <c:when test="${row.inOut == '2'}">
                                            <c:choose>
                                                <c:when test="${row.outWy == '1'}">
                                                    <td>납품</td>
                                                    <td>${row.csmNm}</td>
                                                </c:when>
                                                <c:when test="${row.outWy == '2'}">
                                                    <td>증정</td>
                                                    <td></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>기타</td>
                                                    <td></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <td></td>
                                            <td></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${row.memo}</td>
                                    <td>${row.regId}</td>
                                    <td>
<%--                                        <javatime:format value="${row.regDt}" pattern="yyyy-MM-dd"/>--%>
                                        ${row.regDt}
                                    </td>
                                    <td>
                                        <button class="delete_btn_j" onclick="deleteStock(${row.stoNo})">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- //white_box -->

                <div class="pagination_area">
                    <form:form modelAttribute="stockListReq">
                        <form:select path="pageSize" cssClass="select_form">
                            <form:option value="10">10개 씩보기</form:option>
                            <form:option value="50">50개 씩보기</form:option>
                            <form:option value="100">100개씩 보기</form:option>
                        </form:select>
                    </form:form>
                    <!-- //selsect -->

                    <c:if test="${totalCount > 0}">
                        <ul class="pagination" id="pagination-number"></ul>
                    </c:if>
                    <!-- //pagination -->

<%--                페이징 중앙 정렬을 위해 --%>
                    <div>(C)MAILBOM Inc.</div>
                </div><!--//pagination_area-->
            </div>
            <!-- //contwrap -->

            <%-- paging 태그에서 페이지 번호를 클릭했을때 자바스크립트 함수로 실행하게될 form을 작성한다(읽기전용 form임)--%>
            <form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="stockListReq">
<%--                <form:hidden id="pagingSearchType6" path="searchType"/>--%>
                <form:hidden id="pagingSearchType6" path="inOut"/>
                <form:hidden id="pagingSearchType8" path="outWy"/>
                <form:hidden id="pagingSearchType7" path="searchWord"/>
                <form:hidden id="pagingCurrentPageNo" path="pageNo"/>
                <form:hidden id="pagingRecordCountPerPage" path="pageSize"/>
            </form:form>
        </div>
        <%@ include file="../../layout/footer.jsp" %>
    </div>
    <!-- //container -->
    <button class="btn_top">
        <span>맨 위로</span>
    </button>
    <!-- vvv 팝업 -->
    <div class="layer_popup" id="layer_popup" style="display: none;">
        <div class="layer">
            <h3 class="cont_tit" style="text-align: center;">
                <div id="io_title"></div>
            </h3>
            <!--팝업 컨텐츠 영역-->
            <div class="popup_cont">
                <form method="post" id="frmReg">
                    <input type="hidden" name="login_id" value="${webUtils.getLogin().getAdm().getAdmId()}">
                    <input type="hidden" name="in_out" id="in_out" value="1">
                    <table class="popup_table">
                        <thead>
                        </thead>
                        <tbody id="io_tbody">
                        <tr>
                            <td>제품명</td>
                            <td>
                                <select name="pro_cd" class="pro_cd" style="width: 98%; height: 90%">
                                    <c:if test="${fn:length(product) > 0}">
                                        <c:forEach var="item" items="${product}">
                                            <option value="${item.proCd }">${item.proNm }</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>수량</td>
                            <td><input type="number" pattern="[0-9]+" name="io_cnt" id="io_cnt" style="width: 98%"></td>
                        </tr>
                        <tr>
                            <td>메모</td>
                            <td><input type="text" maxlength='100' name="memo" id="memo" style="width: 98%"></td>
                        </tr>
                        <tr>
                            <td>
                                <div id="out_wy_td_title"></div>
                            </td>
                            <td>
                                <div id="out_wy_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="csm_cd_td_title"></div>
                            </td>
                            <td>
                                <div id="csm_cd_td_content" style="width: 98%; height: 90%"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <!--팝업 버튼 영역-->
            <button class="save_btn_j" onclick="createStock()">저장</button>
            <button class="close_btn_j" onclick="hideStockPopup()">닫기</button>
            <div class="dimmed"></div>
        </div>
    </div>
    <!-- ^^^ 팝업 -->
</div>
</body>
<script type="text/javascript">
    const IO_INPUT = '1';       // 입고
    const IO_OUTPUT = '2';      // 출고
    const OUTWY_SALE = '1';     // 출고사유: 납품
    const OUTWY_GIFT = '2';     // 출고사유: 증정
    const OUTWY_ETC = '3';      // 출고사유: 기타

    $(document).ready(function(){
        console.log(${result.getTotalPages()}); //JSTL
        console.log(${result.getNumber()}); //JSTL

        $('#pagination-number').twbsPagination({
            totalPages: <c:out value="${result.getTotalPages()}"/>,
            visiblePages: 10,
            startPage: <c:out value="${result.getNumber()+1}"/>,
            first: " ",
            prev: " ",
            next: " ",
            last: " ",
            prevClass: 'pg_ctrl pg_prev',
            nextClass: 'pg_ctrl pg_next',
            firstClass: 'pg_ctrl pg_first',
            lastClass: 'pg_ctrl pg_last',
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                go_page(page);
            }
        });

        // $('#btnRegist').on('click', () => {
        //     location.href = '/pharmComp/registForm';
        // });

        $("#searchBtn").on("click", (e) => {
            e.preventDefault();
            $("#pagingCurrentPageNo").val(1);
            $("#searchForm").submit();
        });

        $("#searchWord").on("keydown", (e) => {
            if(e.keyCode == 13){
                $("#searchBtn").trigger("click");
                return false;
            }
        });

        $("#pageSize").change(function() {
            $("#pagingForm input[name=pageSize]").val($(this).val());
            $("#pagingForm input[name=pageNo]").val(1);
            $("#pagingForm")[0].submit();
        });

        changeInoutSelect();
    });

    const go_page = (pageno) => {
        $("#pagingCurrentPageNo").val(pageno);
        $("#pagingForm")[0].submit();
    }

    // 입출력내역 삭제하기
    function deleteStock(sto_no) {
        console.log('deleteStock: %s', sto_no);
        if(!confirm("입출고번호:"+sto_no+" 삭제할까요?")) {
            return;
        }
        let params = {sto_no: sto_no};

        $.ajax({
            dataType : "html",
            type : "POST",
            url : "/inoutMng/api/deleteStock",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('deleteStock success');
                console.log(data);
                alert("삭제되었습니다");
                location.reload();
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    /**
     * form 내의 name tag 와 값을 object 형태로 만들어줌, jinni20231201
     **/
    jQuery.fn.serializeObject = function() {
        let obj = null;
        try {
            if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) {
                let arr = this.serializeArray();
                if(arr){
                    obj = {};
                    jQuery.each(arr, function() {
                        obj[this.name] = this.value;
                    });
                }
            }
        }catch(e) {
            alert(e.message);
        }finally  {}
        return obj;
    }

    // 서버에 재고 등록하기
    function createStock(){
        console.log('createStock');
        if($('#io_cnt').val() < 1) {
            alert("수량은 1개 이상 입력하세요");
            $('#io_cnt').focus();
            return;
        }

        //let in_out = $('input:radio[name="in_out"]:checked').val();
        let in_out = $('#in_out').val();
        if(in_out === IO_INPUT) {
            in_out = '입고';
        } else {
            in_out = '출고';
        }

        if(!confirm("[" + $(".pro_cd :selected").text() + "] " + $('#io_cnt').val() + "개 " + in_out + " 처리할까요?")) {
            return;
        }

        let params = $('#frmReg').serializeObject();
        console.log(params);

        $.ajax({
            dataType : "html",
            type : "POST",
            url : "/inoutMng/api/createStock",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('createStock success');
                console.log(data);
                alert("저장되었습니다");
                location.reload();
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    // 입출고 select box 동적 표시
    function changeInoutSelect() {
        let inOut = $("#inoutSelect option:selected").val();
        let outWy = $("#outwySelect option:selected").val();

        if(inOut === '1' || inOut === 'ALL') {   //입고 또는 전체
            $("#outwySelect option:eq(0)").prop("selected", true);
            $("#outwySelect").attr("disabled", true);
            $("#searchWord").val("");
            $("#searchWord").attr("disabled", true);
        } else {
            $("#outwySelect").attr("disabled", false);
            if(outWy == '1') {      //납품
                $("#searchWord").attr("disabled", false);
            } else {
                $("#searchWord").attr("disabled", true);
            }
        }
    }

    // 검색조건 초기화
    function doInit() {
        $("#inoutSelect option:eq(0)").prop("selected", true);
        $("#outwySelect option:eq(0)").prop("selected", true);
        $("#searchWord").val("");
        location.reload();
    }

    // 입출고 팝업을 보여주기
    function showStockPopup(mode){
        console.log('showStockPopup: '+mode);

        let html = '';
        if(mode === 'CREATE_INPUT') {
            $("#io_title").html("제품입고");
            $("#in_out").val("1");

            html = '';
            $("#csm_cd_td_title").html(html);
            $("#csm_cd_td_content").html(html);
            $("#out_wy_td_title").html(html);
            html = '<input type="hidden" name="out_wy" value="1">';
            $("#out_wy_td_content").html(html);
        } else {
            $("#io_title").html("제품출고");
            $("#in_out").val("2");

            html = '출고사유';
            $("#out_wy_td_title").html(html);
            html = '<input type="radio" id="out_wy1" name="out_wy" value="1" checked>';
            html += '<label for="out_wy1">납품</label>';
            html += '<input type="radio" id="out_wy2" name="out_wy" value="2">';
            html += '<label for="out_wy2">증정</label>';
            html += '<input type="radio" id="out_wy3" name="out_wy" value="3">';
            html += '<label for="out_wy3">기타</label>';
            $("#out_wy_td_content").html(html);

            html = '납품처';
            $("#csm_cd_td_title").html(html);
            html = '<select name="csm_cd" class="csm_cd" style="width: 101%; height: 90%">';
            html += '<c:if test="${fn:length(consumer) > 0}">';
            html += '<c:forEach var="item" items="${consumer}">';
            html += '<option value="${item.csmCd }">${item.csmNm }</option>';
            html += '</c:forEach>';
            html += '</c:if>';
            html += '</select>';
            $("#csm_cd_td_content").html(html);
        }

        $('#io_cnt').val("0");
        $('#memo').val("");

        $('#layer_popup').show();
    }

    // 입출고 팝업을 닫기
    function hideStockPopup(){
        console.log('hideStockPopup');

        $("#layer_popup").hide();
    }
</script>