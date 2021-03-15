<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>GST Master</h3>
	<a href="homePage">Home</a> » <a href="gstList">GST</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form commandName = "gst">
		<div class="col-md-12">
			<table class = "table">	
			<tr>
						<td><strong>Start Date:</strong></td>
						<fmt:parseDate value="${gst.start_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="start_date" type="date" pattern="dd-MM-yyyy" />
						<td style="text-align: left;">${start_date}</td>			
					</tr>
					<tr>
						<td><strong>End Date:</strong></td>
						<fmt:parseDate value="${gst.end_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="end_date" type="date" pattern="dd-MM-yyyy" />
						<td style="text-align: left;">${end_date}</td>			
					</tr>			
				<tr>
					<td><strong>HSN/SAC Number:</strong></td>
					<td style="text-align: left;">${gst.hsc_sac_code}</td>			
				</tr>
				<tr>
					<td><strong>Chapter:</strong></td>
					<td style="text-align: left;">${gst.chapter.chapterNo}</td>			
				</tr>
				<tr>
					<td><strong>Schedule:</strong></td>
					<td style="text-align: left;">${gst.schedule.scheduleName}</td>			
				</tr>
				<tr>
					<td><strong>Description:</strong></td>
					<td style="text-align: left;">${gst.description}</td>			
				</tr>
				<tr>
					<td><strong>CGST:</strong></td>
					<td style="text-align: left;">${gst.cgst}</td>			
				</tr>
				<tr>
					<td><strong>IGST:</strong></td>
					<td style="text-align: left;">${gst.igst}</td>			
				</tr>
				<tr>
					<td><strong>SGST:</strong></td>
					<td style="text-align: left;">${gst.sgst}</td>			
				</tr>
				<tr>
					<td><strong>CESS:</strong></td>
					<td style="text-align: left;">${gst.state_comp_cess}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${gst.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${gst.tax_id})">
				<spring:message code="btn_edit" />
			</button>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editGST"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "gstList"/>');
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>