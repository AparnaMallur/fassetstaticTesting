<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Bills Payable</h3>
	<a href="homePage">Home</a> » <a href="#">Bills Payable</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="billsPayableReportForm" action="showBillsPayableReport" method="post" commandName="form" onsubmit="return checkdata();">
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
					<label>Company Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="clientId" class="logInput"  id="companyId" onChange = "getSupplierList(this.value)">
						<form:option value="0" label="Select Company Name" />
						<c:forEach var="company" items="${companyList}">
							<form:option value="${company.company_id}">${company.company_name} </form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="clientId" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Supplier Name<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="supplierId" class="logInput" id="supplierId">
						<form:option value="0" label="Select Supplier Name" />
					</form:select>
					<span class="logError"><form:errors path="supplierId" /></span>
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
		
		<c:if test="${form.clientId != null && form.clientId > 0}">
			var companyID = <c:out value = "${form.clientId}"/>;
			if(companyID > 0){
				getSupplierList(companyID);
			}
	 	</c:if>
	});


	
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
	
	function getSupplierList(companyId){		
		var supplierArray = [];
		$.ajax({
	        type: "POST",
	        url: "getsuppilerList?id="+companyId,
	        contentType: 'application/json',
	        dataType: 'json',
	        
	        success: function (data) {
	        	console.log("User Name==="+data);
        	 	$('#supplierId').empty();
        	 	$('#supplierId').append('<option value="0">All</option>');
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
	 			for(i=0;i<supplierArray.length;i++){
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
	}
	
	function checkdata(){
		var toDate=document.getElementById("toDate").value;
		var companyId=document.getElementById("companyId").value;

		var date2 = new Date(toDate);		
		  if(toDate=="")
			{
			alert("Please Select To Date");
			return false;
			}
		  else if(companyId=="0")
			{
			alert("Please Select Company Name");
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