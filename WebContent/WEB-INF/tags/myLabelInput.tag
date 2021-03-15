<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="fase" required="true"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="divi" required="false"%>
<%@ attribute name="leng" required="false"%>
<%@ attribute name="divChild" required="false"%>
<%@ attribute name="tblIDChild" required="false"%>
<%@ attribute name="contrID" required="false"%>

<c:choose>
	<c:when test="${fase ne 'REVIEW'}">
		<c:if test="${divi == 'true'}">
			<div class="fillFormDivision"></div>
		</c:if>

		<div class="fillFormSubTitle">
			<div id="${id}">
				<spring:message code="${lbl}" />
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="revQuestion">
			<div class="revInfo">
				<div class="revItemLeft">
					<c:if test="${divi == 'true'}">
						<div class="fillFormDivision"></div>
					</c:if>
					<div class="fillFormSubTitle">
						<div id="${id}">
							<spring:message code="${lbl}" />
						</div>
					</div>
				</div>
				<div id="ID_BTED${id}" class="revItemButton">
					<a class="midBt" id="BTED${id}"><spring:message code="btn_edit" /></a>
				</div>
			</div>

			<!-- <div id="ID_BTSV${id}" class="revSave">
				<div class="editBtSave">
					<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>
				</div>
				<div class="editBtCancel">
					<a class="textBt" id="BTCN${id}"><spring:message
							code="btn_cancel" /></a>
				</div>
			</div>-->
		</div>
	</c:otherwise>
</c:choose>
<script>
$(function(){
	var id = "<c:out value='${id}'/>";

	if (("${fase}" == 'REVIEW') && ("${divChild}" != '')) {
		disableAllChildV2("${divChild}");
		
		var add = $('<div id="ID_BTSV${id}" class="revSave">'+
			    '<div class="editBtSave">'+
			       '<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>'+
			    '</div>'+
			    '<div class="editBtCancel">'+
			       '<a class="textBt" id="BTCN${id}"><spring:message code="btn_cancel" /></a>'+
			    '</div>'+
			'</div>');

		if($('#${divChild} > .qSubsection').length){
			$("#${divChild} .qSubsection").append(add);
		}
		else{
			$("#${divChild}").append(add);
		}
	}
	
	/*id edit*/
	$("#ID_BTSV${id}").hide();
	
	$('#BTED${id}').click(function() {
		$("#ID_BTSV${id}").show();
		$("#ID_BTED${id}").hide();
		$("#LABEL${id}").hide();
		$("#FIELD${id}").show();
		if ("${divChild}" != '') {
			$("#SUBTBL${id}").show();
			
			var url = "${contrID}";
			if (url.indexOf('?') >= 0) {
				url += "&";
			} else {
				url += "?";
			}

			$("#${tblIDChild}").load(url + "edit=1");
			
			savePreviousValues("${divChild}");
			enableAllChildV2("${divChild}");
		}
	});

	$('#BTCN${id}').click(function() {
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#LABEL${id}").show();
		$("#FIELD${id}").hide();
		$("#SUBTBL${id}").hide();
		if ("${divChild}" != '') {
			restorePreviousValues("${divChild}");
			disableAllChildV2("${divChild}");
			
			var url = "${contrID}";
			if (url.indexOf('?') >= 0) {
				url += "&";
			} else {
				url += "?";
			}

			$("#${tblIDChild}").load(url + "edit=0");
		}
		delete prev["${id}"];
	});

	$('#BTSV${id}').click(function() {
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#SUBTBL${id}").hide();
		if ("${divChild}" != '') {
			saveNewValues("${divChild}", "${prefix}");
			disableAllChildV2("${divChild}");
			
			var url = "${contrID}";
			if (url.indexOf('?') >= 0) {
				url += "&";
			} else {
				url += "?";
			}
			$("#${tblIDChild}").load(url + "edit=0");
		}
		delete prev["${id}"];
	});
});
</script>