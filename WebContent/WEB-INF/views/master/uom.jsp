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
	<h3>Unit of Measurement</h3>
	<a href="homePage">Home</a> » <a href="uomList">Unit of Measurement</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">	
	<div class="fassetForm">
		<form:form id="uomForm" action="uom" method="post" commandName = "uom">
			<div class="row">
				<form:input path="uom_id" hidden = "hidden"/>
			</div>
			<div class="row">
				<div class="col-md-3 control-label"><label>Unit of Measurement<span>*</span></label></div>
				<div class="col-md-9">
					<form:input path="unit" class="logInput" id = "unit" placeholder="Unit of Measurement"  maxlength="10" onkeypress="return validData()" />
					<span class="logError"><form:errors path="unit" /></span>
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
/* $(function() {

	$("#unit").keypress(function(e) {
		if (!letters(e)) {
			return false;
		}
	});
	 */
	 
	  function validData()
	 {
		if ((event.keyCode > 64 && event.keyCode < 91) || (event.keyCode > 96 && event.keyCode < 123) || event.keyCode == 8)
	        return true;
	     else
	        {
	          alert("Please enter  alphabets  only");
	            return false;
	        } 
	 }
	function cancel(){
		window.location.assign('<c:url value = "uomList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>