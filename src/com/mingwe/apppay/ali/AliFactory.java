package com.mingwe.apppay.ali;

import com.mingwe.iapppay.IPay;
import com.mingwe.iapppay.IPayFactory;


public class AliFactory implements IPayFactory {

	@Override
	public void printlog() {
	}

	@Override
	public IPay createPayEntry() {
		return new AliPay();
	}

}
