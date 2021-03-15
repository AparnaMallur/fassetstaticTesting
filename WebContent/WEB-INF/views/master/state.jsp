<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<%--<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>State</h3>					
	<a href="homePage">Home</a> » <a href="stateList">State</a> » <a href="#">Create</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="stateForm" action="savestate" method="post" commandName = "state">
				<div class="row">
					<form:input path="state_id" hidden = "hidden"/>
					<form:input path="created_date" hidden = "hidden"/>
				</div>		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Country<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:select path="country_id" class="logInput" placeholder="status">
						<form:option value="0" label="Select Country"/>	
						<c:forEach var = "country" items = "${countryList}">							
							<form:option value = "${country.country_id}">${country.country_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="country_id" /></span>
				</div>
			</div>			
			<div class="row">	
				<div class="col-md-2 control-label"><label>State Name<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:input path="state_name" class="logInput"
						id = "state_name" maxlength="${SIZE_THIRTY}" placeholder="State Name" />
					<span class="logError"><form:errors path="state_name" /></span>
				</div>
			</div>		
			<div class="row">	
				<div class="col-md-2 control-label"><label>State Code<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:input path="state_code" class="logInput"
						id = "state_code" maxlength="${SIZE_SIX}" placeholder="State Code" />
					<span class="logError"><form:errors path="state_code" /></span>
				</div>
			</div>		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>	
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
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
	var datefield=document.getElementById("fromDate");
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#state_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});

		$("#state_code").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		
	});
	function cancel(){
		window.location.assign('<c:url value = "stateList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>