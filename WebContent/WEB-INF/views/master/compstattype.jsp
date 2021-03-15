<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_HUNDRED"><%=MyAbstractController.SIZE_HUNDRED%></c:set>
<div class="breadcrumb">
	<h3>Company Statutory Type</h3>					
	<a href="homePage">Home</a> » <a href="companystatutorytypeList">Company Statutory Type</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="CompanyStatutoryTypeForm" action="savecompanystatutorytype" method="post" commandName ="type">
			<div class="row">
				<form:input path="company_statutory_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company Statutory Type<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company_statutory_name" class="logInput"
						id = "company_statutory_name" maxlength="${SIZE_HUNDRED}" placeholder="Company Statutory Type" />
					<span class="logError"><form:errors path="company_statutory_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-9">
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
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

		$("#company_statutory_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
	});
	
	function cancel(){
		window.location.assign('<c:url value = "companystatutorytypeList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>