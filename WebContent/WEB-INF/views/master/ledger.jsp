<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Ledger</h3>					
	<a href="homePage">Home</a> » <a href="ledgerList">Ledger</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>

<style>
input[type="checkbox"][readonly] 
{
 pointer-events: none;
}
</style>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="LedgerForm" action="saveledger" method="post" commandName = "ledger">
			<div class="row">
				<form:input path="ledger_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
			</div>
			<c:choose>
				<c:when test="${((role == '2') || (role == '3') ||(role == '4'))}">
						<div class="row">			
						<div class="col-md-2 control-label"><label>Company Name<span>*</span></label></div>
						<div class = "col-md-10">
							<form:select path="company_id" class="logInput">
								<form:option value="0" label="Select Company Name"/>					
								<c:forEach var = "company" items = "${companyList}">													
									<form:option value = "${company.company_id}">${company.company_name}</form:option>	
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="company_id" /></span>
						</div>
						</div>
				</c:when>
				<c:otherwise>
						<form:input path="company_id" id="company_id" type="hidden"
								value="${company_id}" />
				</c:otherwise>
			</c:choose>
		
		 <div class="row">	
				<div class="col-md-2 control-label"><label>Sub Group<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="subgroup_Id" id="subGroup" class="logInput">
					<form:option value="0" label="Select Sub Group"/>						
						<%-- <c:forEach var = "list" items = "${accountSubGroupList}">							
							<form:option value = "${list.subgroup_Id}">${list.accountGroup.group_name}-${list.subgroup_name}</form:option>	
						</c:forEach> --%>									
					</form:select>
					<span class="logError"><form:errors path="subgroup_Id" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Ledger Name<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="ledger_name" class="logInput"
						id = "ledger_name" maxlength="50" placeholder="Ledger Name"/>
					<span class="logError"><form:errors path="ledger_name" /></span>
				</div>	
			</div>
				<c:choose>
					<c:when test='${ledger.ledger_id==Null}'>
						<form:input path="credit_opening_balance1" class="logInput" id = "credit_opening_balance"  placeholder="Credit Opening Balance"  value="0"  type="hidden" />
						<form:input path="debit_opening_balance1" class="logInput" id = "debit_opening_balance"  placeholder="Debit Opening Balance" value="0"  type="hidden" />
					</c:when>
					<c:otherwise>
					<form:input path="credit_opening_balance1" class="logInput"
						id = "credit_opening_balance"  placeholder="Credit Opening Balance"  type="hidden" />
					<form:input path="debit_opening_balance1" class="logInput"
						id = "debit_opening_balance"  placeholder="Debit Opening Balance" type="hidden" />
					</c:otherwise>
				</c:choose>

			<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>	
			</div>
			<div class="row check-center">	
				<div class="col-md-12">
					
					
					<c:if test="${ledger.ledger_id != null}">
						<form:input path="as_subledger" id = "as_subledger" hidden = "hidden" readonly="true"/>
					</c:if>
					<c:if test="${ledger.ledger_id == null}">
						<form:input path="as_subledger" id = "as_subledger" hidden = "hidden"/>
						<input type="checkbox" id = "subledger"><label>Is this your Sub Ledger</label>
					</c:if>
					
					
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
		
		$("#as_subledger").val(false);
		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#ledger_name").keypress(function(e) {
			if (!lettersAndHyphennoquote(e)) {
				return false;
			}
		});	
		
		
		$("#subledger").change(function(){
			if(this.checked){
				alert = "checked"
				$("#as_subledger").val(true);
				var test = document.getElementById("as_subledger").value;
			}
		});		
	});
	
	$( document ).ready(function() {	
	    var as_subledger = '<c:out value= "${ledger.as_subledger}"/>';	
	    if(as_subledger=="true")
	    	{
		$("#subledger").prop( "checked", true );
	    	}	    
	    
	    var groupLiabilities = [];	
	  
	    
	    $('#subGroup').find('option').remove();
		$('#subGroup').append($('<option>', {
		value: 0,
		text: 'Select Sub Group'
		}));	
		
		
	    //Liability List
		<c:forEach var = "list1" items = "${accountSubGroupList}">
		var groupname="${list1.accountGroup.group_name}".replace('\'', '\'');
			var sname="${list1.subgroup_name}".replace('\'', '\'');
			
			    var tempArray = [];
			    tempArray["id"]=${list1.subgroup_Id};
			    tempArray["name"]=groupname+' - '+sname;
			    groupLiabilities.push(tempArray);
				
			    groupLiabilities.sort(function(a, b) {
		            var textA = a.name.toUpperCase();
		            var textB = b.name.toUpperCase();
		            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		        });	
		</c:forEach>
		
		  for(i=0;i<groupLiabilities.length;i++)
			{
				 $('#subGroup').append($('<option>', {
			    value: groupLiabilities[i].id,
			    text: groupLiabilities[i].name,
			})); 
			} 
		     	
		      <c:if test="${ledger.accsubgroup != null}">
		      	$('#subGroup').val(${ledger.accsubgroup.subgroup_Id});
			  </c:if>	
			  
	});
	function cancel(){
		window.location.assign('<c:url value = "ledgerList"/>');	
	}	
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>