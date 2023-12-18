/*
input 태그에 숫자만 입력되도록 하는 function
 */
function onlyNumber(obj) {
    obj.value = obj.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
}

/*
입력받은 문자열에 대한 email validation(https://suyou.tistory.com/150)을 해서 맞는 형식일 경우 true를 return 한다
 */
function isEmail(email) {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    return regExp.test(email); // 형식에 맞는 경우 true 리턴
}

function onlyPhoneOrTel(obj) {
    obj.value = obj.value.replace(/[^0-9-.]/g, '').replace(/(\..*)\./g, '$1');
}
/*
입력받은 문자열에 대한 전화번호(휴대폰포함) validation을 해서 맞는 형식일 경우 true를 return 한다
 */
function isTelno(telno) {
    var regExp = /^(010|011|016|018|019|02|031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064)\d{7,8}$/i;
    return regExp.test(telno);
}

/*
입력받은 문자열에 대한 전화번호(휴대폰포함) validation을 해서 맞는 형식일 경우 true를 return 한다
 */
function isURL(string) {
    var res = string.match(/(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g);
    return (res !== null);
}

/*
    Password 유효성 검사(영문/숫자/특수문자를 포함하여 8자리 이상 16자리 이하)
 */
function validatePw1(password) {
    var pwPattern = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^*+=-_]).{8,16}$/;
    return pwPattern.test(password);
}

/*
    Password 유효성 검사(영문/숫자를 포함하여 8자리 이상 16자리 이하)
 */
function validatePw2(password) {
    var pwPattern = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,16}$/;
    return pwPattern.test(password);
}

/*
입력받은 문자열에 대한 사업자등록번호를 체크(https://kimsg.tistory.com/224) 해서 맞는 형식일 경우 true를 return 한다
 1061712345
 */
function checkBizID(bizID) {
    // bizID는 숫자만 10자리로 해서 문자열로 넘긴다.
    var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1);
    var i, chkSum=0, c2, remander;
    var result = true;
    bizID = bizID.replace(/-/gi,'');

    if(bizID.length !== 10) {
        result = false;
    } else {
        for (i=0; i<=7; i++) chkSum += checkID[i] * bizID.charAt(i);
        c2 = "0" + (checkID[8] * bizID.charAt(8));
        c2 = c2.substring(c2.length - 2, c2.length);
        chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));
        remander = (10 - (chkSum % 10)) % 10 ;

        if (Math.floor(bizID.charAt(9)) == remander) result = true ; // OK!
    }

    return result;
}

/*
input 태그에 한글이 입력되지 않도록 하는 function
 */
function notKr(obj) {
    obj.value = obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
}

/*
Ajax를 이용한 파일 다운로드(http://yoonbumtae.com/?p=1170#comment-112 참고)
이것을 이용해서 파일 다운로드를 구현한 소스는 jsp는 WEB-INF/jsp/humandata/humanDataView.jsp에 구현되어 있고
관련 Java 코드는 hbp.cms.admin.humandata.controller.HumanDataController 클래스의 figureFileDownload 메소드에 구현되어 있다
이 두 코드를 참조하면 된다
POST 방식으로만 처리가 가능하기 때문에 Spring Controller 에서 Post 방식으로 파라미터를 받아야 한다(@PostMapping)
 */
