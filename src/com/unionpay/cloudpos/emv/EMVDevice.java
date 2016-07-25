/*
 * EMVDevice.java
 *
 * Copyright (c) 2013 - 2020 UnionPay. All rights reserved.
 *
 *
 * UNIONPAY PROPRIETARY/CONFIDENTIAL.
 *
 */
package com.unionpay.cloudpos.emv;

import java.util.List;

import com.unionpay.cloudpos.Device;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.OperationListener;
import com.unionpay.cloudpos.OperationResult;
import com.unionpay.cloudpos.TimeConstants;
/**
 * <b>EMVDevice</b>定义了对EMV的操作方法。任何具体的实现都必须实现这个接口。
 * <p>设备对象通过<code>POSTerminal</code>的对应方法获得，如下所示：
 * <pre>
 * EMVDevice eMVDevice =
 *         (EMVDevice) POSTerminal.getInstance().getDevice("cloudpos.device.emv");
 * </pre>
 * 其中，"cloudpos.device.emv"是标识EMV的字符串，由具体的实现定义。
 * @see Device
 */
public interface EMVDevice extends Device{
    /**
     * 配置终端设备参数。
     * @param config 终端配置。
     * @return 是否设置成功。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    void setTermConfig(EMVTermConfig config)  throws DeviceException;
    
    /**
     * 获取终端设备参数。
     * @return EMVTermConfig 终端配置。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    EMVTermConfig getTermConfig()  throws DeviceException;
    
    /**
     * 检卡流程，在这个流程中打开指定的卡，并进入等卡逻辑。如果检测到卡，通过listener通知给应用。
     * 本操作是个异步调用，调用后立即返回。
     * 当用户插卡或者挥卡后，结果通过操作监听者{@link OperationListener#handleResult(OperationResult) handleResult()}方法返回。
     * <p>
     * 通常程序必须定义自己的OperationListener，在回调函数handleResult()中对返回结果进行处理。如下所示：
     * <pre>
     * OperationListener operationListener = new OperationListener(){
     *     &#064;Override
     *     public void handleResult(OperationResult result) {
     *         // handleResult
     *     }
     * });
     * </pre>
     * <p>方法通过设置timeout来决定最多等待多长时间。请求开始执行的时候timeout开始计时。
     * <p>如果timeout时间到了，但用户还没有刷卡，也会回调函数handleResult()。此时
     * OperationResult会返回错误：{@link OperationResult#ERR_TIMEOUT ERR_TIMEOUT}，同时没有任何卡片返回
     * <p>如果timeout是{@link TimeConstants#FOREVER FOREVER}，方法会一直等待刷卡，直到刷卡或取消。
     * <p>如果timeout是{@link TimeConstants#IMMEDIATE IMMEDIATE}，方法会马上返回。
     * @param timeout 最大等待时间，通过时间常量设定{@link TimeConstants#SECOND SECOND},{@link TimeConstants#MilliSECOND MilliSECOND},
     * {@link TimeConstants#FOREVER FOREVER},{@link TimeConstants#IMMEDIATE IMMEDIATE}。
     * @param icCard 是否打开IC卡设备。
     * @param rfCard 是否打开非接卡设备。
     * @param listener 操作监听者。
     * 
     * @see OperationListener#handleResult
     * @see EMVCardReaderResult
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    void readCard (int timeout, boolean icCard, boolean rfCard, OperationListener listener)  throws DeviceException;
    
    /**
     * 结束检卡。
     * <p>结束检卡会取消之前的异步读卡，并关闭读卡设备。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    void stopReadCard()  throws DeviceException;
    
    /**
     * 处理EMV流程。
     * <p>结束检卡会取消之前的异步读卡，并关闭读卡设备。
     * @param transData EMV交易数据。
     * @param listener EMV交易监听器，应用通过监听器得到需要的数据，处理确认之后，通过返回值，告诉EMVSDK实现进入下一步流程。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    void process(EMVTransData transData, EMVTransListener listener)  throws DeviceException;
    
    /**
     * 设置TAG值。
     * @param tag 
     * @param value 
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    void setTLV(int tag, byte[] value)  throws DeviceException;
    
    /**
     * 获取指定TAG列表数据。
     * @param tags  tag列表。
     * @return byte[] TLV串。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    byte[] getTLVList(List<Integer> tags)  throws DeviceException;
    
    /**
     * 获取指定TAG的数据。
     * @param tag  TLV tag。
     * @return byte[] TLV value。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    byte[] getTLV(int tag)  throws DeviceException;
    
    /**
     * 查询电子现金余额流程。
     * @param channelType  0（{@link EMVConstants#Channel_Type_IC}）：接触式；1（{@link EMVConstants#Channel_Type_RF}）：非接触式。
     * @return Balance 电子现金余额。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    Balance queryECBalance(int channelType)  throws DeviceException;
    
    /**
     * 查询卡片交易记录流程。
     * @param channelType  0（{@link EMVConstants#Channel_Type_IC}）：接触式；1（{@link EMVConstants#Channel_Type_RF}）：非接触式。
     * @return List<EMVCardLog> 卡片交易记录。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    List<EMVCardLog> queryLogRecord(int channelType)  throws DeviceException;
    
    /**
     * 设置AID参数。这个参数必须TLV格式的一条参数。
     * @param AIDParam  
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    void setAIDParam(byte[] AIDParam)  throws DeviceException;
    
    /**
     * 获取AID参数。
     * @return AIDParam List。List中的每个byte[]是一条TLV格式的参数。 
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    List<byte[]> getAIDParam()  throws DeviceException;
    
    /**
     * 清除AID参数。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    void clearAIDParam ()  throws DeviceException;
    
    /**
     * 设置公钥参数。这个参数必须TLV格式的一条参数。
     * @param CAPKParam 公钥参数。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    void setCAPKParam (byte[] CAPKParam)  throws DeviceException;
    
    /**
     * 获取公钥参数。
     * @return 公钥参数  List。List中的每个byte[]是一条TLV格式的参数。 
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    List<byte[]> getCAPKParam ()  throws DeviceException;
    
       
    /**
     * 清除公钥参数。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。 
     */
    void clearCAPKParam ()  throws DeviceException;
}
