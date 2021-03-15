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
	<h3>GST Auto JV</h3>
	<a href="homePage">Home</a>
</div>
	<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
		</c:if>
<div class="col-md-12">

	<div class="col-md-12 text-center">
		<button id="add" type="button" onclick = "add()">
			Add New GST Auto JV
		</button>
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
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "payroll" items = "${gstAutoJvList}">
			<tr>
					<td style="text-align: left;">
					 <i  id='view_${payroll.gst_id}'  style="cursor: pointer;color:#2e3092;font-size: 15px;margin: 0px 3%"
							  onclick = "viewGST('${payroll.gst_id}')"  class="acs-view fa fa-search" ></i>		
					 <c:forEach var = "year" items = "${yearEndlist}">
						   <c:if test="${payroll.accountingYear.year_id==year.accountingYear.year_id && year.yearEndingstatus==1}">	
						   	 <i  id='delete_${payroll.gst_id}' style="cursor: pointer;color:#ed1c24;font-size: 15px;margin: 0px 3%;" 
							  onclick = "deleteGST('${payroll.gst_id}')"  class="acs-delete fa fa-times" ></i>
					  </c:if>
				   </c:forEach>
					</td>			
					<td style="text-align: left;">${payroll.voucherSeries.voucher_no}</td>
					<td style="text-align: left;"><fmt:parseDate value="${payroll.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />
                    ${date}</td>	
				</tr>
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
   
});
	function viewGST(id){
		window.location.assign('<c:url value="viewGstAutoJV1"/>?id='+id);
	}
	function deleteGST(id){
		 if (confirm("Are you sure you want to delete record?") == true) {
			 window.location.assign('<c:url value="deleteGSTAutoJV"/>?id='+id);
				 } 
		
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
		window.location.assign('<c:url value="gstAutoJvDateSelection"/>');
			 }
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

	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>