function fileDownloadByAjax(formData, url) {
    var xhr = new XMLHttpRequest();
    var showAlert = false;
    xhr.onreadystatechange = function(){
        // 성공, 실패 여부 떠나서 통신이 complete 되면 로딩바 숨김
        if(this.readyState == 4){
            hideLoadingBar();
        }
        if (this.readyState == 4 && this.status == 200){

            var filename = "";
            var disposition = xhr.getResponseHeader('Content-Disposition');
            if (disposition && disposition.indexOf('attachment') !== -1) {
                var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                var matches = filenameRegex.exec(disposition);
                if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                filename = decodeURI(filename);     // 서버에서 UTF-8 인코딩을 한번 진행하고 온것이기 때문에 decodeURI 메소드를 통해 decode를 해야 한글파일명이 깨지지 않는다
            }

            //this.response is what you're looking for
            // console.log(this.response, typeof this.response);
            var blob = new Blob([this.response], {type: "application/octet-stream"});
            if(navigator.msSaveBlob) {      // 사용하는 브라우저가 IE10이나 IE11 이면 다운로드 폴더에 다운로드 받게 해준다
                return navigator.msSaveBlob(blob, filename);
            } else {
                if(!filename) {
                    return;
                }
                var link = document.createElement("a");
                link.href = window.URL.createObjectURL(blob);
                link.download = filename;
                link.click();
            }
        } else if(this.status == 404 && showAlert === false) { // 이 부분을 2번 방문하기 때문에 alert를 한번 보여줬다는 상황을 설정할 필요가 있다.
            alertPopupOpen("다운로드파일이 존재하지 않습니다. 확인해주세요", null, "none");
            showAlert = true;
        } else if(this.status == 401) {
            let callback = () => {
                location.href = "/login";
            }
            alertPopupOpen("세션이 만료되었습니다.", null, "callback", callback);
        }
    }

    xhr.open('POST', url);
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.responseType = 'blob'; // !!필수!!
    xhr.send(formData); // 파라미터 설정하는 부분이며 formData 설정 부분은 생략
}

/*
상세 메뉴 네비게이션 추가
*/
function makeDetailLocation() {

    var spanIcon = "<span class=\"locationIcon\"></span>";
    var nowLocation = "<a href=\"#\" class=\"active\"></a>";
    $(".locationWrap").children("a").removeClass("active");
    $(".locationWrap").append(spanIcon);
    $(".locationWrap").append(nowLocation);
    var detailTitle = $(".pageTit").text();
    $(".locationWrap").children("a").last().text(detailTitle);

}

/*
 bytes To Size
*/
function bytesToSize(bytes) {
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (bytes == 0) return '0 Byte';
    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
    return Math.round(bytes / Math.pow(1024, i), 2) + " " + sizes[i];
}

/*
 Alert Popup Open Focus
*/

function alertPopupOpen(msg, selectorObject, type, callback) {
    $("#alertPopupMsg").html(msg);
    $("#alertPopupAnchor").data("input",selectorObject);
    $("#alertPopupAnchor").data("type", type);
    if(callback){
        $("#alertPopupAnchor").data("callback", callback);
    }
    $('#openAlertPopup').show();
    $('body').css({
        'overflow':'hidden',
    });
}

/*
 Alert Popup Close Focus
 type 별 구분
 none: 닫기
 reload: 새로고침
 focus: input창 포커싱
 back: history.back()
 callback: callback 함수
*/
function alertPopupClose() {

    $('#openAlertPopup').hide();
    $('body').css({
        'overflow':'auto',
    });

    var type = $("#alertPopupAnchor").data("type");

    if(type === undefined) {
        type = "focus";
    }
    if(type === "focus") {
        var selectorObject = $("#alertPopupAnchor").data("input");
        $(selectorObject).focus();
    } else if(type === "reload") {
        location.reload();
    } else if(type === "back") {
        history.back();
    } else if(type === "callback") {
        let callback = $("#alertPopupAnchor").data("callback");
        callback();
    } else if(type === "none") {
        return;
    }
}

function confirmPopupEnterEvent(e) {
    const ok = document.getElementById("confirmOK");
}

function confirmPopupOpen(msg, confirmOKText, callback, confirmCancelText) {
    $("#confirmPopupMsg").html(msg);
    $("#confirmOK").text(confirmOKText);

    if(confirmCancelText === undefined) {
        $("#confirmCancel").text("취소");
    } else {
        $("#confirmCancel").text(confirmCancelText);
    }

    $("#confirmOK").data("callback", callback);
    $("#openConfirmPopup").show();
    $('body').css({
        'overflow':'hidden',
    });
}

