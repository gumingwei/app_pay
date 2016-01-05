# app_pay
package AliPay and WxPay by Abstact Factory<br>
##关于本项目<br>
使用工厂模式对微信支付和支付宝支付进行了封装，只需简单配置三句代码就可以调起支付
* 微信支付<br>
```java
IPayFactory factory = new WxFactory();  
WxPay pay = (WxPay) factory.createPayEntry();  
pay.setWxPay(PayActivity.this);  
pay.pay(info);//info就是你的订单信息
```
* 支付宝支付<br>
```java
IPayFactory factory = new AliFactory();  
AliPay alipay = (AliPay) factory.createPayEntry();  
alipay.setAliPay("商品名称","商品描述", "订单号","价格");  
alipay.pay(PayActivity.this, mHandler, SDK_PAY_FLAG)
```
[我的博客](http://blog.csdn.net/u013045971 "顾明伟的CSDN博客")
