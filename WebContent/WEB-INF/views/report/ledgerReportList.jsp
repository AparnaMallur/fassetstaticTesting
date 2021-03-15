<%-- <%@include file="/WEB-INF/includes/header.jsp"%>
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
  
  
 <c:if test="${option==1}">
	<c:if test="${customerId == -1}">
	<spring:url value="/resources/js/report_table/ledgerReportForCustomerAll.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
	 </c:if>
	<c:if test="${customerId!= -1}">
	 <spring:url value="/resources/js/report_table/ledgerReportForCustomer.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
 </c:if>
  </c:if>
  
  
  <c:if test="${option==2}">
	<c:if test="${supplierId == -2}">
	<spring:url value="/resources/js/report_table/ledgerReportForSupplierAll.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
	 </c:if>
	<c:if test="${supplierId!= -2}">
	 <spring:url value="/resources/js/report_table/ledgerReportForSupplier.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
 </c:if>
  </c:if>
  
  <c:if test="${option==4}">
   <c:if test="${bankId == -4}">
	<spring:url value="/resources/js/report_table/ledgerReportForBankAll.js" var="tableexport" />
 <script type="text/javascript" src="${tableexport}"></script>
	 </c:if>
	<c:if test="${bankId!=-4}">
	 <spring:url value="/resources/js/report_table/ledgerReportForBank.js" var="tableexport" />
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
	<h3>Ledger Report</h3>
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				</tr>
			</thead>
			<tbody>		
			<c:forEach var="sub" items="${allsubList}">
			<c:if test="${sub.size()!=0}">
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				 <c:set var="subID" value="0"/>
				<c:forEach var="subID" items="${sub}">
			    <c:set var="subID" value="${subID.subLedger.subledger_Id}"/>
				</c:forEach>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
					  
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
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
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
	</c:if>
	</c:if>



<c:if test="${option==1}">
	<c:if test="${customerId == -1}">
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
		<table>
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>		
			
			
			<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
			<c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.receipts != null}">
		    <c:forEach var="receipt" items="${ledgerForm.receipts}">
		    <c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
			 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isCustomer==1}">
		     	<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customer.customer_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
				<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customer.customer_id}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
				</c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
			
			
 <c:if test="${customerId != -1}">
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
		<table>
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="Name" data-filter-control="input"
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>		
		     	<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customerId}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customerId}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customerId}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
				 	
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
 </c:if>	 
 </c:if>
	
<c:if test="${option==2}">
	<c:if test="${supplierId == -2}">
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			
			<c:forEach var="supplier" items="${supplierlist}">
			
			<c:set var="isSupplier" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			<c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.payments != null}">
			<c:forEach var="payment" items="${ledgerForm.payments}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isSupplier==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplier.supplier_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
										
								</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplier.supplier_id}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" />
								 <c:set var="row_debit" value="${row_debit + debitNote.round_off}" />
						</td>
						
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-debitNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-debitNote.round_off)+row_running}" />	
						</td>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
					
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
					<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
 <c:if test="${supplierId != -2}">
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			   <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplierId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplierId}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplierId}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
					   		</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplierId}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" />
								 <c:set var="row_debit" value="${row_debit + debitNote.round_off}" />
						</td>
						
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-debitNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-debitNote.round_off)+row_running}" />	
						</td>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				
					
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				   <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>		
 </c:if>	 
 </c:if>

