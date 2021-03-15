<%@include file="/WEB-INF/includes/header.jsp"%>


<div class="breadcrumb">
	<h3>GST Report 1</h3>
	<a href="homePage">Home</a> 
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="GSTReport1Form" action="gstReport1" method="post" commandName="form" onsubmit="return checkdata();">
			<div class="row">
				<form:input path="fromDate" id="from_date" hidden="hidden" />
				<form:input path="toDate" id="to_date" hidden="hidden" />
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>From Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="frmDate" name="frmDate" onchange="setDate(this)" placeholder="From Date" autocomplete="off">
					<span class="logError"><form:errors path="fromDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>To Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="toDate" name="toDate" onchange="setDate1(this)" placeholder="To Date" autocomplete="off"> 
					<span class="logError"><form:errors path="toDate" /></span>
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
		$( "#frmDate" ).datepicker();
		$( "#toDate" ).datepicker();
	});

	function setDate(e){
		document.getElementById("from_date").value = e.value;		
	}
	
	function setDate1(e){
		document.getElementById("to_date").value = e.value;		
	}
	
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
	
	function checkdata(){
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("toDate").value;

		var date1 = new Date(frmDate);
		var date2 = new Date(toDate);		
		   if(frmDate=="")
			{
			alert("Please Select From Date");
			return false;
			}
		  else if(toDate=="")
			{
			alert("Please Select To Date");
			return false;
			}
			else if(date1 > date2)
			{				
				alert("From Date Cann't be greater than To Date");
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