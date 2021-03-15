<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="WEBURL"><%=MyAbstractController.WEBURL%></c:set>
<spring:url value="/resources/images/logo.jpg" var="vdakpologo" />

 <div class="col-md-12 col-sm-12 col-xs-12">
     <a href="${WEBURL}" target="_blank">
  <img src="${vdakpologo}" alt="" class="box-circel" style="height: 80px;background-color: white;padding: 1%;margin: 5%;"> 
 
 	<div class="wrapper-page">
        <div class="panel panel-color panel-primary panel-pages">
			<h3 class="text-center logFormTexts">Code Verification</h3>        
            <div class="panel-body">
		       <form:form class="form-horizontal m-t-20" id="checkCode" name="checkCode" action="checkCode" method="POST" commandName="checkCode">
                    <div class="form-group">
		                 <div class="col-xs-12" style='text-align:center'>
		                    <input type = "text" name = "code" placeholder="Enter the code sent your mail id"/>
			 			</div>
			 				<div class="col-xs-12" style='text-align:center'>
			 			
			 	<c:if test="${codeError != null}">
					<span class="logError">${codeError}</span>
				</c:if>   
						</div>                                      
					   </div>					  
                    <div class="form-group text-center m-t-40">
                        <div class="col-xs-12">
								<button type="submit" >
									<spring:message code="continue" />
								</button>
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