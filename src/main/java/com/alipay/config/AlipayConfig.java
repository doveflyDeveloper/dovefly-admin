package com.alipay.config;

import com.deertt.frame.base.project.ApplicationConfig;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	public static String app_id = ApplicationConfig.ALIPAYCONFIG_APP_ID;
	
	public static String alipay_public_key = ApplicationConfig.ALIPAY_PUBLIC_KEY;
	
	public static String alipay_private_key = ApplicationConfig.ALIPAY_PRIVATE_KEY;
	
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = ApplicationConfig.ALIPAYCONFIG_PARTNER;
	
	// 收款支付宝账号，一般情况下收款账号就是签约账号
	public static String seller_email = ApplicationConfig.ALIPAYCONFIG_SELLER_EMAIL;
	
	// 账户名，个人支付宝账号是真实姓名 公司支付宝账号是公司名称
	public static String seller_name = ApplicationConfig.ALIPAYCONFIG_SELLER_NAME;
	
	// 商户的私钥
	public static String key = ApplicationConfig.ALIPAYCONFIG_KEY;
	
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = ApplicationConfig.ALIPAYCONFIG_LOG_PATH;
	
	// 实时到账支付服务
	public static String service_pay = "create_direct_pay_by_user";
	
	// 实时到账批量退款服务有密退款
	public static String service_refund_pwd = "refund_fastpay_by_platform_pwd";
	
	// 实时到账批量退款服务无密退款
	public static String service_refund_nopwd = "refund_fastpay_by_platform_nopwd";
	
	//批量付款到支付宝账户有密接口
	public static String service_batch_trans_notify = "batch_trans_notify";
	
	//批量付款到支付宝账户有密接口
	public static String service_account_page_query = "account.page.query";
	
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "MD5";

}
