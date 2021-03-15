<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/paymentReport.js" var="tableexport" />
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

<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Payment Report</h3>
	<a href="homePage">Home</a> » <a href="paymentReport">Payment Report</a>
</div>
<div class="col-md-12">
	<div class="borderForm">
		<c:if test="${option == 2}">
		<!-- Excel Start -->
		<div style="display:none" id="excel_report">
							<!-- Date -->	
							<font size="11" face="verdana" >			
					<table>
						<tr style="text-align:center;"><td colspan='10'><b>Payment Report</b></td></tr>	
					<tr></tr>
						<tr style="text-align:center;"><td colspan='10'>Company Name: ${company.company_name}</td></tr>
						<tr style="text-align:center;"><td colspan='10'>Address: ${company.permenant_address}</td></tr>
						<tr style="text-align:center;"><td colspan='10'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy" />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy" />
						From ${from_date} To ${to_date}</td></tr>
						<tr style="text-align:center;"><td colspan='10'>
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
						<th data-field="paymentamount" data-filter-control="input"
							data-sortable="true">Payment Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Amount(Rs.)</th>							
					</tr>
				</thead>
				<tbody>
					<c:set var="row_total_payment" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:forEach var="entry" items="${paymentList}">
							<tr style="border:thin solid black !important ">
								<td style="text-align: left;">
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.supplier_bill_no !=null}"> 
								 <td style="text-align: left;">${entry.supplier_bill_no.supplier_bill_no}</td>
							     </c:if>
							      <c:if test="${entry.supplier_bill_no ==null}"> 
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
							     <td style="text-align: left;">${entry.cheque_dd_no}</td>
					      <td class="tright">
					          <c:set var="row_total_payment" value="${row_total_payment + entry.amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
						  </td>
						   <td class="tright">
					          <c:set var="row_total_tds" value="${row_total_tds + entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />	
						  </td>
					      <td class="tright">
					          <c:set var="row_tota_amount" value="${row_tota_amount + entry.amount+entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />	
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
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_payment}" /></Strong></td>
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tbody>
			</table>
			</font>
		</div>
		<!-- Excel End -->
		
		<!-- pdf for columner start for Hidden table -->
		
		<div class="table-scroll"  style="display:none;" id="tableDiv">
		
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
	<font size="11" face="verdana" >
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Payment Report</td>
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
					<tr >
					
					<td colspan='3'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					
					<tr style="border: solid black;">
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
							data-sortable="true" style="text-align:center;">Ch. No./ IB no.</th>
						<th data-field="paymentAmount" data-filter-control="input"
							data-sortable="true" style="text-align: right;" >Payment Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true" style="text-align: center;">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Amount(Rs.)</th>							
					</tr>
					</c:if>
					
					<tbody>
					<c:set var="row_total_payment" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:forEach var="entry" items="${paymentList}">
							<tr style="border: solid black;">
									<td style="text-align: left;">
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.supplier_bill_no !=null}"> 
								 <td style="text-align: left;">${entry.supplier_bill_no.supplier_bill_no}</td>
							     </c:if>
							      <c:if test="${entry.supplier_bill_no ==null}"> 
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
							     <td style="text-align: center;">${entry.cheque_dd_no}</td>
					     <td class='tright'>
					          <c:set var="row_total_payment" value="${row_total_payment + entry.amount}" />
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
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_payment}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td class='tright'><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tfoot>
			</table>
			</font>
			</div>
					
		<!-- pdf columner end -->
		
		<!-- pdf columner view  start-->
		<div class="table-scroll">
		
		
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">
		
		
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table">
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
						<th data-field="paymentAmount" data-filter-control="input"
							data-sortable="true" style="text-align:right">Payment Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Total Amount</th>							
					</tr>
					</c:if>
				</thead>
				<tbody>
					<c:set var="row_total_payment" value="0" />
					<c:set var="row_total_tds" value="0" />
					<c:set var="row_tota_amount" value="0" />
					<c:forEach var="entry" items="${paymentList}">
							<tr style="border:thin solid black !important ">
								<td style="text-align: left;">
									<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
									${createdDate} 
								</td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td style="text-align: left;">${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
								 <c:if test="${entry.supplier_bill_no !=null}"> 
								 <td style="text-align: left;">${entry.supplier_bill_no.supplier_bill_no}</td>
							     </c:if>
							      <c:if test="${entry.supplier_bill_no ==null}"> 
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
							     <td style="text-align: left;">${entry.cheque_dd_no}</td>
					      <td class="tright">
					          <c:set var="row_total_payment" value="${row_total_payment + entry.amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount}" />	
						  </td>
						   <td class="tright">
					          <c:set var="row_total_tds" value="${row_total_tds + entry.tds_amount}" />
		                     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />	
						  </td>
					      <td class="tright">
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
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_payment}" /></Strong></td>
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_total_tds}" /></Strong></td>
						<td class="tright"><Strong><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row_tota_amount}" /></Strong></td>
					</tr>
				</tfoot>
			</table>
			</div>
		</c:if>
		<c:if test="${option == 1}">
		<!-- Excel Start -->
		<div style="display:none" id="excel_report">
					<!-- Date -->
					<font size="11" face="verdana" >
					<table>
						<tr style="text-align:center;"><td colspan='5'><b>Payment Report</b></td></tr>	
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
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
					</font>
					<!-- Date -->
			<c:set var="Totalamount" value="0"/>	
	<c:forEach var="entry" items="${paymentList}">
	<c:set var="Totalamount" value="${Totalamount + entry.amount+entry.tds_amount}" />
	  </c:forEach>
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
						<th data-field="debit" data-filter-control="input"
							data-sortable="true">Amount(Rs.)</th>	
							
					</tr>
				</thead>
				<tbody>
				<c:forEach var="entry" items="${paymentList}">
					
						<tr style="border:thin solid black !important ">
							<td style="text-align: left;">
							<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   				${createdDate}								
							</td>
							<td style="text-align: left;">${entry.voucher_no}</td>
							<td style="text-align: left;">
								${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Payment</td>
							
							<td class="tright">
											
											<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.amount+entry.tds_amount}" />
						     </td>
								
						</tr>
				
					</c:forEach>
				<tr style="border:thin solid  !important ">
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
						
						<td class="tright"><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></b></td>
			 			</tr>
				</tbody>
			</table>
			</font>
		</div>
	<!-- Excel End -->
	
	<!-- condensed pdf start for hidden table -->
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
	<font size="11" face="verdana" >
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Payment Report</td>
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
					    <th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="debit" data-filter-control="input"
							data-sortable="true" style="text-align:right;">Amount(Rs.)</th>	
					</tr>
				</c:if>
					<tbody>
					<c:forEach var="entry" items="${paymentList}">
					
						<tr>
							<td style="text-align: left;">
							<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   				${createdDate}								
							</td>
							<td style="text-align: left;">${entry.voucher_no}</td>
							<td style="text-align: center;">
								${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Payment</td>
							
							<td class='tright'>
											
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
						
						<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></b></td>
					</tr>
				</tfoot>
			</table>
			</font>
			</div>
	
	<!-- condensed pdf end -->
	
	<!-- condensed pdf view start -->
	
		<div class="table-scroll">
		<c:set var="Totalamount" value="0"/>	
	<c:forEach var="entry" items="${paymentList}">
	<c:set var="Totalamount" value="${Totalamount + entry.amount+entry.tds_amount}" />
	  </c:forEach>
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
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="debit" data-filter-control="input"
							data-sortable="true">Credit</th>	
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entry" items="${paymentList}">
					
						<tr>
							<td style="text-align: left;">
							<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   				${createdDate}								
							</td>
							<td style="text-align: left;">${entry.voucher_no}</td>
							<td style="text-align: left;">
								${entry.supplier.company_name}${entry.subLedger.subledger_name}</td>
							<td style="text-align: left;">Payment</td>
							
							<td class='tright'>
											
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
						
						<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}" /></b></td>
					</tr>
				</tfoot>
			</table>
			</div>
		</c:if>
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
			</button>			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Payment-Report")'>
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
	function viewPayment(id){
		window.location.assign('<c:url value="viewPaymentReport"/>?id='+id);
	}
	
	/* function pdfPayment(){
		window.location.assign('<c:url value="pdfPaymentReport"/>');
	} */
	function back(){
		window.location.assign('<c:url value = "paymentReport"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>