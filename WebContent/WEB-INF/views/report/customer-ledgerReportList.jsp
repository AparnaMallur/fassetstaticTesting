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
						<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
				    <th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>
					
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>		
			
			
			<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="dayBook" items="${dayBookList}">
		 
			<c:if test="${dayBook.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
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
					<td><strong>${customer.firm_name}</strong></td>
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
					 <c:if test="${dayBook.customer.customer_id == customer.customer_id}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
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
						<c:if test="${dayBook.type==5}">

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
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
					<td><strong>${customerName}</strong></td>
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
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
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
						<c:if test="${dayBook.type==5}">

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
					</tr>
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
					<td></td>
				    <td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" /></Strong></td>
				</tr>
			</tbody>
		</table>
	</div>
 </c:if>	 
 </c:if>
 
 
 
<!--  pdf start -->


 <c:if test="${option==1}">
	<c:if test="${customerId == -1}">
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">


		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
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
						data-sortable="true">Customer Name</th>
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
			
				<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="dayBook" items="${dayBookList}">
		 
			<c:if test="${dayBook.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
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
				
	
				<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/customer-ledgerReportHeader.jsp"%>
								</c:if>
							<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
				
					<td><strong>${customer.firm_name}</strong></td>
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
					 <c:if test="${dayBook.customer.customer_id == customer.customer_id}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
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
						<c:if test="${dayBook.type==5}">

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

	<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	
	
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
						data-sortable="true">Customer Name</th>
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
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				<tr>
				
								<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/customer-ledgerReportHeader.jsp"%>
								</c:if>
								
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
								
					<td><strong>${customerName}</strong></td>
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
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
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
						<c:if test="${dayBook.type==5}">

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
					</tr>
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
					<td></td>
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
						data-sortable="true">Customer Name</th>
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
			
				<c:forEach var="customer" items="${customerlist}">
			
			<c:set var="isCustomer" value="0"/>
		    <c:forEach var="dayBook" items="${dayBookList}">
		 
			<c:if test="${dayBook.customer.customer_id == customer.customer_id}">
			<c:set var="isCustomer" value="1"/>
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
				
				
				<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/customer-ledgerReportHeader.jsp"%>
								</c:if>
								
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
								
					<td><strong>${customer.firm_name}</strong></td>
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
					 <c:if test="${dayBook.customer.customer_id == customer.customer_id}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
						<td>${dayBook.voucher_Type}</td>
						
						<c:if test="${dayBook.type==4}">
						<td style="text-align: left;"></td>
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_debit"
									value="${row_debit + dayBook.debit}" /></td>
							<td class="tright">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${(dayBook.debit-0+row_running)}" />
								 <c:set var="row_running" value="${(dayBook.debit-0)+row_running}"/>	
						</td>
							
						</c:if>
						
						<c:if test="${dayBook.type==2}">
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
						<c:if test="${dayBook.type==5}">

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
						data-sortable="true">Customer Name</th>
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
				<c:forEach var="openingbalance" items="${customerOpenBalanceList}">
				 <c:if test="${openingbalance.customer_id == customerId}">
				 <c:set var="credit" value="${credit+openingbalance.credit_balance}" />
				  <c:set var="debit" value="${debit+openingbalance.debit_balance}" />
				 </c:if>	
				</c:forEach>
				
				<tr>
				
				<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/customer-ledgerReportHeader.jsp"%>
								</c:if>
								
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
								
					<td><strong>${customerName}</strong></td>
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
						<td>${dayBook.particulars}</td>
					    <td>${dayBook.customer.firm_name}</td>
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
						<c:if test="${dayBook.type==5}">

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
					</tr>
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
			
			
			<c:if test="${option==1}">
	<c:if test="${customerId == -1}">
	<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Ledger_Customer Report")'>
				Download as Excel
			</button>
	 </c:if>
	<c:if test="${customerId!= -1}">
    <button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Ledger_Customer_All Report")'>
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