function confirmPopupResult(result) {
    $('#openConfirmPopup').hide();
    $('body').css({
        'overflow':'auto',
    });

    var callback = $("#confirmOK").data("callback");
    if(result) {
        callback(true);
    } else {
        callback(false);
    }
}

// 한글 바이트 수 계산
function getByte(str) {
    var resultSize = 0;
    if (str == null) {
        return 0;
    }

    for (var i = 0; i < str.length; i++) {
        var c = escape(str.charAt(i));
        if (c.length == 1) {
            resultSize++;
        } else if (c.indexOf("%u") != -1) {
            resultSize += 2;
        } else {
            resultSize++;
        }
    }
    return resultSize;
}

function substringByte(msg, byte) {
    if(getByte(msg) > byte) {
        var len = msg.length;
        while(getByte(msg) > byte){
            len--;
            msg = msg.substring(0, len);
        }
    }
    return msg;
}

/**
 *  로딩바 화면 중앙에 띄우는 함수
 */
function showLoadingBar(loadingTxt){
    $("#loadingTxt").html(loadingTxt);
    $("#loadingBarPopup").show();
}

/**
 *  로딩바 숨기는 함수
 */
function hideLoadingBar(){
    $("#loadingBarPopup").hide();
    $("#loadingTxt").html("");
}

//파일 사이즈 계산
function fileSizePrint (data){
    var size = "";
    if (data < 1024) size = data + " B";
    else if (data < 1024 * 1024) size = parseInt (data / 1024) + " KB";
    else if (data < 1024 * 1024 * 1024) size = parseInt (data / (1024 * 1024)) + " M";
    else size = parseInt (data / (1024 * 1024 * 1024)) + " G";
    return size;
}

/*
 * 메뉴 구조에 없는 1Depth 네비바 경로 생성
 */
function writeNaviBar(menuName, aLink) {
    var bar = '<li><a href="' + aLink + '">' + menuName + '</a></li>';
    $(".sub_path").empty();
    $(".sub_path").append(bar);
}

/**
 * 문자열 치환 함수
 * @returns {String}
 */
String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replaceAll("{" + arg + "}", arguments[arg]);
    }
    return formatted;
}

/**
 * text 값으로 태그 찾기 위한 jquery 함수
 * $.fn 네임스페이스의 메소드는 selector 와 함께 쓰이는 메소드
 * ex) $('div').findByEqualText('null')
 * @param text
 * @returns {*}
 */
$.fn.findByEqualText = function (text) {
    return this.filter(function () {
        return this.innerHTML == text;
    });
};


/**
 * 숫자 받으면 000 단위로 콤마 찍어주는 함수
 * @param num
 * @returns {str}
 */
function setComma(num){
    var len, point, str;
    num = num + "";
    point = num.length % 3;
    len = num.length;

    str = num.substring(0, point);
    while(point < len) {
        if(str != "") {
            str += ",";
        }
        str += num.substring(point, point + 3);
        point += 3;
    }
    return str;
}

/**
 * 시작일자 문자열이 종료일자 문자열보다 이후에 있는지 확인하는 함수
 * format 은 날짜 문자열 형태(moment.js 의 포맷을 따름) ex) YYYY-MM-DD HH:mm
 * @param sdate {string}
 * @param edate {string}
 * @param format {string}
 * @returns {boolean}
 */
function isStartDateAfterEndDate(sdate, edate, format){
    return moment(sdate, format).isAfter(moment(edate, format));
}


