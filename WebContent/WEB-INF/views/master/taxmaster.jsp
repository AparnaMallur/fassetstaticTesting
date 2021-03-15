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
	<h3>TaxMaster</h3>					
	<a href="homePage">Home</a> » <a href="taxMasterList">TaxMaster</a> » <a href="#">Create</a>
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
	
		<form:form id="taxMasterForm" action="savetaxMaster" method="post" commandName = "taxMaster">
			<div class="row">				
				<form:input path="tax_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Tax Name<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="tax_name" class="logInput" id = "tax_name" placeholder="Tax Name" />
					<span class="logError"><form:errors path="tax_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>VAT<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="vat1" class="logInput" id = "vat1" placeholder="Vat"  maxlength="3"/>
					<span class="logError"><form:errors path="vat1" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>CST<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="cst1" class="logInput" id = "cst1" placeholder="Cst" maxlength="3" />
					<span class="logError"><form:errors path="cst1" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>EXCISE<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="excise1" class="logInput" id = "excise1" placeholder="Excise" maxlength="3" />
					<span class="logError"><form:errors path="excise1" /></span>
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

	    $("#vat1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    $("#cst1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    $("#excise1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
		
	});
	
	function cancel(){
		window.location.assign('<c:url value = "taxMasterList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>