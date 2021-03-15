<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Group</h3>					
	<a href="homePage">Home</a> » <a href="accgroupList">Group</a> » <a href="#">Create</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="AccountGroupForm" action="saveaccountgroup" method="post" commandName = "group">
			<div class="row">	
				<form:input path="group_Id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Group Name<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="group_name" class="logInput"
						id = "group_name" maxlength="50" placeholder="Group Name" />
					<span class="logError"><form:errors path="group_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Group Type<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="account_group_id" class="logInput">
						<form:option value="0" label="Select Group Type"/>						
						<c:forEach var = "list" items = "${accgptypeList}">		
							<form:option value = "${list.account_group_id}">${list.account_group_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="account_group_id" /></span>
				</div>	
			</div>				
			<div class="row">	
				<div class="col-md-2 control-label"><label>Posting<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="posting_id" class="logInput">
						<form:option value="0" label="Select Posting"/>						
						<c:forEach var = "listp" items = "${ledPostingSideList}">		
							<form:option value = "${listp.posting_id}">${listp.posting_title}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="posting_id" /></span>
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

		$("#group_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		$("#sequence_no").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		
	});
	function cancel(){
		window.location.assign('<c:url value = "accgroupList"/>');	
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>