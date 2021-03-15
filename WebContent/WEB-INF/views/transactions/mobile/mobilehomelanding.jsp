<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<spring:url value="/resources/css/mobilestyle.css" var="mobilestyleCSS" />
<link href="${mobilestyleCSS}" rel="stylesheet" />
<%--  <div class="row">
  <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 
  </a>
 	<div class="wrapper-page">
        <div class="panel panel-color panel-primary panel-pages">
        	 <h3 class="text-center logFormTexts">Welcome To Fasset</h3>        
            <div class="panel-body">
		<c:if test="${error != null}">
			<div class="logAlert">
				<strong><spring:message code="warning" /></strong> ${error}
			</div>
		</c:if>
		       <form class="form-horizontal m-t-20" id="loginForm" name="loginForm" action="login" method="POST" autocomplete="off">
                    <div class="form-group">
		                 <div class="col-xs-12">
		                    <c:choose>
	                            <c:when test="${userval != null}">
	                                 <input class="form-control" name="userName" placeholder="Email Address" value="${userval}" type="text" autocomplete="off">
	                             </c:when>
	                            <c:otherwise>
	                                 <input class="form-control" name="userName" placeholder="Email Address" type="text">
	                             </c:otherwise>
	                          </c:choose>
		                    <c:if test="${nologin != null}">
								<span class="logError">${nologin}</span>
							</c:if>
		                                         
					   </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12">
                        <div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                               <c:choose>
	                                 <c:when test="${passwordval != null}">
	                                 	<input class="form-control" name="password" id="password" placeholder="Password" value="${passwordval}" type="password" autocomplete="off">
	                                </c:when>
	                                <c:otherwise>
	                                	 <input class="form-control" name="password" id="password" placeholder="Password" type="password">
	                                 </c:otherwise>
                                </c:choose>
                                </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass();" onmouseout="mouseoutPass();" ></i>
								</span>
						</div>
						
		                    <c:if test="${nopasswd != null}">
								<span class="logError">${nopasswd}</span>
							</c:if>                        
						</div>
                    </div>
                   <!--  <div class="form-group">
                        <div class="col-xs-12">
                            <div class="checkbox checkbox-primary">
                                <input id="checkbox-signup" type="checkbox">
                                <label for="checkbox-signup"> Remember me </label>
                            </div>
                        </div>
                    </div> -->
                    <div class="form-group text-center m-t-40">
                        <div class="col-xs-12">
                           <button class="btn btn-primary btn-block btn-lg waves-effect waves-light logBt" type="submit"  form="loginForm" value="Submit" >Login</button>
                        </div>
                    </div>
                    <div class="form-group m-t-30 m-b-0">
                        <div class="col-sm-7"> <a href="forgotPasswd" class="text-muted"><i class="fa fa-lock m-r-5"></i> Forgot Password?</a></div>
                        <div class="col-sm-5 text-right"> <a href="#" class="text-muted" onclick = "signup()" >Sign Up</a></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
 --%>

<style>

#mobileapp{position: absolute;top: 25%;}
#mobileapp .panel{background-color: rgba(255, 255, 255, 0.3);}
#mobileapp .input-group .input-group-addon{background-color:#e81f18;color:#fff;border: 1px solid #e81f18;border-radius: 4px 0 0 4px;font-size:16px;}
#mobileapp .input-group .input-group-addon .fa-lock{padding: 0 1.5px;}
#mobileapp .input-group .form-control{color:#333;border:1px solid #ccc;	border-left:0;background:#fff;height: 44px;}
::placeholder {color: #737489 !important;}
#mobileapp .logBt{background-image: linear-gradient(#e81f18, #ff6708);border:0 !important;color:#fff;border-radius: 4px;height: 44px;}
#mobileapp .panel{margin:20px;}
</style>
<!-- MOBILE UI FOR LOGIN -->
<div class="bg-mobile">
<div class="row" id="mobileapp">
 	<div class="">
        <div class="panel panel-color panel-primary panel-pages">        
            <div class="panel-body">
		<c:if test="${error != null}">
			<div class="logAlert">
				<strong><spring:message code="warning" /></strong> ${error}
			</div>
		</c:if>
		       <form class="form-horizontal m-t-20" id="loginForm" name="loginForm" action="login" method="POST" autocomplete="off">
                    <div class="form-group">
		                 <div class="col-xs-12">
		                    <c:choose>
	                            <c:when test="${userval != null}">
	                            <div class="input-group">
					                <span class="input-group-addon"><i class="fa fa-user" aria-hidden="true"></i></span>
					                <input type="text" class="form-control" placeholder="Username" name="userName" value="${userval}" autocomplete="off">
					            </div>
	                                 <%-- <input class="form-control" name="userName" placeholder="Email Address" value="${userval}" type="text" autocomplete="off"> --%>
	                             </c:when>
	                            <c:otherwise>
	                            <div class="input-group">
					                <span class="input-group-addon"><i class="fa fa-user" aria-hidden="true"></i></span>
					                <input type="text" class="form-control" placeholder="Username" name="userName" autocomplete="off">
					            </div>
	                                 <!-- <input class="form-control" name="userName" placeholder="Email Address" type="text"> -->
	                             </c:otherwise>
	                          </c:choose>
		                    <c:if test="${nologin != null}">
								<span class="logError">${nologin}</span>
							</c:if>
		                                         
					   </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12">
                              <c:choose>
                                 <c:when test="${passwordval != null}">
                                 <div class="input-group">
					                <span class="input-group-addon"><i class="fa fa-lock" aria-hidden="true"></i></span>
					                <input class="form-control" name="password" id="password" placeholder="Password" value="${passwordval}" type="password" autocomplete="off">
					            </div>
                                 	<%-- <input class="form-control" name="password" id="password" placeholder="Password" value="${passwordval}" type="password" autocomplete="off"> --%>
                                </c:when>
                                <c:otherwise>
                                <div class="input-group">
					                <span class="input-group-addon"><i class="fa fa-lock" aria-hidden="true"></i></span>
					                <input class="form-control" name="password" id="password" placeholder="Password" type="password" autocomplete="off">
					            </div>
                                	 <!-- <input class="form-control" name="password" id="password" placeholder="Password" type="password"> -->
                                 </c:otherwise>
                               </c:choose>
		                    <c:if test="${nopasswd != null}">
								<span class="logError">${nopasswd}</span>
							</c:if>                        
						</div>
                    </div>
                    <input id="is_mobile_app"  hidden = "hidden"/>
                    <div class="form-group text-center m-t-40">
                        <div class="col-xs-12">
                           <button class="btn btn-block waves-effect waves-light logBt" type="submit"  form="loginForm" value="Submit" >SIGN IN</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="clearfix"></div>
</div>

<script>
	function signup(){
		window.location.assign("<c:url value = 'signedUp'/>");
	}
	function mouseoverPass(obj) {
		  var obj = document.getElementById('password');
		  obj.type = "text";
		}
		function mouseoutPass(obj) {
		  var obj = document.getElementById('password');
		  obj.type = "password";
		}
</script>
<%--<%@include file="/WEB-INF/includes/footerLogin.jsp"%>  --%>
