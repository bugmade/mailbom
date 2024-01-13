<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>

<head>
    <title>판매실적</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    판매실적
                </h3>
                <div class="white_box bd_gr">
                    <form:form id="searchForm" name="searchForm" method="get" action="/stats/statsale" modelAttribute="statsaleListReq">
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
<%--                                        <form:select path="outWy" style="width:170px;" onchange="changeInoutSelect()" cssClass="basic_formtype select_form select_sch">--%>
<%--                                            <form:option value="ALL">전체(출고사유)</form:option>--%>
<%--                                            <form:option value="BTOB">납품</form:option>--%>
<%--                                            <form:option value="RETAIL">소매(택배)</form:option>--%>
<%--                                            <form:option value="GIFT">증정</form:option>--%>
<%--                                            <form:option value="ETC">기타</form:option>--%>
<%--                                        </form:select>--%>
                                        <form:input type="text" path="searchWord" value="" style="width:250px;" cssClass="basic_formtype search_form" placeholder="납품처 검색어"/>
                                        <form:input type="text" path="startDt" cssClass="basic_formtype" style="width:120px; margin-right:10px;" placeholder="검색 시작일" />
                                        <form:input type="text" path="endDt" cssClass="basic_formtype" style="width:120px; margin-right:10px;" placeholder="검색 종료일" />
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
                        <col width="15%">
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
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${totalCount == 0}">
                            <td colspan="13" class="list_none">등록된 내역이 없습니다.</td>
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
                                    </td>
                                    <td>${row.stoNo}</td>
                                    <td>
                                        <button class="delete_btn_j" onclick="deleteSales(${row.salesNo})">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- //white_box -->

                <div class="pagination_area">
                    <form:form modelAttribute="statsaleListReq">
                        <form:select path="pageSize" cssClass="select_form">
                            <form:option value="10">10개 씩보기</form:option>
                            <form:option value="50">50개 씩보기</form:option>
                            <form:option value="100">100개씩 보기</form:option>
                        </form:select>
                    </form:form>
                    <!-- //select -->

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
            <form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="statsaleListReq">
                <form:hidden id="pagingSearchStartDt" path="startDt"/>
                <form:hidden id="pagingSearchEndDt" path="endDt"/>
<%--                <form:hidden id="pagingSearchType1" path="outWy"/>--%>
                <form:hidden id="pagingSearchWord" path="searchWord"/>
                <form:hidden id="pagingCurrentPageNo" path="pageNo"/>
                <form:hidden id="pagingRecordCountPerPage" path="pageSize"/>
            </form:form>
        </div>
        <%@ include file="../../layout/footer.jsp" %>
    </div>
    <!-- //container -->
<%--    @@@ 용도를 모르겠다. <button class="btn_top">--%>
<%--        <span>맨 위로</span>--%>
<%--    </button>--%>
</div>
</body>
<script type="text/javascript">
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
        alert("doInit");
        // $("#outWy option:eq(0)").prop("selected", true);
        $("#searchWord").val("");
        $("#startDt").val("");
        $("#endDt").val("");
        location.reload();
    }
</script>