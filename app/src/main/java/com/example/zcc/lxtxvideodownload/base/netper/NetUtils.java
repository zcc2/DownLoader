package com.example.zcc.lxtxvideodownload.base.netper;

import com.pbrx.mylib.base.LibPresenter;

public class NetUtils extends LibPresenter<NetUInterface> {
    protected NetUtils(NetUInterface uiInterface) {
        super(uiInterface);
    }

//    public static void post(String url, BaseRequestBean baseRequestBean, LibObserver libObserver) {
//        Map<String, String> param= null;
//        try {
//            param = TOUtils.toParamMap(baseRequestBean);
//        } catch (TOUtils.NullOauthTokenException e) {
//            e.printStackTrace();
//        }
//        Subscription subscription = LibNetManager.getInstance().create(BaseApi.class, LibConstant.base).post(url,param)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(libObserver);
//
//    }
}
