<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
 <div class="col-md-12 col-sm-12 col-xs-12">
    <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 
 </a>
 	<div class="wrapper-page">
        <div class="panel panel-color panel-primary panel-pages">
			<h3 class="text-center logFormTexts">Reset Password</h3>        
            <div class="panel-body">
		       <form:form class="form-horizontal m-t-20" id="forgotPasswd" name="forgotPasswd" action="forgotPasswd" method="POST" commandName="email">
                    <div class="form-group">
		                 <div class="col-xs-12">
		                    <spring:message code="mail_addr" var="lbl_mail_addr" />
							<form:input type="text" path="login" class="form-control" placeholder="Email Address" />
							<span class="logError"><form:errors path="login" /></span>	                                         
					   </div>
                    </div>
                    <div class="form-group text-center m-t-40">
                        <div class="col-xs-12">
                           <button class="btn btn-primary btn-block btn-lg waves-effect waves-light logBt" type="submit" value="Submit" >Reset Password</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 text-right">
	                        <a href="<c:url value = 'login'/>" class="text-muted">
	                        	<i class='fa fa-arrow-left' style='margin-right:3%'></i>Login
	                       	</a>
                       	</div>
                   	</div>  
                </form:form>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
