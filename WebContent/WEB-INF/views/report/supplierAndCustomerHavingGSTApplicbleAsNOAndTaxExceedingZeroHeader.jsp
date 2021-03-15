	<table id="Hiddentable">
			
	<tr>
					<td></td>
					<td></td>
					<td style="color: blue; margin-left: 50px;">Supplier And Customer Having GSTApplicable As NO And Tax Exceeding Zero
					</td>
				</tr>

				<tr>
					<td align="center">Company Name:</td>
					<td></td>
					<td align="center">${company.company_name}</td>
				</tr>
				<tr>
					<td align="center">Address:</td>
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
					<td>${from_date}To ${to_date}</td>
				</tr>
				<tr>

					<td colspan='3'>CIN: <c:if
							test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}">
					 ${company.registration_no}
				    </c:if>
					</td>
				</tr>
				</table>
				<table>
				<tr>
				<th data-field="date" data-filter-control="input"
							data-sortable="true">Date</th>
						<th data-field="customer" data-filter-control="input"
							data-sortable="true">Customer Name/Supplier Name</th>
						<th data-field="voucher" data-filter-control="input"
							data-sortable="true">Voucher No</th>
					    <th data-field="voucherType" data-filter-control="input"
							data-sortable="true">Voucher Type</th>	
						<th data-field="Inv no" data-filter-control="input"
							data-sortable="true">Inv no</th>

						<th data-field="labour" data-filter-control="input"
							data-sortable="true" class="tright">Labour Charges</th>
						<th data-field="freight" data-filter-control="input"
							data-sortable="true" class="tright">Freight</th>

						<th data-field="others" data-filter-control="input"
							data-sortable="true" class="tright">Others</th>
					
						<th data-field="cgst" data-filter-control="input"
							data-sortable="true" class="tright">CGST</th>
						<th data-field="sgst" data-filter-control="input"
							data-sortable="true" class="tright">SGST</th>
						<th data-field="igst" data-filter-control="input"
							data-sortable="true" class="tright">IGST</th>

					    <th  class='tright' data-field="Total Value(Invoice value) "
						data-filter-control="input" data-sortable="true" class="tright">Total
						Value(Invoice value)</th>
			</tr>
