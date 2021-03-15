<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>GST Report 2</h3>
	<a href="homePage">Home</a> » <a href="gstReport2Input">GST Report
		2</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="borderForm">

		<div class="col-lg-12">
			<ul class="nav nav-tabs navtab-bg">
				<li class="active"><a href="#b2b" data-toggle="tab"
					aria-expanded="true"> <span class="visible-xs"><i
							class="fa fa-home"></i></span> <span class="hidden-xs">B2B</span>
				</a></li>
				<li class=""><a href="#b2cl" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">B2CL</span>
				</a></li>
				<li class=""><a href="#b2cs" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">B2CS</span>
				</a></li>
				<li class=""><a href="#cdnr" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">CDNR</span>
				</a></li>
				<li class=""><a href="#cdnur" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">CDNUR</span>
				</a></li>
				<li class=""><a href="#exp" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">EXP</span>
				</a></li>
				<li class=""><a href="#at" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">AT</span>
				</a></li>
				<li class=""><a href="#atadj" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">ATADJ</span>
				</a></li>
				<li class=""><a href="#exemp" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">EXEMP</span>
				</a></li>
				<li class=""><a href="#hsn" data-toggle="tab"
					aria-expanded="false"> <span class="visible-xs"><i
							class="fa fa-user"></i></span> <span class="hidden-xs">HSN</span>
				</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active table-scroll" id="b2b">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="gst no" data-filter-control="input"
									data-sortable="true">GSTIN/UIN of Recipient</th>
								<th data-field="invoice number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="invvoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="invoice value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="supply place" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="rcm" data-filter-control="input"
									data-sortable="true">Reverse Charge</th>
								<th data-field="type" data-filter-control="input"
									data-sortable="true">Invoice Type</th>
								<th data-field="ecom-gst" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
								<th data-field="rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>

						<tbody>

							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="prchaseEntry" items="${getB2BList}">
							
							 <c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>${prchaseEntry.supplier.gst_no}</td>
									<td>${prchaseEntry.voucher_no}</td>
									<td>${prchaseEntry.supplier_bill_date}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${prchaseEntry.round_off}" /></td>
									<td>${prchaseEntry.supplier.state.state_name}</td>
									<td>${prchaseEntry.supplier.reverse_mecha}</td>
									<td>--</td>
									<td>--</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+prchaseEntry.cgst + prchaseEntry.sgst + prchaseEntry.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" /></td>
									<td><c:set var="Total_tax" value="${Total_tax +prchaseEntry.transaction_value}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.transaction_value}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +prchaseEntry.state_compansation_tax}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.state_compansation_tax}" /></td>
								</tr>
								
							</c:forEach>
						</tbody>

						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>

								<td></td>
								<td></td>
								<td></td>
								<td></td>

								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

							</tr>
						</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="b2cl">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Invoice Number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="Invoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="Invoice Value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="E-Commerce GSTIN" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
							</tr>
						</thead>
						<tbody>

							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="prchaseEntry" items="${getB2CLList}">
							
							<c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>${prchaseEntry.voucher_no}</td>
									<td>${prchaseEntry.supplier_bill_date}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${prchaseEntry.round_off}" /></td>
									<td>${prchaseEntry.supplier.state.state_name}</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+prchaseEntry.cgst + prchaseEntry.sgst + prchaseEntry.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" /></td>
									<td><c:set var="Total_tax" value="${Total_tax +prchaseEntry.transaction_value}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.transaction_value}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +prchaseEntry.state_compansation_tax}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.state_compansation_tax}" /></td>
									<td>--</td>
								</tr>
								
							</c:forEach>
						</tbody>
						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="b2cs">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Type" data-filter-control="input"
									data-sortable="true">Type</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="E-Commerce GSTIN" data-filter-control="input"
									data-sortable="true">E-Commerce GSTIN</th>
							</tr>
						</thead>
						<tbody>

							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="prchaseEntry" items="${getB2CSList}">
							
							<c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>--</td>
									<td>${prchaseEntry.supplier.state.state_name}</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+prchaseEntry.cgst + prchaseEntry.sgst + prchaseEntry.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" /></td>
									<td><c:set var="Total_tax" value="${Total_tax +prchaseEntry.transaction_value}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.transaction_value}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +prchaseEntry.state_compansation_tax}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.state_compansation_tax}" /></td>
									<td>--</td>
								</tr>
								
							</c:forEach>
						</tbody>
						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="cdnr">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Recipient" data-filter-control="input"
									data-sortable="true">GSTIN/UIN of Recipient</th>
								<th data-field="payment Number" data-filter-control="input"
									data-sortable="true">Invoice payment Number</th>
								<th data-field="payment date" data-filter-control="input"
									data-sortable="true">Invoice payment date</th>
								<th data-field="Voucher Number" data-filter-control="input"
									data-sortable="true">Refund Voucher Number</th>
								<th data-field="Voucher date" data-filter-control="input"
									data-sortable="true">Refund Voucher date</th>
								<th data-field="Document Type" data-filter-control="input"
									data-sortable="true">Document Type</th>
								<th data-field="document" data-filter-control="input"
									data-sortable="true">Reason For Issuing document</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Voucher Value" data-filter-control="input"
									data-sortable="true">Refund Voucher Value</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="Pre GST" data-filter-control="input"
									data-sortable="true">Pre GST</th>
							</tr>
						</thead>
						<tbody>

							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="debitNote" items="${cdnrList}">
							<c:if test="${debitNote.supplier != null}">
							<c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>${debitNote.supplier.gst_no}</td>
									<td>${debitNote.purchase_bill_id.voucher_no}</td>
									<td>${debitNote.purchase_bill_id.supplier_bill_date}</td>
									<td>${debitNote.voucher_no}</td>
									<td>${debitNote.date}</td>
									<td>--</td>
									<td>--</td>
									<td>${debitNote.supplier.state.state_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${debitNote.round_off}" /></td>

									<td>
										<c:set var="total_count_rate" value="${total_count_rate+debitNote.CGST_head+debitNote.SGST_head+debitNote.IGST_head}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
                                     </td>
									<td><c:set var="Total_tax" value="${Total_tax +debitNote.transaction_value_head}" />	
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.transaction_value_head}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +debitNote.SCT_head}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SCT_head}" /></td>
									<td>--</td>
								</tr>
							</c:if>
							</c:forEach>
						</tbody>

						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="cdnur">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="UR Type" data-filter-control="input"
									data-sortable="true">UR Type</th>
								<th data-field="Note/Refund Voucher Number"
									data-filter-control="input" data-sortable="true">Refund
									Voucher Number</th>
								<th data-field="Note/Refund Voucher date"
									data-filter-control="input" data-sortable="true">Refund
									Voucher date</th>
								<th data-field="Document Type" data-filter-control="input"
									data-sortable="true">Document Type</th>
								<th data-field="Invoice/Advance payment date"
									data-filter-control="input" data-sortable="true">Advance
									payment date</th>
								<th data-field="Reason For Issuing document"
									data-filter-control="input" data-sortable="true">Reason
									For Issuing document</th>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Note/Refund Voucher Value"
									data-filter-control="input" data-sortable="true">Refund
									Voucher Value</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
								<th data-field="Pre GST" data-filter-control="input"
									data-sortable="true">Pre GST</th>
							</tr>
						</thead>
						<tbody>

							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="debitNote" items="${cdnurList}">
							<c:if test="${debitNote.supplier != null}">
							<c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>--</td>
									<td>${debitNote.voucher_no}</td>
									<td>${debitNote.date}</td>
									<td>--</td>
									<td>--</td>
									<td>--</td>
									<td>${debitNote.supplier.state.state_name}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${debitNote.round_off}" /></td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+debitNote.CGST_head+debitNote.SGST_head+debitNote.IGST_head}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
                                     </td>
									<td><c:set var="Total_tax" value="${Total_tax +debitNote.transaction_value_head}" />	
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.transaction_value_head}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +debitNote.SCT_head}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SCT_head}" /></td>
									<td>--</td>
								</tr>
							</c:if>
							</c:forEach>
						</tbody>
						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

								<td></td>
							</tr>
						</tfoot>

					</table>
				</div>
				<div class="tab-pane table-scroll" id="exp">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Export Type" data-filter-control="input"
									data-sortable="true">Export Type</th>
								<th data-field="Invoice Number" data-filter-control="input"
									data-sortable="true">Invoice Number</th>
								<th data-field="Invoice date" data-filter-control="input"
									data-sortable="true">Invoice date</th>
								<th data-field="Invoice Value" data-filter-control="input"
									data-sortable="true">Invoice Value</th>
								<th data-field="Port Code" data-filter-control="input"
									data-sortable="true">Port Code</th>
								<th data-field="Shipping Bill Number"
									data-filter-control="input" data-sortable="true">Shipping
									Bill Number</th>
								<th data-field="Shipping Bill Date" data-filter-control="input"
									data-sortable="true">Shipping Bill Date</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="Total_rate" value="0" />
							<c:set var="Total_tax" value="0" />
							<c:set var="Total_cess" value="0" />

							<c:forEach var="prchaseEntry" items="${getExpList}">
							
							 <c:set var="total_count_rate" value="0"/>	
								<tr>
									<td>--</td>
									<td>${prchaseEntry.voucher_no}</td>
									<td>${prchaseEntry.supplier_bill_date}</td>
									<td><fmt:formatNumber type="number" minFractionDigits="2"
											maxFractionDigits="2" value="${prchaseEntry.round_off}" /></td>
									<td>--</td>
									<td>${prchaseEntry.shipping_bill_no}</td>
									<td>${prchaseEntry.shipping_bill_date}</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+prchaseEntry.cgst + prchaseEntry.sgst + prchaseEntry.igst}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" /></td>
									<td><c:set var="Total_tax" value="${Total_tax +prchaseEntry.transaction_value}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${prchaseEntry.transaction_value}" /></td>
								</tr>
									
							</c:forEach>
						</tbody>

						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		    <td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_tax}" /></b></td> 	

								<td></td>
							</tr>
						</tfoot>
					</table>

				</div>
				<div class="tab-pane table-scroll" id="at">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Advance Received" data-filter-control="input"
									data-sortable="true">Gross Advance Received</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="Total_rate" value="0" />
							<c:set var="Total_gross" value="0" />
							<c:set var="Total_cess" value="0" />
							<c:forEach var="payment" items="${getATList}">
							 <c:set var="total_count_rate" value="0"/>	
							<c:if test="${payment.supplier != null}"> <!-- // for restrict entries against subledger -->
								<tr>
									<td>${payment.supplier.state.state_name}</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+payment.CGST_head + payment.SGST_head + payment.IGST_head}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
									</td>
									<td><c:set var="Total_gross" value="${Total_gross +payment.amount}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +payment.SCT_head}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.SCT_head}" /></td>
								</tr>
							</c:if>
								
							</c:forEach>
						</tbody>

						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_gross}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>
			 	
							</tr>
						</tfoot>

					</table>
				</div>
				<div class="tab-pane table-scroll" id="atadj">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Place Of Supply" data-filter-control="input"
									data-sortable="true">Place Of Supply</th>
								<th data-field="Rate" data-filter-control="input"
									data-sortable="true">Rate</th>
								<th data-field="Advance Received" data-filter-control="input"
									data-sortable="true">Gross Advance Received</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="Total_rate" value="0" />
							<c:set var="Total_gross" value="0" />
							<c:set var="Total_cess" value="0" />
							<c:forEach var="payment" items="${getATAdjList}">
							 <c:set var="total_count_rate" value="0"/>	
							<c:if test="${payment.supplier != null}"> <!-- // for restrict entries against subledger -->
								<tr>
									<td>${payment.supplier.state.state_name}</td>
									<td>
										<c:set var="total_count_rate" value="${total_count_rate+payment.CGST_head + payment.SGST_head + payment.IGST_head}" />
										<c:set var="Total_rate" value="${Total_rate +total_count_rate}" />	 
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${total_count_rate}" />
									</td>
									<td><c:set var="Total_gross" value="${Total_gross +payment.amount}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" /></td>
									<td><c:set var="Total_cess" value="${Total_cess +payment.SCT_head}" />
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.SCT_head}" /></td>
								</tr>
							</c:if>
								
							</c:forEach>
						</tbody>
						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_rate}" /></b></td>
			 		
			 		<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_gross}" /></b></td>
			 		
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b></td>

								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div class="tab-pane table-scroll" id="exemp">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="Description" data-filter-control="input"
									data-sortable="true">Description</th>
								<th data-field="Nil Rated Purchases" data-filter-control="input"
									data-sortable="true">Nil Rated Purchases</th>
								<th data-field="Exempted" data-filter-control="input"
									data-sortable="true">Exempted (other than nil rated/non GST supply )</th>
								<th data-field="Non-GST Purchases" data-filter-control="input"
									data-sortable="true">Non-GST Purchases</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot style='background-color: #eee'>							
						</tfoot>
					</table>
				</div>
				
				<div class="tab-pane table-scroll" id="hsn">
					<table id="table" data-toggle="table" data-search="false"
						data-escape="false" data-filter-control="true"
						data-show-export="false" data-click-to-select="true"
						data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
						class="table gst-table">
						<thead>
							<tr>
								<th data-field="HSN" data-filter-control="input"
									data-sortable="true">HSN</th>
								<th data-field="Description" data-filter-control="input"
									data-sortable="true">Description</th>
								<th data-field="UQC" data-filter-control="input"
									data-sortable="true">UQC</th>
								<th data-field="Total Quantity" data-filter-control="input"
									data-sortable="true">Total Quantity</th>
								<th data-field="Total Value" data-filter-control="input"
									data-sortable="true">Total Value</th>
								<th data-field="Taxable Value" data-filter-control="input"
									data-sortable="true">Taxable Value</th>
								<th data-field="Integrated Tax Amount" data-filter-control="input"
									data-sortable="true">Integrated Tax Amount</th>
								<th data-field="Central Tax Amount" data-filter-control="input"
									data-sortable="true">Central Tax Amount</th>
								<th data-field="State/UT Tax Amount" data-filter-control="input"
									data-sortable="true">State/UT Tax Amount</th>
								<th data-field="Cess Amount" data-filter-control="input"
									data-sortable="true">Cess Amount</th>
							</tr>
						</thead>
						<tbody>
							
						
							<c:set var="Total_value" value="0" />
							<c:set var="Total_taxable" value="0" />
							<c:set var="Total_igst" value="0" />
							<c:set var="Total_sgst" value="0" />
							<c:set var="Total_cgst" value="0" />
							<c:set var="Total_cess" value="0" />
							<c:set var="TotalQuantity" value="0" />
							
							<c:forEach var="hsnReport" items="${hsnReportList}">
								<tr>
									<td>${hsnReport.hsnCode}</td>
									<td></td>
									<td>${hsnReport.uqc}</td>
									<td>
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.totalQuantity}" />
									<c:set var="TotalQuantity" value="${TotalQuantity +hsnReport.totalQuantity}" />
									</td>
									<td>
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.totalValue}" />
										<c:set var="Total_value" value="${Total_value +hsnReport.totalValue}" />
									</td>
									<td>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.taxableValue}" />
										<c:set var="Total_taxable" value="${Total_taxable +hsnReport.taxableValue}" />
									</td>
									<td>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.igstAmount}" />
										<c:set var="Total_igst" value="${Total_igst +hsnReport.igstAmount}" />
									</td>
									<td>
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.cgstAmount}" />
										<c:set var="Total_cgst" value="${Total_cgst +hsnReport.cgstAmount}" />
									</td>
									<td>
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.sgstAmount}" />
										<c:set var="Total_sgst" value="${Total_sgst +hsnReport.sgstAmount}" />
									</td>
									<td>
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${hsnReport.cessAmount}" />
										<c:set var="Total_cess" value="${Total_cess +hsnReport.cessAmount}" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot style='background-color: #eee'>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td>
								<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalQuantity}" /></b>
								</td>
								<td>
									
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_value}" /></b>
								</td>
								<td>
								
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_taxable}" /></b>
								</td>
								<td>
								
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_igst}" /></b>
								</td>
								<td>
								
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cgst}" /></b>
								</td>
								<td>
									
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_sgst}" /></b>
								</td>
								<td>
									
									<b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Total_cess}" /></b>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>

		<div class="row text-center-btn">
		<c:if test="${role!=7}">
			<button class="fassetBtn" type="button" onclick="pdf();">
				Download as Excel
			</button>
			</c:if>
			<button class="fassetBtn" type="button" onclick="back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function back(){
		window.location.assign('<c:url value = "gstReport2Input"/>');	
	}
	function pdf(){
		window.location.assign('<c:url value = "downloadGSTReport2Excel"/>');
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>