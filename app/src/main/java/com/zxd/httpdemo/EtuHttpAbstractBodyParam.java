package com.zxd.httpdemo;

import com.zxd.etuhttp.wrapper.param.AbstractBodyParam;

/**
 * author: zxd
 * created on: 2021/7/26 15:46
 * description:
 */
public class EtuHttpAbstractBodyParam <P extends AbstractBodyParam<P>,R extends EtuHttpAbstractBodyParam<P,R>> extends EtuHttp<P,R>{

    protected EtuHttpAbstractBodyParam(P param){
        super(param);
    }

    public final R setUploadMaxLength(long maxLength){
        param.setUploadMaxLength(maxLength);
        return (R) this;
    }

}
