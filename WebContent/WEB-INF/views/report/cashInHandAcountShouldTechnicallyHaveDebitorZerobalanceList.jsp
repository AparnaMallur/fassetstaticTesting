<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport7.js" var="tableexport" />
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
	<h3>NEGATIVE CASH REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport7">NEGATIVE CASH REPORT</a>
</div>
<div class="col-md-12">	

<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>NEGATIVE CASH REPORT</b></td></tr>					
						<tr><td colspan='4'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='4'>Address: ${company.permenant_address}</td></tr>
						<tr><td colspan='4'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr><td colspan='4'>
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
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>		
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Amount</th>
					<th data-field="balance" data-filter-control="input"
						data-sortable="true">Balance</th>
					
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
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="amount" value="0"/>
				
				<c:forEach var="balance" items="${subledgerOPBalanceList}">
				
				 <c:if test="${balance.sales!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				 <td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
				</td>
				<td style="text-align: left;">${balance.sales.voucher_no}</td>	
				<td style="text-align: left;">Sales</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				  <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				    </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.receipt!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">Receipt</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.credit!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">Credit Note</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				 	<c:if test="${balance.purchase!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">Purchase</td>
						
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.payment!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">Payment</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.debit!=null}">
				  
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			     <td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">Debit</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.contra!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			   <td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">Contra</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				</c:forEach>
				
			  <%--  <tr>
					<td></td>
					<td></td>
					<td ></td>
					<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${amount}"/></Strong></td>
				    <td></td>
				</tr> --%>
								</tbody>
						</table>		
				</div>


<div class="table-scroll"  style="display:none;" id="tableDiv">


<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">

	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">NEGATIVE CASH REPORT</td>
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
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>		
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Amount</th>
					<th data-field="balance" data-filter-control="input"
						data-sortable="true">Balance</th>
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
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="amount" value="0"/>
				
				<c:forEach var="balance" items="${subledgerOPBalanceList}">
				
				 <c:if test="${balance.sales!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				  
				  
				  	<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceHeader.jsp"%>
								</c:if>
						
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />

				  
				 <td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
				</td>
				<td style="text-align: left;">${balance.sales.voucher_no}</td>	
				<td style="text-align: left;">Sales</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				  <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				    </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.receipt!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">Receipt</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.credit!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">Credit Note</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				 	<c:if test="${balance.purchase!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">Purchase</td>
						
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.payment!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">Payment</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.debit!=null}">
				  
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			     <td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">Debit</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.contra!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			   <td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">Contra</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				</c:forEach>

			</tbody>
			
			<tfoot style='background-color: #eee'>
				<%-- 	 <tr>
					<td></td>
					<td></td>
					<td ></td>
					<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${amount}"/></Strong></td>
				    <td></td>
			        </tr> --%>
				</tfoot>
		</table>	
	</div>	
	
	<div class = "borderForm" >
	
	<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		
		<table id="table" 
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
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="voucherNumber" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="voucherType" data-filter-control="input"
						data-sortable="true">Voucher Type</th>		
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Amount</th>
					<th data-field="balance" data-filter-control="input"
						data-sortable="true">Balance</th>
					
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
				<c:set var="row_running" value="${debit-credit}"/>
				<c:set var="amount" value="0"/>
				
				<c:forEach var="balance" items="${subledgerOPBalanceList}">
				
				 <c:if test="${balance.sales!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				  
				  
				  		<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/cashInHandAcountShouldTechnicallyHaveDebitorZerobalanceHeader.jsp"%>
								</c:if>
						
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
				  
				  
				 <td style="text-align: left;">
						<fmt:parseDate value="${balance.sales.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
				</td>
				<td style="text-align: left;">${balance.sales.voucher_no}</td>	
				<td style="text-align: left;">Sales</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				  <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				    </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.receipt!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.receipt.voucher_no}</td>
						<td style="text-align: left;">Receipt</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.credit!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
				<td style="text-align: left;">
						<fmt:parseDate value="${balance.credit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.credit.voucher_no}</td>
						<td style="text-align: left;">Credit Note</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				 	<c:if test="${balance.purchase!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.purchase.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.purchase.voucher_no}</td>
						<td style="text-align: left;">Purchase</td>
						
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				 <c:if test="${balance.payment!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			<td style="text-align: left;">
						<fmt:parseDate value="${balance.payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.payment.voucher_no}</td>
						<td style="text-align: left;">Payment</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.debit!=null}">
				  
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			     <td style="text-align: left;">
						<fmt:parseDate value="${balance.debit.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.debit.voucher_no}</td>
						<td style="text-align: left;">Debit</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 
				  <c:if test="${balance.contra!=null}">
				 <c:set var="row_running" value="${(balance.debit_balance-balance.credit_balance)+row_running}"/>
				 <c:if test="${row_running<0}">
				  <tr>
			   <td style="text-align: left;">
						<fmt:parseDate value="${balance.contra.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
						</td>
						<td style="text-align: left;">${balance.contra.voucher_no}</td>
						<td style="text-align: left;">Contra</td>
				 <c:set var="amount" value="${amount + balance.credit_balance}" />
				  <c:set var="amount" value="${amount + balance.debit_balance}" />
				  <c:if test="${balance.credit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.credit_balance}"/></td>
				 </c:if>
				 	<c:if test="${balance.debit_balance!=0}">
						<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${balance.debit_balance}"/></td>
				  </c:if>
				   <td class="tright">
				    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_running}" />
				   </td>
				   </tr>
				  </c:if>
				 </c:if>
				 	
				</c:forEach>
				
			 <%--   <tr>
					<td></td>
					<td></td>
					<td ></td>
					<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${amount}"/></Strong></td>
				    <td></td>
				</tr> --%>
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("NEGATIVE CASH REPORT")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>

</div>

<%@include file="/WEB-INF/includes/footer.jsp" %>
