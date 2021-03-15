<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/paymentAccountingVoucher.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>
<style>
.breadcrumb h3 {
    margin: 0px;
    display: inline-block;
    text-align: center;
    line-height: 15px;
}
.breadcrumb a{float:left;}
</style>
<div class="breadcrumb text-center">
	<a href=""><i class="fa fa-arrow-left" aria-hidden="true"></i> Back</a><h3>Payment</h3>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	
<div class="fassetForm">
	<form:form id="paymentForm"  commandName = "payment">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active"><a href="#home" data-toggle="tab"
				aria-expanded="true"> <span class="visible-xs"><i
						class="fa fa-home"></i></span> <span class="hidden-xs">Details</span>
			</a></li>
			<!-- <li class=""><a href="#profile" data-toggle="tab"
				aria-expanded="false"> <span class="visible-xs"><i
						class="fa fa-user"></i></span> <span class="hidden-xs">Accounting
						Voucher</span>
			</a></li> -->
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="home">
				<div class="table-responsive">				
					<table class = "table">
						<tr><td colspan='6' style='text-align:center'><strong>Financial Year:${payment.accountingYear.year_range}</strong></td></tr>
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${payment.voucher_no}</td>	
							<td><strong>Supplier Name:</strong></td>
							<td style="text-align: left;">
								<c:choose>
				                    <c:when test='${payment.supplier==null}'>Expense</c:when>
				                    <c:otherwise>${payment.supplier.company_name} </c:otherwise>
			                    </c:choose>
		                    </td>	
		                </tr>
						<tr>	
							<c:choose>
			                    <c:when test='${payment.supplier==null}'>
				                    <td><strong>Expense Type:</strong></td>
									<td style="text-align: left;">${payment.subLedger.subledger_name} </td>
			                   	</c:when>
			                    <c:otherwise> 
			                    <td><strong>Bill No:</strong></td>
								<td style="text-align: left;">
									<c:choose>
										<c:when test="${payment.entry_status==3}">NA</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${payment.supplier_bill_no!=null}">
														${payment.supplier_bill_no.supplier_bill_no}
														<c:set var="sup_bill_no" value="${payment.supplier_bill_no.supplier_bill_no}"></c:set>
													</c:when>
													<c:otherwise>Advance<c:set var="sup_bill_no" value="Advance"></c:set></c:otherwise>
												</c:choose>
							                </c:otherwise>
						             </c:choose>
						         </td>	
								</c:otherwise>
							</c:choose>	
							<td><strong>Date:</strong></td>					
							<td style="text-align: left;"> 
								<fmt:parseDate value="${payment.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
		                   		<fmt:formatDate value="${parsedDate}" var="bill_date" type="date" pattern="dd-MM-yyyy"/>
							   ${bill_date}
							</td>
						</tr>
						<tr>
							<td><strong>Payment Type:</strong></td>
							<td style="text-align: left;">
								<c:choose>
									<c:when test="${payment.payment_type==1}">Cash</c:when>
									<c:when test="${payment.payment_type==2}">Cheque</c:when>
									<c:when test="${payment.payment_type==3}">DD</c:when>
									<c:otherwise>NEFT/RTGS/Net banking/Account Transfer</c:otherwise>
								</c:choose>	
							</td>
							<td><strong>Bank Name:</strong></td>
							<td>
								<c:choose>
									<c:when test="${payment.payment_type==1}">NA
									</c:when>
									<c:otherwise>
									${payment.bank.bank_name} - ${payment.bank.account_no}								
									</c:otherwise>
								</c:choose>
							</td>							
						</tr>
						<tr>  			
							<td><strong>Cheque Date:</strong></td>
							<td style="text-align: left;">${payment.cheque_date}</td>
		                   	<td><strong>Cheque No:</strong></td>
							<td style="text-align: left;">${payment.cheque_dd_no}</td>	
						</tr>
						<tr>
							<td><strong>Advance Payment:</strong></td>
							<td style="text-align: left;">${payment.advance_payment==true ? "Yes" : "No"}</td>	
							<td><strong>GST Applicable:</strong></td>
							<td style="text-align: left;">${payment.gst_applied==true ? "Yes" : "No"}</td>		
						</tr>
						<tr>	
							<td><strong>Amount:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${payment.amount}" /></td>
							<td><strong>Other Remark:</strong></td>
							<td style="text-align: left;">${payment.other_remark}</td>	
						</tr>
						<tr>				
							<td><strong>Transaction Amount:</strong></td>
						   	<td style="text-align: left;" ><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.transaction_value_head}" /></td>							
							<td><strong>CGST:</strong></td>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.CGST_head}" />	</td>
						</tr>
						<tr>
							<td><strong>SGST:</strong></td>
							<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.SGST_head}" /></td>
							<td><strong>IGST:</strong></td>
							<td style="text-align: left;">${payment.IGST_head}</td>
						</tr>
						<tr>								
							<td><strong>CESS:</strong></td>
							<td style="text-align: left;">${payment.SCT_head}</td>
						    <td><strong>TDS Paid:</strong></td>
							<td style="text-align: left;">${payment.tds_paid==true ? "Yes" : "No"}</td>
						</tr>
						<tr>
							<c:if test="${payment.tds_paid==true}">
								<td><strong>TDS Amount:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${payment.tds_amount}" /></td>
					     	</c:if>
					        <td><strong>Status:</strong></td>
							<td style="text-align: left;">${payment.entry_status==3 ? "Disable" : "Enable"}</td>
							<td><strong>Closing Balance:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber type="number" value="${closingbalance}" /></td>
							<c:if test="${payment.excel_voucher_no != null}">
								<td><strong>Excel Voucher No</strong></td>
							</c:if>
							<c:if test="${payment.excel_voucher_no != null}">
								<td>${payment.excel_voucher_no}</td>
							</c:if>
						</tr>
						 <tr>			
							<td><strong>Created By</strong></td>							    
							<td style="text-align: left;">${created_by.first_name} ${created_by.last_name}</td>
							<td><strong>Updated By</strong></td>
							<td style="text-align: left;">${updated_by.first_name} ${updated_by.last_name}</td>
						</tr>
					</table>
				</div>
				<c:if test="${(payment.gst_applied == true)}">
					<%-- <h3>Product Details</h3>			
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Product </th>
								<th>Quantity</th>
								<th>UOM</th>
								<th>HSN/SAC Code</th>
								<th>Rate</th>
								<th>Discount</th>
								<th>Labour Charge</th>
								<th>Freight Charges</th>
								<th>Others</th>
								<th>CGST</th>
								<th>SGST</th>
								<th>IGST</th>
								<th>CESS</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var = "prod_list" items = "${paymentDetailsList}">	
								<tr>
									<td>${prod_list.product_id.product_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.quantity}" /></td>
									<td>${prod_list.uom_id.unit}</td>
									<c:if test="${payment.excel_voucher_no==null}">
										<td>${prod_list.product_id.hsn_san_no}</td>
									</c:if>
									<c:if test="${payment.excel_voucher_no!=null}">
										<td>${prod_list.HSNCode}</td>
									</c:if>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.rate}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.discount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.frieght}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.others}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.cgst}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.sgst}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.igst}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.state_com_tax}" /></td>
								</tr>
							</c:forEach>	
						</tbody>
					</table> --%>
					
					
					
					<div class="panel-group">
					    <div class="panel panel-default">
					      <div class="panel-heading">
					        <h3 class="panel-title">
					          <a data-toggle="collapse" href="#collapse1">Product Details</a>
					        </h3>
					      </div>
					      <div id="collapse1" class="panel-collapse collapse">
					        <div class="panel-body">
					        <c:forEach var = "prod_list" items = "${paymentDetailsList}">	
					        	<table class="table table-bordered table-striped" style="border:2px solid #000;">
									<tbody>
											<tr>
												<td>Product </td>
												<td>${prod_list.product_id.product_name}</td>
											</tr>
											<tr>
												<td>Quantity</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.quantity}" /></td>
											</tr>
											<tr>
												<td>UOM</td>
												<td>${prod_list.uom_id.unit}</td>
											</tr>
											<tr>
												<td>HSN/SAC Code</td>
												<c:if test="${payment.excel_voucher_no==null}">
													<td>${prod_list.product_id.hsn_san_no}</td>
												</c:if>
												<c:if test="${payment.excel_voucher_no!=null}">
													<td>${prod_list.HSNCode}</td>
												</c:if>
											</tr>
											<tr>
												<td>Rate</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.rate}" /></td>
											</tr>
											<tr>
												<td>Discount</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.discount}" /></td>
											</tr>
											<tr>
												<td>Labour Charge</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
											</tr>
											<tr>
												<td>Freight Charges</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.frieght}" /></td>
											</tr>
											<tr>
												<td>Otders</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.others}" /></td>
											</tr>
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.cgst}" /></td>
											</tr>
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.sgst}" /></td>
											</tr>
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.igst}" /></td>
											</tr>
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.state_com_tax}" /></td>
											</tr>
									</tbody>
								</table>
								</c:forEach>
					        </div>
					      </div>
					    </div>
					</div>
					
					
				</c:if>	
				<%-- <div class="row"  style = "text-align: center; margin:15px;">
						<c:if test="${payment.entry_status!=3 || payment.entry_status!=4}">	
						<button class="fassetBtn" type="button" onclick = "DownloadPdf(${payment.payment_id})">
								Download As Pdf
							</button>
						</c:if>
			    </div> --%>
			</div>
			
			<!--UI view starts for Account Voucher  -->
			
			<%-- <div class="tab-pane" id="profile">
				<div class="col-md-12">
					<p class="col-md-6 col-xs-6 text-left" style="padding-left: 0px">
						<b>Payment No: ${payment.voucher_no}</b>
					</p>
					<p class="col-md-6 col-xs-6 text-right" style="padding-left: 0px">
						<b>Date: ${bill_date}
						</b>
					</p>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 70%">Particulars</th>
							<th>Debit</th>
							<th>Credit</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${payment.gst_applied==true}">
							<c:choose>
								<c:when test='${payment.supplier!=null}'>
									<c:set var="cust_name" value="${payment.supplier.company_name}"></c:set>
										<c:set var="nrtn" value="amount paid to"></c:set>
									
									<tr>
										<td>${payment.supplier.company_name} <br /> Cur Bal:
											<fmt:formatNumber type="number" value="${closingbalance}" />
										
										</td>
										<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
								</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
										<td></td>
									</tr>
									<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td>${payment.tds_amount}</td>												
											</tr>									
									</c:if>
								</c:when>
							<c:otherwise>
								<c:set var="cust_name"
									value="${payment.subLedger.subledger_name}"></c:set>
									<c:set var="nrtn" value="payment made for"></c:set>
								<tr>
									<td> ${payment.subLedger.subledger_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" />
										
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td>${payment.tds_amount}</td>
												
											</tr>									
									</c:if>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${payment.payment_type==1}">
								<tr>
									<td> Cash In Hand</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.transaction_value_head}" /></td>
									<td>${payment.transaction_value_head-payment.tds_amount}</td>
								</tr>
								<c:if test="${payment.CGST_head!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.CGST_head}" />
									</td>												
											
										</tr>				
								</c:if>
								<c:if test="${payment.SGST_head!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SGST_head}" />
									</td>												
										</tr>				
								</c:if>
								<c:if test="${payment.IGST_head!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.IGST_head}" />
									</td>																						
										</tr>				
								</c:if>
								<c:if test="${payment.SCT_head!=0}">
										<tr>
											<td>CESS										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SCT_head}" />
									</td>																														
										</tr>	
								</c:if>
								<c:if test="${payment.total_vat!=''}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vat}" />
									</td>												
																	
										</tr>	
								</c:if>
								<c:if test="${payment.total_vatcst!=''}">
										<tr>
											<td>CST</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vatcst}" />
									</td>	
											
										</tr>	
								</c:if>
								<c:if test="${payment.total_excise!=''}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_excise}" />
									</td>	
																			
										</tr>	
								</c:if>
								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through cash.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr> 	
								
							</c:when>
							<c:otherwise>
								<tr>
									<td> ${payment.bank.bank_name} -
										${payment.bank.account_no} <br /> Cur Bal:
										${(payment.supplier.openingbalances.debit_balance)-(payment.supplier.openingbalances.credit_balance)}
										
									</td>
									<td></td>
									<td>${payment.transaction_value_head}</td>
								</tr>
								
									<c:if test="${payment.CGST_head!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>												
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.CGST_head}" />
									</td>		
										</tr>				
								</c:if>
								<c:if test="${payment.SGST_head!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SGST_head}" />
									</td>		
										</tr>				
								</c:if>
								<c:if test="${payment.IGST_head!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.IGST_head}" />
									</td>									
										</tr>				
								</c:if>
								<c:if test="${payment.SCT_head!=0}">
										<tr>
											<td>CESS										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SCT_head}" />
									</td>		
																													
										</tr>	
								</c:if>
								<c:if test="${payment.total_vat!=''}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vat}" />
									</td>		
																					
										</tr>	
								</c:if>
								<c:if test="${payment.total_vatcst!=''}">
										<tr>
											<td>CST</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vatcst}" />
									</td>		
											
										</tr>	
								</c:if>
								<c:if test="${payment.total_excise!=''}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_excise}" />
									</td>		
																					
										</tr>	
								</c:if>

								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through ${payment.bank.bank_name} - ${payment.bank.account_no} by ${payment.cheque_dd_no}.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
							</c:otherwise>
						</c:choose>
							
							
							
							</c:when>
							<c:otherwise>
								<c:choose>
							<c:when test='${payment.supplier!=null}'>
								<c:set var="cust_name" value="${payment.supplier.company_name}"></c:set>
								<c:set var="nrtn" value="amount paid to"></c:set>
								<tr>
									<td>${payment.supplier.company_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" /> Dr
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.tds_amount}" />
									</td>		
												
												
											</tr>									
							   </c:if>
							</c:when>
							<c:otherwise>
								<c:set var="cust_name"
									value="${payment.subLedger.subledger_name}"></c:set>
								<c:set var="nrtn" value="payment made for"></c:set>
								<tr>
									<td>${payment.subLedger.subledger_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" />
										
										
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.tds_amount}" />
									</td>		
											</tr>									
									</c:if>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${payment.payment_type==1}">
								<tr>
									<td> Cash In Hand</td>
									<td></td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
								</tr>

								<tr style='border: 1px solid #bbb'>
								 <c:if test="${payment.supplier != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through cash.
									</td>
								</c:if>
								 <c:if test="${payment.subLedger != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} through cash.
									</td>
								</c:if>
								<td></td>
								<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td> ${payment.bank.bank_name} -
										${payment.bank.account_no} <br /> Cur Bal:
											<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${(payment.supplier.openingbalances.debit_balance)-(payment.supplier.openingbalances.credit_balance)}" />
										
									</td>
									<td></td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
								</tr>

                                <c:if test="${payment.supplier != null}">
								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through ${payment.bank.bank_name}${payment.bank.account_no} by ${payment.cheque_dd_no}.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
								</c:if>
								
								  <c:if test="${payment.subLedger != null}">
								<tr style='border: 1px solid #bbb'>
								 <c:if test="${payment.bank != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} through ${payment.bank.bank_name}${payment.bank.account_no}.
									</td>
								</c:if>
								 <c:if test="${payment.bank == null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name}.
									</td>
								</c:if>
								<td></td>
								<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
								</tr>
								</c:if>
							</c:otherwise>
						</c:choose>
								
						</c:otherwise>
						</c:choose>
						
					</tbody>
				</table>
				<div class="row"  style = "text-align: center; margin:15px;">
			<c:if test="${payment.entry_status!=3 || payment.entry_status!=4}">	
			<button class="fassetBtn" type="button"  onclick ="paymentAccountingVoucherPdf('#Hiddentable', {type: 'pdf',
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
			</c:if>
			</div>
			</div> --%>
			
			<!--UI view ends for Account Voucher  -->
			
			<!--PDF view starts for the Account Voucher  -->
			
			<%-- <div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Payment</td>
						
					</tr>
			
					<tr>
							<td>Date: ${bill_date}</td>
					</tr>
			
					<tr>
						<td>Payment No: ${payment.voucher_no}</td>
						
					</tr>


					<tr id="row">

						
							<th style="width: 70%">Particulars</th>
							<th>Debit</th>
							<th>Credit</th>
						
					</tr>
					<tbody>
				    
				<c:choose>
							<c:when test="${payment.gst_applied==true}">
							<c:choose>
								<c:when test='${payment.supplier!=null}'>
									<c:set var="cust_name" value="${payment.supplier.company_name}"></c:set>
										<c:set var="nrtn" value="amount paid to"></c:set>
									
									<tr>
										<td>${payment.supplier.company_name} <br /> Cur Bal:
											<fmt:formatNumber type="number" value="${closingbalance}" />
										
										</td>
										<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
								</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
										<td></td>
									</tr>
									<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td>${payment.tds_amount}</td>												
											</tr>									
									</c:if>
								</c:when>
							<c:otherwise>
								<c:set var="cust_name"
									value="${payment.subLedger.subledger_name}"></c:set>
									<c:set var="nrtn" value="payment made for"></c:set>
								<tr>
									<td> ${payment.subLedger.subledger_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" />
										
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td>${payment.tds_amount}</td>
												
											</tr>									
									</c:if>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${payment.payment_type==1}">
								<tr>
									<td> Cash In Hand</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.transaction_value_head}" /></td>
									<td>${payment.transaction_value_head-payment.tds_amount}</td>
								</tr>
								<c:if test="${payment.CGST_head!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.CGST_head}" />
									</td>												
											
										</tr>				
								</c:if>
								<c:if test="${payment.SGST_head!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SGST_head}" />
									</td>												
										</tr>				
								</c:if>
								<c:if test="${payment.IGST_head!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.IGST_head}" />
									</td>																						
										</tr>				
								</c:if>
								<c:if test="${payment.SCT_head!=0}">
										<tr>
											<td>CESS										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SCT_head}" />
									</td>																														
										</tr>	
								</c:if>
								<c:if test="${payment.total_vat!=''}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vat}" />
									</td>												
																	
										</tr>	
								</c:if>
								<c:if test="${payment.total_vatcst!=''}">
										<tr>
											<td>CST</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vatcst}" />
									</td>	
											
										</tr>	
								</c:if>
								<c:if test="${payment.total_excise!=''}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_excise}" />
									</td>	
																			
										</tr>	
								</c:if>
								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through cash.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr> 	
								
							</c:when>
							<c:otherwise>
								<tr>
									<td> ${payment.bank.bank_name} -
										${payment.bank.account_no} <br /> Cur Bal:
										${(payment.supplier.openingbalances.debit_balance)-(payment.supplier.openingbalances.credit_balance)}
										
									</td>
									<td></td>
									<td>${payment.transaction_value_head}</td>
								</tr>
								
									<c:if test="${payment.CGST_head!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>												
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.CGST_head}" />
									</td>		
										</tr>				
								</c:if>
								<c:if test="${payment.SGST_head!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SGST_head}" />
									</td>		
										</tr>				
								</c:if>
								<c:if test="${payment.IGST_head!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.IGST_head}" />
									</td>									
										</tr>				
								</c:if>
								<c:if test="${payment.SCT_head!=0}">
										<tr>
											<td>CESS										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.SCT_head}" />
									</td>		
																													
										</tr>	
								</c:if>
								<c:if test="${payment.total_vat!=''}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vat}" />
									</td>		
																					
										</tr>	
								</c:if>
								<c:if test="${payment.total_vatcst!=''}">
										<tr>
											<td>CST</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_vatcst}" />
									</td>		
											
										</tr>	
								</c:if>
								<c:if test="${payment.total_excise!=''}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.total_excise}" />
									</td>		
																					
										</tr>	
								</c:if>

								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through ${payment.bank.bank_name} - ${payment.bank.account_no} by ${payment.cheque_dd_no}.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
							</c:otherwise>
						</c:choose>
							
							
							
							</c:when>
							<c:otherwise>
								<c:choose>
							<c:when test='${payment.supplier!=null}'>
								<c:set var="cust_name" value="${payment.supplier.company_name}"></c:set>
								<c:set var="nrtn" value="amount paid to"></c:set>
								<tr>
									<td>${payment.supplier.company_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" /> Dr
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>
												<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.tds_amount}" />
									</td>		
												
												
											</tr>									
							   </c:if>
							</c:when>
							<c:otherwise>
								<c:set var="cust_name"
									value="${payment.subLedger.subledger_name}"></c:set>
								<c:set var="nrtn" value="payment made for"></c:set>
								<tr>
									<td>${payment.subLedger.subledger_name} <br /> Cur Bal:
										<fmt:formatNumber type="number" value="${closingbalance}" />
										
										
									</td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
									<td></td>
								</tr>
								<c:if test="${payment.tds_paid==true}">									
											<tr>
												<td>TDS
												</td>
												<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.tds_amount}" />
									</td>		
											</tr>									
									</c:if>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${payment.payment_type==1}">
								<tr>
									<td> Cash In Hand</td>
									<td></td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
								</tr>

								<tr style='border: 1px solid #bbb'>
								 <c:if test="${payment.supplier != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through cash.
									</td>
								</c:if>
								 <c:if test="${payment.subLedger != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} through cash.
									</td>
								</c:if>
								<td></td>
								<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td> ${payment.bank.bank_name} -
										${payment.bank.account_no} <br /> Cur Bal:
											<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${(payment.supplier.openingbalances.debit_balance)-(payment.supplier.openingbalances.credit_balance)}" />
										
									</td>
									<td></td>
									<td> <c:choose>
								<c:when test="${payment.tds_paid==true}"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
									</c:when>
								<c:otherwise><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" />
								   </c:otherwise>
					   </c:choose></td>
								</tr>

                                <c:if test="${payment.supplier != null}">
								<tr style='border: 1px solid #bbb'>
									<td>Narration: <br /> Being ${nrtn} ${cust_name} against ${sup_bill_no} through ${payment.bank.bank_name}${payment.bank.account_no} by ${payment.cheque_dd_no}.
									</td>
									<td></td>
									<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
									
								</tr>
								</c:if>
								
								  <c:if test="${payment.subLedger != null}">
								<tr style='border: 1px solid #bbb'>
								 <c:if test="${payment.bank != null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name} through ${payment.bank.bank_name}${payment.bank.account_no}.
									</td>
								</c:if>
								 <c:if test="${payment.bank == null}">
									<td>Narration: <br /> Being ${nrtn} ${cust_name}.
									</td>
								</c:if>
								<td></td>
								<td></td>
									<c:choose>
										<c:when test="${payment.tds_paid==true}">	
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount+payment.tds_amount}" /></td>
										</c:when>
										<c:otherwise>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
										maxFractionDigits="2" value="${payment.amount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${payment.amount}" /></td>
										</c:otherwise>
									</c:choose>		
								</tr>
								</c:if>
							</c:otherwise>
						</c:choose>
								
						</c:otherwise>
						</c:choose>
					
				</tbody>
				
			</table>
		</div>  --%>
			
			
			<!--PDF view starts for the Account Voucher  -->
		</div>
	</form:form>
		<div class="row"  style = "text-align: center; margin:15px;">	
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back" />
			</button>
		</div>
</div>
<script type="text/javascript">
	
	
function paymentAccountingVoucherPdf(selector, params) {
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
	
	function edit(id){
		window.location.assign('<c:url value = "editPayment"/>?id='+id);	
	}
	
	function back(){
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "paymentList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "paymentFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadPayment"/>?id='+id);	
	}
	function DownloadAccountingPdf(id){
		window.location.assign('<c:url value = "downloadPaymentAccountingVoucher"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>