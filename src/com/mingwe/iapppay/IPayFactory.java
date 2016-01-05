package com.mingwe.iapppay;

/**
 * 
 * @author mingwei http://blog.csdn.net/u013045971
 * 
 *         This project about WxPay and AliPay by Abstract Factory
 * 
 *         IPayFactory is Abstract Fractory
 */
public interface IPayFactory {

	public abstract void printlog();

	public abstract IPay createPayEntry();
}
