/*
 * PrinterDeviceSpec.java
 *
 * Copyright (c) 2013 - 2020 UnionPay. All rights reserved.
 *
 *
 * UNIONPAY PROPRIETARY/CONFIDENTIAL.
 *
 */

package com.unionpay.cloudpos.printer;

import com.unionpay.cloudpos.DeviceSpec;
/**
 * <b>PrinterDeviceSpec</b>定义了对打印机的详细描述。
 * <p>可以得到终端中关于打印机定义的信息。
 * @date August 06, 2015
 */
public interface PrinterDeviceSpec extends DeviceSpec {
    
    /**
     * 下述条码常量主要针对某些可以直接打印条码的打印机。
     * <p>
     * UPC-A条形码。
     */
    public static final String BARCODE_TYPE_UPC_A = "UPC-A";
    
    /**
     * UPC-E条形码。
     */
    public static final String BARCODE_TYPE_UPC_E = "UPC-E";
    
    /**
     * JAN13条形码。
     */
    public static final String BARCODE_TYPE_JAN13 = "JAN13";
    
    /**
     * JAN8条形码。
     */
    public static final String BARCODE_TYPE_JAN8 = "JAN8";
    
    /**
     * CODE39码。
     */
    public static final String BARCODE_TYPE_CODE39 = "CODE39";
    
    /**
     * TIF码。
     */
    public static final String BARCODE_TYPE_ITF = "TIF";
    
    /**
     * CODABAR。
     */
    public static final String BARCODE_TYPE_CODABAR = "CODABAR";
    
    /**
     * CODE93。
     */
    public static final String BARCODE_TYPE_CODE93 = "CODE93";
    
    /**
     * CODE128。
     */
    public static final String BARCODE_TYPE_CODE128 = "CODE128";
    
	/**
	 * 返回当前终端有几个打印机。
	 * @return 终端中打印机个数，不支持返回0.
	 * */
	int getCounts();
	
	/**
	 * 是否支持打印浓度的设定。
	 * @param logicalID  打印机逻辑ID，默认为0
	 * @return {@code true} 支持，{@code false}不支持，参数错误也返回false.
	 * */
	boolean canSetDensity(int logicalID);
	
	/**
	 * 返回标准字体的点数高度。
	 * @param logicalID  打印机逻辑ID，默认为0
	 * @return 打印机的标准字体的高度，参数错误及不支持返回0.
	 * */
	int getBaseFontHeight(int logicalID);
	
	/**
	 * 返回可打印的最大宽度。
	 * @param logicalID  打印机逻辑ID，默认为0
	 * @return 打印机的最大显示宽度	，参数错误及不支持返回0.
	 * */
	int getWidth(int logicalID);
	
	/**
	 * 返回可打印barcode的类型。具体值参照本接口定义的常量：{@link #BARCODE_TYPE_UPC_A}, {@link #BARCODE_TYPE_UPC_E  }，{@link #BARCODE_TYPE_JAN13}, {@link #BARCODE_TYPE_JAN8  }，{@link #BARCODE_TYPE_CODE39}, {@link #BARCODE_TYPE_ITF  }，{@link #BARCODE_TYPE_CODABAR}, {@link #BARCODE_TYPE_CODE93  }，{@link #BARCODE_TYPE_CODE128}。
	 * @param logicalID  打印机逻辑ID，默认为0
	 * @return 可打印条码的格式，参数错误及不支持返回null.
	 * */
	String [] getBarCodeFormat(int logicalID);
	
	/**
	 *  打印机是否能切纸。
	 *  @param logicalID  打印机逻辑ID，默认为0
	 *  @return {@code true} 支持，{@code false} 不支持，参数错误也返回false.
	 * */
	boolean canCutPaper(int logicalID);
}
