<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<%-- <spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" /> --%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>YearEnd Journal Voucher</h3>
	<a href="homePage">Home</a> » <a href="yearEndAutoJVList">YearEnd Journal Voucher</a>
</div>	
<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
			</c:if>
<div class="col-md-12">	
<div class="col-md-12 text-center" >
<button  id="add" type="button" onclick = "add()">
			Add YearEnd Journal Voucher
		</button>	
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
				 <c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
				<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
				 </c:if>	
				<th  data-field="voucher" data-filter-control="input" data-sortable="true" >Voucher No</th>					
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>	
				
				<th  data-field="netLoss" data-filter-control="input" data-sortable="true" >Net Loss</th>	
				<th  data-field="netProfit" data-filter-control="input" data-sortable="true" >Net Profit</th>	
			</tr>
			</thead>
			<tbody>
			 <c:set var="amount_cr" value="0"/>	
			  <c:set var="amount_dr" value="0"/>	
			<c:forEach var = "journal" items = "${yearEndAutoJVList}">
				<tr>
					<td style="text-align: left;">
					<i  id='view-ico' onclick = "viewJournal('${journal.year_end_jVId}')" class="acs-view fa fa-search" ></i>	
					<i  id='delete-ico' onclick = "deleteyearendJournal('${journal.year_end_jVId}')" class="acs-delete fa fa-times" ></i>
				
		
					</td>
				   <c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
				<td style="text-align: left;">${journal.company.company_name}</td>
				 </c:if>			
					<td style="text-align: left;">${journal.voucher_no}</td>
					<td style="text-align: left;"><fmt:parseDate value="${journal.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />
                    ${date}</td>	
                    		<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${journal.netProfit}" /></td>
                    
					  <%--   <c:set var="amount_cr" value="${amount_cr + journal.netProfit}" /> --%>
					         
					           	<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${journal.netLoss}" /></td>
                 <%--    <c:set var="amount_dr" value="${amount_dr + journal.netLoss}" /> --%>
					 
				</tr>
			</c:forEach>
			<%--  <tr>
					<td bgcolor="black">Total</td>
					<td></td>
					<td></td>
					 <c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
				<td></td>
				 </c:if>
						<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${amount_dr}" /></b></td>
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${amount_cr}" /></b></td>
					</tr> --%>
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
	
	function viewJournal(id){
		window.location.assign('<c:url value="viewYearEndJournal"/>?id='+id);
	}
	
	function deleteyearendJournal(id){
 		if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteyearendJournal"/>?id='+id);
	 	} 
	}	
	
	function add()
	{
		window.location.assign('<c:url value="yearEndAutoJV"/>');
	}	
	$(document).ready(function () {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    var access_Insert;
	     var access_Update;
	     var access_View;
	     var access_Delete;
		var menuid=localStorage.getItem("menu_aid");
	   	$.ajax({
	        type: 'POST',
	        url: 'getActionAccess?menuid='+menuid,
	        async:false,
            contentType: 'application/json',
	      	success: function (data){  
	      	
	      		 if(access_Insert=='false')
		      		{
						
						 
						var link = document.getElementById("add");
						link.style.display = 'none';
						
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
		window.location.assign('<c:url value = "manualjournalVoucherList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>