function validateYouTubeUrl(url) {
    if (url) {
        let regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=|\?v=)([^#\&\?]*).*/;
        let match = url.match(regExp);
        return match && match[2].length == 11;
    } else {
        return false;
    }
}

/**
 * 파일명 확장자에 맞게 아이콘 클래스명 return 하는 함수
 * @param fileNm
 * @returns {string}
 */
function getExtClassNm(fileNm) {
    const ext = fileNm.substring(fileNm.lastIndexOf('.') + 1);
    if(ext === 'pdf' || ext === 'doc' || ext === 'docx' || ext === 'hwp'){
        return ext;
    } else if(ext === 'jpg' || ext === 'jpeg' || ext === 'gif' || ext === 'png') {
        return 'img'
    } else {
        return 'etc';
    }
}

/**
 * 최종 업데이트 상태 조회 팝업
 * @param msg
 */
function showJobStatInfo(time, worker){
    alertPopupOpen(`최종 업데이트 시간 : ${time}<br/>최종 작업자 : ${worker}`, null, 'none');
}

/**
 * 오류 메시지 조회 팝업
 * 백그라운드 연산 오류 시 메시지 보여주는 함수
 * @param msg
 */
function showErrMsg(msg){
    if(msg && msg.length > 100){
        msg = msg.substring(0, 100);
        msg = msg + '...';
    }
    alertPopupOpen(msg, null, 'none');
}

/**
 * 검색 폼의 데이터를 유지한 채로 목록으로 이동
 * 이전 링크가 유효하면 뒤로가기 처리, 아니면 파라미터로 주어진 목록 링크로 이동
 * @param listLink
 */
function goListWithQuery(listLink){
    const preLink = document.referrer
    if(preLink === "") location.href = listLink; //이전 링크가 없으면 (외부 링크거나 url 새로 입력해 들어온 경우) 목록 링크로 이동
    else if(!preLink.includes(listLink)) location.href = listLink; // (링크를 포함하지 않는 경우) 목록 링크로 이동
    else window.location.replace(preLink); // 모든 조건이 아니면 뒤로가기 처리
}

/**
 * url에서 특정 파라미터 추출
 * @param key
 * @param url
 **/
function extractUrlValue(key, url)
{
    if (typeof(url) === 'undefined')
        url = window.location.href;
    let match = url.match('[?&]' + key + '=([^&]+)');
    return match ? match[1] : null;
}

/**
 * url에서 특정 파라미터 제거
 * @param key
 * @param sourceURL
 **/
function removeURLParam(key, sourceURL) {
    var rtn = sourceURL.split("?")[0],
        param,
        params_arr = [],
        queryString = (sourceURL.indexOf("?") !== -1) ? sourceURL.split("?")[1] : "";
    if (queryString !== "") {
        params_arr = queryString.split("&");
        for (var i = params_arr.length - 1; i >= 0; i -= 1) {
            param = params_arr[i].split("=")[0];
            if (param === key) {
                params_arr.splice(i, 1);
            }
        }
        if (params_arr.length) rtn = rtn + "?" + params_arr.join("&");
    }
    return rtn;
}

/**
 * url에 파라미터 추가, 이미 존재할 경우 변경한다.
 * @param name
 * @param value
 * @param sourceURL
 **/
function appendURLParam(name, value, sourceURL) {

    sourceURL = removeURLParam(name, sourceURL);

    let href = sourceURL;
    let regex = new RegExp("[&\\?]" + name + "=");
    if(regex.test(href))
    {
        regex = new RegExp("([&\\?])" + name + "=\\d+");
        href = href.replace(regex, "$1" + name + "=" + value);
    }
    else
    {
        if(href.indexOf("?") > -1)
            href = href + "&" + name + "=" + value;
        else
            href = href + "?" + name + "=" + value;
    }

    return href;
}

function ajaxError(xhr) {
    if(xhr.status === 401) {
        let callback = () => {
            location.href = "/login";
        }
        alertPopupOpen("세션이 만료되었습니다.", null, "callback", callback);
    } else {
        alertPopupOpen("오류가 발생했습니다.<br/>나중에 다시 시도해 주세요.", null, "none");
    }
}


const zeroPad = (num, places) => String(num).padStart(places, '0')