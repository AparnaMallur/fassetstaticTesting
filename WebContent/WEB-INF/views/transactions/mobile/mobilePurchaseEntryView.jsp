<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
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
<spring:url value="/resources/js/report_table/purchaseAccountingVoucher.js" var="tableexport" />
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
	<a href=""><i class="fa fa-arrow-left" aria-hidden="true"></i> Back</a><h3>Purchase Voucher</h3>
</div>
<div class="fassetForm">
		<form:form id="PurchaseEntryForm"  commandName = "entry">
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
		<div class="tab-content" style="padding:0;">
			<div class="tab-pane active" id="home">
				<div class="col-md-12">
					<div class="table-responsive">
						<table class = "table">
							<tr><td colspan='4' style='text-align:center'><strong>Financial Year:${entry.accountingYear.year_range}</strong></td></tr>
							<tr>
								<td><strong>Voucher No:</strong></td>
								<td style="text-align: left;">${entry.voucher_no}</td>
								<td><strong>Supplier Name:</strong></td>
								<td style="text-align: left;">${entry.supplier.company_name}</td>
							</tr>
							<tr>
								<td><strong>Supplier Bill Date:</strong></td>
								<td style="text-align: left;">
								<fmt:parseDate value="${entry.supplier_bill_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
		                  	    <fmt:formatDate value="${parsedDate}" var="supplier_bill_date" type="date" pattern="dd-MM-yyyy"  />
								${supplier_bill_date}</td>
								<td><strong>Supplier Bill Number:</strong></td>
								<td style="text-align: left;">${entry.supplier_bill_no}</td>					
							</tr>
							<tr>						
								<td><strong>Expense type:</strong></td>
								<td style="text-align: left;">${entry.subledger.subledger_name}</td>
								<td><strong>Remark:</strong></td>
								<td style="text-align: left;" >${entry.remark}</td>						
							</tr>
							<tr>					
								<td><strong>Bill date:</strong></td>
								<td style="text-align: left;">
									<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
			                  	    <fmt:formatDate value="${parsedDate}" var="voucher_date" type="date" pattern="dd-MM-yyyy" />
									${voucher_date}
								</td> 
								<td><strong>Transaction Amount:</strong></td>
								<td style="text-align: left;" ><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.transaction_value}" /></td>
							</tr>
							<tr>
								<td><strong>CGST:</strong></td>
								<td style="text-align: left;">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}" />
								</td>
								<td><strong>SGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.sgst}" /></td>
							</tr>
							<tr>
								<td><strong>IGST:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.igst}" /></td>
								<td><strong>CESS:</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.state_compansation_tax}" /></td>
							</tr>
							<tr>
							    <td><strong>VAT</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.total_vat}" /></td>
								<td><strong>CST</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.total_vatcst}" /></td>
							</tr>
							<tr>
								<td><strong>EXCISE</strong></td>
								<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.total_excise}" /></td>
								<c:if test="${entry.excel_voucher_no != null}">
									<td><strong>Excel Voucher No</strong></td>
								</c:if>
								<c:if test="${entry.excel_voucher_no != null}">
									<td>${entry.excel_voucher_no}</td>
								</c:if>
							</tr>
							<tr>
								<td>Export Type</td>
								<td style="text-align: left;">${entry.entrytype==1 ? "Local" : "Import" }
								<td><strong>Total Amount</strong></td>
		 						<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${entry.round_off}" /></td>
							</tr>
							<tr>
								<td><strong>TDS Amount:</strong></td>
								<td style="text-align: left;">
									<c:choose>
										<c:when test="${entry.tds_amount!=null && entry.tds_amount!=0}">
											<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.tds_amount}" />
										</c:when>
										<c:otherwise>0</c:otherwise>
									</c:choose>
								</td>
								<td><strong>Closing Balance</strong></td>
								<td><fmt:formatNumber type="number" value="${closingbalance}" /></td>
							</tr>
							<tr>			
								<td><strong>Created By</strong></td>							    
								<td style="text-align: left;">${created_by.first_name} ${created_by.last_name}</td>
								<td><strong>Updated By</strong></td>
								<td style="text-align: left;">${updated_by.first_name} ${updated_by.last_name}</td>
							</tr>
						</table>
					</div>
				</div>
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
							<th>CGST/VAT</th>
							<th>SGST/CST</th>
							<th>IGST/EXCISE</th>
							<th>CESS</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var = "prod_list" items = "${suppilerproductList}">	
							<tr>
								<td>${prod_list.product_name}</td>
								<td>${prod_list.quantity}</td>
								<td>${prod_list.UOM}</td>
								<td>${prod_list.HSNCode}</td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.rate}" /></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.discount}" /></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.freight}" /></td>
								<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.others}" /></td>
								<c:if test="${prod_list.is_gst == 1}">
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.CGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.SGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.IGST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.state_com_tax}" />	</td>
								</c:if>
								<c:if test="${prod_list.is_gst == 2}">
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.VAT}" />	</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.VATCST}" /></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.excise}" />	</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="0.0" /> </td>
								</c:if>
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
					        <c:forEach var = "prod_list" items = "${suppilerproductList}">	
					        	<table class="table table-bordered table-striped"  style="border: 2px solid #000;">
									<tbody>
										<tr>
											<td>Product </td>
											<td>${prod_list.product_name}</td>
										</tr>
										<tr>
											<td>Quantity</td>
											<td>${prod_list.quantity}</td>
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
											<td>CGST/VAT</td>
											<c:if test="${prod_list.is_gst == 1}">
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.CGST}" /></td>
											</c:if>
											<c:if test="${prod_list.is_gst == 2}">
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.VAT}" />	</td>
											</c:if>
										</tr>
										<tr>
											<td>SGST/CST</td>
											<c:if test="${prod_list.is_gst == 1}">
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.SGST}" /></td>
											</c:if>
											<c:if test="${prod_list.is_gst == 2}">
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.VATCST}" /></td>
											</c:if>
										</tr>
										<tr>
											<td>IGST/EXCISE</td>
											<c:if test="${prod_list.is_gst == 1}">
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.IGST}" /></td>
											</c:if>
											<c:if test="${prod_list.is_gst == 2}">
												<td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prod_list.excise}" />	</td>
											</c:if>
										</tr>
										<tr>
											<td>CESS</td>
											<c:if test="${prod_list.is_gst == 1}">
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="${prod_list.state_com_tax}" />	</td>
											</c:if>
											<c:if test="${prod_list.is_gst == 2}">
												<td><fmt:formatNumber type="number" minFractionDigits="2"	maxFractionDigits="2" value="0.0" /> </td>
											</c:if>
										</tr>
									</tbody>
								</table>
							</c:forEach>	 
				        </div>
				      </div>
				    </div>
				</div>
			
				
				
				<%--  <div class="col-md-12"  style = "text-align: center; margin:15px;">
					<button class="fassetBtn" type="button" onclick = "DownloadPdf(${entry.purchase_id})">  Download As Pdf </button>
				 </div> --%>
			</div>
			
			<!--UI view started for Account Voucher  -->
			
				<%-- <div class="tab-pane" id="profile">
			<div class="row">
				<div class="col-md-12">
					<p class="col-md-6 col-xs-6 text-left" style="padding-left:0px">			
					<b>Purchase Voucher No: ${entry.voucher_no}</b>
					</p>
					<p class="col-md-6 col-xs-6 text-right" style="padding-left:0px">			
					<b>Date: ${supplier_bill_date}</b>
					</p>
				</div>
				<div class="col-md-12">
					<p><b>Party A/c Name: ${entry.supplier.company_name}</b></p>
					<p><b>Current Balance: <fmt:formatNumber type="number" value="${closingbalance}" /> Cr
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
									<td>${entry.supplier.company_name}</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" /> Cr
									</td>
								</tr>
								<tr>
									<td>${entry.subledger.subledger_name}										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" /> Dr
									</td>
								</tr>
								<c:if test="${entry.tds_amount!=0 && entry.tds_amount!=null}">								
								<tr>
									<td>TDS</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.tds_amount}" /> Cr</td>
								</tr>
								</c:if>
								<c:if test="${entry.cgst!=0}">
										<tr>
											<td>CGST</td>
											<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.cgst}" /> Dr</td> 
										</tr>				
								</c:if>
								<c:if test="${entry.sgst!=0}">
										<tr>
											<td>SGST</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.sgst}" /> Dr</td>
											
										</tr>				
								</c:if>
								<c:if test="${entry.igst!=0}">
										<tr>
											<td>IGST</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.igst}" /> Dr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.state_compansation_tax!=0}">
										<tr>
											<td>CESS										
												</td>
												<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.state_compansation_tax}" /> Dr</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vat!=0}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vat}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vatcst!=0}">
										<tr>
											<td>CST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vatcst}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_excise!=0}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_excise}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being purchase made from ${entry.supplier.company_name} against ${entry.supplier_bill_no} dated ${supplier_bill_date}.</td>
									<td></td>
									<td>
									</td>
								</tr>
					</tbody>
				</table>
				 <div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button"  onclick ="purchaseAccountingVoucherPdf('#Hiddentable', {type: 'pdf',
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
		 </div>
			</div> --%>
			
			<!--UI view ended for Account Voucher  -->
			
			<!--PDF view started for Account Voucher  -->
			
			<%-- <div class="table-scroll" style="display:none;" id="tableDiv">
			
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">Purchase</td>
						
					</tr>
			
					<tr>
							<td>Date: ${supplier_bill_date}</td>
					</tr>
			
					<tr>
						<td>Purchase Voucher No: ${entry.voucher_no}</td>
						
					</tr>
					
					<tr>
						 <td>Party A/c Name: ${entry.supplier.company_name}</td>
					</tr>
					
					<tr>
						<td>Current Balance: <fmt:formatNumber type="number" value="${closingbalance}" /> Cr</td>
					</tr>
					
					
					<tr id="row">
					
						<th style="width: 70%">Particulars</th>
							<th>Rate</th>
							<th>Amount</th>
					
					</tr>
				<tbody>
				    
				      <tr>
									<td>${entry.supplier.company_name}</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" /> Cr
									</td>
								</tr>
								<tr>
									<td>${entry.subledger.subledger_name}										
										</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" /> Dr
									</td>
								</tr>
								<c:if test="${entry.tds_amount!=0 && entry.tds_amount!=null}">								
								<tr>
									<td>TDS</td>
									<td></td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.tds_amount}" /> Cr</td>
								</tr>
								</c:if>
								<c:if test="${entry.cgst!=0}">
										<tr>
											<td>CGST</td>
											<td></td>												
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.cgst}" /> Dr</td> 
										</tr>				
								</c:if>
								<c:if test="${entry.sgst!=0}">
										<tr>
											<td>SGST</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.sgst}" /> Dr</td>
											
										</tr>				
								</c:if>
								<c:if test="${entry.igst!=0}">
										<tr>
											<td>IGST</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.igst}" /> Dr</td>
										</tr>				
								</c:if>
								<c:if test="${entry.state_compansation_tax!=0}">
										<tr>
											<td>CESS										
												</td>
												<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.state_compansation_tax}" /> Dr</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vat!=0}">
										<tr>
											<td>VAT										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vat}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_vatcst!=0}">
										<tr>
											<td>CST										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_vatcst}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<c:if test="${entry.total_excise!=0}">
										<tr>
											<td>EXCISE										
												</td>
											<td></td>
											<td><fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.total_excise}" /> Dr
									</td>
											
										</tr>	
								</c:if>
								<tr style='border:1px solid #bbb'>
									<td>Narration: <br /> Being purchase made from ${entry.supplier.company_name} against ${entry.supplier_bill_no} dated ${supplier_bill_date}.</td>
									<td></td>
									<td>
									</td>
								</tr>
					
				</tbody>
				
			</table>
		</div>  --%>
			
			
			<!--PDF view ended for Account Voucher  -->
		</div>
			
	</form:form>
		
		 <div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "back()">	<spring:message code="btn_back" /></button>
		</div>
</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	

	 function purchaseAccountingVoucherPdf(selector, params) {
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
		window.location.assign('<c:url value = "editPurchaseEntry"/>?id='+id);	
	}
	
	function back(){
		
		<c:if test="${flag==true}">  
		window.location.assign('<c:url value = "purchaseEntryList"/>');
		</c:if>
		<c:if test="${flag==false}">  
		window.location.assign('<c:url value = "purchaseEntryFailure"/>');
		</c:if>
	}
	
	function DownloadPdf(id){
		window.location.assign('<c:url value = "downloadPurchase"/>?id='+id);	
	}
	
	function DownloadAccountingPdf(id){
		window.location.assign('<c:url value = "downloadPurchaseAccountingVoucher"/>?id='+id);	
	}
</script>
<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>