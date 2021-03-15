<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<%-- <spring:url value="/resources/images/closeRed.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" /> --%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>




<div class="breadcrumb">

<a href="homePage">Home</a> » <a href="depreciationAutoJVList">Depriciation Auto JV</a> » <a href="#">Create</a>
</div>

<div class="col-md-12 text-center">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>
</div>


<div class="col-md-12 wideform">
	<div class="fassetForm">
				
		<form:form action="saveDepreciationAutoJV" method="post" commandName="dAutoJV" onsubmit="return validateForm();">
		<form:input path="depreciation_id" type="hidden" />
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
														<form:radiobutton path="year_id" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
												</c:when>
												<c:otherwise>
														<form:radiobutton path="year_id" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
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
				<div class="col-md-2 control-label"><label>Date<span>*</span></label></div>		
				<div class="col-md-10">	
			   		<form:input type = "text" style = "color: black;" path="date" id = "date" name = "date"
			   		  placeholder = "Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()"/>	
					<span class="logError"><form:errors path="date" /></span>					
				</div>
			
			</div>	
			<form:input path="dr_ledgerlist" id="dr_ledgerlist" hidden="hidden"/>
 	<div class = "borderForm">
		<table id="officers-table" 
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
				<!-- <th class='test' >Action</th> -->
				<th  data-field="Subledgername" data-filter-control="select" data-sortable="true" >SubLedgerName</th>						
				<th  data-field="Subledgername" data-filter-control="select" data-sortable="true" >Amount</th>
			</tr>
			</thead>
			<tbody>
            
        <c:forEach var ="subLedger" items = "${subledgerList}">        
            <tr>
             
            <td style="text-align: left;">${subLedger.subledger_name}</td>
            	
            
        
            <td style="text-align: left;">
			<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${subLedger.depSubLedgerAmount}" var="amtvalue"/>
		 <form:input path="amount1" class="logInput"  placeholder="Amount"  autocomplete="off" id="amount" maxlength="10" value="${amtvalue}" onChange="calculaterate(this,${subLedger.subledger_Id})"/>
		            
            </td>    
            </tr>
                </c:forEach>
            <%-- </c:forEach> --%>
            
            </tbody>
		
		 <tfoot>
			<tr>
			<td  style="color:#000;font-weight:bold">Total</td>
			<td>
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${dAutoJV.amount}" var="amtvalue"/>
					 <form:input path="amount" class="logInput1"  placeholder="Amount"  value="${amtvalue}" id="amount" maxlength="10" readonly="true" />
		 </td>
			</tr>
			</tfoot>

		</table>






		 	
		</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
			</form:form>
			
	</div>
	</div>


<script>
function dateSelect()
{
	 var  date=$("#date").val();
	 
	 if(date=="")
		 {
		 alert("please Select Date First");
		 return false;
		 }
} 

    var sum = 0;
	var subDetails = [];
	
function calculaterate(e,sub)
{
	var amount = [];
	var totalAmount=0;
	var  subledger_id =0;
	var  subAmount=0;
	
	var drDetails = [];
	
	subledger_id=sub;
	var getval = e.value;
		
	 var sum=0;
    $(".logInput").each(function() {
    	sum+=+$(this).val();
    });
    $(".logInput1").val(sum);
 
    for(var i=0;i<subDetails.length;i++)
	{
    	if(subDetails[i].subId==subledger_id)
    		{
    		subDetails.splice(i,1);
    		}
	}
    console.log(JSON.stringify(subDetails));
	subDetails.push({
		
   		"subId":subledger_id,
   		"subAmount":getval
   		
   		});
	console.log(JSON.stringify(subDetails));
}

$(document).ready(function() {	
	var drDetails = [];
	setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);

	//code for update(push) depreciation details
	
	 <c:forEach var="dpreDetails"  items="${dAutoJV.depriciationSubledgerDetails}">
	// alert('${dpreDetails.subledger.subledger_Id}');
	 subDetails.push({
			"subId":'${dpreDetails.subledger.subledger_Id}',
			"subAmount":'${dpreDetails.subLedgerAmount}'
			});	
	 </c:forEach>  
	 console.log(JSON.stringify(subDetails));
    $("#amount").keypress(function(e) {
		if (!digitsAndDotOnly(e)) {
			return false;
		}
	}); 
   
});


function cancel(){
	 window.location.assign('<c:url value = "depreciationform"/>');	
}
	
function validateForm()
{
	 $("#Amount").val(sum);
		$("#dr_ledgerlist").val(JSON.stringify(subDetails)); 
		
		var date = document.getElementById("date").value;
		
		
			 if(date=="")
			{
			alert("Please Select Date ");
			return false;
			}
}
	
	
	
	
// Code  for Soft Deletion of a record From a table
$("#officers-table").on('click', '.fa-times', function(e) {
    var whichtr = $(this).closest("tr");
    whichtr.remove();      
});


 function setDate(e){
 	document.getElementById("from_date").value = e.value;	
 	// date format validation
 	var datevali = document.getElementById("date").value;
 if(isValidDate(datevali)==true){
 	 
 		 return true;
  }else{
 	alert("Invalid Date");
 	window.location.reload();
 	
 }  
 }

 function isValidDate(dateStr) {
 	
 	 var datePat= /^((0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2})*$/;
 	 
 	 var matchArray = dateStr.match(datePat); // is the format ok?
 			 
 	 if (matchArray == null) {
 	  return false;
 	 }
 	 
 	 month = matchArray[2];
 	 day   = matchArray[3];
 	 
 	 
 	 if (month < 1 || month> 12) { // check month range
 		  alert("Month must be between 1 and 12");
 		  return false;
 		 }
 	 
 	 if (day < 1 || day > 31) {
 		  alert("Day must be between 1 and 31");
 		  return false;
 		 }
 	
 	 return true;  // date is valid
 	}

 function saveyearid(){
	 $('#year-model').modal('hide');
}

 $(function() {
 		$( "#date" ).datepicker({maxDate:0});
 		
    });

	
	var datefield=document.getElementById("date");
	
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
		 var yyyy=parseInt(res[2]);
		 
		    var nd = res[1]+"/"+res[0]+"/"+res[2];
		    var ud = new Date(nd);
			var td = new Date();
			
			ud.setHours(0,0,0,0);
			
			td.setHours(0,0,0,0);
		var flag = 	 checkDate(mm,dd,yyyy);
		
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
	 
	 function checkDate(dd,mm,yyyy)
	 {
		// alert("dd:"+dd+"mm:"+mm+"yy:"+yyyy);
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
				var flag = yyyy%4 == 0 & yyyy%100 != 0 || yyyy %400 == 0; 
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
	 
</script> 