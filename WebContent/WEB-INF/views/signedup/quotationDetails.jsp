<%@include file="/WEB-INF/includes/headerLogin.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/js/validations.js" var="valid" />
<spring:url value="/resources/images/closeRed.png" var="closeBtn" />
<spring:url value="/resources/images/next.png" var="next" />
<spring:url value="/resources/css/images/user-profile.png" var="userprofile" />
<c:set var = "paymentOnline"><%=MyAbstractController.PAYMENT_ONLINE%></c:set>

<script type="text/javascript" src="${valid}"></script>
<div class="col-md-12 col-sm-12 col-xs-12 ws">
	<form:form action="proceedNext" id = "payDetailForm" method="post" modelAttribute="quotation">
		<form:input path="quotation_id" hidden = "hidden"/>
		<form:input path="paymentType" hidden = "hidden"/>
		<form:input path="amount" hidden = "hidden"/>
		<form:input path="companyId" hidden = "hidden"/>
	 	<div class="row quote-data">
			<div class="col-md-3 col-sm-3 col-xs-3"><img src="${userprofile}"></div>
				<div class="co-md-9 col-xs-9">
				  <p><i class="fa fa-user" /></i>${quotation.first_name} ${quotation.last_name}</p>
				  <p><i class="fa fa-university" /></i>${quotation.company_name}</p>
				  <p><i class="fa fa-phone" /></i>${quotation.mobile_no}</p>
				  <p><i class="fa fa-building" /></i>${quotation.companystatutorytype.company_statutory_name}</p>
				  <p><i class="fa fa-industry" /></i>${quotation.industrytype.industry_name}</p>
				 <c:set var="payment" value="${quotation.paymentType}" />			
				  
			</div>
		  	<div class="col-md-12">
			    <table class="table table-hover">
	    			<thead>
	   					<tr>
							<th>Service</th>
							<th>Frequency</th>
							<th>Amount</th>
	   					</tr>
	    			</thead>
	    			<tbody>
					<c:forEach var = "quotDetail" items = "${quotation.quotationDetails}">
						<tr>
							<td style="text-align: left;">${quotDetail.service_id.service_name}</td>
							<td style="text-align: left;">${quotDetail.frequency_id.frequency_name}</td>
							<td style="text-align: left;">${quotDetail.amount}</td>
						</tr>
					</c:forEach>
	    				<tr>
	    					<td colspan="2" style="text-align:right">Total Amount</td>
	    					<td>${quotation.amount}</td>
	    				</tr>
	    			</tbody>
	   			</table>
		  	</div>
			  
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<c:if test="${payment != paymentOnline}">
					<button class="fassetBtn" type="submit" onclick = "next()">
						Continue
					</button>
				</c:if>				
				<c:if test="${payment == paymentOnline}">
					<button class="fassetBtn" type="submit" onclick = "ccavenuepayment()">
						Pay Through CCAvenue
					</button>
				</c:if>
			</div>
		</div>
	</form:form>
</div>

<script>
function next(){
	window.location.assign("<c:url value = 'proceedNext'/>");
}
function ccavenuepayment(){
	$("#payDetailForm").attr('action', 'ccavenuepayment');
}
</script>
<%@include file="/WEB-INF/includes/footerLogin.jsp"%>
