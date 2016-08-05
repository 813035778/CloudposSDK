
package com.unionpay.cloudpos.emv;

import java.util.Map;

import com.unionpay.cloudpos.DeviceException;

public class EMVUtils  implements EMVConstants{

    /**
     * 解析AID参数。
     * <p>
     * 传入TLV格式的AID参数，解析成EMVAIDParam.
     * 
     * @param AIDParam
     * @return EMVAIDParam
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static EMVAIDParam parseAIDParam(byte[] AIDParam) throws DeviceException {
        AIDTable tmpAid = getEMVParamInfo(AIDParam);
        EMVAIDParam parseAIDParam = new EMVAIDParam();

        // 转换赋值
        parseAIDParam.setAID(tmpAid.getAid()); // 0x9F06
        parseAIDParam.setSelFlag((tmpAid.getNeedCompleteMatching() == 0x00) ? 0 : 1); // 0xDF01
        parseAIDParam.setOnlinePin((tmpAid.getSupportOnlinePin() == 0x00) ? false : true); // 0xDF18

        parseAIDParam.setECTTLVal(tmpAid.getUpcashTransLimit()); // 9F7B （EC
                                                                 // Terminal
                                                                 // Transaction
                                                                 // Limit）
        if (parseAIDParam.getECTTLVal() != 0)
            parseAIDParam.setECTTLFlg(true);
        else
            parseAIDParam.setECTTLFlg(false);

        parseAIDParam.setRdCVMLmt(tmpAid.getCvmLimit()); // 0xDF21
        if (parseAIDParam.getRdCVMLmt() != 0)
            parseAIDParam.setRdCVMLmtFlg(true);
        else
            parseAIDParam.setRdCVMLmtFlg(false);

        parseAIDParam.setRdClssTxnLmt(tmpAid.getContactlessLimit()); // 0xDF20
        if (parseAIDParam.getRdClssTxnLmt() != 0)
            parseAIDParam.setRdClssTxnLmtFlg(true);
        else
            parseAIDParam.setRdClssTxnLmtFlg(false);

        parseAIDParam.setRdClssFLmt(tmpAid.getContactlessFloorLimit()); // 0xDF19
        if (parseAIDParam.getRdClssFLmt() != 0)
            parseAIDParam.setRdClssFLmtFlg(true);
        else
            parseAIDParam.setRdClssFLmtFlg(false);

        /* 最低限额 */
        parseAIDParam.setFloorLimit(tmpAid.getTermFloorLimit()); // 0x9F1B
        if (parseAIDParam.getFloorLimit() != 0)
            parseAIDParam.setFloorlimitCheck(true);
        else
            parseAIDParam.setFloorlimitCheck(false);

        /* 目标百分比数 */
        parseAIDParam.setTargetPer(tmpAid.getTargetPercentage()); // 0xDF17

        /* 最大目标百分比数 */
        parseAIDParam.setMaxTargetPer(Integer.valueOf(StringUtil.toHexString(tmpAid
                .getMaxTargetPercentage())));// 0xDF16

        /* 终端行为代码（拒绝） */
        parseAIDParam.setTacDenial(tmpAid.getTACDenial()); // 0xDF13

        /* 终端行为代码（联机） */
        parseAIDParam.setTacOnline(tmpAid.getTACOnline()); // 0xDF12

        /* 终端行为代码（缺省） */
        parseAIDParam.setTacDefault(tmpAid.getTACDefault()); // 0xDF14

        /* 应用版本 */
        parseAIDParam.setVersion(tmpAid.getAppVersionNumber()); // 0x9F09

        /* 终端缺省DDOL */
        parseAIDParam.setDDOL(tmpAid.getDefaultDDOL()); // 0xDF14

        /* 终端缺省TDOL */
        parseAIDParam.setTDOL(tmpAid.getDefaultTDOL()); // 交易证书数据对象列表（TDOL）

        /* 收单行标志 */
        parseAIDParam.setAcquierId(tmpAid.getAcquirerId()); //

        /* 阀值 */
        parseAIDParam.setThreshold(tmpAid.getThresholdValue()); // 0xDF15

        /* 是否进行随机交易选择 */
        parseAIDParam.setRandTransSel(true);

