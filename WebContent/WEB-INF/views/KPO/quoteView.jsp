<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>


<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>

<div class="breadcrumb">
	<h3>Quotation</h3>					
	<a href="homePage">Home</a> » <a href="quotationList">Quotation</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="quotationForm"  commandName = "quotation">
		<div class="col-md-12">
			<table class = "table">
			<tr>
				<td><strong>Quotation No:</strong></td>
					<td style="text-align: left;">${quotation.quotation_no}</td>			
				</tr>
				<tr>
				<td><strong>Name:</strong></td>
					<td style="text-align: left;">${quotation.first_name}
					${quotation.last_name}</td>			
				</tr>
				
				<tr>
				<td><strong>Date:</strong></td>
				
				 <td style="text-align: left;">
						<fmt:parseDate value="${quotation.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="quotaDate" type="date" pattern="dd-MM-yyyy"/>
						${quotaDate}
				 </td>
				</tr>
				
				<%-- <tr>
				<td><strong>Time:</strong></td>
					<td style="text-align: left;">${quotation.time}</td>			
				</tr> --%>
				
				<tr>
				<td><strong>Email:</strong></td>
					<td style="text-align: left;">${quotation.email}</td>			
				</tr>
				<tr>
				<td><strong>Mobile:</strong></td>
					<td style="text-align: left;">${quotation.mobile_no}</td>			
				</tr>
				<tr>
				<td><strong>Company Name:</strong></td>
					<td style="text-align: left;">${quotation.company_name}
								<%-- <c:forEach var = "quoted" items = "${quoteDetails}">
								${quoted}
						</c:forEach> --%>
					 </td>			
				</tr>
				<tr>
				<td><strong>Company Statutory Type:</strong></td>
					<td style="text-align: left;">${type.company_statutory_name}</td>			
				</tr>
				<tr>
				<td><strong>Industry Type:</strong></td>
					<td style="text-align: left;">${industryType.industry_name}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${quotation.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</table>
			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th>Service </th>
						<th>Frequency</th>
						<th>Amount</th>						
					</tr>
			</thead>
			<tbody>
					<c:forEach var = "quote_list" items = "${quotation.quotationDetails}">	
								<tr>
									<td>${quote_list.service_id.service_name}</td>
										<td>${quote_list.frequency_id.frequency_name}</td>
										<td>${quote_list.amount}</td>
								</tr>
					</c:forEach>
			</tbody>
			</table>
		</div>
		<div class="row"  style = "text-align: center; margin:15px;">
		<c:if test="${quotation.flag!=true}">
		<%-- 	<button class="fassetBtn" type="button" onclick = "edit(${quotation.quotation_id})">
				<spring:message code="btn_edit"/>
			</button> --%>
			<button class="fassetBtn" type="button" onclick = "sendmailQuotation(${quotation.quotation_id})">
Send Mail			</button>
			</c:if>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back"/>
			</button>
		</div>
	</form:form>
</div>
			<!-- Small modal -->
			<div id="mail-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Send Quotation</h4>
	               		</div>
		   				<div class="modal-body">
							<form class="form-horizontal" role="form" method='POST' action='sendmailQuotation'>
								<div class="form-group"> 
									<label for="inputEmail3" class="col-sm-2 control-label">Message</label>
											<div class="col-sm-10"> 
												<input class="form-control" type="hidden" id="quote_id" name="quote_id" placeholder="Quotation Id" value="${quotation.quotation_id}">											
												<input class="form-control" type="hidden" id="email_id" placeholder="Email Id" value="${quotation.email}">											
												<textarea class="form-control" id="message" >Message</textarea>
											</div>
								</div>
								<div class="form-group">
								<input class="filestyle" data-iconname="fa fa-cloud-upload" id="filestyle-6" style="position: absolute; clip: rect(0px, 0px, 0px, 0px);" tabindex="-1" type="file">
								<div class="bootstrap-filestyle input-group" style="width: 97%;margin: auto;">
									<input class="form-control " placeholder="" disabled="" type="text"> 
										<span class="group-span-filestyle input-group-btn" tabindex="0">
											<label for="filestyle-6" class="btn btn-default ">
												<span class="icon-span-filestyle fa fa-cloud-upload"></span> 
												<span class="buttonText">Attach Quotation</span>
											</label></span>
								</div>
								</div>
										               		<button type="submit" class="btn btn-primary waves-effect waves-light" >Send Mail</button>
								
							</form>
						</div>
						<div class="modal-footer">
						</div>
	       			</div><!-- /.modal-content -->
	      		</div>
			</div>
			<!-- /.modal -->   
<script type="text/javascript">

$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	function edit(id){
		window.location.assign('<c:url value = "editQuotation"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "quotationList"/>');	
	}
	
	function sendmailQuotation(id){
		// $('#mail-model').modal('hide');
		window.location.assign('<c:url value = "sendmailQuotation"/>?id='+id);	
	}
	function showmail(id){
		$('#mail-model').modal({
	    	show: true,
	   	});
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>