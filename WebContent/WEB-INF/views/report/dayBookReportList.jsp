<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js"
	var="jspdfauto" />
<spring:url value="/resources/js/report_table/dayBook.js"
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
	<h3>Day Book</h3>
	<a href="homePage">Home</a> » <a href="dayBookReport">Day Book</a>
</div>
<div class="col-md-12">
	<c:if test="${successMsg != null}">
		<div class="successMsg" id="successMsg">
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<!-- code for Excel   -->
	<!-- Excel Start -->
	<div style="display: none" id="excel_report">
		<!-- Date -->
		<table>
			<tr style="text-align: center;">
				<td></td>
				<td></td>
				<td><b>Day Book</b></td>
			</tr>
			<tr></tr>
			<tr>
				<td colspan='6'>Company Name: ${company.company_name}</td>
			</tr>
			<tr>
				<td colspan='6'>Address: ${company.permenant_address}</td>
			</tr>
			<tr>
				<td colspan='6'>Date: <fmt:parseDate value="${date}"
						pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
						value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
					${date}
				</td>
			</tr>
			<tr>
				<td colspan='6'>CIN: <c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
						 ${company.registration_no}
					    </c:if>
				</td>
			</tr>
		</table>
		<!-- Date -->
		<table>
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="vtype" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="type" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>

				</tr>
			</thead>
			<tbody>

				<c:set var="row_count_credit" value="0" />
				<c:set var="row_count_debit" value="0" />

		<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td><c:choose>
			                    <c:when test='${dayBook.type==7}'> 
			                     <c:if test="${dayBook.particulars=='Deposit'}"> 
			                     	${dayBook.deposit_to}
			                    	</c:if>	
			                    	<c:if test="${dayBook.particulars=='Withdraw'}"> 
			                    		${dayBook.withdraw_from}
			                    	</c:if>
			                    	<c:if test="${dayBook.particulars=='Transfer'}"> 
			                    			${dayBook.withdraw_from}
			                    	</c:if>
			                    </c:when>
			                  
			                    <c:otherwise>	${dayBook.particulars} </c:otherwise>
                  		  </c:choose></td>
						<c:if test="${dayBook.type==1}">
		
		                <c:if test="${dayBook.subLedger!=null}">
		               <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
							</c:if>
							  <c:if test="${dayBook.subLedger==null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
					    </c:if>
						
						</c:if>
						<c:if test="${dayBook.type==2}">
						<c:if test="${dayBook.subLedger!=null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						 </c:if>
						<c:if test="${dayBook.subLedger==null}">
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
					    </c:if>
							
							
						</c:if>
						<c:if test="${dayBook.type==3}">
							 <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>

						</c:if>
						<c:if test="${dayBook.type==4}">
							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>

						</c:if>
						<c:if test="${dayBook.type==5}">

							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						</c:if>
						<c:if test="${dayBook.type==6}">
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
						</c:if>

						<c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Deposit'}"> 
						
					    <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						</c:if>	
						<c:if test="${dayBook.particulars=='Withdraw'}"> 
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						
						</c:if>
						<c:if test="${dayBook.particulars=='Transfer'}"> 
					   <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						</c:if>
						</c:if>	

					</tr>

						  
					  <c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Transfer'}"> 
						 <tr>
						<td>
							<fmt:parseDate value="${dayBook.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   			${createdDate}						
						</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td>
							${dayBook.deposit_to}
						</td>
					   <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						 </tr>
						</c:if>
						</c:if> 
				</c:forEach>
				<tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_debit}" /></b></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_credit}" /></b></td>

				</tr>
			</tbody>
		</table>
	</div>
	<!-- Excel End -->
	<!-- code for hidden table  -->
	
	<div class="borderForm" style="display: none;" id="tableDiv">
		
			<c:set var="rowcount" value="0" scope="page" />
			<c:if test="${rowcount == 0}">
		
		<table id="Hiddentable">
			<!-- for PDf heading -->
			<tr>
				<td></td>

				<td style="text-align: center;">Day Book</td>
			</tr>
			<tr>
				<td>Company Name: ${company.company_name}</td>

			</tr>
			<tr>
				<td>Address: ${company.permenant_address}</td>

			</tr>
			<tr>
				<td>Date: ${date}</td>
				<%-- <fmt:parseDate value="${date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                   			 <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" /> --%>
				<%-- <td>${date}</td> --%>
			</tr>
			<tr>
				<td>CIN: ${company.registration_no}</td>
				<td><c:if
						test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">

					</c:if></td>
			</tr>

			<tr>
				<th data-field="date" data-filter-control="input"
					data-sortable="true">Date</th>
				<th data-field="vtype" data-filter-control="input"
					data-sortable="true">Voucher Number</th>
				<th data-field="type" data-filter-control="input"
					data-sortable="true">Voucher Type</th>
				<th data-field="particulars" data-filter-control="input"
					data-sortable="true">Particulars</th>
				<th data-field="debit" data-filter-control="input"
					data-sortable="true">Debit</th>
				<th data-field="credit" data-filter-control="input"
					data-sortable="true">Credit</th>
			</tr>
			</c:if>
			
			<tbody>

				<c:set var="row_count_credit" value="0" />
				<c:set var="row_count_debit" value="0" />

			<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td><c:choose>
			                    <c:when test='${dayBook.type==7}'> 
			                     <c:if test="${dayBook.particulars=='Deposit'}"> 
			                     	${dayBook.deposit_to}
			                    	</c:if>	
			                    	<c:if test="${dayBook.particulars=='Withdraw'}"> 
			                    		${dayBook.withdraw_from}
			                    	</c:if>
			                    	<c:if test="${dayBook.particulars=='Transfer'}"> 
			                    			${dayBook.withdraw_from}
			                    	</c:if>
			                    </c:when>
			                  
			                    <c:otherwise>	${dayBook.particulars} </c:otherwise>
                  		  </c:choose></td>
						<c:if test="${dayBook.type==1}">
		
		                <c:if test="${dayBook.subLedger!=null}">
		               <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
							</c:if>
							  <c:if test="${dayBook.subLedger==null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
					    </c:if>
						
						</c:if>
						<c:if test="${dayBook.type==2}">
						<c:if test="${dayBook.subLedger!=null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						 </c:if>
						<c:if test="${dayBook.subLedger==null}">
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
					    </c:if>
							
							
						</c:if>
						<c:if test="${dayBook.type==3}">
							 <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>

						</c:if>
						<c:if test="${dayBook.type==4}">
							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>

						</c:if>
						<c:if test="${dayBook.type==5}">

							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						</c:if>
						<c:if test="${dayBook.type==6}">
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
						</c:if>

						<c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Deposit'}"> 
						
					    <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						</c:if>	
						<c:if test="${dayBook.particulars=='Withdraw'}"> 
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						
						</c:if>
						<c:if test="${dayBook.particulars=='Transfer'}"> 
					   <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						</c:if>
						</c:if>	

					</tr>

						  
					  <c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Transfer'}"> 
						 <tr>
						<td>
							<fmt:parseDate value="${dayBook.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   			${createdDate}						
						</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td>
							${dayBook.deposit_to}
						</td>
					   <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						 </tr>
						</c:if>
						</c:if> 
				</c:forEach>

			</tbody>
			<tfoot style='background-color: #eee'>
				<tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_debit}" /></b></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_credit}" /></b></td>

				</tr>
			</tfoot>
		</table>
	</div>

	<!-- code for view page for pdf generation -->
	<div class="borderForm" >
	
	
		
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="vtype" data-filter-control="input"
						data-sortable="true">Voucher Number</th>
					<th data-field="type" data-filter-control="input"
						data-sortable="true">Voucher Type</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true">Debit</th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true">Credit</th>


				</tr>
			</thead>
			<tbody>

				<c:set var="row_count_credit" value="0" />
				<c:set var="row_count_debit" value="0" />

				<c:forEach var="dayBook" items="${dayBookList}">
					<tr>
					
						
						<td><fmt:parseDate value="${dayBook.date}"
								pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
								value="${parsedDate}" var="createdDate" type="date"
								pattern="dd-MM-yyyy" /> ${createdDate}</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td><c:choose>
			                    <c:when test='${dayBook.type==7}'> 
			                     <c:if test="${dayBook.particulars=='Deposit'}"> 
			                     	${dayBook.deposit_to}
			                    	</c:if>	
			                    	<c:if test="${dayBook.particulars=='Withdraw'}"> 
			                    		${dayBook.withdraw_from}
			                    	</c:if>
			                    	<c:if test="${dayBook.particulars=='Transfer'}"> 
			                    			${dayBook.withdraw_from}
			                    	</c:if>
			                    </c:when>
			                  
			                    <c:otherwise>	${dayBook.particulars} </c:otherwise>
                  		  </c:choose></td>
						<c:if test="${dayBook.type==1}">
		
		                <c:if test="${dayBook.subLedger!=null}">
		               <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
							</c:if>
							  <c:if test="${dayBook.subLedger==null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
					    </c:if>
						
						</c:if>
						<c:if test="${dayBook.type==2}">
						<c:if test="${dayBook.subLedger!=null}">
						<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						 </c:if>
						<c:if test="${dayBook.subLedger==null}">
	                     <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
					    </c:if>
							
							
						</c:if>
						<c:if test="${dayBook.type==3}">
							 <td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>

						</c:if>
						<c:if test="${dayBook.type==4}">
							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>

						</c:if>
						<c:if test="${dayBook.type==5}">

							<td style="text-align: left;"></td>
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.credit}" /> <c:set var="row_count_credit"
									value="${row_count_credit + dayBook.credit}" /></td>
						</c:if>
						<c:if test="${dayBook.type==6}">
							<td class="tright"><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${dayBook.debit}" /> <c:set var="row_count_debit"
									value="${row_count_debit + dayBook.debit}" /></td>
							<td style="text-align: left;"></td>
						</c:if>

						<c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Deposit'}"> 
						
					    <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						</c:if>	
						<c:if test="${dayBook.particulars=='Withdraw'}"> 
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						
						</c:if>
						<c:if test="${dayBook.particulars=='Transfer'}"> 
					   <td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.debit}" />
								 <c:set var="row_count_debit" value="${row_count_debit + dayBook.debit}" />
					    </td>
					    <td style="text-align: left;">
						</td>
						</c:if>
						</c:if>	

					</tr>

						  
					  <c:if test="${dayBook.type==7}"> 
						<c:if test="${dayBook.particulars=='Transfer'}"> 
						 <tr>
						<td>
							<fmt:parseDate value="${dayBook.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                   			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
                   			${createdDate}						
						</td>
						<td>${dayBook.voucher_Number}</td>
						<td>${dayBook.voucher_Type}</td>
						<td>
							${dayBook.deposit_to}
						</td>
					   <td style="text-align: left;">
						</td>
						<td class="tright">
					    <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dayBook.credit}" />
								 <c:set var="row_count_credit" value="${row_count_credit + dayBook.credit}" />
					    </td>
						 </tr>
						</c:if>
						</c:if> 
				</c:forEach>
			</tbody>
			<tfoot style='background-color: #eee'>
				<tr>
					<td>Total</td>
					<td></td>
					<td></td>
					<td></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_debit}" /></b></td>
					<td class="tright"><b><fmt:formatNumber type="number"
								minFractionDigits="2" maxFractionDigits="2"
								value="${row_count_credit}" /></b></td>

				</tr>
			</tfoot>
		</table>
	</div>
	<!--End of view page code for pdf generation  -->
	<!-- code for button -->
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
				onclick='exportexcel("DayBook-Report")'>Download as Excel</button>
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
	function back() {
		window.location.assign('<c:url value = "dayBookReport"/>');
	}
	/* function pdf(){
		window.location.assign('<c:url value = "pdfdayBookReport"/>');
	} */
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>