<c:if test="${option==4}">

	<c:if test="${bankId == -4}">
	
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				
				</tr>
			</thead>
			<tbody>		
					<c:forEach var="bank" items="${banklist}">
			<c:set var="isBank" value="0"/>
			
			<c:forEach var="receipt" items="${receiptList}">
				<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
				<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
	                 <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
			<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
			
			<c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       <c:forEach var="payment" items="${paymenttList}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
               <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
		    <c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
			
		  <c:set var="isBank" value="1"/>
		  </c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       
			<c:forEach var="contra" items="${contraList}">
			<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
			<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
			 <c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
			
			<c:if test="${isBank==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bank.bank_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
				<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bank.bank_id)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
							
							    <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">ts="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>	
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>	
		</c:forEach>		
			</tbody>
		</table>
	</div>		
	</c:if>
			
			
 <c:if test="${bankId != -4}">
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
		<table>
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
					<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bankId)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bankId)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bankId)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bankId))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bankId))}">
							
							   <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				
					
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
</div>
</c:if>
</c:if>
	<!------------------------------------------------------------ Excel End ---------------------------------------------------->
	
	
	<!-- pdf starts ------------------------------------------------------------------------>
	
	<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	
		<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
			
			<c:forEach var="sub" items="${allsubList}">
			<c:if test="${sub.size()!=0}">
			
			 <c:set var="credit" value="0"/>		
			 <c:set var="debit" value="0"/>
				
				 <c:set var="subID" value="0"/>
				<c:forEach var="subID" items="${sub}">
			    <c:set var="subID" value="${subID.subLedger.subledger_Id}"/>
				</c:forEach>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
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
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					
				  <td ></td>
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
	</c:if>
	</c:if>
	
	<!-- -------------------------------------------------------------------------------------------------------- -->
	
	<c:if test="${option==1}">
	<c:if test="${customerId == -1}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
				<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
			<c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.receipts != null}">
		    <c:forEach var="receipt" items="${ledgerForm.receipts}">
		    <c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
			 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isCustomer==1}">
		     	<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customer.customer_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customer.customer_id}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				   <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
				</c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
	
	<c:if test="${customerId != -1}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Cash/Bank Book</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customerId}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customerId}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customerId}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
					
				 	
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
 </c:if>	 
 </c:if>
 
 <!-- ----------------------------------------------------------------------------------------- -->
 
 
 <c:if test="${option==2}">

	<c:if test="${supplierId == -2}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
			
			<c:forEach var="supplier" items="${supplierlist}">
			
			<c:set var="isSupplier" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			<c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.payments != null}">
			<c:forEach var="payment" items="${ledgerForm.payments}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isSupplier==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplier.supplier_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount+payment.tds_amount}" />
					             	</td>
					             	
					      		<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
										
								</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplier.supplier_id}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>

				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
					<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
	
	<c:if test="${supplierId != -2}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
			 <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplierId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplierId}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplierId}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td style="text-align: left;">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
										
								</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplierId}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" />
								 <c:set var="row_debit" value="${row_debit + debitNote.round_off}" />
						</td>
						
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-debitNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-debitNote.round_off)+row_running}" />	
						</td>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>		
 </c:if>	 
 </c:if>
	
	<!--  -------------------------------------------------------------------------------------------->
	
	<c:if test="${option==4}">

	<c:if test="${bankId == -4}">
	
	  <div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Ledger Report</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
			<c:forEach var="bank" items="${banklist}">
			<c:set var="isBank" value="0"/>
			
			<c:forEach var="receipt" items="${receiptList}">
				<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
				<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
	                 <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
			<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
			
			<c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       <c:forEach var="payment" items="${paymenttList}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
               <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
		    <c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
			
		  <c:set var="isBank" value="1"/>
		  </c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       
			<c:forEach var="contra" items="${contraList}">
			<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
			<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
			 <c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
			
			<c:if test="${isBank==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bank.bank_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
				<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bank.bank_id)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
							
							    <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">ts="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>	
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				 
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>	
		</c:forEach>
	   		
			</tbody>
		</table>
</div>
	</c:if>
	
	<c:if test="${bankId != -4}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Cash/Bank Book</td>
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
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
				
				<tbody>
			
			<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
					<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bankId)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bankId)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bankId)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bankId))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bankId))}">
							
							   <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				 	
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				 
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
	   		
			</tbody>
		</table>
</div>
</c:if>
</c:if>
	
	
	
<!-- pdf ends ------------------------------------------------------------------------>
	
	
	
	<c:if test="${option==3}">
	<c:if test="${subledgerId==0}">
	
	<div class="table-scroll">
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
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
				<c:forEach var="subID" items="${sub}">
			    <c:set var="subID" value="${subID.subLedger.subledger_Id}"/>
				</c:forEach>
				
				<c:forEach var="openingbalance" items="${subledgerOpenBalanceList}">
				 <c:if test="${openingbalance.sub_id == subID}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>
				</c:forEach>
				
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					
				  <td ></td>
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
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
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
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
						
						${balance.receipt.customer.firm_name}
							
						</td>
						<td style="text-align: left;">Receipt</td>
						
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
						
						${balance.payment.supplier.company_name}
							
						</td>
						<td style="text-align: left;">Payment</td>
						
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
							  
							</c:forEach>
				
       		     <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
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
	</c:if>
	</c:if>



