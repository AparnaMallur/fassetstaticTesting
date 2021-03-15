<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Ledger Report</h3>
	<a href="homePage">Home</a> » <a href="ledgerReport">Ledger Report</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="ledgerReportForm"
			action="ledgerReport" method="post" commandName="ledgerReportForm" onsubmit="return checkdata();">
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
						placeholder="Ledger Name" onChange="getList(this.value)">
						<form:option value="0" label="All" />
						<c:forEach var="ledger" items="${ledgerList}">
							<form:option value="${ledger.ledger_id}">${ledger.ledger_name}  </form:option>
						</c:forEach>
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
						                              placeholder="Bank Name">
						                
					    <form:option value="-4" label="All" />
						<c:forEach var="bank" items="${bankList}">
							<form:option value="${bank.bank_id}">${bank.bank_name}  </form:option>
						</c:forEach>
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
						<c:forEach var="customer" items="${customerList}">
							<form:option value="${customer.customer_id}">${customer.firm_name}  </form:option>
						</c:forEach>
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
						placeholder="Supplier Name" >
						
						<form:option value="-2" label="All" />
						<c:forEach var="supplier" items="${suppliersList}">
							<form:option value="${supplier.supplier_id}">${supplier.company_name}  </form:option>
						</c:forEach>
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
	
	
	
	function getList(ledgerId){
		<c:forEach var = "ledger" items = "${ledgerList}">
			var id = <c:out value = "${ledger.ledger_id}"/>
			if(id == ledgerId){
				
				$('#subledgerId').find('option').remove();
				$('#subledgerId').append($('<option>', {
				    value: 0,
				    text: 'All'
				}));
				<c:forEach var = "subledger" items = "${ledger.subLedger}">
				
					var sub_name="${subledger.subledger_name}".replace('\'', '\'');
					
					<c:if test="${subledger.status==true && subledger.flag==true && subledger.subledger_approval==3}">	
					$('#subledgerId').append($('<option>', {
					    value: '${subledger.subledger_Id}',
					    text: sub_name,// dont change it. it is working fine. 
					}));
					</c:if>
	         	</c:forEach>	
			}
	   </c:forEach>
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