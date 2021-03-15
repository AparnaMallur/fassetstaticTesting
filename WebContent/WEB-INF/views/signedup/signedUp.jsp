<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/validations.js" var="valid" />
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<div class="col-md-12 col-sm-12 col-xs-12 ws">
  <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 
  </a>
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
				<form:form  class='form-horizontal m-t-20' id="signupForm" action="signedUp" method="post" commandName="signedUpForm">     
					<div class="form-group">
						<div class="col-xs-12 " style='text-align:center'>
							<div class="radio radio-info radio-inline"> 
								<form:radiobutton path = "signUpType" id="trialSignUpType" value="<%=MyAbstractController.STATUS_TRIAL_LOGIN%>" name="signUpType" checked="checked" onchange = "showTrial(this)"/> 
								<label for="inlineRadio1"> Trial User </label>
							</div>
							<div class="radio  radio-info radio-inline"> 
								<form:radiobutton path = "signUpType" id="subscribeSignUpType" value="<%=MyAbstractController.STATUS_PENDING_FOR_APPROVAL%>" name="signUpType" onchange = "showSubscribe(this)"/> 
								<label for="inlineRadio2"> Subscribe User </label>
							</div>			                          
				 	  	</div>  
					</div>
					<div class="form-group" id = "userName">
						<div class="col-xs-6">
	                   		<form:input path="user.first_name" class="form-control" id = "first_name" maxlength="${SIZE_THIRTY}" placeholder="First Name" />
							<span class="logError"><form:errors path="user.first_name" /></span>                                         
				 	  	</div>
				 	  	<div class="col-xs-6">
	                  		<form:input path="user.last_name" class="form-control" id = "last_name" maxlength="${SIZE_THIRTY}" placeholder="Last Name" />
							<span class="logError"><form:errors path="user.last_name" /></span>                                      
				 	  	</div>
					</div>
					<div class="form-group">
						<div class="col-xs-6">
							<form:input path="company.email_id" class="form-control" placeholder="Email Address" />
							<span class="logError"><form:errors path="company.email_id" /></span>                                        
						</div>
						<div class="col-xs-6">
							<form:input path="company.mobile" class="form-control" id = "mobile" maxlength="${SIZE_TEN}" placeholder="Mobile Number" />
							<span class="logError"><form:errors path="company.mobile" /></span>                                    
				 	  	</div>                        
					</div>
					<div class="form-group" id = "companyName">
						<div class="col-xs-12">
							<form:input path="company.company_name" class="form-control" placeholder="Company Name" />
							<span class="logError"><form:errors path="company.company_name" /></span>                                       
						</div> 
					</div>
					<div class="form-group" id = "quotNo">
						<div class="col-xs-12">
							<form:input path="quotationNo" class="form-control" placeholder="Quotation Number" />
							<span class="logError"><form:errors path="quotationNo" /></span>                                       
						</div>
					</div>
					<div class="form-group" id = "companyTypeRad">
						<div class="col-xs-6">
							<form:select path = "industryTypeId" class="form-control" >
								<form:option value="0" label="Industry Type"/>								
								<c:forEach var = "industryType" items = "${industryTypes}" >
									<form:option value="${industryType.industry_id}" style="background-color:rgba(0,0,0,0.5)!important">${industryType.industry_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="industryTypeId" /></span>                                      
				 	  	</div>
						<div class="col-xs-6">
							<form:select path = "companyTypeId" class="form-control">
								<form:option value="0" label="Company Statutory Type"/>		
								<c:forEach var = "companyType" items = "${companyTypes}">
									<form:option value="${companyType.company_statutory_id}" style="background-color:rgba(0,0,0,0.5)!important">${companyType.company_statutory_name}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="companyTypeId" /></span>
						</div> 	
						<div class="col-xs-6">
							<form:select path = "company.YearRangeId" class="form-control">
								<form:option value="0" label="Accounting Year"/>		
								<c:forEach var = "accYear" items = "${accountingYear}">
									<form:option value="${accYear.year_id}" style="background-color:rgba(0,0,0,0.5)!important">${accYear.year_range}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="company.YearRangeId" /></span>
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

<script>
	
	$(document).ready(function() {
	
		var signUpType = $("input[type=radio][name='signUpType']:checked").val();
		if(signUpType == '<%=MyAbstractController.STATUS_TRIAL_LOGIN%>') {
			trialForm();
	   	}
  	 	else {
			subscribeForm();
	   	}
		
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
	
	function showSubscribe(e){
		if(e.checked){
			subscribeForm();
		}
	}
	
	function showTrial(e){
		if(e.checked){
			trialForm();
		}
	}
	
	function subscribeForm(){
		$("#userName").hide();
		$("#companyTypeRad").hide();
		$("#companyName").hide();
		$("#paymentMethod").show();
		$("#quotNo").show();		
	}
	
	function trialForm(){
		$("#userName").show();
		$("#companyTypeRad").show();
		$("#companyName").show();
		$("#paymentMethod").hide();
		$("#quotNo").hide();		
	}
</script>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>