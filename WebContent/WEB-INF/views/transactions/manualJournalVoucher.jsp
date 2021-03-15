	<%@include file="/WEB-INF/includes/header.jsp"%>
	<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
	<spring:url value="/resources/images/closeRed.png" var="deleteImg" /> 
	<script type="text/javascript" src="${valid}"></script>
	<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
	<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
	<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
	<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:if test="${successMsg != null}">
			<div class="successMsg" id = "successMsg"> 
				<strong>${successMsg}</strong>		
			</div>
			</c:if>

	<div class="breadcrumb">
		<h3>Manual Journal Voucher</h3>
		<a href="homePage">Home</a> » <a href="manualjournalVoucherList">Manual Journal Voucher</a> » <a href="#">Create</a>
	</div>
	<div class="col-md-12 wideform">
		<div class="fassetForm">
			<form:form id="journalVoucherForm" action="saveJournalVoucher" method="post"
				commandName="journalVoucher" onsubmit="return setDate()">
				<div class="row">
					<form:input path="journal_id" hidden="hidden" />
					
						<form:input path="dr_ledgerlist" id="dr_ledgerlist" hidden="hidden"/>
							<form:input path="cr_ledgerlist" id="cr_ledgerlist" hidden="hidden"/>
				</div>
				
				<div id="year-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Select Accounting Year</h4>
	               		</div>
		   				<div class="modal-body">
		   				<c:set var="first_year" value="0" />		   				
							<c:forEach var="year" items="${yearList}">
							<c:choose>
									<c:when test="${first_year==0}">
								<form:radiobutton path="year_id" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
									</c:when>
									<c:otherwise>
									<form:radiobutton path="year_id" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
									</c:otherwise>
							</c:choose>
							<c:set var="first_year" value="${first_year+1}" />
							</c:forEach>
							
						</div>
						<div class="modal-footer">
		               		<button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>Save Year</button>
						</div>
	       			</div><!-- /.modal-content -->
	      		</div>
			</div>
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Date<span>*</span></label></div>		
				<div class="col-md-10">	
			   		<form:input type = "text" style = "color: black;" path="date" id = "date" name = "date"
			   		  placeholder = "Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()"/>	
					<span class="logError"><form:errors path="date" /></span>					
				</div>
			
			</div>
	
					<div class="row">
					<div class="col-md-2 control-label">
						<label>Remark</label>
					</div>
					<div class="col-md-10">
							<form:textarea path="remark" class="logInput" id = "remark" rows="3"  placeholder="Remark" maxlength="250"/>
							
						</div>		
					
				</div>
			
			
			<%-- <c:if test="${mjvDetailList.size() >0}">
				<table class="table table-bordered table-striped" id="maintable">
					<thead>
						<tr>
									<th>Action</th>
									<th>Sub-Ledger Name</th>
										<th>Amount(Dr)</th>
									<th>Amount(Cr)</th>
								</tr>
					</thead>
					<tbody>
						<c:forEach var="drmanualdetail" items="${drmanualdetail}">
							<tr>
								<td><a href="#"
									onclick="deleteMJVDetail(${drmanualdetail.detailjv_id})"><img
										src='${deleteImg}' style='width: 20px;' /></a></td>


								<td>${drmanualdetail.subledgerdr.subledger_name}</td>

									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${drmanualdetail.dramount}" /></td>
								
								<c:set var="amount_dr"
									value="${amount_dr + drmanualdetail.dramount}" />
								<td class='tright'>${mjvDetailList.cramount}</td>
					         <c:set var="amount_cr" value="${amount_cr + drmanualdetail.cramount}" />
									<td></td>
							</tr>

						</c:forEach>
						<c:forEach var="crmanualdetail" items="${crmanualdetail}">
							<tr>
								<td><a href="#"
									onclick="deleteMJVDetail(${crmanualdetail.detailjv_id})"><img
										src='${deleteImg}' style='width: 20px;' /></a></td>


								<td>${crmanualdetail.subledgercr.subledger_name}</td>
								<td></td>

								
									<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${crmanualdetail.cramount}" /></td>

								<c:set var="amount_cr"
									value="${amount_cr + crmanualdetail.cramount}" />
							</tr>

						</c:forEach>
					</tbody>
				</table>
				</c:if> --%>
			
					<table class="table table-bordered table-striped"  id="detailTable">
							<thead>
								<tr>
									<th>Action</th>
									<th>Sub-Ledger Name</th>
										<th>Amount(Dr)</th>
									<th>Amount(Cr)</th>
								</tr>
							</thead>
							<tbody>
						
							</tbody>
				 </table>
			
				<div class="row">
					<div class="col-md-1 control-label">
						<label>Dr<span>*</span></label>
					</div>
					<div class="col-md-5">
					 <select class="logInput" id="select_dr_id" >
							<option value="0" label="Select Sub-Ledger/Supplier/Customer Name "/>
								<c:forEach var="subledger" items="${subledgerList}">
												<option data-othervalue="sub"  value="${subledger.subledger_Id}">${subledger.subledger_name}</option>
											</c:forEach>
											<c:forEach var="supplier" items="${supplierList}">
												<option data-othervalue="sup"  value="${supplier.supplier_id}">${supplier.company_name}</option>
												
											</c:forEach>
											
										<c:forEach var="customer" items="${customerList}">
							<option data-othervalue="cus"  value="${customer.customer_id}">${customer.firm_name}</option>
						</c:forEach>
						</select>
						</div>	
						<div class="col-md-1 control-label">
						<label>Amount</label>
					</div>
										    <div class="col-md-2">
							                <form:input path="amount1" class="logInput" id = "amount_dr"  placeholder="Amount" maxlength="10" />
					 	                    
						                    </div>
							<i class="fa fa-plus btn-plus"  onclick="addDr()"></i>
				
				</div>
			
					<div class="row">
					<div class="col-md-1 control-label">
						<label>Cr<span>*</span></label>
					</div>
					<div class="col-md-5">
						<select  class="logInput" id="select_cr_id"  >
							<option value="0" label="Select Sub-Ledger/Supplier/Customer Name "/>
								<c:forEach var="subledger" items="${subledgerList}">
												<option data-othervalue="sub"  value="${subledger.subledger_Id}">${subledger.subledger_name}</option>
											</c:forEach>
											<c:forEach var="supplier" items="${supplierList}">
												<option data-othervalue="sup"  value="${supplier.supplier_id}">${supplier.company_name}</option>
												
											</c:forEach>
											
										<c:forEach var="customer" items="${customerList}">
							<option data-othervalue="cus"  value="${customer.customer_id}">${customer.firm_name}</option>
						</c:forEach>
						</select>
					
					<%-- 	<span class="logError"><form:errors path="subledger_id2" /></span> --%>
					</div>
		
					<div class="col-md-1 control-label">
						<label>Amount</label>
					</div>
					
						
					 <div class="col-md-2">
							                <form:input path="amount1" class="logInput" id = "amount_cr"  placeholder="Amount"  maxlength="10"></form:input>
					            
						                    </div>	
						                <i class="fa fa-plus btn-plus"  onclick="addCr()"></i>
				</div>
			
				<div class="row text-center-btn">
					<button class="fassetBtn" type="submit">
						<spring:message code="btn_save" />
					</button>
					<button class="fassetBtn" type="button" onclick="cancel()">
						<spring:message code="btn_cancel" />
					</button>
				</div>
			</form:form>
		</div>
	</div>
	<script type="text/javascript">
	var startdate="";
	var enddate="";
	var drDetails = [];
	var crDetails = [];
	var drStatus=1;
	var crStatus=2;
	var debitId="DR";
	var creditId="CR";
	
	function setDatePicker()
	 {
	 	
	 	$("#date").datepicker({dateFormat: 'dd-mm-yy'});
	 	 $('#date').datepicker("option", { maxDate: new Date() });
	 	$('#date').value = '';
	 	$('#date').focus(); // ui-datepicker-div
	 	 event.preventDefault();
	 	
	 } 
	
	 function dateRestriction() {
		 
		 
		 var datefield=document.getElementById("date").value;
		
		 var res = datefield.split("-");
		 var dd=parseInt(res[0]);
		 
		
		 var mm=parseInt(res[1]);
		 
		 var yy=parseInt(res[2]);
		 
		 
		 
		    var nd = res[1]+"/"+res[0]+"/"+res[2];
		    var ud = new Date(nd);
			var td = new Date();
			
			ud.setHours(0,0,0,0);
			
			td.setHours(0,0,0,0);
		var flag = 	 checkDate(dd,mm,yy);
		
		if(flag == false){
			alert("Invalid date");
			var id = document.getElementById("date");

			id.value = '';
			id.focus(); // ui-datepicker-div
			event.preventDefault();	
		}
		else if(ud > td ){
				
				alert("Cannot create voucher for future date");
				var id = document.getElementById("date");

				id.value = '';
				id.focus(); // ui-datepicker-div
				event.preventDefault();		
				
			}
		
		
	 }
	
	 
	 

	 
	 function checkDate(dd,mm,yy)
	 {
		if(mm>0 && mm<13)
		{
			if(mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12 && (dd>0 && dd <32))
			{	
				return true;
			}
			else if(mm == 4 || mm == 6 || mm == 9 || mm == 11 && (dd>0 && dd <31))
			{	
				return true;
			}
			else if(mm == 2)
			{
				var flag = yy%4 == 0 & yy%100 != 0 || yy %400 == 0; 
				if(flag == true && (dd>0 && dd <30))
				{
				  	return true;
				}
				else if(flag == false && (dd>0 && dd < 29))
				{
				  	return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		
	 }
	 
		$(document).ready(function() {
			
			var dateTest = '<c:out value= "${journalVoucher.date}"/>';			
			if(dateTest!=""){			
			 	newdate=formatDate(dateTest);	
				$("#date").datepicker("setDate", newdate);
				$("#date").datepicker("refresh");
			}
			
		
			
			var years = '<c:out value= "${yearList}"/>';	
			var transaction_id = '<c:out value= "${journalVoucher.journal_id}"/>';	
			var ysize = '<c:out value= "${yearList.size()}"/>';	
			if((years!="")&&(transaction_id=="")){
				if(ysize!=1){
			 		$('#year-model').modal({
				    	show: true,
				   	});	 
			 		var j=0;
			 		<c:forEach var = "year" items = "${yearList}">
				    if(j==0)
				    	{			
				    	    startdate1='${year.start_date}';
				    	    enddate1='${year.end_date}';
				    	setdatelimit('${year.start_date}','${year.end_date}');
				    	}
				    j++;
				     </c:forEach>
				    
				}
				else
				{
					<c:forEach var = "year" items = "${yearList}">
			    	        startdate1='${year.start_date}';
			    	        enddate1='${year.end_date}';
						   setdatelimit('${year.start_date}','${year.end_date}');
					</c:forEach>	
					
				}			
			}	
			else
			{
			var savedyear='<c:out value= "${journalVoucher.accounting_year_id.year_id}"/>';
			<c:forEach var = "year" items = "${yearList}">
				if("${year.year_id}"==savedyear)
				{
			    	  startdate1='${year.start_date}';
			    	  enddate1='${year.end_date}';
			       setdatelimit('${year.start_date}','${year.end_date}');
			       var supdate="${journalVoucher.date}";
				   supdate=formatDate(supdate);
				   $("#date").val(supdate);
				}  
			</c:forEach>   
			
			}
			
	
			 <c:forEach var="mjDetails"  items="${drmanualdetail}">
				if('${mjDetails.dramount}'!=0)
			     {
					var id=0;
					var subname="";
					
					if('${mjDetails.subledgerdr.subledger_Id}'>0){
						id='${mjDetails.subledgerdr.subledger_Id}';
						subname='${mjDetails.subledgerdr.subledger_name}';
					}else if ('${mjDetails.supplierdr.supplier_id}'>0){
						
						id='${mjDetails.supplierdr.supplier_id}';
						subname='${mjDetails.supplierdr.company_name}'
					}else if('${mjDetails.customerdr.customer_id}'>0){
						
						id='${mjDetails.customerdr.customer_id}';
						subname='${mjDetails.customerdr.firm_name}';
					}
						
					drDetails.push({
						
						"drledgerId":id,
						"dramountId":'${mjDetails.dramount}',
						"subledger_name":subname,
						"debitId":debitId
					
						});	

		 $("#dr_ledgerlist").val(JSON.stringify(drDetails)); 
						console.log($("#dr_ledgerlist").val());
					
				
						   var markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+ id +","+'${mjDetails.dramount}'+","+drStatus+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
					  		+"<td>"+ subname +"</td>"+
							"<td>"+'${mjDetails.dramount}'+"</td>"+
							 "<td></td>"+ 
						
						"</tr>";
					
						$('#detailTable tr:last').after(markup);
						document.getElementById("detailTable").style.display="table";
					
				 }
				  </c:forEach> 
				  
		     <c:forEach var="mjDetails"  items="${crmanualdetail}">
			 if('${mjDetails.cramount}'!=0)
			{
				 var id=0;
					var subname="";
				 if('${mjDetails.subledgercr.subledger_Id}'>0){
						id='${mjDetails.subledgercr.subledger_Id}';
						subname='${mjDetails.subledgercr.subledger_name}';
					}else if ('${mjDetails.suppliercr.supplier_id}'>0){
						
						id='${mjDetails.suppliercr.supplier_id}';
						subname='${mjDetails.suppliercr.company_name}'
					}else if('${mjDetails.customerdr.customer_id}'>0){
						id='${mjDetails.customercr.customer_id}';
						subname='${mjDetails.customercr.firm_name}';
					}
				 crDetails.push({
						
						"crledgerId":id,
						"cramountId":'${mjDetails.cramount}',
						"subledger_namecr":subname,
						"creditId":creditId
					
						});	
			 $("#cr_ledgerlist").val(JSON.stringify(crDetails)); 
						console.log($("#cr_ledgerlist").val());
						
						  var markup = "<tr><td><a href = '#' onclick = 'deleteRowCr(this,"+ id +","+'${mjDetails.cramount}'+","+crStatus+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
							+"<td>"+subname+"</td>"+
							 "<td></td>"+ 
					 	"<td>"+'${mjDetails.cramount}'+"</td>"+
					 	/* "<td>"+creditId+"</td>"+ */
				 	
						"</tr>";
					
						$('#detailTable tr:last').after(markup);
						document.getElementById("detailTable").style.display="table";
			} 
		  </c:forEach> 
		  
		 
			
			
			
		});
		
		function formatDate(date) {
		    var d = new Date(date),
		        month = '' + (d.getMonth() + 1),
		        day = '' + d.getDate(),
		        year = d.getFullYear();
		    if (month.length < 2) month = '0' + month;
		    if (day.length < 2) day = '0' + day;	
		    return [day,month,year].join('-');
		}
		function formatDate1(date) {
		    var d = new Date(date),
		        month = '' + (d.getMonth() + 1),
		        day = '' + d.getDate(),
		        year = d.getFullYear();
		    if (month.length < 2) month = '0' + month;
		    if (day.length < 2) day = '0' + day;	
		    return [day,month,year].join('/');
		}
		
		function setdatelimit(startdate,enddate)
		{
			startdate1=startdate;
			enddate1=enddate;
			startdate=formatDate(startdate);
			enddate=formatDate(enddate);	
			curdate=formatDate(new Date());		
			var maxdate=curdate;		
			if(curdate < enddate)
				maxdate=enddate;		
			
			$("#date").datepicker({dateFormat: 'dd-mm-yy'});
			 $('#date').datepicker("option", { minDate: startdate, 
	            maxDate: enddate });
		}
		
		function isValidDate(dateStr) {
			 
			 // Checks for the following valid date formats:
			 // MM/DD/YYYY
			 // Also separates date into month, day, and year variables
			 var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
			 
			 var matchArray = dateStr.match(datePat); // is the format ok?
			 if (matchArray == null) {
			  //alert("Date must be in MM-DD-YYYY format")
			  return false;
			 }
			 
			 month = matchArray[1]; // parse date into variables
			 day = matchArray[3];
			 year = matchArray[4];
			 if (month < 1 || month >  12) { // check month range
			  //alert("Month must be between 1 and 12");
			  return false;
			 }
			 if (day < 1 || day > 31) {
			 // alert("Day must be between 1 and 31");
			  return false;
			 }
			 if ((month==4 || month==6 || month==9 || month==11) && day==31) {
			  //alert("Month "+month+" doesn't have 31 days!")
			  return false;
			 }
			 if (month == 2) { // check for february 29th
			  var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
			  if (day>29 || (day==29 && !isleap)) {
			  // alert("February " + year + " doesn't have " + day + " days!");
			   return false;
			    }
			 }
			 return true;  // date is valid
			}
		
		function process(date){
			   var parts = date.split("-");
			   var date = new Date(parts[1] + "/" + parts[0] + "/" + parts[2]);
			   return date.getTime();
			}
		
	function saveyearid(){
		 $('#year-model').modal('hide');
	}


	
		$(function() {		
			setTimeout(function() {
			    $("#successMsg").hide()
			}, 3000);
			
			$("#amount_cr").keypress(function(e) {
				if (!digitsAndDotOnly(e)) {
					return false;
				}
			});
			
			$("#amount_dr").keypress(function(e) {
				if (!digitsAndDotOnly(e)) {match
					return false;
				}
			});
			
		});
	
		function cancel(){
			window.location.assign('<c:url value = "manualjournalVoucherList"/>');	
		}
		
		function isDigit(){
			var id= document.getElementById("amount_dr");
		
		}
	
		function addCr()
		{
			var drledgerId = $("#select_dr_id").val();
			var crledgerId = $("#select_cr_id").val();
			var cramountId = $("#amount_cr").val();
			
			var subledger_namecr = "";
		
			 var type =$('#select_cr_id').find('option:selected').attr('data-othervalue');;
		
			if(crledgerId == 0){
				alert("Please select ledger.");
				return false;
			}
			else if(cramountId == 0){
				alert("Please enter Amount.");
				return false; 
			}
			else
				{
				var flag = false;
				for(var i=parseInt(0);i<crDetails.length;i++) {
					var tempcrDetails = crDetails[i].crledgerId;
				
					if(tempcrDetails==crledgerId)
						{
						flag=true;
						break;
						}
	
					  }	
				for(var i=parseInt(0);i<drDetails.length;i++) {
					var tempdrDetails = drDetails[i].drledgerId;
				
					if(tempdrDetails==crledgerId)
						{
						flag=true;
						break;
						}
	
					  }			
				
				if(flag==false)
				{
/* 		<c:forEach var="subledger" items="${subledgerList}" >
		subledger_id = ${subledger.subledger_Id};
		if(crledgerId==subledger_id)
			{
		
			subledger_namecr = "${subledger.subledger_name}";
			}
		</c:forEach> */
	
	
		subledger_namecr = $("#select_cr_id option:selected").text();
		

		crDetails.push({
		
			"crledgerId":crledgerId,
			"cramountId":cramountId,
			"subledger_namecr":subledger_namecr,
			"creditId":creditId,
			"type":type
			});	
	         var crAmount = parseFloat(cramountId);
	
		 $("#cr_ledgerlist").val(JSON.stringify(crDetails)); 
			console.log($("#cr_ledgerlist").val());
			
			  var markup = "<tr><td><a href = '#' onclick = 'deleteRowCr(this,"+crledgerId+","+cramountId+","+crStatus+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
				+"<td>"+subledger_namecr+"</td>"+
				 "<td></td>"+ 
		 	"<td>"+crAmount+"</td>"+
		 	/* "<td>"+creditId+"</td>"+ */
	 	
			"</tr>";
		
			$('#detailTable tr:last').after(markup);
			document.getElementById("detailTable").style.display="table";
		
			var crledgerId = $("#select_cr_id").val("0");
			var cramountId = $("#amount_cr").val("0");
				}
				else
				{
				alert("Ledger is already added");
			
				var crledgerId = $("#select_cr_id").val("0");
				var cramountId = $("#amount_cr").val("0");
			
				$("#cr_ledgerlist").val(JSON.stringify(crDetails)); 
				return false; 
				}
			
				}
		}
		function addDr(){
		
			var crledgerId = $("#select_cr_id").val();
			var drledgerId = $("#select_dr_id").val();
			var dramountId = $("#amount_dr").val();
			
			var subledger_name = "";
	        var type =$('#select_dr_id').find('option:selected').attr('data-othervalue');;
	        
	       
	   		
		
			if(drledgerId == 0){
				alert("Please select ledger.");
				return false;
			}
			else if(dramountId == 0){
				alert("Please enter Amount.");
				return false; 
			}
	
			else
				{
				var flag = false;
				for(var i=parseInt(0);i<drDetails.length;i++) {
					var tempdrDetails = drDetails[i].drledgerId;
				
					if(tempdrDetails==drledgerId)
						{
						flag=true;
						break;
						}
	
					  }	
				for(var i=parseInt(0);i<crDetails.length;i++) {
					var tempcrDetails = crDetails[i].crledgerId;
				
					if(tempcrDetails==drledgerId)
						{
						flag=true;
						break;
						}
	
					  }		
				
				if(flag==false)
					{
			/* <c:forEach var="subledger" items="${subledgerList}" >
			subledger_id = ${subledger.subledger_Id};
	 	if(drledgerId==subledger_id)
	 		{
			
	 		subledger_name = "${subledger.subledger_name}";
	 		}
		
		  	 </c:forEach> */
		  	subledger_name = $("#select_dr_id option:selected").text();
					drDetails.push({
			
				"drledgerId":drledgerId,
				"dramountId":dramountId,
				"subledger_name":subledger_name,
				"debitId":debitId,
				"type":type
				});	
		
			   var drAmount = parseFloat(dramountId);
					
			 $("#dr_ledgerlist").val(JSON.stringify(drDetails)); 
				console.log($("#dr_ledgerlist").val());
			
		
				   var markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+drledgerId+","+dramountId+","+drStatus+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
			  		+"<td>"+subledger_name+"</td>"+
					"<td>"+drAmount+"</td>"+
					 "<td></td>"+ 
				
				"</tr>";
			
				$('#detailTable tr:last').after(markup);
				document.getElementById("detailTable").style.display="table";
			
				var drledgerId = $("#select_dr_id").val("0");
				var dramountId = $("#amount_dr").val("0");
				}
			
			else
				{
				alert("Ledger is already added");
			
				var drledgerId = $("#select_dr_id").val("0");
				var dramountId = $("#amount_dr").val("0");
			
		
				$("#dr_ledgerlist").val(JSON.stringify(drDetails)); 
				return false; 
				}
				}
		
		}
		function deleteRow(e, drledgerId, dramountId, drStatus){
			
	
			if(drledgerId>0 && drStatus==1){
			
			   $(e).closest('tr').remove();
			
				for(i = 0; i<drDetails.length;i++){
					if(drDetails[i].drledgerId == drledgerId){
						drDetails.splice(i,1);
					}
					console.log(JSON.stringify(drDetails));
					}
				$("#dr_ledgerlist").val(JSON.stringify(drDetails)); 
			}
		
		}
	
	function deleteRowCr(e, crledgerId, cramountId,crStatus){

			if(crledgerId>0 && cramountId>0 &&  crStatus==2){
			
			   $(e).closest('tr').remove();
			
				for(i = 0; i<crDetails.length;i++){
					if(crDetails[i].crledgerId == crledgerId){
						crDetails.splice(i,1);
					}
					console.log(JSON.stringify(crDetails));
					}
				$("#cr_ledgerlist").val(JSON.stringify(crDetails)); 
			}
		
		}
	


	function setDate(){
		
		var drAmount=0;
		var crAmount=0;
		var drledgerId = document.getElementById("select_dr_id").value;
		var crledgerId = document.getElementById("select_cr_id").value;
		var cramountId = document.getElementById("amount_cr").value;
		var dramountId = document.getElementById("amount_dr").value;
		
		if(isValidDate(startdate1)==false){
			startdate1=formatDate(startdate1);
		 }
		if(isValidDate(enddate1)==false){
			enddate1=formatDate(enddate1);
			 }	
		
		var dateString = $("#date").val();
		var fisrtDate = dateString.split("/");
		var secondDate = dateString.split("-");		
		
		if(fisrtDate[1]!=undefined)
			{
			   var dateString1=formatDate1(fisrtDate);
			  dateString1 = dateString1.split("/");		
			   dateString=dateString1[0]+"-"+dateString1[1]+"-"+dateString1[2];
			 }
			if(secondDate[1]!=undefined)
			{
			   dateString=secondDate[0]+"-"+secondDate[1]+"-"+secondDate[2];			
			}
		if(dateString!="")
			{
		
			var dateStringnew=process(dateString);
			var startdatenew=process(startdate1);
			var enddatenew=process(enddate1);
				if(dateStringnew < startdatenew)
				  {
					alert("Select Date between "+startdate1+" and "+enddate1);
				  		return false;
				  }
				  else if(dateStringnew > enddatenew)
				  {
					  alert("Select Date between "+startdate1+" and "+enddate1);
				  		return false;
				  }
				  else
					{
					  
						var oldDate = dateString.split("-");						
						//alert(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
						$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
						
					}
			}
		
		for(var i=parseInt(0);i<crDetails.length;i++) {
			crAmount = parseFloat(crDetails[i].cramountId)+parseFloat(crAmount);
			  }	
		
		for(var i=parseInt(0);i<drDetails.length;i++) {
			drAmount = parseFloat(drDetails[i].dramountId)+parseFloat(drAmount);

			  }	

	 	var frmDate=document.getElementById("date").value;
		
		 if(frmDate=="")
			{
			alert("Please Select Date");
			return false;
			}
		else if(drDetails.length==0 && crDetails.length==0)
		{
		alert("Please Add ledger in credit and debit ");
		
		return false;
		}
		else if(crAmount!=drAmount)
			{
		
			alert(" Credit and Debit amount is not matched");
			return false;
			}
		else
			{
			return true;
			}
	}
	function deleteMJVDetail(id){
		window.location.assign('<c:url value="deleteMVJDetail"/>?id='+id);
	}

 </script> 
	<%@include file="/WEB-INF/includes/footer.jsp"%>