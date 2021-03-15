<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<%-- <spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" /> --%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Manual Journal Voucher</h3>
	<a href="homePage">Home</a> » <a href="manualjournalVoucherList">Manual Journal Voucher</a>
</div>	
<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
			</c:if>
<div class="col-md-12">	
<div class="col-md-12 text-center" >
<button  id="add" type="button" onclick = "add()">
			Add Manual Journal Voucher
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
				
				<th  data-field="amountdr" data-filter-control="input" data-sortable="true" >Amount(Dr)</th>	
				<th  data-field="amountcr" data-filter-control="input" data-sortable="true" >Amount(Cr)</th>	
			</tr>
			</thead>
			<tbody>
			 <c:set var="amount_cr" value="0"/>	
			  <c:set var="amount_dr" value="0"/>	
			<c:forEach var = "journal" items = "${manualjournalVoucherList}">
				<tr>
					<td style="text-align: left;">
					<i  id='view-ico' onclick = "viewJournal('${journal.journal_id}')" class="acs-view fa fa-search" ></i>	
					<i  id='update-ico' onclick = "editJournal('${journal.journal_id}')" class="acs-update fa fa-pencil" ></i>
					<i  id='delete-ico' onclick = "deleteJournal('${journal.journal_id}')" class="acs-delete fa fa-times" ></i>
				
		
					</td>
				   <c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
				<td style="text-align: left;">${journal.company.company_name}</td>
				 </c:if>			
					<td style="text-align: left;">${journal.voucher_no}</td>
					<td style="text-align: left;"><fmt:parseDate value="${journal.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />
                    ${date}</td>	
                    		<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${journal.amount}" /></td>
                    
					  
					         <c:set var="amount_dr" value="${amount_dr + journal.amount}" />
					           	<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${journal.amount}" /></td>
                    
					   <c:set var="amount_cr" value="${amount_cr + journal.amount}" />
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
		window.location.assign('<c:url value="viewJournal"/>?id='+id);
	}
	
	function deleteJournal(id){
 		if (confirm("Are you sure you want to delete record?") == true) {
			window.location.assign('<c:url value="deleteJournal"/>?id='+id);
	 	} 
	}	
	function editJournal(id){
		window.location.assign('<c:url value="editJournal"/>?id='+id);
	}
	function add()
	{
		window.location.assign('<c:url value="manualJournalVoucher"/>');
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