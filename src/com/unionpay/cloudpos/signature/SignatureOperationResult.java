package com.unionpay.cloudpos.signature;

import com.unionpay.cloudpos.OperationResult;

public interface SignatureOperationResult extends OperationResult {
    /**
     * 返回签字
     * 
     * @return 签字buffer
     */
    byte[] getSignature();
    
    /**
     * 返回签字长度
     * 
     * @return 签字buffer长度
     */    
    int getSignatureLength();

}
