<%@ include file="../../../common/taglibs.jsp" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script>
    $(function () {
        <c:if test="${not empty errMsg}">
        let msg = '${errMsg}';
        alertPopupOpen(msg, null, "back");
        </c:if>
    });
</script>
