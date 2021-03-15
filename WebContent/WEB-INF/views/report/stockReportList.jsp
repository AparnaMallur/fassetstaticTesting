<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/stockReport.js" var="tableexport" />
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
	<h3>Stock Report</h3>
	<a href="homePage">Home</a> » <a href="stockReport">Stock Report</a>
</div>
<!--  -->
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<!-- code for Excel -->
		<!-- Excel Start -->
				<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Stock Report</b></td></tr>	
					<tr></tr>
						<tr><td colspan='2'>Company Name: ${company.company_name}</td></tr>
						<tr><td colspan='2'>Address: ${company.permenant_address}</td></tr>	
						<tr><td colspan='5'>
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
					<th data-field="product" data-filter-control="input"
						data-sortable="true">Product</th>
					<th data-field="quantity" data-filter-control="input"
						data-sortable="true">Total Quantity</th>
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Total Amount</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="stock" items="${stockList}">
				     <c:if test="${stock != null}">
					<tr>
						<td style="text-align: left;">${stock.productName}</td>
						<td style="text-align: left;">${stock.quantity}</td>
						<td style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${stock.amount}" />
						
						</td>
					</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
				</div>
	<!-- Excel End -->
	<!-- code generation for Hidden Table  -->
	<div class="borderForm"  style="display:none;" id="tableDiv">
		
		
			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
	
		<table id="Hiddentable">
			<tr>
				<td></td>

				<td>Stock Report</td>

			</tr>

			<tr>
				<td>Company Name:  ${company.company_name}</td>
				
			</tr>
			<tr>
				<td>Address:  ${company.permenant_address}</td>
				
			</tr>

			<tr>
				<td>CIN: ${company.registration_no} <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						
					    </c:if>
				</td>
				
			</tr>
			<tr>
				
					<th data-field="product" data-filter-control="input"
						data-sortable="true">Product</th>
					<th data-field="quantity" data-filter-control="input" style="margin-left: 200px;"
						data-sortable="true">Total Quantity</th>
					<th data-field="amount" data-filter-control="input"
						data-sortable="true"  style="text-align: right;">Total Amount</th>
					
				</tr>
		</c:if>	
			<tbody>
				<c:forEach var="stock" items="${stockList}">
				     <c:if test="${stock != null}">
					<tr>
						
						<td style="text-align: left;">${stock.productName}</td>
						<td style="margin-right: 200px; text-align: left: ;" >${stock.quantity}</td>
						<td class='tright'><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${stock.amount}" />	</td>
					</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- code for pdf Generation for view page -->
	<div class="borderForm">
		
		
			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
	
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
				
					<th data-field="product" data-filter-control="input"
						data-sortable="true">Product</th>
					<th data-field="quantity" data-filter-control="input"
						data-sortable="true">Total Quantity</th>
					<th data-field="amount" data-filter-control="input"
						data-sortable="true">Total Amount</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="stock" items="${stockList}">
				     <c:if test="${stock != null}">
					<tr>
						<td style="text-align: left;">${stock.productName}</td>
						<td class="tright">${stock.quantity}</td>
						<td class="tright" style="text-align: left;"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${stock.amount}" /></td>
					</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="row" style="text-align: center; margin: 15px;">
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
		<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Stock-Report")'>
				Download as Excel
			</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
		
	</div>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function viewStockReport(product_name){
		window.location.assign('<c:url value="viewStockReport"/>?product_name='+product_name);
	}
	function back(){
		window.location.assign('<c:url value = "stockReport"/>');	
	}
	/* function PDF(){
		window.location.assign('<c:url value = "stockSummaryPdf"/>');	
	} */
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>