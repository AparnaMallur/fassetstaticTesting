<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg"  />
<spring:url value="/resources/images/view.png" var="viewImg"  />
<script type="text/javascript" src="${valid}" ></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin"  />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto"  />
<spring:url value="/resources/js/report_table/salesReport.js" var="tableexport"  />
 <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js" ></script>
 <script type="text/javascript" src="${jspdfmin}" ></script>
 <script type="text/javascript" src="${jspdfauto}" ></script>
 <script type="text/javascript" src="${tableexport}" ></script>
 
 
  <script type="text/javascript" >
 
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


<div class="breadcrumb" >
	<h3>Sales Entry Report</h3>
	<a href="homePage" >Home</a> ? <a href="salesReport" >Sales Entry Report</a>
</div>
<div class="row" >
	<c:if test="${successMsg != null}" >
		<div class="successMsg" id="successMsg" >
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="borderForm " >
	
	<c:set var="TotalCgst" value="0" />	
	<c:set var="Totalsgst" value="0" />	
	<c:set var="Totaligst" value="0" />	
	<c:set var="Totalsct" value="0" />	
	<c:set var="Totalvat" value="0" />	
	<c:set var="Totalvatcst" value="0" />	
	<c:set var="Totalexcise" value="0" />
	<c:set var="Totallabourcharges" value="0" />
	<c:set var="Totalfreight" value="0" />
	<c:set var="TotalOthers" value="0" />		
	<c:set var="BasicAmount" value="0" />
	<c:set var="Taxablevalue" value="0" />		
    <c:set var="InvoiceValue" value="0" />  
	
	<c:forEach var="entry" items="${salesEntryList}" >
	<c:set var="TotalCgst" value="${TotalCgst + entry.cgst}"  />
	<c:set var="Totalsgst" value="${Totalsgst + entry.sgst}"  />
	<c:set var="Totaligst" value="${Totaligst + entry.igst}"  />
	<c:set var="Totalsct" value="${Totalsct + entry.state_compansation_tax}"  />
	<c:set var="Totalvat" value="${Totalvat + entry.total_vat}"  />
	<c:set var="Totalvatcst" value="${Totalvatcst + entry.total_vatcst}"  />
	<c:set var="Totalexcise" value="${Totalexcise + entry.total_excise}"  />
	
	
	  <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	  
	     <c:forEach var="info" items="${entry.productinfoList}" >
	     <c:set var="basicAmount" value="${(info.rate*info.quantity)-(info.discount)}" />
	<c:set var="freight" value="${info.freight}" />
	<c:set var="others" value="${info.others}" />		
	<c:set var="labourcharges" value="${info.labour_charges}" />
	<c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}" />		
    <c:set var="invoiceValue" value="${taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}" />
    
		 <c:set var="BasicAmount" value="${BasicAmount + basicAmount}"  />
		  <c:set var="Totallabourcharges" value="${Totallabourcharges + labourcharges}"  />
		  <c:set var="Totalfreight" value="${Totalfreight + freight}"  />
		   <c:set var="TotalOthers" value="${TotalOthers + others}"  />
		   <c:set var="Taxablevalue" value="${Taxablevalue + taxablevalue}" />
		    <c:set var="InvoiceValue" value="${InvoiceValue + invoiceValue}" />
		 </c:forEach>
	  </c:if>
		
	</c:forEach>
	
		<c:if test="${option == 2}" >
		<!-- Excel Start -->
				<div style="display:none" id="excel_report" >
					<!-- Date -->
					<font size="11" face="verdana" >
					<table id="colmn">
						<tr style="text-align:center;"><td colspan='9'><b>Sales Entry Report</b></td></tr>	
					<tr></tr>
						<tr style="text-align:center;"><td colspan='9'>Company Name: ${company.company_name}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>Address: ${company.permenant_address}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate1" type="date"  />
	                   			 <fmt:formatDate value="${parsedDate1}" var="fromdate" type="date" pattern="dd-MM-yyyy"  />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate2" type="date"  />
	                   			 <fmt:formatDate value="${parsedDate2}" var="todate" type="date" pattern="dd-MM-yyyy"  />
						From ${fromdate} To ${todate}</td></tr>
						<tr style="text-align:center;"><td colspan='9'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}" >
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
							data-sortable="true" >Created Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true" >Voucher Number</th>	
					   <th data-field="particulars" data-filter-control="input"
							data-sortable="true" >Particulars</th>	
						<th data-field="income type" data-filter-control="input"
							data-sortable="true" >Income Type</th>
						<th data-field="basic amount" data-filter-control="input"
							data-sortable="true" >Basic Amount</th>
							<c:if test="${Totalfreight>0}" >
						  <th data-field="freight" data-filter-control="input"
							data-sortable="true" >Freight</th>
							</c:if>
						<c:if test="${Totallabourcharges>0}" >
						  <th data-field="labour" data-filter-control="input"
							data-sortable="true" >Labour Cost</th>
							</c:if>
						<c:if test="${TotalOthers>0}" >
						  <th data-field="others" data-filter-control="input"
							data-sortable="true" >Others</th>
							</c:if>
						<th data-field="taxable value" data-filter-control="input"
							data-sortable="true" >Taxable value</th>
						  <c:if test="${TotalCgst>0}" >
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" >CGST</th>
						</c:if>	
						 <c:if test="${Totalsgst>0}" >	
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" >SGST</th>
						</c:if>	
							<c:if test="${Totaligst>0}" >
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" >IGST</th>
							</c:if>	
							<c:if test="${Totalsct>0}" >
						<th data-field="sct" data-filter-control="input"
							data-sortable="true" >CESS</th>
							</c:if>	
                          <c:if test="${Totalvat>0}" >		
						<th data-field="tvat" data-filter-control="input"
							data-sortable="true" >VAT</th>
							</c:if>	
							<c:if test="${Totalvatcst>0}" >
						<th data-field="tvcst" data-filter-control="input"
							data-sortable="true" >CST</th>
							</c:if>	
							<c:if test="${Totalexcise>0}" >
						<th data-field="texcise" data-filter-control="input"
							data-sortable="true" >EXCISE</th>
							</c:if>	
						<th data-field="invoice value" data-filter-control="input"
							data-sortable="true" >Amount(Rs.)</th>
					</tr>
					
				
				</thead>
				<tbody>
					<c:forEach var="entry" items="${salesEntryList}" >
					<c:if test="${entry.sale_type !=null}" >
						<tr style="border:thin solid  !important ">
							<td style="text-align: left;" >
								<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
								${createdDate}
							</td>
							<td style="text-align: left;" >${entry.voucher_no}</td>
							<td style="text-align: left;" >
									<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
						Cash Sales
						</c:if>	
						<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
						 Card Sales 
						 <%--  <c:if test="${(entry.bank!=null)}" >
						  - ${entry.bank.bank_name} - ${entry.bank.account_no}
						  </c:if> --%>
						</c:if>	
						<c:if test="${entry.customer!=null}" >
						${entry.customer.firm_name}
						</c:if>	
						</td>
							<td style="text-align: left;" >${entry.subledger.subledger_name}</td>	
							
							
							  <c:if test="${BasicAmount>0}" >
							<td style="text-align: left;" >
							<c:set var="basicAmount" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-(info.discount)}" />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${basicAmount}"  />							
							</td>
							</c:if>
							
							 
					     <c:if test="${Totalfreight>0}" >
				          	<c:set var="freight" value="0" />
							<td style="text-align: left;" >
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="freight" value="${freight + info.freight}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${freight}"  />							
							</td>
					     </c:if>	
					     
					       <c:if test="${Totallabourcharges>0}" >
							   <c:set var="labourcharges" value="0" />
							<td style="text-align: left;" >
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="labourcharges" value="${labourcharges + info.labour_charges}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${labourcharges}"  />							
							</td>
							</c:if>
							
							 <c:if test="${TotalOthers>0}" >
								   <c:set var="other" value="0" />
							<td style="text-align: left;" >
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="other" value="${other + info.others}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${other}"  />							
							</td>
							</c:if>
							
							<c:if test="${Taxablevalue>0}">
							<td style="text-align: left;">
							     <c:set var="taxablevalue" value="0"/>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }">
	                           <c:forEach var="info" items="${entry.productinfoList}">
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-(info.discount)}"/>
	                           <c:set var="freight" value="${info.freight}"/>
	                           <c:set var="others" value="${info.others}"/>		
	                           <c:set var="labourcharges" value="${info.labour_charges}"/>
	                           <c:set var="taxablevalue" value="${taxablevalue+basicAmount+freight+labourcharges+others}"/>	
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${taxablevalue}"/>							
							</td>
							</c:if>
							
							  <c:if test="${TotalCgst>0}" > 
							<c:if test="${entry.cgst!=null && entry.cgst>0}" >
							<td style="text-align: left;" >
							  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}"/>						
							</td>
							</c:if>
							<c:if test="${entry.cgst==null || entry.cgst==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							
							 <c:if test="${Totalsgst>0}" > 
							<c:if test="${entry.sgst!=null && entry.sgst>0}" >
							<td style="text-align: left;" >
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.sgst==null || entry.sgst==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totaligst>0}" > 
							<c:if test="${entry.igst!=null && entry.igst>0}" >
							<td style="text-align: left;" >							
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.igst==null || entry.igst==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
			
						    <c:if test="${Totalsct>0}" > 
							<c:if test="${entry.state_compansation_tax!=null && entry.state_compansation_tax>0}" >
							<td style="text-align: left;" >
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.state_compansation_tax}"  />					
							</td>
							</c:if>
							<c:if test="${entry.state_compansation_tax==null || entry.state_compansation_tax==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totalvat>0}" >	
							<c:if test="${entry.total_vat!=null && entry.total_vat>0}" >
							<td style="text-align: left;" >
								 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vat}"  />					
							</td>
							</c:if>
							<c:if test="${entry.total_vat==null || entry.total_vat==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalvatcst>0}" >
							<c:if test="${entry.total_vatcst!=null && entry.total_vatcst>0}" >					
							<td style="text-align: left;" >
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vatcst}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_vatcst==null || entry.total_vatcst==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalexcise>0}" >
							<c:if test="${entry.total_excise!=null && entry.total_excise>0}" >		
							<td style="text-align: left;" >
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_excise}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_excise==null || entry.total_excise==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>
						
					     
					     	<c:if test="${InvoiceValue>0}" >
							<td style="text-align: left;" >
							     <c:set var="invoiceValue" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-(info.discount)}" />
	                           <c:set var="freight" value="${info.freight}" />
	                           <c:set var="others" value="${info.others}" />		
	                           <c:set var="labourcharges" value="${info.labour_charges}" />
	                           <c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}" />	
	                            <c:set var="invoiceValue" value="${invoiceValue+taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}" />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${invoiceValue}"  />							
							</td>
							</c:if>	
						</tr>
				 </c:if>
					</c:forEach>
				
					<tr style="border:thin solid  !important ">
					<td>Total</td>
					<td></td>
					<td ></td>
					<td></td>
				 <c:if test="${BasicAmount>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${BasicAmount}"  /></b></td>
				 </c:if>
				  <c:if test="${Totalfreight>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalfreight}"  /></b></td>
				 </c:if>
				  <c:if test="${Totallabourcharges>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totallabourcharges}"  /></b></td>
				 </c:if>
				   <c:if test="${TotalOthers>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalOthers}"  /></b></td>
				 </c:if>
				   <c:if test="${Taxablevalue>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Taxablevalue}"  /></b></td>
				 </c:if>

				  <c:if test="${TotalCgst>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalCgst}"  /></b></td>
				 </c:if>	
				 <c:if test="${Totalsgst>0}" >	
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsgst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totaligst>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totaligst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalsct>0}" >
					<td ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsct}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalvat>0}" >		
					<td ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvat}"  /></b></td>
				 </c:if>
					<c:if test="${Totalvatcst>0}" >
					<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvatcst}"  /></b></td>
					</c:if>
					<c:if test="${Totalexcise>0}" >
					<td ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalexcise}"  /></b></td>
					</c:if>
					<c:if test="${InvoiceValue>0}" >
					<td ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
					</c:if>
					</tr>
				</tbody>
			</table>
			</font>
				</div>
		<!-- Excel End -->
		<!-- Hidden Table for PDF Creation -->
	 <div class="table-scroll" style="display:none;" id="tableDiv" >
			
			
			<c:set var="rowcount" value="0" scope="page" />
			
		
		<font size="11" face="verdana" >
		
			<table id="Hiddentable" >
					<!-- for PDf heading -->
					<tr >
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;" >Sales Register</td>
						
					</tr>
			
					<tr>
						
						<td align="center" >Company Name: </td>
						<td></td>
						<td align="center" >${company.company_name}</td>
					</tr>
					<tr>
							<td align="center" >Address: </td>
							<td></td>
							<td align="center" >${company.permenant_address}</td>
					</tr>
					<tr>
						<td colspan='9'>
							<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate1" type="date"  />
                   			 <fmt:formatDate value="${parsedDate1}" var="fromdate" type="date" pattern="dd-MM-yyyy"  />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate2" type="date"  />
                   			 <fmt:formatDate value="${parsedDate2}" var="todate" type="date" pattern="dd-MM-yyyy"  /> --%>
						From:
						</td>
						<td></td>
						<td colspan='9'>${fromdate} To ${todate}</td>
					</tr>
					<tr>
					
					<td colspan='9'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}" >
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
					<tr id="row" style="border: 1px solid #ccc"" >
					   <th data-field="date" data-filter-control="input"
							data-sortable="true" >Created Date</th>
						<th align="left" style="color:blue; margin-right: 200em;" data-field="voucher number" data-filter-control="input"
							data-sortable="true" >Voucher Number123</th>	
					   <th  data-field="particulars" data-filter-control="input"
							data-sortable="true" >Particulars</th>	
						<th data-field="income type" data-filter-control="input"
							data-sortable="true" >Income Type</th>
						<th data-field="basic amount" data-filter-control="input"
							data-sortable="true" >Basic Amount</th>
							<c:if test="${Totalfreight>0}" >
						 <th data-field="freight" data-filter-control="input"
							data-sortable="true" >Freight</th>
							</c:if>
						<c:if test="${Totallabourcharges>0}" >
						  <th data-field="labour" data-filter-control="input"
							data-sortable="true" >Labour Cost</th>
							</c:if>
						<c:if test="${TotalOthers>0}" >
						  <th data-field="others" data-filter-control="input"
							data-sortable="true" class='tright'>Others</th>
							</c:if>
						<th data-field="taxable value" data-filter-control="input"
							data-sortable="true" class='tright'>Taxable value</th>
						  <c:if test="${TotalCgst>0}" >
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" class='tright'>CGST</th>
						</c:if>	
						 <c:if test="${Totalsgst>0}" >	
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" class='tright'>SGST</th>
						</c:if>	
							<c:if test="${Totaligst>0}" >
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" class='tright'>IGST</th>
							</c:if>	
							<c:if test="${Totalsct>0}" >
						<th data-field="sct" data-filter-control="input"
							data-sortable="true" >CESS</th>
							</c:if>	
                          <c:if test="${Totalvat>0}" >		
						<th data-field="tvat" data-filter-control="input"
							data-sortable="true" >VAT</th>
							</c:if>	
							<c:if test="${Totalvatcst>0}" >
						<th data-field="tvcst" data-filter-control="input"
							data-sortable="true" >CST</th>
							</c:if>	
							<c:if test="${Totalexcise>0}" >
						<th data-field="texcise" data-filter-control="input"
							data-sortable="true" >EXCISE</th>
							</c:if>	
						<th data-field="invoice value" data-filter-control="input"
							data-sortable="true" class='tright'>Amount(Rs.)</th>
					</tr>
				
				<tbody>
				    
					<c:forEach var="entry" items="${salesEntryList}" >
					<c:if test="${entry.sale_type !=null}" >					
						<tr>
							<td style="text-align: left;" >
								<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
								${createdDate}
							</td>
							<td style="text-align: left;" >${entry.voucher_no}</td>
							<td style="text-align: left;" >
									<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
						Cash Sales
						</c:if>	
						<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
						 Card Sales 
						  <c:if test="${(entry.bank!=null)}" >
						  - ${entry.bank.bank_name} - ${entry.bank.account_no}
						  </c:if>
						</c:if>	
						<c:if test="${entry.customer!=null}" >
						${entry.customer.firm_name}
						</c:if>	
						</td>
							<td style="text-align: left;" >${entry.subledger.subledger_name}</td>	
							
							
							  <c:if test="${BasicAmount>0}" >
							
							<c:set var="basicAmount" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.discount))}" />
		                      </c:forEach>
	                          </c:if>
	                          <td class='tright'>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${basicAmount}"  />							
							</td>
							</c:if>
							
							 
					     <c:if test="${Totalfreight>0}" >
				          	<c:set var="freight" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="freight" value="${freight + info.freight}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${freight}"  />							
							</td>
					     </c:if>	
					     
					       <c:if test="${Totallabourcharges>0}" >
							   <c:set var="labourcharges" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="labourcharges" value="${labourcharges + info.labour_charges}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${labourcharges}"  />							
							</td>
							</c:if>
							
							 <c:if test="${TotalOthers>0}" >
								   <c:set var="other" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="other" value="${other + info.others}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${other}"  />							
							</td>
							</c:if>
							
							<c:if test="${Taxablevalue>0}" >
							<td class='tright'>
							     <c:set var="taxablevalue" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-(info.discount)}" />
	                           <c:set var="freight" value="${info.freight}" />
	                           <c:set var="others" value="${info.others}" />		
	                           <c:set var="labourcharges" value="${info.labour_charges}" />
	                           <c:set var="taxablevalue" value="${taxablevalue+basicAmount+freight+labourcharges+others}" />	
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${taxablevalue}"  />							
							</td>
							</c:if>
							
							  <c:if test="${TotalCgst>0}" > 
							<c:if test="${entry.cgst!=null && entry.cgst>0}" >
							<td class='tright'>
							  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}"  />						
							</td>
							</c:if>
							<c:if test="${entry.cgst==null || entry.cgst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							
							 <c:if test="${Totalsgst>0}" > 
							<c:if test="${entry.sgst!=null && entry.sgst>0}" >
							<td class='tright'>
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.sgst==null || entry.sgst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totaligst>0}" > 
							<c:if test="${entry.igst!=null && entry.igst>0}" >
							<td class='tright'>							
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.igst==null || entry.igst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
			
						    <c:if test="${Totalsct>0}" > 
							<c:if test="${entry.state_compansation_tax!=null && entry.state_compansation_tax>0}" >
							<td class='tright'>
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.state_compansation_tax}"  />					
							</td>
							</c:if>
							<c:if test="${entry.state_compansation_tax==null || entry.state_compansation_tax==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totalvat>0}" >	
							<c:if test="${entry.total_vat!=null && entry.total_vat>0}" >
							<td class='tright'>
								 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vat}"  />					
							</td>
							</c:if>
							<c:if test="${entry.total_vattbody==null || entry.total_vat==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalvatcst>0}" >
							<c:if test="${entry.total_vatcst!=null && entry.total_vatcst>0}" >					
							<td class='tright'>
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vatcst}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_vatcst==null || entry.total_vatcst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalexcise>0}" >
							<c:if test="${entry.total_excise!=null && entry.total_excise>0}" >		
							<td class='tright'>
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_excise}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_excise==null || entry.total_excise==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>
						
					     
					     	<c:if test="${InvoiceValue>0}" >
							<td style="text-align: right;" >
							     <c:set var="invoiceValue" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.discount))}" />
	                           <c:set var="freight" value="${info.freight}" />
	                           <c:set var="others" value="${info.others}" />		
	                           <c:set var="labourcharges" value="${info.labour_charges}" />
	                           <c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}" />	
	                            <c:set var="invoiceValue" value="${invoiceValue+taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}" />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${invoiceValue}"  />							
							</td>
							</c:if>	
						</tr>
				 </c:if>
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee;height:100px !important;'>
					<tr>
					
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					
				 <c:if test="${BasicAmount>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${BasicAmount}"  /></b></td>
				 </c:if>
				  <c:if test="${Totalfreight>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalfreight}"  /></b></td>
				 </c:if>
				  <c:if test="${Totallabourcharges>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totallabourcharges}"  /></b></td>
				 </c:if>
				   <c:if test="${TotalOthers>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalOthers}"  /></b></td>
				 </c:if>
				   <c:if test="${Taxablevalue>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Taxablevalue}"  /></b></td>
				 </c:if>

				  <c:if test="${TotalCgst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalCgst}"  /></b></td>
				 </c:if>	
				 <c:if test="${Totalsgst>0}" >	
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsgst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totaligst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totaligst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalsct>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsct}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalvat>0}" >		
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvat}"  /></b></td>
				 </c:if>
					<c:if test="${Totalvatcst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvatcst}"  /></b></td>
					</c:if>
					<c:if test="${Totalexcise>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalexcise}"  /></b></td>
					</c:if>
					<c:if test="${InvoiceValue>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
					</c:if>
					</tr>
				</tfoot>
			</table>
			</font>
		</div> 
		<!-- Table to show list -->
		<div class="table-scroll" >
		
		
		<c:set var="rowcount" value="0" scope="page" />
		
		
		
		
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table" style="width:100%;border:1px solid black;" >
				<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true" >Created Date </th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true" >Voucher Number</th>	
					   <th data-field="particulars" data-filter-control="input"
							data-sortable="true" >Particulars</th>	
						<th data-field="income type" data-filter-control="input"
							data-sortable="true" >Income Type</th>
						<th data-field="basic amount" data-filter-control="input"
							data-sortable="true" >Basic Amount</th>
							<c:if test="${Totalfreight>0}" >
						  <th data-field="freight" data-filter-control="input"
							data-sortable="true" >Freight</th>
							</c:if>
						<c:if test="${Totallabourcharges>0}" >
						  <th data-field="labour" data-filter-control="input"
							data-sortable="true" >Labour Cost</th>
							</c:if>
						<c:if test="${TotalOthers>0}" >
						  <th data-field="others" data-filter-control="input"
							data-sortable="true" >Others</th>
							</c:if>
						<th data-field="taxable value" data-filter-control="input"
							data-sortable="true" >Taxable value</th>
						  <c:if test="${TotalCgst>0}" >
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" >CGST</th>
						</c:if>	
						 <c:if test="${Totalsgst>0}" >	
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" >SGST</th>
						</c:if>	
							<c:if test="${Totaligst>0}" >
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" >IGST</th>
							</c:if>	
							<c:if test="${Totalsct>0}" >
						<th data-field="sct" data-filter-control="input"
							data-sortable="true" >SCT</th>
							</c:if>	
                          <c:if test="${Totalvat>0}" >		
						<th data-field="tvat" data-filter-control="input"
							data-sortable="true" >VAT</th>
							</c:if>	
							<c:if test="${Totalvatcst>0}" >
						<th data-field="tvcst" data-filter-control="input"
							data-sortable="true" >CESS</th>
							</c:if>	
							<c:if test="${Totalexcise>0}" >
						<th data-field="texcise" data-filter-control="input"
							data-sortable="true" >EXCISE</th>
							</c:if>	
						<th data-field="invoice value" data-filter-control="input"
							data-sortable="true" >Invoice Value</th>
						</tr>
					
				</thead>
				<tbody>
					<c:forEach var="entry" items="${salesEntryList}" >
					<c:if test="${entry.sale_type !=null}" >
						<tr>
							<td style="text-align: left;" >
								<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
								${createdDate}
							</td>
							<td style="text-align: left;" >${entry.voucher_no}</td>
							<td style="text-align: left;" >
									<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
						Cash Sales
						</c:if>	
						<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
						 Card Sales 
						 <%--  <c:if test="${(entry.bank!=null)}" >
						  - ${entry.bank.bank_name} - ${entry.bank.account_no}
						  </c:if> --%>
						</c:if>	
						<c:if test="${entry.customer!=null}" >
						${entry.customer.firm_name}
						</c:if>	
						</td>
							<td style="text-align: left;" >${entry.subledger.subledger_name}</td>	
							
							
							  <c:if test="${BasicAmount>0}" >
							<td class='tright'>
							<c:set var="basicAmount" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${((info.rate*info.quantity)-(info.discount))}" />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${basicAmount}"  />							
							</td>
							</c:if>
							
							 
					     <c:if test="${Totalfreight>0}" >
				          	<c:set var="freight" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="freight" value="${freight + info.freight}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${freight}"  />							
							</td>
					     </c:if>	
					     
					       <c:if test="${Totallabourcharges>0}" >
							   <c:set var="labourcharges" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="labourcharges" value="${labourcharges + info.labour_charges}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${labourcharges}"  />							
							</td>
							</c:if>
							
							 <c:if test="${TotalOthers>0}" >
								   <c:set var="other" value="0" />
							<td class='tright'>
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                      <c:set var="other" value="${other + info.others}"  />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${other}"  />							
							</td>
							</c:if>
							
							<c:if test="${Taxablevalue>0}" >
							<td class='tright'>
							     <c:set var="taxablevalue" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.discount))}" />
	                           <c:set var="freight" value="${info.freight}" />
	                           <c:set var="others" value="${info.others}" />		
	                           <c:set var="labourcharges" value="${info.labour_charges}" />
	                           <c:set var="taxablevalue" value="${taxablevalue+basicAmount+freight+labourcharges+others}" />	
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${taxablevalue}"  />							
							</td>
							</c:if>
							
							  <c:if test="${TotalCgst>0}" > 
							<c:if test="${entry.cgst!=null && entry.cgst>0}" >
							<td class='tright'>
							  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.cgst}"  />						
							</td>
							</c:if>
							<c:if test="${entry.cgst==null || entry.cgst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							
							 <c:if test="${Totalsgst>0}" > 
							<c:if test="${entry.sgst!=null && entry.sgst>0}" >
							<td class='tright'>
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.sgst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.sgst==null || entry.sgst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totaligst>0}" > 
							<c:if test="${entry.igst!=null && entry.igst>0}" >
							<td class='tright'>							
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.igst}"  />					
							</td>
							</c:if>
							<c:if test="${entry.igst==null || entry.igst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
			
						    <c:if test="${Totalsct>0}" > 
							<c:if test="${entry.state_compansation_tax!=null && entry.state_compansation_tax>0}" >
							<td class='tright'>
							 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.state_compansation_tax}"  />					
							</td>
							</c:if>
							<c:if test="${entry.state_compansation_tax==null || entry.state_compansation_tax==0}" >
							<td style="text-align: left;" >0</td>
							</c:if>
							</c:if>	
							
							 <c:if test="${Totalvat>0}" >	
							<c:if test="${entry.total_vat!=null && entry.total_vat>0}" >
							<td class='tright'>
								 <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vat}"  />					
							</td>
							</c:if>
							<c:if test="${entry.total_vat==null || entry.total_vat==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalvatcst>0}" >
							<c:if test="${entry.total_vatcst!=null && entry.total_vatcst>0}" >					
							<td class='tright'>
							     <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_vatcst}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_vatcst==null || entry.total_vatcst==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>	
							
							<c:if test="${Totalexcise>0}" >
							<c:if test="${entry.total_excise!=null && entry.total_excise>0}" >		
							<td class='tright'>
								<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.total_excise}"  />						
							</td>
							</c:if>	
							<c:if test="${entry.total_excise==null || entry.total_excise==0}" >
							<td class='tright'>0</td>
							</c:if>
							</c:if>
						
					     
					     	<c:if test="${InvoiceValue>0}" >
							<td class='tright'" >
							     <c:set var="invoiceValue" value="0" />
							 <c:if test="${entry.productinfoList!=null && entry.productinfoList.size()>0 }" >
	                           <c:forEach var="info" items="${entry.productinfoList}" >
		                       <c:set var="basicAmount" value="${(info.rate*info.quantity)-((info.discount))}" />
	                           <c:set var="freight" value="${info.freight}" />
	                           <c:set var="others" value="${info.others}" />		
	                           <c:set var="labourcharges" value="${info.labour_charges}" />
	                           <c:set var="taxablevalue" value="${basicAmount+freight+labourcharges+others}" />	
	                            <c:set var="invoiceValue" value="${invoiceValue+taxablevalue+info.CGST+info.SGST+info.IGST+info.VAT+info.VATCST+info.excise}" />
		                      </c:forEach>
	                          </c:if>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${invoiceValue}"  />							
							</td>
							</c:if>	
						</tr>
				 </c:if>
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
					<td>Total</td>
					<td></td>
					<td ></td>
					<td></td>
				 <c:if test="${BasicAmount>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${BasicAmount}"  /></b></td>
				 </c:if>
				  <c:if test="${Totalfreight>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalfreight}"  /></b></td>
				 </c:if>
				  <c:if test="${Totallabourcharges>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totallabourcharges}"  /></b></td>
				 </c:if>
				   <c:if test="${TotalOthers>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalOthers}"  /></b></td>
				 </c:if >
				   <c:if test="${Taxablevalue>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Taxablevalue}"  /></b></td>
				 </c:if>

				  <c:if test="${TotalCgst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TotalCgst}"  /></b></td>
				 </c:if>	
				 <c:if test="${Totalsgst>0}" >	
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsgst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totaligst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totaligst}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalsct>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalsct}"  /></b></td>
				 </c:if>
				 <c:if test="${Totalvat>0}" >		
					<td ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvat}"  /></b></td>
				 </c:if>
					<c:if test="${Totalvatcst>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalvatcst}"  /></b></td>
					</c:if>
					<c:if test="${Totalexcise>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalexcise}"  /></b></td>
					</c:if>
					<c:if test="${InvoiceValue>0}" >
					<td class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
					</c:if>
					</tr>
				</tfoot>
			</table>
			</div>
		</c:if>
		
		<!-- for option condensed-->
		
	<c:if test="${option == 1}" >
		
	<c:set var="Totalamount" value="0" />	
	<c:set var="Totalcreditamount" value="0" />	
	<c:forEach var="entry" items="${salesEntryList}" >
	<c:set var="Totalamount" value="${Totalamount + entry.round_off}"  />
	<c:set var="Totalcreditamount" value="${Totalcreditamount + entry.round_off}"  />
	<!-- Code for Excel -->
	</c:forEach>
	<!-- Excel Start -->
				<!--code for excel  -->
				<div  id="excel_report" style="display:none" >
				<!-- Date -->
				<font size="11" face="verdana" >
					<table>
						<tr style="text-align:center;"><td colspan='5'><b>Sales Entry Report</b></td></tr>	
					<tr></tr>
						<tr style="text-align:center;"><td colspan='5'>Company Name: ${company.company_name}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>Address: ${company.permenant_address}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>
								<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate1" type="date"  />
	                   			 <fmt:formatDate value="${parsedDate1}" var="fromdate" type="date" pattern="dd-MM-yyyy"  />
	                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate2" type="date"  />
	                   			 <fmt:formatDate value="${parsedDate2}" var="todate" type="date" pattern="dd-MM-yyyy"  />
						From ${fromdate} To ${todate}</td></tr>
						<tr style="text-align:center;"><td colspan='5'>
						CIN:
						<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}" >
						 ${company.registration_no}
					    </c:if>	
						</td></tr>
					</table>
					</font>
			<!-- Date -->
			<font size="11" face="verdana" >
							<table style="border:1pt solid  !important  border-collapse: collapse;" >
							<thead>
								<tr style="border:thin solid  !important ">
									<th data-field="date" data-filter-control="input"
										data-sortable="true" >Created Date</th>
								     <th data-field="voucher number" data-filter-control="input"
										data-sortable="true" >Voucher Number</th>
									<th data-field="particulars" data-filter-control="input"
										data-sortable="true" >Particulars</th>
									<th data-field="voucher type" data-filter-control="input"
										data-sortable="true" >Voucher Type</th>								
									<!-- <th data-field="debit" data-filter-control="input"
										data-sortable="true" >Debit</th> -->
									<th data-field="credit11" data-filter-control="input"
										data-sortable="true" >Amount(Rs.)</th>
										
								</tr>
							</thead>
							<tbody>
								<c:set var="row_total" value="0"  />
								<c:forEach var="entry" items="${salesEntryList}" >
								<c:if test="${(entry.sale_type!=null)}" >
									<tr style="border:thin solid  !important ">
										<td style="text-align: left;" >
											<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
			                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
											${createdDate}
										</td>
										<td style="text-align: left;" >${entry.voucher_no}</td>			
										<td style="text-align: left;" >
												<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
									Cash Sales
									</c:if>	
									<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
									 Card Sales 
									  <c:if test="${(entry.bank!=null)}" >
									  - ${entry.bank.bank_name} - ${entry.bank.account_no}
									  </c:if>
									</c:if>	
									<c:if test="${entry.customer!=null}" >
									${entry.customer.firm_name}
									</c:if>	
									</td>
										<td style="text-align: left;" >Sales</td>
										<%-- 				
										<td style="text-align: left;" >
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}"  />						
										</td> --%>
										<td style="text-align: left;" >
										
										<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.transaction_value}"  /></td>
										
									</tr>
							</c:if>	
								</c:forEach>
							
								<tr style="border:thin solid  !important ">
									<td>Total</td>
									<td></td>
									<td></td>
									<td></td>						
									<%-- 
									<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}"  /></b></td>
						 	       
						 	          --%>
						 	         
									<td class="tright" ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
						 	         
								</tr>
						</tbody>
						</table>
						</font>
				</div>
			
			<!-- Excel End -->
			<!-- code for pdf  -->
			
			<!-- code for Hiden Table for pdf creation  -->
			<div class="table-scroll"  style="display:none;" id="tableDiv" >
			
			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
			<font size="11" face="verdana" >
			<table id="Hiddentable" >
			
					<tr >
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;" >Sales Register</td>
						
					</tr>
			
					<tr>
						
						<td align="center" >Company Name: </td>
						<td></td>
						<td align="center" >${company.company_name}</td>
					</tr>
					<tr>
							<td align="center" >Address: </td>
							<td></td>
							<td align="center" >${company.permenant_address}</td>
					</tr>
					<tr>
						<td>
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate1" type="date"  />
                   			 <fmt:formatDate value="${parsedDate1}" var="fromdate" type="date" pattern="dd-MM-yyyy"  />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate2" type="date"  />
                   			 <fmt:formatDate value="${parsedDate2}" var="todate" type="date" pattern="dd-MM-yyyy"  />
						From:
						</td>
						<td></td>
						<td>${fromdate} To ${todate}</td>
					</tr>
					<tr>
					
					<td colspan='6'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}" >
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
				
					<tr  style="border:thin solid  !important ">
						<th data-field="date" data-filter-control="input"
							data-sortable="true" >Created Date</th>
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Type</th>
					<!-- 
						<th data-field="debit" data-filter-control="input"
							data-sortable="true" >Debit</th> -->
						<th data-field="credit" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Amount(Rs.)</th>
							
					</tr>
				
				<tbody>
					<c:set var="row_total" value="0"  />
					<c:forEach var="entry" items="${salesEntryList}" >
					<c:if test="${(entry.sale_type!=null)}" >
						<tr style="border:thin solid  !important ">
							<td style="text-align: left;" >
								<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
								${createdDate}
							</td>
							<td class='tright'>${entry.voucher_no}</td>			
							<td class='tright'>
									<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
						Cash Sales
						</c:if>	
						<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
						 Card Sales 
						  <c:if test="${(entry.bank!=null)}" >
						  - ${entry.bank.bank_name} - ${entry.bank.account_no}
						  </c:if>
						</c:if>	
						<c:if test="${entry.customer!=null}" >
						${entry.customer.firm_name}
						</c:if>	
						</td>
							<td style="text-align: right;" >Sales</td>
							<%-- 				
							<td style="text-align: left;" >
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}"  />						
							</td> --%>
							<td class='tright'>
							
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.transaction_value}"  /></td>
							
						</tr>
				</c:if>	
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>						
						<%-- 
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}"  /></b></td>
			 	       
			 	         
			 	          --%>
						<td  class='tright'><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
			 	         
					</tr>
				</tfoot>
			</table>
			</font>
		</div>
		<!-- Code for viewing page for pdf -->
		<div class="table-scroll" >
			<table id="table" data-toggle="table" data-search="false"
				data-escape="false" data-filter-control="true"
				data-show-export="false" data-click-to-select="true"
				data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
				class="table table-scroll" >
				<thead>
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true" >Created Date</th>
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Type</th>
					<!-- 
						<th data-field="debit" data-filter-control="input"
							data-sortable="true" >Debit</th> -->
						<th data-field="credit" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Credit</th>
							
					</tr>
				</thead>
				<tbody>
					<c:set var="row_total" value="0"  />
					<c:forEach var="entry" items="${salesEntryList}" >
					<c:if test="${(entry.sale_type!=null)}" >
						<tr>
								<td style="text-align: left;" >
								<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"  />
                   				<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy"  />
								${createdDate}
							</td>
							<td class='tright'>${entry.voucher_no}</td>			
							<td style="text-align: left;" >
									<c:if test="${(entry.customer==null && entry.sale_type==1)}" >
						Cash Sales
						</c:if>	
						<c:if test="${(entry.customer==null && entry.sale_type==2)}" >
						 Card Sales 
						  <c:if test="${(entry.bank!=null)}" >
						  - ${entry.bank.bank_name} - ${entry.bank.account_no}
						  </c:if>
						</c:if>	
						<c:if test="${entry.customer!=null}" >
						${entry.customer.firm_name}
						</c:if>	
						</td>
							<td style="text-align: left;" >Sales</td>
							<%-- 				
							<td style="text-align: left;" >
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}"  />						
							</td> --%>
							<td class='tright'>
							
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.transaction_value}"  /></td>
							
						</tr>
				</c:if>	
					</c:forEach>
				</tbody>
				<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>						
						<%-- 
						<td><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Totalamount}"  /></b></td>
			 	       
			 	         
			 	          --%>
						<td class="tright" ><b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${InvoiceValue}"  /></b></td>
			 	         
					</tr>
				</tfoot>
			</table>
		</div>
		</c:if>
	</c:if>
		<!--End of Condensed Option  -->
		<div class="row text-center-btn" >
		<c:if test="${role!=7}" >
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
                }});" >
				Download as PDF
			</button>
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Sales-Report")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();" >
				<spring:message code="btn_back"  />
			</button>
		</div>
		
	</div>

	
	</div>
	
	


