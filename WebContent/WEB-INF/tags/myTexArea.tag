<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="leng" required="true"%>
<%@ attribute name="rows" required="true"%>
<%@ attribute name="cols" required="true"%>
<%@ attribute name="subgrp" required="false"%>

<spring:message code="write.explanation" var="lbl_some" />

<c:set var="classQuestion" value="revQuestion" />
<c:if test="${fase ne 'REVIEW' || subgrp == true}">
	<c:set var="classQuestion" value="question" />
</c:if>

<div class="${classQuestion}">
	<c:if test="${fase ne 'REVIEW'}">
		<label class="testLabelThin subLabelAdjust"><spring:message
				code="${lbl}" /></label>
		<c:if test="${lbltp != null}">
			<span class="appQuestion"> <span class="appQuestionTooltip">
					<span class="tootltipMessage"> <spring:message
							code="${lbltp}" />
				</span> <span class="tootltipClose"></span>
			</span>
			</span>
		</c:if>
		<div id="ERROR_${id}" class="adjust"></div>

		<spring:bind path="${vari}">
			<c:if test="${status.error}">
				<div class="appError">
					<span class="appErrorIcon"> <span class="appErrorTooltip">
							<span class="tootltipMessage"><form:errors
									path="${status.expression}" /></span>
					</span>
					</span>
				</div>
			</c:if>
			<form:textarea placeholder="${lbl_some}" id="${id}"
				path="${status.expression}" class="fillInput" maxlength="${leng}"
				rows="${rows}" cols="${cols}" />

		</spring:bind>
	</c:if>
	<c:if test="${fase eq 'REVIEW'}">
		<div class="revInfo">
			<div class="revItemLeft">
				<div class="revLabelFix">
					<label class="fillLabel"><spring:message code="${lbl}" /></label>
				</div>
				<div id="LABEL${id}" class="revAnwser">
					<span><spring:bind path="${vari}">
						${status.value}
					</spring:bind></span>
				</div>
			</div>
			<c:if test="${subgrp == null || subgrp == ''}">
				<div id="ID_BTED${id}" class="revItemButton">
					<a class="midBt" id="BTED${id}"><spring:message code="btn_edit" /></a>
				</div>
			</c:if>
		</div>
		<div id="ERROR_${id}"></div>
		<spring:bind path="${vari}">
			<div id="FIELD${id}" class="field-hidden">
				<form:textarea placeholder="${lbl_some}" id="${id}"
					path="${status.expression}" class="fillInput" maxlength="${leng}"
					rows="${rows}" cols="${cols}" />
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
		
		<div id="ID_BTSV${id}">
			<div class="revItemButton revSave">
				<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>
			</div>
			<div class="revItemButton revSave">
				<a class="midBt" id="BTCN${id}"><spring:message
						code="btn_cancel" /></a>
			</div>
		</div>
	</c:if>
</div>
<script>
	/*id edit*/
	$("#ID_BTSV${id}").hide();
	$("#FIELD${id}").hide();

	$("#BTED${id}").click(function() {
		prev["${id}"] = $("#${id}").val();
		$("#ID_BTSV${id}").show();
		$("#ID_BTED${id}").hide();
		$("#FIELD${id}").show();
		$("#LABEL${id}").hide();
	});

	$('#BTCN${id}').click(function() {
		$("#${id}").val(prev["${id}"]);
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").show();

		delete prev["${id}"];
	});

	$('#BTSV${id}').click(function() {
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").empty().html($("#${id}").val()).show();
		delete prev["${id}"];
	});
</script>
