<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
%>	
<html>
<head>
<%@include file="/jsp/include/dvGlobal.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/highcharts/exporting.js"></script>
<style type="text/css">
<!--
-->
</style>
<script type="text/javascript">
	
	function goChangePwd_onclick() {
		top.rightFrame.addTab('26', '我的个人信息', '<%=request.getContextPath()%>/userController/userInfo');
	}
	
	function loadCityManagerInfo() {
		
		$.ajax({
			type: "GET",
			async: true,
			dataType: "json",
			url: context_path + "/reportController/reportByOperationManager",
			data: {},
			success: function(result){
				if(result){//存在
					if(result.success){

						var order_group_by_date = result.data.order_group_by_date;
						var order_data_spline = [];
						var manager = {};
						for (var i = 0; i < order_group_by_date.length; i++) {
							var obj = order_group_by_date[i];
							if (manager.name != obj.manager_name) {
								manager = {name: obj.manager_name, data: [[Date.parse(obj.bill_date), obj.bill_amount]]};
								order_data_spline.push(manager);
							} else {
								manager.data.push([Date.parse(obj.bill_date), obj.bill_amount]);
							}
						}
						
						var trade_group_by_date = result.data.trade_group_by_date;
						var trade_data_spline = [];
						var manager = {};
						for (var i = 0; i < trade_group_by_date.length; i++) {
							var obj = trade_group_by_date[i];
							if (manager.name != obj.manager_name) {
								manager = {name: obj.manager_name, data: [[Date.parse(obj.bill_date), obj.bill_amount]]};
								trade_data_spline.push(manager);
							} else {
								manager.data.push([Date.parse(obj.bill_date), obj.bill_amount]);
							}
						}
						
					    $('#order_line').highcharts({
					        chart: {
					            type: 'spline'
					        },
					        title: {
					            text: '本月店长进货订单量走势图'
					        },
					        subtitle: {
					            text: '(' + (new Date().format("yyyy年MM月")) + ')'
					        },
					        xAxis: {
					            type: 'datetime',
					            dateTimeLabelFormats: {
					                day:"%m-%e",
					                month:"%Y-%m"
					            },
					            title: {
					                text: '日期'
					            }
					        },
					        yAxis: {
					            title: {
					                text: '交易金额 (元)'
					            },
					            min: 0
					        },
					        tooltip: {
					            headerFormat: '<b>{series.name}</b> ',
					            pointFormat: '({point.x:%m-%e})<br>￥{point.y:.2f}'
					        },
					        plotOptions: {
					            spline: {
					                marker: {
					                    enabled: true
					                }
					            }
					        },
						    exporting: {
								url:'<%=request.getContextPath()%>/chartExportController/exportChart'
							},
							credits: {
								enabled: false,
							},
					        series: order_data_spline
					    });
					    
					    $('#trade_line').highcharts({
					        chart: {
					            type: 'spline'
					        },
					        title: {
					            text: '本月店长销售订单量走势图'
					        },
					        subtitle: {
					            text: '(' + (new Date().format("yyyy年MM月")) + ')'
					        },
					        xAxis: {
					            type: 'datetime',
					            dateTimeLabelFormats: {
					                day:"%m-%e",
					                month:"%Y-%m"
					            },
					            title: {
					                text: '日期'
					            }
					        },
					        yAxis: {
					            title: {
					                text: '交易金额 (元)'
					            },
					            min: 0
					        },
					        tooltip: {
					            headerFormat: '<b>{series.name}</b> ',
					            pointFormat: '({point.x:%m-%e})<br>￥{point.y:.2f}'
					        },
					        plotOptions: {
					            spline: {
					                marker: {
					                    enabled: true
					                }
					            }
					        },
						    exporting: {
								url:'<%=request.getContextPath()%>/chartExportController/exportChart'
							},
							credits: {
								enabled: false,
							},
					        series: trade_data_spline
					    });
					}
				}
			}
		});
	}
	
	$(document).ready(function() {
		
		if ('${bean.pwd_reset }' == '1') {//密码重置提醒
			var msg = '您的账号已被管理员重置，<br>请立即前往“<span class="left_ts" style="cursor:pointer;text-decoration:underline;" onclick="goChangePwd_onclick();">我的个人信息</span>”修改密码！';
			$.jBox.messager(msg, '重要提示', 5000);
		}
		
		loadCityManagerInfo();
		
	});
	
</script>
</head>
<body>
	<div style="height: 4px"></div>
	<div class="border_div" >
		<div class="header_div">
			<div class="left_div">
				<div class="table_title">本月店长进货订单量一览&nbsp;</div>
			</div>
			<div class="right_div">
				<div class="right_menu"></div>
			</div>
		</div>
		<div class="padding_2_div">
			<table class="detail_table">
				<tr>
					<td>
					<div id="order_line" style="height:300px; width:98%;"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="space_h15_div"></div>
	<div class="border_div" >
		<div class="header_div">
			<div class="left_div">
				<div class="table_title">本月店长销售订单量一览&nbsp;</div>
			</div>
			<div class="right_div">
				<div class="right_menu"></div>
			</div>
		</div>
		<div class="padding_2_div">
			<table class="detail_table">
				<tr>
					<td>
					<div id="trade_line" style="height:300px; width:98%;"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
