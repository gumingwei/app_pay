package com.mingwe.apppay.wx;

import com.mingwe.iapppay.IPay;
import com.mingwe.iapppay.IPayFactory;

public class WxFactory implements IPayFactory {

	public void printlog() {
	}

	@Override
	public IPay createPayEntry() {
		return new WxPay();
	}
}
