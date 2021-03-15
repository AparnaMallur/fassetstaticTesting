<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<spring:url value="/resources/images/approve.png" var="defaultImg" />
<script type="text/javascript" src="${valid}"></script>
<style>
@media (max-width:480px) {
			.breadcrumb{margin-top: 15%;}
		}
</style>
<div class="breadcrumb">
	<h3>Sales Voucher</h3>	
</div>	
<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
</c:if>
<c:if test="${errorMsg != null}">
	<div class="errorMsg" id = "errorMsg"> 
		<strong>${errorMsg}</strong>		
	</div>
</c:if>
<c:if test="${flagFailureList==true}">	
	<div class="col-md-12 text-center" >
		<button  id="add" type="button" onclick = "add()">
			Add New Sales Entry
		</button>
	</div>
</c:if> 
	<div class = "borderForm" >
			<!-- MOBILE VIEW TABLE -->
		
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			 <!-- <thead>
			 <tr>
				 <th class='test' >Action</th>	
				 <th  data-field="Sales Voucher Number" data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="Bill Date" data-filter-control="input" data-sortable="true" >Bill Date</th>
				<th  data-field="Customer Name" data-filter-control="input" data-sortable="true" >Customer Name</th>
				<th  data-field="Round Off" data-filter-control="input" data-sortable="true" >Total Amount</th>
		
			</tr>
			</thead> -->
			<tbody>
			<c:forEach var = "list" items = "${entryList}">
			<c:choose>
				<c:when test="${list.entry_status==3}">
					<tr class='sales-disabled'>
					<td style="text-align: left;">
						<fmt:parseDate value="${list.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="customer_bill_date" type="date" pattern="dd-MM-yyyy"/>
						${customer_bill_date}<br/>
						${list.voucher_no}<br/>
						  <c:choose>
			                    <c:when test='${list.sale_type==1}'>Cash Sales</c:when>			                   
			                   <c:when test='${list.sale_type==2}'>Card Sales - ${list.bank.bank_name} - ${list.bank.account_no}</c:when>			                    
			                    <c:otherwise>${list.customer.firm_name} </c:otherwise>
                  		  </c:choose>
                  	</td>					
					<td style="text-align: right;"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${list.round_off+list.tds_amount}" /></td>	
					<td style="text-align: left;">
					          <%--   <c:if test="${access_View == true}"> --%>
								<i  id='view_${list.sales_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;" onclick = "viewSalesEntry('${list.sales_id}')" class="acs-view fa fa-search"></i>
								<%-- </c:if> --%>
								<c:forEach var = "year" items = "${yearEndlist}">
								<c:if test="${list.accountingYear.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">	
									<i  id='update_${list.sales_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" onclick = "activateSalesEntry('${list.sales_id}')" class="acs-check fa fa-check-circle" ></i>
								</c:if>
								</c:forEach>
					</td>	
                     </tr>
				</c:when>
				<c:otherwise>
				<tr>
					     <td style="text-align: left;">
							<fmt:parseDate value="${list.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
		                    <fmt:formatDate value="${parsedDate}" var="customer_bill_date" type="date" pattern="dd-MM-yyyy"/>
							${customer_bill_date}<br/>
							${list.voucher_no}<br/>
							  <c:choose>
				                    <c:when test='${list.sale_type==1}'>Cash Sales</c:when>			                   
				                   <c:when test='${list.sale_type==2}'>Card Sales - ${list.bank.bank_name} - ${list.bank.account_no}</c:when>			                    
				                    <c:otherwise>${list.customer.firm_name} </c:otherwise>
	                  		  </c:choose>
                  		 </td>					
					    <td style="text-align: right;"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${list.round_off+list.tds_amount}" /></td>	
						<td style="text-align: left;">
							<%--  <c:if test="${access_View == true}"> --%>
								<i  id='view_${list.sales_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;" onclick = "viewSalesEntry('${list.sales_id}')" class="acs-view fa fa-search" ></i>
							<%-- 	</c:if> --%>
								<c:forEach var = "year" items = "${yearEndlist}">
								<c:if test="${list.accountingYear.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">
								<%--  <c:if test="${access_Update == true}"> --%>
								<i  id='update_${list.sales_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" onclick = "editSalesEntry('${list.sales_id}')" class="acs-update fa fa-pencil" ></i>
							<%-- 	</c:if> --%>
								 <%-- <c:if test="${access_Delete == true}"> --%>
								<i  id='delete_${list.sales_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" onclick = "deleteSalesEntry('${list.sales_id}')" class="acs-delete fa fa-times" ></i>
									</c:if>
									<%-- </c:if> --%>
								</c:forEach>	
					     </td>	
                 </tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
			</tbody>
		</table>
	</div>
	 <c:if test="${flagFailureList==false}">	
	<div class="row" style="text-align: center; margin: 15px;">
		<button class="fassetBtn" type="button" onclick="back()">
			<spring:message code="btn_back" />
		</button>
	</div>
	</c:if>

