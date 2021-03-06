<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/approve.png" var="approveImg" />
<spring:url value="/resources/images/reject.png" var="rejectImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Product Secondary Validation</h3>
	<a href="homePage">Home</a> ? <a href="#">Product Secondary Validation</a>
</div>	
<div class="col-md-12" >	
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
				<th><input type='checkbox' id='allcb' name='allcb' onchange="checkall(this)"/> </th>			
			
				<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="Product" data-filter-control="input" data-sortable="true" >Product</th>
				<th  data-field="Hsn/San Number" data-filter-control="input" data-sortable="true" >HSN/SAC Number</th>
				<th  data-field="Sgst" data-filter-control="input" data-sortable="true" >SGST</th>
				<th  data-field="Cgst" data-filter-control="input" data-sortable="true" >CGST</th>
				<th  data-field="Igst" data-filter-control="input" data-sortable="true" >IGST</th>
				<th  data-field="SCT" data-filter-control="input" data-sortable="true" >CESS</th>
				<th  data-field="Vat" data-filter-control="input" data-sortable="true" >VAT</th>
				<th  data-field="Vatcst" data-filter-control="input" data-sortable="true" >CST</th>
				<th  data-field="Excise" data-filter-control="input" data-sortable="true" >EXCISE</th>
				<th  data-field="UOM" data-filter-control="input" data-sortable="true" >UOM</th>
				<th class='test' >Change Status</th>			
			</tr>
			</thead>
			<tbody>			
			<c:forEach var = "product" items = "${productList}">
				<tr>	
				 	<td><input name="32-ck" id="32-ck" value="${product.product_id}"
												type="checkbox" onchange="addPro(this)"></td>	
					<td style="text-align: left;">${product.company.company_name}</td>
					<td style="text-align: left;">${product.product_name}</td>
					<td style="text-align: left;">${product.hsn_san_no}</td>
					<td style="text-align: left;">${product.gst_id.sgst}</td>
					<td style="text-align: left;">${product.gst_id.cgst}</td>
					<td style="text-align: left;">${product.gst_id.igst}</td>
					<td style="text-align: left;">${product.gst_id.state_comp_cess}</td>
					<td style="text-align: left;">${product.taxMaster.vat}</td>
					<td style="text-align: left;">${product.taxMaster.cst}</td>
					<td style="text-align: left;">${product.taxMaster.excise}</td>
					<td style="text-align: left;">${product.uom.unit}</td>
					<td style="text-align: left;">
						<i  id='view-ico' onclick = "viewProduct('${product.product_id}')" class="acs-view fa fa-search" ></i>
						<a href = "#" onclick = "approveProduct('${product.product_id}')" title="Approve"><img src='${approveImg}' style = "width: 20px;"/></a>
						<a href = "#" onclick = "rejectProduct('${product.product_id}')" title="Reject"><img src='${rejectImg}' style = "width: 20px;"/></a>
					
					</td>	
				</tr>
			</c:forEach>
			</tbody>
		</table>
	
	
		<div class="row">
		<form:form id="productForm" action="Batchproduct" method="post" commandName = "batchproduct">									
			<form:input path="productList" id="productList" hidden="hidden" />
			<form:input path="primaryApproval" id="primaryApproval" hidden="hidden" />
			<form:input path="rejectApproval" id="rejectApproval" hidden="hidden" />
			<div class="col-md-12 text-center">
				<button class="fassetBtn" type="submit" onclick="return ApproveFlag()">
					Approve By Batch
				</button>
				<button class="fassetBtn" type="submit" onclick="return RejectFlag()">
					Reject By Batch
				</button>
			</div>
			
		</form:form>
	  </div>
	  
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

	function viewProduct(id){
		window.location.assign('<c:url value="viewProduct"/>?id='+id+'&flag='+3);
	}
	
	function editProduct(id){
		window.location.assign('<c:url value="editProduct"/>?id='+id);
	}
	
	function approveProduct(id){
		window.location.assign('<c:url value="approveProduct"/>?id='+id+"&primaryApproval="+false);
	}
	
	function rejectProduct(id){
		if (confirm("Are you sure you want to Reject record?") == true) {
			window.location.assign('<c:url value="rejectProduct"/>?id='+id+ "&rejectApproval="+false);
		    } 
	}
	
	function ApproveFlag(){
		if (confirm("Are you sure you want to Approve records?") == true) {
			$("#productList").val(proList);
			$("#primaryApproval").val(false);
		}
		else
			return false;
	}
	
	
	function RejectFlag(){
		if (confirm("Are you sure you want to Reject records?") == true) {
				$("#productList").val(proList);
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