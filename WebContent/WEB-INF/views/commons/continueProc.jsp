<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<div class="col-md-12">
			<div class='thanx-note'>
					<h2>${headTitle}</h2>
					<c:if test="${error != null}">
						<div class="logAlert">
							<strong><spring:message code="warning" /></strong>
							<spring:message code="P1.error" />
						</div>
					</c:if>
					<h4>${message}</h4>
						<div class='text-center-btn'>
					<a href="${nextForm}" >
						<button class="logBt">
							<spring:message code="continue" />
						</button></a>		
						</div>			
			</div>

<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
