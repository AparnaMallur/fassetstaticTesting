	<table id="Hiddentable" >

					<tr>
						<td></td>
						<td></td>
						<td style="color: blue;text-align:center; margin-left: 50px;">Purchase Register Report</td>
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
							<%-- <fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
                   			 <fmt:formatDate value="${parsedDate}" var="from_date" type="date" pattern="dd-MM-yyyy"/>
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
                   			 <fmt:formatDate value="${parsedDate}" var="to_date" type="date" pattern="dd-MM-yyyy"/> --%>
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
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Supplier Bill Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="expense type" data-filter-control="input"
							data-sortable="true">Expense Type</th>
						<th data-field="basic amount" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Basic Amount</th>
							<c:if test="${Totalfreight>0}">
						  <th data-field="freight" data-filter-control="input"
							data-sortable="true">Freight</th>
							</c:if>
						<c:if test="${Totallabourcharges>0}">
						  <th data-field="labour" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Labour Cost</th>
							</c:if>
						<c:if test="${TotalOthers>0}">
						  <th data-field="others" data-filter-control="input"
							data-sortable="true">Others</th>
							</c:if>
						<th data-field="taxable value" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Taxable value</th>
						  <c:if test="${TotalCgst>0}">
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" style="text-align:center;">CGST</th>
						</c:if>	
						 <c:if test="${Totalsgst>0}">	
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" style="text-align:center;">SGST</th>
						</c:if>	
							<c:if test="${Totaligst>0}">
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" style="text-align:center;">IGST</th>
							</c:if>	
							<c:if test="${Totalsct>0}">
						<th data-field="sct" data-filter-control="input"
							data-sortable="true" style="text-align:center;">CESS</th>
							</c:if>	
                          <c:if test="${Totalvat>0}">		
						<th data-field="tvat" data-filter-control="input"
							data-sortable="true" style="text-align:center;">VAT</th>
							</c:if>	
							<c:if test="${Totalvatcst>0}">
						<th data-field="tvcst" data-filter-control="input"
							data-sortable="true" style="text-align:center;">CST</th>
							</c:if>	
							<c:if test="${Totalexcise>0}">
						<th data-field="texcise" data-filter-control="input"
							data-sortable="true" style="text-align:center;">EXCISE</th>
							</c:if>	
						<th data-field="invoice value" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Invoice Value</th>
					</tr>