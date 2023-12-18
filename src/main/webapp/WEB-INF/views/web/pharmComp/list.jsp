<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<head>
    <title>제약사 관리</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    제약사 관리
                </h3>
                <div class="white_box bd_gr">
                    <form:form id="searchForm" name="searchForm" method="get" action="/pharmComp/list" modelAttribute="pharmCompListReq">
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
                                        <label for="searchWord">검색어</label>
                                    </th>
                                    <td colspan="3" class="fz0">
                                        <form:select path="searchType" cssClass="basic_formtype select_form select_sch">
                                            <form:option value="ALL">전체</form:option>
                                            <form:option value="CD">제약사 코드</form:option>
                                            <form:option value="NM">제약사명</form:option>
                                        </form:select>
                                        <form:input type="text" path="searchWord" value="" cssClass="basic_formtype search_form" placeholder="검색어를 입력하세요"/>
                                        <button id="searchBtn" class="search_btn">
                                            검색
                                            <span class="ir_so">검색버튼</span>
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form:form>
                </div>
                <!-- //white_box -->

                <c:set value="${result.getTotalElements()}" var="totalCount"/>
                <div class="clgraph_info clearfix mb20">
                    <h4 class="wb_tit fl_l">
                        총 <span class="col_acc">${totalCount}</span> 건
                    </h4>
                </div>
                <table class="list_table">
                    <colgroup>
                        <col width="8%">
                        <col width="10%">
                        <col width="*">
                        <col width="20%">
                        <col width="5%">
                        <col width="8%">
                        <col width="5%">
                        <col width="10%">
                        <col width="5%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>No.</th>
                        <th>제약사명</th>
                        <th>제약사 코드</th>
                        <th>과세유형</th>
                        <th>주소</th>
                        <th>소액기준액</th>
                        <th>비고</th>
                        <th>작성일</th>
                        <th>작성자</th>
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
                                    <td>${idx-status.index}</td>
                                    <td class="pl20 ta_c ellipsis">
                                        <a class="list_link ellipsis" href="">${row.pharmCompNm}</a>
                                    </td>
                                    <td>${row.pharmCompCd}</td>
                                    <td>${row.bizRegNo}</td>
                                    <td>${row.taxTypeCcdNm}</td>
                                    <td>${row.addr}</td>
                                    <td>${row.addr}</td>
                                    <td>${row.modId}</td>
                                    <td>
                                        <javatime:format value="${row.modDt}" pattern="yyyy-MM-dd"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <!-- //white_box -->

                <div class="pagination_area">
                    <form:form modelAttribute="pharmCompListReq">
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

                    <button class="btn" id="btnRegist">등록</button>
                    <!-- //button -->
                </div><!--//pagination_area-->
            </div>
            <!-- //contwrap -->

            <%-- paging 태그에서 페이지 번호를 클릭했을때 자바스크립트 함수로 실행하게될 form을 작성한다(읽기전용 form임)--%>
            <form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="pharmCompListReq">
                <form:hidden id="pagingSearchType6" path="searchType"/>
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

        $('#btnRegist').on('click', () => {
            location.href = '/pharmComp/registForm';
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
    });

    const go_page = (pageno) => {
        $("#pagingCurrentPageNo").val(pageno);
        $("#pagingForm")[0].submit();
    }
</script>