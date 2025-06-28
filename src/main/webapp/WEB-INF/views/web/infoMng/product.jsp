<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <title>재품정보관리</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    제품정보관리
                </h3>
                <div class="white_box bd_gr">
                    <form:form id="searchForm" name="searchForm" method="get" action="/infoMng/product" modelAttribute="productListReq">
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
                                    <form:input type="text" path="searchWord" value="" style="width:300px;" id="searchWord" cssClass="basic_formtype search_form" placeholder="제품명 검색어"/>
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
                    <button class="input_btn_j" onclick="showProductPopup('CREATE')">등록</button>
                    <%--                    <button class="excel_btn_j" onclick="excelDownload()">엑셀</button>--%>
                </div>

                <table class="list_table">
                    <colgroup>
                        <col width="10%">
                        <col width="20%">
                        <col width="20%">
                        <col width="10%">
                        <col width="15%">
                        <col width="15%">
                        <col width="*">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>제품코드</th>
                        <th>제품명</th>
                        <th>제품설명</th>
                        <th>박스 당 봉지수량</th>
                        <th>본사창고재고(HQ)<br>박스/봉</th>
                        <th>위탁창고1재고(FIRST)<br>박스/봉</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${totalCount == 0}">
                            <td colspan="6" class="list_none">등록된 내역이 없습니다.</td>
                        </c:when>
                        <c:otherwise>
                            <c:set var="idx" value="${totalCount - ((result.getNumber()) * result.getSize())}"/>
                            <c:forEach var="row" items="${result.getContent()}" varStatus="status">
                                <tr>
                                    <td>${row.proCd}</td>
                                    <td>${row.proNm}</td>
                                    <td>${row.proDt}</td>
                                    <td>${row.bongBox}</td>
                                    <td>
                                        <fmt:formatNumber value="${row.hqStorage}" type="number" groupingUsed="true" />
                                        <br>
                                        <script>
                                            (function () {
                                                const ioCnt = parseInt('${row.hqStorage}');
                                                const divisor = parseInt('${row.bongBox}');
                                                const box = Math.floor(ioCnt / divisor);
                                                const remain = Math.floor(ioCnt % divisor);
                                                document.write(box.toFixed(0) + ' / ' + remain.toFixed(0));
                                            })();
                                        </script>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${row.firstStorage}" type="number" groupingUsed="true" />
                                        <br>
                                        <script>
                                            (function () {
                                                const ioCnt = parseInt('${row.firstStorage}');
                                                const divisor = parseInt('${row.bongBox}');
                                                const box = Math.floor(ioCnt / divisor);
                                                const remain = Math.floor(ioCnt % divisor);
                                                document.write(box.toFixed(0) + ' / ' + remain.toFixed(0));
                                            })();
                                        </script>
                                    </td>
                                    <td>
                                        <button class="delete_btn_j" onclick="deleteProduct('${row.proCd}')">삭제</button>
                                        <button class="update_btn_j" onclick="updateModal('${row.proCd}')">수정</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- //white_box -->

                <div class="pagination_area">
                    <form:form modelAttribute="productListReq">
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
            <form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="productListReq">
                <%--                <form:hidden id="pagingSearchType6" path="searchType"/>--%>
                <%--                <form:hidden id="pagingSearchType6" path="inOut"/>--%>
                <%--                <form:hidden id="pagingSearchType8" path="outWy"/>--%>
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

    <!-- 팝업 -->
    <div class="layer_popup" id="layer_popup" style="display: none;">
        <div class="layer">
<%--            <h1 style="text-align: center;">제품 등록</h1>--%>
            <h3 class="cont_tit" style="text-align: center;">
                <div id="popup_title"></div>
            </h3>
            <!--팝업 컨텐츠 영역-->
            <div class="popup_cont">
                <form method="post" id="frmReg">
                    <input type="hidden" name="login_id" id="login_id" value="${webUtils.getLogin().getAdm().getAdmId()}">
                    <table class="popup_table">
<%--                        <thead>--%>
<%--&lt;%&ndash;                        <tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <th>구분</th>&ndash;%&gt;--%>
<%--&lt;%&ndash;                            <th scope="col">내용</th>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        </tr>&ndash;%&gt;--%>
<%--                        </thead>--%>
                        <tbody>
                        <tr>
                            <td>제품코드 *</td>
                            <td><input type="text" maxlength='25' name="pro_cd" id="pro_cd" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>제품명 *</td>
                            <td><input type="text" maxlength='100' name="pro_nm" id="pro_nm" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>제품설명 *</td>
                            <td><input type="text" maxlength='250' name="pro_dt" id="pro_dt" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>본사창고재고</td>
                            <td><input type="number" pattern="[0-9]+" name="hq_storage" id="hq_storage" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>위탁창고1 재고</td>
                            <td><input type="number" pattern="[0-9]+" name="first_storage" id="first_storage" style="width: 98%;"></td>
                        </tr>
                        </tbody>
                    </table>
                    <%--                    <input type="submit" value="등록하기">--%>
                </form>
            </div>
            <!--팝업 버튼 영역-->
            <button class="save_btn_j" onclick="createProduct()">저장</button>
            <button class="close_btn_j" onclick="hideProductPopup()">닫기</button>
