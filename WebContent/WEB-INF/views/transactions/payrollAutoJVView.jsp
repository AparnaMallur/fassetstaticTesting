
<%@include file="/WEB-INF/includes/header.jsp"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url
	value="/resources/js/report_table/payrollAutoJVAccountingVoucher.js"
	var="tableexport" />
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js">
	<script type="text/javascript"	src="http://code.jquery.com/jquery-latest.min.js">
</script>
<script type="text/javascript" src="${jspdfmin}"></script>
<script type="text/javascript" src="${jspdfauto}"></script>
<script type="text/javascript" src="${tableexport}"></script>

<div class="breadcrumb">
	<h3>Payroll Auto JV</h3>
	<a href=" homePage ">Home</a> » <a href="payrollAutoJVList">Payroll Auto JV</a>
</div> 
<div class="fassetForm">
	<form:form id="payrollAutoJVForm" commandName="payrollAutoJV">
		<ul class="nav nav-tabs navtab-bg">
			<li class="active"><a href="#home" data-toggle="tab"
				aria-expanded="true"> <span class="visible-xs"> <i
						class="fa fa-home"></i>
				</span> <span class="hidden-xs">Details</span>
			</a></li>
			<li class=""><a href="#profile" data-toggle="tab"
				aria-expanded="false"> <span class="visible-xs"> <i
						class="fa fa-user"></i>
				</span> <span class="hidden-xs">Accounting Voucher</span>
			</a></li>
		</ul>


		<div class="tab-content">
			<div class="tab-pane active" id="home">
				<div class="table-responsive" >


					<table class="table">
						<!--Profile UI view started  -->

						<tr>
							<td><strong>Payroll Auto Voucher No:</strong></td>
							<td style="text-align: left;">${payrollAutoJV.voucherSeries.voucher_no}</td>
						</tr>
						<tr>
							<td><strong> Payroll Auto Date:</strong></td>
							<fmt:parseDate value="${payrollAutoJV.date}" pattern="yyyy-MM-dd"
								var="parsedDate" type="date" />
							<fmt:formatDate value="${parsedDate}" var="date" type="date"
								pattern="dd-MM-yyyy" />
							<td style="text-align: left;">${date}</td>
						</tr>

						<tr>
							<td><strong>Payroll Auto Other Remark::</strong></td>
							<td style="text-align: left;">${payrollAutoJV.other_remark}</td>
						</tr>

					</table>
					<h3>Employee Details</h3>
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Employee ID</th>
								<th>Employee Name</th>
								<th>Basic Salary</th>
								<th>DA</th>
								<th>Conv. Allowance</th>
								<th>Other Allowances</th>
								<th>Gross Salary</th>
								<th>PfEmployee</th>
								<th>ESIC Employee</th>
								<th>PT</th>
								<th>LWF</th>
								<th>TDS</th>
								<th>Other Deduction</th>
								<th>Advance Adjustment</th>
								<th>Total Deduction</th>
								<th>NET Salary</th>
								<th>PF Employer</th>
								<th>ESIC Employer</th>
								<th>PF Admin Charges</th>
							</tr>
						</thead>
						<c:forEach var="emp_list" items="${payrollEmployeeList}">
							<tbody>
								<tr>
									<td>${emp_list.code}</td>
									<td>${emp_list.name}</td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.basicSalary}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.DA}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.conveyanceAllowance}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.otherAllowances}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.grossSalary}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.pfEmployeeContribution}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.eSICEmployeeContribution}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.professionTax}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.lWF}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.tDS}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.otherDeductions}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.advanceAdjustment}" /></td>

									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.totalDeductions}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.netSalary}" /></td>
									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.pfEmployerContribution}" /></td>

									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.eSICEmployerContribution}" /></td>

									<td class="tright"><fmt:formatNumber type="number"
											minFractionDigits="2" maxFractionDigits="2"
											value="${emp_list.pFAdminCharges}" /></td>

								</tr>
							</tbody>

						</c:forEach>
					</table>
				</div>
				<%-- <div class="col-md-12" style="text-align: center; margin: 15px;">

					<button class="fassetBtn" type="button" onclick = "DownloadPdf(${payrollAutoJV.payroll_id})"> 
			   Download As Pdf
		        </button>
				</div> --%>
				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="payrollVoucherrpdf('#Hiddentable1', {type: 'pdf',
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

			<!--UI view started for Account Voucher  -->

			<div class="tab-pane" id="profile">
				<div class="col-md-12">


					<p class="col-md-6 col-xs-6 text-left" style="padding-left: 0px">
						<b>Payroll Auto Voucher No: ${payrollAutoJV.voucherSeries.voucher_no}</b>
					</p>

					<p class="col-md-6 col-xs-6 text-right" style="padding-left: 0px">
						<b>Date: <fmt:parseDate value="${payrollAutoJV.date}" pattern="yyyy-MM-dd"
							var="parsedDate" type="date" />
						<fmt:formatDate value="${parsedDate}" var="date" type="date"
							pattern="dd-MM-yyyy" />
							${date} </b>
 					</p>

				</div>

				<table class="table">
					<thead>
						<tr>
							<th>Subledger-Name</th>
							<th>Amount(Dr)</th>
								<th>Amount(Cr)</th>

						</tr>
					</thead>


					<tbody>
						
						<c:forEach var="subledger"
							items="${payrollAutoJV.payrollSubledgerDetails}">
							<c:if test="${subledger.drAmount!=null}">
							<tr>
									<c:if test="${subledger.drAmount>0}">
								<td style="text-align: left;">${subledger.subledgerName}</td>
								
								<td style="text-align: right;">${subledger.drAmount}</td>
								
									<td style="text-align: right;">${subledger.crAmount}</td>
									</c:if>
							</tr>
							</c:if>
							
						</c:forEach>
						<c:forEach var="subledger"
							items="${payrollAutoJV.payrollSubledgerDetails}">
							<c:if test="${subledger.crAmount!=null}">
							<tr>
							<c:if test="${subledger.crAmount>0}">
								<td style="text-align: left;">${subledger.subledgerName}</td>
								<td style="text-align: right;">${subledger.drAmount}</td>
								<td style="text-align: right;">${subledger.crAmount}</td>
								</c:if>
							</tr>
							</c:if>
							
						</c:forEach>
					</tbody>
					
					<tr style='border: 1px solid #bbb'>
	
						<td>Narration for: <%-- <br /> Being salary for the month of
							<fmt:parseDate value="${payrollAutoJV.date}" pattern="yyyy-MM-dd"
							var="parsedDate" type="date" />
						<fmt:formatDate value="${parsedDate}" var="date" type="date"
							pattern="MMMM yyyy" />
							${date} 
							accounted for. --%>
							${payrollAutoJV.other_remark}
						</td>
						<td></td>
						<td></td>
					</tr>
				</table>




				<div class="row" style="text-align: center; margin: 15px;">
					<button class="fassetBtn" type="button"
						onclick="payrollVoucherAccountingVoucherpdf('#Hiddentable', {type: 'pdf',
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
					<button class="fassetBtn" type="button" onclick="back();">
						<spring:message code="btn_back" />
					</button>
				</div>
			</div>

			<div class="table-scroll" style="display: none;" id="tableDiv1">
				<table class="table">
					<!--Profile UI view started  -->

					<tr>
						<td><strong>Payroll Auto Voucher No:</strong></td>
						<td style="text-align: left;">${payrollAutoJV.voucherSeries.voucher_no}</td>
					</tr>
					<tr>
						<td><strong> Payroll Auto Date:</strong></td>
						<fmt:parseDate value="${payrollAutoJV.date}" pattern="yyyy-MM-dd"
							var="parsedDate" type="date" />
						<fmt:formatDate value="${parsedDate}" var="date" type="date"
							pattern="dd-MM-yyyy" />
						<td style="text-align: left;">${date}</td>
					</tr>

					<tr>
						<td><strong>Payroll Auto Other Remark::</strong></td>
						<td style="text-align: left;">${payrollAutoJV.other_remark}</td>
					</tr>

				</table>
				<h3>Employee Details</h3>
				<table class="table table-bordered table-striped">

					<tr>
						<th>Employee ID</th>
						<th>Employee Name</th>
						<th>Basic Salary</th>
						<th>DA</th>
						<th>Conv. Allowance</th>
						<th>Other Allowances</th>
						<th>Gross Salary</th>
						<th>PfEmployee</th>
						<th>ESIC Employee</th>
						<th>PT</th>
						<th>LWF</th>
						<th>TDS</th>
						<th>Other Deduction</th>
						<th>Advance Adjustment</th>
						<th>Total Deduction</th>
						<th>NET Salary</th>
						<th>PF Employer</th>
						<th>ESIC Employer</th>
						<th>PF Admin Charges</th>
					</tr>

					<c:forEach var="emp_list" items="${payrollEmployeeList}">
						<tbody>
							<tr>
								<td>${emp_list.code}</td>
								<td>${emp_list.name}</td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.basicSalary}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.DA}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.conveyanceAllowance}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.otherAllowances}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.grossSalary}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.pfEmployeeContribution}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.eSICEmployeeContribution}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.professionTax}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.lWF}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.tDS}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.otherDeductions}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.advanceAdjustment}" /></td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.totalDeductions}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.netSalary}" /></td>
								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.pfEmployerContribution}" /></td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.eSICEmployerContribution}" /></td>

								<td class="tright"><fmt:formatNumber type="number"
										minFractionDigits="2" maxFractionDigits="2"
										value="${emp_list.pFAdminCharges}" /></td>

							</tr>
						</tbody>

					</c:forEach>
				</table>
			</div>
			<div class="table-scroll" style="display: none;" id="tableDiv">
				<table id="Hiddentable">
					<tr>
						<td style="color: blue; margin-left: 50px; text-align: center;">Payroll
							Auto JV</td>
					</tr>

					<tr>
						<td>Payroll Auto Voucher No: ${payrollAutoJV.voucherSeries.voucher_no}</td>
					</tr>


					<tr>
						<td><fmt:parseDate value="${payrollAutoJV.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="payroll_bill_date" type="date"
								pattern="dd-MM-yyyy" /> <b>Date: ${payroll_bill_date}</b></td>
					</tr>
					<tr>
						<th>Subledger-Name</th>
					
						<th>Amount(Dr)</th>
							<th>Amount(Cr)</th>

					</tr>


					<tbody>
						<c:forEach var="subledger"
							items="${payrollAutoJV.payrollSubledgerDetails}">
							<c:if test="${subledger.drAmount!=null}">
							<tr>
									<c:if test="${subledger.drAmount>0}">
								<td style="text-align: left;">${subledger.subledgerName}</td>
								
								<td style="text-align: right;">${subledger.drAmount}</td>
								
									<td style="text-align: right;">${subledger.crAmount}</td>
									</c:if>
							</tr>
							</c:if>
							
						</c:forEach>
						<c:forEach var="subledger"
							items="${payrollAutoJV.payrollSubledgerDetails}">
							<c:if test="${subledger.crAmount!=null}">
							<tr>
							<c:if test="${subledger.crAmount>0}">
								<td style="text-align: left;">${subledger.subledgerName}</td>
								<td style="text-align: right;">${subledger.drAmount}</td>
								<td style="text-align: right;">${subledger.crAmount}</td>
								</c:if>
							</tr>
							</c:if>
							
						</c:forEach>
					</tbody>
