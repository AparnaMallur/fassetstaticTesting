<table id="Hiddentable">
			
					<tr>
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;">Cash Flow Summary</td>
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
				<th data-field="particulars" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="openingbalance" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="debit" data-filter-control="input"
						data-sortable="true"></th>
					<th data-field="credit" data-filter-control="input"
						data-sortable="true"></th>
			</tr>
