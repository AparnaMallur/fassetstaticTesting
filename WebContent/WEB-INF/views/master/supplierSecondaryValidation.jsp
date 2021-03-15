<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/approve.png" var="approveImg" />
<spring:url value="/resources/images/reject.png" var="rejectImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<div class="breadcrumb">
	<h3>Supplier Secondary Validation</h3>					
	<a href="homePage">Home</a> » <a href="#">Supplier Secondary Validation</a>
</div>	
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
				<th><input type='checkbox' id='allcb' name='allcb' onchange="checkall(this)"/> </th>			
				<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>	
				<th  data-field="Supplier Name" data-filter-control="input" data-sortable="true" >Supplier Name</th>
				<th data-field="Supplier Company Name" data-filter-control="input" data-sortable="true" >Supplier Company Name</th>	
				<th data-field="Status" data-filter-control="input" data-sortable="true" class='test' >Change Status</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "supplier" items = "${supplierList}">
				<tr>
					<td><input name="32-ck" id="32-ck" value="${supplier.supplier_id}" type="checkbox" onchange="addPro(this)">
					</td>
					<td style="text-align: left;">${supplier.company.company_name}</td>
					<td style="text-align: left;">${supplier.contact_name}</td>
					<td style="text-align: left;">${supplier.company_name}</td>
					<td style="text-align: left;">
						<i  id='view-ico' onclick = "viewSupplier('${supplier.supplier_id}')" class="acs-view fa fa-search" ></i>
						<a href = "#" onclick = "approveSupplier('${supplier.supplier_id}')" title="Approve"><img src='${approveImg}' style = "width: 20px;"/></a>
						<a href = "#" onclick = "rejectSupplier('${supplier.supplier_id}')" title="Reject"><img src='${rejectImg}' style = "width: 20px;"/></a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<div class="row">
			<form:form id="subledgerForm" action="Batchsupplier" method="post"
				commandName="supplierbatch">
				<form:input path="supplierList" id="supplierList" hidden="hidden" />
				<form:input path="primaryApproval" id="primaryApproval"
					hidden="hidden" />
				<form:input path="rejectApproval" id="rejectApproval"
					hidden="hidden" />
				<div class="col-md-12 text-center">
					<button class="fassetBtn" type="submit" onclick="return ApproveFlag()">
						Approve By Batch</button>
					<button class="fassetBtn" type="submit" onclick="return RejectFlag()">
						Reject By Batch</button>
						</div>
			</form:form>
		</div>
		
	</div>

<script type="text/javascript">
var proList = [];
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});

$(function() {		
    setTimeout(function() {
        $("#errorMsg").hide()
    }, 3000);
});

	function viewSupplier(id){
		window.location.assign('<c:url value="viewSupplier"/>?id='+id+'&flag='+3);
	}
	
	function editSuppliers(id){
		window.location.assign('<c:url value="editSuppliers"/>?id='+id);
	}	
	
	function approveSupplier(id){
		window.location.assign('<c:url value="approveSupplier"/>?id='+id+"&primaryApproval="+false);
	}
	
	function rejectSupplier(id){
		if (confirm("Are you sure you want to Reject record?") == true) {
			window.location.assign('<c:url value="rejectSupplier"/>?id='+id+"&rejectApproval="+false);
		    } 
	}
	
	function ApproveFlag(){
		if (confirm("Are you sure you want to Approve records?") == true) {
				$("#supplierList").val(proList);
				$("#primaryApproval").val(false);
		}	
		else
			return false;
	}
	
	function RejectFlag(){
		if (confirm("Are you sure you want to Reject records?") == true) {
			$("#supplierList").val(proList);
			$("#rejectApproval").val(false);
		}
		else
			return false;
	}
	
	function addPro(product) {
		var proValue = product.value;
		if (product.checked) {
			proList.push(proValue);
			console.log(proList);
		} else {
			for (i = 0; i < proList.length; i++) {
				if (proList[i] == proValue) {
					proList.splice(i, 1);
					break;
				}
			}
			console.log(proList);
		}
	}
	function checkall(e)
	{		
		 if($(e).prop('checked')){
	        $('tbody tr td input[type="checkbox"]').each(function(){
	            $(this).prop('checked', true);
	            addPro(this);
	        });
	    }else{
	        $('tbody tr td input[type="checkbox"]').each(function(){
	            $(this).prop('checked', false);
	            addPro(this);
	        });
	    }
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>