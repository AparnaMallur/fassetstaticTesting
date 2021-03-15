<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />


<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>GST Master</h3>
	<a href="homePage">Home</a> » <a href="#">GST</a>
</div>	
<div class="col-md-12" >	
<c:choose>
	<c:when test="${isImport == true}">
				<!-- Small modal -->
			<div id="file-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Import Status</h4>
	               		</div>
		   				<div class="modal-body">
		   				<c:if test="${successList.size()==0 && failureList.size()==0 && updateList.size()==0}">
		   				<label style="color:red;">${error}</label>
		   				</c:if>
		   				
		   				<c:if test="${successList.size()!=0}">
		   				<label style="color:green;">${successImportmsg}</label>
							<c:forEach var = "success" items = "${successList}">
							<ul><li>${success}</li></ul>
			                </c:forEach>
			       
		   				</c:if>
		   				<c:if test="${updateList.size()!=0}">
		   				<label style="color:green;">${updatemsg}</label>
							<c:forEach var = "success" items = "${updateList}">
							<ul><li>${success}</li></ul>
			                </c:forEach>
		   				</c:if>
		   				<c:if test="${failureList.size()!=0}">
		   				<label style="color:red;">${failmsg}</label>
							<c:forEach var = "success" items = "${failureList}">
							<ul><li>${success}</li></ul>
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
	</c:otherwise>
	
</c:choose>
<div class="col-md-12  text-center">
		<button id="add" type="button" id = "btnAdd" onclick = "addGst()">
			Add New GST
		</button>
						
			<button class="fassetBtn" type="button" onclick = "uploaddiv()">
						Import Excel
			</button>
			<!-- <button class="fassetBtn" type="button" onclick = "uploaddiv()">
						Import Excel
			</button> -->
		<%-- <c:if test="${importfail==true}">		</c:if>		 --%>
			<!-- <button class="fassetBtn" type="button" onclick = "gstimportfailure()">
						Failure List
			</button> -->
	
	</div>
	
	
	<div id="bulk_upload" class='row' style="display:none" >
				<div id="progress-div" style="text-align:center;display:none" >
					<img src="${processing}" style="height:80px"/>
					<h4>File uploading is in progress...</h4>
				</div>
			<div class="text-center" id="file-div">
				<form:form  action="importExcelGST" method="post" enctype="multipart/form-data" onsubmit = "return validate();">
			    	
			    		<p>Please download template from here to import <a href="resources/templates/GST-template.xlsx" style='font-weight:bold'> Download </a> </p>	
			    				    			
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
	
	

	
	<div class = "borderForm">
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
				<th  data-field="HSN" data-filter-control="input" data-sortable="true" >HSN/SAC No</th>	
				<th  data-field="description" data-filter-control="input" data-sortable="true" >Description</th>		
		 			<th  data-field="Chapter" data-filter-control="select" data-sortable="true" >Chapter</th>	 
		 				<th  data-field="Schedule" data-filter-control="select" data-sortable="true" >Schedule</th>	 
		 				<th   data-field=gstrate data-filter-control="input" data-sortable="true" >GST-Rate</th>
		 				<th   data-field="start" data-filter-control="input" data-sortable="true" >Start Date</th>
		 		<th  data-field="end" data-filter-control="input" data-sortable="true" >End Date</th>
		 		<th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 
		 	
			</tr>
			</thead>
			<tbody>	
			<c:forEach var = "gst" items = "${gstList}">
				<tr>
					<td style="text-align: left;">
							    <i  id='view-ico' onclick = "viewGST('${gst.tax_id}')" class="fa fa-search" ></i>
								<i  id='update-ico' onclick = "editGST('${gst.tax_id}')" class="fa fa-pencil" ></i>
								<%-- <i  id='delete-ico' onclick = "deleteGST('${gst[0]}')" class="fa fa-times" ></i> --%>	
					</td>   
					<td style="text-align: left;">${gst.hsc_sac_code}</td>
					<td style="text-align: left;">${gst.description}</td>					
				
                    <td> ${gst.chapter.chapterNo}</td>
                      <td> ${gst.schedule.scheduleName} </td>
                          <td  class="tright">${gst.igst}</td> 
                   
                      	<td style="text-align: left;">
					<fmt:parseDate value="${gst.start_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="startdate" type="date" pattern="dd-MM-yyyy" />
                    ${startdate}</td>
					<td style="text-align: left;">
					<fmt:parseDate value="${gst.end_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="enddate" type="date" pattern="dd-MM-yyyy" />
                    ${enddate}</td>
                
					<td style="text-align: left;">${gst.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</c:forEach> 	
			</tbody>
		</table>	

	</div>
</div>
<script>
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
    /* 	function PrevList()
	{
		window.location.assign('<c:url value="gstprevList"/>');
	}
	function NextList()
	{
		window.location.assign('<c:url value="gstNextList"/>');
	} */
	function addGst(){
		window.location.assign('<c:url value="addGST"/>');
	}
	
	function editGST(taxId) {
		
		window.location.assign('<c:url value = "editGST"/>?id='+taxId);	
	}

	function deleteGST(taxId) {
		 if (confirm("Are you sure you want to delete record?") == true) {
		window.location.assign('<c:url value="deleteGST"/>?id='+taxId);
		 } 
	}
	
	function viewGST(taxId){
		window.location.assign('<c:url value="viewGstAutoJV"/>?id='+taxId);
	}
	
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
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
			   
				
				 <c:forEach  items = "${gstList}">
				 
				 if(access_Insert=='false')
		      		{
						var link = document.getElementById("add");
						link.style.display = 'none';
		      		}
				 </c:forEach>
				
			}
	    </c:forEach>
	   /* $.ajax({
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
		      		    $("td a").removeClass("acs-update");
		      		}
		      		if(data["access_View"]==true)
		      		{
		      		    $("td a").removeClass("acs-view");
		      		}
		      		if(data["access_Delete"]==true)
		      		{
		      		    $("td a").removeClass("acs-delete");
		      		}
		      		
		      	},
		        error: function (e) {
		            console.log("ERROR: ", e);
		        },
		        done: function (e) {
		            console.log("DONE");
		        }        
	    });	    */
	   $('#file-model').modal({
	    	show: true,
//	    	asadnsadhu nukhdukwehuurh uoewu;crhuh
	   	});	
	});
	function uploaddiv(){
		$("#bulk_upload").show();
	}
	function hideuploaddiv(){
		$("#bulk_upload").hide();
	}
	function gstimportfailure(){
		window.location.assign('<c:url value="gstimportfailure"/>');
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