<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Checklist Master</h3>					
	<a href="homePage">Home</a> » <a href="checklist">Checklist Master</a>  » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="checklistForm" action="saveclientValidation" method="post" commandName = "validation">
			<div class="row">				
				<form:input path="checklist_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Checklist<span>*</span></label></div>		
				<div class="col-md-10">		
					<form:input path="checklist_name" class="logInput" id = "checklist_name" placeholder="Checklist Name" />
					<span class="logError"><form:errors path="checklist_name" /></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Is Checklist Mandatory<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="is_mandatory" value="true" checked="checked" />Yes
					<form:radiobutton path="is_mandatory" value="false" />No
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
var comList = []; 
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#checklist_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
	});
	
	/* function deleteSubledger(id){
		window.location.assign('<c:url value="deleteIndustrySubLedger"/>?id='+id);
	} */
	
	function cancel(){
		window.location.assign('<c:url value = "checklist"/>');	
	}
	
	function addCom(company){
		var comValue = company.value;
		if(company.checked){
			comList.push(comValue);
			console.log(comList);
		}
		else{
			for(i = 0; i<comList.length; i++){
				if(comList[i] == comValue){
					comList.splice(i,1);
					break;
				}
			}
			console.log(comList);
		}
	}
	
	function addCompany(){
		$("#companyList").val(comList);
		return true;
	}
</script>
	
	<%@include file="/WEB-INF/includes/footer.jsp" %>