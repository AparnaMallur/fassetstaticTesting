<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Sub Ledger</h3>
	<a href="homePage">Home</a> » <a href="subledgerList">Sub Ledger</a> »
	<a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="SubLedgerForm" action="savesubledger" method="post"
			commandName="subledger">
			<div class="row">
				<form:input path="subledger_Id" hidden="hidden" />
				<form:input path="created_date" hidden="hidden" />
				<form:input path="company_id" id="company_id" type="hidden" value="${company_id}" />
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Ledger<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:select path="ledger_id" class="logInput" id="ledger">
						<form:option value="0" label="Select Ledger" />
						<%-- <c:forEach var = "list" items = "${ledgerList}">							
							<form:option value = "${list.ledger_id}">${list.ledger_name}</form:option>	
						</c:forEach> --%>
					</form:select>
					<span class="logError"><form:errors path="ledger_id" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Sub Ledger<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:input path="subledger_name" class="logInput"
						id="subledger_name" maxlength="30"
						placeholder="SubLedger Name" />
					<span class="logError"><form:errors path="subledger_name" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Status<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:radiobutton path="status" value="true" checked="checked" />
					Enable
					<form:radiobutton path="status" value="false" />
					Disable
				</div>
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
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#subledger_name").keypress(function(e) {
			if (!lettersAndHyphennoquote(e)) {
				return false;
			}
		});
		$("#credit_opening_balance,#debit_opening_balance").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		
		var ledgerList = [];		
		  
	    $('#ledger').find('option').remove();
		$('#ledger').append($('<option>', {
			value: 0,
			text: 'Select Ledger'
		}));	
		<c:forEach var = "list" items = "${ledgerList}">
		var groupname = "${list.accsubgroup.subgroup_name}".replace('\'', '\'');
		var ledgername="${list.ledger_name}".replace('\'', '\'');
		if(groupname != 'Cash at Bank' && groupname != 'Cash &amp; Bank Balances' && groupname != 'Cash in Hand'){
			 var tempArray = [];
			    tempArray["id"]=${list.ledger_id};
			    tempArray["name"]=ledgername;
			    ledgerList.push(tempArray);
				
			    ledgerList.sort(function(a, b) {
		            var textA = a.name.toUpperCase();
		            var textB = b.name.toUpperCase();
		            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		        });		
		}
		</c:forEach>
		
	  	for(i=0;i<ledgerList.length;i++){
			 $('#ledger').append($('<option>', {
			    value: ledgerList[i].id,
			    text: ledgerList[i].name,
		    })); 
		} 
		
		<c:if test="${subledger.ledger_id != null}">
	   		$("#ledger").val(${subledger.ledger_id});
        </c:if>
	});
	
	function cancel(){
		window.location.assign('<c:url value = "subledgerList"/>');	
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>