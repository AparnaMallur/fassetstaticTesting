<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Ledger Report</h3>
	<a href="homePage">Home </a> » <a href="ledgerReport">Ledger Report </a>
</div>

<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="ledgerReportForm"
			action="ledgerReportForKpo" method="post" commandName="ledgerReportForm" onsubmit="return checkdata();">
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
						name="frmDate" onchange="setDate(this)" placeholder="From Date" autocomplete="off">
					<span class="logError"><form:errors path="fromDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>To Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="toDate" name="toDate"
						onchange="setDate1(this)" placeholder="To Date" autocomplete="off"> <span
						class="logError"><form:errors path="toDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Company Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="companyId" class="logInput" id="companyId"
						placeholder="Company Name" onChange= "getLedgerList(this.value)">
						<form:option value="0" label="Select Company Name" />
						<c:forEach var="company" items="${companyList}">
							<form:option value="${company.company_id}" >${company.company_name}  </form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="companyId" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Select Report Against<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="reportAgainst" class="logInput" id="reportAgainst"
						placeholder="Report Against">
						<form:option value="0" label="Select Report Against" />
						<form:option value="1" label="Customer" />
						<form:option value="2" label="Supplier" />
						<form:option value="3" label="Subledger" />
						<form:option value="4" label="Bank" />
					</form:select>
					<span class="logError"><form:errors path="reportAgainst" /></span>
				</div>
			</div>
			
			<div id="LedgerDiv">
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Ledger Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="ledgerId" class="logInput"
						placeholder="Ledger Name" onChange= "getSubLedgerList(this.value)">
						<form:option value="0" label="All" />
						
					</form:select>
					<span class="logError"><form:errors path="ledgerId" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Subledger Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="subledgerId" class="logInput"
						placeholder="Subledger Name" id="subledgerId">
						<form:option value="0" label="All" />
					</form:select>
					<span class="logError"><form:errors path="subledgerId" /></span>
				</div>
			</div>
	 	  </div>
	 	
	 <div id="BankDiv">
	 	<div class="row">
				<div class="col-md-2 control-label">
					<label>Bank Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="bankId" class="logInput" id="bankId"
						                              placeholder="Bank Name" >   
					    <form:option value="-4" label="All" />
						
				   </form:select>
					<span class="logError"><form:errors path="bankId" /></span>
				</div>
			</div>
	  </div>
	 
			
	 	
	 	<div id="CustomerDiv">
				<div class="row">
					<div class="col-md-2 control-label">
						<label>Customer Name<span>*</span></label>
				    </div>
					<div class="col-md-10">
					<form:select path="customerId" id="customerId" class="logInput"
						placeholder="Customer Name">
						<form:option value="-1" label="All" />
						
					</form:select>
					<span class="logError"><form:errors path="customerId" /></span>
					</div>
				</div>
		</div>
			
	 	<div id = "SuppilerDiv">
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Supplier Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="supplierId" class="logInput" id="supplierId"
						placeholder="Supplier Name">  
						<form:option value="-2" label="All" />
						
					</form:select>
					<span class="logError"><form:errors path="supplierId" /></span>
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
		var datevali1 = document.getElementById("toDate").value;
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
	
	
	function getLedgerList(companyId){
		var customerArray = [];
		var supplierArray = [];		
		var ledgerArray = [];
		var bankArray = [];
		
		$.ajax({
	        type: "POST",
	        url: "getBankList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#bankId').empty();
	        	 $('#bankId').append('<option value="-4">All</option>');
	        	 $.each(data, function (index, bank) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=bank.bank_id;
					    tempArray["name"]=bank.bank_name;
					    bankArray.push(tempArray);  
	                         
	             });
	        	 
	        	 bankArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<bankArray.length;i++)
		 			{
		 				 $('#bankId').append($('<option>', {
		 			    value: bankArray[i].id,
		 			    text: bankArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
		

		$.ajax({
	        type: "POST",
	        url: "getcustomerList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#customerId').empty();
	        	 $('#customerId').append('<option value="-1">All</option>');
	        	 $.each(data, function (index, customer) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=customer.customer_id;
					    tempArray["name"]=customer.firm_name;
					    customerArray.push(tempArray);
	                         
	             });
	        	 
	        	 customerArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<customerArray.length;i++)
		 			{
		 				 $('#customerId').append($('<option>', {
		 			    value: customerArray[i].id,
		 			    text: customerArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
		
		$.ajax({
	        type: "POST",
	        url: "getsuppilerList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#supplierId').empty();
	        	 $('#supplierId').append('<option value="-2">All</option>');
	        	 $.each(data, function (index, sup) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=sup.supplier_id;
					    tempArray["name"]=sup.company_name;
					    supplierArray.push(tempArray);
	                         
	             });
	        	 
	        	 supplierArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<supplierArray.length;i++)
		 			{
		 				 $('#supplierId').append($('<option>', {
		 			    value: supplierArray[i].id,
		 			    text: supplierArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
		
		$.ajax({
	        type: "POST",
	        url: "getledgerList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	             $('#ledgerId').empty();
	        	 $('#ledgerId').append('<option value="0">All</option>'); 
	        	 $.each(data, function (index, ledger) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=ledger.ledger_id;
					    tempArray["name"]=ledger.ledger_name;
					    ledgerArray.push(tempArray);
	                         
	             });
	        	 
	        	 ledgerArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
	        	 getSubLedgerList(ledgerArray[0].id);
		 			for(i=0;i<ledgerArray.length;i++)
		 			{
		 				$('#ledgerId').append($('<option>', {
		 			    value: ledgerArray[i].id,
		 			    text: ledgerArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
	}
	
	function getSubLedgerList(ledgerId){
		var subledgerArray = [];
			$.ajax({
	        type: "POST",
	        url: "getSubLedgerList?id="+ledgerId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#subledgerId').empty();
	        	 $('#subledgerId').append('<option value="0">All</option>');
	        	 $.each(data, function (index, sub) {
	        		 
	        		 var tempArray = [];
					    tempArray["id"]=sub.subledger_Id;
					    tempArray["name"]=sub.subledger_name;
					    subledgerArray.push(tempArray);
	                         
	             });
	        	 
	        	 subledgerArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<subledgerArray.length;i++)
		 			{
		 				 $('#subledgerId').append($('<option>', {
				 		 value: subledgerArray[i].id,
				 		 text: subledgerArray[i].name,
		 			})); 
		 			}
	        },
	        error: function (e) {
	            console.log("ERROR: ", e);
	            
	        },
	        done: function (e) {
	            
	            console.log("DONE");
	        }
	    });
	}
	
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#toDate" ).datepicker();
			
			var frmDate = '<c:out value = "${ledgerReportForm.fromDate}"/>';
			var toDate = '<c:out value = "${ledgerReportForm.toDate}"/>';
			
			if(frmDate!=""){
				frmDate=formatDate(frmDate);	
				$("#frmDate").datepicker("setDate", frmDate);
				$("#frmDate").datepicker("refresh"); 
			}
			
			if(toDate!=""){
				toDate=formatDate(toDate);	
				$("#toDate").datepicker("setDate", toDate);
				$("#toDate").datepicker("refresh"); 
			}
		
			
			function formatDate(date) {
			    var d = new Date(date),
			        month = '' + (d.getMonth() + 1),
			        day = '' + d.getDate(),
			        year = d.getFullYear();

			    if (month.length < 2) month = '0' + month;
			    if (day.length < 2) day = '0' + day;

			    return [month,day,year].join('/');
			}
			
			
			var reportAgainst = '<c:out value = "${ledgerReportForm.reportAgainst}"/>';
			var compId = '<c:out value = "${ledgerReportForm.companyId}"/>';
		
			
			if(compId>0)
				{
				getLedgerList(compId);
				}
			if(reportAgainst==1)
				{
				$("#LedgerDiv").hide();
				$("#SuppilerDiv").hide();
				$("#CustomerDiv").show();
				$("#BankDiv").hide();
				
				}
			else if(reportAgainst==2)
				{
				$("#LedgerDiv").hide();
				$("#SuppilerDiv").show();
				$("#CustomerDiv").hide();
				$("#BankDiv").hide();
				}
			else if(reportAgainst==3)
			{
			$("#LedgerDiv").show();
			$("#SuppilerDiv").hide();
			$("#CustomerDiv").hide();
			$("#BankDiv").hide();
			}else if(reportAgainst==4)
				{
				$("#LedgerDiv").hide();
				$("#SuppilerDiv").hide();
				$("#CustomerDiv").hide();
				$("#BankDiv").show();
				}
			else
				{
				$("#LedgerDiv").hide();
				$("#SuppilerDiv").hide();
				$("#CustomerDiv").hide();
				$("#BankDiv").hide();
				}
			
			
			
			$("#reportAgainst").on("change", function(){
				if(this.value == 2){
					$("#bankId").val(0);	
					$("#customerId").val(0);	
					$("#LedgerDiv").hide();
					$("#SuppilerDiv").show();
					$("#CustomerDiv").hide();
					$("#BankDiv").hide();
					
				}
				else
					if(this.value == 1)
						{
						
						$("#bankId").val(0);	
						$("#supplierId").val(0);	
						$("#LedgerDiv").hide();
						$("#CustomerDiv").show();
						$("#SuppilerDiv").hide();
						$("#BankDiv").hide();
						
						
						}
				else if(this.value == 3){
					$("#bankId").val(0);	
					$("#customerId").val(0);	
					$("#supplierId").val(0);
					$("#LedgerDiv").show();
					$("#SuppilerDiv").hide();
					$("#CustomerDiv").hide();
					$("#BankDiv").hide();
				}
				else
					{
					$("#customerId").val(0);
					$("#supplierId").val(0);	
					$("#BankDiv").show();
					$("#LedgerDiv").hide();
					$("#SuppilerDiv").hide();
					$("#CustomerDiv").hide();
					}
			});	 
	   });
	
	function checkdata(){
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("toDate").value;
		var reportAgainst=document.getElementById("reportAgainst").value;
		var companyId=document.getElementById("companyId").value;
		
		
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
			else if(companyId=="0")
			{
			alert("Please Select Company Name");
			return false;
			}
			else if(reportAgainst=="0")
			{
			alert("Please Select Report Against");
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