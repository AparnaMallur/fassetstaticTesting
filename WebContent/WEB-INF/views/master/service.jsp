<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_10%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Service</h3>					
	<a href="homePage">Home</a> » <a href="serviceList">Service</a> » <a href="#">Create</a>
</div>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="serviceForm" action="saveservice" method="post" commandName = "service">
			<div class="row">
				<form:input path="id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Service<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="service_name" class="logInput"
						id = "service_name" maxlength="30" placeholder="Service Name" />
					<span class="logError"><form:errors path="service_name" /></span>
				</div>	
			</div>	
			<!-- <div class="row">	
				<div class="col-md-2 control-label"><label>Service Charge<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="service_charge" class="logInput"
						id = "service_charge" maxlength="${SIZE_TWO_HUNDRED}" placeholder="Service Charge" />
					<span class="logError"><form:errors path="service_charge" /></span>
				</div>	
			</div>		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Frequency<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="service_frequency" class="logInput" placeholder="Service Frequency">	
						<form:option value="0" label="Select Frequency"/>						
						<c:forEach var = "frequency" items = "${frequencyList}">							
							<form:option value = "${frequency.frequency_id}">${frequency.frequency_name}</form:option>	
						</c:forEach>
					</form:select>
				</div>	
			</div>	-->
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

		$("#service_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		$("#service_charge").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});	
	});
	
	function cancel(){
		window.location.assign('<c:url value = "serviceList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>