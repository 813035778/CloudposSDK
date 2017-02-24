package com.unionpay.cloudpos.signature;

import com.unionpay.cloudpos.Device;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.OperationListener;
import com.unionpay.cloudpos.OperationResult;
import com.unionpay.cloudpos.TimeConstants;
import com.unionpay.cloudpos.serialport.SerialPortOperationResult;

/**
 * <b>SignatureDevice</b>定义了签字设备的接口。
 * <p>设备对象通过<code>POSTerminal</code>的对应方法获得，如下所示：
 * <pre>
 * SignatureDevice signatureDevice =
 *         (SignatureDevice) POSTerminal.getInstance().getDevice("cloudpos.device.signature");
 * </pre>
 * 其中，"cloudpos.device.signature"是标识签字设备的字符串，由具体的实现定义。
 * 为了正常访问签字设备，请在Android Manifest文件中设置签字设备的访问权限，具体如下所示：
 * <pre> &lt;uses-permission android:name="android.permission.CLOUDPOS_SIGNATURE"/>
 * </pre>
 * @see Device
 */
public interface SignatureDevice extends Device{
    /**
     * 打开某个逻辑ID的签字设备
     * <p>
     * 
     * @param logicID 签字设备逻辑ID 0 模拟；1 外接
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。
     */
    void open(int logicID) throws DeviceException;
    /**
     * 异步方法，等待签字。
     * 本方法会正确响应
     * {@link #cancelRequest()}方法来取消操作。
     *<p>本方法是一个异步方法，应用程序发出读取命令后，终端通过操作监听者{@link OperationListener#handleResult(OperationResult) handleResult()}方法返回结果。
     *每个应用程序必须定义自己的OperationListener，在回调函数handleResult()中对返回结果进行处理。如下所示：
     * <pre>
     * OperationListener operationListener = new OperationListener(){
     *     &#064;Override
     *     public void handleResult(OperationResult result) {
     *         // handleResult
     *     }
     * });
     * </pre>
     * <p>方法通过设置timeout来决定最多等待多长时间。请求开始执行的时候timeout开始计时。
     * <p>如果timeout时间到了，但还没有数据读到，也会回调函数handleResult()。此时
     * OperationResult会返回错误：{@link OperationResult#ERR_TIMEOUT ERR_TIMEOUT}，同时没有任何数据返回
     * <p>如果timeout是{@link TimeConstants#FOREVER FOREVER}，方法会一直等待，直到有数据返回或取消。
     * <p>如果timeout是{@link TimeConstants#IMMEDIATE IMMEDIATE}，方法会马上返回。
     * 
     * @param transactionCode 交易特征码
     * @param listener 操作监听者。
     * @param timeout 最大等待时间，通过时间常量设定{@link TimeConstants#SECOND SECOND},{@link TimeConstants#MilliSECOND MilliSECOND},
     * {@link TimeConstants#FOREVER FOREVER},{@link TimeConstants#IMMEDIATE IMMEDIATE}。
     * 
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。  
     * @see OperationListener#handleResult
     * @see SignatureOperationResult
     * @see TimeConstants#FOREVER
     * @see TimeConstants#IMMEDIATE
     */
    void listenSignature(String transactionCode,OperationListener listener, int timeout) throws DeviceException;
    
    /**
     * 本方法是上述对应的
     * {@link #listenSignature(String, OperationListener, int)}方法的同步版本。
     * <p>
     * 只有当超时发生或者操作正常完成，本次调用才会返回。
     * <p>
     * 由于带有超时，本方法会响应{@link #cancelRequest()}方法。
     * <p>
     * 如果超时发生，会返回这个操作结果：
     * {@link OperationResult#ERR_TIMEOUT ERR_TIMEOUT}。
     * 
     * @param transactionCode 交易特征码
     * @param timeout 超时
     * @return 操作结果<code>SignatureOperationResult</code>
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。  
     * @see SerialPortOperationResult
     * @see TimeConstants#FOREVER
     * @see TimeConstants#IMMEDIATE
     */
    SignatureOperationResult waitSignature(String transactionCode, int timeout) throws DeviceException;
  
}
