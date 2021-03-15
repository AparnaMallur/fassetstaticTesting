<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_HUNDRED"><%=MyAbstractController.SIZE_HUNDRED%></c:set>
<div class="breadcrumb">
	<h3>Industry Type</h3>					
	<a href="homePage">Home</a> » <a href="industrytypeList">Industry Type</a>  » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="IndustryTypeForm" action="saveindustrytype" method="post" commandName = "type" onsubmit = "return addSubledger();">
			<div class="row">				
				<form:input path="industry_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
				<form:hidden path="subLedgerList" id = "subLedgerList"/>
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Industry Name<span>*</span></label></div>		
				<div class="col-md-10">		
					<form:input path="industry_name" class="logInput" id = "industry_name" maxlength="30" placeholder="Industry Name" />
					<span class="logError"><form:errors path="industry_name" /></span>
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
				<div class="col-md-2 control-label"><label>Ledger Subledger<span>*</span></label></div>		
				<div class="col-md-10">	
				<div class="row">
				
				
						  
				<c:forEach var = "ledger" items = "${ledgerList}">
			<c:set var="total_defaultledger" value="0" />
				<c:set var="total_subledger" value="" />
						 <c:forEach var = "subledger" items = "${ledger.subLedger}">
						 			<c:set var="total_subledger" value="${total_subledger+1}" />	
						 			<c:if test="${subledger.setDefault==true}">
									<c:set var="total_defaultledger" value="${total_defaultledger+1}" />	
						 			</c:if>
						 
						</c:forEach>
					<c:if test="${total_defaultledger!=total_subledger}"> 			
						
						<div class="info-sidebar col-md-4 col-sm-6 " >
		                                        <h4 class="m-b-30 m-t-0">${ledger.ledger_name}</h4>
		                                   <div class="info-data">		  
								          <c:choose>
						                	<c:when test="${not empty ledger.subLedger}">
						             			<c:forEach var = "subledger" items = "${ledger.subLedger}">
						             			<c:if test="${subledger.status==true}">
													                             <p class="font-600 m-b-5">${subledger.subledger_name} 
														                             <span class="text-primary pull-left">
														                          			 <input style="margin-right:5px" name="32-ck" id="32-ck" value="${subledger.subledger_Id}" type="checkbox" onchange = "addSub(this)">
														                              </span>	                             
													                             </p>											   </c:if>	
											   </c:forEach>	
											</c:when>
						                	<c:otherwise>
												No Data
											</c:otherwise>
						                </c:choose>		                                     
		                                   </div>                                  
		                 </div>
		                   </c:if>
		         </c:forEach>
              
				
                 
                 
                
                 	</div>
		<div class="info-sidebar col-md-12 " >
		                  <h4 class="m-b-30 m-t-0">Added Subledgers</h4>
				<c:forEach var = "subledger" items = "${subLedgers}">
				 <p class="indus-sub font-600 m-b-5">${subledger.subledger_name} 
					<span class="text-primary pull-left">
						<a href = "#" onclick = "deleteSubledger('${subledger.subledger_Id}')"><img src='${deleteImg}' style = "width: 20px;"/></a>
					</span>	                             
				</p>												                             
				
			</c:forEach>
			</div>
				</div>
			</div>		
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
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
	var subList = []; 
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#industry_name").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		
	});
	
	function deleteSubledger(id){
		window.location.assign('<c:url value="deleteIndustrySubLedger"/>?id='+id);
	}
	
	function cancel(){
		window.location.assign('<c:url value = "industrytypeList"/>');	
	}
	
	function addSub(subledger){
		var subValue = subledger.value;
		if(subledger.checked){
			subList.push(subValue);
			console.log(subList);
		}
		else{
			for(i = 0; i<subList.length; i++){
				if(subList[i] == subValue){
					subList.splice(i,1);
					break;
				}
			}
			console.log(subList);
		}
	}
	
	function addSubledger(){
		var subLedgers = '<c:out value= "${type.subLedgers}"/>';	
		if((subList=="")&&(subLedgers=="[]"))
		{
			alert("Please select Sub Ledger for industry type");
			return false;
		}
		else
		{
		$("#subLedgerList").val(subList);
		return true;
		}
	}
	/* 
	$('#allcb').change(function(){
	    if($(this).prop('checked')){
	        $('tbody tr td input[type="checkbox"]').each(function(){
	            $(this).prop('checked', true);
	        });
	    }else{
	        $('tbody tr td input[type="checkbox"]').each(function(){
	            $(this).prop('checked', false);
	        });
	    }
	}); */
	</script>
	
	<%@include file="/WEB-INF/includes/footer.jsp" %>