<%--            <button class="btn_close" onclick="hideProductPopup()">CLOSE</button>--%>
            <div class="dimmed"></div>
        </div>
    </div>
    <!-- //container -->
    <button class="btn_top">
        <span>맨 위로</span>
    </button>
</div>
</body>

<script type="text/javascript">
    let create_or_update;
    const DO_CREATE = '1';
    const DO_UPDATE = '2';

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
    })

    const go_page = (pageno) => {
        $("#pagingCurrentPageNo").val(pageno);
        $("#pagingForm")[0].submit();
    }

    // 검색조건 초기화
    function doInit() {
        $("#searchWord").val("");
        location.reload();
    }

    /**
     * form 내의 name tag 와 값을 object 형태도 만들어줌, jinni20231201
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

    // 서버에 제품등록하기
    function createProduct(){
        console.log('createProduct');
        if($('#pro_cd').val() =="") {
            alert("제품코드를 입력하세요");
            $('#pro_cd').focus();
            return;
        }
        if($('#pro_nm').val() =="") {
            alert("제품명을 입력하세요");
            $('#pro_nm').focus();
            return;
        }
        if($('#pro_dt').val() =="") {
            alert("제품설명을 입력하세요");
            $('#pro_dt').focus();
            return;
        }

        let params = $('#frmReg').serializeObject();
        console.log(params);

        if(create_or_update === DO_CREATE) {
            $.ajax({
                dataType: "html",
                type: "POST",
                url: "/infoMng/api/createProduct",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: params,
                success: function (data) {
                    console.log('createProduct success');
                    console.log(data);
                    if(data === '1')
                        alert("저장되었습니다");
                    else
                        alert("이미 존재하는 제품코드입니다");
                    location.reload();
                },
                error: function (data, status, err) {
                    console.log('error forward : ' + data);
                }
            });
        } else {
            $.ajax({
                dataType: "html",
                type: "POST",
                url: "/infoMng/api/updateProduct",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: params,
                success: function (data) {
                    console.log('updateProduct success');
                    console.log(data);
                    alert("저장되었습니다");
                    location.reload();
                },
                error: function (data, status, err) {
                    console.log('error forward : ' + data);
                }
            });
        }
    }

    // 제품삭제하기
    function deleteProduct(pro_cd) {
        console.log('deleteProduct: %s', pro_cd);
        if(!confirm(pro_cd+" 삭제할까요?")) {
            return;
        }
        let pwd = prompt("비밀번호를 입력하세요");
        if(pwd == null) {
            return;
        }
        else if(pwd.length < 1) {
            alert("비밀번호는 1자리 이상입니다.")
            return;
        }
        let login_id = $("#login_id").val();

        let params = {pro_cd: pro_cd, login_id: login_id, pwd: pwd};

        $.ajax({
            dataType : "html",
            type : "POST",
            url : "/infoMng/api/deleteProduct",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('deleteProduct success');
                console.log(data);
                if(data == '1') {
                    alert("삭제되었습니다");
                } else {
                    alert("비밀번호가 틀립니다");
                }
                location.reload();
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    // 제품수정화면 띄우기
    function updateModal(pro_cd) {
        console.log('updataModal: %s', pro_cd);

        let params = {pro_cd: pro_cd};

        $.ajax({
            dataType : "html",
            type : "GET",
            url : "/infoMng/api/readProductDetail",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('readProductDetail success');
                console.log(data);
                data = JSON.parse(data);

                $('#pro_cd').val(data.proCd);
                $("#pro_cd").attr("readonly",true);
                $('#pro_nm').val(data.proNm);
                $('#pro_dt').val(data.proDt);
                $('#hq_storage').val(data.hqStorage);
                $('#first_storage').val(data.firstStorage);
                showProductPopup('UPDATE');
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    // 제품등록 팝업을 보여주기
    function showProductPopup(mode){
        console.log('showProductPopup: '+mode);

        if(mode === 'CREATE') {
            $('#pro_cd').val("");
            $("#pro_cd").removeAttr("readonly");
            $('#pro_nm').val("");
            $('#pro_dt').val("");
            $('#hq_storage').val("0");
            $('#first_storage').val("0");
        }
        $('#layer_popup').show();
        if(mode === 'CREATE') {
            $('#pro_cd').focus();
            $('#popup_title').html("제품등록");
            create_or_update = DO_CREATE;
        } else {
            $('#popup_title').html("제품수정");
            create_or_update = DO_UPDATE;
        }
    }

    // 제품등록 팝업을 닫기
    function hideProductPopup(){
        console.log('hideProductPopup');

        $("#layer_popup").hide();
    }
</script>