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
        height: 450px;
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
    <title>재품입출고관리</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    제품입출고관리
                </h3>
                <div class="main_wrap">
                    <div class="btn_area">
                        <button class="btn_register" style="float: left;" onclick="showStockPopup('CREATE_INPUT')">입고</button>
                        <button class="btn_hide" style="float: right;" onclick="showStockPopup('CREATE_OUTPUT')">출고</button>
                    </div>
                    <table class="gray_table half_table">
                        <thead>
                        <tr>
                            <th>번호</th>
<%--                            <th>제품코드</th>--%>
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
                        <tbody id="dynamicTbody">
                    </table>
                </div>
            </div>
            <!-- //contwrap -->
        </div>
        <%@ include file="../../layout/footer.jsp" %>
    </div>

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
                                    <select name="pro_cd" class="pro_cd" style="width: 100%; height: 90%">
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
                                <td><input type="number" pattern="[0-9]+" name="io_cnt" id="io_cnt" style="width: 100%"></td>
                            </tr>
                            <tr>
                                <td>메모</td>
                                <td><input type="text" maxlength='100' name="memo" id="memo" style="width: 100%"></td>
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
                                    <div id="csm_cd_td_content" style="width: 100%; height: 90%"></div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <!--팝업 버튼 영역-->
            <div class="btn_area">
                <button class="btn_register" style="float: left;" onclick="createStock()">저장</button>
                <button class="btn_hide" style="float: right;" onclick="hideStockPopup()">닫기</button>
            </div>
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
        readStockList();
    })

    // 제품 입출고 리스트 가져오기 - 전통적인 방법
    readStockList = function() {
        $.ajax({
            dataType : "html",
            type : "GET",
            url : "/inoutMng/api/readStockList",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : {"test" : "1" },   // query string
            success : function(data) {
                console.log('readStockList success');
                console.log(data);              // 얘는 string, 서버에서 애초부터 JSON 객체로 반환하는 방법은???
                console.log(typeof data);       //string
                if(typeof data !== 'object') data = JSON.parse(data);        // JSON 문자열을 JavaScript 객체로 변환
                console.log(typeof data);       //object
                // JSON은 JavaScript Object Notation
                // JSON.stringify(): JavaScript 객체를 JSON 문자열로 변환
                console.log(data);
                console.log(data[0]);
                console.log(data[1]);

                let html = '';
                data.forEach(function(obj, idx) {
                    html += '<tr>';
                    html += '<td>'+obj.stoNo+'</td>';
                    // html += '<td>'+obj.proCd+'</td>';
                    html += '<td>'+obj.proNm+'</td>';
                    if(obj.inOut === IO_INPUT)
                        html += '<td>입고</td>';
                    else
                        html += '<td>출고</td>';
                    html += '<td>'+obj.ioCnt+'</td>';
                    if(obj.inOut === IO_OUTPUT) {
                        if (obj.outWy === OUTWY_SALE) {
                            html += '<td>납품</td>';
                            html += '<td>' + obj.csmNm + '</td>';
                        } else if (obj.outWy === OUTWY_GIFT) {
                            html += '<td>증정</td>';
                            html += '<td></td>';
                        } else {
                            html += '<td>기타</td>';
                            html += '<td></td>';
                        }
                    } else {
                        html += '<td></td>';
                        html += '<td></td>';
                    }
                    html += '<td>'+obj.memo+'</td>';
                    html += '<td>'+obj.regId+'</td>';
                    html += '<td>'+obj.regDt.substring(0, 16)+'</td>';
                    html += '<td>' +
                            '<input type="button" onclick="deleteStock('+obj.stoNo+')" value="삭제">' +
                            '</td>';
                    html += '</tr>';
                });

                $("#dynamicTbody").empty();
                $("#dynamicTbody").append(html);
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
            html = '<select name="csm_cd" class="csm_cd" style="width: 100%; height: 100%">';
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