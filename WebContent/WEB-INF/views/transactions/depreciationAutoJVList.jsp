<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<%-- <spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" /> --%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Depreciation Auto Journal  Voucher</h3>
	<a href="homePage">Home</a> » <a href="depreciationAutoJVList">Depreciation Auto Journal  Voucher</a>
</div>	 
<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
			</c:if>
			<div class="col-md-12 text-center" >
<button  id="add" type="button" onclick = "add()">
			Add Depreciation Auto Journal  Voucher
		</button>	
		</div>
<div class="col-md-12">	

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
		
				<th  data-field="voucher" data-filter-control="input" data-sortable="true" >Voucher No</th>					
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>	
				
				<th  data-field="amountdr" data-filter-control="input" data-sortable="true" >Amount(Dr)</th>	
				
			</tr>
			</thead>
			<!--  subledgerList   depreciationAutoJVList-->
			<tbody>
			
			<c:forEach var = "depreciation" items = "${depreciationAutoJVList}">
				<tr>
				<td style="text-align: left;">
					<i  id='view-ico' onclick = "viewDepreciationJournal('${depreciation.depreciation_id}')" class="acs-view fa fa-search" ></i>	
						<i  id='update-ico' onclick = "updateDepreciationJournal('${depreciation.depreciation_id}')" class="acs-update fa fa-pencil" ></i>
					<i  id='delete-ico' onclick = "deleteDepreciationJournal('${depreciation.depreciation_id}')" class="acs-delete fa fa-times" ></i>
				
					</td>
			
				<td style="text-align: left;">${depreciation.voucherSeries.voucher_no}</td>
					<td style="text-align: left;"><fmt:parseDate value="${depreciation.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy"  />${date}</td>	
                    <td class='tright'>
                    	<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${depreciation.amount}" />
                    	
                    	<%-- <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${basicAmount}"  /> --%>
                    	
                    </td>
                    
					
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
		
		function add()
		{
			window.location.assign('<c:url value="depreciationform"/>');
		}

		function updateDepreciationJournal(id){
			window.location.assign('<c:url value="updateDepreciationJournal"/>?id='+id);
		}
		
		
		function viewDepreciationJournal(id){
			window.location.assign('<c:url value="viewDepreciationJournal"/>?id='+id);
		}
		
		function deleteDepreciationJournal(id){
	 		if (confirm("Are you sure you want to delete record?") == true) {
				window.location.assign('<c:url value="deleteDepriciationAutoJV"/>?id='+id);
		 	} 
		}	
		
		</script>
		<%@include file="/WEB-INF/includes/footer.jsp" %>
		
	