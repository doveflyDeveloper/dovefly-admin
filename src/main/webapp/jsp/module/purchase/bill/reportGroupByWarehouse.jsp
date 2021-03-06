<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
%>
<html>
<head>
<%@include file="/jsp/include/dvGlobal.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>采购统计</title>
<style type="text/css">
<!--
-->
</style>

<script type="text/javascript">
	var formHelper = new DvFormHelper("purchaseBillReportController");
	
	/** 查询 */
	function query_onclick() {
		if (validateForm()){
			formHelper.jSubmit(formHelper.buildAction("purchaseBillReportController", "reportGroupByWarehouse"));
		}
	}
	
	/** 批量导出 */
	function export_onclick() {
		var ids = findSelections("dv_checkbox", "value");
		if(!ids || ids.length == 0) ids = "all";
		formHelper.submit(formHelper.buildAction("purchaseBillReportController", "exportGroupByWarehouse", ids));
	}
	
	/** 页面加载后需绑定的事件 */
	$(document).ready(function() {
		
	});
</script>
</head>
<body>
	<form id="form" name="form" method="get">
		<div style="height: 4px"></div>
		<div class="border_div" >
			<div class="header_div">
				<div class="left_div">
					<div class="table_title">查询条件&nbsp;</div>
					<div class="table_title_tip" title="不会操作？点我吧！" rel="<%=request.getContextPath()%>/helpController/getHelp/purchase_bill"></div>
				</div>
				<div class="right_div">
					<div class="right_menu">
						<label style="color: #CCC"><input type="checkbox" id="query_state" name="query_state" class="checkbox" value="1" <c:if test='${param.query_state == 1 }'>checked="checked"</c:if>/>更多</label>&nbsp;&nbsp;
						<input type="button" name="query" value="查询" class="button" onclick="query_onclick();"/>
					</div>
				</div>
			</div>
			<div class="padding_2_div">
				<table class="query_table">
					<tr>
						<c:if test="${sessionScope.DV_USER_VO.headquartersRole}">
						<td class="label">货仓：</td>
						<td class="field">
							<dv:select name="warehouse_id" dicKeyword="DIC_SYS_WAREHOUSE" defaultValue="${param.warehouse_id }" ignoreValues="1" hasEmpty="true"/>
						</td>
						</c:if>
						<td class="label"><span class="required_red">* </span>统计周期（订单日期）：</td>
						<td class="field">
							<input type="text" name="bill_date_from" class="half_input Wdate" value="${bill_date_from }" inputName="起始日期" validate="notNull"/>&nbsp;&nbsp;~&nbsp;
							<input type="text" name="bill_date_to" class="half_input Wdate" value="${bill_date_to }" inputName="终止日期" validate="notNull"/>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="space_h15_div"></div>
		<div class="border_div" >
			<div class="header_div">
				<div class="left_div">
					<div class="table_title">统计列表&nbsp;</div>
				</div>
				<div class="right_div">
					<div class="right_menu">
						<input type="button" name="detail" value="导出" class="button" onclick="export_onclick();"/>
					</div>
				</div>
			</div>
			<div class="padding_2_div">
				<table class="list_table">
					<tr>
						<th width="5%">序号</th>
						<th width="10%">城市</th>
						<th width="10%">货仓</th>
						<th width="10%">订单量（笔）</th>
						<th width="10%">订单总额（元）</th>
					</tr>
					<c:forEach var="bean" varStatus="status" items="${beans }">
					<tr>
						<td>${status.count }</td>
						<td><c:out value="${bean.city_name }"/></td>
						<td><c:out value="${bean.warehouse_name }"/></td>
						<td><c:out value="${bean.bill_count }"/></td>
						<td><c:out value="${bean.bill_amount }"/></td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</form>
</body>
</html>
