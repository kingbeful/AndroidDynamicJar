# AndroidDynamicJar
a simple demo for loading jar dynamically on android

## How to

Use __DexClassLoader__ to load all the jar files we want to load.

Use __Java Reflection__ to invoke all the methods we want to run.

## Demo Structure

#### __DynaDemo__
__DynaDemo__ is an Android app which use the SDK __DynaSDK__ as a library.

#### __DynaSDK__
__DynaSDK__ is the SDK will be used by customer(app developer). It will load the 3rd party SDK __Dyna3rdSDK__. In the demo, the 3rd party SDK is placed in the app's private folder. You can also put it on the SD card.

#### __Dyna3rdSDK__

a simple 3rd party SDK


## Consol Output and Java Code

Methods in DynamicLoader.java


```java
public void purchase(int count, int price);
public void purchase(int count, int price, final purchaseCallback callback);

```

Code in Alipay.java (3rd Party SDK)
```java

public boolean pay(int rmb) {
	Log.i(TAG, "Pay RMB = " + rmb);
		return true;
	}
	
public boolean payWithListener(int rmb, payListener listener) {
	Log.i(TAG, "Pay RMB = " + rmb);
	if (rmb > 1000) {
		listener.onError(rmb, "Error Message from 3rd-party sdk");
	} else {
		listener.onSuccess(rmb);
	}
	return true;
}
```

Code in MainActivity.java

```java
DynamicLoader.INSTANCE.purchase(2, 50);
DynamicLoader.INSTANCE.purchase(2, 100, callback);
DynamicLoader.INSTANCE.purchase(2, 1000, callback);

private purchaseCallback callback = new purchaseCallback(){
		@Override
		public void onSuccess(int result) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onSuccess: " + result);
		}

		@Override
		public void onError(int errorid, String errorMsg) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onError: " + errorid + " msg: " + errorMsg);
		}};
```

output in console
```
> I/ALI(14743): Pay RMB = 100
> I/ALI(14743): Pay RMB = 200
> I/MainActivity(14743): onSuccess: 200
> I/ALI(14743): Pay RMB = 2000
> I/MainActivity(14743): onError: 2000 msg: Error Message from 3rd-party sdk
```

## Trick

The Dyna3rdSDK's jar file should use __dx__  to convert to a jar includes __Dalvik Executable__ (__dex__) bytecode file 

The command:

> shell$ dx --dex --output=dyna3rdsdk_dex.jar dyna3rdsdk.jar
