package com.unionpay.cloudpos.hsm;

import com.unionpay.cloudpos.DeviceException;

/**
 * 本接口由各个厂商实现，仅供银联调用，不需要公开给第三方应用。是HSMDevice的补充接口，使用时对权限的要求同HSMDevice。
 *
 */
public interface HSMDeviceForUnionpay extends HSMDevice{
    /**
     * 注入终端私钥证书。改私钥证书仅供银联使用，id为client2048，别名为pk2048。更新私钥也使用本接口。
     * <p>
     * 与这个私钥对应的公钥证书通过{@link HSMDevice#injectPublicKeyCertificate(String , String , byte[] , int )}方法注入。
     * <br>
     * 该操作是独占的。
     *    
     * @param keyBuffer         私钥数据流。
     * @param dataFormat        证书数据格式，目前只支持{@link HSMDevice#CERT_FORMAT_PEM}。
     * @return {@code true} 成功。{@code false} 失败。
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。
     */
    boolean injectPrivateKey(byte[] keyBuffer,int dataFormat) throws DeviceException;

}
