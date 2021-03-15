<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Payment Report</td>
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
						<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="voucher number" data-filter-control="input"
							data-sortable="true">Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true">Particulars</th>
						<th data-field="Ref NO" data-filter-control="input"
							data-sortable="true">Ref NO.</th>
						<th data-field="bank" data-filter-control="input"
							data-sortable="true">Cash/bank</th>
						<th data-field="chno" data-filter-control="input"
							data-sortable="true" style="text-align:center;">Ch. No./ IB no.</th>
						<th data-field="paymentAmount" data-filter-control="input"
							data-sortable="true" style="text-align: right;" >Payment Amount</th>
					    <th data-field="tds" data-filter-control="input"
							data-sortable="true" style="text-align: center;">TDS</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true" style="text-align: center;">Total Amount</th>							
					</tr>