<tr style='border: 1px solid #bbb'>
	
						<td>Narration for: <%-- <br /> Being salary for the month of
							<fmt:parseDate value="${payrollAutoJV.date}" pattern="yyyy-MM-dd"
							var="parsedDate" type="date" />
						<fmt:formatDate value="${parsedDate}" var="date" type="date"
							pattern="MMMM yyyy" />
							${date} 
							accounted for. --%>
							${payrollAutoJV.other_remark}
						</td>
						<td></td>
						<td></td>
					</tr>
					
						<tr>
                                <c:if test="${payrollAutoJV.other_remark!=null }"> 
								
                                  <td>Remark:  ${payrollAutoJV.other_remark}</td>
                              </c:if>
                              <c:if test="${payrollAutoJV.other_remark==null }"> 
								
                                  <td>Remark:  </td>
                              </c:if>
							</tr>
				</table>

			</div>
		</div>

	</form:form>
</div>



<script>

/* function getMonth()
{
	var date=document.getElementById("datemonth").value; 
	alert(date);
	} */
	function payrollVoucherAccountingVoucherpdf(selector, params) {
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

	function payrollVoucherrpdf(selector, params) {
		selector = "#tableDiv1";
		$("#tableDiv1").css("display", "block");
		var options = {
			//ignoreRow: [1,11,12,-2],
			//ignoreColumn: [0,-1],
			//pdfmake: {enabled: true},
			tableName : 'Countries',
			worksheetName : 'Countries by population'
		};
		$.extend(true, options, params);
		$(selector).tableExport(options);
		$("#tableDiv1").css("display", "none");
	}

	function back() {
		window.location.assign('<c:url value = "payrollAutoJVList"/>');
	}

	function DownloadPdf(id) {
		window.location
				.assign('<c:url value = "downloadPayrollPdf"/>?id=' + id);
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>