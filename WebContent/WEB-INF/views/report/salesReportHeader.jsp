<table id="Hiddentable" >
			
					<tr >
						<td></td>
						<td></td>
						<td style="color:blue; margin-left: 50px;" >Sales Register</td>
						
					</tr>
			
					<tr>
						
						<td align="center" >Company Name: </td>
						<td></td>
						<td align="center" >${company.company_name}</td>
					</tr>
					<tr>
							<td align="center" >Address: </td>
							<td></td>
							<td align="center" >${company.permenant_address}</td>
					</tr>
					<tr>
						<td>
							<fmt:parseDate value="${from_date}" pattern="yyyy-MM-dd" var="parsedDate1" type="date"  />
                   			 <fmt:formatDate value="${parsedDate1}" var="fromdate" type="date" pattern="dd-MM-yyyy"  />
                   			 <fmt:parseDate value="${to_date}" pattern="yyyy-MM-dd" var="parsedDate2" type="date"  />
                   			 <fmt:formatDate value="${parsedDate2}" var="todate" type="date" pattern="dd-MM-yyyy"  />
						From:
						</td>
						<td></td>
						<td>${fromdate} To ${todate}</td>
					</tr>
					<tr>
					
					<td colspan='3'>
					CIN:
					<c:if test="${company.company_statutory_type.company_statutory_id==10 || company.company_statutory_type.company_statutory_id==14}" >
					 ${company.registration_no}
				    </c:if>	
					</td>
					</tr>
				
					<tr>
						<th data-field="date" data-filter-control="input"
							data-sortable="true" >Created Date</th>
					     <th data-field="voucher number" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Number</th>
						<th data-field="particulars" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Particulars</th>
						<th data-field="voucher type" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Voucher Type</th>
					<!-- 
						<th data-field="debit" data-filter-control="input"
							data-sortable="true" >Debit</th> -->
						<th data-field="credit" data-filter-control="input"
							data-sortable="true" style="text-align:right;" >Credit</th>
							
					</tr>