<c:if test="${option==1}">
	<c:if test="${customerId == -1}">		
			<div class="table-scroll">
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
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
				<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
			<c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.receipts != null}">
		    <c:forEach var="receipt" items="${ledgerForm.receipts}">
		    <c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
			 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isCustomer==1}">
		     	<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customer.customer_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customer.customer_id}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customer.customer_id}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customer.customer_id}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
				</c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
			
			
 <c:if test="${customerId != -1}">
 		<div class="table-scroll">
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
						data-sortable="true">Customer Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
								
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
				
				<c:if test="${(ledgerForm.type == 'Sales')&&(ledgerForm.flag=='true')}">
				 <c:if test="${ledgerForm.customer.customer_id == customerId}">
					<c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">				
							
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.customer!=null}">
						${ledgerForm.customer.firm_name}
						</c:if>	
						</td>
						<td style="text-align: left;">${ledgerForm.type}</td>
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_debit" value="${row_debit + ledgerForm.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}"/>	
						</td>
					</tr>
					
					</c:if>	
				</c:if>	
				</c:if>	
					<c:if test="${ledgerForm.receipts != null}">
							<c:forEach var="receipt" items="${ledgerForm.receipts}">
							<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
							 <c:if test="${receipt.customer.customer_id == customerId}">
							<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
									 <c:if test="${receipt.payment_type !=null && receipt.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_credit" value="${row_credit + receipt.amount}" />
					     </c:if>	
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount+receipt.tds_amount))+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(receipt.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(receipt.amount))+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
						<c:if test="${ledgerForm.creditNotes != null}">
							<c:forEach var="creditNote" items="${ledgerForm.creditNotes}">
							
							<c:if test="${(creditNote.customer != null)&&(creditNote.flag==true)}">
			                <c:if test="${creditNote.customer.customer_id == customerId}">
			               <c:if test="${(creditNote.entry_status !=null)&&((creditNote.entry_status == '1')||(creditNote.entry_status=='2'))}">
			                <c:if test="${creditNote.date !=null &&  creditNote.date >= fromDate && creditNote.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${creditNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${creditNote.voucher_no}</td>
									<td style="text-align: left;">${creditNote.subledger.subledger_name}</td>
									<td style="text-align: left;">${creditNote.customer.firm_name}</td>
									<td style="text-align: left;">Credit Note</td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${creditNote.round_off}" />
								 <c:set var="row_credit" value="${row_credit + creditNote.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-creditNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-creditNote.round_off)+row_running}" />	
						</td>
							</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
						</c:if>
       		</c:forEach>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
					
				 	
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
 </c:if>	 
 </c:if>
	
<c:if test="${option==2}">

	<c:if test="${supplierId == -2}">
       <div class="table-scroll">
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			
			<c:forEach var="supplier" items="${supplierlist}">
			
			<c:set var="isSupplier" value="0"/>
		    <c:forEach var="ledgerForm" items="${ledgerReport}">
		    <c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			<c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
		    </c:if>	
		    </c:if>	
		    <c:if test="${ledgerForm.payments != null}">
			<c:forEach var="payment" items="${ledgerForm.payments}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
			<c:set var="isSupplier" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
		    </c:if>	
		    </c:forEach>
		    
		     <c:if test="${isSupplier==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplier.supplier_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplier.supplier_id}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplier.supplier_id}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount+payment.tds_amount}" />
					             	</td>
					             	
					      		<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
										
								</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplier.supplier_id}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>

				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
					<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>
				
		</c:forEach>
			</tbody>
		</table>
	</div>		
	</c:if>
 <c:if test="${supplierId != -2}">
	
