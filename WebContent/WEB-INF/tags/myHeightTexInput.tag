<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="id1" required="true"%>
<%@ attribute name="id2" required="true"%>
<%@ attribute name="vari1" required="true"%>
<%@ attribute name="vari2" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="leng" required="true"%>
<%@ attribute name="img" required="false"%>
<%@ attribute name="subgrp" required="false"%>

<c:set var="classQuestion" value="revQuestion" />
<c:if test="${fase ne 'REVIEW' || subgrp == true}">
	<c:set var="classQuestion" value="question" />
</c:if>

<div class="${classQuestion}">
	<c:if test="${fase ne 'REVIEW'}">
		<label class="fillLabel"><spring:message code="${lbl}" /></label>
		<c:if test="${lbltp != null}">
			<span class="appQuestion"> <span class="appQuestionTooltip">
					<span class="tootltipMessage"> <spring:message
							code="${lbltp}" />
				</span> <c:if test="${img != null && img != ''}">
						<span class="tooltipImage"> <img src="${img}" />
						</span>
					</c:if> <span class="tootltipClose"></span>
			</span>
			</span>
		</c:if>
		<div class="height">
			<spring:bind path="${vari1}">
				<form:input id="${id1}" path="${status.expression}"
					class="fillInput" maxlength="${leng}" type="text" />
			</spring:bind>
			<spring:bind path="${vari2}">
				<form:input id="${id2}" path="${status.expression}"
					class="fillInput" maxlength="${leng}" type="text" />
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
	</c:if>
	<c:if test="${fase eq 'REVIEW'}">
		<div class="revInfo">
			<div class="revItemLeft">
				<div class="revLabelFix">
					<label class="fillLabel"><spring:message code="${lbl}" /></label>
				</div>
				<div id="LABEL${id1}" class="revAnwser">
					<span><spring:bind path="${vari1}">
						${status.value}'
					</spring:bind> <spring:bind path="${vari2}">
						${status.value}''
					</spring:bind></span>
				</div>
			</div>
			<c:if test="${subgrp == null || subgrp == ''}">
				<div id="ID_BTED${id1}" class="revItemButton">
					<a class="midBt" id="BTED${id1}"><spring:message
							code="btn_edit" /></a>
				</div>
			</c:if>
		</div>
		<div class="floatInputTravel travelAdjust">
			<spring:bind path="${vari1}">
				<div id="FIELD${id1}" class="field-hidden">
					<form:input id="${id1}" path="${status.expression}"
						class="fillInput" maxlength="${leng}" type="text" />
				</div>
			</spring:bind>
		</div>
		<div class="floatInputTravel travelAdjust">
			<spring:bind path="${vari2}">
				<div id="FIELD${id2}" class="field-hidden">
					<form:input id="${id2}" path="${status.expression}"
						class="fillInput" maxlength="${leng}" type="text" />
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
		</div>
		<div id="ID_BTSV${id1}">
			<div class="revItemButton revSave">
				<a class="midBt" id="BTSV${id1}"><spring:message code="btn_save" /></a>
			</div>
			<div class="revItemButton revSave">
				<a class="midBt" id="BTCN${id1}"><spring:message
						code="btn_cancel" /></a>
			</div>
		</div>
	</c:if>
</div>
<script>
	/*id edit*/
	$("#ID_BTSV${id1}").hide();
	$("#FIELD${id1}").hide();
	$("#FIELD${id2}").hide();

	$("#BTED${id1}").click(function() {
		prev["${id1}"] = $("#${id1}").val();
		prev["${id2}"] = $("#${id2}").val();
		$("#ID_BTSV${id1}").show();
		$("#ID_BTED${id1}").hide();
		$("#FIELD${id1}").show();
		$("#FIELD${id2}").show();
		$("#LABEL${id1}").hide();
	});

	$('#BTCN${id1}').click(function() {
		$("#${id1}").val(prev["${id1}"]);
		$("#${id2}").val(prev["${id2}"]);
		$("#ID_BTED${id1}").removeAttr("style");
		$("#ID_BTSV${id1}").hide();
		$("#FIELD${id1}").hide();
		$("#FIELD${id2}").hide();
		$("#LABEL${id1}").show();
		delete prev["${id1}"];
		delete prev["${id2}"];
	});

	$('#BTSV${id1}').click(function() {
		$("#ID_BTED${id1}").removeAttr("style");
		$("#ID_BTSV${id1}").hide();
		$("#FIELD${id1}").hide();
		$("#FIELD${id2}").hide();
		$("#LABEL${id1}").empty().html($("#${id1}").val() + "' " + $("#${id2}").val() + "'' ").show();
		delete prev["${id1}"];
		delete prev["${id2}"];
	});
</script>