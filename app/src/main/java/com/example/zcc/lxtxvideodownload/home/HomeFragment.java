package com.example.zcc.lxtxvideodownload.home;


import android.Manifest;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zcc.lxtxvideodownload.DownListActivity;
import com.example.zcc.lxtxvideodownload.R;
import com.example.zcc.lxtxvideodownload.base.BaseFragment;
import com.example.zcc.lxtxvideodownload.base.Constant;
import com.example.zcc.lxtxvideodownload.base.rxbus.RxBus;
import com.example.zcc.lxtxvideodownload.base.utils.AppUtils;
import com.pbrx.mylib.download.DownInfo;
import com.pbrx.mylib.download.HttpDownManager;
import com.pbrx.mylib.download.HttpDownOnNextListener;
import com.pbrx.mylib.permission.RequestResult;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    @BindView(R.id.edt_url)
    EditText mEdtUrl;
    @BindView(R.id.bt_download)
    Button mBtDownload;
    Unbinder unbinder;
    private HttpDownManager manager;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews() {
        manager = HttpDownManager.getInstance();
        RxBus.get().register(this);
        File file=new File( Constant.DOWN_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        requestPremission(new RequestResult() {
            @Override
            public void successResult() {

            }

            @Override
            public void failuerResult() {
                toastShort("权限拒绝无法下载");
                getActivity().finish();
            }
        },permissions);

    }
    @OnClick({R.id.tv_list, R.id.bt_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_list:
                goActivity(DownListActivity.class);
                break;
            case R.id.bt_download:
                String edtUrl=mEdtUrl.getText().toString().trim();
                if(TextUtils.isEmpty(edtUrl)){
                    toastShort("输入网址为空");
                    return;
                }

//               RxBus.get().post(edtUrl);
                try {
                    DownInfo apkApi = new DownInfo(edtUrl);
                    apkApi.setUpdateProgress(true);
                    File outputFile = new File(Constant.DOWN_PATH,
                            AppUtils.fileName(edtUrl));
                    apkApi.setSavePath(outputFile.getAbsolutePath());
                    apkApi.setFileName(AppUtils.fileName(edtUrl));
                    progressDialog =  new ProgressDialog(getActivity());//实例化ProgressDialog
                    progressDialog.setMax(100);//设置最大值
                    progressDialog.setTitle("下载");//设置标题
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置样式为横向显示进度的样式
                    progressDialog.setMessage("正在下载，请稍后！");
                    progressDialog.incrementProgressBy(0);//设置初始值为0，其实可以不用设置，默认就是0
                    progressDialog.setIndeterminate(false);//是否精确显示对话框，flase为是，反之为否
                    //是否可以通过返回按钮退出对话框
                    progressDialog.setCancelable(true);
                    progressDialog.show();//显示对话框
                    apkApi.setListener(new HttpDownOnNextListener<DownInfo>() {
                        @Override
                        public void onNext(DownInfo o) {

                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            toastShort("下载成功，请去记录查看");
                            progressDialog.dismiss();
                        }

                        @Override
                        public void updateProgress(long readLength, long countLength) {
                            Log.e("length===", readLength + "countLength===" + countLength);
//                        progressBar.setMax((int) countLength);
//                        progressBar.setProgress((int) readLength);
                            if(progressDialog!=null){
                                double read=  (readLength*1.00/countLength*100);
                                Log.e("read===", read +"");
                                progressDialog.setProgress((int) read);
                                if(100==read){
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });
                    manager.startDown(apkApi);
                }catch (Exception e){
                    toastShort("无法解析该网址");
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }

                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
