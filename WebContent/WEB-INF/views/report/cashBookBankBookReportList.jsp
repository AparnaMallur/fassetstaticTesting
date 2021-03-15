<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/cashAndBankBook.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>
 
 
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
	<h3>Cash/Bank Book</h3>
	<a href="homePage">Home</a> » <a href="cashBookBankBookReport">Cash/Bank Book</a>
</div>
<div class="col-md-12">
	 <c:if test="${option==1}"> 
		<div style="display:none" id="excel_report">
						<!-- Date -->				
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Cash Book</b></td></tr>	
					<tr></tr>
						<tr><td colspan='6'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='6'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='6'>
								<fmt:parseDate value="${fromDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${toDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='6'>
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
	<!-- Excel End -->
	
	<!-- pdf for option==2 start -->
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			
		
		
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Cash Book</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						<td></td>
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
	
	
	<!-- pdf for option==2 end -->
	
	
	<!-- pdf for option==2 view start -->
		<div class="borderForm">
		
		
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

		<div class="row text-center-btn">
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Cash Book-Report")'>
						Download as Excel
			</button>
		</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>	                     	
     </c:if>
     <c:if test="${option==2}"> 
			 <div style="display:none" id="excel_report">
						<!-- Date -->				
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Bank Book</b></td></tr>	
					<tr></tr>
						<tr><td colspan='6'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='6'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='6'>
								<fmt:parseDate value="${fromDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${toDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='6'>
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
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
					<td><strong>${bankName}</strong></td>
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
					<td></td>
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				   
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
					<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						 <c:choose>
                    <c:when test="${dayBook.contratType==3}">
                    <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                    	<td>${dayBook.withdraw_from.bank_name}</td>
					    <td>${dayBook.deposite_to.bank_name}</td>
					 </c:if>
					     <c:if test="${dayBook.deposite_to.bank_id==bankId}">
					     <td>${dayBook.deposite_to.bank_name}</td>
					    <td>${dayBook.withdraw_from.bank_name}</td>
					     </c:if>
                    			
                    </c:when>
                    <c:otherwise> 
                           <td>${dayBook.particulars}</td>
					    <td>${dayBook.suppCustName}</td>     
				    </c:otherwise>
                    </c:choose>
						<td>${dayBook.voucher_Type}</td>
						
						<c:if test="${dayBook.type==4}">
						
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
							
						</c:if>
						
						<c:if test="${dayBook.type==2}">
							
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>

						</c:if>
						<c:if test="${dayBook.type==1}">
					<td style="text-align: left;"></td>
							
						<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
						</c:if>
						<c:if test="${dayBook.type==7}">
                       <c:if test="${dayBook.contratType==1}">
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
                       </c:if>
                        <c:if test="${dayBook.contratType==2}">
                        <td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
                       </c:if>

                        <c:if test="${dayBook.contratType==3}">
                        
                        <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                        	<td style="text-align: left;"></td>
                        <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.transferAmount}" /></td>
						
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.transferAmount))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.transferAmount))+row_running}" />	
						    </td>
                        </c:if>
                        <c:if test="${dayBook.deposite_to.bank_id==bankId}">
                      
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.transferAmount}" /></td>
									 <td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.transferAmount-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.transferAmount-0)+row_running}"/>	
						</td>
                        </c:if>
							
						
                       </c:if>
						
						
						</c:if>
					</tr>
				</c:forEach>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				
					
				
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
	<!-- Excel End -->
	
	<!-- pdf for option==2 start -->
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
	
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Bank Book</td>
					</tr>
			
					<tr>
						<td align="center">Company Name: </td>
						<td></td>
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
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
							
					<td><strong>${bankName}</strong></td>
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
					<td></td>
						  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
					<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						 <c:choose>
                    <c:when test="${dayBook.contratType==3}">
                    <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                    	<td>${dayBook.withdraw_from.bank_name}</td>
					    <td>${dayBook.deposite_to.bank_name}</td>
					 </c:if>
					     <c:if test="${dayBook.deposite_to.bank_id==bankId}">
					     <td>${dayBook.deposite_to.bank_name}</td>
					    <td>${dayBook.withdraw_from.bank_name}</td>
					     </c:if>
                    			
                    </c:when>
                    <c:otherwise> 
                           <td>${dayBook.particulars}</td>
					    <td>${dayBook.suppCustName}</td>     
				    </c:otherwise>
                    </c:choose>
						<td>${dayBook.voucher_Type}</td>
						
						<c:if test="${dayBook.type==4}">
						
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
							
						</c:if>
						
						<c:if test="${dayBook.type==2}">
							
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>

						</c:if>
						<c:if test="${dayBook.type==1}">

							<td style="text-align: left;"></td>
						<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
						</c:if>
						<c:if test="${dayBook.type==7}">
                       <c:if test="${dayBook.contratType==1}">
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
                       </c:if>
                        <c:if test="${dayBook.contratType==2}">
                        <td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
                       </c:if>

                        <c:if test="${dayBook.contratType==3}">
                        
                        <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                        <td style="text-align: left;"></td>
                        <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.transferAmount}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.transferAmount))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.transferAmount))+row_running}" />	
						    </td>
                        </c:if>
                        <c:if test="${dayBook.deposite_to.bank_id==bankId}">
                       
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.transferAmount}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.transferAmount-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.transferAmount-0)+row_running}"/>	
						</td>
                        </c:if>
							
						
                       </c:if>
						
						
						</c:if>
					</tr>
				</c:forEach>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 	<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				
					
				
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
	
	
	<!-- pdf for option==2 end -->
	
	
	<!-- pdf for option==2 view start -->
		<div class="borderForm">
		
		
				
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
				<c:forEach var="openingbalance" items="${bankOpenBalanceList}">
				 <c:if test="${openingbalance.bank_id == bankId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
				
					<td><strong>${bankName}</strong></td>
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
					<td></td>
					  <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit}" /></Strong></td>
				 
					 <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${credit}" /></Strong></td>
					   <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debit-credit}" /></Strong></td>
				</tr>
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="row_credit" value="0"/>		
				<c:set var="row_debit" value="0"/>
				
					<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						 <c:choose>
                    <c:when test="${dayBook.contratType==3}">
                    <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                    	<td>${dayBook.withdraw_from.bank_name}</td>
					    <td>${dayBook.deposite_to.bank_name}</td>
					 </c:if>
					     <c:if test="${dayBook.deposite_to.bank_id==bankId}">
					     <td>${dayBook.deposite_to.bank_name}</td>
					    <td>${dayBook.withdraw_from.bank_name}</td>
					     </c:if>
                    			
                    </c:when>
                    <c:otherwise> 
                           <td>${dayBook.particulars}</td>
					    <td>${dayBook.suppCustName}</td>     
				    </c:otherwise>
                    </c:choose>
						<td>${dayBook.voucher_Type}</td>
						
						<c:if test="${dayBook.type==4}">
						
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
							
						</c:if>
						
						<c:if test="${dayBook.type==2}">
							
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>

						</c:if>
						<c:if test="${dayBook.type==1}">

							<td style="text-align: left;"></td>
						<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
						</c:if>
						<c:if test="${dayBook.type==7}">
                       <c:if test="${dayBook.contratType==1}">
							
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
                       </c:if>
                        <c:if test="${dayBook.contratType==2}">
                        	<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.credit}" /></td>
						
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.credit))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.credit))+row_running}" />	
						</td>
                       </c:if>

                        <c:if test="${dayBook.contratType==3}">
                        
                        <c:if test="${dayBook.withdraw_from.bank_id==bankId}">
                        <td style="text-align: left;"></td>
                        <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_credit"
									value="${row_credit + dayBook.transferAmount}" /></td>
							
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(0-(dayBook.transferAmount))+row_running}" />
								 <c:set var="row_running" value="${(0-(dayBook.transferAmount))+row_running}" />	
						    </td>
                        </c:if>
                        <c:if test="${dayBook.deposite_to.bank_id==bankId}">
                       
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.transferAmount}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.transferAmount}" /></td>
									<td style="text-align: left;"></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.transferAmount-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.transferAmount-0)+row_running}"/>	
						</td>
                        </c:if>
							
						
                       </c:if>
						
						
						</c:if>
					</tr>
				</c:forEach>
				   
				   <tr>
					<td></td>
					<td></td>
					<td></td>
					<td ></td>
					<td ></td>
				 
					
					
					<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_debit}" /></b></td>
				<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_credit}"/></b></td>
				
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

		<div class="row text-center-btn">
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Bank Book-Report")'>
						Download as Excel
			</button>
		</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>                    	
     </c:if>	
			                    				
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function back(){
		window.location.assign('<c:url value = "cashBookBankBookReport"/>');	
	}
	/* function pdf(){
		window.location.assign('<c:url value = "pdfcashBookBankBookReport"/>');
	} */
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>