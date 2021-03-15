<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
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
	<h3>Contra</h3>					
	<a href="homePage">Home</a> » <a href="contraList">Contra</a> » <a href="#">Create</a>
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="contraform" action="saveContra" method="post" commandName = "contra" onsubmit = "return setDate()">	
			<!-- Small modal -->
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
								<form:radiobutton path="accountYearId" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
									</c:when>
									<c:otherwise>
									<form:radiobutton path="accountYearId" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
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
			<!-- /.modal -->                           
			<div class="row">				
				<form:input path="transaction_id" type = "hidden"/>
				<form:input path="status"  type = "hidden" value="true"/>
				<%-- <form:input path="type" hidden = "hidden1"/>	 --%>
						
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
				<div class="col-md-2 control-label"><label >Type<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="type" class="logInput" id = "type" onChange="checktype(this.value)">	
						<form:option value="0" label="Select Type"/>	
						<form:option value = "1">Deposit</form:option>					
						<form:option value = "2">Withdraw</form:option>
						<form:option value = "3">Transfer</form:option>						
					</form:select>
					<span class="logError"><form:errors path="type" /></span>
				</div>	
			</div>
				
			<div class="row" id="from-div">		
				<div class="col-md-2 control-label"><label id="lable-from">From<span>*</span></label></div>		
				<div class="col-md-10">
				<form:select path="withdrawFrom" class="logInput" placeholder="SubLedger">
				<form:option value="0" label="Select Bank"/>	
					<c:forEach var = "bank" items = "${bankList}">							
						<form:option value = "${bank.bank_id}">${bank.bank_name}-${bank.account_no}</form:option>	
					</c:forEach>									
				</form:select>
				<span class="logError"><form:errors path="withdrawFrom" /></span>
				</div>		
			</div>	
			<div class="row" id="to-div">		
				<div class="col-md-2 control-label" ><label id="lable-to">To<span>*</span></label></div>		
				<div class="col-md-10">
				<form:select path="depositeTo" class="logInput">
					<form:option value="0" label="Select Bank"/>	
					<c:forEach var = "bank" items = "${bankList}">							
						<form:option value = "${bank.bank_id}">${bank.bank_name}-${bank.account_no}</form:option>	
					</c:forEach>									
				</form:select>
				<span class="logError"><form:errors path="depositeTo" /></span>
				</div>		
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Amount<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="amount" class="logInput" id = "amount" 	maxlength="18"  placeholder="Amount" />
					<span class="logError"><form:errors path="amount" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Remark</label></div>		
				<div class="col-md-10">
				<form:textarea path="other_remark" class="logInput" id = "other_remark" rows="5"  placeholder="Remark"  maxlength="1000"></form:textarea>
					<span class="logError"><form:errors path="other_remark" /></span>
				</div>	
			</div>	
	
			
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit"  onclick = " return checkBank()">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">

function checkBank(){
	var type =document.getElementById('type').value;
	if(type==3){	
		var w=document.getElementById('withdrawFrom').value;
		var d=document.getElementById('depositeTo').value;
		if(w==d){
			alert("withdraw and deposit bank  name must be different");
		return false;
	
		}
		else
			return true;
}
}
$("#amount").keypress(function(e) {
	
	if (!digitsAndDotOnly(e)) {
		return false;
	}
});
 var startdate="";
 var enddate="";
 
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
		
		var dateTest = '<c:out value= "${contra.date}"/>';			
		if(dateTest!=""){			
		 	newdate=formatDate(dateTest);	
			$("#date").datepicker("setDate", newdate);
			$("#date").datepicker("refresh");
		}
		
		var type =document.getElementById('type').value;
		if(type!=0){
			if(type==1){
				document.getElementById('lable-from').innerHTML = 'Deposite From';
				document.getElementById('lable-to').innerHTML = 'Deposite To';
				document.getElementById('from-div').style.display="none";
				document.getElementById('to-div').style.display="block";
			}
			else if(type==2){
				document.getElementById('lable-from').innerHTML = 'Withdrawal From';
				document.getElementById('lable-to').innerHTML = 'Withdrawal To';
				document.getElementById('from-div').style.display="block";
				document.getElementById('to-div').style.display="none";
			}
			else if(type==3){
				document.getElementById('lable-from').innerHTML = 'Transfer From';
				document.getElementById('lable-to').innerHTML = 'Transfer To';
				document.getElementById('from-div').style.display="block";
				document.getElementById('to-div').style.display="block";
			}
		}
		
		var years = '<c:out value= "${yearList}"/>';	
		var transaction_id = '<c:out value= "${contra.transaction_id}"/>';	
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
		var savedyear='<c:out value= "${contra.accounting_year_id.year_id}"/>';
		<c:forEach var = "year" items = "${yearList}">
			if("${year.year_id}"==savedyear)
			{
		    	  startdate1='${year.start_date}';
		    	  enddate1='${year.end_date}';
		       setdatelimit('${year.start_date}','${year.end_date}');
		       var supdate="${contra.date}";
			   supdate=formatDate(supdate);
			   $("#date").val(supdate);
			}  
		</c:forEach>   
		
		}
		var id = '<c:out value= "${contra.transaction_id}"/>';
		/* if(id > 0){
			$("#type").attr('disabled', 'disabled');
		} */
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
	
	function checktype(type){
		if(type!=0){
			if(type==1){
				document.getElementById('lable-from').innerHTML = 'Deposite From';
				document.getElementById('lable-to').innerHTML = 'Deposite To';
				document.getElementById('from-div').style.display="none";
				document.getElementById('to-div').style.display="block";
			}
			else if(type==2){
				document.getElementById('lable-from').innerHTML = 'Withdrawal From';
				document.getElementById('lable-to').innerHTML = 'Withdrawal To';
				document.getElementById('from-div').style.display="block";
				document.getElementById('to-div').style.display="none";
			}
			else if(type==3){
				document.getElementById('lable-from').innerHTML = 'Transfer From';
				document.getElementById('lable-to').innerHTML = 'Transfer To';
				document.getElementById('from-div').style.display="block";
				document.getElementById('to-div').style.display="block";
			}
		}
	}
	
	function cancel(){
		window.location.assign('<c:url value = "contraList"/>');	
	}
	
	function saveyearid(){
		 $('#year-model').modal('hide');
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
	function setDate(){
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
						return true;
					}
			}
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
		 if (month < 1 || month > 12) { // check month range
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


</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>