<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Country</h3>					
	<a href="homePage">Home</a> » <a href="countryList">Country</a> » <a href="#">Create</a>
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
	
		<form:form id="countryForm" action="savecountry" method="post" commandName = "country">
			<div class="row">				
				<form:input path="country_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Country<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="country_name" class="logInput" id = "country_name" maxlength="${SIZE_THIRTY}" placeholder="Country Name" />
					<span class="logError"><form:errors path="country_name" /></span>
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

		$("#country_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
	});
	
	function cancel(){
		window.location.assign('<c:url value = "countryList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>