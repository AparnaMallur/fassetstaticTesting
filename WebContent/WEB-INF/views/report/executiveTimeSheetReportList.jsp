<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<div class="breadcrumb">
	<h3>Executive Timesheet Report</h3>					
	<a href="homePage">Home</a> » <a href="#">Executive Timesheet Report</a>
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
			<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
			<th  data-field="Company" data-filter-control="input" data-sortable="true" >Company</th>	
			<th  data-field="Service" data-filter-control="input" data-sortable="true" >Category</th>
			<th  data-field="Total" data-filter-control="input" data-sortable="true" >Total Time in Hrs</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var = "executive" items = "${executiveTimesheetList}">
			<c:set var="total_time" value="0"/>	
			
			<c:forEach var = "detail" items = "${executive.details}">
						<c:set var="total_time" value="${total_time + detail.total_time}" />
		   </c:forEach>
			<tr>
				<td style="text-align: left;">${executive.date}</td>
				<td style="text-align: left;">
					<c:forEach var = "detail" items = "${executive.details}">
						${detail.company.company_name} <br>
					</c:forEach>
				</td>
				<td style="text-align: left;">
					<c:forEach var = "detail" items = "${executive.details}">
						${detail.service.service_name} <br>
					</c:forEach>
				</td>
				<td style="text-align: left;">
				<c:forEach var = "detail" items = "${executive.details}">
					${detail.total_time} <br>
					</c:forEach>
				<%-- 	<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_time}"/><br>
			 --%>	</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
         <div class="row text-center-btn">
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
        </div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

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
	function back(){
		window.location.assign('<c:url value = "executiveTimeSheetReport"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
