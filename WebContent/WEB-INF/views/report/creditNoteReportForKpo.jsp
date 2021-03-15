<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Credit Note Register</h3>
	<a href="homePage">Home</a> » <a href="#">Credit Note Register</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="creditNoteReportForm"
			action="showCreditNoteReportforKpo" method="post" commandName="form" onsubmit="return checkdata();">
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
					<form:select path="companyId" class="logInput"
						placeholder="Company Name" onChange = "getLedgerList(this.value)" id="companyId">
						<form:option value="0" label="Select Company Name" />
						<c:forEach var="company" items="${companyList}">
							<form:option value="${company.company_id}">${company.company_name}  </form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="companyId" /></span>
				</div>
			</div>
			
			<%-- <div class="row">
				<div class="col-md-2 control-label">
					<label>Select Report Against<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="reportAgainst" class="logInput" id="reportAgainst"
						placeholder="Report Against">
						<form:option value="0" label="Select Report Against" />
						<form:option value="1" label="Customer" />
						<form:option value="2" label="Subledger" />
					</form:select>
					<span class="logError"><form:errors path="reportAgainst" /></span>
				</div>
			</div> --%>

			<%-- <div id="LedgerDiv">
				<div class="row">
					<div class="col-md-2 control-label">
						<label>Ledger Name<span>*</span></label>
					</div>
					<div class="col-md-10">
						<form:select path="ledgerId" class="logInput" id="ledgerId"
							placeholder="Ledger Name"  onChange = "getSubledgerList(this.value)">
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
			</div> --%>

			
				<div class="row">
					<div class="col-md-2 control-label">
						<label>Customer Name<span>*</span></label>
					</div>
					<div class="col-md-10">
						<form:select path="customerId" class="logInput"
							placeholder="Customer Name" id="customerId">
							<form:option value="0" label="All" />
						</form:select>
						<span class="logError"><form:errors path="customerId" /></span>
					</div>
				</div>
			

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Type<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="option" class="logInput"
						placeholder="Option" id="typeOption">
						<form:option value="0" label="Select Option" />
						<form:option value="1" label="Condensed" />
						<form:option value="2" label="Columnar" />
					</form:select>
					<span class="logError"><form:errors path="option" /></span>
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
		var firmArray = [];		
		$.ajax({
	        type: "POST",
	        url: "getcustomerList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
	        	 $('#customerId').empty();
	        	 $('#customerId').append('<option value="0">All</option>');
	        	 $.each(data, function (index, customer) {	        		 
	        		 var tempArray = [];
					    tempArray["id"]=customer.customer_id;
					    tempArray["name"]=customer.firm_name;
					    firmArray.push(tempArray);                 
	              });
	        	 
	        	 firmArray.sort(function(a, b) {
		 	            var textA = a.name.toUpperCase();
		 	            var textB = b.name.toUpperCase();
		 	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		 	        });
		 			for(i=0;i<firmArray.length;i++)
		 			{
		 				 $('#customerId').append($('<option>', {
		 			    value: firmArray[i].id,
		 			    text: firmArray[i].name,
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
			<c:if test="${form.companyId != null && form.companyId > 0}">
				var companyID = <c:out value = "${form.companyId}"/>;
				if(companyID > 0){
					
					getLedgerList(companyID);
				}
			 </c:if>			
			});	  
	
						function checkdata(){
							var frmDate=document.getElementById("frmDate").value;
							var toDate=document.getElementById("toDate").value;
							var typeOption=document.getElementById("typeOption").value;
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
								else if(typeOption=="0")
								{
								alert("Please Select Option");
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