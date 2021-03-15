<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport5.js"
	var="tableexport" />
<script type="text/javascript"
	src="http://code.jquery.com/jquery-latest.min.js"></script>
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
			tableName : 'Countries',
			worksheetName : 'Countries by population'
		};
		$.extend(true, options, params);
		$(selector).tableExport(options);
		$("#tableDiv").css("display", "none");
	}
</script>

<div class="breadcrumb">
	<h3>EXCESS GST EXCEPTION REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport5">EXCESS GST EXCEPTION REPORT</a> 

</div>
<div class="col-md-12">
<c:set var="TotalCgst" value="0"/>	
	<c:set var="Totalsgst" value="0"/>	
	<c:set var="Totaligst" value="0"/>	
	<c:set var="Totallabourcharges" value="0"/>
	<c:set var="Totalfreight" value="0"/>
	<c:set var="TotalOthers" value="0"/>		
	<c:set var="BasicAmount" value="0"/>
	<c:set var="Taxablevalue" value="0"/>		
    <c:set var="InvoiceValue" value="0"/> 
    
    
    <c:set var="TotalCgst1" value="0"/>	
	<c:set var="Totalsgst1" value="0"/>	
	<c:set var="Totaligst1" value="0"/>	
	<c:set var="Totalsct1" value="0"/>	
	<c:set var="Totalvat1" value="0"/>	
	<c:set var="Totalvatcst1" value="0"/>	
	<c:set var="Totalexcise1" value="0"/>
	<c:set var="Totallabourcharges1" value="0"/>
	<c:set var="Totalfreight1" value="0"/>
	<c:set var="TotalOthers1" value="0"/>		
	<c:set var="BasicAmount1" value="0"/>
	<c:set var="Taxablevalue1" value="0"/>		
    <c:set var="InvoiceValue1" value="0"/> 
    
    
      <c:set var="TotalCgst2" value="0"/>	
	<c:set var="Totalsgst2" value="0"/>	
	<c:set var="Totaligst2" value="0"/>	
	<c:set var="Totallabourcharges2" value="0"/>
	<c:set var="Totalfreight2" value="0"/>
	<c:set var="TotalOthers2" value="0"/>		
	<c:set var="BasicAmount2" value="0"/>
	<c:set var="Taxablevalue2" value="0"/>		
    <c:set var="InvoiceValue2" value="0"/>
    
    
     <c:set var="TotalCgst3" value="0"/>	
	<c:set var="Totalsgst3" value="0"/>	
	<c:set var="Totaligst3" value="0"/>	
	<c:set var="Totallabourcharges3" value="0"/>
	<c:set var="Totalfreight3" value="0"/>
	<c:set var="TotalOthers3" value="0"/>		
	<c:set var="BasicAmount3" value="0"/>
	<c:set var="Taxablevalue3" value="0"/>		
    <c:set var="InvoiceValue3" value="0"/>


	<c:forEach var="entry" items="${purchaseEntryList}">
	<c:set var="TotalCgst" value="${TotalCgst + entry.cgst}" />
	<c:set var="Totalsgst" value="${Totalsgst + entry.sgst}" />
	<c:set var="Totaligst" value="${Totaligst + entry.igst}" />
	
	 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }">
	     <c:forEach var="info" items="${entry.productinfoList}">
		<c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.rate*info.quantity*info.discount)/100)}"/>
	<c:set var="freight" value="${info.freight}"/>
	<c:set var="others" value="${info.others}"/>		
	<c:set var="labourcharges" value="${info.labour_charges}"/>
	<c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}"/>		
    <c:set var="invoiceValue" value="${taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}"/>
    
		 <c:set var="BasicAmount" value="${BasicAmount + basicAmount}" />
		  <c:set var="Totallabourcharges" value="${Totallabourcharges + labourcharges}" />
		  <c:set var="Totalfreight" value="${Totalfreight + freight}" />
		   <c:set var="TotalOthers" value="${TotalOthers + others}" />
		   <c:set var="Taxablevalue" value="${Taxablevalue + taxablevalue}"/>
		    <c:set var="InvoiceValue" value="${InvoiceValue + invoiceValue}"/>
		 </c:forEach>
	  </c:if>

	</c:forEach>
	
	<c:forEach var="entry" items="${salesList}">
	<c:set var="TotalCgst1" value="${TotalCgst1 + entry.cgst}" />
	<c:set var="Totalsgst1" value="${Totalsgst1 + entry.sgst}" />
	<c:set var="Totaligst1" value="${Totaligst1 + entry.igst}" />
	
	
	  <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }">
	  
	     <c:forEach var="info" items="${entry.productinfoList}">
	     <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.rate*info.quantity*info.discount)/100)}"/>
	<c:set var="freight" value="${info.freight}"/>
	<c:set var="others" value="${info.others}"/>		
	<c:set var="labourcharges" value="${info.labour_charges}"/>
	<c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}"/>		
    <c:set var="invoiceValue" value="${taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}"/>
    
		 <c:set var="BasicAmount1" value="${BasicAmount1 + basicAmount}" />
		  <c:set var="Totallabourcharges1" value="${Totallabourcharges1 + labourcharges}" />
		  <c:set var="Totalfreight1" value="${Totalfreight1 + freight}" />
		   <c:set var="TotalOthers1" value="${TotalOthers1 + others}" />
		   <c:set var="Taxablevalue1" value="${Taxablevalue1 + taxablevalue}"/>
		    <c:set var="InvoiceValue1" value="${InvoiceValue1 + invoiceValue}"/>
		 </c:forEach>
	  </c:if>
		
	</c:forEach>
	
		<c:forEach var="entry" items="${receciptList}">
	<c:set var="TotalCgst2" value="${TotalCgst2 + entry.cgst}" />
	<c:set var="Totalsgst2" value="${Totalsgst2 + entry.sgst}" />
	<c:set var="Totaligst2" value="${Totaligst2 + entry.igst}" />
	  <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }">
	  
	     <c:forEach var="info" items="${entry.productinfoList}">
	     <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.rate*info.quantity*info.discount)/100)}"/>
	<c:set var="freight" value="${info.freight}"/>
	<c:set var="others" value="${info.others}"/>		
	<c:set var="labourcharges" value="${info.labour_charges}"/>
	<c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}"/>		
    <c:set var="invoiceValue" value="${taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}"/>
    
		 <c:set var="BasicAmount2" value="${BasicAmount2 + basicAmount}" />
		  <c:set var="Totallabourcharges2" value="${Totallabourcharges2 + labourcharges}" />
		  <c:set var="Totalfreight2" value="${Totalfreight2 + freight}" />
		   <c:set var="TotalOthers2" value="${TotalOthers2 + others}" />
		   <c:set var="Taxablevalue2" value="${Taxablevalue2 + taxablevalue}"/>
		    <c:set var="InvoiceValue2" value="${InvoiceValue2 + invoiceValue}"/>
		 </c:forEach>
	  </c:if>
		
	</c:forEach>
	
	
	<c:forEach var="entry" items="${paymentList}">
	<c:set var="TotalCgst3" value="${TotalCgst3 + entry.CGST_head}" />
	<c:set var="Totalsgst3" value="${Totalsgst3 + entry.SGST_head}" />
	<c:set var="Totaligst3" value="${Totaligst3 + entry.IGST_head}" />
	  <c:if test="${entry.paymentDetails!=null && entry.paymentDetails.size()>0 }">
	  
	     <c:forEach var="info" items="${entry.paymentDetails}">
	     <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.rate*info.quantity*info.discount)/100)}"/>
	<c:set var="freight" value="${info.frieght}"/>
	<c:set var="others" value="${info.others}"/>		
	<c:set var="labourcharges" value="${info.labour_charges}"/>
	<c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}"/>		
    <c:set var="invoiceValue" value="${taxablevalue+info.cgst+info.sgst+info.igst}"/>
    
		 <c:set var="BasicAmount3" value="${BasicAmount3 + basicAmount}" />
		  <c:set var="Totallabourcharges3" value="${Totallabourcharges3 + labourcharges}" />
		  <c:set var="Totalfreight3" value="${Totalfreight3 + freight}" />
		   <c:set var="TotalOthers3" value="${TotalOthers3 + others}" />
		   <c:set var="Taxablevalue3" value="${Taxablevalue3 + taxablevalue}"/>
		    <c:set var="InvoiceValue3" value="${InvoiceValue3 + invoiceValue}"/>
		 </c:forEach>
	  </c:if>
		
	</c:forEach>
	
	