        /* 是否进行频度检测 */
        parseAIDParam.setVelocityCheck(true);

        /* 风险管理数据 */
        /*
         * private int contactlessLimit; private int cvmLimit; private int
         * contactlessFloorLimit;
         */
        String tmpRiskManageData = "";
        if (parseAIDParam.getRdClssFLmt() != 0)
            tmpRiskManageData += "DF19" + parseAIDParam.getRdClssFLmt();
        if (parseAIDParam.getRdClssTxnLmt() != 0)
            tmpRiskManageData += "DF20" + parseAIDParam.getRdClssTxnLmt();
        if (parseAIDParam.getRdCVMLmt() != 0)
            tmpRiskManageData += "DF21" + parseAIDParam.getRdCVMLmt();
        parseAIDParam.setRiskManageData(tmpRiskManageData);

        /* 扩展域 */
        return parseAIDParam;

    }

    /**
     * 解析公钥参数。
     * <p>
     * 传入TLV格式的公钥参数，解析成EMVCAPKParam.
     * 
     * @param CAPKParam
     * @return EMVAIDParam。
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static EMVCAPKParam parseCAPKParam(byte[] CAPKParam) throws DeviceException {
        EMVCAPKParam tmpEmvcapkParam = new EMVCAPKParam();
        CAPKTable tmpCapkTable = getCAPKParamInfo(CAPKParam);
        
        //设置capk属性
        tmpEmvcapkParam.setArithInd(tmpCapkTable.getArithIndex());      /* RSA算法标志，SM算法时，值为04     DF07 */
        tmpEmvcapkParam.setRID(tmpCapkTable.getRID());                  /* 应用注册服务ID    9F06*/
        tmpEmvcapkParam.setKeyID(tmpCapkTable.getCapki());              /* 密钥索引         9F22*/
        tmpEmvcapkParam.setHashInd(tmpCapkTable.getHashIndex());        /* HASH算法标志,SM算法时，值为07      DF06*/
        tmpEmvcapkParam.setModul(tmpCapkTable.getModul());              /* 模，SM算法时，为公钥值             DF02*/
        tmpEmvcapkParam.setExponent(tmpCapkTable.getExponent());        /* 指数，SM算法时，为0，不检查此数据项*/
        tmpEmvcapkParam.setExpDate(tmpCapkTable.getExpiry());           /* 有效期（YYMMDD）*/
        tmpEmvcapkParam.setCheckSum(tmpCapkTable.getChecksum());        /* 密钥校验和*/

        /* 扩展域 */
        // private Map<String,Object> extField;

        return tmpEmvcapkParam;
    }

    /**
     * 解析TLV参数。这是一个TLV串。
     * <p>
     * 传入TLV格式的参数，解析成单个的TLV map.
     * 
     * @param tlvList
     * @return Map。
     * @throws DeviceException 具体定义参考{@link DeviceException DeviceException}的文档。
     */
    public static Map<Integer,byte[]> parseTLVList(byte[] tlvList) throws DeviceException{
//		if(!isOpened)
//		throw new DeviceException(ERR_NO_OPEN,"EmvDevice did not opened");
    	
    	 Map<Integer,byte[]> OutMap = null;
    	 try {
    		 OutMap = TlvUtils.builderTlvMap(StringUtil.toHexString(tlvList));
		} catch (Exception e) {
			new DeviceException(ERR_UNKNOWERR,e.toString());
		}
    	 
        return OutMap;
    }

    private static AIDTable getEMVParamInfo(byte[] tmpdata)
    {
        AIDTable theAidTable = new AIDTable();
        byte[] data = new byte[tmpdata.length + 1];
        System.arraycopy(tmpdata, 0, data, 1, tmpdata.length);
        for (int i = 1, i2, i3; i < data.length; i = i3 + data[i2]) {
            i2 = i + 2;
            i3 = i + 3;
            int limit;
            byte[] limitA;
            int lowToHigh = ((data[i] & 0xFF) << 8) | (data[i + 1] & 0xFF);
            switch (lowToHigh)
            {
                case 0x9F06:
                    theAidTable.setAid(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF01:
                    theAidTable.setNeedCompleteMatching(data[i3]);
                    break;
                case 0x9F08:
                case 0x9F09:
                    theAidTable.setAppVersionNumber(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF11:
                    theAidTable.setTACDefault(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF12:
                    theAidTable.setTACOnline(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF13:
                    theAidTable.setTACDenial(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF14:
                    theAidTable.setDefaultDDOL(ByteUtil.arrayToHexStr(data, i3, data[i2]));
                    break;
                case 0xDF15: // 有 NumberUtil.byte4ToInt(byte[] src, int offset)
                             // 该多好!!! DuanCS@[20140708]
                    byte[] thresholdValue = new byte[data[i2]];
                    System.arraycopy(data, i3, thresholdValue, 0, thresholdValue.length);
                    theAidTable.setThresholdValue(NumberUtil.byte4ToInt(thresholdValue));
                    break;
                case 0xDF16:
                    theAidTable.setMaxTargetPercentage(data[i3]);
                    break;
                case 0xDF17:
                    theAidTable.setTargetPercentage(data[i3]);
                    break;
                case 0xDF18:
                    theAidTable.setSupportOnlinePin(data[i3]);
                    break;
                case 0xDF19: // 有 AppUtil.toAmount(byte[] src, int offset)
                             // 该多好!!! DuanCS@[20140708]
                    limitA = new byte[data[i2]]; // contactlessFloorLimit
                    System.arraycopy(data, i3, limitA, 0, limitA.length);
                    limit = AppUtil.toAmount(limitA);
                    theAidTable.setContactlessFloorLimit(limit);
                    break;
                case 0xDF20:
                    limitA = new byte[data[i2]]; // contactlessLimit
                    System.arraycopy(data, i3, limitA, 0, limitA.length);
                    limit = AppUtil.toAmount(limitA);
                    theAidTable.setContactlessLimit(limit);
                    break;
                case 0xDF21:
                    limitA = new byte[data[i2]]; // cvmLimit
                    System.arraycopy(data, i3, limitA, 0, limitA.length);
                    limit = AppUtil.toAmount(limitA);
                    theAidTable.setCvmLimit(limit);
                    break;
                case 0x9F1B:
                    limitA = new byte[data[i2]];
                    System.arraycopy(data, i3, limitA, 0, limitA.length);
                    theAidTable.setTermFloorLimit(NumberUtil.byte4ToInt(limitA));
                    break;
                case 0x9F7B:
                    limitA = new byte[data[i2]];
                    System.arraycopy(data, i3, limitA, 0, limitA.length);
                    theAidTable.setUpcashTransLimit(AppUtil.toAmount(limitA));
                    break;
            }
        }
        theAidTable.setTransReferCurrencyCode("0156");
        theAidTable.setTransReferCurrencyExponent((byte) 2);
        theAidTable.setDefaultTDOL("");
        theAidTable.setAppPreferredName("");
        theAidTable.setAppLabel("");

        return theAidTable;
    }

    private static CAPKTable getCAPKParamInfo(byte[] data)
    {
        CAPKTable theCapkTable = new CAPKTable();
        byte[] tlvData = new byte[data.length];
        byte[] tmpData = null;
        System.arraycopy(data, 0, tlvData, 0, tlvData.length);

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x02});
        theCapkTable.setModul(ByteUtil.arrayToHexStr(tmpData));

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0x9f, (byte) 0x06});
        theCapkTable.setRID(ByteUtil.arrayToHexStr(tmpData));

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0x9f, (byte) 0x22});
        theCapkTable.setCapki(tmpData[0]);

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x05});
        if(tmpData.length==4)
            theCapkTable.setExpiry(StringUtil.toHexString(tmpData));
        else if(tmpData.length == 8)
            theCapkTable.setExpiry(new String(tmpData));

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x06});
        theCapkTable.setHashIndex(tmpData[0]);

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x07});
        theCapkTable.setArithIndex(tmpData[0]);

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x04});
        theCapkTable.setExponent(ByteUtil.arrayToHexStr(tmpData));

        tmpData = UtilityTLV.getTlvData(tlvData, new byte[]{(byte) 0xdf, (byte) 0x03});
        theCapkTable.setChecksum(ByteUtil.arrayToHexStr(tmpData));

        return theCapkTable;
    }
}