<div class="table-scroll">
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
						data-sortable="true">Supplier Name</th>
				    <th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			 <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${supplierOpenBalanceList}">
				 <c:if test="${openingbalance.supplier_id == supplierId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit-debit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${credit-debit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>			
							
				<c:forEach var="ledgerForm" items="${ledgerReport}">
			<c:if test="${(ledgerForm.type == 'Purchase')&&(ledgerForm.flag=='true')}">									
			   <c:if test="${ledgerForm.supplier.supplier_id == supplierId}">
			    <c:if test="${(ledgerForm.entry_status !=null)&&((ledgerForm.entry_status == '1')||(ledgerForm.entry_status=='2'))}">
					<tr>
						<td style="text-align: left;">
						<fmt:parseDate value="${ledgerForm.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${ledgerForm.voucher_no}</td>
						<td style="text-align: left;">
						<c:if test="${ledgerForm.subledger!=null}">
						${ledgerForm.subledger.subledger_name}
						</c:if>	
						</td>
						<td style="text-align: left;">
							<c:if test="${ledgerForm.supplier!=null}">
									${ledgerForm.supplier.company_name}
							</c:if>	
						</td>	
						<td style="text-align: left;">${ledgerForm.type}</td>
						
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${ledgerForm.round_off}" />
								 <c:set var="row_credit" value="${row_credit + ledgerForm.round_off}" />
						</td>
						<td style="text-align: left;"></td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(ledgerForm.round_off-0)+row_running}" />
								 <c:set var="row_running" value="${(ledgerForm.round_off-0)+row_running}" />	
						</td>
						
					</tr>
				</c:if>	
				</c:if>	
				</c:if>	
				
					
					<c:if test="${ledgerForm.payments != null}">
							<c:forEach var="payment" items="${ledgerForm.payments}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							 <c:if test="${payment.supplier.supplier_id == supplierId}">
							  <c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                    <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
									<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
									
									 <c:if test="${payment.payment_type !=null && payment.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    <td style="text-align: left;"></td>
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_debit" value="${row_debit + payment.amount}" />
					             	</td>
					             	 </c:if>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
										
								</tr>
								
								</c:if>
								</c:if>
								</c:if>
								</c:if>
								
							</c:forEach>
					</c:if>
						
					<c:if test="${ledgerForm.debitNotes != null}">
							<c:forEach var="debitNote" items="${ledgerForm.debitNotes}">
							 <c:if test="${(debitNote.flag==true)}">
							   <c:if test="${debitNote.supplier.supplier_id == supplierId}">
							     <c:if test="${(debitNote.entry_status !=null)&&((debitNote.entry_status == '1')||(debitNote.entry_status=='2'))}">
			                       <c:if test="${debitNote.date !=null && debitNote.date >= fromDate && debitNote.date <= toDate}">
									<tr>
									<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${debitNote.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${debitNote.voucher_no}</td>
									<td style="text-align: left;">${debitNote.subledger.subledger_name}</td>	
									<td style="text-align: left;">${debitNote.supplier.company_name}</td>
									<td style="text-align: left;">Debit Note</td>
									
									<td style="text-align: left;"></td>
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" />
								 <c:set var="row_debit" value="${row_debit + debitNote.round_off}" />
						</td>
						
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-debitNote.round_off)+row_running}" />
								 <c:set var="row_running" value="${(0-debitNote.round_off)+row_running}" />	
						</td>
										
								</tr>
								</c:if>
								</c:if>
								</c:if>
								</c:if>
							</c:forEach>
				   </c:if>
       		</c:forEach>
       		<tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				  
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}" /></b></td>
				 
			
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>		
 </c:if>	 
 </c:if>

<c:if test="${option==4}">

	<c:if test="${bankId == -4}">
	<div class="table-scroll">
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach var="bank" items="${banklist}">
			<c:set var="isBank" value="0"/>
			
			<c:forEach var="receipt" items="${receiptList}">
				<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
				<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
	                 <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
			<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
			
			<c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       <c:forEach var="payment" items="${paymenttList}">
			<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
			<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
               <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
		    <c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
			
		  <c:set var="isBank" value="1"/>
		  </c:if>	
			</c:if>	
			</c:if>	
			</c:if>	
		       </c:forEach>
		       
		       
			<c:forEach var="contra" items="${contraList}">
			<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
			<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
			 <c:set var="isBank" value="1"/>
			</c:if>	
			</c:if>	
			</c:forEach>
			
			<c:if test="${isBank==1}">
			    <c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bank.bank_id}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
				
				<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bank.bank_id)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bank.bank_id)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bank.bank_id)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bank.bank_id))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bank.bank_id))}">
							
							    <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">ts="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						              <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>	
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				 
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td></td>
				</tr>
		   </c:if>	
		</c:forEach>
	   		
			</tbody>
		</table>
