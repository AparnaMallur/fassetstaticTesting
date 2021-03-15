<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
<spring:url value="/resources/js/jspdf.min.js" var="jspdfmin" />
<spring:url value="/resources/js/jspdf.plugin.autotable.js" var="jspdfauto" />
<spring:url value="/resources/js/report_table/manualJV.js" var="tableexport" />
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
	<h3>Manual JV Register</h3>					
	<a href="homePage">Home</a> » <a href="manualJVReport">Manual JV Register</a>
</div>	
<div class="col-md-12" >
	<!-- Excel Start -->
				<div style="display:none" id="excel_report">
				<!-- Date -->
					<table>
						<tr style="text-align:center;"><td></td><td></td><td><b>Manual JV Register</b></td></tr>					
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
				<th  data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>	
				<th  data-field="Vch Type" data-filter-control="input" data-sortable="true" >Voucher Type</th>	
				<th  data-field="Vch No." data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="Debit" data-filter-control="input" data-sortable="true" >Debit</th>
				<th  data-field="Credit" data-filter-control="input" data-sortable="true" >Credit</th>
							</tr>
							</thead>
								<c:forEach var = "mj" items = "${journalRegisterList}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${mj.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
						<td style="text-align: left;">${mj.voucherType}</td>
					<td style="text-align: left;">${mj.voucherNumber}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${mj.creditBalance}</td>
					
				</tr>
				
					<c:forEach var = "details" items = "${mj.subList}">
					<c:if test="${details.debitBalance!=null}"> 
			               <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.debitBalance}</td>
					<td style="text-align: left;"></td>
					
				   </tr>
				     			
			        </c:if>
					</c:forEach>
					
					<c:forEach var = "details" items = "${mj.subList}">
			        <c:if test="${details.creditBalance!=null}"> 
			          <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.creditBalance}</td>
					
				   </tr>          			
			        </c:if>
					</c:forEach>
			</c:forEach>
							<tbody>
					<%-- 		<c:forEach var = "mj" items = "${mJVlist}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${mj.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">Manual Journal</td>
					<td style="text-align: left;">${mj.voucher_no}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					
				</tr>
				
					<c:forEach var = "details" items = "${mj.detailList}">
					<c:if test="${details.subledgerdr!=null}"> 
			               <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerdr.subledger_name}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.dramount}</td>
					<td style="text-align: left;"></td>
					
				   </tr>
				     			
			        </c:if>
					</c:forEach>
					
					<c:forEach var = "details" items = "${mj.detailList}">
			        <c:if test="${details.subledgercr!=null}"> 
			          <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgercr.subledger_name}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.cramount}</td>
					
				   </tr>          			
			        </c:if>
					</c:forEach>
			</c:forEach> --%>
			
			<%-- <c:forEach var = "pjv" items = "${pJVlist}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${pjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">Payroll Auto JV</td>
					<td style="text-align: left;">${pjv.voucher_no}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					
				</tr>
				
			
			</c:forEach> --%>
			
			
			
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
						<td style="color:blue; margin-left: 50px;">Manual JV Register</td>
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
				<th  data-field="Date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>	
				<th  data-field="Vch Type" data-filter-control="input" data-sortable="true" >Voucher Type</th>	
				<th  data-field="Vch No." data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="Debit" data-filter-control="input" data-sortable="true" >Debit</th>
				<th  data-field="Credit" data-filter-control="input" data-sortable="true" >Credit</th>
			</tr>
			
			<tbody>
				<c:forEach var = "mj" items = "${journalRegisterList}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${mj.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
						<td style="text-align: left;">${mj.voucherType}</td>
					<td style="text-align: left;">${mj.voucherNumber}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${mj.creditBalance}</td>
					
				</tr>
				
					<c:forEach var = "details" items = "${mj.subList}">
					<c:if test="${details.debitBalance!=null}"> 
			               <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.debitBalance}</td>
					<td style="text-align: left;"></td>
					
				   </tr>
				     			
			        </c:if>
					</c:forEach>
					
					<c:forEach var = "details" items = "${mj.subList}">
			        <c:if test="${details.creditBalance!=null}"> 
			          <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.creditBalance}</td>
					
				   </tr>          			
			        </c:if>
					</c:forEach>
			</c:forEach>
		<%-- 	<c:forEach var = "mj" items = "${mJVlist}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${mj.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">Manual Journal</td>
					<td style="text-align: left;">${mj.voucher_no}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					
				</tr>
				
					<c:forEach var = "details" items = "${mj.detailList}">
					<c:if test="${details.subledgerdr!=null}"> 
			               <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerdr.subledger_name}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.dramount}</td>
					<td style="text-align: left;"></td>
					
				   </tr>
				     			
			        </c:if>
					</c:forEach>
					
					<c:forEach var = "details" items = "${mj.detailList}">
			        <c:if test="${details.subledgercr!=null}"> 
			          <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgercr.subledger_name}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.cramount}</td>
					
				   </tr>          			
			        </c:if>
					</c:forEach>
			</c:forEach> --%>
			</tbody>
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
				<th  data-field="Particulars" data-filter-control="input" data-sortable="true" >Particulars</th>	
				<th  data-field="Vch Type" data-filter-control="input" data-sortable="true" >Voucher Type</th>	
				<th  data-field="Vch No." data-filter-control="input" data-sortable="true" >Voucher Number</th>
				<th  data-field="Debit" data-filter-control="input" data-sortable="true" >Debit</th>
				<th  data-field="Credit" data-filter-control="input" data-sortable="true" >Credit</th>
			</tr>
			</thead>
			<tbody>
		
			<c:forEach var = "mj" items = "${journalRegisterList}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${mj.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${mj.voucherType}</td>
					<td style="text-align: left;">${mj.voucherNumber}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${mj.creditBalance}</td>
					
				</tr>
				
					<c:forEach var = "details" items = "${mj.subList}">
					<c:if test="${details.debitBalance!=null}"> 
			               <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.debitBalance}</td>
					<td style="text-align: left;"></td>
					
				   </tr>
				     			
			        </c:if>
					</c:forEach>
					
					<c:forEach var = "details" items = "${mj.subList}">
			        <c:if test="${details.creditBalance!=null}"> 
			          <tr>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">${details.subledgerName}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					<td class="tright">${details.creditBalance}</td>
					
				   </tr>          			
			        </c:if>
					</c:forEach>
			</c:forEach>
			<%-- 
				<c:forEach var = "pjv" items = "${pJVlist}">
				<tr>
					<td style="text-align: left;">
						<fmt:parseDate value="${pjv.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
              			<fmt:formatDate value="${parsedDate}" var="createdDate" type="date" pattern="dd-MM-yyyy" />
						${createdDate}					
					</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;">Payroll Auto JV</td>
					<td style="text-align: left;">${pjv.voucher_no}</td>
					<td style="text-align: left;"></td>
					<td style="text-align: left;"></td>
					
				</tr>
				
			
			</c:forEach> --%>
			</tbody>
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
			<button class="fassetBtn" type="button" id='btnExport3' onclick = 'exportexcel("Manual JV Register")'>
				Download as Excel
			</button>
			</c:if>
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</div>
   
</div>
<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "manualJVReport"/>');	
	}
	
	/* function pdf(){
		window.location.assign('<c:url value = "pdfBillsPayable"/>');	
	} */
</script>
	<%@include file="/WEB-INF/includes/footer.jsp" %>