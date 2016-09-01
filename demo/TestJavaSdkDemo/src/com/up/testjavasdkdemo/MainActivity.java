package com.up.testjavasdkdemo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.unionpay.cloudpos.AlgorithmConstants;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.OperationListener;
import com.unionpay.cloudpos.OperationResult;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.TerminalSpec;
import com.unionpay.cloudpos.emv.EMVCardReaderResult;
import com.unionpay.cloudpos.emv.EMVConstants;
import com.unionpay.cloudpos.emv.EMVDevice;
import com.unionpay.cloudpos.emv.EMVTransData;
import com.unionpay.cloudpos.emv.EMVTransListener;
import com.unionpay.cloudpos.emv.OnlineResult;
import com.unionpay.cloudpos.emv.PINResult;
import com.unionpay.cloudpos.hsm.HSMDevice;
import com.unionpay.cloudpos.led.LEDDevice;
import com.unionpay.cloudpos.msr.MSRDevice;
import com.unionpay.cloudpos.msr.MSROperationResult;
import com.unionpay.cloudpos.msr.MSRTrackData;
import com.unionpay.cloudpos.pinpad.KeyInfo;
import com.unionpay.cloudpos.pinpad.PINPadDevice;
import com.unionpay.cloudpos.pinpad.PINPadOperationResult;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;
import com.unionpay.cloudpos.printer.PrinterDeviceSpec;
import com.up.testjavasdkdemo.ssltest.AjaxCallBack;
import com.up.testjavasdkdemo.ssltest.HttpManager;
import com.up.testjavasdkdemo.util.CommonUtil;
import com.up.testjavasdkdemo.util.LogCat;

/**
 * ***********************************
 * 类描述： JAVASDK Demo
 * 创建者： 瞿峰
 * 创建时间： 2016-8-29 下午4:32:24
 *************************************
 */
public class MainActivity extends Activity {
	public static final String URL_START_UP = "https://202.101.25.188:8188/CloudPosPayment/StartupServlet";
	private String str = "";
	private TextView txt;
	//磁条卡测试
	private Button btnMSR;
	private MSRDevice msrDevice;
	//打印机测试
	private Button btnPrinter;
	private Button btnOffPinPad;
	private PrinterDevice printerDevice;
	private Format format;
	//EMV获取卡号
	private Button btnEmvCardNo;
	private EMVDevice emvDevice;
	//LED测试
	private Button btnLED;
	private LEDDevice ledDevice;
	//密码键盘测试
	private Button btnPinPad;
	private PINPadDevice pinpadDevice;

	//HSM测试
	private Button btnHSM;
	private HSMDevice hsmDevice;

	//终端信息测试
	private Button btnTermSpec;

