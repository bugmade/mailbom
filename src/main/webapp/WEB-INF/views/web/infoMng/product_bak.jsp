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
                <div class="main_wrap">
                    <button class="register_btn_j" onclick="showProductPopup('CREATE')">제품등록</button>
                    <table class="gray_table half_table">
                        <thead>
                        <tr>
                            <th style="text-align: center;">제품코드</th>
                            <th style="text-align: center;">제품명</th>
                            <th style="text-align: center;">제품설명</th>
                            <th style="text-align: center;">본사창고재고(HQ)</th>
                            <th style="text-align: center;">위탁창고1재고(FIRST)</th>
<%--                            <th>등록일</th>--%>
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
                    <input type="hidden" name="login_id" value="${webUtils.getLogin().getAdm().getAdmId()}">
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
        readProductList();
    })

    // 제품리스트 가져오기 - 전통적인 방법
    readProductList = function() {
        $.ajax({
            dataType : "html",
            type : "GET",
            url : "/infoMng/api/readProductList",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : {"test" : "1" },   // query string
            success : function(data) {
                console.log('getProductListClassic success');
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

                // @@@ 전통적인 방법
                // for (let i = 0; i < data.length; i++) {
                //     console.log('id:', data[i].addr, 'name:', data[i].num);
                //     html += '<tr>';
                //     html += '<td>'+data[i].addr+'</td>';
                //     html += '<td>'+data[i].num+'</td>';
                //     html += '<td>'+data[i].pw+'</td>';
                //     html += '<td>'+data[i].tel+'</td>';
                //     html += '</tr>';
                // }

                data.forEach(function(obj, idx) {
                    console.log(idx, obj.proNo, obj.proCd);
                    html += '<tr>';
                    html += '<td style="text-align: center;">'+obj.proCd+'</td>';
                    html += '<td style="text-align: center;">'+obj.proNm+'</td>';
                    html += '<td style="text-align: center;">'+obj.proDt+'</td>';
                    html += '<td style="text-align: center;">'+obj.hqStorage+'</td>';
                    html += '<td style="text-align: center;">'+obj.firstStorage+'</td>';
                    // html += '<td>'+obj.regDt+'</td>';
                    html += '<td>' +
                            '<button class="delete_btn_j" onclick="deleteProduct(\''+obj.proCd+'\')">삭제</button>' +
                            '<button class="update_btn_j" onclick="updateModal(\''+obj.proCd+'\')">수정</button>' +
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
        let params = {pro_cd: pro_cd};

        $.ajax({
            dataType : "html",
            type : "POST",
            url : "/infoMng/api/deleteProduct",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('deleteProduct success');
                console.log(data);
                alert("삭제되었습니다");
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