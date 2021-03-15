<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />

 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 
 
  <c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	<spring:url value="/resources/js/report_table/ledgerReportForSubLedgerAll.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
	 </c:if>
	<c:if test="${subledgerId!=0}">
	 <spring:url value="/resources/js/report_table/ledgerReportForSubLedger.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
 </c:if>
  </c:if>
  
    <script type="text/javascript">
 
 function pdf(selector, params) {
	 selector = "#tableDiv";
	 $("#tableDiv").css("display", "block"); 	 	
     var options = {
       //ignoreRow: [1,11,12,-2],
       //ignoreColumn: [0,-1],
       //pdfmake: {enabled: true},
       tableName: 'Countries',
       worksheetName: 'Countries by population'
     };
     $.extend(true, options, params);
     $(selector).tableExport(options);
     $("#tableDiv").css("display", "none"); 	
   }
 
</script>

<div class="breadcrumb">
	<h3>Ledger Report </h3>
	<a href="homePage">Home</a> » <a href="ledgerReport">Ledger Report</a>
</div>

<div class="col-md-12">
<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	
		<!-- Excel Start -->
				<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center"><td></td><td> Ledger Report</td></tr>
						<tr><td colspan='5'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='5'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='5'>
								<fmt:parseDate value="${fromDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${toDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='5'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
			<!-- Date -->
		<table  >
			<thead>
				<tr>
					<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
						<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>		
			<c:forEach var="sub" items="${allsubList}">
			<c:if test="${sub.size()!=0}">
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				 <c:set var="subID" value="0"/>
				  <c:set var="sublegderName"/>
					<c:forEach var="subform" items="${sub}">
			    <c:set var="sublegderName" value="${subform.subLedger.subledger_name}"/>
			     <c:set var="subID" value="${subform.subLedger.subledger_Id}"/>
				</c:forEach>
				
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				<tr>
					<td><strong>${sublegderName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
						 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
					  
				
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <c:choose>
					   <c:when test="${empty(subledgerOpenBalanceList)}">
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>	
				    </c:when>
				    <c:otherwise>
			          <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				     <c:if test="${openingbalance.sub_id == subID}">
					 <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				   <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				   <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>
				    </c:forEach>				
				    </c:otherwise>
				    </c:choose>
				    
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${sub}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
				
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
								<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  	   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						<%-- ${balance.sales.customer.firm_name} --%>
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
							   							    <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  		    <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				
					<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td ></td>
				    <td ></td>
				    </tr>
				    
		
		  </c:if>
		   <c:set var="row_running" value="0"/>
		  </c:forEach> 
			</tbody>
		</table>
	</div>
	</c:if>
	
	<c:if test="${subledgerId!=0}">
	<div style="display:none" id="excel_report">
					<!-- Date -->
					<table>
						<tr><td colspan='5'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='5'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='5'>
								<fmt:parseDate value="${fromDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${toDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='5'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
			<!-- Date -->
		<table >
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
						<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>		
			<tbody>
								
			<c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subledgerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
					<td><strong>${subName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
					
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <c:choose>
					  <c:when test="${subledgerOpenBalanceList.size()!=0}">
					  <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
					   <c:if test="${openingbalance.sub_id == subledgerId}">
					  <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>	
				   </c:forEach>
				       </c:when>
				    <c:otherwise>
						 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>						
				    </c:otherwise>
				    </c:choose>
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${subledgerOPBalanceList}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						<%-- ${balance.sales.customer.firm_name} --%>
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
							   							    <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 

				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			
			</tbody>
		</table>
	</div>
	 <c:set var="row_running" value="0"/>
	</c:if>
	</c:if>
	
	<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	
	
	<!-- pdf start -->
	
	
		<div class="table-scroll"  style="display:none;" id="tableDiv">
	
	
	<c:set var="rowcount" value="0" scope="page" />
	<c:if test="${rowcount == 0}">
	
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report22</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						
						<td align="center">${company.company_name}</td>
					</tr>
					<tr>
							<td align="center">Address: </td>
							<td></td>
							<td align="center">${company.permenant_address}</td>
					</tr>
					<tr>
						<td>
							<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" /> --%>
						From: 
						</td>
						<td></td>
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				  
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
						  <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
</c:if>				
				<tbody>
			
			
			<c:forEach var="sub" items="${allsubList}">
			<c:if test="${sub.size()!=0}">
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				 <c:set var="subID" value="0"/>
					<c:forEach var="subform" items="${sub}">
			    <c:set var="sublegderName" value="${subform.subLedger.subledger_name}"/>
			     <c:set var="subID" value="${subform.subLedger.subledger_Id}"/>
				</c:forEach>
				
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				<tr>
				
				<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/subledger-ledgerReportHeader.jsp"%>
								</c:if>
							
						<c:set var="rowcount" value="${rowcount + 1}" scope="page" />	
							
							
					<td><strong>${sublegderName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
					
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <c:choose>
					  <c:when test="${empty(subledgerOpenBalanceList)}">
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>	
				    </c:when>
				    <c:otherwise>
			          <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				     <c:if test="${openingbalance.sub_id == subID}">
					 <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>
				    </c:forEach>					
				    </c:otherwise>
				    </c:choose>
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${sub}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
					
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
							<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						<%-- ${balance.sales.customer.firm_name} --%>
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
				       <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				
					<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td ></td>
				    <td ></td>
				    </tr>
				    
		
		  </c:if>
		   <c:set var="row_running" value="0"/>
		  </c:forEach> 
			</tbody>
			</table>
	</div>
	
	</c:if>
	
	<c:if test="${subledgerId!=0}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
	
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report45</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						
						<td align="center">${company.company_name}</td>
					</tr>
					<tr>
							<td align="center">Address: </td>
							<td></td>
							<td align="center">${company.permenant_address}</td>
					</tr>
					<tr>
						<td>
							<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" /> --%>
						From: 
						</td>
						<td></td>
						<td>${from_date} To ${to_date}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					
					<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				   
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
						 <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
	</c:if>			
				<tbody>
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subledgerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
				
				
						<c:if test="${rowcount >  45}">
							<c:set var="rowcount" value="0" scope="page" />
						</c:if>
						<c:if test="${rowcount > 44}">
							<%@include file="/WEB-INF/views/report/subledger-ledgerReportHeader.jsp"%>
						</c:if>
					
					<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
					
					<td><strong>${subName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
						 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <c:choose>
					  <c:when test="${subledgerOpenBalanceList.size()!=0}">
					   <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
					   <c:if test="${openingbalance.sub_id == subledgerId}">
					   <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>
				     </c:forEach>
				     </c:when>
				    <c:otherwise>
						 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>						
				    </c:otherwise>
				    </c:choose>
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${subledgerOPBalanceList}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
					
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
							
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						<%-- ${balance.sales.customer.firm_name} --%>
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
							   							    <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
					
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
							<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
					
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
							<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					
				  <td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
	 <c:set var="row_running" value="0"/>
	</c:if>
	</c:if>
	
	<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	
	<div class="table-scroll">
	
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
	
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				   
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
						 <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				</c:if>
			</thead>
			<tbody>
			
			
			<c:forEach var="sub" items="${allsubList}">
			<c:if test="${sub.size()!=0}">
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				 <c:set var="subID" value="0"/>
				  <c:set var="sublegderName"/>
				<c:forEach var="subform" items="${sub}">
			    <c:set var="sublegderName" value="${subform.subLedger.subledger_name}"/>
			     <c:set var="subID" value="${subform.subLedger.subledger_Id}"/>
				</c:forEach>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				
				<tr>
					
					
					
					
						<c:if test="${rowcount >  45}">
							<c:set var="rowcount" value="0" scope="page" />
						</c:if>
						<c:if test="${rowcount > 44}">
							<%@include file="/WEB-INF/views/report/subledger-ledgerReportHeader.jsp"%>
						</c:if>
					
				<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
				
				
					<td><strong>${sublegderName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
				
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
					
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <c:choose>
					  <c:when test="${empty(subledgerOpenBalanceList)}">
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>	
				    </c:when>
				    <c:otherwise>
			          <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				     <c:if test="${openingbalance.sub_id == subID}">
					 <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>
				    </c:forEach>					
				    </c:otherwise>
				    </c:choose>
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${sub}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit Note</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  <!-- The Year End code for All subledger start -->
							  		   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  <!-- The year End code for All subledger Ends -->
							  
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							  <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
							   							    <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					
				  <td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				
					<tr>
					<td></td>
					<td ></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td ></td>
				    </tr>
				    
		
		  </c:if>
		   <c:set var="row_running" value="0"/>
		  </c:forEach> 
			</tbody>
		</table>
	</div>
	
	</c:if>
	
	<c:if test="${subledgerId!=0}">
	<div class="table-scroll">
	
	<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
	
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer/Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				 
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
						   <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				</c:if>
			</thead>
			<tbody>
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subledgerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
				
				
					<c:if test="${rowcount >  45}">
							<c:set var="rowcount" value="0" scope="page" />
						</c:if>
						<c:if test="${rowcount > 44}">
							<%@include file="/WEB-INF/views/report/subledger-ledgerReportHeader.jsp"%>
						</c:if>
					
				<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
				
				
					<td><strong>${subName}</strong></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
						  <c:choose>
					  <c:when test="${subledgerOpenBalanceList.size()!=0}">
					   <c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
					   <c:if test="${openingbalance.sub_id == subledgerId}">
					   <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				    <c:set var="row_running" value="${debit-credit}"/></td>
				    </c:if>
				      <c:if test="${openingbalance.subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong>
				    <c:set var="row_running" value="${credit-debit}"/></td>
				    </c:if>
				     </c:if>
				     </c:forEach>
				     </c:when>
				    <c:otherwise>
						 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong>
				         <c:set var="row_running" value="${debit-credit}"/></td>						
				    </c:otherwise>
				    </c:choose>
				</tr>
				
				
				
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
					
							<c:forEach var="balance" items="${subledgerOPBalanceList}">
							
							 <c:if test="${balance.sales!=null}">
							 <c:if test="${(balance.sales.entry_status !=null)&&((balance.sales.entry_status == '1')||(balance.sales.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.sales.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sales.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Sales</td>
					
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
							
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.receipt!=null}">
							 <c:if test="${(balance.receipt.entry_status !=null)&&((balance.receipt.entry_status == '1')||(balance.receipt.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.receipt.subLedger.subledger_name}
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <c:if test="${balance.credit!=null}">
							 <c:if test="${(balance.credit.entry_status !=null)&&((balance.credit.entry_status == '1')||(balance.credit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.credit.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Credit NotAAA</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							  <!-- Including Year End Jv entries -->
							  
							   <c:if test="${balance.yearEndJV!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.yearEndJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.yearEndJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
				
							
						</td>
						<td style="text-align: left;">Year End Journal</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  <!-- End of the code -->
							  <c:if test="${balance.purchase!=null}">
							 <c:if test="${(balance.purchase.entry_status !=null)&&((balance.purchase.entry_status == '1')||(balance.purchase.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.purchase.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Purchase</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  
							   <c:if test="${balance.payment!=null}">
							 <c:if test="${(balance.payment.entry_status !=null)&&((balance.payment.entry_status == '1')||(balance.payment.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						${balance.payment.subLedger.subledger_name}
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							   <c:if test="${balance.debit!=null}">
							 <c:if test="${(balance.debit.entry_status !=null)&&((balance.debit.entry_status == '1')||(balance.debit.entry_status=='2'))}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.debit.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Debit</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
					<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 </c:if>
							  </c:if>
							  
							  <c:if test="${balance.contra!=null}">
							
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
							<c:if test="${(balance.contra.type != null)&&(balance.contra.type==2)}">
									Contra-Withdraw
									</c:if>
									<c:if test="${(balance.contra.type != null)&&(balance.contra.type==1)}">
									Contra-Deposit
									</c:if>
						<%-- ${balance.sales.customer.firm_name} --%>
							
						</td>
						<td style="text-align: left;">Contra</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							   <c:if test="${balance.mjv!=null && balance.mjv.entry_status!='3' && (balance.credit_balance>0 || balance.debit_balance>0)}">
							   <tr>
							   <td style="text-align: left;">
						<fmt:parseDate value="${balance.mjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.mjv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						
						${balance.sublegderCr}${balance.sublegderdr}
							
						</td>
						<td style="text-align: left;">Manual JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
							   </tr>
							   </c:if>
							   
							   
							    <c:if test="${balance.payAutoJv!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.payAutoJv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payAutoJv.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Payroll Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							    <c:if test="${balance.gstAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.gstAutoJV.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.gstAutoJV.voucher_no}</td>
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">GST Auto JV</td>
						
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							  <c:if test="${balance.depriAutoJV!=null}">
							 <tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${balance.depriAutoJV.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.depriAutoJV.voucher_no}</td>
						
						<td style="text-align: left;">
						
						${balance.subLedger.subledger_name}
							
						</td>
						<td style="text-align: left;">
						</td>
						<td style="text-align: left;">Depreciation Auto JV</td>
						
						
						<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
						 <c:set var="row_debit" value="${row_debit + balance.debit_balance}" />
						</c:if>
						<c:if test="${balance.debit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						<c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
						 <c:set var="row_credit" value="${row_credit + balance.credit_balance}" />
						</c:if>
						<c:if test="${balance.credit_balance==0}">
						<td style="text-align: left;"></td>
						</c:if>
						
						
						<td class="tright">
					<c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 1}">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				   
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3}">
				  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.debit_balance-balance.credit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>	
				    </c:if>
				      <c:if test="${balance.subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
				   <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(balance.credit_balance-balance.debit_balance)+row_running}" />
								 <c:set var="row_running" value="${(balance.credit_balance-balance.debit_balance)+row_running}"/>
				    </c:if>
				    </td>
					</tr>
							 
							  </c:if>
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
	 <c:set var="row_running" value="0"/>
	</c:if>
	</c:if>
	
</div>

<div class="col-md-12" style="text-align: center; margin: 15px;">
	  <c:if test="${role!=7}">
		<button class="fassetBtn" type="button"  onclick ="pdf('#Hiddentable', {type: 'pdf',
                jspdf: {
                    autotable: {
                        styles: {overflow: 'linebreak',
                             fontSize: 9,
                             rowHeight: 5},
                        headerStyles: {rowHeight: 10,
                             fontSize: 8},
                        bodyStyles: {rowHeight: 10}
                    }
                }});">
				Download as PDF
			</button>
			
			<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Ledger_SubLedgerAll Report")'>
				Download as Excel
			</button>
	 </c:if>
	<c:if test="${subledgerId!=0}">
	<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Ledger_SubLedger Report")'>
				Download as Excel
			</button>
 </c:if>
  </c:if>
		
			
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function viewTrialBalanceReport(id){
		window.location.assign('<c:url value="viewLedgerBalanceReport"/>?id='+id);
	}
	function back(){
		window.location.assign('<c:url value = "ledgerReport"/>');	
	}
	/* function pdf(){
		window.location.assign('<c:url value = "pdfLedgerReport"/>');	
	} */
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>