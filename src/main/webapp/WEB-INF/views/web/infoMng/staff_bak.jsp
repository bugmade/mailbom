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
    <title>직원정보관리</title>
    <link rel="shortcut icon" href="/resources/img/favicon.ico" type="image/png">
</head>
<body>
<div class="wrap">
    <div class="container">
        <%@ include file="../../layout/header.jsp" %>
        <div class="container_inner">
            <div class="contwrap">
                <h3 class="cont_tit">
                    직원정보관리
                </h3>
                <div class="main_wrap">
                    <button class="register_btn_j" onclick="showStaffPopup('CREATE')">직원등록</button>
                    <table class="gray_table half_table">
                        <thead>
                        <tr>
                            <th>아이디</th>
                            <th>직원성명</th>
                            <th>이메일</th>
                            <th>전화번호</th>
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

    <!-- 팝업 -->
    <div class="layer_popup" id="layer_popup" style="display: none;">
        <div class="layer">
            <h3 class="cont_tit" style="text-align: center;">
                <div id="popup_title"></div>
            </h3>
            <!--팝업 컨텐츠 영역-->
            <div class="popup_cont">
                <form method="post" id="frmReg">
                    <input type="hidden" name="login_id" value="${webUtils.getLogin().getAdm().getAdmId()}">
                    <table class="popup_table">
                        <tbody>
                        <tr>
                            <td>아이디 *</td>
                            <td><input type="text" maxlength='32' name="adm_id" id="adm_id" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>비밀번호 *</td>
                            <td><input type="text" maxlength='32' name="adm_pw" id="adm_pw" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>직원성명 *</td>
                            <td><input type="text" maxlength='5' name="adm_nm" id="adm_nm" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>이메일</td>
                            <td><input type="text" maxlength='250' name="email" id="email" style="width: 98%;"></td>
                        </tr>
                        <tr>
                            <td>전화번호 *</td>
                            <td><input type="text" maxlength='13' name="tel_no" id="tel_no" style="width: 98%;"></td>
                        </tr>
                        </tbody>
                    </table>
                    <%--                    <input type="submit" value="등록하기">--%>
                </form>
            </div>
            <!--팝업 버튼 영역-->
            <button class="save_btn_j" onclick="createStaff()">저장</button>
            <button class="close_btn_j" onclick="hideStaffPopup()">닫기</button>
<%--            <button class="btn_close" onclick="hideStaffPopup()">CLOSE</button>--%>
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
        readStaffList();
    })

    // 제품리스트 가져오기 - 전통적인 방법
    readStaffList = function() {
        $.ajax({
            dataType : "html",
            type : "GET",
            url : "/infoMng/api/readStaffList",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : {"test" : "1" },   // query string
            success : function(data) {
                console.log('getStaffListClassic success');
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
                    html += '<td>'+obj.admId+'</td>';
                    html += '<td>'+obj.admNm+'</td>';
                    html += '<td>'+obj.email+'</td>';
                    html += '<td>'+obj.telNo+'</td>';
                    html += '<td>'+obj.regDt.substring(0, 10)+'</td>';
                    if(obj.admId === 'admin') {
                        html += '<td>' +
                            //'<input type="button" onclick="updateModal(\'' + obj.admId + '\')" value="수정">' +
                            '<button class="update_btn_j" onclick="updateModal(\''+obj.admId+'\')">수정</button>' +
                            '</td>';
                    } else {
                        html += '<td>' +
                            //'<input type="button" onclick="deleteStaff(\'' + obj.admId + '\')" value="삭제">' +
                            '<button class="delete_btn_j" onclick="deleteStaff(\''+obj.admId+'\')">삭제</button>' +
                            //'<input type="button" onclick="updateModal(\'' + obj.admId + '\')" value="수정" style="margin-left: 5px">' +
                            '<button class="update_btn_j" onclick="updateModal(\''+obj.admId+'\')">수정</button>' +
                            '</td>';
                    }
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

    // 서버에 직원등록하기
    function createStaff(){
        console.log('createStaff');
        if($('#adm_id').val() =="") {
            alert("아이디를 입력하세요");
            $('#adm_id').focus();
            return;
        }
        if($('#adm_pw').val() =="") {
            alert("비밀번호를 입력하세요");
            $('#adm_pw').focus();
            return;
        }
        if($('#adm_nm').val() =="") {
            alert("직원성명을 입력하세요");
            $('#adm_nm').focus();
            return;
        }
        if($('#tel_no').val() =="") {
            alert("전화번호를 입력하세요");
            $('#tel_no').focus();
            return;
        }

        let params = $('#frmReg').serializeObject();
        console.log(params);

        if(create_or_update === DO_CREATE) {
            $.ajax({
                dataType: "html",
                type: "POST",
                url: "/infoMng/api/createStaff",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: params,
                success: function (data) {
                    console.log('createStaff success');
                    console.log(data);
                    if(data === '1')
                        alert("저장되었습니다");
                    else
                        alert("이미 존재하는 아이디입니다");
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
                url: "/infoMng/api/updateStaff",
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: params,
                success: function (data) {
                    console.log('updateStaff success');
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

    // 직원정보 삭제하기
    function deleteStaff(adm_id) {
        console.log('deleteStaff: %s', adm_id);
        if(!confirm(adm_id+" 삭제할까요?")) {
            return;
        }
        let params = {adm_id: adm_id};

        $.ajax({
            dataType : "html",
            type : "POST",
            url : "/infoMng/api/deleteStaff",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('deleteStaff success');
                console.log(data);
                alert("삭제되었습니다");
                location.reload();
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    // 직원정보수정화면 띄우기
    function updateModal(adm_id) {
        console.log('updataModal: %s', adm_id);

        let params = {adm_id: adm_id};

        $.ajax({
            dataType : "html",
            type : "GET",
            url : "/infoMng/api/readStaffDetail",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data : params,
            success : function(data) {
                console.log('readStaffDetail success');
                console.log(data);
                data = JSON.parse(data);

                $('#adm_id').val(data.admId);
                $("#adm_id").attr("readonly",true);
                $('#adm_pw').val(data.admPw);
                $("#adm_pw").attr("readonly",true);
                $('#adm_nm').val(data.admNm);
                $('#email').val(data.email);
                $('#tel_no').val(data.telNo);
                $('#reg_dt').val(data.regDt);
                showStaffPopup('UPDATE');
            },
            error: function(data, status, err) {
                console.log('error forward : ' + data);
            }
        });
    }

    // 직원등록 팝업을 보여주기
    function showStaffPopup(mode){
        console.log('showStaffPopup: '+mode);

        if(mode === 'CREATE') {
            $('#adm_id').val("");
            $("#adm_id").removeAttr("readonly");
            $('#adm_pw').val("");
            $("#adm_pw").removeAttr("readonly");
            $('#adm_nm').val("");
            $('#email').val("");
            $('#tel_no').val("");
            $('#reg_dt').val("");
        }
        $('#layer_popup').show();
        if(mode === 'CREATE') {
            $('#adm_id').focus();
            $('#popup_title').html("직원등록");
            create_or_update = DO_CREATE;
        } else {
            $('#popup_title').html("직원수정");
            create_or_update = DO_UPDATE;
        }
    }

    //직원등록 팝업을 닫기
    function hideStaffPopup(){
        console.log('hideStaffPopup');

        $("#layer_popup").hide();
    }
</script>