<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Industry Type</h3>					
	<a href="homePage">Home</a> » <a href="industrytypeList">Industry Type</a> » <a href="#">View</a>
</div>

	<div class="fassetForm">
		<form:form id="IndustryTypeForm"  commandName = "type">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>IndustryType:</strong></td>
						<td style="text-align: left;">${type.industry_name}</td>			
					</tr>
					<c:forEach var = "subledger" items = "${type.subLedgers}">
					<tr>
					<td style="text-align: left;">
						${subledger.ledger.ledger_name}
					</td>
					<td >${subledger.subledger_name}</td>
				    </tr>
					</c:forEach>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${type.status==true ? "Enable" : "Disable"}</td>			
					</tr>
				</table>
			<c:choose>
				<c:when test="${!empty subLedgers}">
				<h4>Subledgers</h4>
					<c:forEach var="subledger" items="${subLedgers}">
						<div class='col-md-3 col-sm-4 col-xs-6'
							style='background: #E1E1E1; border: 1px solid #ccc'>
							<p class="indus-sub font-600 m-b-5">${subledger.subledger_name}
							</p>
						</div>
					</c:forEach>
				</c:when>
			</c:choose>
		</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${type.industry_id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editIndustryType"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "industrytypeList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>