package com.example.zcc.lxtxvideodownload.file;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zcc.lxtxvideodownload.R;
import com.example.zcc.lxtxvideodownload.base.BaseFragment;
import com.example.zcc.lxtxvideodownload.base.Constant;
import com.example.zcc.lxtxvideodownload.base.readapter.recyclerview.CommonAdapter;
import com.example.zcc.lxtxvideodownload.base.readapter.recyclerview.MultiItemTypeAdapter;
import com.example.zcc.lxtxvideodownload.base.readapter.recyclerview.base.ViewHolder;
import com.example.zcc.lxtxvideodownload.base.rxbus.RxBus;
import com.example.zcc.lxtxvideodownload.base.rxbus.annotation.Subscribe;
import com.example.zcc.lxtxvideodownload.base.utils.AppUtils;
import com.example.zcc.lxtxvideodownload.base.utils.DisplayUtil;
import com.example.zcc.lxtxvideodownload.base.widget.SimpleDividerItemDecoration;
import com.pbrx.mylib.download.DownInfo;
import com.pbrx.mylib.download.DownState;
import com.pbrx.mylib.download.HttpDownManager;
import com.pbrx.mylib.download.HttpDownOnNextListener;
import com.pbrx.mylib.util.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileFragment extends BaseFragment {


    @BindView(R.id.recyView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    private HttpDownManager manager;
    private List<DownInfo> dowmList = new ArrayList<>();
    private DownloadAdapter adapter;

    public FileFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_file;
    }

    @Override
    protected void initViews() {
        RxBus.get().register(this);
        createLayoutManger(recyclerView);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), DisplayUtil.dip2px(2)));
        adapter = new DownloadAdapter(getActivity(), dowmList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        adapter.getDatas().get(position).getFileName());
                if (outputFile.exists()) {

                }
                return false;
            }
        });
       setNewDatas();
        mRefresh.setEnableLoadMore(false);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                setNewDatas();
                if(mRefresh.isRefreshing()){
                    mRefresh.finishRefresh();
                }
            }
        });
    }

    private void setNewDatas() {
        List<DownInfo> newDownList=new ArrayList<>();
        for (String name :getFilesAllName(Constant.DOWN_PATH)) {
            DownInfo apkApi = new DownInfo();
            apkApi.setId(10);
            Log.e("edturlname=====", name);
            File file=new File(name);
            apkApi.setFileName(file.getName());
            apkApi.setSavePath(name);
            newDownList.add(apkApi);
        }
        Log.e("edturlsize====", dowmList.size()+"");
        adapter.setDatas(newDownList);
        adapter.notifyDataSetChanged();

    }

    @Subscribe()
    public void getVideoUrl(String edtUrl) {
        DownInfo apkApi = new DownInfo(edtUrl);
        apkApi.setUpdateProgress(true);
        File outputFile = new File(Constant.DOWN_PATH,
                AppUtils.fileName(edtUrl));
        apkApi.setSavePath(outputFile.getAbsolutePath());
        Log.e("edturl1111=====", AppUtils.fileName(edtUrl));
        Log.e("edturl2222=====", apkApi.getSavePath());
        apkApi.setFileName(AppUtils.fileName(edtUrl));
        apkApi.setId(5);
        if (!containUrl(edtUrl)) {
            dowmList.add(apkApi);
        }
        Log.e("dowmList.size=====", dowmList.size() + "");
//        adapter.setDatas(dowmList);
        adapter.notifyDataSetChanged();
    }

    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }
    private boolean containUrl(String url) {
        boolean contain = false;
        for (DownInfo d : dowmList) {
            if (d.getUrl().equals(url)) {
                contain = true;
            }
        }
        return contain;
    }

    class DownloadAdapter extends CommonAdapter<DownInfo> {
        private HttpDownManager manager;

        public DownloadAdapter(Context context, List<DownInfo> datas) {
            super(context, R.layout.travel_download_item, datas);
            manager = HttpDownManager.getInstance();
        }

        @Override
        protected void convert(ViewHolder holder, final DownInfo baseBean, int position) {
            final TextView tvLook = holder.getView(R.id.tv_look);
            final ProgressBar progressBar = holder.getView(R.id.progressbar);
            final File file = new File(baseBean.getSavePath());
            holder.setText(R.id.tv_name, baseBean.getFileName());
            if (file.exists()) {
                tvLook.setText("查看");
            } else {
                tvLook.setText("下载");
            }
            if (baseBean.getId() == 5 && AppUtils.checkStrsNoNull(baseBean.getUrl())) {
                manager.startDown(baseBean);
            }
            progressBar.setMax((int) baseBean.getCountLength());
            progressBar.setProgress((int) baseBean.getReadLength());
            baseBean.setListener(new HttpDownOnNextListener<DownInfo>() {
                @Override
                public void onNext(DownInfo o) {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onComplete() {
                    tvLook.setText("查看");
                }

                @Override
                public void updateProgress(long readLength, long countLength) {
                    LogUtil.e("length===", readLength + "countLength===" + countLength);
                    progressBar.setMax((int) countLength);
                    progressBar.setProgress((int) readLength);
                }
            });
            tvLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(baseBean.getUrl())&&("下载").equals(tvLook.getText().toString())) {
                        toastShort("下载地址为空，无法下载查看");
                        return;
                    }
                    if (!file.exists() && tvLook.getText().toString().equals("下载")) {
                        if (baseBean.getState() != DownState.FINISH) {
                            manager.startDown(baseBean);
                            tvLook.setText("下载中");
                        }
                    } else if (("查看").equals(tvLook.getText().toString())) {
                        try {
                            if (TextUtils.isEmpty(baseBean.getFileName())) {
                                toastShort("未命名文件，无法查看");
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = getMimeType(baseBean.getFileName());
                            //设置intent的data和Type属性。
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(getActivity(), Constant.FILE_PROVIDER, file);
                                intent.setDataAndType(contentUri, type);
                            } else {
                                intent.setDataAndType(Uri.fromFile(file), type);
                            }
//                            intent.setDataAndType(Uri.fromFile(file), type);
                            startActivity(intent);
                        } catch (Exception e) {
                            LogUtil.e("查看exception===", e.toString());
                            toastShort("没有任何与该类型文件关联的程序");
                        }

                    }

                }
            });
        }


    }

    /**
     * 获取文件MimeType
     *
     * @param filename
     * @return
     */
    public static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
