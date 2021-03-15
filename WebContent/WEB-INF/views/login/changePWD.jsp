<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<spring:url value="/resources/images/logo.jpg" var="vdakpo" />

<div class="col-md-12 col-sm-12 col-xs-12">
	<a href="http://fassetweb.disctesting.in/">  
	<img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 

	</a>
	<div class="wrapper-page">
		<div class="panel panel-color panel-primary panel-pages">
			<div class="logFormTexts">
				<h1 style='color:#fff'>
					<spring:message code="P3.reset" />
				</h1>
				<p>
					<spring:message code="P3.newpwd" />
				</p>
			</div>
			<div class="panel-body">

				<c:if test="${error != null}">
					<div class="logAlert">
						<strong><spring:message code="warning" /></strong> ${error}
					</div>
				</c:if>
				<form:form id="changePWD" action="changePWD" method="post"
					class="form-horizontal m-t-20" commandName="changePass">
					<form:input path="userName" type = "hidden"/>
					<div class="form-group">
						<div class="col-xs-12">
							<div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                              		 <form:password path="pass"  class="form-control" placeholder="New Password" id="password1"/>
                              </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i>
								</span>
							</div>							
							<span class="logError"><form:errors path="pass" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-12">
							<div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                              		 <form:password path="confPass"  class="form-control" placeholder="Confirm Password" id="password2"/>
                              </div> 
                                <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(2);" onmouseout="mouseoutPass(2);" ></i>
								</span>
							</div>	
							<span class="logError"><form:errors path="confPass" /></span>
						</div>
					</div>
						<div class="form-group">
						<c:if test="${companyList.size()>1 }">
		<div class="col-xs-12">
		
		   				
		   				
							<form:select path="company_id" class="form-control" >
								<form:option value="0" label="Company "/>								
								<c:forEach var = "user" items = "${companyList}" >
									<form:option value="${user.company.company_id}" style="background-color:rgba(0,0,0,0.5)!important">${user.company.company_name}</form:option>
								</c:forEach>
							</form:select>
							                              
				 	  <span class="logError"><form:errors path="company_id" /></span>
				 	  	</div>
				 	  	</c:if>
				 	  	<c:if test="${companyList.size()==1 }">
				 	  	<div class="col-xs-12">
							<div class="input-group m-b-15 password-checker">
                               <div class="bootstrap-timepicker">
                               <c:forEach var = "user" items = "${companyList}" >
                              		 <form:input path="company_id" class="form-control" placeholder="Company " id="companyId" value ="${user.company.company_id}"/>
                              		</c:forEach>
                              </div> 
                                
							</div>	
							<span class="logError"><form:errors path="company_id" /></span>
						</div>
						</c:if>
				 	  	</div>
					<div class="form-group text-center m-t-40">
						<div class="col-xs-12">
							<button class="fassetBtn" type="submit" form="changePWD"
								value="Submit">
								<spring:message code="P3.change" /> 
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>
<script>
	function mouseoverPass(obj) {
		  var obj = document.getElementById('password'+obj);
		  obj.type = "text";
		}
		function mouseoutPass(obj) {
		  var obj = document.getElementById('password'+obj);
		  obj.type = "password";
		}
</script>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
