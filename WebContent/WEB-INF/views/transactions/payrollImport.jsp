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
<c:if test="${filemsg != null}">
				<!-- Small modal -->
			<div id="file-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	     		<div class="modal-dialog ">
	          		<div class="modal-content">
	              		<div class="modal-header">
	                   		<h4 class="modal-title" id="mySmallModalLabel">Import Status</h4>
	               		</div>
		   				<div class="modal-body">
		   			
		   				<label style="color:red;">${filemsg}</label>
				            
			               
			
						</div>
						<div class="modal-footer">
						<iframe id="invisible" style="display:none;"></iframe>
 						<button class="fassetBtn" type="button" onclick="cancel1()">
					<spring:message code="btn_cancel" />
				</button>
 					</div>
	       			</div><!-- /.modal-content -->
	      		</div>
	      		
			</div>
</c:if>
<c:if test="${success != null}">

<c:if test="${success == 1}">	
<div class="clearfix"></div>		
 	<div class = "borderForm" >
	 	<div class="col-sm-offset-3 col-sm-5 col-xs-12">
		 	<div class="table-responsive">
				<table class="table">
					<thead>
					<tr>
						<th>Subledger-Name</th>
						<th>Amount(Dr)</th>
							<th>Amount(Cr)</th>
								
					</tr>
					</thead>
					<tbody>
								
						<c:forEach var="subledger"
							items="${payroll.payrollSubledgerDetails}">
							<c:if test="${subledger.drAmount!=null}">
							<tr>

								<td style="text-align: left;">${subledger.subledgerName}</td>
								<td style="text-align: right;">${subledger.drAmount}</td>
								<td style="text-align: right;">${subledger.crAmount}</td>
							</tr>
							</c:if>
							
						</c:forEach>
						<c:forEach var="subledger"
							items="${payroll.payrollSubledgerDetails}">
							<c:if test="${subledger.crAmount!=null}">
							<tr>

								<td style="text-align: left;">${subledger.subledgerName}</td>
								<td style="text-align: right;">${subledger.drAmount}</td>
								<td style="text-align: right;">${subledger.crAmount}</td>
							</tr>
							</c:if>
							
						</c:forEach>
			<%-- 		<c:forEach var = "subledger" items = "${payroll.payrollSubledgerDetails}">
					<tr>
									
							<td style="text-align: left;">${subledger.subledgerName}</td>
							<td style="text-align: right;">${subledger.crAmount}</td>
							<td style="text-align: right;">${subledger.drAmount}</td>
						</tr>
					</c:forEach> --%>
					</tbody>
				</table>
			</div>
		</div><div class="clearfix"></div>
	</div>
		   			<div class="clearfix"></div>
		
     <div class="row" style="text-align: center; margin: 15px;">
				<button class="fassetBtn" type="button" onclick="ok()">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
				<%-- <button class="fassetBtn" type="button" onclick= "editpayrollAutoJV('${payroll.payroll_id}')">
					<spring:message code="Edit" />
				</button> --%>
             </div>	
	       			

			
</c:if>


</c:if>

<script>

$(document).ready(function () {


		   $('#file-model').modal({
		    	show: true,
		   	});	
	
 
		   
});





function cancel1(){
	//alert("cancel1");
	
	/* const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    // the filename you want
    a.download = 'todo-1.json';; */
    let link = document.createElement("a");
    link.download = "ErrorFile.txt";
    link.href ="resources/templates/ErrorFile.txt";
    link.click();
    //var iframe = document.getElementById('invisible');
    //iframe.src = "resources/templates/ErrorFile.txt";
	//window.location='resources/templates/ErrorFile.txt';
	
	window.location.assign('<c:url value="payrollAutoJVList"/>');
}

function cancel(){
	//alert("cancel");
	window.location.assign('<c:url value="editpayrollAutoJV"/>');
}
function ok(){
	window.location.assign('<c:url value="savePayrollImport"/>');
}



function editpayrollAutoJV(id)
{
	
	window.location.assign('<c:url value="editpayrollAutoJV"/>?id='+id);
}

</script>

<%@include file="/WEB-INF/includes/footer.jsp" %>
