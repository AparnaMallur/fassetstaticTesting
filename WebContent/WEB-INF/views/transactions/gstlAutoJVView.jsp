<%@include file="/WEB-INF/includes/header.jsp"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url
	value="/resources/js/report_table/GSTAutoJVReport.js"
	var="tableexport" />
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js">
	<script type="text/javascript"	src="http://code.jquery.com/jquery-latest.min.js">
</script>
<script type="text/javascript" src="${jspdfmin}"></script>
<script type="text/javascript" src="${jspdfauto}"></script>
<script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>GST Auto JV</h3>
	<a href=" homePage ">Home</a>» <a href="gstAutoJVList">GST Auto JV</a>
</div>



<div class="fassetForm">
	<form:form id="payrollAutoJVForm" commandName="payrollAutoJV">
		
		<div class="tab-content">
			<div class="tab-pane active" id="home">
				<div class="table-responsive" >


					<table class="table">
						<!--Profile UI view started  -->
						
  
						<tr>
							<td><strong>Voucher No:</strong></td>
							<td style="text-align: left;">${autoJv.voucherSeries.voucher_no}</td>
						</tr>
						<tr>
							<td><strong> Date:</strong></td>
							<fmt:parseDate value="${autoJv.created_date}" pattern="yyyy-MM-dd"
								var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
								pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>
					
					</table>
					<h3> GST AUTO JV Details</h3>
					<table class="table table-bordered table-striped">
					<thead>
				<tr>
				<th>sr. no</th>
				<th>sub-ledger</th>
				<th>cr</th>
				<th>dr</th>
			</tr>
		</thead>
			<tbody>
			<tr>
				<td rowspan="2">1</td>
				<td>i/p Igst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputIgstDrBalance}" /></td>
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p igst</td>
				
				
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputIgstCrBalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">2</td>
				<td>i/p cgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputCgstDrBalance}" /></td>
				
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p cgst</td>
				
										<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputCgstCrbalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">3</td>

				<td>i/p sgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputSgstDrBalance}" /></td>
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p sgst</td>
				
				
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputSgstCrBalance}" /></td>
			</tr>
			<%-- <tr>
				<td rowspan="2">4</td>
				<td>gst payable</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.gstPaybleBalance}" /></td>
				<td></td>
			</tr> --%>
		</tbody>

						
					</table>
				</div>

				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="gstVoucherrpdf('#Hiddentable', {type: 'pdf',
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

				</div>

				<div class="col-md-12" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button" onclick="back();">
						<spring:message code="btn_back" />
					</button>
				</div>

			</div>
			
			
			
			

			<!--Download GST  view started  -->
								<div class="table-scroll" style="display: none;" id="tableDiv">
				<table id="Hiddentable">
						<!--Profile UI view started  -->
<tr >
						
						<td style="color:blue; margin-left: 50px;text-align: center;">GST  JV</td>
						
					</tr>
						<tr>
							<td><strong> Voucher No:</strong></td>
							<td style="text-align: left;">${autoJv.voucherSeries.voucher_no}</td>
						</tr>
						<tr>
							<td><strong>Voucher  Date:</strong></td>
							<fmt:parseDate value="${autoJv.created_date}" pattern="yyyy-MM-dd"
								var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
								pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>
					<tr></tr>
							<tr></tr>
					</table>
					
									
					<table class="table table-bordered table-striped">
					<tr></tr>
							<tr></tr>	
			<tr>
				<th>sr. no</th>
				<th>sub-ledger</th>
				<th>cr</th>
				<th>dr</th>
			</tr>
	
			<tr>
				<td rowspan="2">1</td>
				<td>i/p Igst</td>
					<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputIgstDrBalance}" /></td>
				<td></td>
			
				
			</tr>
			<tr>
				<td>o/p igst</td>
				
			
						<td></td>
							<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputIgstCrBalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">2</td>
				<td>i/p cgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputCgstDrBalance}" /></td>
				<td></td>
			
				
			</tr>
			<tr>
				<td>o/p cgst</td>
				
				
						<td></td>
							<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputCgstCrbalance}" /></td>
			</tr>
			<tr>
				<td rowspan="2">3</td>

				<td>i/p sgst</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalInputSgstDrBalance}" /></td>
				<td></td>
				
				
			</tr>
			<tr>
				<td>o/p sgst</td>
				
				
						<td></td>
						<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.totalOutputSgstCrBalance}" /></td>
			</tr>
			<%-- <tr>
				<td rowspan="2">4</td>
				<td>gst payable</td>
				<td style="text-align: right;"><fmt:formatNumber type="number"
						minFractionDigits="2" maxFractionDigits="2"
						value="${autoJv.gstPaybleBalance}" /></td>
				<td></td>
			</tr>
 --%>
						
					</table>
				</div>


		</div>

	</form:form>
</div>



<script>


	function gstVoucherrpdf(selector, params) {
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

	function back() {
		window.location.assign('<c:url value = "gstAutoJVList"/>');
	}

	function DownloadPdf(id) {
		window.location
				.assign('<c:url value = "downloadPayrollPdf"/>?id=' + id);
	}
</script>