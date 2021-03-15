<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<div class="breadcrumb text-center">
	<h3>Receipt</h3>
</div>
<div class="col-md-12">	
	<c:choose>
		<c:when test="${(filemsg != null)}">
				<!-- Small modal -->
			<div id="file-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Import Status</h4>
	               		</div>
		   				<div class="modal-body">
			   				<c:if test="${successVocharList.size()==0 && failureVocharList.size()==0}">
			   					<label style="color:red;">${filemsg}</label>
			   				</c:if>
		   					<c:if test="${successVocharList.size()!=0 && failureVocharList.size()!=0}">
		   						<label style="color:green;">${filemsg}</label>
								<c:forEach var = "success" items = "${successVocharList}">
									<ul><li>${success}</li></ul>
				                </c:forEach>
				                	<label style="color:red;">${filemsg1}</label>
				                <c:forEach var = "fail" items = "${failureVocharList}">
					            	<ul><li> ${fail}</li></ul>
				            	</c:forEach>
			   				</c:if>
			   				<c:if test="${successVocharList.size()!=0 && failureVocharList.size()==0}">
			   					<label style="color:green;">${filemsg}</label>
								<c:forEach var = "success" items = "${successVocharList}">
									<ul><li>${success}</li></ul>
				                </c:forEach>
			   				</c:if>
			   				<c:if test="${successVocharList.size()==0 && failureVocharList.size()!=0}">
				                <label style="color:red;">${filemsg1}</label>
				                <c:forEach var = "fail" items = "${failureVocharList}">
					            	<ul><li> ${fail}</li></ul>
				                </c:forEach>
			   				</c:if>
						</div>
						<div class="modal-footer">
	 						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	 					</div>
	       			</div><!-- /.modal-content -->
	      		</div>
			</div>
			<!-- /.modal -->     
			
		</c:when>
		<c:otherwise>
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
		</c:otherwise>
	</c:choose>
	<c:if test="${flagFailureList==true}">	
		<div class="col-md-12 text-center">
			<c:if test="${importflag==false}">
				<button  id="add" type="button" onclick = "add()">Add New Receipt</button>
			</c:if>	
			<c:if test="${importflag==true}">
				<button  id="add" type="button" onclick = "checkImportReceipt()">Add New Receipt</button>
			</c:if>	
			<c:if test="${importflag==true}">
			 	<button class="fassetBtn" type="button" onclick = "uploaddiv()">Import Excel</button>
			</c:if>
		</div>
	</c:if>
	<div id="bulk_upload" class='row' style="display:none" >
		<div id="progress-div" style="text-align:center;display:none">
			<img src="${processing}" style="height:80px"/>
			<h4>File uploading is in progress...</h4>
		</div>
		<div class="text-center" id="file-div">
			<form:form  action="importExcelReceipt" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
		    	<p>Please download template from here to import <a href="resources/templates/Receipt Voucher-Template.xlsx" style='font-weight:bold'> Download </a> </p>
    			<div style = "text-align: center;">			    				
					<label for="excelFile">Select Excel File:</label>	
					<input id = "excelFile" name="excelFile" type="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" style = "display: inline; margin: 10px 0px 10px 0px;">	
    			</div>
    			<div class="form-inline" style="margin-top:2%" >
					<div class="form-group">
						<input type ="submit" class="logBt btn btn-primary" value="<spring:message code="btn_upload"/>">				
					</div>
					<div class="form-group" style="margin-left: 30px;">
						<input class="logBt btn btn-primary" type="button" onclick = "hideuploaddiv()" id = "btnCancel" value="<spring:message code="btn_cancel"/>">				
					</div>
				</div>
			</form:form>
		</div>
	</div>	
	<div class="clearfix"></div>
	<div class = "borderForm" >
	<div class="table-responsive">
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
				<th  data-field="Voucher" data-filter-control="input" data-sortable="true" >Voucher No</th>
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="customer" data-filter-control="input" data-sortable="true" >Particulars</th>
						
				<th  data-field="Receipt" data-filter-control="input" data-sortable="true" >Total Amount</th>
				<th  data-field="type" data-filter-control="input" data-sortable="true" >Receipt Type</th>		
				<th  data-field="Status" data-filter-control="input" data-sortable="true" >Status</th>				
						
			</tr>
			</thead> -->
			<tbody>	
				<c:forEach var = "receipt" items = "${receiptList}">
				<tr class='sales-disabled'>
					
                    <td style="text-align: left;">
                    	<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   		<fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />
                   		${date}<br/>
                   		${receipt.voucher_no}${receipt.customer_id}</br/>
                   		<c:choose>
		                    <c:when test='${receipt.customer==null}'>${receipt.subLedger.subledger_name }</c:when>
		                    <c:otherwise>${receipt.customer.firm_name } </c:otherwise>
	                    </c:choose>
                    </td>
                  	<td style="text-align: right;">
					     <c:choose>
							<c:when test="${receipt.tds_paid==true}">
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount+receipt.tds_amount}" />
							</c:when>
							<c:otherwise>
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
							</c:otherwise>
					   	</c:choose>
				    </td>
                    <td  style="text-align: left;">	
                         <c:if test="${receipt.payment_type!=null}">
                            <c:choose>
								<c:when test="${receipt.payment_type==1}">Cash</c:when>
								<c:when test="${receipt.payment_type==2}">Cheque</c:when>
								<c:when test="${receipt.payment_type==3}">DD</c:when>
								<c:otherwise>NEFT/RTGS/Net banking/Account Transfer</c:otherwise>
							</c:choose>	
						</c:if>	
					</td>
                   <%-- <td style="text-align: left;">${receipt.entry_status==3 ? "Disable" : "Enable"}</td> --%>
                   <c:choose>
							<c:when test="${receipt.entry_status==3 || receipt.entry_status==4 || receipt.entry_status==2}">
								<c:if test="${receipt.entry_status==3 || receipt.entry_status==4}">
									
									<td style="text-align: left;">
									    <i   id='view_${receipt.receipt_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;" onclick = "viewReceipt('${receipt.receipt_id}')" class="acs-view fa fa-search" ></i>
									</td>
							  	</c:if>
							  	<c:if test="${receipt.entry_status==2}">
									<td style="text-align: left;">
									    <i  id='view-ico' onclick = "viewReceipt('${receipt.receipt_id}')" class="acs-view fa fa-search" ></i>
									</td>
							   	</c:if>
							</c:when>
							<c:otherwise>
								<td style="text-align: left;">
									<i   id='view_${receipt.receipt_id}' style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%;" onclick = "viewReceipt('${receipt.receipt_id}')" class="acs-view fa fa-search" ></i>
									<c:forEach var = "year" items = "${yearEndlist}">
										<c:if test="${receipt.accountingYear.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">
											<c:if test="${receipt.advreceipt==null}">
												<i  id='update_${receipt.receipt_id}' style="cursor: pointer;color:green;font-size: 15px;margin: 0px 3%;" onclick = "editReceipt('${receipt.receipt_id}')" class="acs-update fa fa-pencil"></i>		
											</c:if>	
											<i  id='delete_${receipt.receipt_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;"onclick = "deleteReceipt('${receipt.receipt_id}')" class="acs-delete fa fa-times" ></i>
										</c:if>
									</c:forEach>
								</td>
							</c:otherwise>
					</c:choose>		
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
	<c:if test="${flagFailureList==false}">	
	<div class="row" style="text-align: center; margin: 15px;">
		<button class="fassetBtn" type="button" onclick="back()">
			<spring:message code="btn_back" />
		</button>
	</div>
	</c:if>
