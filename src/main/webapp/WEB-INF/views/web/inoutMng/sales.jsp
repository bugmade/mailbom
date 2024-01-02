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
        width: 600px;
        height: 420px;
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
    <title>제품출고이력</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    제품출고이력
                </h3>
                <div class="white_box bd_gr">
                    <form:form id="searchForm" name="searchForm" method="get" action="/inoutMng/sales" modelAttribute="salesListReq">
                        <table class="gray_table half_table">
                            <colgroup>
                                <col width="10%">
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
                                        <form:select path="outWy" style="width:170px;" id="outwySelect" onchange="changeInoutSelect()" cssClass="basic_formtype select_form select_sch">
                                            <form:option value="ALL">전체(출고사유)</form:option>
                                            <form:option value="BTOB">납품</form:option>
                                            <form:option value="RETAIL">소매(택배)</form:option>
                                            <form:option value="GIFT">증정</form:option>
                                            <form:option value="ETC">기타</form:option>
                                        </form:select>
                                        <form:input type="text" path="searchWord" value="" style="width:250px;" id="searchWord" cssClass="basic_formtype search_form" placeholder="납품처 검색어"/>
                                        <form:input type="text" path="startDt" id="startDt" cssClass="basic_formtype" style="width:120px; margin-right:10px;" placeholder="검색 시작일" />
                                        <form:input type="text" path="endDt" id="endDt" cssClass="basic_formtype" style="width:120px; margin-right:10px;" placeholder="검색 종료일" />
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
                    <div style="margin-bottom: 5px; margin-top: 5px;">
                        총 <span >${totalCount}</span> 건
                        <button class="excel_btn_j" onclick="excelSalesDownload()">엑셀</button>
                    </div>

                <table class="list_table">
                    <colgroup>
                        <col width="5%">
                        <col width="20%">
                        <col width="5%">
                        <col width="5%">
                        <col width="5%">
                        <col width="7%">
                        <col width="18%">
                        <col width="5%">
                        <col width="10%">
                        <col width="5%">
                        <col width="*">
                        <col width="5%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>출고번호</th>
                        <th>제품명</th>
                        <th>보관창고</th>
                        <th>로트번호</th>
                        <th>유통기한</th>
                        <th>출고사유</th>
                        <th>납품처</th>
                        <th>수량</th>
                        <th>메모</th>
                        <th>등록자</th>
                        <th>등록일</th>
                        <th>입고번호</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${totalCount == 0}">
                            <td colspan="12" class="list_none">등록된 내역이 없습니다.</td>
                        </c:when>
                        <c:otherwise>
                            <c:set var="idx" value="${totalCount - ((result.getNumber()) * result.getSize())}"/>
                            <c:forEach var="row" items="${result.getContent()}" varStatus="status">
                                <tr>
                                    <td>${row.salesNo}</td>
                                    <td>${row.proNm}</td>
                                    <td>${row.fromStorage}</td>
                                    <td>${row.lotNo}</td>
                                    <td>${row.expDt}</td>
                                    <c:choose>
                                        <c:when test="${row.outWy == 'BTOB'}">
                                            <td>납품</td>
                                            <td>${row.csmNm}</td>
                                        </c:when>
                                        <c:when test="${row.outWy == 'RETAIL'}">
                                            <td>소매(택배)</td>
                                            <td></td>
                                        </c:when>
                                        <c:when test="${row.outWy == 'GIFT'}">
                                            <td>증정</td>
                                            <td></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>기타</td>
                                            <td></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${row.outCnt}</td>
                                    <td>${row.memo}</td>
                                    <td>${row.regId}</td>
                                    <td>
                                            ${row.regDt}
<%--                                        <fmt:parseDate value='${row.regDt}' pattern="yyyy-MM-dd-hh:mm" var='carguip'/>--%>
<%--                                        <fmt:formatDate value="${carguip}" pattern="yyyy-MM-dd"/>--%>
<%--                                        <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${row.regDt}"/>--%>
                                    </td>
                                    <td>${row.stoNo}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- //white_box -->

                <div class="pagination_area">
                    <form:form modelAttribute="salesListReq">
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
            <form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="salesListReq">
                <form:hidden id="pagingSearchType6" path="startDt"/>
                <form:hidden id="pagingSearchType9" path="endDt"/>
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
                    <input type="hidden" name="login_id" id="login_id" value="${webUtils.getLogin().getAdm().getAdmId()}">
                    <input type="hidden" name="in_out" id="in_out" value="IN">
                    <table class="popup_table">
                        <thead>
                        </thead>
                        <tbody id="io_tbody">
                        <tr>
                            <td>제품명</td>
                            <td>
                                <div id="pro_cd_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>로트번호</td>
                            <td>
                                <div id="lot_no_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>유통기한</td>
                            <td>
                                <div id="exp_dt_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>수량</td>
                            <td><div id="io_cnt_td_content"></div></td>
                        </tr>
                        <tr>
                            <td>메모</td>
                            <td><div id="memo_td_content"></div></td>
                        </tr>
                        <tr>
                            <td>
                                <div id="content1_td_title"></div>
                            </td>
                            <td>
                                <div id="content1_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="content2_td_title"></div>
                            </td>
                            <td>
                                <div id="content2_td_content"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="content3_td_title"></div>
                            </td>
                            <td>
                                <div id="content3_td_content" style="width: 98%; height: 90%"></div>
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
    let CREATE_MODE = 0;
    const CREATE_INPUT = 1;     // 입고
    const CREATE_OUTPUT = 2;    // 출고
    const CREATE_TRANSFER = 3;    // 창고이동

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
    });

    const go_page = (pageno) => {
        $("#pagingCurrentPageNo").val(pageno);
        $("#pagingForm")[0].submit();
    }

    // 검색조건 초기화
    function doInit() {
        $("#outwySelect option:eq(0)").prop("selected", true);
        $("#searchWord").val("");
        $("#startDt").val("");
        $("#endDt").val("");
        location.reload();
    }

    // 입출고 팝업을 닫기
    function hideStockPopup(){
        console.log('hideStockPopup');

        $("#layer_popup").hide();
    }

    function excelSalesDownload() {
        console.log('excelSalesDownload');

        $('#pagingForm').attr("action", "/inoutMng/api/excelSalesDownload");
        $('#pagingForm').submit();

        // @@@ 엑셀다운로드에서 ajax를 쓰려면 특수처리가 필요하다???
        // let params = $('#pagingForm').serializeObject();
        // console.log(params);
        // $.ajax({
        //     dataType : "html",
        //     type : "POST",
        //     url : "/inoutMng/api/excelSalesDownload",
        //     contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        //     data : params,
        //     success : function(data) {
        //         console.log('excelSalesDownload success');
        //         console.log(data);
        //         alert("엑셀저장성공");
        //         location.reload();
        //     },
        //     error: function(data, status, err) {
        //         console.log('error forward : ' + data);
        //     }
        // });
    }
</script>