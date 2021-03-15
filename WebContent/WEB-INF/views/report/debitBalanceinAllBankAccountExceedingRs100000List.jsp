<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>


<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/exceptionReport2.js"
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
	<h3>BANK BALANCE REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport2">BANK BALANCE REPORT</a>
</div>
<div class="col-md-12">
	<!-- Excel Start -->
	<div style="display: none" id="excel_report">
	<table>
				<tr style="text-align: center;">
					<td></td>
					<td></td>
				<td><b>BANK BALANCE REPORT</b></td>
				</tr>
				<tr></tr>
				<tr>
					<td colspan='7'>From <fmt:parseDate value="${from_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> 
							<fmt:formatDate value="${parsedDate}" var="from_date" type="date"
							pattern="dd-MM-yyyy" /> To 
							<fmt:parseDate value="${to_date}"
							pattern="yyyy-MM-dd" var="parsedDate" type="date" /> 
							<fmt:formatDate value="${parsedDate}" var="to_date" type="date"
							pattern="dd-MM-yyyy" /> From ${from_date} To ${to_date}</td>
				</tr>
			
			</table>
	<table id="table">
			

					
			<tr>
				<th  data-field="bank" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  class='left' data-field="debit" data-filter-control="input" data-sortable="true" >Total Debit Balance of All Banks</th>		
			</tr>
			
			<tbody>	
			<c:forEach var = "form" items = "${monthlyAvgBalanceList}">
			<tr>
				  <td style="text-align: left;">${form.companyName}</td>
				  <td class='tright'>
				  	  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${form.monthAvgBalance}" />
				  	  </td>						
				  
		    </tr>
			</c:forEach>
			</tbody>
		</table>
		
	
	</div>
	
	<div class="table-scroll" style="display: none;" id="tableDiv">
		
		<c:set var="rowcount" value="0" scope="page" />
		
		<c:if test="${rowcount == 0}">
		
			<table id="Hiddentable" >
			
				<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">BANK BALANCE REPORT
					</td>
				</tr>
				<tr>
					<td>From: ${from_date} To ${to_date}</td>
				</tr>
				<tr>
				<th  data-field="bank" data-filter-control="input" data-sortable="true" >Company Name</th>
			 	<th  class='tright' data-field="debit" data-filter-control="input" data-sortable="true" >Total Debit Balance of All Banks</th>		
			  </tr>
			</c:if>
			<tbody>	
		    <c:forEach var = "form" items = "${monthlyAvgBalanceList}">
			<tr>
				  <td style="text-align: left;">${form.companyName}</td>
				   <td class='tright'>
				  	  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${form.monthAvgBalance}" />
				  	  </td>		
		    </tr>
			</c:forEach>
			</tbody>
		
		</table>

	
	</div>
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
			
			
					<c:if test="${rowcount >  45}">
									<c:set var="rowcount" value="0" scope="page" />
								</c:if>
								<c:if test="${rowcount > 44}">
									<%@include file="/WEB-INF/views/report/debitBalanceinAllBankAccountExceedingRs100000Header.jsp"%>
								</c:if>
						
								<c:set var="rowcount" value="${rowcount + 1}" scope="page" />
			
			
			
			
			
				<th  data-field="bank" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  class='tright' data-field="debit" data-filter-control="input" data-sortable="true" >Total Debit Balance of All Banks</th>		
			</tr>
			</thead>
			<tbody>	
			<c:forEach var = "form" items = "${monthlyAvgBalanceList}">
			<tr>
				  <td style="text-align: left;">${form.companyName}</td>
				  <td class='tright'>
				  	  <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${form.monthAvgBalance}" />
				  	  </td>		
		    </tr>
			</c:forEach>
			</tbody>
		</table>
	</div>

	
	<div class="row text-center-btn">
		<c:if test="${role!=7}">
			<button class="fassetBtn" type="button"
				onclick="pdf('#Hiddentable', {type: 'pdf',
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
				Download as PDF</button>
			<button class="fassetBtn" type="button" id='btnExport3'
				onclick='exportexcel("BANK BALANCE REPORT-Report")'>Download as Excel</button>
		</c:if>
		<button class="fassetBtn" type="button" onclick="back();">
			<spring:message code="btn_back" />
		</button>
	</div>
</div>
<script type="text/javascript">	
	
	function back(){
		window.location.assign('<c:url value = "exceptionReport2"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>