<script type="text/javascript" >
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function back(){
		window.location.assign('<c:url value = "salesReport" />');	
	}
	
	var count=7;
	if(${Totalfreight}>0){
		count=count+1;
	}
	if(${Totallabourcharges}>0){
		count=count+1;
	}
	if(${TotalOthers}>0){
		count=count+1;
	}
	if(${TotalCgst}>0){
		count=count+1;
	}
	if(${Totalsgst}>0){
		count=count+1;
	}
	if(${Totaligst}>0){
		count=count+1;
	}
	if(${Totalsct}>0){
		count=count+1;
	}
	if(${Totalvat}>0){
		count=count+1;
	}
	if(${Totalvatcst}>0){
		count=count+1;
	}
	if(${Totalvatcst}>0){
		count=count+1;
	}
	if(${Totalexcise}>0){
		count=count+1;
	}
	//count=count+ ++ +++  ++;
	 var y=document.getElementById('colmn').rows[0].cells;
	 y[0].colSpan=count;
	var table = document.getElementById("colmn");
	
	//alert(count);
	var tbl=table.rows.length;;
//	alert(tbl);
	for (var i = 2; i < tbl; i++){
	 var x=document.getElementById('colmn').rows[i].cells;
	// alert("hi");
	// alert(x[0]);
	    x[0].colSpan=count;
	   // x[1].colSpan="6"
	}
	/* function pdf(){
		window.location.assign('<c:url value = "pdfSalesEntry" />');	
	}  */

</script>

<%@include file="/WEB-INF/includes/footer.jsp"%>