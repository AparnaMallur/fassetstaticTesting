<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="leng" required="false"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="lstItems" required="true" type="java.util.List"%>
<%@ attribute name="readOnly" required="false"%>
<%@ attribute name="subgrp" required="false"%>
<%@ attribute name="subq" required="false"%>
<%@ attribute name="divChild" required="false"%>
<%@ attribute name="divChildren" required="false"%>
<%@ attribute name="content" required="false"%>
<%@ attribute name="errorClass" required="false"%>

<c:set var="error" value="not_adjust" />
<c:if test="${errorClass == 'true'}">
	<c:set var="error" value="adjust" />
</c:if>

<c:set var="arrayVari" value="${fn:split(vari, ',')}" />

<c:choose>
	<c:when test="${subq == true && fase ne 'REVIEW'}">
		<div class="floatInputTravel travelAdjust">
			<label class="testLabelThin "><spring:message code="${lbl}" /></label>
			<spring:bind path="${vari}">
				<form:select id="${id}" path="${status.expression}"
					items="${lstItems}" itemValue="id" itemLabel="label"
					class="fillInput" />
				<c:if test="${status.error}">
					<div class="appError">
						<span class="appErrorIcon"> <span class="appErrorTooltip">
								<span class="tootltipMessage"><form:errors
										path="${status.expression}" /></span>
						</span>
						</span>
					</div>
				</c:if>
			</spring:bind>
		</div>
		<div id="ERROR_${id}" class="${error}"></div>
	</c:when>
	<c:otherwise>
		<c:set var="classQuestion" value="revQuestion" />
		<c:if
			test="${fase ne 'REVIEW' || readOnly == 'true' || subgrp == 'true'}">
			<c:set var="classQuestion" value="question" />
		</c:if>
		<div class="${classQuestion}">
			<c:if test="${fase ne 'REVIEW'}">
				<label class="fillLabel"><spring:message code="${lbl}" /></label>
				<c:if test="${lbltp != null}">
					<span class="appQuestion"> <span class="appQuestionTooltip">
							<span class="tootltipMessage"><spring:message
									code="${lbltp}" /></span><span class="tootltipClose"></span>
					</span></span>
				</c:if>
				<spring:bind path="${vari}">
					<form:select id="${id}" path="${status.expression}"
						items="${lstItems}" itemValue="id" itemLabel="label"
						class="fillInput" />
					<c:if test="${status.error}">
						<div class="appError">
							<span class="appErrorIcon"> <span class="appErrorTooltip">
									<span class="tootltipMessage"><form:errors
											path="${status.expression}" /></span>
							</span>
							</span>
						</div>
					</c:if>
				</spring:bind>
				<div id="ERROR_${id}" class="${error}"></div>
			</c:if>
			<c:if test="${fase eq 'REVIEW'}">
				<div class="revInfo">
					<div class="revItemLeft">
						<div class="revLabelFix">
							<label class="fillLabel"><spring:message code="${lbl}" /></label>
						</div>
						<div id="LABEL${id}" class="revAnwser">
							<span> <spring:bind path="${vari}">
								${lstItems[status.value].label} 
						</spring:bind></span>
						</div>
					</div>
					<c:if
						test="${(readOnly == null || readOnly == '') && (subgrp == null || subgrp == '')}">
						<div id="ID_BTED${id}" class="revItemButton">
							<a class="midBt" id="BTED${id}"><spring:message
									code="btn_edit" /></a>
						</div>
					</c:if>
				</div>
				<spring:bind path="${vari}">
					<div id="FIELD${id}" class="field-hidden">
						<form:select id="${id}" path="${status.expression}"
							items="${lstItems}" itemValue="id" itemLabel="label"
							class="fillInput" />
					</div>
					<c:if test="${status.error}">
						<div class="appError">
							<span class="appErrorIcon"> <span class="appErrorTooltip">
									<span class="tootltipMessage"><form:errors
											path="${status.expression}" /></span>
							</span>
							</span>
						</div>
					</c:if>
				</spring:bind>
				<div id="ERROR_${id}" class="${error}"></div>
				<c:if test="${(divChildren == '' || divChildren == null) && (divChild == '' || divChild == null)}">
					<div id="ID_BTSV${id}" class="revSave">
						<div class="editBtSave">
							<a class="midBt" id="BTSV${id}"><spring:message
									code="btn_save" /></a>
						</div>
						<div class="editBtCancel">
							<a class="textBt" id="BTCN${id}"><spring:message
									code="btn_cancel" /></a>
						</div>
					</div>
				</c:if>
			</c:if>
		</div>
	</c:otherwise>
</c:choose>
<script>
$(function() {
	$("#${id}").chosen({
		allow_single_deselect : true,
		width: 100
	});
	
	if ("${fase}" == 'REVIEW') {
		var add =$('<div id="ID_BTSV${id}" class="revSave">'+
			    '<div class="editBtSave">'+
			       '<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>'+
			    '</div>'+
			    '<div class="editBtCancel">'+
			       '<a class="textBt" id="BTCN${id}"><spring:message code="btn_cancel" /></a>'+
			    '</div>'+
			'</div>');
		if ("${divChildren}" != '' ){
				$("#${divChildren} .qSubsection").append(add);
				disableAllChildV2("${id}"+$("#${id}").val());
		} else {
			if("${divChild}" != '') {
				disableAllChildV2("${divChild}");
				
				if ($('#${divChild} .qSubsection').length) {
					$("#${divChild} .qSubsection").append(add);
				} else {
					$("#${divChild}").append(add);
				}
			}
		}
	}
	
	/*id edit*/
	$("#ID_BTSV${id}").hide();
	$("#FIELD${id}").hide();

	$("#BTED${id}").click(function() {
		prev["${id}"] = $("#${id}").val();
		$("#ID_BTSV${id}").show();
		$("#ID_BTED${id}").hide();
		$("#FIELD${id}").show();
		$("#LABEL${id}").hide();
		
		if("${divChildren}" != ''){
				savePreviousValues("${id}"+$("#${id}").val());
				
				$("#${id} option").each(function(){
					enableAllChildV2("${id}"+$(this).val());
				});
				//enableAllChildV2("${id}"+$("#${id}").val());
		} else {
			if("${divChild}" != ''){
				savePreviousValues("${divChild}");
				enableAllChildV2("${divChild}");
			}
		}
	});

	$('#BTCN${id}').click(function() {
		
		$("#${id}"+$("#${id}").val()).hide();
		$("#${id}").val(prev["${id}"]);
		$("#${id}"+prev["${id}"]).show();
		$("#${id}").trigger("chosen:updated");
		
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").show();
		if("${divChildren}" != ''){
			restorePreviousValues("${id}"+prev["${id}"]);
			disableAllChildV2("${id}"+prev["${id}"]);
		} else {
			if("${divChild}" != ''){
				restorePreviousValues("${divChild}");
				disableAllChildV2("${divChild}");
			}
		}
	});

	$('#BTSV${id}').click(function() {
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();

		$("#LABEL${id}").empty().html(
				$("#${id} option:selected").text()).show();
		
		if("${divChildren}" != ''){
			saveNewValues("${id}"+$("#${id} option:selected").val(), "${prefix}");
			disableAllChildV2("${id}"+$("#${id} option:selected").val());
		} else {
			if ("${divChild}" != '') {
				saveNewValues("${divChild}", "${prefix}");
				disableAllChildV2("${divChild}");
			}
		}
	});
});
</script>