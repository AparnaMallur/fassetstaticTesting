<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<style>
#changePassForm .form-control, #changePassForm .form-control:focus{
       border: 0;
       border-color: transparent;
       border-bottom: 1px solid #ccc;
       min-height: 38px;
       box-shadow:none;
       border-radius: 0;
       background-color: #fff;

     }
#changePassForm  span.input-group-addon, #changePassForm  span.input-group-addon:hover{
 border: 0;
 border-bottom: 1px solid #ccc;
 background-color: #fff;
 box-shadow: none;
 border-radius: 0;
}
#changePassForm  span.input-group-addon i{color:#6f6f6f;    font-size: 20px;}
.breadcrumb h3 {
   margin: 0px;
   display: inline-block;
   text-align: center;
   line-height: 15px;
}
.breadcrumb a{float:left;}
</style>
<c:if test="${errorMsg != null}">
    <div class="errorMsg" id = "errorMsg">
        <strong>${errorMsg}</strong>
    </div>
</c:if>
<div class="breadcrumb text-center">    
    <a href="mobileHomePage"><i class="fa fa-arrow-left" aria-hidden="true"></i> Back</a><h3>Change Password</h3>
</div>
<div class="col-md-12 wideform">    
    <div class="fassetForm">
        <form:form id="changePassForm" action="changePassword" method="post" commandName = "changePass">
            <div class="form-group">
                <div class="input-group ">
                    <form:password path="oldPass"  class="logInput form-control" placeholder="Old Password" id="password1" />
                     <span class="input-group-addon"><i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i></span>
                </div>
                <span class="logError"><form:errors path="oldPass" /></span>
            </div>
            <div class="form-group">
                <div class="input-group password-checker">
                    <form:password path="pass"  class="logInput form-control " placeholder="New Password" id="password2" />
                     <span class="input-group-addon"><i class="fa fa-eye" onmouseover="mouseoverPass(2);" onmouseout="mouseoutPass(2);" ></i></span>
                </div>
                <span class="logError"><form:errors path="pass" /></span>
            </div>
            <div class="form-group">
                <div class="input-group password-checker">
                    <form:password path="confPass" class="logInput form-control "   placeholder="Confirm Password" id="password3" />
                    <span class="input-group-addon"><i class="fa fa-eye" onmouseover="mouseoverPass(3);" onmouseout="mouseoutPass(3);" ></i></span>
                </div>
                   <span class="logError"><form:errors path="confPass" /></span>
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
    	  /* <a class='logo1' href=mobileHomePage>Cancel</a> */
        window.location.assign('<c:url value = "/mobileHomePage"/>');    
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
<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>