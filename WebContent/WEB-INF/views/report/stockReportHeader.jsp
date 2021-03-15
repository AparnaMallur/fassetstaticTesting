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