	//SSL测试
	private Button btnSSL;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		initData();
		context = this;
	}

	private void initViews() {
		txt = (TextView) findViewById(R.id.txt);
		btnMSR = (Button) findViewById(R.id.btnMSR);
		btnPrinter = (Button) findViewById(R.id.btnPrinter);
		btnOffPinPad = (Button) findViewById(R.id.btnOffPinPad);
		btnLED = (Button) findViewById(R.id.btnLED);
		btnPinPad = (Button) findViewById(R.id.btnPinPad);
		btnHSM = (Button) findViewById(R.id.btnHSM);
		btnTermSpec = (Button) findViewById(R.id.btnTermSpec);
		btnSSL = (Button) findViewById(R.id.btnSSL);
		btnEmvCardNo = (Button) findViewById(R.id.btnEmvCardNo);

	}

	private void initData() {
		emvDevice = (EMVDevice) POSTerminal.getInstance(getApplicationContext()).getDevice("cloudpos.device.emv");
		msrDevice = (MSRDevice) POSTerminal.getInstance(getApplicationContext()).getDevice("cloudpos.device.msr");
		printerDevice = (PrinterDevice) POSTerminal.getInstance(getApplicationContext()).getDevice(
				"cloudpos.device.printer");
		ledDevice = (LEDDevice) POSTerminal.getInstance(getApplicationContext()).getDevice("cloudpos.device.led");
		pinpadDevice = (PINPadDevice) POSTerminal.getInstance(getApplicationContext()).getDevice(
				"cloudpos.device.pinpad");
		hsmDevice = (HSMDevice) POSTerminal.getInstance(getApplicationContext()).getDevice("cloudpos.device.hsm");

		txt.setMovementMethod(new ScrollingMovementMethod());
		btnMSR.setOnClickListener(msrClickListener);
		btnPrinter.setOnClickListener(printerClickListener);
		btnOffPinPad.setOnClickListener(btnOffPinPadClickListener);
		btnLED.setOnClickListener(ledClickListener);
		btnPinPad.setOnClickListener(pinpadClickListener);
		btnHSM.setOnClickListener(hsmClickListener);
		btnTermSpec.setOnClickListener(termSpecClickListener);
		btnSSL.setOnClickListener(sslClickListener);
		btnEmvCardNo.setOnClickListener(emvCardNoClickListener);
	}

	private Handler handler = new Handler();

	private Runnable myRunnable = new Runnable() {
		public void run() {
			txt.setText(str);
		}
	};

	OnClickListener sslClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initHttpsConnect();
			str = "正在进行双向认证，请稍候...\n";
			handler.post(myRunnable);
			if (isNetConnected(context)) {
				HttpManager.getInstance(context).doPost(null, URL_START_UP, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						LogCat.i("response:" + t);
						str += "双向认证成功...\n";
						handler.post(myRunnable);
					}

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						LogCat.i("onFailure:" + t + "  strMsg:" + strMsg);
						str += "双向认证失败...\n";
						handler.post(myRunnable);
					}
				});
			} else {
				LogCat.i("网络连接失败，请检查网络!");
				str += "网络连接失败，请检查网络!";
				handler.post(myRunnable);
			}
		}

	};

	private void initHttpsConnect() {
		LogCat.i("----------start init https connection------");
		System.setProperty("javax.net.ssl.keyStoreProvider", "SunPKCS11-wizarpos");
		System.setProperty("javax.net.ssl.certAlias", "client2048");
	}

	/**
	* 检测网络是否连接
	* 
	* @return true:已连接；false:未连接
	*/
	private boolean isNetConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo == null || !activeInfo.isConnected()) {
			return false;
		}

		return true;
	}

	OnClickListener termSpecClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TerminalSpec terminalSpec = POSTerminal.getInstance(MainActivity.this).getTerminalSpec();
			String Manufacturer = terminalSpec.getManufacturer();
			String Model = terminalSpec.getModel();
			String OSVersion = terminalSpec.getOSVersion();
			String APILevel = terminalSpec.getAPILevel();
			String SerialNumber = terminalSpec.getSerialNumber();
			String text = "厂商:" + Manufacturer + "\n" + "终端型号:" + Model + "\n" + "操作系统版本:" + OSVersion + "\n"
					+ "API版本:" + APILevel + "\n" + "终端序列号:" + SerialNumber + "\n";
			txt.setText(text);

		}

	};
	OnClickListener hsmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				str = "正在打开安全模块,请稍后...\n";
				hsmDevice.open();
				str += "安全模块打开成功...\n";
				handler.post(myRunnable);
				byte[] cert = hsmDevice.getCertificate(hsmDevice.CERT_TYPE_PUBLIC_KEY, "client2048",
						hsmDevice.CERT_FORMAT_PEM);
				if (cert != null && cert.length > 0) {
					InputStream in = null;
					try {
						in = new ByteArrayInputStream(cert, 0, cert.length);

						CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
						X509Certificate certX509 = (X509Certificate) certFactory.generateCertificate(in);

						String strCert = certX509.getSubjectX500Principal().getName();
						String[] strCertValues = strCert.split(",");
						for (int i = 0; i < strCertValues.length; i++) {
							String value = strCertValues[i];
							if (value.startsWith("CN=")) {
								String posId = new String(value.substring(3, value.length()));
								str += "posId:" + posId + "\n";
								String merchantNo = posId.substring(0, 15);
								str += "merchantNo:" + merchantNo + "\n";
								String terminalNo = posId.substring(15, 23);
								str += "terminalNo:" + terminalNo + "\n";
								handler.post(myRunnable);
								Log.i("posId", posId);
								break;
							}
						}
					} catch (CertificateException e) {
						e.printStackTrace();
						str += "证书读取失败...\n";
						handler.post(myRunnable);
					} finally {
						try {
							in.close();
						} catch (IOException e) {
						}
					}
				}
			} catch (DeviceException de) {
				de.printStackTrace();
				str += "安全模块打开失败...\n";
				handler.post(myRunnable);
			} finally {
				try {
					hsmDevice.close();
					str += "安全模块关闭成功...\n";
					handler.post(myRunnable);
				} catch (DeviceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					str += "安全模块关闭失败...\n";
					handler.post(myRunnable);
				}
			}

		}

	};

	OnClickListener btnOffPinPadClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				str = "正在打开脱机密码键盘，请稍后...\n";
				handler.post(myRunnable);
				pinpadDevice.open();
				str += "脱机密码键盘打开成功，请输入脱机密码...\n";
				handler.post(myRunnable);
				try {
					pinpadDevice.listenForOfflinePin(true, new OperationListener() {

						@Override
						public void handleResult(OperationResult result) {
							// TODO Auto-generated method stub
							if (result.getResultCode() == result.SUCCESS) {
								str += "输入脱机密码成功\n";
								handler.post(myRunnable);
							} else if (result.getResultCode() == result.ERR_TIMEOUT) {
								str += "输入脱机密码超时...\n";
								handler.post(myRunnable);
							} else {
								str += "输入脱机密码失败...\n";
								handler.post(myRunnable);
							}
							try {
								pinpadDevice.close();
								str += "脱机密码键盘关闭成功...\n";
								handler.post(myRunnable);
							} catch (DeviceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								str += "脱机密码键盘关闭失败...\n";
								handler.post(myRunnable);
							}
						}
					}, 60 * 1000);
				} catch (DeviceException de) {
					de.printStackTrace();
					str += "输入脱机密码失败...\n";
					handler.post(myRunnable);
				}
			} catch (DeviceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				str = "脱机密码键盘打开失败...\n";
				handler.post(myRunnable);
			}

		}

	};
	OnClickListener pinpadClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				str = "正在打开密码键盘，请稍后...\n";
				handler.post(myRunnable);
				pinpadDevice.open();
				str += "密码键盘打开成功，请输入密码...\n";
				handler.post(myRunnable);
				try {
					KeyInfo keyInfo = new KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0, AlgorithmConstants.ALG_3DES);
					String pan = "1234567890123456";
					pinpadDevice.listenForPinBlock(keyInfo, pan, true, new OperationListener() {

						@Override
						public void handleResult(OperationResult result) {
							// TODO Auto-generated method stub
							if (result.getResultCode() == result.SUCCESS) {
								PINPadOperationResult operationResult = (PINPadOperationResult) result;
								String pinblock = CommonUtil.bytes2HexStr(operationResult.getEncryptedPINBlock());
								str += "pinblock:" + pinblock + "\n";
								handler.post(myRunnable);
							} else if (result.getResultCode() == result.ERR_TIMEOUT) {
								str += "获取pinblock超时...\n";
								handler.post(myRunnable);
							} else {
								str += "获取pinblock失败...\n";
								handler.post(myRunnable);
							}
							try {
								pinpadDevice.close();
								str += "密码键盘关闭成功...\n";
								handler.post(myRunnable);
							} catch (DeviceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								str += "密码键盘关闭失败...\n";
								handler.post(myRunnable);
							}
						}
					}, 60 * 1000);
				} catch (DeviceException de) {
					de.printStackTrace();
					str += "获取pinblock失败...\n";
					handler.post(myRunnable);
				}
			} catch (DeviceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				str = "密码键盘打开失败...\n";
				handler.post(myRunnable);
			}

		}

	};
	OnClickListener emvCardNoClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				str = "正在打开EMV设备，请稍后...\n";
				handler.post(myRunnable);
				emvDevice.open();
				emvDevice.readCard(60000, true, true, readCardListener);
				str += "EMV设备已成功打开，请插卡或挥卡...\n";
				handler.post(myRunnable);
			} catch (DeviceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	OperationListener readCardListener = new OperationListener() {

		@Override
		public void handleResult(OperationResult result) {
			// TODO Auto-generated method stub
			EMVCardReaderResult mCardReaderResult = (EMVCardReaderResult) result;
			if (mCardReaderResult.getResultCode() == OperationResult.SUCCESS) {
				final EMVTransData transData = new EMVTransData();
				if (mCardReaderResult.getChannelType() == EMVConstants.Channel_Type_IC) {
					transData.setChannelType(EMVConstants.Channel_Type_IC);
				} else if (mCardReaderResult.getChannelType() == EMVConstants.Channel_Type_RF) {
					transData.setChannelType(EMVConstants.Channel_Type_RF);
				}
				transData.setFlow(EMVTransData.TRANSDATA_FLOW_SIMPLE);//flow 流程类型，0x01：标准的授权过程；0x02：简易流程；0x03：qPBOC流程
				String strSysDate = CommonUtil.getSysDate();
				String strDate = strSysDate.substring(0, 8);
				String strTime = strSysDate.substring(8, 14);
				transData.setTransDate(strDate);
				transData.setTransType((byte) 0x00);
				transData.setTransTime(strTime);
				transData.setSupportEC(false); //不用电子现金 
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {

						EMVTransListener mEMVTransListener = new EMVTransListener() {

							@Override
							public int onWaitAppSelect(List<String> appNameList, boolean isFirstSelect) {
								// TODO Auto-generated method stub
								return 0;
							}

							@Override
							public void onTransResult(int code, String desc) {
								// TODO Auto-generated method stub
								if (code == EMVConstants.Process_Result_Approve) {
									str += "交易成功...\n";
									handler.post(myRunnable);
								} else {
									str += "交易拒绝...\n";
									handler.post(myRunnable);
								}
								try {
									emvDevice.close();
									str += "EMV设备已成功关闭...\n";
									handler.post(myRunnable);
								} catch (DeviceException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									str += "EMV设备关闭失败...\n";
									handler.post(myRunnable);
								}
							}

							@Override
							public OnlineResult onOnlineProc() {
								return null;
							}

							@Override
							public int onConfirmEC() {
								// TODO Auto-generated method stub
								return 0;
							}

							@Override
							public int onConfirmCardNo(String cardNO) {
								str += "卡号：" + cardNO + "\n";
								handler.post(myRunnable);
								return EMVConstants.App_Confirm_OK;
							}

							@Override
							public int onCertVerfiy(String certType, String certValue) {
								// TODO Auto-generated method stub
								return 0;
							}

							@Override
							public PINResult onCardHolderPwd(boolean arg0, int arg1) {
								// TODO Auto-generated method stub
								return null;
							}

						};

						// TODO Auto-generated method stub
						try {
							emvDevice.process(transData, mEMVTransListener);
						} catch (DeviceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();
			} else if (mCardReaderResult.getResultCode() == OperationResult.ERR_TIMEOUT) {
				try {
					emvDevice.stopReadCard();
					emvDevice.close();
					str += "EMV设备已成功打开，请插卡或挥卡...\n";
					handler.post(myRunnable);
				} catch (DeviceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					str += "EMV设备打开失败...\n";
					handler.post(myRunnable);
				}
			}
		}
	};

	OnClickListener msrClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				str = "正在打开磁条卡阅读器，请稍后...\n";
				handler.post(myRunnable);
				msrDevice.open();
				str += "磁条卡阅读器已成功打开，请刷卡...\n";
				handler.post(myRunnable);
				try {
					msrDevice.listenForSwipe(new OperationListener() {

						@Override
						public void handleResult(OperationResult result) {
							// TODO Auto-generated method stub
							if (result.getResultCode() == result.SUCCESS) {
								MSROperationResult msrOperationResult = (MSROperationResult) result;
								MSRTrackData tarckData = msrOperationResult.getMSRTrackData();
								if (tarckData.getTrackError(0) == MSRTrackData.NO_ERROR) {
									if (tarckData.getTrackData(0) != null) {
										String track1Data = new String(tarckData.getTrackData(0));
										str += "第一磁道信息:" + track1Data + "\n";
										handler.post(myRunnable);
										Log.i("", "第一磁道信息:" + track1Data);
									}
								} else {
									if (tarckData.getTrackData(0) == null) {
										str += "第一磁道信息:\n";
										handler.post(myRunnable);
										Log.i("", "第一磁道信息:");
									}
								}
								if (tarckData.getTrackError(1) == MSRTrackData.NO_ERROR) {
									if (tarckData.getTrackData(1) != null) {
										String track2Data = new String(tarckData.getTrackData(1));
										str += "第二磁道信息:" + track2Data + "\n";
										handler.post(myRunnable);
										Log.i("", "第二磁道信息:" + track2Data);
									}
								} else {
									if (tarckData.getTrackData(1) == null) {
										str += "第二磁道信息:\n";
										handler.post(myRunnable);
										Log.i("", "第二磁道信息:");
									}
								}
								if (tarckData.getTrackError(2) == MSRTrackData.NO_ERROR) {
									if (tarckData.getTrackData(2) != null) {
										String track3Data = new String(tarckData.getTrackData(2));
										str += "第三磁道信息:" + track3Data + "\n";
										handler.post(myRunnable);
										Log.i("", "第三磁道信息:" + track3Data);
									}
								} else {
									if (tarckData.getTrackData(2) == null) {
										str += "第三磁道信息:\n";
										handler.post(myRunnable);
										Log.i("", "第三磁道信息:");
									}
								}
							} else if (result.getResultCode() == result.ERR_TIMEOUT) {
								str += "读取磁道信息超时..\n";
								handler.post(myRunnable);
							} else {
								str += "读取磁道信息失败...\n";
								handler.post(myRunnable);
							}

							try {
								msrDevice.close();
								str += "磁条卡阅读器已成功关闭...\n";
								handler.post(myRunnable);
							} catch (DeviceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								str += "磁条卡阅读器关闭失败...\n";
								handler.post(myRunnable);
							}
						}
					}, 10000);

				} catch (DeviceException de) {
					str += "读取磁道信息失败...\n";
					handler.post(myRunnable);
				}
			} catch (DeviceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				str += "磁条卡阅读器打开失败...\n";
				handler.post(myRunnable);
			}
		}

	};

	OnClickListener printerClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				str = "正在打开打印机设备，请稍后...\n";
				handler.post(myRunnable);
				printerDevice.open();
				str += "打印机设备打开成功...\n";
				handler.post(myRunnable);
				format = new Format();
				try {
					if (printerDevice.queryStatus() == -101) {
						str += "打印机缺纸...\n";
						handler.post(myRunnable);
						closePrinter();
					} else if (printerDevice.queryStatus() == -102) {
						str += "打印机状态异常...\n";
						handler.post(myRunnable);
						closePrinter();
					} else if (printerDevice.queryStatus() == 1) {
						str += "打印机状态正常，开始打印...\n";
						handler.post(myRunnable);
						Thread thread = new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									printerDevice.printText(format, "以默认格式打印\n");
									format.setParameter("align", "left");
									format.setParameter("size", "extra-large");
									printerDevice.printText(format, "extra-large(特大)，银联POS签购单\n");
									format.setParameter("size", "large");
									printerDevice.printText(format, "large(大)，银联POS签购单\n");
									format.setParameter("size", "medium");
									printerDevice.printText(format, "medium(中)，银联POS签购单\n");
									format.setParameter("size", "small");
									printerDevice.printText(format, "small(小)，银联POS签购单\n");
									format.setParameter("size", "extra-small");
									printerDevice.printText(format, "extra-small(特小)，银联POS签购单\n");

									format.clear();
									printerDevice.printText(format, "<打印图片>\n");
									Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
											R.drawable.ic_launcher);
									printerDevice.printBitmap(bitmap);
									PrinterDeviceSpec printerDeviceSpec = (PrinterDeviceSpec) POSTerminal.getInstance(
											context).getDeviceSpec("cloudpos.device.printer");
									String[] strs = printerDeviceSpec.getBarCodeFormat(0);
									if (strs != null) {
										for (String str : strs) {
											if (PrinterDeviceSpec.BARCODE_TYPE_CODABAR.equals(str)) {
												printerDevice.printText(format, "<BARCODE_CODABAR 条码上方>\n");
												format.setParameter("HRI-location", "up");
												printerDevice.printBarcode(format, printerDevice.BARCODE_CODABAR,
														"A1234567890B");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_UPC_A.equals(str)) {
												printerDevice.printText(format, "<BARCODE_UPC_A 条码上下方>\n");
												format.setParameter("HRI-location", "up-down");
												printerDevice.printBarcode(format, printerDevice.BARCODE_UPC_A,
														"123456789012");
											}

											if (PrinterDeviceSpec.BARCODE_TYPE_CODE93.equals(str)) {
												printerDevice.printText(format, "<BARCODE_CODE93条码下方>\n");
												format.setParameter("HRI-location", "down");
												printerDevice.printBarcode(format, printerDevice.BARCODE_CODE93,
														"12345678901");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_UPC_E.equals(str)) {
												printerDevice.printText(format, "<BARCODE_UPC_E没有>\n");
												format.setParameter("HRI-location", "none");
												printerDevice.printBarcode(format, printerDevice.BARCODE_UPC_E,
														"01234565");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_JAN13.equals(str)) {
												printerDevice.printText(format, "<BARCODE_JAN13条码上方>\n");
												format.setParameter("HRI-location", "up");
												printerDevice.printBarcode(format, printerDevice.BARCODE_JAN13,
														"1234567890128");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_JAN8.equals(str)) {
												printerDevice.printText(format, "<BARCODE_JAN8条码上下方>\n");
												format.setParameter("HRI-location", "up-down");
												printerDevice.printBarcode(format, printerDevice.BARCODE_JAN8,
														"12345670");
											}

											if (PrinterDeviceSpec.BARCODE_TYPE_CODE39.equals(str)) {
												printerDevice.printText(format, "<BARCODE_CODE39条码下方>\n");
												format.setParameter("HRI-location", "down");
												printerDevice.printBarcode(format, printerDevice.BARCODE_CODE39,
														"*1234567890*");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_ITF.equals(str)) {
												printerDevice.printText(format, "<BARCODE_ITF没有>\n");
												format.setParameter("HRI-location", "none");
												printerDevice.printBarcode(format, printerDevice.BARCODE_ITF,
														"246824682468");
											}
											if (PrinterDeviceSpec.BARCODE_TYPE_CODE128.equals(str)) {
												printerDevice.printText(format, "<BARCODE_CODE128条码上方>\n");
												format.setParameter("HRI-location", "up");
												printerDevice.printBarcode(format, printerDevice.BARCODE_CODE128,
														"12345678900001012345610");
											}

										}
									}
									format = new Format();
									printerDevice.printText("\n");
									printerDevice.printText("\n");
									format.setParameter("align", "center");
									format.setParameter("bold", "true");
									format.setParameter("size", "large");
									printerDevice.printText(format, "银联POS签购单");
									printerDevice.printText("\n");
									printerDevice.printText("\n");
									format.clear();
									format.setParameter("align", "left");
									format.setParameter("size", "medium");
									printerDevice.printText(format, "商户名称：研究院-何舟\n");
									printerDevice.printText(format, "商户编号：123456789000010\n");
									printerDevice.printText(format, "终端编号：12345610\n");
									printerDevice.printText(format, "操作员号：01\n");
									printerDevice.printText(format, "发卡行号：01020000\n");
									printerDevice.printText(format, "发卡行：中国工商银行\n");
									printerDevice.printlnText(format, "卡号：621795****0017");
									printerDevice.printlnText(format, "收单行号：00072900");
									printerDevice.printlnText(format, "收单行：未知机构");
									printerDevice.printlnText(format, "交易类型：消费（SALE）");
									printerDevice.printText(format, "有效期：1507  批次号：000111\n");
									printerDevice.printText(format, "凭证号：001127\n");
									printerDevice.printText(format, "参考号：103425000437\n");
									printerDevice.printText(format, "日期时间：11/23 10:34:25\n");
									printerDevice.printText(format, "金额（AMOUNT）：0.01元\n");
									printerDevice.printText(format, "备注：");
									printerDevice.printText("\n");
									printerDevice.printText(format, "AID：A00000033310101");
									printerDevice.printText(format, "ARQC：TC");
									printerDevice.printText(format, "持卡人签名：");
									printerDevice.printText(format, "CARD HOLDER SIGNATURE");
									printerDevice.printText("\n");
									printerDevice.printText("\n");
									format.setParameter("size", "small");
									printerDevice.printText(format, "本人确认以上交易，同意将其写入本卡账户");
									printerDevice.printText(format,
											"I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES");
								} catch (DeviceException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									try {
										if (printerDevice.queryStatus() == -101) {
											str += "打印机缺纸...\n";
											handler.post(myRunnable);
										} else if (printerDevice.queryStatus() == -102) {
											str += "打印机状态异常...\n";
											handler.post(myRunnable);
										} else {
											str += "打印失败，未知异常...\n";
											handler.post(myRunnable);
										}
									} catch (DeviceException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
										str += "打印失败，查看打印机状态失败...\n";
										handler.post(myRunnable);
									}
								} finally {
									closePrinter();
								}
							}
						});
						thread.start();
					}
				} catch (DeviceException de) {
					str += "查看打印机状态失败...\n";
					handler.post(myRunnable);
					de.printStackTrace();
					closePrinter();
				}
			} catch (DeviceException de) {
				de.printStackTrace();
				str += "打印机设备打开失败...\n";
				handler.post(myRunnable);
			}

		}
	};

	OnClickListener ledClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				str = "正在打开LED设备，请稍后...\n";
				handler.post(myRunnable);
				ledDevice.open();
				str += "打开LED设备成功...\n";
				handler.post(myRunnable);
				try {
					ledDevice.blink(1000, 1000, 3);
					str += "开始闪烁成功...\n";
					handler.post(myRunnable);
				} catch (DeviceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					str += "开始闪烁失败...\n";
					handler.post(myRunnable);
				}
				try {
					ledDevice.close();
					str += "关闭LED设备成功...\n";
					handler.post(myRunnable);
				} catch (DeviceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					str += "关闭LED设备失败...\n";
					handler.post(myRunnable);
				}
			} catch (DeviceException e) {
				// TODO Auto-generated catch block
				str += "打开LED设备失败...\n";
				handler.post(myRunnable);
				e.printStackTrace();
			}
		}
	};

	private void closePrinter() {
		try {
			printerDevice.close();
			str += "打印设备关闭成功...\n";
			handler.post(myRunnable);
		} catch (DeviceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			str += "打印设备关闭失败...\n";
			handler.post(myRunnable);
		}
	}

}
