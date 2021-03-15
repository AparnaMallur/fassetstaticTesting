<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/validations.js" var="valid" />
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />

<div class="col-md-12 col-sm-12 col-xs-12 ws">
    <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 

 	<div class="wrapper-page ">
        <div class="panel panel-color panel-primary panel-pages">
			<h3 class="text-center logFormTexts">Sign Up</h3>        
            <div class="panel-body">
				<c:if test="${error != null}">
					<div class="logAlert">
						<strong><spring:message code="warning" /></strong>
						<spring:message code="P1.error" />
					</div>
				</c:if>
				<form:form  class='form-horizontal m-t-20' id="signupForm" action="subscribedSignedUp" method="post" commandName="signedUpForm">     
					<div class="form-group">
						<div class="col-xs-12">
							<form:input path="company.company_name" class="form-control" placeholder="Company Name" />
							<span class="logError"><form:errors path="company.company_name" /></span>                                       
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<form:select path = "companyTypeId" class="form-control">
								<form:option value="0" label="Select Statutory Type"/>		
								<c:forEach var = "companyType" items = "${companyTypes}">
									<form:option value="${companyType.company_statutory_id}" style="background-color:rgba(0,0,0,0.5)!important">${companyType.company_statutory_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="companyTypeId" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<form:select path = "industryTypeId" class="form-control" >
								<form:option value="0" label="Select Industry"/>								
								<c:forEach var = "industryType" items = "${industryTypes}" >
									<form:option value="${industryType.industry_id}" style="background-color:rgba(0,0,0,0.5)!important">${industryType.industry_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="industryTypeId" /></span>                                      
				 	  	</div> 
					</div>
					<div class="form-group">
						<div class="col-xs-12 " style='text-align:center'>
							<div class="radio radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio1" value="option1" name="radioInline" checked="checked"/> 
								<label for="inlineRadio1"> Online Payment </label>
							</div>
							<div class="radio  radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio2" value="option2" name="radioInline"/> 
								<label for="inlineRadio2"> RTGS/IMPS </label>
							</div>
							<div class="radio  radio-info radio-inline"> 
								<form:radiobutton path = "paymentType" id="inlineRadio3" value="option3" name="radioInline"/> 
								<label for="inlineRadio3"> Offline </label>
							</div>			                          
				 	  	</div>  
					</div>
					<div class="form-group text-center ">
						<div class="col-xs-12">
							<button class="btn btn-primary btn-block btn-lg waves-effect waves-light logBt" type="submit"  form="signupForm" value="Submit" >
								<spring:message code="signedUp" />
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
<script type="text/javascript">
	$(function() {

		$("#first_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});

		$("#last_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
		$("#mobile").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
	});
	function login(){
		window.location.assign("<c:url value = 'login'/>");
	}
</script>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
