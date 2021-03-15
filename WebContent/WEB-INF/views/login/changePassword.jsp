<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<c:if test="${errorMsg != null}">
	<div class="errorMsg" id = "errorMsg"> 
		<strong>${errorMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">	
	<h3>Change Password</h3>
</div>
<div class="col-md-12 wideform">	
	<div class="fassetForm">
		<form:form id="changePassForm" action="changePassword" method="post" commandName = "changePass">
			<div class="row">
				<div class="col-md-3 control-label"><label>Old Password<span>*</span></label></div>
				<div class="col-md-9">
						<div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                               		<form:password path="oldPass"  class="form-control logInput" placeholder="Old Password" id="password1" />
                              </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i>
								</span>
						</div>
					<span class="logError"><form:errors path="oldPass" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>New Password<span>*</span></label></div>
				<div class="col-md-9">
						<div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                               		<form:password path="pass"  class="form-control logInput" placeholder="New Password" id="password2" />
                               </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(2);" onmouseout="mouseoutPass(2);" ></i>
								</span>
						</div>	
					<span class="logError"><form:errors path="pass" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Confirm Password<span>*</span></label></div>
				<div class="col-md-9">
				        <div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                               		<form:password path="confPass" class="form-control logInput"   placeholder="Confirm Password" id="password3" />
                              </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(3);" onmouseout="mouseoutPass(3);" ></i>
								</span>
						</div>						
					<span class="logError"><form:errors path="confPass" /></span>
				</div>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	function cancel(){
		window.location.assign('<c:url value = "login"/>');	
	}
	function mouseoverPass(obj) {
		  var obj = document.getElementById('password'+obj);
		  obj.type = "text";
		}
		function mouseoutPass(obj) {
		  var obj = document.getElementById('password'+obj);
		  obj.type = "password";
		}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>