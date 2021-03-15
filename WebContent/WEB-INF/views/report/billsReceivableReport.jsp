<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Bills Receivable</h3>
	<a href="homePage">Home</a> » <a href="#">Bills Receivable</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="billsReceivableReportForm" action="showBillsReceivableReport" method="post" commandName="form" onsubmit="return checkdata();">
			<div class="row">
				<form:input path="toDate" id="to_date" hidden="hidden" />
			</div>

		
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="toDate" name="toDate" onchange="setDate1(this)" placeholder="Report Date" autocomplete="off"> 
					<span class="logError"><form:errors path="toDate" /></span>
				</div>
			</div>		
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Customer Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="customerId" class="logInput" name="customerid">
						<form:option value="-1" label="All" />
						<c:forEach var="customer" items="${customerList}">
							<form:option value="${customer.customer_id}">${customer.firm_name}</form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="customerId" /></span>
				</div>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
					<spring:message code="btn_show_report" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$( "#toDate" ).datepicker();
	});


	function setDate1(e){
		document.getElementById("to_date").value = e.value;		
	}
	
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
	
	function checkdata(){
		var toDate=document.getElementById("toDate").value;

		var date2 = new Date(toDate);		
		 if(toDate=="")
			{
			alert("Please Select To Date");
			return false;
			}
			
		else
			{
			return true;
			}
		//frmDate
		//toDate
	}
</script>

<%@include file="/WEB-INF/includes/footer.jsp"%>