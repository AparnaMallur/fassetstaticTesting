<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/receiptAccountingVoucher.js" var="tableexport" />
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
	<a href=""><i class="fa fa-arrow-left" aria-hidden="true"></i> Back</a><h3>Receipt</h3>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>

<div class="fassetForm">
	<form:form id="ReceiptForm" commandName="receipt">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active">
				<a href="#home" data-toggle="tab" aria-expanded="true"> <span class="visible-xs"><i class="fa fa-home"></i></span> <span class="hidden-xs">Details</span></a>
			</li>
			<!-- <li class=""><a href="#profile" data-toggle="tab"
				aria-expanded="false"> <span class="visible-xs"><i
						class="fa fa-user"></i></span> <span class="hidden-xs">Accounting
						Voucher</span>
			</a></li> -->
		</ul>
		<div class="tab-content"  style="padding:0;">
			<div class="tab-pane active" id="home">
				<div class="table-responsive">
					<table class="table">
						<%--<tr><td colspan='6' style='text-align:center'><strong>Financial Year:${receipt.accountingYear.year_range}</strong></td></tr> --%>
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${receipt.voucher_no}</td>
							<td><strong>Customer Name:</strong></td>
							<td style="text-align: left;">
								<c:choose>
									<c:when test='${receipt.customer==null}'>Income</c:when>
									<c:otherwise>${receipt.customer.firm_name} </c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td><strong>Date:</strong></td>
							<td style="text-align: left;">
								<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" /> 
								<fmt:formatDate value="${parsedDate}" var="bill_date" type="date" pattern="dd-MM-yyyy" />
								${bill_date}
							</td>
							<c:choose>
								<c:when test='${receipt.customer==null}'>
									<td><strong>Income Type:</strong></td>
									<td style="text-align: left;">${receipt.subLedger.subledger_name}</td>
								</c:when>
								<c:otherwise>
									<td><strong>Bill No:</strong></td>
									<td style="text-align: left;">
										<c:choose>
											<c:when test="${receipt.sales_bill_id!=null}">
												${receipt.sales_bill_id.voucher_no}	
												<c:set var="cust_bill_no" value="${receipt.sales_bill_id.voucher_no}"></c:set>
											</c:when>
											<c:otherwise>
												<c:set var="cust_bill_no" value="Advance"></c:set>	Advance
											</c:otherwise>
										</c:choose>
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
						<tr>
							<td><strong>Cheque No:</strong></td>
							<td style="text-align: left;">${receipt.cheque_no}</td>
							<td><strong>Advance Receipt:</strong></td>
							<td style="text-align: left;">${receipt.advance_payment==true ? "Yes" : "No"}</td>
						</tr>
						<tr>
							<td><strong>Receipt Type:</strong></td>
							<td style="text-align: left;">
								<c:choose>
									<c:when test="${receipt.payment_type==1}">Cash</c:when>
									<c:when test="${receipt.payment_type==2}">Cheque</c:when>
									<c:when test="${receipt.payment_type==3}">DD</c:when>
									<c:otherwise>NEFT/RTGS/Net banking/Account Transfer</c:otherwise>
								</c:choose>
							</td>
							<td><strong>Bank Name:</strong></td>
							<td>
								<c:choose>
									<c:when test="${receipt.payment_type==1}">NA</c:when>
									<c:otherwise>
										${receipt.bank.bank_name}${receipt.bank.account_no}								
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td><strong>Cheque Date:</strong></td>
							<td style="text-align: left;">${receipt.cheque_date}</td>
							<td><strong>GST Applicable:</strong></td>
							<td style="text-align: left;">${receipt.gst_applied==1 ? "Yes" : "No"}</td>
						</tr>
						<tr>
							<td><strong>Other Remark:</strong></td>
							<td style="text-align: left;">${receipt.other_remark}</td>
							<td><strong>Amount:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber	type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}" /></td>
						</tr>
						<tr>
							<td><strong>Transaction Amount:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber	type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.transaction_value}" /></td>
							<td><strong>CGST:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber	type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.cgst}" /></td>
						</tr>
						<tr>
							<td><strong>SGST:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber	type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.sgst}" /></td>
							<td><strong>IGST:</strong></td>
							<td style="text-align: left;">${receipt.igst}</td>
						</tr>
						<tr>
							<td><strong>CESS:</strong></td>
							<td style="text-align: left;">${receipt.state_compansation_tax}</td>
							<td><strong>TDS Paid:</strong></td>
							<td style="text-align: left;">${receipt.tds_paid==true ? "Yes" : "No"}</td>
						</tr>
						<tr>
							<c:if test="${receipt.tds_paid==true}">
								<td><strong>TDS Amount:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber	type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.tds_amount}" /></td>
							</c:if>
							<td><strong>Status:</strong></td>
							<td style="text-align: left;">${receipt.entry_status==3 ? "Disable" : "Enable"}</td>
						
							<td><strong>Closing Balance:</strong></td>
							<td style="text-align: left;"><fmt:formatNumber	type="number" value="${closingbalance}" /></td>
							<c:if test="${receipt.excel_voucher_no != null}">
								<td><strong>Excel Voucher No</strong></td>
							</c:if>
							<c:if test="${receipt.excel_voucher_no != null}">
								<td>${receipt.excel_voucher_no}</td>
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

				<c:if test="${(receipt.gst_applied == 1)}">
					<!-- <h3>Product Details</h3> -->
					<%-- <table class="table table-bordered table-striped" >
						<thead>
							<tr>
								<th>Product</th>
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
							<c:forEach var="prod_list" items="${customerproductList}">
								<tr>
									<td>${prod_list.product_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.quantity}" /></td>
									<td>${prod_list.UOM}</td>
									<td>${prod_list.HSNCode}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.rate}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.discount}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.freight}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.others}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.CGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.SGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.IGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.state_com_tax}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>--%>
					 
					<div class="panel-group">
					    <div class="panel panel-default">
					      <div class="panel-heading">
					        <h3 class="panel-title">
					          <a data-toggle="collapse" href="#collapse1">Product Details</a>
					        </h3>
					      </div>
					      <div id="collapse1" class="panel-collapse collapse">
					        <div class="panel-body">
						        <c:forEach var="prod_list" items="${customerproductList}">
						        	<table class="table table-bordered table-striped" style="border: 2px solid #000;">
										<tbody>
											<tr>
												<td>Product</td>
												<td>${prod_list.product_name}</td>
											</tr>
											<tr>
												<td>Quantity</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.quantity}" /></td>
											</tr>
											<tr>
												<td>UOM</td>
												<td>${prod_list.UOM}</td>
											</tr>
											<tr>
												<td>HSN/SAC Code</td>
												<td>${prod_list.HSNCode}</td>
											</tr>
											<tr>
												<td>Rate</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.rate}" /></td>
											</tr>
											<tr>
												<td>Discount</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.discount}" /></td>
											</tr>
											<tr>
												<td>Labour Charge</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
											</tr>
											<tr>
												<td>Freight Charges</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.freight}" /></td>
											</tr>
											<tr>
												<td>Others</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.others}" /></td>
											</tr>
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.CGST}" /></td>
											</tr>
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.SGST}" /></td>
											</tr>
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.IGST}" /></td>
											</tr>
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.state_com_tax}" /></td>
											</tr>
										</tbody>
									</table>
								</c:forEach>
					        </div>
					      </div>
					    </div>
					</div>
				</c:if>
				<%-- <div class="row" style="text-align: center; margin: 15px;">
					<c:if test="${receipt.entry_status!=3 || receipt.entry_status!=4}">
						<button class="fassetBtn" type="button"	onclick="DownloadPdf(${receipt.receipt_id})">Download As Pdf</button>
					</c:if>
	            </div> --%>
			</div>
			
			<!--UI view for Account Voucher started -------------------------------------------------------------------- -->
			
