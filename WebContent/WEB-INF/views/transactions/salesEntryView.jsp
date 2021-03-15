<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/salesAccountingVoucher.js" var="tableexport" />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
 <script type="text/javascript" src="${jspdfmin}"></script>
 <script type="text/javascript" src="${jspdfauto}"></script>
 <script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>Sales Entry</h3>					
	<a href="homePage">Home</a> » <a href="salesEntryList">Sales Entry</a> » <a href="#">View</a>
</div>
<div class="fassetForm">

		<form:form id="SalesEntryForm"  commandName = "entry">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active"><a href="#home" data-toggle="tab"
				aria-expanded="true"> <span class="visible-xs"><i
						class="fa fa-home"></i></span> <span class="hidden-xs">Details</span>
			</a></li>
			<li class=""><a href="#profile" data-toggle="tab"
				aria-expanded="false"> <span class="visible-xs"><i
						class="fa fa-user"></i></span> <span class="hidden-xs">Accounting
						Voucher</span>
			</a></li>

		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="home">
						<div class="col-md-12">
				<table class = "table">
					<tr><td colspan='4' style='text-align:center'><strong> Financial Year:${entry.accountingYear.year_range}</strong></td></tr>
				<tr>
						<td><strong>Voucher No</strong></td>
						<td style="text-align: left;">${entry.voucher_no}</td>
						
						<td><strong>Customer Name</strong></td>
						<td style="text-align: left;">
							 <c:choose>
			                    <c:when test='${entry.sale_type==1}'>Cash Sales</c:when>
			                    <c:when test='${entry.sale_type==2}'>Card Sales - ${entry.bank.bank_name} - ${entry.bank.account_no}</c:when>			                
			                    <c:otherwise>${entry.customer.firm_name} </c:otherwise>
                  		  </c:choose>
                  		 </td>							
					</tr>
					<tr>
						<td><strong>Bill Date</strong></td>
						<td style="text-align: left;">
							 <fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			 <fmt:formatDate value="${parsedDate}" var="customer_bill_date" type="date" pattern="dd-MM-yyyy"  />
						${customer_bill_date}</td>
						<td><strong>Income type</strong></td>
						<td style="text-align: left;">${entry.subledger.subledger_name}</td>				
					</tr>
					<tr>

						<td><strong>Remark</strong></td>
						<td style="text-align: left;" >${entry.remark}</td>
						<td><strong>Transaction Amount</strong></td>
						<td style="text-align: left;" ><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" /></td>
					</tr>
								<tr>
							    <td><strong>CGST</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${entry.cgst}" /></td>
								<td><strong>SGST</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${entry.sgst}" /></td>
							</tr>
							<tr>
								<td><strong>IGST</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${entry.igst}" /></td>
							
							<td><strong>CESS</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${entry.state_compansation_tax}" /></td>	
											
								
							</tr>
							
									<tr>
								    <td><strong>VAT</strong></td>
									<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${entry.total_vat}" /></td>
									<td><strong>CST</strong></td>
									<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${entry.total_vatcst}" /></td>
								</tr>
								<tr>
									<td><strong>EXCISE</strong></td>
									<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${entry.total_excise}" /></td>
								
								
								<c:if test="${entry.excel_voucher_no != null}">
						<td><strong>Excel Voucher No</strong></td>
						</c:if>
					<c:if test="${entry.excel_voucher_no != null}">
						<td>${entry.excel_voucher_no}</td>
						</c:if>
								</tr>
						
					
					<tr>			
						<td><strong>Total Amount</strong></td>							    
						<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" /></td>
						<td><strong>Entry Type</strong></td>
						<td style="text-align: left;">${entry.entrytype==1 ? "Local" : "Export" }
						</td>
						
					</tr>
					
					<tr>
					<c:if test="${entry.tds_amount>0}">
					<td><strong>TDS Amount:</strong></td>
									<td style="text-align: left;">
									<c:choose>
											<c:when test="${entry.tds_amount!=null && entry.tds_amount>0}">
											<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.tds_amount}" />
											</c:when>
											<c:otherwise>
											0</c:otherwise>
					              </c:choose>
									
				                 </td>
				     </c:if>       
					<td><strong>Closing Balance</strong></td>
					<td><fmt:formatNumber type="number" value="${closingbalance}" />
					</td>
					
					<tr>			
						<td><strong>Created By</strong></td>							    
						<td style="text-align: left;">${created_by.first_name} ${created_by.last_name}</td>
						<td><strong>Updated By</strong></td>
						<td style="text-align: left;">${updated_by.first_name} ${updated_by.last_name}</td>
					</tr>
				</table>
			</div>
		<h3>Product Details</h3>			
			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th>Product </th>
						<th>Quantity</th>
						<th>UOM</th>
						<th>HSN/SAC Code</th>
						<th>Rate</th>
						<th>Discount(%)</th>
						<th>Labour Charge</th>
						<th>Freight Charges</th>
						<th>Others</th>
						<th>CGST/VAT</th>
						<th>SGST/CST</th>
						<th>IGST/EXCISE</th>
						<th>CESS</th>
						
					</tr>
			</thead>
			<tbody>
						<c:forEach var = "prod_list" items = "${customerProductList}">	
								<tr>
									<td>${prod_list.product_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.quantity}" />
									</td>
									
									<td>${prod_list.UOM}</td>
									<td>${prod_list.HSNCode}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.rate}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.discount}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.labour_charges}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.freight}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.others}" />
									</td>
								
									<c:if test="${prod_list.is_gst == 1}">
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.CGST}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.SGST}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.IGST}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.state_com_tax}" />
									</td>
									</c:if>
									<c:if test="${prod_list.is_gst == 2}">
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.VAT}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.VATCST}" />
									</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${prod_list.excise}" />
									</td>
									<td>
									<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="0.0" />
								    </td>
									</c:if>
								</tr>
							</c:forEach>	
			</tbody>
		</table>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<c:if test="${entry.entry_status!=3}">
				<button class="fassetBtn" type="button" onclick = "DownloadPdf(${entry.sales_id})"> 
			   Download As Pdf
		        </button>
				</c:if>
		</div>
		</div>
		
		<!--UI view started for Account Voucher  -->
		
			<div class="tab-pane" id="profile">
			<div class="row">
				<div class="col-md-12">
					<p class="col-md-6 col-xs-6 text-left" style="padding-left:0px">			
					<b>Sales Voucher No: ${entry.voucher_no}</b>
					</p>
					<p class="col-md-6 col-xs-6 text-right" style="padding-left:0px">			
					<b>Date: ${customer_bill_date}</b>
					</p>
				</div>
				<div class="col-md-12">
						 <c:choose>
						 	 <c:when test='${entry.sale_type==1}'><p><b>Party A/c Name: Cash Sales</b></p>
						 	 <c:set var="customername" value="Cash Sales"></c:set>
						 	 </c:when>
						 	 <c:when test='${entry.sale_type==2}'><p><b>Party A/c Name: Card Sales - ${entry.bank.bank_name} - ${entry.bank.account_no}</b></p>
						 	 		<c:set var="customername" value="Card Sales- ${entry.bank.bank_name} - ${entry.bank.account_no}"></c:set>
						 	 </c:when>
						     <c:otherwise>
			                    <p><b>Party A/c Name: ${entry.customer.firm_name}</b></p>					
						<c:set var="customername" value="${entry.customer.firm_name}"></c:set>					
			                     </c:otherwise>			                     
                  	</c:choose>	
                  
                  	<p><b>Current Balance: <fmt:formatNumber type="number" value="${closingbalance}" />
					</b></p>
					
				</div>
			</div>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 70%">Particulars</th>
							<th>Rate</th>
							<th>Amount</th>
						</tr>
					</thead>
					<tbody>
					
					            <tr>
								
									<td>${customername}</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" /> Dr</td>
								</tr>
								<tr>
									<td>${entry.subledger.subledger_name}										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" /> Cr</td>
								</tr>
								<c:if test="${entry.tds_amount>0}">
								<tr>
									<td>TDS										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.tds_amount}" /> Dr</td>
								</tr>
								</c:if>
								<%-- <c:if test="${entry.receipts!=null}">                  	
									<c:forEach var="receipt" items="${entry.receipts}">
									<c:set var="TotalTds" value="${TotalTds + receipt.tds_amount}" />						
									</c:forEach>
								</c:if>
								<c:if test="${TotalTds!=0}">								
								<tr>
									<td>TDS</td>
									<td></td>
									<td>${TotalTds} Dr</td>
								</tr>
								</c:if> --%>
					
								<c:if test="${entry.cgst!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.cgst}" /> Cr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.sgst!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.sgst}" /> Cr</td>											
										</tr>				
								</c:if>
								<c:if test="${entry.igst!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.igst}" /> Cr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.state_compansation_tax!=0}">
										<tr>
											<td>CESS										
												</td>
												<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.state_compansation_tax}" /> Cr</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vat!=0}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vat}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vatcst!=0}">
										<tr>
											<td>CST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vatcst}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_excise!=0}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_excise}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<tr style='border:1px solid #bbb'>
								<c:if test="${entry.excel_voucher_no ==null }">
									<td>Narration: <br /> Being supply effected to ${customername} through ${entry.voucher_no} dated ${customer_bill_date}.</td>
								</c:if>
								
								<c:if test="${entry.excel_voucher_no!=null }">
									<td>Narration: <br /> Being supply effected to ${customername} through ${entry.excel_voucher_no} dated ${customer_bill_date}.</td>
								</c:if>	
								
								<%-- 	<td>Narration: <br /> Being supply effected to ${customername} through ${entry.voucher_no} dated ${customer_bill_date}.</td>
								 --%>	<td></td>
									<td></td>
								</tr>
									<tr style='border:1px solid #bbb'>
								<c:if test="${entry.remark!=null }">
								
                 <td  colspan="3">Remark:  ${entry.remark}</td>
                 
            </c:if>
									<c:if test="${entry.remark==null }">
								
                 <td  colspan="3">Remark:  </td>
                 
            </c:if>
								
								</tr>
					


								<%-- <tr style='border:1px solid #bbb'>
								
									<td>Narration: <br /> Being supply effected to ${customername} through ${entry.voucher_no} dated ${customer_bill_date}.</td>
									<td></td>
									<td></td>
								</tr> --%>
					</tbody>
				</table>
				 <div class="col-md-12"  style = "text-align: center; margin:15px;">
				<c:if test="${entry.entry_status!=3}">
				<button class="fassetBtn" type="button"  onclick ="salesAccountingVoucherPdf('#Hiddentable', {type: 'pdf',
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
			  </div>
			  
			  <!--UI view ended for Account Voucher  -->
			  
			  
			  <!--PDF view started for Account Voucher  -->
			  
			  
			  <div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Sales</td>
						
					</tr>
			
					<tr>
							<td>Date: ${customer_bill_date}</td>
					</tr>
			
					<tr>
						<td>Sales Voucher No: ${entry.voucher_no}</td>
						
					</tr>
					
					<tr>
					
						<c:choose>
						 	 <c:when test='${entry.sale_type==1}'><td>Party A/c Name: Cash Sales</td>
						 	 <c:set var="customername" value="Cash Sales"></c:set>
						 	 </c:when>
						 	 <c:when test='${entry.sale_type==2}'><td>Party A/c Name: Card Sales - ${entry.bank.bank_name} - ${entry.bank.account_no}</td>
						 	 		<c:set var="customername" value="Card Sales- ${entry.bank.bank_name} - ${entry.bank.account_no}"></c:set>
						 	 </c:when>
						     <c:otherwise>
			                    <td>Party A/c Name: ${entry.customer.firm_name}</td>					
						<c:set var="customername" value="${entry.customer.firm_name}"></c:set>					
			                     </c:otherwise>			                     
                  	</c:choose>	
					
					</tr>
					
					<tr>
						<td>Current Balance: <fmt:formatNumber type="number" value="${closingbalance}" /></td>
					
					</tr>
					
					
				
					<tr id="row">
					
						<th style="width: 70%">Particulars</th>
							<th>Rate</th>
							<th>Amount</th>
					
					</tr>
				<tbody>
				    
				      <tr>
								
									<td>${customername}</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" /> Dr</td>
								</tr>
								<tr>
									<td>${entry.subledger.subledger_name}										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" /> Cr</td>
								</tr>
								<c:if test="${entry.tds_amount>0}">
								<tr>
									<td>TDS										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.tds_amount}" /> Dr</td>
								</tr>
								</c:if>
								<%-- <c:if test="${entry.receipts!=null}">                  	
									<c:forEach var="receipt" items="${entry.receipts}">
									<c:set var="TotalTds" value="${TotalTds + receipt.tds_amount}" />						
									</c:forEach>
								</c:if>
								<c:if test="${TotalTds!=0}">								
								<tr>
									<td>TDS</td>
									<td></td>
									<td>${TotalTds} Dr</td>
								</tr>
								</c:if> --%>
					
								<c:if test="${entry.cgst!=0}">
										<tr>
											<td>CGST										
												</td>
											<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.cgst}" /> Cr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.sgst!=0}">
										<tr>
											<td>SGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.sgst}" /> Cr</td>											
										</tr>				
								</c:if>
								<c:if test="${entry.igst!=0}">
										<tr>
											<td>IGST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.igst}" /> Cr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.state_compansation_tax!=0}">
										<tr>
											<td>CESS										
												</td>
												<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.state_compansation_tax}" /> Cr</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vat!=0}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vat}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vatcst!=0}">
										<tr>
											<td>CST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vatcst}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_excise!=0}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_excise}" /> Cr
									</td>
											
										</tr>	
								</c:if>
								<tr style='border:1px solid #bbb'>
								<c:if test="${entry.excel_voucher_no ==null }">
									<td>Narration: <br /> Being supply effected to ${customername} through ${entry.voucher_no} dated ${customer_bill_date}.</td>
								</c:if>
								
								<c:if test="${entry.excel_voucher_no!=null }">
									<td>Narration: <br /> Being supply effected to ${customername} through ${entry.excel_voucher_no} dated ${customer_bill_date}.</td>
								</c:if>	
								
								<%-- 	<td>Narration: <br /> Being supply effected to ${customername} through ${entry.voucher_no} dated ${customer_bill_date}.</td>
								 --%>	<td></td>
									<td></td>
								</tr>
								<tr style='border:1px solid #bbb'>
								<c:if test="${entry.remark!=null }">
								
                 <td  colspan="3">Remark:  ${entry.remark}</td>
                 
            </c:if>
									<c:if test="${entry.remark==null }">
								
                 <td  colspan="3">Remark:  </td>
                 
            </c:if>
								
								</tr>
					
				</tbody>
				
			</table>
		</div> 
			  
			  <!--PDF view ended for Account Voucher  -->
					
			</div>
		</form:form>
		 <div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
		</div>
	</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	

	 function salesAccountingVoucherPdf(selector, params) {
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
		window.location.assign('<c:url value = "editSalesEntry"/>?id='+id);	
	}
	
	function back(){
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "salesEntryList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "salesEntryFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadSales"/>?id='+id);	
	}
	
	function DownloadAccountingPdf(id){
		window.location.assign('<c:url value = "downloadSalesAccountingVoucher"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>