<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="fase" required="true"%>
<%@ attribute name="lbl" required="true"%>
<%@ attribute name="lbltp" required="false"%>
<%@ attribute name="vari" required="true"%>
<%@ attribute name="subgrp" required="false"%>
<%@ attribute name="img" required="false"%>
<%@ attribute name="divChild" required="false"%>
<%@ attribute name="lblarg" required="false"%>

<c:set var="classQuestion" value="revQuestion" />
<c:if test="${fase ne 'REVIEW' || subgrp == true}">
	<c:set var="classQuestion" value="question" />
</c:if>

<div class="${classQuestion}">
	<c:if test="${fase ne 'REVIEW'}">
		<div>
			<label for="${id}" class="fillLabel">Statement</label>
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
		</div>
		<div class="sectionBox">
			<div class="sectionBoxText">
				<p>
					<spring:message code="${lbl}" arguments="${lblarg}" />
				</p>
			</div>
			<spring:bind path="${vari}">
				<div class="checkbox">
					<label class="check"> <form:checkbox id="${id}"
							path="${status.expression}" class="logCheck" /> <span
						class="checkbox-replacement-helper"></span>I agree
					</label>
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
	</c:if>
	
	<c:if test="${fase eq 'REVIEW'}">
		<div class="revInfo">
			<div class="revItemLeft">
				<div class="revLabelFix">
					<label class="fillLabel"><spring:message code="${lbl}"
							arguments="${lblarg}" /></label>
				</div>
				<div id="LABEL${id}">
					<span><spring:bind path="${vari}">
							<div class="checkbox">
								<label class="check"> <form:checkbox id="${id}"
										path="${status.expression}" class="logCheck" disabled="true" />
									<span class="checkbox-replacement-helper"></span>I agree
								</label>
							</div>
						</spring:bind></span>
				</div>
			</div>
			<c:if test="${subgrp == null || subgrp == ''}">
				<div id="ID_BTED${id}" class="revItemButton">
					<a class="midBt" id="BTED${id}"><spring:message code="btn_edit" /></a>
				</div>
			</c:if>
		</div>
		<spring:bind path="${vari}">
			<div id="FIELD${id}" class="field-hidden">
				<div class="checkbox">
					<label class="check"> <form:checkbox id="${id}"
							path="${status.expression}" class="logCheck" /> <span
						class="checkbox-replacement-helper"></span>I agree
					</label>
				</div>
			</div>
			<c:if test="${status.error}">
				<div class="appError">
					<span class="appErrorIcon"><span class="appErrorTooltip">
							<span class="tootltipMessage"><form:errors path="${status.expression}" /></span>
					</span>
					</span>
				</div>
			</c:if>
		</spring:bind>
		<div id="ID_BTSV${id}" class="revSave">
			<div class="editBtSave">
				<a class="midBt" id="BTSV${id}"><spring:message code="btn_save" /></a>
			</div>
			<div class="editBtCancel">
				<a class="textBt" id="BTCN${id}"><spring:message
						code="btn_cancel" /></a>
			</div>
		</div>
	</c:if>
</div>
<script>
$(function(){
	/*id edit*/
	$("#ID_BTSV${id}").hide();
	$("#FIELD${id}").hide();

	if (("${fase}" == 'REVIEW') && ("${divChild}" != '')) {
		disableAllChildV2("${divChild}");
	}

	$("#BTED${id}").click(function() {
		prev["${id}"] = $("#${id}").val();
		$("#ID_BTSV${id}").show();
		$("#ID_BTED${id}").hide();
		$("#FIELD${id}").show();
		$("#LABEL${id}").hide();
		if ("${divChild}" != '') {
			savePreviousValues("${divChild}");
			enableAllChildV2("${divChild}");
		}
	});

	$('#BTCN${id}').click(function() {
		$("#${id}").val(prev["${id}"]);
		$("#ID_BTED${id}").removeAttr("style");
		$("#ID_BTSV${id}").hide();
		$("#FIELD${id}").hide();
		$("#LABEL${id}").show();
		if ("${divChild}" != '') {
			restorePreviousValues("${divChild}");
			disableAllChildV2("${divChild}");
		}
		delete prev["${id}"];
	});

	$('#BTSV${id}').click(
			function() {
				$("#ID_BTED${id}").removeAttr("style");
				$("#ID_BTSV${id}").hide();
				$("#FIELD${id}").hide();

				var current = $('#${id}').is(':checked')

				$.ajax({
					url : "bundle",
					data : {
						"key" : "radio." + current
					},
					type : "POST",
					success : function(data) {
						$("#LABEL${id}").empty().append(
								"<span>" + data + "</span>").show();
					}
				});

				if ("${divChild}" != '') {
					saveNewValues("${divChild}", "${prefix}");
					disableAllChildV2("${divChild}");
				}

				delete prev["${id}"];
			});
});
</script>