</div>
	</c:if>
			
			
 <c:if test="${bankId != -4}">
	
		<div class="table-scroll">
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
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>
			
			<c:set var="credit" value="0"/>		
				<c:set var="debit" value="0"/>
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
			     <tr>
					<td></td>
					<td>Opening Balance</td>
					<td ></td>
					<td ></td>
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
				
				<c:if test="${salesEntryList != null}">
							<c:forEach var="sales" items="${salesEntryList}">
								
							<c:if test="${(sales.bank!=null)&&(sales.bank.bank_id==bankId)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${sales.voucher_no}</td>
							      
							    
								 <td style="text-align: left;">${sales.subledger.subledger_name}</td>
							 
								 <td style="text-align: left;">Card Sales - ${sales.bank.bank_name} - ${sales.bank.account_no}</td>
								 <td style="text-align: left;">Sales</td>
								
						<td style="text-align: left;"></td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sales.round_off}" />
								 <c:set var="row_debit" value="${row_debit + sales.round_off}" />
						</td>
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(sales.round_off-0+row_running)}" />
								 <c:set var="row_running" value="${(sales.round_off-0)+row_running}"/>	
						</td>
						 </tr>
								</c:if>	
							</c:forEach>
				</c:if>
				
				<c:if test="${receiptList != null}">
							<c:forEach var="receipt" items="${receiptList}">
								<c:if test="${(receipt.customer != null)&&(receipt.flag==true)}">
								<c:if test="${(receipt.entry_status !=null)&&((receipt.entry_status == '1')||(receipt.entry_status=='2'))}">
			                   <c:if test="${receipt.date !=null && receipt.date >= fromDate && receipt.date <= toDate}">
							<c:if test="${(receipt.bank!=null)&&(receipt.bank.bank_id==bankId)&&(receipt.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${receipt.voucher_no}</td>
							      <c:if test="${receipt.payment_type !=null && receipt.payment_type!=1}"> 
							       <c:if test="${receipt.bank !=null}"> 
								 <td style="text-align: left;">${receipt.bank.bank_name}-${receipt.bank.account_no}</td>
							      </c:if>
							       <c:if test="${receipt.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${receipt.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
								 <td style="text-align: left;">${receipt.customer.firm_name}</td>
								 <td style="text-align: left;">Receipt</td>
								
								<td style="text-align: left;"></td>
						<td class="tright">
						 <c:if test="${receipt.advance_payment==true}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)}" />
								 <c:set var="row_debit" value="${row_debit + (receipt.amount+receipt.tds_amount)}" />
					     </c:if>	
					      <c:if test="${receipt.advance_payment ==false}"> 
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" />
								 <c:set var="row_debit" value="${row_debit + receipt.amount}" />
					     </c:if>	
						</td>
						
						<td class="tright">
						<c:if test="${receipt.advance_payment==true}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount+receipt.tds_amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount+receipt.tds_amount)+row_running}" />	
						 </c:if>
						 <c:if test="${receipt.advance_payment==false}"> 
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(receipt.amount)+row_running}" />
								 <c:set var="row_running" value="${(receipt.amount)+row_running}" />	
						 </c:if>
						</td>
						 </tr>
								
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
								
							</c:forEach>
				</c:if>
						
						<c:if test="${paymenttList != null}">
							<c:forEach var="payment" items="${paymenttList}">
							<c:if test="${(payment.supplier != null)&&(payment.flag==true)}">
							<c:if test="${(payment.entry_status !=null)&&((payment.entry_status == '1')||(payment.entry_status=='2'))}">
			                <c:if test="${payment.date !=null && payment.date >= fromDate && payment.date <= toDate}">
						<c:if test="${(payment.bank!=null)&&(payment.bank.bank_id==bankId)&&(payment.payment_type != 1)}">
								<tr>
									<td style="text-align: left;">
									<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${payment.voucher_no}</td>
							      <c:if test="${payment.payment_type !=null && payment.payment_type!=1}"> 
							       <c:if test="${payment.bank !=null}"> 
								 <td style="text-align: left;">${payment.bank.bank_name}-${payment.bank.account_no}</td>
							      </c:if>
							       <c:if test="${payment.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${payment.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
									<td style="text-align: left;">${payment.supplier.company_name}</td>
								    <td style="text-align: left;">Payment</td>
								    
									<c:if test="${payment.advance_payment==true}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(payment.amount+payment.tds_amount)}" />
								 <c:set var="row_credit" value="${row_credit + (payment.amount+payment.tds_amount)}" />
					             	</td>
					             	 </c:if>
					             	   <c:if test="${payment.advance_payment==false}"> 
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" />
								 <c:set var="row_credit" value="${row_credit + payment.amount}" />
					             	</td>
					             	 </c:if>
					             	 <td style="text-align: left;"></td>
						
						 <c:if test="${payment.advance_payment==true}"> 
						<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount+payment.tds_amount))+row_running}" />	
						</td>	
						</c:if>	
						 <c:if test="${payment.advance_payment==false}"> 
						 	<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(payment.amount))+row_running}" />
								 <c:set var="row_running" value="${(0-(payment.amount))+row_running}" />	
						</td>	
						 </c:if>
									
								</tr>
								</c:if>	
								</c:if>	
								</c:if>	
								</c:if>	
							</c:forEach>
					</c:if>
	   		
	   		
	   		              <c:if test="${contraList != null}">
							<c:forEach var="contra" items="${contraList}">
							<c:if test="${contra.date !=null && contra.date >= fromDate && contra.date <= toDate}">
							<c:if test="${((contra.deposite_to != null)&&(contra.deposite_to.bank_id==bankId))||((contra.withdraw_from!=null)&&(contra.withdraw_from.bank_id==bankId))}">
							
							   <tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">${contra.withdraw_from.bank_name}</td>
									</c:if>
									
									<td style="text-align: left;"></td>	
											
								
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
									<td style="text-align: left;">Contra-Withdraw</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;">Contra-Deposit</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
									<td style="text-align: left;">Contra-Transfer</td>
									</c:if>
									
									<c:if test="${(contra.type != null)&&(contra.type==2)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						</td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==1)}">
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
									</c:if>
									<c:if test="${(contra.type != null)&&(contra.type==3)}">
										<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_credit" value="${row_credit + contra.amount}" />
						               </td>
						               <td style="text-align: left;"></td>	
						               <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-contra.amount)+row_running}" />
								 <c:set var="row_running" value="${(0-contra.amount)+row_running}" />	
						             </td>
									</c:if>
									
					    		</tr>
					    		<c:if test="${(contra.type != null)&&(contra.type==3)}">
					    		<td style="text-align: left;">
										<fmt:parseDate value="${contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
									</td>
									<td style="text-align: left;">${contra.voucher_no}</td>	
									
									
									<td style="text-align: left;">${contra.deposite_to.bank_name}</td>
									
									
									<td style="text-align: left;"></td>	
											
									
									<td style="text-align: left;">Contra-Transfer</td>
									
									<td style="text-align: left;"></td>	
									<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${contra.amount}" />
								 <c:set var="row_debit" value="${row_debit + contra.amount}" />
						           </td>
						             <td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(contra.amount-0)+row_running}" />
								 <c:set var="row_running" value="${(contra.amount-0)+row_running}" />	
						            </td>
					    		</c:if>
					    		
								</c:if>	
								</c:if>	
							</c:forEach>
				   </c:if>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				 	
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				 
				 <td ></td>
				</tr>
				<tr>
					<td></td>
					<td>Closing Balance</td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
	   		
			</tbody>
		</table>
</div>
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
		<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("ledger_report")'>
				Download as Excel
			</button>
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
<%@include file="/WEB-INF/includes/footer.jsp"%> --%>