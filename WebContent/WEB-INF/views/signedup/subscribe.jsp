<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/validations.js" var="valid" />
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<div class="col-md-12 col-sm-12 col-xs-12 ws">
  <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 
  </a>
 	<div class="wrapper-page ">
        <div class="panel panel-color panel-primary panel-pages">
			<h3 class="text-center logFormTexts">Subscribe</h3>        
            <div class="panel-body">
				<c:if test="${error != null}">
					<div class="logAlert">
						<strong><spring:message code="warning" /></strong>
						<spring:message code="P1.error" />
					</div>
				</c:if>
				<form:form  class='form-horizontal m-t-20' id="signupForm" action="signedUp" method="post" commandName="signedUpForm">     
					
					<div class="form-group" id = "quotNo">
						<div class="col-xs-12">			
							<form:input path="companyId" hidden = "hidden"/>									
							<form:input path="signUpType" hidden = "hidden"/>
							<form:input path="quotationNo" class="form-control" placeholder="Quotation Number" />
							<span class="logError"><form:errors path="quotationNo" /></span>                           
						</div>
					</div>
					<div class="form-group" id = "paymentMethod">
						<div class="col-xs-12 " style='text-align:center'>
							<div class="radio radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio1" value="<%=MyAbstractController.PAYMENT_ONLINE%>" name="radioInline" checked="checked"/> 
								<label for="inlineRadio1"> Online Payment </label>
							</div>
							<div class="radio  radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio2" value="<%=MyAbstractController.PAYMENT_RTGS%>" name="radioInline"/> 
								<label for="inlineRadio2"> RTGS/IMPS </label>
							</div>
							<div class="radio  radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio3" value="<%=MyAbstractController.PAYMENT_OFFLINE%>" name="radioInline"/> 
								<label for="inlineRadio3"> Offline </label>
							</div>			                          
				 	  	</div>  
					</div>
					
					<div class="form-group text-center ">
						<div class="col-xs-12">
							<button class="btn btn-primary btn-block btn-lg waves-effect waves-light logBt" type="submit"  form="signupForm" value="Submit" >
								Subscribe
							</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12 text-right"> 
							<a href="#" class="text-muted" onclick = "login()" ><i class='fa fa-arrow-left' style='margin-right:3%'></i>Login</a>
						</div>
					</div>                   
				</form:form>
            </div>
        </div>
    </div>
</div>

<script>
	function login(){
		window.location.assign("<c:url value = 'login'/>");
	}	
</script>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
