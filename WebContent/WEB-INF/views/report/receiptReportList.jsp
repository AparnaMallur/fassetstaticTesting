<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/receiptReport.js" var="tableexport" />
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
	<h3>Receipt Report</h3>
	<a href="homePage">Home</a> » <a href="receiptReport">Receipt Report</a>
</div>	
<div class="col-md-12">	
	<div class="borderForm">
	<!-- For option columner -->
		<c:if test="${option == 2}">
		<!-- Excel Start -->
		
		<div style="display:none" id="excel_report">
				<!-- Date -->
				<font size="11" face="verdana" >
					<table >
						<!--<tr style="text-align:center;"><td></td><td></td><td><b>Receipt Report</b></td></tr>-->
						<tr style="text-align:center;"><td colspan='9'><b>Receipt Report</b></td></tr>
					<tr></tr>
						<tr style="text-align:center;"><td colspan='9'>Company Name: ${company.company_name}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>Address: ${company.permenant_address}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
					</font>
			<!-- Date -->
			<font size="11" face="verdana" >
		<table style="border:1pt solid  !important  border-collapse: collapse;">
				<thead>
					<tr style="border:thin solid  !important ">
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="Ref NO" data-filter-control="input"
							data-sortable="true">Ref NO.</th>
						<th data-field="bank" data-filter-control="input"
							data-sortable="true">Cash/bank</th>
						<th data-field="chno" data-filter-control="input"
							data-sortable="true">Ch. No./ IB no.</th>
						<th data-field="receiptamount" data-filter-control="input"
							data-sortable="true">Receipt Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Total Amount(Rs)</th>	
							
					</tr>
				
				</thead>
				<tbody>
					<c:set var="row_total_receipt" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:forEach var="entry" items="${receiptList}">
							<tr style="border:thin solid black !important ">
								
								<td style="text-align: left;" >
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.sales_bill_id !=null}"> 
								 <td style="text-align: left;">${entry.sales_bill_id.voucher_no}</td>
							     </c:if>
							      <c:if test="${entry.sales_bill_id ==null}"> 
								 <td style="text-align: left;">Advance</td>
							     </c:if>
							     
								 <c:if test="${entry.payment_type !=null && entry.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${entry.payment_type !=null && entry.payment_type!=1}"> 
							       <c:if test="${entry.bank !=null}"> 
								 <td style="text-align: left;">${entry.bank.bank_name}-${entry.bank.account_no}</td>
							      </c:if>
							       <c:if test="${entry.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${entry.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
							     <td style="text-align: left;">${entry.cheque_no}</td>
					      <td style="text-align: left;">
					          <c:set var="row_total_receipt" value="${row_total_receipt + entry.amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
						  </td>
						   <td style="text-align: left;">
					          <c:set var="row_total_tds" value="${row_total_tds + entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />	
						  </td>
					      <td style="text-align: left;">
					          <c:set var="row_tota_amount" value="${row_tota_amount + entry.amount+entry.tds_amount}" />
					           <c:if test="${entry.amount+entry.tds_amount <1000}">
					         <fmt:formatNumber type="number" maxFractionDigits = "3" value="${entry.amount+entry.tds_amount}" />	
					          </c:if>
					          <c:if test="${entry.amount+entry.tds_amount >1000}">
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
						 </c:if>
						  </td>
							</tr>
					</c:forEach>
				
					<tr style="border:thin solid black !important ">
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_receipt}" /></Strong></td>
						<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->
		<!-- Code for Pdf  -->
		<!-- Code of Hidden Table for PDf generation for columner -->
		<div class="table-scroll" style="display:none;" id="tableDiv" >
					<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		<font size="11" face="verdana" >
			<table  class="noBorder">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td>Receipt Report</td>
						</tr>
						<tr>
							<td >Company Name: </td>
							<td></td>
							<td >${company.company_name}</td>
						</tr>
						<tr>
							<td>Address: </td>
							<td></td>
							<td>${company.permenant_address}</td>
						</tr>
						<tr>
							<td>From </td>
							<td></td>
							<td> ${from_date} To ${to_date}</td>
						</tr>
						<tr>
						<td >CIN88:</td>
						<td></td>
							<td >
							<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
							 ${company.registration_no}
						    </c:if>	
							</td>
						</tr>
					
			
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="Ref NO" data-filter-control="input"
							data-sortable="true">Ref NO.</th>
						<th data-field="bank" data-filter-control="input"
							data-sortable="true">Cash/bank</th>
						<th data-field="chno" data-filter-control="input"
							data-sortable="true">Ch. No./ IB no.</th>
						<th data-field="receiptamount" data-filter-control="input"
							data-sortable="true">Receipt Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true" style="text-align: right;">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Total Amount(Rs)</th>							
					</tr>
					</c:if>
				
				<tbody>
					<c:set var="row_total_receipt" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:forEach var="entry" items="${receiptList}">
							<tr>
								<td style="text-align: left;">
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.sales_bill_id !=null}"> 
								 <td style="text-align: left;">${entry.sales_bill_id.voucher_no}</td>
							     </c:if>
							      <c:if test="${entry.sales_bill_id ==null}"> 
								 <td style="text-align: left;">Advance</td>
							     </c:if>
							     
								 <c:if test="${entry.payment_type !=null && entry.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${entry.payment_type !=null && entry.payment_type!=1}"> 
							       <c:if test="${entry.bank !=null}"> 
								 <td style="text-align: left;">${entry.bank.bank_name}-${entry.bank.account_no}</td>
							      </c:if>
							       <c:if test="${entry.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${entry.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
							     <td style="text-align: left;">${entry.cheque_no}</td>
					      <td class='tright'>
					          <c:set var="row_total_receipt" value="${row_total_receipt + entry.amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
						  </td>
						   <td class='tright'>
					          <c:set var="row_total_tds" value="${row_total_tds + entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />	
						  </td>
					      <td class='tright'>
					          <c:set var="row_tota_amount" value="${row_tota_amount + entry.amount+entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
						  </td>
							</tr>
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_receipt}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tfoot>
			</table>
			</font>
			</div>
			
			<!-- code for view page for PDF generation -->
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
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="Ref NO" data-filter-control="input"
							data-sortable="true">Ref Data</th>
						<th data-field="bank" data-filter-control="input"
							data-sortable="true">Cash/bank</th>
						<th data-field="chno" data-filter-control="input"
							data-sortable="true">Ch. No./ IB no.</th>
						<th data-field="receiptamount" data-filter-control="input"
							data-sortable="true">Receipt Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Debit</th>							
					</tr>
				</thead>
				<tbody>
					<c:set var="row_total_receipt" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:set var="refdata" value="0" />
					<c:forEach var="entry" items="${receiptList}">
							<tr>
								<td style="text-align: left;">
								<c:set var="refdata" value="9" />
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td class='tright'>${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.sales_bill_id !=null}"> 
								 <c:set var="refdata" value="1" />
								 <td style="text-align: left;">${entry.sales_bill_id.voucher_no}</td>
							     </c:if>
							      <c:if test="${entry.advreceipt !=null } ||  ${entry.againstOpeningBalnce ==null}"> 
								<c:set var="refdata" value="1" />
								 <td style="text-align: left;">Advance</td>
							     </c:if>
							     
							      <c:if test="${entry.againstOpeningBalnce !=null} || ${entry.advreceipt ==null}"> 
								<c:set var="refdata" value="1" />
								 <td style="text-align: left;">Opening Balance</td>
							     </c:if>
							     
							     <c:if test="${refdata==9 }">
							      <td style="text-align: left;"></td>
							     </c:if>
								 <c:if test="${entry.payment_type !=null && entry.payment_type==1}"> 
								 <td style="text-align: left;">Cash</td>
							     </c:if>
							      <c:if test="${entry.payment_type !=null && entry.payment_type!=1}"> 
							       <c:if test="${entry.bank !=null}"> 
								 <td style="text-align: left;">${entry.bank.bank_name}-${entry.bank.account_no}</td>
							      </c:if>
							       <c:if test="${entry.bank ==null}"> 
								 <td style="text-align: left;"></td>
							      </c:if>
							      </c:if>
							      <c:if test="${entry.payment_type ==null}"> 
								 <td style="text-align: left;"></td>
							     </c:if>
							     <td style="text-align: left;">${entry.cheque_no}</td>
					      <td class='tright'>
					          <c:set var="row_total_receipt" value="${row_total_receipt + entry.amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
						  </td>
						   <td class='tright'>
					          <c:set var="row_total_tds" value="${row_total_tds + entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />	
						  </td>
					      <td class='tright'>
					          <c:set var="row_tota_amount" value="${row_tota_amount + entry.amount+entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
						  </td>
							</tr>
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_receipt}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tfoot>
			</table>
			</div>
		</c:if>
		
		<!-- option for Condensed -->
		<c:if test="${option == 1}">
		
		<c:set var="Totalamount" value="0"/>	
	<c:forEach var="entry" items="${receiptList}">
	<c:choose>
								<c:when test="${entry.tds_paid==true}">
								<c:set var="Totalamount" value="${Totalamount + entry.amount+entry.tds_amount}" />
									</c:when>
								<c:otherwise>
								<c:set var="Totalamount" value="${Totalamount + entry.amount}" />
								   </c:otherwise>
					   </c:choose>
	
     </c:forEach>
     
     <!-- Excel Start -->
     <!--Code for Excel Report  -->
		<div style="display:none" id="excel_report">
		<!-- Date -->
		<font size="11" face="verdana" >
					<table>
						<!-- <tr style="text-align:center;"><td></td><td></td><td><b>Receipt Report</b></td></tr> -->
						<tr style="text-align:center;"><td colspan='5'><b>Receipt Report</b></td></tr>	
					<tr></tr>
						<tr style="text-align:center;"><td colspan='5'>Company Name: ${company.company_name}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>Address: ${company.permenant_address}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>
						CIN :
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
					</font>
					<!-- Date -->
					<font size="11" face="verdana" >
		<table style="border:1pt solid  !important  border-collapse: collapse;">
				<thead>
					<tr style="border:thin solid  !important ">
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
					   <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="credit" data-filter-control="input"
							data-sortable="true">Amount(Rs.)</th>
							
					</tr>
				</thead>
				<tbody>
						<c:forEach var="entry" items="${receiptList}">
				
							<tr style="border:thin solid  !important ">
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate} 
								</td>
								<td style="text-align: left;">
							${entry.voucher_no}</td>
							<td style="text-align: left;">
								${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Receipt</td>
							
								<td class='tright'>
								<c:choose>
								<c:when test="${entry.tds_paid==true}">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
									</c:when>
								<c:otherwise>
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
								   </c:otherwise>
					   </c:choose>
						
							</td>
											
						</tr>
					
					</c:forEach>
				
					<tr style="border:thin solid  !important ">
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
			 		<td><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></Strong></td>
			 	    
						
					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->
		<!-- Code for PDf of option condensed  for hidden table-->
		<div class="table-scroll" style="display:none;" id="tableDiv">
		<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">

			<table id="Hiddentable">
			<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Receipt Report</td>
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
					CIN77:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
				
				
					<tr >
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
					   <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="credit" data-filter-control="input"
							data-sortable="true" style="text-align: right">Amount(Rs.)</th>
							
					</tr>
					</c:if>
				
				<tbody>
					
					<c:forEach var="entry" items="${receiptList}">
				
							<tr>
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate} 
								</td>
								<td style="text-align: left;">
							${entry.voucher_no}</td>
							<td style="text-align: left;">
								${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Receipt</td>
							
								<td class='tright'>
								<c:choose>
								<c:when test="${entry.tds_paid==true}">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
									</c:when>
								<c:otherwise>
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
								   </c:otherwise>
					   </c:choose>
						
							</td>
											
						</tr>
					
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
			 	<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></Strong></td>
			 	
			 	    
						
					</tr>
				</tfoot>
			</table>
			</div>
			<!--code for view page of condensed option  -->
			<div class="table-scroll">
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table table-scroll">
				<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
					   <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="credit" data-filter-control="input"
							data-sortable="true">Debit</th>
							
					</tr>
				</thead>
				<tbody>
					
					<c:forEach var="entry" items="${receiptList}">
				
						<tr>
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate} 
								</td>
								<td style="text-align: left;">
							${entry.voucher_no}</td>
							<td style="text-align: left;">
								${entry.customer.firm_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Receipt</td>
							
								<td class='tright'>
								<c:choose>
								<c:when test="${entry.tds_paid==true}">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
									</c:when>
								<c:otherwise>
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
								   </c:otherwise>
					   </c:choose>
						
							</td>
											
						</tr>
					
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
			 	<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></Strong></td>
			 	
			 	    
						
					</tr>
				</tfoot>
			</table>
			</div>
		</c:if>
		<div class="row text-center-btn">
		
			<c:if test="${role!=7}">
				<button class="fassetBtn" type="button" onclick ="pdf('#Hiddentable', {type: 'pdf',
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Receipt-Report")'>
				Download as Excel
			</button>
		    </c:if>
				<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	function pdfReceipt(id){
		window.location.assign('<c:url value="pdfReceiptReport"/>');
	}
	function back(){
		window.location.assign('<c:url value = "receiptReport"/>');	
	}
	$(document).on('keydown', function(e) {
		var role='<c:out value= "${role}"/>';
		
		if(role==7)
		{			
		    if(e.ctrlKey && (e.key == "p" || e.charCode == 16 || e.charCode == 112 || e.keyCode == 80) ){
		        alert("You Cann't Print the Report");
		        e.cancelBubble = true;
		        e.preventDefault();
	
		        e.stopImmediatePropagation();
		    }  
		}
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>