</div>
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
	function viewReceipt(id){
		window.location.assign('<c:url value="viewReceipt"/>?id='+id);
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
		window.location.assign('<c:url value="receipt"/>');
			 }
	}
	
	function checkImportReceipt(){
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
			 alert("importflag after is :"+importflag);
			 if (confirm("Are you sure that you have imported all transactions.") == true) {
					window.location.assign('<c:url value="receipt"/>');
					} 
				else
					{
					return false;
					}
			 }
	}
	
	
	function editReceipt(id){
		window.location.assign('<c:url value="editReceipt"/>?id='+id);
	}
	
	function deleteReceipt(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteReceipt"/>?id='+id);
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
	
	function receiptFailure(){
		window.location.assign('<c:url value="receiptFailure"/>');
	}
	
	$(document).ready(function () {
		
	  /*  $.ajax({
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
	    });	     */
	    
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
			   
				
				 <c:forEach var = "entry" items = "${receiptList}">
				 
				 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
		      		}
				if(access_View=='false')
	      		{
					
					 var ID='view_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Update=='false')
	      		{
					
					 var ID='update_${entry.receipt_id}';
					var link = document.getElementById(ID);
					link.style.display = 'none';
					
	      		}
				if(access_Delete=='false')
	      		{

					var ID='delete_${entry.receipt_id}';
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
		window.location.assign('<c:url value = "receiptList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>