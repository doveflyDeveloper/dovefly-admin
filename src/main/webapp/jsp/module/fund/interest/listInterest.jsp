<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/jsp/include/dvGlobal.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>利息流水明细管理</title>
<style type="text/css">
<!--
-->
</style>
<script type="text/javascript">
	var formHelper = new DvFormHelper("interestController");
	
	/** 查询 */
	function query_onclick() {
		formHelper.jSubmit(formHelper.buildQueryAction());
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
					<div class="table_title_tip" rel="<%=request.getContextPath()%>/helpController/getHelp/interest"></div>
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
						<td class="label"><span class="required_red">* </span>起止日期：</td>
						<td class="field">
							<input type="text" name="interest_date_from" class="half_input Wdate" value="${param.interest_date_from }" />&nbsp;&nbsp;~&nbsp;
							<input type="text" name="interest_date_to" class="half_input Wdate" value="${param.interest_date_to }" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="space_h15_div"></div>
		<div class="border_div" >
			<div class="header_div">
				<div class="left_div">
					<div class="table_title">利息流水明细列表&nbsp;</div>
				</div>
				<div class="right_div">
					<div class="right_menu">
					</div>
				</div>
			</div>
			<div class="padding_2_div">
				<table class="list_table">
					<tr>
						<th width="5%">
							<input type="checkbox" name="dv_checkbox_all" value="" onclick="selectAll(this)"/>	
						</th>
						<th width="5%">序号</th>
						<th width="10%">用户姓名</th>
						<th width="10%">当日本金</th>
						<th width="10%">当日利息</th>
						<th width="10%">当日总利息</th>
						<th width="10%">日利率</th>
						<th width="10%">交易概述</th>
					</tr>
					<c:forEach var="bean" varStatus="status" items="${beans }">
					<tr>
						<td>
							<input type="checkbox" name="dv_checkbox" value="<c:out value="${bean.id }"/>" onclick="selectCheckBox(this)"/>
						</td>
						<td>${status.count }</td>
						<td><c:out value="${bean.user_name }"/></td>
						<td><c:out value="${bean.capital_amount }"/></td>
						<td><fmt:formatNumber type="number" pattern="#.#######" value="${bean.interest_amount }" /></td>
						<td><fmt:formatNumber type="number" pattern="#.#######" value="${bean.all_interest_amount }" /></td>
						<td><fmt:formatNumber type="percent" pattern="#.#####%" value="${bean.interest_rate }" /></td>
						<td><c:out value="${bean.brief }"/></td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="page_div">
			<jsp:include page="/jsp/include/page.jsp"/>
		</div>
	</form>
</body>
</html>
