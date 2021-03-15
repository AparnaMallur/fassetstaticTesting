<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
	
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Client Allocation</h3>
	<a href="homePage">Home</a> » <a href="clientAllocationToKpoExecutiveList">Client Allocation</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">	
	<div class="fassetForm">
		<form:form id="clientAllocationForm" action="clientallocation" method="post" commandName = "clientAllocationToKpoExecutive">									
			<form:hidden path="allocation_Id"/>
			<form:hidden path="allocation_Id" id = "allocation_Id"/>	
			<div class="row">
				<div class="col-md-3 control-label"><label>Executive/Manager Name<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="user_id" class="logInput">
						<form:option value="0" label="Select User"/>	
						<c:forEach var = "user" items = "${userList}">													
							<form:option value = "${user.user_id}">${user.first_name} ${user.last_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="user_id" /></span>
				</div>
			</div>
			<div class="row">				
				<div class="col-md-3 control-label"><label>Company/Firm Name<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="company_id" class="logInput">
						<form:option value="0" label="Select Company"/>	
						<c:forEach var = "company" items = "${companyList}">													
							<form:option value = "${company.company_id}">${company.company_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company_id" /></span>
				</div>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>	
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function cancel(){
		window.location.assign('<c:url value = "clientAllocationToKpoExecutiveList"/>');	
	}
	
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>