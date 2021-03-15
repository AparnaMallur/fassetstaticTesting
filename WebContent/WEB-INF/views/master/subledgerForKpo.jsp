<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Sub Ledger</h3>					
	<a href="homePage">Home</a> » <a href="subledgerList">Sub Ledger</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="SubLedgerForm" action="savesubledger" method="post" commandName = "subledger">
			<div class="row">
				<form:input path="subledger_Id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			
			<div class="row">			
				<div class="col-md-2 control-label"><label>Company Name<span>*</span></label></div>
				<div class = "col-md-10">
					<form:select path="company_id" class="logInput" placeholder="Company Name" onChange="getLedgerList(this.value)">
						<form:option value="0" label="Select Company Name"/>	
						<c:forEach var = "company" items = "${companyList}">													
							<form:option value = "${company.company_id}">${company.company_name}</form:option>	
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="company_id" /></span>
				</div>
			</div>
			
					
			<div class="row">	
				<div class="col-md-2 control-label"><label>Ledger<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:select path="ledger_id" id="ledgerId" class="logInput">
						<form:option value="0" label="Select Ledger"/>								
					</form:select>
					<span class="logError"><form:errors path="ledger_id" /></span>
				</div>
			</div>			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Sub Ledger<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:input path="subledger_name" class="logInput"
						id = "subledger_name" maxlength="${SIZE_THREE_HUNDRED}" placeholder="SubLedger Name" />
					<span class="logError"><form:errors path="subledger_name" /></span>
				</div>
			</div>	
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Set Default<span>*</span></label></div>		
				<div class="col-md-10">					
					<form:radiobutton path="setDefault" value="true"  />Yes 
					<form:radiobutton path="setDefault" value="false"  />No
				</div>
			</div>	
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
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
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

	    $("#subledger_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		$("#credit_opening_balance,#debit_opening_balance").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		
		<c:if test="${subledger.ledger_id != null}">
			getLedgerList('${subledger.company_id}');
			//alert(${subledger.ledger_id});
	   		$("#ledgerId").val(${subledger.ledger_id});
        </c:if>
	});
	function cancel(){
		window.location.assign('<c:url value = "subledgerList"/>');	
	}
	
	function getLedgerList(company_id){	
		var ledgerArray = [];
		
		$.ajax({
	        type: "POST",
	        url: "getledgerList?id="+company_id,
	        contentType: 'application/json',
	        dataType: 'json',
	        async: false,	        
	        success: function (data) {
	        	console.log("User Name==="+data);
        	 	$('#ledgerId').empty();
        	 	$('#ledgerId').find('option').remove();
				$('#ledgerId').append($('<option>', {
				    value: 0,
				    text: 'Select Ledger'
				}));
	        	 
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
		 			
        	 	for(i=0;i<ledgerArray.length;i++){
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
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>