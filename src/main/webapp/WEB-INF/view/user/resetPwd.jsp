<jsp:include page="../inc/head.jsp"></jsp:include>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
      <div class="modal-content">
         <div class="modal-body">
            <form id="form" class="form-horizontal" >
				<div class="form-group">
					<span class="col-sm-3 control-label no-padding-right">手机号：</span> 
					<div class="col-sm-9">
						<input type="text" id="mobile"  name="mobile" readonly="readonly" value="${user.mobile }" class="col-xs-10 col-sm-5" />
						<c:if test="${empty  user.mobile}">
							<span style="color:red;">请先绑定手机号</span>
						</c:if>
						<c:if test="${!empty  user.mobile}">
							<a href="javascript:void(0)" onclick="sendVerifyCode('${user.mobile}')">发送短信码</a>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<span class="col-sm-3 control-label no-padding-right">新密码：</span> 
					<div class="col-sm-9">
						<input type="password"  name="newPwd" value="" class="col-xs-10 col-sm-5" />
					</div>
				</div>
				<div class="form-group">
					<span class="col-sm-3 control-label no-padding-right">验证码：</span> 
					<div class="col-sm-9">
						<input type="text"  name="verifyCode" value="" class="col-xs-10 col-sm-5" />
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
        url: '${ServiceName}' + "/user/doResetPwd",
        data: data,
        dataType:'json'
    }).done(function (data) {
        if (data.code == -1) {
            layer.msg(data.desc);
        } else {
        	layer.msg('保存成功');
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