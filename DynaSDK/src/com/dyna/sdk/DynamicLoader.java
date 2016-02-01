package com.dyna.sdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;




import dalvik.system.DexClassLoader;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public enum DynamicLoader {
	INSTANCE;
	
	private static final String TAG = "Dyna";
	private static final String SDK_JAR_NAME = "dyna3rdsdk_dex.jar";
	private static final String SDK_JAR_DIR = "/droi/";
	
	private Class<?> utilsClass;
	private Object utilsInstance;
	private Context mContext;
	
	public void init(Context context) {
		mContext = context;
		Log.i(TAG, "Private Folder = " + context.getFilesDir().getAbsolutePath());
		
		File file = getPrivateFile(context, SDK_JAR_NAME);
		copyFromAssets(context, SDK_JAR_NAME, file);
		if (isFileLegal(file) == false) {
			Log.e(TAG, "[ERROR] can\'t load jar file");
			return;
		}
	 
		DexClassLoader dexClassLoader = null;
		if (dexClassLoader == null) {
			dexClassLoader = new DexClassLoader(file.toString(), context.getFilesDir().getAbsolutePath(), null, context.getClassLoader());
		}
		try {
			Class<?> localClass = dexClassLoader.loadClass("com.dyna.ali.sdk.Alipay");

			Method method = localClass.getDeclaredMethod("getInstance", new Class[] { Context.class });
			Object instance = method.invoke(localClass, new Object[]{context});
			utilsClass = localClass;
			utilsInstance = instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void purchase(int count, int price) {
		Method method;
		try {
			method = utilsClass.getDeclaredMethod("pay", new Class[] { int.class });
			try {
				method.invoke(utilsInstance, new Object[] { count*price });
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void purchase(int count, int price, final purchaseCallback callback) {
		Method method;
		try {
			ClassLoader loader = utilsClass.getClassLoader();
			Class<?> interfazz = loader.loadClass("com.dyna.ali.sdk.Alipay$payListener");
		
			Object clazzInstance = Proxy.newProxyInstance(loader, new Class[] { interfazz }, new InvocationHandler() {

			    @Override
			    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
			        if (method.getName().equals("onSuccess")) {
			            callback.onSuccess(((Integer)args[0]).intValue());
			            return null;
			        } else if (method.getName().equals("onError")) {
			            callback.onError(((Integer)args[0]).intValue(), ((String)args[1]));
			            return null;
			        } else {
			            return method.invoke(obj, args);
			        }
			    }
			
			});
			
			method = utilsClass.getDeclaredMethod("payWithListener", new Class[] { int.class, interfazz});
			
			try {
				method.invoke(utilsInstance, new Object[] { count*price, clazzInstance});
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public interface purchaseCallback {
		public void onSuccess(int result);
		public void onError(int errorid, String errorMsg);
	}
	
//	private File getSdcardFile(String jarName) {
//		try {
//			return new File(Environment.getExternalStorageDirectory() + SDK_JAR_DIR + jarName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	private File getPrivateFile(Context context, String jarName) {
		try {
			return new File(context.getFilesDir().getAbsolutePath() + "/" + jarName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean isFileLegal(File file) {
		try {
			Log.i(TAG, "Jar File = " + file);

			if (file != null) {
				if (file.exists()) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void copyFromAssets(Context context, String sourceName, File destFile) {
		AssetManager assetManager = context.getAssets();
	 
        InputStream in = null;
        OutputStream out = null;
        try {
          in = assetManager.open(sourceName);
          out = new FileOutputStream(destFile);
          copyFile(in, out);
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + sourceName, e);
        }     
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}
