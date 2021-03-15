<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/billsPayable.js" var="tableexport" />
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
	<h3>Bills Payable</h3>					
	<a href="homePage">Home</a> » <a href="billsPayableReport">Bills Payable</a>
</div>	

<c:if test="${option==0}">
<div class="col-md-12" >
	<!-- Excel Start -->
				<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Bills Payable</b></td></tr>					
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
					<th data-field="Company" data-filter-control="input"
						data-sortable="true">Party Name</th>
					<th data-field="Pending amount" data-filter-control="input"
						data-sortable="true">Pending Amount</th>
				</tr>
			</thead>
							<tbody>
							<c:set var="TAmount" value="0"/>		
							
							<c:forEach var = "entry" items = "${billsReceivable1}">
								<tr>
					
					               <td style="text-align: left;">${entry.key}</td>
					               <td class="tright">
					               <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.value}" />
					               <c:set var="TAmount" value="${TAmount + entry.value}" />
					              </td>
				              </tr>
							</c:forEach>
							
									<tr>
										<td>Total</td>
										<td></td>
										<td></td>
									  <td>
							 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
									</tr>
								</tbody>
						</table>		
				</div>
	<!-- Excel End -->
	
	<!-- pdf start -->
	
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
	
		<c:set var="rowcount" value="0" scope="page" />
		<c:if test="${rowcount == 0}">


			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Bills Payable Report</td>
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
				<tr>
				<th  data-field="Company" data-filter-control="input" data-sortable="true" >Party Name</th>	
				<th  data-field="Pending amount" data-filter-control="input" data-sortable="true" >Pending Amount</th>
			</tr>
			</tr>
			</c:if>
			<tbody>
			<c:set var="TAmount" value="0"/>		
			
			<c:forEach var = "entry" items = "${billsReceivable1}">
				<tr>
				<td style="text-align: left;">${entry.key}</td>
					<td class="tright">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.value}" />
					<c:set var="TAmount" value="${TAmount + entry.value}" />
					</td>
				</tr>
			</c:forEach>
			</tbody>
			
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td class="tright">
			 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
					</tr>
				</tfoot>
		</table>	
	</div>	
			

			<!-- pdf end -->
	
	<div class = "borderForm" >
	
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
				<th  data-field="Company" data-filter-control="input" data-sortable="true" >Party Name</th>	
				<th  data-field="Pending amount" data-filter-control="input" data-sortable="true" >Pending Amount</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="TAmount" value="0"/>		
			
			<c:forEach var = "entry" items = "${billsReceivable1}">
				<tr>
					
					<td style="text-align: left;">${entry.key}</td>
					<td class="tright">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.value}" />
					<c:set var="TAmount" value="${TAmount + entry.value}" /></td>
				</tr>
			</c:forEach>
			</tbody>
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						
					  <td class="tright">
			 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("BillsPayable-Report")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
   
</div>
</c:if>
<c:if test="${option>0}">
<div class="col-md-12" >
	<!-- Excel Start -->
				<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Bills Payable</b></td></tr>					
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
								<th  data-field="Date" data-filter-control="input" data-sortable="true" >Date</th>
								<th  data-field="Voucher Number" data-filter-control="input" data-sortable="true" >Voucher Number</th>	
								<th data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>
								<th  data-field="Company" data-filter-control="input" data-sortable="true" >Party Name</th>	
								<th  data-field="Pending amount" data-filter-control="input" data-sortable="true" >Pending Amount</th>
							</tr>
							</thead>
							<tbody>
							<c:set var="TAmount" value="0"/>		
							
							<c:forEach var = "entry" items = "${billsPayable}">
								<tr>
									<td style="text-align: left;">
										<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
				              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
										${createdDate}					
									</td>
									<td style="text-align: left;">${entry.voucher_no}</td>
									<td style="text-align: left;">${entry.particulars}</td>
									<td style="text-align: left;">${entry.supplier.company_name}</td>
									<td style="text-align: left;">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}" />
									<c:set var="TAmount" value="${TAmount + entry.round_off}" /></td>
								</tr>
							</c:forEach>
							
									<tr>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									  <td>
							 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
									</tr>
								</tbody>
						</table>		
				</div>
	<!-- Excel End -->
	
	<!-- pdf start -->
	
	
	<div class="table-scroll"  style="display:none;" id="tableDiv">
	
			<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Bills Payable Report</td>
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
				<th data-field="Date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="Voucher Number" data-filter-control="input"
					data-sortable="true">Voucher Number</th>
				<th data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>
				<th data-field="Company" data-filter-control="input"
					data-sortable="true">Party Name</th>
				<th data-field="Pending amount" data-filter-control="input"
					data-sortable="true" style="text-align:right;">Pending Amount</th>
			</tr>
			
			<tbody>
			<c:set var="TAmount" value="0"/>		
			
			<c:forEach var = "entry" items = "${billsPayable}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;">${entry.voucher_no}</td>
					<td style="text-align: left;">${entry.particulars}</td>
					<td style="text-align: left;">${entry.supplier.company_name}</td>
					<td class="tright">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}" />
					<c:set var="TAmount" value="${TAmount + entry.round_off}" /></td>
				</tr>
			</c:forEach>
			</tbody>
			
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
					  <td class="tright">
			 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
					</tr>
				</tfoot>
		</table>	
	</div>	
			

			<!-- pdf end -->
	
	<div class = "borderForm" >
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
				<th  data-field="Date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="Voucher Number" data-filter-control="input" data-sortable="true" >Voucher Number</th>	
				<th data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>
				<th  data-field="Company" data-filter-control="input" data-sortable="true" >Party Name</th>	
				<th  data-field="Pending amount" data-filter-control="input" data-sortable="true" >Pending Amount</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="TAmount" value="0"/>		
			
			<c:forEach var = "entry" items = "${billsPayable}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${entry.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;">${entry.voucher_no}</td>
					<td style="text-align: left;">${entry.particulars}</td>
					<td style="text-align: left;">${entry.supplier.company_name}</td>
					<td class="tright">
					<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${entry.round_off}" />
					<c:set var="TAmount" value="${TAmount + entry.round_off}" /></td>
				</tr>
			</c:forEach>
			</tbody>
			
			<tfoot style='background-color: #eee'>
					<tr>
						<td>Total</td>
						<td></td>
						<td></td>
						<td></td>
					  <td class="tright">
			 	   <b><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${TAmount}" /></b></td>
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("BillsPayable-Report")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
   
</div>
</c:if>

<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "billsPayableReport"/>');	
	}
	
	
</script>
	<%@include file="/WEB-INF/includes/footer.jsp" %>