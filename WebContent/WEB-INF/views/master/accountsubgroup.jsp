<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Sub Group</h3>					
	<a href="homePage">Home</a> » <a href="accsubgroupList">Sub Group</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="AccountSubGroupForm" action="saveaccountsubgroup" method="post" commandName = "subgroup">
			<div class="row">
				<form:input path="subgroup_Id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">
				<div class="col-md-2 control-label"><label>Group<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="group_Id" class="logInput">
						<form:option value="0" label="Select Group"/>							
						<c:forEach var = "list" items = "${accountGroupList}">	
							<form:option value = "${list.group_Id}">${list.group_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="group_Id" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label"><label>Sub Group<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="subgroup_name" class="logInput" id = "subgroup_name" maxlength="${SIZE_THREE_HUNDRED}" placeholder="Sub Group Name" />
					<span class="logError"><form:errors path="subgroup_name" /></span>
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
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#subgroup_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
	});
	function cancel(){
		window.location.assign('<c:url value = "accsubgroupList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>