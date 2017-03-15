<jsp:include page="../inc/head.jsp"></jsp:include>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
      <div class="modal-content">
         <div class="modal-body">
            <form id="form" class="form-horizontal" >
				<div class="form-group">
					<span class="col-sm-3 control-label no-padding-right">手机号码：</span> 
					<div class="col-sm-9">
						<input type="text" id="mobile"  name="mobile" value="${user.mobile }" class="col-xs-10 col-sm-5" />
						<a href="javascript:void(0)" onclick="sendVerifyCode('${user.mobile}')">发送短信码</a>
					</div>
				</div>
				<div class="form-group">
					<span class="col-sm-3 control-label no-padding-right">验证码：</span> 
					<div class="col-sm-9">
						<input type="text"  name="verifyCode" class="col-xs-10 col-sm-5" />
					</div>
				</div>
			</form>
         </div>
         <div class="modal-footer">
            <button type="button" id="item_submit" class="btn btn-primary">
               	保存
            </button>
         </div>
      </div>

<script type="text/javascript">
//提交
$("#item_submit").on("click", function (e) {
    //check form data
    var data = $('#form').serialize();
    $.ajax({
        type: "POST",
        url: '${ServiceName}' + "/user/doBindMobile",
        data: data,
        dataType:'json'
    }).done(function (data) {
        if (data.code == -1) {
            layer.msg(data.desc);
        } else {
        	layer.msg('绑定成功');
        	closeWindowAndRefreshParent();
        }
    });
});
function sendVerifyCode(){
	var mobile = $('#mobile').val();
	$.ajax({
        type: "POST",
        url: '${ServiceName}' + "/user/sendVerifyCode",
        data: {mobile:mobile},
        dataType:'json'
    }).done(function (data) {
        if (data.code == -1) {
            layer.msg(data.desc);
        } else {
        	layer.msg('验证码已发送');
        }
    });
}
</script>