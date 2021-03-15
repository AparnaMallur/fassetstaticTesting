<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/approve.png" var="approveImg" />
<spring:url value="/resources/images/reject.png" var="rejectImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Bank Secondary Validation</h3>
	<a href="homePage">Home</a> » <a href="#">Bank Secondary Validation</a>
</div>	
<div class="col-md-12">		
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
				<th  data-field="bankName" data-filter-control="input" data-sortable="true" >Bank Name</th>	
				<th  data-field="branchName" data-filter-control="input" data-sortable="true" >Branch Name</th>	
				<th class='test' >Change Status</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "bank" items = "${bankList}">
				<tr>		
					<td><input name="32-ck" id="32-ck" value="${bank.bank_id}" type="checkbox" onchange="addPro(this)">
					</td>			
					<td style="text-align: left;">${bank.company.company_name}</td>	
					<td style="text-align: left;">${bank.bank_name}</td>
					<td style="text-align: left;">${bank.branch}</td>
					<td style="text-align: left;">
						<i  id='view-ico' onclick = "viewBank('${bank.bank_id}')" class="acs-view fa fa-search" ></i>
						<a href = "#" onclick = "approveBank('${bank.bank_id}')" title="Approve"><img src='${approveImg}' style = "width: 20px;"/></a>
						<a href = "#" onclick = "rejectBank('${bank.bank_id}')" title="Reject"><img src='${rejectImg}' style = "width: 20px;"/></a>
					</td>
					
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<div class="row">
			<form:form id="subledgerForm" action="Batchbank" method="post"
				commandName="bankbatch">
				<form:input path="bankList" id="bankList" hidden="hidden" />
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
	function viewBank(id){
		window.location.assign('<c:url value="viewBank"/>?id='+id+'&flag='+3);
	}
	
	function editBank(id){
		window.location.assign('<c:url value="editBank"/>?id='+id);
	}
	
	function approveBank(id){
		window.location.assign('<c:url value="approveBank"/>?id='+id + "&primaryApproval="+false);
	}
	
	function rejectBank(id){
		if (confirm("Are you sure you want to Reject record?") == true) {
			window.location.assign('<c:url value="rejectBank"/>?id='+id + "&rejectApproval="+false);
		    } 
	}
	
	function ApproveFlag(){
		if (confirm("Are you sure you want to Approve records?") == true) {
				$("#bankList").val(proList);
				$("#primaryApproval").val(false);	
		}
		else
			return false;
	}
	
	function RejectFlag(){
		if (confirm("Are you sure you want to Reject records?") == true) {
				$("#bankList").val(proList);
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