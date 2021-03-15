<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
	
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<script type="text/javascript" src="${valid}"></script>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Product</h3>
	<a href="homePage">Home</a> » <a href="productList">Product</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">	
	<div class="fassetForm">
		<form:form id="productForm" action="product" method="post" commandName = "product">									
			<form:hidden path="product_id"/>
			<form:hidden path="tax_id" id = "tax_id"/>	
			<c:if test="${((role == '2') || (role == '3') ||(role == '4'))}">
			<div class="row">			
				<div class="col-md-3 control-label"><label>Company Name<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="company_id" class="logInput">
						<form:option value="0" label="Select Company Name"/>	
						<c:forEach var = "company" items = "${companyList}">													
							<form:option value = "${company.company_id}">${company.company_name}</form:option>	
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="company_id" /></span>
				</div>
			</div>
			</c:if>			
			<div class="row">
				<div class="col-md-3 control-label"><label>Product Name<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path="product_name" class="logInput" id = "product_name" placeholder="Product Name" maxlength="30" />
					<span class="logError"><form:errors path="product_name" /></span>
				</div>
			</div>
			<div class="row">				
				<div class="col-md-3 control-label"><label>Product Type<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="type" class="logInput">
						<form:option value="0" label="Select product type"/>
						<form:option value="1" label="Goods"/>
						<form:option value="2" label="Services"/>														
					</form:select>
					<span class="logError"><form:errors path="type" /></span>
				</div>
			</div>			
			<div class="row">
				<div class="col-md-3 control-label"><label>Unit of Measurement<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="unit" class="logInput">
						<form:option value="0" label="Select Unit of Measurement"/>	
						<c:forEach var = "uom" items = "${uomList}">													
							<form:option value = "${uom.uom_id}">${uom.unit}</form:option>	
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="unit" /></span>
				</div>
			</div>
			<div class="row">	
						
				<div class="col-md-3 control-label"><label>Tax Type</label></div>
				<div class = "col-md-9">
					<form:select path="tax_type" class="logInput" id = "tax_type" onChange="checktype(this.value)">	
					   <form:option value = "0">Select Tax Type</form:option>
						<form:option value = "1">GST</form:option>					
						<form:option value = "2">VAT</form:option>
					</form:select>
					<span class="logError"><form:errors path="tax_type" /></span>
				</div>
			</div>
			<div id='vat-div' style='display:none'>
						<div class="row">
						<div class="col-md-3 control-label"><label>Tax Name<span>*</span></label></div>
						<div class = "col-md-9">
							<form:select path="vat_id" class="logInput" onChange="getvalue(this.value)">
								<form:option value="0" label="Select Tax"/>	
								<c:forEach var = "tax" items = "${taxList}">													
									<form:option value = "${tax.tax_id}">${tax.tax_name}</form:option>	
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="vat_id" /></span>
						</div>
					</div>
					<div class="row">				
						<div class="col-md-3 control-label"><label>VAT</label></div>
						<div class = "col-md-9">
								<input type='text' id='vat-value' readonly="true">
						</div>
					</div>
					<div class="row">				
						<div class="col-md-3 control-label"><label>CST</label></div>
						<div class = "col-md-9">
								<input type='text' id='cst-value' readonly="true">
						</div>
					</div>
					<div class="row">				
						<div class="col-md-3 control-label"><label>EXCISE</label></div>
						<div class = "col-md-9">
								<input type='text' id='excise-value' readonly="true">
						</div>
					</div>
			
			</div>
			<div id='gst-div'>
				<div class="row">				
					<div class="col-md-3 control-label"><label>HSN/SAC Number<span>*</span></label></div>
					<div class = "col-md-9">
						<form:input path="hsn_san_no" class="logInput" id = "hsn_san_no" placeholder="Enter 4 charecter of HSN/SAC" />
						<span class="logError"><form:errors path="hsn_san_no" /></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 control-label"><label>SGST<span>*</span></label></div>
					<div class = "col-md-9">
						<input class="logInput" type='text' id = "sgst" placeholder="SGST" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 control-label"><label>CGST<span>*</span></label></div>
					<div class = "col-md-9">
						<input class="logInput"  type='text' id = "cgst" placeholder="CGST" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 control-label"><label>IGST<span>*</span></label></div>
					<div class = "col-md-9">
						<input class="logInput"  type='text' id = "igst" placeholder="IGST" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 control-label"><label>CESS<span>*</span></label></div>
					<div class = "col-md-9">
						<input class="logInput"  type='text' id = "stat_comp_tax" placeholder="CESS" />
					</div>				
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-9">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div>	
			
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>	
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
		
	    disabled();
	    
	    $("#product_name").keypress(function(e) {
			if (!lettersAndHyphennoquote(e)) {
				return false;
			}
		});
	});
	
	$("#hsn_san_no").autocomplete({
	    source: function (request, response) {
	    	
	    	if($("#hsn_san_no").val().length>=4)
	    	{
				$.ajax({
	           		type: "GET",
	            		url: '<c:url value="getHSNSACNo?term="/>'+request.term,                     		
	            		cache: false,                     		
	            		success: function (data) {
	            			
	            	
	            			response($.map(data, function(item){	  
	            				            				
	                  			return{
	                  				label:"HSN/SAC-No:"+item.hsc_sac_code+" Chapter-No:"+item.chapterNo+" Schedule-Name:"+item.scheduleName+" GST-Rate:"+item.igst+"%",
	                  				value:item.hsc_sac_code,
	                  				cgst:item.cgst,
	                 				igst:item.igst,
	                 				sgst:item.sgst,
	                 				scc:item.state_comp_cess,
	                 				taxId:item.tax_id
	                  			}
	                  		
	        	            		
	            			}));                       		
	        		},
	            	error: function (e) {
	            		//alert(e);
	                	alert("Error while retrieving HSN/SAC number list(Connection failed).");
	            	}
	        	});	             	
	    	}
    	},
    	select: function (event, ui) {
    		$("#igst").val(ui.item.igst);
    		$("#cgst").val(ui.item.cgst);
    		$("#sgst").val(ui.item.sgst);
    		$("#stat_comp_tax").val(ui.item.scc);
    		$("#tax_id").val(ui.item.taxId);
	  	},
    	minLength: 2 
	});
	function getgstvalue(hsn)
	{		
			$.ajax({
           		type: "GET",
            		url: '<c:url value="getHSNSACNo?term="/>'+hsn,                     		
            		cache: false,                     		
            		success: function (data) {
            			$.map(data, function(item){
            				
            				$("#igst").val(item.igst);
            	    		$("#cgst").val(item.cgst);
            	    		$("#sgst").val(item.sgst);
            	    		$("#stat_comp_tax").val(item.state_comp_cess);
            	    		$("#tax_id").val(item.taxId);            				
            			});
           			                     		
        		},
            	error: function () {
                	alert("Error while retrieving HSN/SAC number list(Connection failed).");
            	}
        	});	  		
	}
	$("#product_category").change(function(){
		var cat = $("#product_category").val();
		if(cat == 1){
			disabled();
		}
		else if(cat == 2){
			enabled();
		}
	});
	
	function cancel(){
		window.location.assign('<c:url value = "productList"/>');	
	}
	
	function disabled(){
		$("#igst").attr('disabled', 'disabled');
		$("#cgst").attr('disabled', 'disabled');
		$("#sgst").attr('disabled', 'disabled');
		$("#stat_comp_tax").attr('disabled', 'disabled');
	}
	
	function enabled(){
		$("#igst").removeAttr('disabled');
		$("#cgst").removeAttr('disabled');
		$("#sgst").removeAttr('disabled');
		$("#stat_comp_tax").removeAttr('disabled');

		$("#hsn_san_no").val('');
		$("#igst").val('');
		$("#cgst").val('');
		$("#sgst").val('');
		$("#stat_comp_tax").val('');		
	}
	function checktype(type){
		if(type==1)
		{
			document.getElementById('gst-div').style.display="block";
			document.getElementById('vat-div').style.display="none";
		}
		else
		{
			document.getElementById('gst-div').style.display="none";
			document.getElementById('vat-div').style.display="block";	
		}
	
	}
	function getvalue(vat){
		<c:forEach var = "vats" items = "${taxList}">
				if(vat=="${vats.tax_id}")
					{
						document.getElementById('vat-value').value="${vats.vat}";
						document.getElementById('cst-value').value="${vats.cst}";
						document.getElementById('excise-value').value="${vats.excise}";
					}
		</c:forEach>
	}
	$( document ).ready(function() {
	var type =document.getElementById('tax_type').value;
	var vat =document.getElementById('vat_id').value;
	var hsn =document.getElementById('hsn_san_no').value;

		if(type==1)
		{
			document.getElementById('gst-div').style.display="block";
			document.getElementById('vat-div').style.display="none";
		}
		else
		{
			document.getElementById('gst-div').style.display="none";
			document.getElementById('vat-div').style.display="block";	
		}
		
		getvalue(vat);					
		if(hsn.trim() != ""){ 
			getgstvalue(hsn);
		}	
	
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>