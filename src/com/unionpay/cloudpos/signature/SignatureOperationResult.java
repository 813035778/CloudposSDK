package com.unionpay.cloudpos.signature;

import com.unionpay.cloudpos.OperationResult;

public interface SignatureOperationResult extends OperationResult {
    /**
     * 作电子签购单签名用途时，返回签字图像经JBIG1压缩后的数据
     * 
     * @return 签字buffer
     */
    byte[] getSignature();
    
    /**
     * 作电子签购单签名用途时，返回压缩数据长度
     * 
     * @return buffer长度
     */    
    int getSignatureLength();
    
    /**
     * 返回用户签字图像，如果是电子签购单，则返回用户签名和水印合成的图像；如果是其它签名，则直接返回用户签名图像
     * 
     * @return 签字图像
     */    
    Bitmap getSignature();

}