<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);	
    
    setTimeout(function() {
        $("#errorMsg").hide()
    }, 3000);
    $("#bulk_upload").hide();
});
	function viewSalesEntry(id){
		window.location.assign('<c:url value="viewSalesEntry"/>?id='+id);
	}
	
	function add(){
		 var opening_flag='<c:out value="${opening_flag}"/>';
		
		 if(opening_flag==0)
			 {
				if (confirm("Please upload your opening balance first. Press OK to add Opening Balance") == true) {
					window.location.assign('<c:url value="subledgerOpeningList"/>');
					} 
				else
					{
					return false;
					}
			 return false;
			 }
		 else
			 {
		window.location.assign('<c:url value="salesEntry"/>');
			 }
	}
	
	function editSalesEntry(id){
		window.location.assign('<c:url value="editSalesEntry"/>?id='+id);
	}
	
	function deleteSalesEntry(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteSalesEntry"/>?id='+id);
		 } 

	}
	/* function activateSalesEntry(id){
		 if (confirm("Are you sure you want to Activate record?") == true) {
			window.location.assign('<c:url value="activateSalesEntry"/>?id='+id);
		 } 
	} */
	
	function uploaddiv(){
		 var opening_flag='<c:out value="${opening_flag}"/>';
		 if(opening_flag==0)
			 {
				if (confirm("Please upload your opening balance first. Press OK to add Opening Balance") == true) {
					window.location.assign('<c:url value="subledgerOpeningList"/>');
					} 
				else
					{
					return false;
					}
			 return false;
			 }
		 else
			 {
		$("#bulk_upload").show();
			 }
	}
	function hideuploaddiv(){
		$("#bulk_upload").hide();
	}
	
	function salesEntryFailure(){
		window.location.assign('<c:url value="salesEntryFailure"/>');
	}

	$(document).ready(function () {
     var access_Insert;
     var access_Update;
     var access_View;
     var access_Delete;
	 var menuid=localStorage.getItem("menu_aid");
	 <c:forEach var = "menu" items = "${menuList}">
		var id='${menu.menu_Id}';
		if(id==menuid){
			
			var access_Insert='${menu.access_Insert}';
			var access_Update='${menu.access_Update}';
			var access_View='${menu.access_View}';
			var access_Delete='${menu.access_Delete}';
		   
			
			 <c:forEach var = "entry" items = "${entryList}">
			 
			 if(access_Insert=='false')
	      		{
					
					 
					var link = document.getElementById("add");
					link.style.display = 'none';
					
	      		}
			if(access_View=='false')
      		{
				
				 var ID='view_${entry.sales_id}';
				var link = document.getElementById(ID);
				link.style.display = 'none';
				
      		}
			if(access_Update=='false')
      		{
				
				 var ID='update_${entry.sales_id}';
				var link = document.getElementById(ID);
				link.style.display = 'none';
				
      		}
			if(access_Delete=='false')
      		{
				
				 var ID='delete_${entry.sales_id}';
				var link = document.getElementById(ID);
				link.style.display = 'none';
      		}
			 </c:forEach>
			
		}
    </c:forEach>
		document.getElementById('progress-div').style.display="none";
		$("#file-div").show();	
		var file='<c:out value="${successMsg}"/>';
	     if(file=="NA")
			{
				   $('#file-model').modal({
				    	show: true,
				   	});	
			}
	});
	
	function validate(){
		
		var validFileExtensions = [".xls", ".xlsx"];
		var filePath = document.getElementById("excelFile").value;
		if(filePath == null || filePath == ""){
			alert("Please select excel file");
			return false;
		}
		else{
			var blnValid = false;
		    for (var j = 0; j < validFileExtensions.length; j++) {
		        var sCurExtension = validFileExtensions[j];
		        if (filePath.substr(filePath.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		            blnValid = true;
		            break;
		        }
		    }
		    
		    if (!blnValid) {
		        alert("File format should be " + validFileExtensions.join(","));
		        $("#excelFile").val('');
		        return false;
		    }
		}
		document.getElementById('progress-div').style.display="block";
		$("#file-div").hide();
		return true;
	}
	function back(){
		window.location.assign('<c:url value = "salesEntryList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>