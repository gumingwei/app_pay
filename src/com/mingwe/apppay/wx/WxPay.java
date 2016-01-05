package com.mingwe.apppay.wx;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.mingwe.iapppay.IPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @author mingwei http://blog.csdn.net/u013045971
 * 
 *         This project about WxPay and AliPay by Abstract Factory
 * 
 *         WxPay
 */
public class WxPay implements IPay {

	private PayReq mPayReq;
	/**
	 * key
	 */
	private final String KEY = WxPayConfig.KEY;
	private IWXAPI mIwxapi;

	public WxPay() {

	}

	/**
	 * 
	 * @param context
	 *            init WxPay need parametar
	 */
	public void setWxPay(Context context) {
		mPayReq = new PayReq();
		mIwxapi = WXAPIFactory.createWXAPI(context, null);
	}

	@Override
	public void print() {

	}

	/**
	 * 
	 * @param context
	 *            pay send data to WX
	 */
	public void pay(String info) {
		Map<String, String> hashMap=decodeXml(info);
		if(hashMap==null){
			throw new IllegalArgumentException("XML pay data is null");
		}
		if (mPayReq == null) {
			throw new IllegalArgumentException("wxpay PayReq is null");
		}
		if (mIwxapi == null) {
			throw new IllegalArgumentException("wxpay IWXAPI is null");
		}
		mPayReq.appId = hashMap.get(WxPayConfig.APP_ID);
		mPayReq.partnerId = hashMap.get(WxPayConfig.MCH_ID);
		mPayReq.prepayId = hashMap.get(WxPayConfig.PREPAY_ID);
		mPayReq.packageValue = "Sign=WXPay";
		mPayReq.nonceStr = genNonceStr();
		mPayReq.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair(WxPayConfig.APP_ID, mPayReq.appId));
		signParams.add(new BasicNameValuePair(WxPayConfig.ORDER_NONCE_STR, mPayReq.nonceStr));
		signParams.add(new BasicNameValuePair(WxPayConfig.ORDER_PACKAGE, mPayReq.packageValue));
		signParams.add(new BasicNameValuePair(WxPayConfig.ORDER_PARTNERID, mPayReq.partnerId));
		signParams.add(new BasicNameValuePair(WxPayConfig.ORDER_PREPAY_ID, mPayReq.prepayId));
		signParams.add(new BasicNameValuePair(WxPayConfig.ORDER_TIMESTAMP, mPayReq.timeStamp));
		mPayReq.sign = genAppSign(signParams);
		mIwxapi.registerApp(hashMap.get(WxPayConfig.APP_ID));
		mIwxapi.sendReq(mPayReq);
	}

	/**
	 * 
	 * @return get a nonce String use sign
	 */
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(KEY);

		// this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
		}
		return null;

	}

}
