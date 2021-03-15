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

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Quotation</h3>					
	<a href="homePage">Home</a> » <a href="quotationList">Quotation</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">	
		<form:form id="quoteform" action="saveQuotation" method="post" commandName = "quotation">
			<div class="row">				
				<form:input path="quotation_id" hidden = "hidden"/>	
				<form:input path="save_id" id = "save_id" type = "hidden"/>			
				<form:input path="QuoteDetails" id = "QuoteDetails" type = "hidden"/>
				
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>First Name</label></div>		
				<div class="col-md-9">
					<input value="${quotation.first_name}" disabled="disabled"/>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Last Name</label></div>		
				<div class="col-md-9">
					<input value="${quotation.last_name}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company Name</label></div>		
				<div class="col-md-9">
					<input value="${quotation.company_name}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Email Id</label></div>		
				<div class="col-md-9">
					<input value="${quotation.email}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Mobile</label></div>		
				<div class="col-md-9">
					<input value="${quotation.mobile_no}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Type of Company</label></div>		
				<div class="col-md-9">
					<input value="${quotation.companystatutorytype.company_statutory_name}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Industry Type</label></div>		
				<div class="col-md-9">
					<input value="${quotation.industrytype.industry_name}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Status</label></div>		
				<div class="col-md-9">				
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>			
			
			

			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th></th>
						<th>Service </th>
						<th>Frequency</th>
						<th>Amount</th>	
						<th>Status</th>		
					</tr>
			</thead>
			<tbody>
					<c:forEach var = "quote_list" items = "${quotation.quotationDetails}">	
								<tr>
										<td><a href = "#" onclick ="deleteQuotation('${quote_list.quotation_detail_id}')"><img src='${deleteImg}' style = "width: 20px;"/></a>
										<td>${quote_list.service_id.service_name}</td>
										<td>${quote_list.frequency_id.frequency_name}</td>
										<td>
											<div class="col-md-9">		
				<c:choose>
                	<c:when test="${quote_list.amount == null}">
             			 <input id="amount_${quote_list.quotation_detail_id}" value="${quote_list.amount}"/>
						<i class="fa fa-plus btn-plus" id="amountplus_${quote_list.quotation_detail_id}" onclick="adddetails('${quote_list.quotation_detail_id}')"></i>
					
					</c:when>
                	<c:otherwise>
						${quote_list.amount}              	
					</c:otherwise>
                </c:choose>			
					                        </div>
				                        </td>
				                        <td>				                        		
											${quote_list.service_status==true ? "Active" : "Inactive"}
				                        </td>
								</tr>
					</c:forEach>
			</tbody>
			</table>
			<button type="button" class="fassetBtn waves-effect waves-light" onclick="showdiv()">Add Quotation Details</button>
		<div id="details-data" style="display:none">
				<table class="table table-bordered table-striped">
						<thead>
								<tr>
									<th>Service </th>
									<th>Frequency</th>
									<th>Amount</th>		
									<th style='width:25%'>Status</th>											
								</tr>
						</thead>
						<tbody>
		
								<tr>
									<td>
									<form:select path="service_id" class="logInput" >
									<form:option value="0" label="Select Service"/>		
					               <c:forEach var = "service" items = "${serviceList}">							
							       <form:option value = "${service.id}">${service.service_name}</form:option>	
					               </c:forEach>
				                   </form:select>
				                    <span class="logError"><form:errors path="service_id" /></span>
									</td>
										
									<td>
									<form:select path="frequency_id" class="logInput" >
									<form:option value="0" label="Select Frequency"/>		
					               <c:forEach var = "frequency" items = "${frequencyList}">							
							       <form:option value = "${frequency.frequency_id}">${frequency.frequency_name}</form:option>	
					               </c:forEach>
				                   </form:select>
				                   <span class="logError"><form:errors path="frequency_id" /></span>	
									</td>
									<td>
								        <div class="col-md-10">
					                    <form:input path="amount" class="logInput" id = "amount"  placeholder="Amount" />
				 	                    <span class="logError"><form:errors path="amount" /></span>
				                        </div>
				                    </td>
				                    <td>
						                 <div class="col-md-10">
						                    <form:radiobutton path="service_status" value="true" checked="checked" />Active 
											<form:radiobutton path="service_status" value="false" />Inactive
						                    </div>   
				                    </td>					
			
								</tr>
						</tbody>	
	    	</table>
	    		  			  <button type="submit" class="fassetBtn waves-effect waves-light" onclick = "SaveQuotationDetails()">Save Quotation Details</button>
	    	
	    	</div>
			<div class="text-center">
	   		<button class="fassetBtn" type="submit" onclick = "save()"> 
			Save & Back
			</button>
	   		</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
var qtList = [];  
 $(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	
	});
    function save(){
		$("#save_id").val(1);  
	}
    function SaveQuotationDetails(){
		$("#save_id").val(0);  
	}
    function showdiv(){
		document.getElementById("details-data").style.display="block";
            }
	function cancel(){
		window.location.assign('<c:url value = "quotationList"/>');	
	}
	function deleteQuotation(id){
		window.location.assign('<c:url value="deleteQuotationDetails"/>?id='+id);
	}
	function adddetails(detail_id)
	{
		amount=document.getElementById("amount_"+detail_id).value;
		if(amount!="")
		{
			qtList.push({"quotation_detail_id":detail_id, "amount":amount});
			$("#QuoteDetails").val(JSON.stringify(qtList));
			document.getElementById("amount_"+detail_id).readOnly="true";
			document.getElementById("amount_"+detail_id).style.border="none";
			document.getElementById("amount_"+detail_id).style.background="transparent";
			document.getElementById("amountplus_"+detail_id).style.display="none";		
			alert("Amount Added");
		}
		else
		{
			alert("Please Insert Amount for service");
		}
	}
	
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>