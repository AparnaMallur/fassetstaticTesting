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
	<h3>Quotation</h3>					
	<a href="homePage">Home</a> » <a href="#">Quotation</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="col-md-12 text-center" >
		<!--  <button  class="fassetBtn acs-insert" type="button" onclick = "add()">
			Add Quotation
		</button>-->
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
				<th  data-field="fisrt_name" data-filter-control="input" data-sortable="true" >First Name</th>
				<th  data-field="quoteno" data-filter-control="input" data-sortable="true" >Quotation No</th>
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
				<!-- <th  data-field="time" data-filter-control="input" data-sortable="true" >Time</th> -->
				<th  data-field="company_name" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="email" data-filter-control="input" data-sortable="true" >Email</th>
				<th  data-field="mobile_no" data-filter-control="select" data-sortable="true" >Mobile</th>
				<th  data-field="status" data-filter-control="select" data-sortable="true" >Status</th>	
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "quote" items = "${quoteList}">
				<tr>
					<td style="text-align: left;">
								<i  id='view-ico' onclick = "viewQuotation('${quote[0]}')" class="acs-view fa fa-search" ></i>
							<c:if test="${quote[8]!=true}">
								<i  id='update-ico' onclick = "editQuotation('${quote[0]}')" class="acs-update fa fa-pencil" ></i>
								<%-- <i  id='delete-ico' onclick = "deleteQuotation('${quote[0]}')" class="acs-delete fa fa-times" ></i> --%>	
							</c:if>					
					</td>
					
					<td style="text-align: left;">${quote[1]} 
					 ${quote[2]}</td>	
					 <td style="text-align: left;">${quote[3]}</td>	
					 
					 <td style="text-align: left;">
						<fmt:parseDate value="${quote[9]}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="quotaDate" type="date" pattern="dd-MM-yyyy"/>
						${quotaDate}</td>	
				    <%-- <td style="text-align: left;">${quote[10]}</td> --%>					 
					<td style="text-align: left;">${quote[4]}</td>	
					<td style="text-align: left;">${quote[5]}</td>	
					<td style="text-align: left;">${quote[6]}</td>					
					<td style="text-align: left;">${quote[7]==true ? "Enable" : "Disable"}</td>
					
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
	function viewQuotation(id){
		window.location.assign('<c:url value="viewQuotation"/>?id='+id);
	}
	
	function add(){
		window.location.assign('<c:url value="quotation"/>');
	}
	
	function editQuotation(id){
		window.location.assign('<c:url value="editQuotation"/>?id='+id);
	}
	function deleteQuotation(id){

		 if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteQuotation"/>?id='+id);
		 } 

	}
	$(document).ready(function () {
		var menuid=localStorage.getItem("menu_aid");
	   $.ajax({
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
	    });	     	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