<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
				<tr style="text-align: center;">
					<td></td>
					<td></td>
				<td><b>EXCESS GST EXCEPTION REPORT</b></td>
				</tr>
				<tr></tr>
				<tr>
					<td colspan='7'>Company Name: ${company.company_name}</td>
				</tr>
				<tr>
					<td colspan='7'>Address: ${company.permenant_address}</td>
				</tr>
				<tr>
					<td colspan='7'><fmt:parseDate value="${from_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="from_date" type="date"
							pattern="dd-MM-yyyy" /> <fmt:parseDate value="${to_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
							value="${parsedDate}" var="to_date" type="date"
							pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
				</tr>
				<tr>
					<td colspan='5'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
					</td>
				</tr>
			</table>
			<!-- Date -->
										<table>
				<tr>
				<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="customer" data-filter-control="input"
							data-sortable="true">Customer Name/Supplier Name</th>
						<th data-field="voucher" data-filter-control="input"
							data-sortable="true">Voucher No</th>
					    <th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="Inv no" data-filter-control="input"
							data-sortable="true">Inv no</th>

						<th data-field="labour" data-filter-control="input"
							data-sortable="true">Labour Charges</th>
						<th data-field="freight" data-filter-control="input"
							data-sortable="true">Freight</th>

						<th data-field="others" data-filter-control="input"
							data-sortable="true">Others</th>
					
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true">CGST</th>
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true">SGST</th>
						<th data-field="igst" data-filter-control="input"
							data-sortable="true">IGST</th>

					    <th data-field="Total Value(Invoice value) "
						data-filter-control="input" data-sortable="true">Total
						Value(Invoice value)</th>
			</tr>
			
                  <tbody>


					  <c:forEach var="entry" items="${exceptionReport5List}">
						<tr>
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
							</td>
						<td style="text-align: left;">
						${entry.cusSupplierName}
						</td>
							<td style="text-align: left;">${entry.voucherNumber}</td>
							<td style="text-align: left;">${entry.voucherType}</td>
							<td style="text-align: left;">${entry.invocieNumber}</td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.labourcharges}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.freight}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.others}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.invoiceValue}" /></td>
						</tr>
				
					</c:forEach>

					
				
					
					
							<%-- <tr>
					<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
			   			<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totallabourcharges+Totallabourcharges1+Totallabourcharges2+Totallabourcharges3}" /></b></td>
			
                    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalfreight+Totalfreight1+Totalfreight2+Totalfreight3}" /></b></td>
							<td class="tright" ><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalOthers+TotalOthers1+TotalOthers2+TotalOthers3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalCgst+TotalCgst1+TotalCgst2+TotalCgst3}" /></b></td>
			          		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalsgst+Totalsgst1+Totalsgst2+Totalsgst3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totaligst+Totaligst1+Totaligst2+Totaligst3}" /></b></td>
				    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${InvoiceValue+InvoiceValue1+InvoiceValue2+InvoiceValue3}" /></b></td>
		
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
					<td style="color: blue; margin-left: 50px;">EXCESS GST EXCEPTION REPORT
					</td>
				</tr>

				<tr>
					<td align="center">Company Name:</td>
					<td></td>
					<td align="center">${company.company_name}</td>
				</tr>
				<tr>
					<td align="center">Address:</td>
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
					<td>${from_date}To ${to_date}</td>
				</tr>
				<tr>

					<td colspan='3'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
					</td>
				</tr>
				</table>
				<table>
				<tr>
				<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="customer" data-filter-control="input"
							data-sortable="true">Customer Name/Supplier Name</th>
						<th data-field="voucher" data-filter-control="input"
							data-sortable="true">Voucher No</th>
					    <th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>	
						<th data-field="Inv no" data-filter-control="input"
							data-sortable="true">Inv no</th>

						<th data-field="labour" data-filter-control="input"
							data-sortable="true" class="tright">Labour Charges</th>
						<th data-field="freight" data-filter-control="input"
							data-sortable="true" class="tright">Freight</th>

						<th data-field="others" data-filter-control="input"
							data-sortable="true" class="tright">Others</th>
					
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" class="tright">CGST</th>
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" class="tright">SGST</th>
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" class="tright">IGST</th>

					    <th  class='tright' data-field="Total Value(Invoice value) "
						data-filter-control="input" data-sortable="true" class="tright">Total
						Value(Invoice value)</th>
			</tr>
		</c:if>	
            <tbody>
		          <c:forEach var="entry" items="${exceptionReport5List}">
						<tr>
						
						
								<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/supplierAndCustomerHavingGSTApplicbleAsNOAndTaxExceedingZeroHeader.jsp"%>
								</c:if>
						
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />

						
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
							</td>
						<td style="text-align: left;">
						${entry.cusSupplierName}
						</td>
							<td style="text-align: left;">${entry.voucherNumber}</td>
							<td style="text-align: left;">${entry.voucherType}</td>
							<td style="text-align: left;">${entry.invocieNumber}</td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.labourcharges}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.freight}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.others}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.invoiceValue}" /></td>
						</tr>
				
					</c:forEach>
			</tbody>
			
			<tfoot style='background-color: #eee'>
							<tr>
						<%-- <td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
			   			<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totallabourcharges+Totallabourcharges1+Totallabourcharges2+Totallabourcharges3}" /></b></td>
			
                    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalfreight+Totalfreight1+Totalfreight2+Totalfreight3}" /></b></td>
							<td class="tright" ><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalOthers+TotalOthers1+TotalOthers2+TotalOthers3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalCgst+TotalCgst1+TotalCgst2+TotalCgst3}" /></b></td>
			          		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalsgst+Totalsgst1+Totalsgst2+Totalsgst3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totaligst+Totaligst1+Totaligst2+Totaligst3}" /></b></td>
				    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${InvoiceValue+InvoiceValue1+InvoiceValue2+InvoiceValue3}" /></b></td> --%>
		
					</tr>
				</tfoot>
			
		
		</table>	
	</div>
	
	<div class="borderForm ">
	
	
		<div class="table-scroll">
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table ">
				<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="customer" data-filter-control="input"
							data-sortable="true">Customer Name/Supplier Name</th>
						<th data-field="voucher" data-filter-control="input"
							data-sortable="true">Voucher No</th>
					    <th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>
						<th data-field="Inv no" data-filter-control="input"
							data-sortable="true">Inv no</th>

						<th data-field="labour" data-filter-control="input"
							data-sortable="true">Labour Charges</th>
						<th data-field="freight" data-filter-control="input"
							data-sortable="true">Freight</th>

						<th data-field="others" data-filter-control="input"
							data-sortable="true">Others</th>
					
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true">CGST</th>
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true">SGST</th>
						<th data-field="igst" data-filter-control="input"
							data-sortable="true">IGST</th>

					    <th class="tright" data-field="Total Value(Invoice value) "
						data-filter-control="input" data-sortable="true">Total
						Value(Invoice value)</th>

					</tr>
				</thead>
				<tbody>
					<c:forEach var="entry" items="${exceptionReport5List}">
						<tr>
							<td style="text-align: left;">
								<fmt:parseDate value="${entry.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
								${createdDate}
							</td>
						<td style="text-align: left;">
						${entry.cusSupplierName}
						</td>
							<td style="text-align: left;">${entry.voucherNumber}</td>
							<td style="text-align: left;">${entry.voucherType}</td>
							<td style="text-align: left;">${entry.invocieNumber}</td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.labourcharges}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.freight}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.others}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}" /></td>
							<td class="tright"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.invoiceValue}" /></td>
						</tr>
				
					</c:forEach>
	
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<%-- <td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
			   			<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totallabourcharges+Totallabourcharges1+Totallabourcharges2+Totallabourcharges3}" /></b></td>
			
                    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalfreight+Totalfreight1+Totalfreight2+Totalfreight3}" /></b></td>
							<td class="tright" ><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalOthers+TotalOthers1+TotalOthers2+TotalOthers3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${TotalCgst+TotalCgst1+TotalCgst2+TotalCgst3}" /></b></td>
			          		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totalsgst+Totalsgst1+Totalsgst2+Totalsgst3}" /></b></td>
							<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${Totaligst+Totaligst1+Totaligst2+Totaligst3}" /></b></td>
				    		<td class="tright"><b><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${InvoiceValue+InvoiceValue1+InvoiceValue2+InvoiceValue3}" /></b></td> --%>
		
					</tr>
				</tfoot>
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
			<button class="fassetBtn" type="button" id='btnExport3' 
			onclick = 'exportexcel("EXCESS GST EXCEPTION REPORT")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
		</div>
	</div>
</div>


<script type="text/javascript">
	$(function() {
		setTimeout(function() {
			$("#successMsg").hide();
		}, 3000);
	});
	function back() {
		window.location.assign('<c:url value = "exceptionReport5"/>');
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>