<%-- 			<div class="tab-pane" id="profile">
				<div class="col-md-12">
					<p class="col-md-6 col-xs-6 text-left" style="padding-left: 0px">
						<b>Receipt No: ${receipt.voucher_no}</b>
					</p>
					<p class="col-md-6 col-xs-6 text-right" style="padding-left: 0px">
						<b>Date: ${bill_date}</b>
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
							<c:when test="${receipt.gst_applied==1}">
								<c:choose>
									<c:when test='${receipt.customer!=null}'>									
										<c:set var="cust_name" value="${receipt.customer.firm_name}"></c:set>
										<tr>
											<td>${receipt.customer.firm_name} <br /> Cur Bal: <fmt:formatNumber	type="number" value="${closingbalance}" /></td>
											<td></td>
											<c:choose>
												<c:when test="${receipt.tds_paid==true}">
													<td>
														<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount+receipt.tds_amount}" />
													</td>
												</c:when>
												<c:otherwise>
													<td>
														<fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount}" />
													</td>
								   				</c:otherwise>
					  						 </c:choose>
										</tr>
										<c:if test="${receipt.tds_paid==true}">
											<tr>
												<td>TDS</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.tds_amount}" /></td>
												<td></td>															
											</tr>		
										</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="cust_name"	value="${receipt.subLedger.subledger_name}"></c:set>
										<tr>
											<td>${receipt.subLedger.subledger_name} <br/> 
												Current Bal: <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${closingbalance}" /> 
											</td>
											<td></td>
											<c:choose>
												<c:when test="${receipt.tds_paid==true}">
													<td>
														<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount+receipt.tds_amount}" />
													</td>
												</c:when>
												<c:otherwise>
													<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount}" /></td>
												</c:otherwise>
					   						</c:choose>
										</tr>
										<c:if test="${receipt.tds_paid==true}">
											<tr>
												<td>TDS</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.tds_amount}" /></td>
												<td></td>															
											</tr>		
										</c:if>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${receipt.payment_type==1}">
										<tr>
											<td>Cash In Hand</td>
											<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.transaction_value}" /></td>
											<td></td>
										</tr>
										<c:if test="${receipt.cgst!=0}">
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.cgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.sgst!=0}">
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.sgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.igst!=0}">
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.igst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.state_compansation_tax!=0}">
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.state_compansation_tax}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.total_vat!=0}">
											<tr>
												<td>VAT</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.total_vat}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.total_vatcst!=0}">
											<tr>
												<td>CST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.total_vatcst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.total_excise!=0}">
											<tr>
												<td>EXCISE</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2" value="${receipt.total_excise}" /></td>
												<td></td>
											</tr>
										</c:if>
										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in Cash	from ${cust_name} against ${cust_bill_no}.</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in Cash	from ${cust_name}.</td>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${receipt.tds_paid==true}">
													<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount+receipt.tds_amount}" />	</td>
												</c:when>
												<c:otherwise>
													<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount}" /></td>
												</c:otherwise>
					   						</c:choose>
											<c:choose>
												<c:when test="${receipt.tds_paid==true}">
													<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount+receipt.tds_amount}" /></td>
												</c:when>
												<c:otherwise>
													<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.amount}" /></td>
												</c:otherwise>
										   </c:choose>
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<td> ${receipt.bank.bank_name} -	${receipt.bank.account_no}</td>
											<td>${receipt.transaction_value}</td>
											<td></td>
										</tr>
                                        <c:if test="${receipt.cgst!=0}">
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.cgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.sgst!=0}">
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number"	minFractionDigits="2" maxFractionDigits="2"	value="${receipt.sgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.igst!=0}">
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.igst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.state_compansation_tax!=0}">
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.state_compansation_tax}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vat!=0}">
											<tr>
												<td>VAT</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vat}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vatcst!=0}">
											<tr>
												<td>CST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vatcst}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_excise!=0}">
											<tr>
												<td>EXCISE</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_excise}" /></td>
												<td></td>

											</tr>
										</c:if>
										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name}${receipt.bank.account_no} from
														${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
										Being amount received in
										${receipt.bank.bank_name}${receipt.bank.account_no} from ${cust_name}.
										</c:otherwise>
											</c:choose>

											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
								
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
									</c:otherwise>
								</c:choose>



							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test='${receipt.customer!=null}'>
										<c:set var="cust_name" value="${receipt.customer.firm_name}"></c:set>
										<tr>
											<td>${receipt.customer.firm_name} <br /> Cur Bal: <fmt:formatNumber
													type="number" value="${closingbalance}" /> 
											</td>
											<td></td>
											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
										
											<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>
												     </tr>
											</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="cust_name"
											value="${receipt.subLedger.subledger_name}"></c:set>

										<tr>
											<td>${receipt.subLedger.subledger_name} <br /> Cur
												Bal: <fmt:formatNumber type="number" 
													value="${closingbalance}" /> 
											</td>
											<td></td>
											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
										<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>															
												     </tr>		
										</c:if>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${receipt.payment_type==1}">
										<tr>
											<td>Cash In Hand</td>
											<td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" /></td>
											<td></td>
										</tr>

										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name}.
													</td>
												</c:otherwise>
											</c:choose>

											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<td>${receipt.bank.bank_name} -
												${receipt.bank.account_no} 
											</td>
											<td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" /></td>
											<td></td>
										</tr>

										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name} - ${receipt.bank.account_no}
														from ${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name} - ${receipt.bank.account_no}
														from ${cust_name}.
													</td>

												</c:otherwise>
											</c:choose>

										 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
									</c:otherwise>
								</c:choose>

							</c:otherwise>
						</c:choose>

					</tbody>
				</table>
				<div class="row" style="text-align: center; margin: 15px;">

		<c:if test="${receipt.entry_status!=3 || receipt.entry_status!=4}">
			<button class="fassetBtn" type="button"  onclick ="receiptAccountingVoucherPdf('#Hiddentable', {type: 'pdf',
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
			
			<!--UI view for Account Voucher ended  --------------------------------------------------------------------------------->
			
			
			<!-- PDF view for Account voucher started ------------------------------------------------------------------------------>
			
			<%-- <div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Receipt</td>
						
					</tr>
			
					<tr>
							<td>Date: ${bill_date}</td>
					</tr>
			
					<tr>
						<td>Receipt No: ${receipt.voucher_no}</td>
						
					</tr>
				
					<tr id="row">
					
						<th style="width: 70%">Particulars</th>
							<th>Debit</th>
							<th>Credit</th>
					
					</tr>
				<tbody>
				    
				  <c:choose>
							<c:when test="${receipt.gst_applied==1}">
								<c:choose>
									<c:when test='${receipt.customer!=null}'>									
										<c:set var="cust_name" value="${receipt.customer.firm_name}"></c:set>
										<tr>
											<td>${receipt.customer.firm_name} <br /> Cur Bal: <fmt:formatNumber
													type="number" value="${closingbalance}" /> 
											</td>
											<td></td>
											
											  <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
					   
										</tr>
											<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>															
												     </tr>		
										</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="cust_name"
											value="${receipt.subLedger.subledger_name}"></c:set>

										<tr>
											<td>${receipt.subLedger.subledger_name} <br /> Cur
												Bal: <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
													value="${closingbalance}" /> 
											</td>
											<td></td>
											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
										<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>															
												     </tr>		
										</c:if>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${receipt.payment_type==1}">
										<tr>
											<td>Cash In Hand</td>
											<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.transaction_value}" /></td>
											<td></td>
										</tr>
										<c:if test="${receipt.cgst!=0}">
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.cgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.sgst!=0}">
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.sgst}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.igst!=0}">
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.igst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.state_compansation_tax!=0}">
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.state_compansation_tax}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vat!=0}">
											<tr>
												<td>VAT</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vat}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vatcst!=0}">
											<tr>
												<td>CST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vatcst}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_excise!=0}">
											<tr>
												<td>EXCISE</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_excise}" /></td>
												<td></td>

											</tr>
										</c:if>
										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name}.
													</td>
												</c:otherwise>
											</c:choose>

											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
											
												
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
											
										</tr>

									</c:when>
									<c:otherwise>
										<tr>
											<td> ${receipt.bank.bank_name} -
												${receipt.bank.account_no} <br /> Cur Bal: <fmt:formatNumber
													type="number" minFractionDigits="2" maxFractionDigits="2"
													value="${(receipt.customer.openingbalances.debit_balance)-(receipt.customer.openingbalances.credit_balance)}" />
									
											</td>
											<td>${receipt.transaction_value}</td>
											<td></td>
										</tr>

                                        <c:if test="${receipt.cgst!=0}">
											<tr>
												<td>CGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.cgst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.sgst!=0}">
											<tr>
												<td>SGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.sgst}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.igst!=0}">
											<tr>
												<td>IGST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.igst}" /></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${receipt.state_compansation_tax!=0}">
											<tr>
												<td>CESS</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.state_compansation_tax}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vat!=0}">
											<tr>
												<td>VAT</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vat}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_vatcst!=0}">
											<tr>
												<td>CST</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_vatcst}" /></td>
												<td></td>

											</tr>
										</c:if>
										<c:if test="${receipt.total_excise!=0}">
											<tr>
												<td>EXCISE</td>
												<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.total_excise}" /></td>
												<td></td>

											</tr>
										</c:if>
										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name}${receipt.bank.account_no} from
														${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
										Being amount received in
										${receipt.bank.bank_name}${receipt.bank.account_no} from ${cust_name}.
										</c:otherwise>
											</c:choose>

											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
								
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
									</c:otherwise>
								</c:choose>



							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test='${receipt.customer!=null}'>
										<c:set var="cust_name" value="${receipt.customer.firm_name}"></c:set>
										<tr>
											<td>${receipt.customer.firm_name} <br /> Cur Bal: <fmt:formatNumber
													type="number" value="${closingbalance}" /> 
											</td>
											<td></td>
											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
										
											<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>
												     </tr>
											</c:if>
									</c:when>
									<c:otherwise>
										<c:set var="cust_name"
											value="${receipt.subLedger.subledger_name}"></c:set>

										<tr>
											<td>${receipt.subLedger.subledger_name} <br /> Cur
												Bal: <fmt:formatNumber type="number" 
													value="${closingbalance}" /> 
											</td>
											<td></td>
											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
										<c:if test="${receipt.tds_paid==true}">
													<tr>
													<td>TDS
													</td>
													<td><fmt:formatNumber type="number"
															minFractionDigits="2" maxFractionDigits="2"
															value="${receipt.tds_amount}" /></td>
												<td></td>															
												     </tr>		
										</c:if>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${receipt.payment_type==1}">
										<tr>
											<td>Cash In Hand</td>
											<td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" /></td>
											<td></td>
										</tr>

										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in Cash
														from ${cust_name}.
													</td>
												</c:otherwise>
											</c:choose>

											 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
										</tr>
									</c:when>
									<c:otherwise>
										<tr>
											<td>${receipt.bank.bank_name} -
												${receipt.bank.account_no} <br /> Cur Bal: <fmt:formatNumber
													type="number" minFractionDigits="2" maxFractionDigits="2"
													value="${(receipt.customer.openingbalances.debit_balance)-(receipt.customer.openingbalances.credit_balance)}" />
												
											</td>
											<td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" /></td>
											<td></td>
										</tr>

										<tr style='border: 1px solid #bbb'>
											<c:choose>
												<c:when test='${receipt.customer!=null}'>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name} - ${receipt.bank.account_no}
														from ${cust_name} against ${cust_bill_no}.
													</td>
												</c:when>
												<c:otherwise>
													<td>Narration: <br /> Being amount received in
														${receipt.bank.bank_name} - ${receipt.bank.account_no}
														from ${cust_name}.
													</td>

												</c:otherwise>
											</c:choose>

										 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
												
												 <c:choose>
								<c:when test="${receipt.tds_paid==true}"><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount+receipt.tds_amount}" />
											</td>
									</c:when>
								<c:otherwise><td><fmt:formatNumber type="number"
													minFractionDigits="2" maxFractionDigits="2"
													value="${receipt.amount}" />
											</td>
								   </c:otherwise>
					   </c:choose>
										</tr>
									</c:otherwise>
								</c:choose>

							</c:otherwise>
						</c:choose>
					
				</tbody>
				
			</table>
		</div>  --%>
			
			
			<!-- PDF view for Account voucher ended -->
		</div>
	</form:form>
	<div class="row" style="text-align: center; margin: 15px;">
		<button class="fassetBtn" type="button" onclick="back()"><spring:message code="btn_back" /></button>
	</div>
</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function receiptAccountingVoucherPdf(selector, params) {
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
		window.location.assign('<c:url value = "editReceipt"/>?id='+id);	
	}
	
	function back(){
		
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "receiptList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "receiptFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadReceipt"/>?id='+id);	
	}
	
	function DownloadAccountingPdf(id){
		window.location.assign('<c:url value = "downloadReceiptAccountingVoucher"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/mobileFooter.jsp"%>