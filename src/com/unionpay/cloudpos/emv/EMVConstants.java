package com.unionpay.cloudpos.emv;
/**
 * EMV中常量。
 * 
 */
public interface EMVConstants {

    /** 通道类型：接触式*/
    public static final int Channel_Type_IC = 0;
    
    /** 通道类型：非接触式*/
    public static final int Channel_Type_RF = 1;
    
    /** Application Confirm*/
    public static final int App_Confirm_OK = 0;
    public static final int App_Confirm_Cancel = -1;
    public static final int App_Confirm_Failure = -2;
    
    /** PIN input result*/
    public static final int PIN_Input_Success = 0;
    public static final int PIN_Input_Bypass = 1;
    public static final int PIN_Input_Cancel = -1;
    public static final int PIN_Input_Failure = -2;
    public static final int PIN_Input_Timeout = -3;
    
    /** 交易结果*/
    public static final int Process_Result_Approve = 0;
    public static final int Process_Result_Refuse = 1;
    /** 交易货币代码，人民币*/
    public static final String Currency_Code_RMB = "156";
    
    /** 交易货币代码，积分*/
    public static final String Currency_Code_JIFEN = "999";
    
    /** 行业识别码（IIN）*/
    public static final int TAG_42 = 0x42;
    /** 应用标识符(AID)*/
    public static final int TAG_4F = 0x4F;
    /** 应用标签*/
    public static final int TAG_50 = 0x50;
    /** 磁条2等效数据*/
    public static final int TAG_57 = 0x57;
    /** 应用主账号(PAN)*/
    public static final int TAG_5A = 0x5A;
    /** 目录定义文件(DDF)名称*/
    public static final int TAG_5D = 0x5D;
    /** 持卡人姓名*/
    public static final int TAG_5F20 = 0x5F20;
    /** 应用失效日期*/
    public static final int TAG_5F24 = 0x5F24;
    /** 应用生效日期*/
    public static final int TAG_5F25 = 0x5F25;
    /** 发卡行国家代码*/
    public static final int TAG_5F28 = 0x5F28;
    /** 交易货币代码*/
    public static final int TAG_5F2A = 0x5F2A;
    /** 首选语言*/
    public static final int TAG_5F2D = 0x5F2D;
    /** 服务码*/
    public static final int TAG_5F30 = 0x5F30;
    /** 应用主帐号序列号*/
    public static final int TAG_5F34 = 0x5F34;
    /** 发卡行URL*/
    public static final int TAG_5F50 = 0x5F50;
    /** 国际银行账号(IBAN)*/
    public static final int TAG_5F53 = 0x5F53;
    /** 银行标识符代码(BIC)*/
    public static final int TAG_5F54 = 0x5F54;
    /** 发卡行国家代码(alpha2格式)*/
    public static final int TAG_5F55 = 0x5F55;
    /** 发卡行国家代码(alpha3格式)*/
    public static final int TAG_5F56 = 0x5F56;
    /** 应用模板*/
    public static final int TAG_61 = 0x61;
    /** 文件控制信息(FCI)模板*/
    public static final int TAG_6F = 0x6F;
    /** 响应报文数据*/
    public static final int TAG_70 = 0x70;
    /** 响应报文模板格式2*/
    public static final int TAG_77 = 0x77;
    /** 发卡行脚本模板2*/
    public static final int TAG_72 = 0x72;
    /** 目录自定义模板*/
    public static final int TAG_73 = 0x73;
    /** 响应报文模板格式*/
    public static final int TAG_80 = 0x80;
    /** 应用交互特征(AIP)*/
    public static final int TAG_82 = 0x82;
    /** 专用文件(DF)名称*/
    public static final int TAG_84 = 0x84;
    /** 发卡行脚本命令*/
    public static final int TAG_86 = 0x86;
    /** 应用优先级权指示符*/
    public static final int TAG_87 = 0x87;
    /** 短文件标识符(SFI)*/
    public static final int TAG_88 = 0x88;
    /** 授权响应码*/
    public static final int TAG_8A = 0x8A;
    /** 卡片风险管理数据对象列表1*/
    public static final int TAG_8C = 0x8C;
    /** 卡片风险管理数据对象列表2*/
    public static final int TAG_8D = 0x8D;
    /** 持卡人验证方法(CVM)列表*/
    public static final int TAG_8E = 0x8E;
    /** CA公钥索引(PKI)*/
    public static final int TAG_8F = 0x8F;
    /** 发卡行公钥证书*/
    public static final int TAG_90 = 0x90;
    /** 发卡行认证数据*/
    public static final int TAG_91 = 0x91;
    /** 发卡行公钥余数*/
    public static final int TAG_92 = 0x92;
    /** 签名的静态应用数据(SAD)*/
    public static final int TAG_93 = 0x93;
    /** 应用文件定位器(AFL)*/
    public static final int TAG_94 = 0x94;
    /** 交易证书数据对象列表(TDOL)*/
    public static final int TAG_97 = 0x97;
    /** 交易日期*/
    public static final int TAG_9A = 0x9A;
    /** 交易类型*/
    public static final int TAG_9C = 0x9C;
    /** 目录数据文件(DDF)名称*/
    public static final int TAG_9D = 0x9D;
    /** 授权金额*/
    public static final int TAG_9F02 = 0x9F02;
    /** 其它金额*/
    public static final int TAG_9F03 = 0x9F03;
    /** 应用自定义数据*/
    public static final int TAG_9F05 = 0x9F05;
    /** 应用标识符(AID)-终端*/
    public static final int TAG_9F06 = 0x9F06;
    /** 应用用途控制(AUC)*/
    public static final int TAG_9F07 = 0x9F07;
    /** 卡片应用版本号*/
    public static final int TAG_9F08 = 0x9F08;
    /** 终端应用版本号*/
    public static final int TAG_9F09 = 0x9F09;
    /** 持卡人姓名扩展*/
    public static final int TAG_9F0B = 0x9F0B;
    /** 发卡行行为代码(IAC)-缺省*/
    public static final int TAG_9F0D = 0x9F0D;
    /** 发卡行行为代码(IAC)-拒绝*/
    public static final int TAG_9F0E = 0x9F0E;
    /** 发卡行行为代码(IAC)-联机*/
    public static final int TAG_9F0F = 0x9F0F;
    /** 发卡行应用数据*/
    public static final int TAG_9F10 = 0x9F10;
    /** 发卡行代码表索引*/
    public static final int TAG_9F11 = 0x9F11;
    /** 应用首选名称*/
    public static final int TAG_9F12 = 0x9F12;
    /** 上次联机应用交易计数器(ATC)寄存器*/
    public static final int TAG_9F13 = 0x9F13;
    /** 连续脱机交易下限*/
    public static final int TAG_9F14 = 0x9F14;
    /** PIN尝试计数器*/
    public static final int TAG_9F17 = 0x9F17;
    /** 终端国家代码*/
    public static final int TAG_9F1A = 0x9F1A;
    /** 终端最低限额*/
    public static final int TAG_9F1B = 0x9F1B;
    /** 磁条1自定义数据*/
    public static final int TAG_9F1F = 0x9F1F;
    /** 交易时间*/
    public static final int TAG_9F21 = 0x9F21;
    /** 连续脱机交易上限*/
    public static final int TAG_9F23 = 0x9F23;
    /** 应用密文(AC)*/
    public static final int TAG_9F26 = 0x9F26;
    /** 密文信息数据(CID)*/
    public static final int TAG_9F27 = 0x9F27;
    /** 发卡行公钥指数*/
    public static final int TAG_9F32 = 0x9F32;
    /** 应用交易计数器(ATC)*/
    public static final int TAG_9F36 = 0x9F36;
    /** 处理选项数据对象列表PDOL*/
    public static final int TAG_9F38 = 0x9F38;
    /** 应用货币代码*/
    public static final int TAG_9F42 = 0x9F42;
    /** 应用货币指数*/
    public static final int TAG_9F44 = 0x9F44;
    /** 数据认证码*/
    public static final int TAG_9F45 = 0x9F45;
    /** IC卡公钥证书*/
    public static final int TAG_9F46 = 0x9F46;
    /** IC卡公钥指数*/
    public static final int TAG_9F47 = 0x9F47;
    /** IC卡公钥余数*/
    public static final int TAG_9F48 = 0x9F48;
    /** 动态数据认证数据对象列表(DDOL)*/
    public static final int TAG_9F49 = 0x9F49;
    /** 静态数据认证标签列表*/
    public static final int TAG_9F4A = 0x9F4A;
    /** 签名的动态应用数据*/
    public static final int TAG_9F4B = 0x9F4B;
    /** IC动态数*/
    public static final int TAG_9F4C = 0x9F4C;
    /** 日志入口*/
    public static final int TAG_9F4D = 0x9F4D;
    /** 商户名称*/
    public static final int TAG_9F4E = 0x9F4E;
    /** 日志格式*/
    public static final int TAG_9F4F = 0x9F4F;
    /** 发卡行URL*/
    public static final int TAG_9F50 = 0x9F50;
    /** 应用货币代码*/
    public static final int TAG_9F51 = 0x9F51;
    /** 应用缺省行为(ADA)*/
    public static final int TAG_9F52 = 0x9F52;
    /** 连续脱机交易限制数(国际-货币)*/
    public static final int TAG_9F53 = 0x9F53;
    /** 累计脱机交易金额限制数*/
    public static final int TAG_9F54 = 0x9F54;
    /** 发卡行认证指示位*/
    public static final int TAG_9F56 = 0x9F56;
    /** 发卡行国家代码*/
    public static final int TAG_9F57 = 0x9F57;
    /** 连续脱机交易下限*/
    public static final int TAG_9F58 = 0x9F58;
    /** 连续脱机交易上限*/
    public static final int TAG_9F59 = 0x9F59;
    /** 发卡行URL2*/
    public static final int TAG_9F5A = 0x9F5A;
    /** 累计脱机交易金额上限*/
    public static final int TAG_9F5C = 0x9F5C;
    /** 持卡人证件号*/
    public static final int TAG_9F61 = 0x9F61;
    /** 持卡人证件类型*/
    public static final int TAG_9F62 = 0x9F62;
    /** 卡产品标识信息*/
    public static final int TAG_9F63 = 0x9F63;
    /** 电子现金重置阈值(EC Reset Threshold)*/
    public static final int TAG_9F6D = 0x9F6D;
    /** 连续脱机交易限制数(国际-国家)*/
    public static final int TAG_9F72 = 0x9F72;
    /** 货币转换因子*/
    public static final int TAG_9F73 = 0x9F73;
    /** 电子现金发卡行授权码(EC Issuer Authorization Code)*/
    public static final int TAG_9F74 = 0x9F74;
    /** 累计脱机交易金额限制数(双货币)*/
    public static final int TAG_9F75 = 0x9F75;
    /** 第2应用货币代码*/
    public static final int TAG_9F76 = 0x9F76;
    /** 电子现金余额上限(EC Balance Limit)*/
    public static final int TAG_9F77 = 0x9F77;
    /** 电子现金单笔交易限额(EC Single Transaction Limit)*/
    public static final int TAG_9F78 = 0x9F78;
    /** 电子现金余额(EC Balance)*/
    public static final int TAG_9F79 = 0x9F79;
    /** 电子现金终端支持指示器(EC Terminal Support Indicator)*/
    public static final int TAG_9F7A = 0x9F7A;
    /** 电子现金终端交易限额(EC Terminal Transaction Limit)*/
    public static final int TAG_9F7B = 0x9F7B;
    /** 文件控制信息(FCI)专有模板*/
    public static final int TAG_A5 = 0xA5;
    /** 发卡行自定义数据FCI*/
    public static final int TAG_BF0C = 0xBF0C;
    
}
