<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>

<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Inventory Quantity Adjustment</h3>
	<a href="homePage">Home</a> » <a href="inventoryQuantityList">Inventory Quantity Adjustment</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="inventoryQuantityForm" action="inventoryQuantity" method="post" commandName="inventoryQuantity" onsubmit = "return setDate()">
		 <!-- Small modal -->
                  <div id="year-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                       <div class="modal-dialog ">
                            <div class="modal-content">
                                <div class="modal-header">
                                     <h4 class="modal-title" id="mySmallModalLabel">Select Accounting Year</h4>
                                 </div>
				                 <div class="modal-body">
									<c:set var="first_year" value="0" />		   				
										<c:forEach var="year" items="${accountingYearList}">
										<c:choose>
												<c:when test="${first_year==0}">
											<form:radiobutton path="yearId" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
												</c:when>
												<c:otherwise>
												<form:radiobutton path="yearId" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
												</c:otherwise>
										</c:choose>
										<c:set var="first_year" value="${first_year+1}" />
										</c:forEach>
				                </div>
				                <div class="modal-footer">
				                               <button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>Save Year</button>
				                </div>
                           </div><!-- /.modal-content -->
                      </div>
                </div>
            <!-- /.modal -->  
            
				<form:input path="inventory_adj_id" hidden="hidden" />
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Date<span>*</span></label>
				</div>
				<div class="col-md-9">
					<input type = "text" style = "color: black;" id = "date" name = "date"  placeholder = "Date" autocomplete="off">	
					<span class="logError"><form:errors path="dateString" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Product<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:select path="productId" class="logInput" id='productId'>
						<form:option value="0" label="--- Select Product ---" />
						<c:forEach var="product" items="${productList}">
							<c:if test="${product.type==1}">
								<form:option value="${product.product_id}">${product.product_name}</form:option>
							</c:if>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="productId" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-3 control-label">
					<label>Quantity<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:input path="quantity" class="logInput" id="quantity" placeholder="Quantity" />
					<span class="logError"><form:errors path="quantity" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Value Per Product<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:input path="value" class="logInput" id="value" placeholder="Value" />
					<span class="logError"><form:errors path="value" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Stock<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:radiobutton path="is_addition" value="true" checked="checked" />Addition
					<form:radiobutton path="is_addition" value="false" />Substraction
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Remark</label>
				</div>
				<div class="col-md-9">
					<form:textarea path="remark" class="logInput" id = "remark" rows="3"  placeholder="Remark"></form:textarea>
					<span class="logError"><form:errors path="remark" /></span>
				</div>
			</div>
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
$(function() {
    $( "#date" ).datepicker();
    
    
    $("#quantity").keypress(function(e) {
		if (!digitsAndDotOnly(e)) {
			return false;
		}
	});
  });
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [month,day,year].join('/');
}
$( document ).ready(function() {
	var productid = '<c:out value= "${inventoryQuantity.product.product_id}"/>';	
	var yearid = '<c:out value= "${inventoryQuantity.accounting_year_id.year_id}"/>';	
	$("#productId option[value='"+productid+"']").attr("selected", "selected");
	$("#yearId option[value='"+yearid+"']").attr("selected", "selected");
	var dateTest = '<c:out value= "${inventoryQuantity.date}"/>';	
	
	if(dateTest!="")
	{
	 	newdate=formatDate(dateTest);	
		$("#date").datepicker("setDate", newdate);
		$("#date").datepicker("refresh");
	}
	var years = '<c:out value= "${accountingYearList}"/>';	
	var transaction_id = '<c:out value= "${inventoryQuantity.inventory_adj_id}"/>';	
	var ysize = '<c:out value= "${yearList.size()}"/>';	

		if((years!="")&&(transaction_id==""))
		{
			if(ysize!=1){
				$('#year-model').modal({
			    	show: true,
			   	});	 
		 		var j=0;
		 		<c:forEach var = "year" items = "${accountingYearList}">
			    if(j==0)
			    	{			
			    	    startdate1='${year.start_date}';
			    	    enddate1='${year.end_date}';
			    	setdatelimit('${year.start_date}','${year.end_date}');
			    	}
			    j++;
			     </c:forEach>
			}
			else
			{
				<c:forEach var = "year" items = "${accountingYearList}">
		    	        startdate1='${year.start_date}';
		    	        enddate1='${year.end_date}';
					   setdatelimit('${year.start_date}','${year.end_date}');
				</c:forEach>	
				
			}	
		}
		else
		{
		var savedyear='<c:out value= "${inventoryQuantity.accounting_year_id.year_id}"/>';
		<c:forEach var = "year" items = "${accountingYearList}">
			if("${year.year_id}"==savedyear)
			{
		    	  startdate1='${year.start_date}';
		    	  enddate1='${year.end_date}';
		       setdatelimit('${year.start_date}','${year.end_date}');
		      
			}  
		</c:forEach>   
		
		}
});
	function saveyearid()
	{
		 $('#year-model').modal('hide');
	}
	$(function() {		
		setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	function setdatelimit(startdate,enddate)
	{
		startdate=formatDate(startdate);
		enddate=formatDate(enddate);			
		curdate=formatDate(new Date());		
		var maxdate=curdate;		
		if(curdate < enddate)
			maxdate=enddate;	
		$("#date").datepicker({dateFormat: 'dd-mm-yy'});
		 $('#date').datepicker("option", { minDate: startdate, 
            maxDate: enddate });
	}
	function cancel(){
		window.location.assign('<c:url value = "inventoryQuantityList"/>');	
	}
	function setDate(){
			startdate1=formatDate(startdate1);
			enddate1=formatDate(enddate1);
			
		var dateString = $("#date").val();	
		if(dateString!="")
			{			
			var dateStringnew=process(dateString);
			var startdatenew=process(startdate1);
			var enddatenew=process(enddate1);
			
				if(dateStringnew < startdatenew)
				  {
					alert("Select Date between "+startdate1+" and "+enddate1);
				  		return false;
				  }
				  else if(dateStringnew > enddatenew)
				  {
					  alert("Select Date between "+startdate1+" and "+enddate1);
				  		return false;
				  }
				  else
					{					  
						return true;
					}
			}
	}

	function process(date){
	  var parts = date.split("/");
	   var date = new Date(parts[0] + "/" + parts[1] + "/" + parts[2]);
	   return date.getTime();
	}

</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>