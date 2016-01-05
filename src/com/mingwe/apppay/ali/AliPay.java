package com.mingwe.apppay.ali;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.mingwe.iapppay.IPay;

/**
 * 
 * @author mingwei http://blog.csdn.net/u013045971
 * 
 *         This project about WxPay and AliPay by Abstract Factory
 * 
 *         WxPay
 */
public class AliPay implements IPay {
	private String mOrderString;
	private String mSubject;
	private String mBody;
	private String mPrice;
	/**
	 * partner id
	 */
	private String PARTNER = AliConfig.PARTNER;
	/**
	 * seller id
	 */
	private String SELLER = AliConfig.SELLER;
	private String mNotify = AliConfig.NOTIFY_URL;
	/**
	 * Alipay developerment private key, public key
	 */
	final String RSA_PRIVATE = AliConfig.RES_PRIVATE;
	final String RSA_PUBLIC = AliConfig.RES_PUBLIC;

	public AliPay() {

	}

	public void setAliPay(String subject, String body, String order, String price) {
		mSubject = subject;
		mBody = body;
		mOrderString = order;
		mPrice = price;

	}

	@Override
	public void print() {
	}

	public void pay(final Activity activity, final Handler handler, final int flag) {

		if (mSubject.isEmpty()) {
			throw new IllegalArgumentException("alipay order product name is null");
		}
		if (mBody.isEmpty()) {
			throw new IllegalArgumentException("alipay order description body is null");
		}
		if (mOrderString.isEmpty()) {
			throw new IllegalArgumentException("alipay order orderinfo is null");
		}
		if (mPrice.isEmpty()) {
			throw new IllegalArgumentException("alipay order orderorice is null");
		}
		String orderInfo = assembleOrderInfo(mSubject, mBody, mOrderString, mPrice);
		String sign = sign(orderInfo);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 构�?�PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结�?
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = flag;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	public String assembleOrderInfo(String subject, String body, String order, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账�?
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单�?
		orderInfo += "&out_trade_no=" + "\"" + order + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步�?�知页面路径
		orderInfo += "&notify_url=" + "\"" + mNotify + "\"";

		// 服务接口名称�? 固定�?
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型�? 固定�?
		orderInfo += "&payment_type=\"1\"";

		// 参数编码�? 固定�?
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭�??
		// 取�?�范围：1m�?15d�?
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）�?
		// 该参数数值不接受小数点，�?1.5h，可转换�?90m�?
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支�?
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可�?
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，�?配置此参数，参与签名�? 固定�?
		// （需要签约�?�无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
