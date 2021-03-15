<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Cash/Bank Book</h3>
	<a href="homePage">Home</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="cashBookBankBookReportForm" action="cashBookBankBookReport" method="post" commandName="form" onsubmit="return checkdata();">
			<div class="row">
				<form:input path="fromDate" id="from_date" hidden="hidden" />
				<form:input path="toDate" id="to_date" hidden="hidden" />
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>From Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="frmDate"
						name="frmDate" onchange="setDate(this)" placeholder="From date"
						autocomplete="off"> 
						<%-- <span class="logError"><form:errors path="fromDate" /></span> --%>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>To Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="tDate" name="tDate" onchange="setDate1(this)" placeholder="To date" autocomplete="off">
					<%-- <span class="logError"><form:errors path="toDate" /></span> --%>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Book Type<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="type" class="logInput" id="typeId"
						placeholder="Payment Type">
						<form:option value="0" label="Select Type" />
						<form:option value="2" label="Bank Book" />
						<form:option value="1" label="Cash Book" />

					</form:select>
					<span class="logError"><form:errors path="type" /></span>
				</div>
			</div>

			<div id="BankDiv">
				<div class="row">
					<div class="col-md-2 control-label">
						<label>Bank Name<span>*</span></label>
					</div>
					<div class="col-md-10">
						<form:select path="bankId" class="logInput" id="bankId"
							placeholder="Bank Name">

							<form:option value="0" label="Select Bank" />
							<c:forEach var="bank" items="${bankList}">
								<form:option value="${bank.bank_id}">${bank.bank_name} - ${bank.account_no} </form:option>
							</c:forEach>
						</form:select>
						<span class="logError"><form:errors path="bankId" /></span>
					</div>
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

	function setDate(e){
		document.getElementById("from_date").value = e.value;
		// date format validation
		var datevali = document.getElementById("frmDate").value;
	if(isValidDate(datevali)==true){
		 
			 return true;
	 }else{
		alert("Invalid Date");
		window.location.reload();
		
	}  
		
	}
	
	function setDate1(e){
		document.getElementById("to_date").value = e.value;
		// date format validation
		var datevali1 = document.getElementById("tDate").value;
	if(isValidDate(datevali1)==true){
		 
			 return true;
	 }else{
		alert("Invalid Date");
		window.location.reload();
		
	}  
		
	}
	
	
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#tDate" ).datepicker();
			$("#BankDiv").hide();
			$("#typeId").on("change", function(){
				if(this.value == 2){
					$("#BankDiv").show();
				}
				else
				{
					$("#BankDiv").hide();
				}
						
			});
	   });
	
	
	function checkdata(){
		
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("tDate").value;
		var typeId=document.getElementById("typeId").value;
		var bankId=document.getElementById("bankId").value;
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
		else if(date1>date2)
			{
				alert("From Date Cann't be greater than To Date");
				$("#frmDate").val("");
				$("#tDate").val("");
				return false;
			}
		
		if(typeId=="0")
			{
				alert("please select Book type");
				return false;
			}
		else if(typeId=="2")
		{
			if(bankId=="0")
				{
					alert("Please Select Bank");
					return false;
				}

		}

		
		 //date vali
		var datevali = document.getElementById("frmDate").value;
		
		if(isValidDate(datevali)==true){
			return true;
		}
		else{
			alert("Invalid Date: Date must be mm/dd/yyyy");
			return false;
		}
		
		var datevali1 = document.getElementById("toDate").value;
		
		if(isValidDate(datevali1)==true)
		{
			return true;
		}
		else
		{
			alert("Invalid Date: mm/dd/yyyy");
			return false;
		}
	}
	
	function isValidDate(dateStr) {
		
		
		 // Checks for the following valid date formats:
		 // MM/DD/YYYY
		 // Also separates date into month, day, and year variables
		 //var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
		 
		 //var datePat= /^\d{2}\/\d{2}\/\d{4}$/;
		 
		 var datePat= /^((0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2})*$/;
		 
		 var matchArray = dateStr.match(datePat); // is the format ok?
				 
				//alert(matchArray);
				 
		 if (matchArray == null) {
		  //alert("Date must be in MM-DD-YYYY format")
		  return false;
		 }
		 
		 month = matchArray[2];
		 day   = matchArray[3];
		 
		 
		 if (month < 1 || month > 12) { // check month range
			  alert("Month must be between 1 and 12");
			  return false;
			 }
		 
		 if (day < 1 || day > 31) {
			  alert("Day must be between 1 and 31");
			  return false;
			 }
		
		 return true;  // date is valid
		}
	
 </script>

<%@include file="/WEB-INF/includes/footer.jsp"%>