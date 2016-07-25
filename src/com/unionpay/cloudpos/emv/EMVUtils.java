package com.unionpay.cloudpos.emv;

import java.util.Map;

import com.unionpay.cloudpos.DeviceException;

public class EMVUtils {

    /**
     * 解析AID参数。
     * <p>传入TLV格式的AID参数，解析成EMVAIDParam.
     * @param AIDParam
     * @return EMVAIDParam  
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static EMVAIDParam parseAIDParam (byte[] AIDParam)  throws DeviceException{
        return null;
        
    }
    /**
     * 解析公钥参数。
     * <p>传入TLV格式的公钥参数，解析成EMVCAPKParam.
     * @param CAPKParam
     * @return EMVAIDParam。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static EMVCAPKParam parseCAPKParam (byte[] CAPKParam)  throws DeviceException{
        return null;
    }
    /**
     * 解析TLV参数。这是一个TLV串。
     * <p>传入TLV格式的参数，解析成单个的TLV map.
     * @param tlvList
     * @return Map。
     * @throws DeviceException  具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static Map<Integer,byte[]> parseTLVList(byte[] tlvList) throws DeviceException{
        return null;
    }
}
