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
	<h3>Payroll Auto JV</h3>
	<a href="homePage">Home</a> » <a href="payrollAutoJVList">Payroll Auto JV</a>
</div>
	<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
		</c:if>
<div class="col-md-12">

	<div class="col-md-12 text-center">
		<button id="add" type="button" onclick = "add()">
			Add New Payroll Auto JV
		</button>
		 <button class="fassetBtn" type="button" onclick = "uploaddiv()">
					Import Payroll Auto JV
		</button>	
	</div>

	<div id="bulk_upload" class='row' style="display:none">
				<div id="progress-div" style="text-align:center;display:none">
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
			<form:form  action="importExcelPayroll" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    		<p>Please download template from here to import <a href="resources/templates/payrollNew.xlsx" style='font-weight:bold'>Download </a> </p>	
			    			
			    			<div style = "text-align: center;">			    				
								<label for="excelFile">Select Excel File:</label>	
								<input id = "excelFile" name="excelFile" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" style = "display: inline; margin: 10px 0px 10px 0px;">	
			    			</div>
			    			<div class="form-inline" style="margin-top:2%" >
								<div class="form-group">
									<input type ="submit" class="logBt btn btn-primary" value="<spring:message code="btn_upload" />">				
								</div>
								<div class="form-group" style="margin-left: 30px;">
									<input class="logBt btn btn-primary" type="button" onclick = "hideuploaddiv()" id = "btnCancel" value="<spring:message code="btn_cancel"/>">				
								</div>
							</div>
						</form:form>
			</div>
	</div>	
	<div class = "borderForm" >
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
			<thead>
			<tr>
				<th class='test' >Action</th>
				<th  data-field="voucherNumber" data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="drAmount" data-filter-control="input" data-sortable="true" >Amount</th>			
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "payroll" items = "${payrollList}">
			<c:set var="total_amount" value="0" />
			<%-- <c:if test="total_amount" >'0'> --%>
			<tr>
					
					<td style="text-align: left;">
					 <i  id='view_${payroll.payroll_id}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%"
							  onclick = "viewPayroll('${payroll.payroll_id}')"  class="acs-view fa fa-search" ></i>	
							  
							  <c:forEach var = "year" items = "${yearEndlist}">
							    <c:if test="${payroll.accounting_year_id.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">
							
							  <i  id='update-ico' onclick = "editpayrollAutoJV('${payroll.payroll_id}')" class="acs-update fa fa-pencil" ></i>
					
						   	 <i  id='delete_${payroll.payroll_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
							  onclick = "deletePayroll('${payroll.payroll_id}')"  class="acs-delete fa fa-times" ></i>
							  </c:if>
					 </c:forEach>
				
					</td>			
					<td style="text-align: left;">${payroll.voucherSeries.voucher_no}</td>
					<td style="text-align: left;"><fmt:parseDate value="${payroll.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />${date}</td>


                     <c:forEach var = "subledger" items = "${payroll.payrollSubledgerDetails}">  
                      <c:set var="total_amount" value="${total_amount + subledger.drAmount}" />
					</c:forEach>	
					 <td style="text-align: right;">${total_amount}</td>			                    
                     	
				</tr>
			<%-- 	</c:if> --%>
			</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
    $("#bulk_upload").hide();
});
	function viewPayment(id){
		window.location.assign('<c:url value="viewPayrollAutoJV"/>?id='+id);
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
		window.location.assign('<c:url value="payrollAutoJV"/>');
			 }
	}
	
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
			   
				
				 <c:forEach var = "entry" items = "${paymentList}">
				 
				 if(access_Insert=='false')
		      		{
						var link = document.getElementById("add");
						link.style.display = 'none';
		      		}
				if(access_View=='false')
	      		{
					 var ID='view_${entry.payment_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${entry.payment_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{
					
					 var ID='delete_${entry.payment_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
	      		}
				 </c:forEach>
				
			}
	    </c:forEach>
		
	/*    $.ajax({
	        type: 'POST',
	        url: 'getActionAccess?menuid='+menuid,
	        async:false,
            contentType: 'application/json',
		      	success: function (data){  
		      	
		      		if(data["access_Insert"]==true)
		      		{
		      		    $("button").removeClass("acs-insert");
		      		}
		      		if(data["access_Update"]==true)
		      		{
		      		    $("i.update-ico").removeClass("acs-update");
		      		}
		      		if(data["access_View"]==true)
		      		{
		      		    $("i.view-ico").removeClass("acs-view");
		      		}
		      		if(data["access_Delete"]==true)
		      		{
		      		    $("i.delete-ico").removeClass("acs-delete");
		      		}
		      		
		      	},
		        error: function (e) {
		            console.log("ERROR: ", e);
		        },
		        done: function (e) {
		            console.log("DONE");
		        }        
	    }); */	     
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
	function deletePayroll(id){
		window.location.assign('<c:url value="deletePayroll"/>?id='+id);
	}
	
	function viewPayroll(id){
		window.location.assign('<c:url value="viewPayroll"/>?id='+id);
	}
	function editpayrollAutoJV(id){
		window.location.assign('<c:url value="editpayrollAutoJV"/>?id='+id);
	}
	
	
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
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>