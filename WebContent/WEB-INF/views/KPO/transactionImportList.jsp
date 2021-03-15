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
	<h3>Transaction Import</h3>					
	<a href="homePage">Home</a> » <a href="#">Transaction Import</a>
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
				<!-- <th  data-field="fisrt_name" data-filter-control="input" data-sortable="true" >Name</th> -->
				<th  data-field="quoteno" data-filter-control="input" data-sortable="true" >Quotation No</th>
				<th  data-field="company_name" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="email" data-filter-control="input" data-sortable="true" >Email Address</th>
				<!-- <th  data-field="amount" data-filter-control="select" data-sortable="true" >Amount</th>	 -->			
				<th  data-field="status" data-filter-control="select" data-sortable="true"  >Status</th>	
			</tr>
			</thead>
			<tbody>
			
			<c:forEach var = "imports" items = "${importList}">
				<tr>
					<%-- <td style="text-align: left;">${imports.quotation_id.first_name} ${imports.quotation_id.last_name}</td>	 --%>
					 <td style="text-align: left;">${imports.quotation_id.quotation_no}</td>						 
					 <td style="text-align: left;">${imports.quotation_id.company_name}</td>						 
					<td style="text-align: left;">${imports.quotation_id.email}</td>	
				   <%--  <td style="text-align: left;">${imports.amount}</td> --%>				
					<td style="text-align: left;" class='st-quote'>
					<c:choose>
						<c:when test="${imports.service_status==true}">						
								 <input onchange='setStatusflag(this,${imports.quotation_detail_id})' id="activeid" value="${imports.service_status}" name="radioInline${imports.quotation_id}" checked="checked" type="radio"> <label for="inlineRadio1"> Enable</label>
								  <input onchange='setStatusflag(this,${imports.quotation_detail_id})' id="inactiveid" value="${imports.service_status}" name="radioInline${imports.quotation_id}" type="radio"> <label for="inlineRadio2"> Disable </label>
						</c:when>
						<c:otherwise>
							 <input onchange='setStatusflag(this,${imports.quotation_detail_id})' id="activeid" value="${imports.service_status}" name="radioInline${imports.quotation_id}" type="radio"> <label for="inlineRadio1"> Enable</label>
						     <input onchange='setStatusflag(this,${imports.quotation_detail_id})' id="inactiveid" value="${imports.service_status}" name="radioInline${imports.quotation_id}" type="radio" checked="checked" > <label for="inlineRadio2"> Disable </label>
	 				</c:otherwise>
					</c:choose>
						</td>
					
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
function setStatusflag(e,id)
{ 
	
	if(e.id=="activeid")
	{
		var status=true;
	
	}
	else
	{
		var status=false;
	}
    $.ajax({
        type: "post",
        url: "saveservicestatus?Id="+id,
       	data: JSON.stringify(status),
        contentType: 'application/json',
         success: function (data)
        { 
        	 alert(data);
         },
         error: function (e) {
            console.log("ERROR: ", e);
            
        }
    });
	
}
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	
	function deleteQuotation(id){
		window.location.assign('<c:url value="deleteQuotation"/>?id='+id);
	}
	
</script>
<style>
.st-quote{width:18%}
</style>
<%@include file="/WEB-INF/includes/footer.jsp" %>