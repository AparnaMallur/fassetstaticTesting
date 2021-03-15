
<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong >${successMsg}</strong>
		
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Menu Master</h3>					
		 <a href=" homePage">Home</a> » <a href="MenuList">Menu Master</a> » <a href="#">Create</a> 
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="menuMasterForm" action="saveMenuMaster" method="post" commandName = "menumaster" >
		
			<div class="row">				
				<form:input path="menu_id" hidden = "hidden"/>
				
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Menu Name<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="menu_name" class="logInput" id = "menu_name" placeholder="Menu Name" />
					<span class="logError"><form:errors path="menu_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Menu Url<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="menu_url" class="logInput" id = "menu_url" placeholder="Menu Url" />
					<span class="logError"><form:errors path="menu_url" /></span>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Sequence No<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="sequence_no" class="logInput" id = "sequence_no" placeholder="Sequence No" />
					<span class="logError"><form:errors path="sequence_no" /></span>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Parent <span>*</span></label></div>		
				<div class="col-md-10">
				
					 <form:select path="parent"  id="parent_id" class="logInput" placeholder="parent Id" onChange="checksign(this.value)">
					 <form:option value = "0">No Parent</form:option>
					 <c:forEach var = "MenuMaster" items = "${menuMasterList}">	
						<form:option value = "${MenuMaster.menu_id}">${MenuMaster.menu_name}</form:option>					
					</c:forEach>		
					</form:select> 
				
					<span class="logError"><form:errors path="parent_id" /></span>
				</div>	
			</div>	
			
			 <div class="row" id="icon-div">	
				<div class="col-md-2 control-label"><label>Menu Icon<span id='req-icon' style='display:none'>*</span></label></div>		
				<div class="col-md-10">	
					<form:select path="menu_icon" class="logInput" placeholder="menu_icon">	
					<form:option value = "N/A">-- Select Icon --</form:option>			
						<form:option value = "N/A">NA</form:option>			
						<form:option value = "fa-user-plus">fa-user-plus</form:option>		
						<form:option value = "fa-bank">fa-bank</form:option>	
						<form:option value = "fa-users">fa-users</form:option>	
						<form:option value = "fa-user">fa-user</form:option>		
						<form:option value = "fa-file-text">fa-file-text</form:option>	
						<form:option value = "fa-bars">fa-bars</form:option>		
						<form:option value = "fa-newspaper-o">fa-newspaper-o</form:option>			
						<form:option value = "fa-picture-o">fa-picture-o</form:option>		
						<form:option value = "fa-building">fa-building</form:option>		
						<form:option value = "fa-phone-square">fa-phone-square</form:option>			
						<form:option value = "fa-archive">fa-archive</form:option>			
						<form:option value = "fa-cloud">fa-cloud</form:option>			
						<form:option value = "fa-paper-plane">fa-paper-plane</form:option>		
						<form:option value = "fa-cloud-download">fa-cloud-download</form:option>		
						<form:option value = "fa-gear">fa-gear</form:option>		
						<form:option value = "fa-pencil-square">fa-pencil-square</form:option>			
						<form:option value = "fa-comments">fa-comments</form:option>												
						<form:option value = "fa-question-circle">fa-question-circle</form:option>	
						<form:option value = "fa-envelope">fa-envelope</form:option>	
						<form:option value = "fa-map-marker">fa-map-marker</form:option>	
						<form:option value = "fa-folder-open">fa-folder-open</form:option>	
						<form:option value = "fa-server">fa-server</form:option>		
						<form:option value = "fa-life-ring">fa-life-ring</form:option>			
						<form:option value = "fa-briefcase">fa-briefcase</form:option>				
						<form:option value = "fa-graduation-cap">fa-graduation-cap</form:option>			
						<form:option value = "fa-download">fa-download</form:option>			
						<form:option value = "fa-globe">fa-globe</form:option>			
						<form:option value = "fa-pencil">fa-pencil</form:option>	
						<form:option value = "fa-laptop">fa-laptop</form:option>			
						<form:option value = "fa-certificate">fa-certificate</form:option>	
					</form:select>
					<span class="logError"><form:errors path="menu_icon" /></span>
					
				</div>	
			</div>	 
			<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>	
		<%-- 	 <div class="row">
					<div class="col-md-4 " style='text-align:center'>				 
			  			<form:input path="auditor" id = "auditor" value="" hidden = "hidden"/>
					 	<input style="position: absolute; " type="checkbox" onchange = "Auditoradd(this)"> 
				 	<label style="margin-left: 8%;">ClientAuditor</label></div>
				 	
					<div class="col-md-4" style='text-align:center'>
				  		<form:input path="client" id = "client" value="" hidden = "hidden"/>
					 	<input style="position: absolute; "  type="checkbox" onchange = "Clientadd(this)"> 
				 	<label style="margin-left: 8%;">ClientSupervisor</label></div>
				 	
				 	
					<div class="col-md-4 " style='text-align:center'>
						  <form:input path="auditor" id = "auditor" value="" hidden = "hidden"/>
						 <input style="position: absolute; " type="checkbox"  onchange = "Auditorradd(this,${roleMenuMaster.auditorId})"> 
					 <label style="margin-left: 8%;">Auditor</label></div>
					 			 
				</div> 
			 --%>
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

		$("#menu_name,#menu_url").keypress(function(e) {
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
		window.location.assign('<c:url value = "MenuList"/>');	
	}
</script>
			
			<script type="text/javascript">
			function checksign(parent)
			{
				if(parent!=0)
					document.getElementById('req-icon').style.display="inline";
				else
					document.getElementById('req-icon').style.display="none";

			}
			
			/* function SuperUseradd(e,auditorId)
			{
				if(e.checked){
					
					$("#auditor").val(auditorId);
				}
				else{
					;
					$("#auditor").val("");
				}
			} */
			var clientId;
			var auditorId;
			function Clientadd(e)
			{
				if(e.checked)
					{
					clientId=1;
						$("#client").val(clientId);
					}
				else
					{
					clientId=0;
						$("#client").val(clientId);
					}
			}
			
			function Auditoradd(e)
			{
				if(e.checked)
					{
					auditorId=1;
						$("#auditor").val(auditorId);
					}
				else
					{
					auditorId=0;
						$("#auditor").val(auditorId);
					}
			}
			
			</script>
<script type="text/javascript">


$(document).ready(function() 
		{
			//var x = document.getElementById("client").value;		
			var parent = '<c:out value= "${menumaster.parent_id}"/>';
			if(parent!=0)
			document.getElementById('req-icon').style.display="inline";
			else
				document.getElementById('req-icon').style.display="none";

			//document.getElementById("client").checked = true;
		}); 
		


</script>			
			
<%@include file="/WEB-INF/includes/footer.jsp" %>