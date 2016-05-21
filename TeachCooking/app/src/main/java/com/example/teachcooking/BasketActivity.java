package com.example.teachcooking;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.example.teachcooking.adpter.BasketActivityAdapter;
import com.example.teachcooking.entity.BasketInfo;
import com.example.teachcooking.entity.CookBookInfo;
import com.example.teachcooking.entity.UsuerInfo;
import com.example.teachcooking.util.ShowToast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BasketActivity extends Fragment implements OnClickListener {
    private ListView listView;
    private BasketActivityAdapter adapter;
    private View header;
    private UsuerInfo usuerInfo;
    private List<CookBookInfo> cookBookInfos;
    private TextView add_basket_number;
    private TextView basket_clear;
    private ProgressBar basket_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_basket, null);
        listView = (ListView) view.findViewById(R.id.activity_basket_listview);
        basket_clear = (TextView) view.findViewById(R.id.basket_clear);
        basket_progress = (ProgressBar) view.findViewById(R.id.basket_progress);
        header = LayoutInflater.from(getActivity()).inflate(
                R.layout.activity_basket_header, null);
        add_basket_number = (TextView) header
                .findViewById(R.id.add_basket_number);
        basket_clear.setOnClickListener(this);
        listView.addHeaderView(header);
        return view;
    }


    private void init() {
        adapter = new BasketActivityAdapter(new ArrayList<CookBookInfo>(),
                getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        getBasket();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getBasket();
    }

    //获取菜篮数据
    private void getBasket() {
        basket_progress.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        usuerInfo = BmobUser.getCurrentUser(getActivity(), UsuerInfo.class);
        cookBookInfos = new ArrayList<CookBookInfo>();
        if (usuerInfo != null) {
            BmobQuery<BasketInfo> query = new BmobQuery<BasketInfo>();
            query.addWhereEqualTo("user", usuerInfo);
            query.order("-createdAt");
            query.include("cookBookInfo,author");
            query.findObjects(getActivity(), new FindListener<BasketInfo>() {

                @Override
                public void onSuccess(List<BasketInfo> object) {
                    List<BasketInfo> list = object;
                    for (BasketInfo basketInfo : list) {
                        UsuerInfo author = basketInfo.getAuthor();
                        CookBookInfo cookBookInfo = basketInfo
                                .getCookBookInfo();
                        cookBookInfo.setAuthor(author);
                        cookBookInfos.add(cookBookInfo);
                    }
                    adapter.setCookBookInfos(cookBookInfos);
                    add_basket_number.setText("已添加 " + cookBookInfos.size()
                            + " 个");
                }

                @Override
                public void onError(int code, String msg) {
                    ShowToast.ShowS("msg");
                }
            });
        }
        basket_progress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void dialog1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认清空?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                basket_progress.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                cleanBasket();
                basket_progress.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                //  dialog.dismiss(); //关闭dialog
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        dialog1();

    }

    private void cleanBasket() {
        if (usuerInfo != null) {
            BmobQuery<BasketInfo> query = new BmobQuery<BasketInfo>();
            query.addWhereEqualTo("user", usuerInfo);
            query.order("-createdAt");
            query.include("cookBookInfo,author");
            query.findObjects(getActivity(), new FindListener<BasketInfo>() {

                @Override
                public void onSuccess(List<BasketInfo> object) {
                    List<BasketInfo> list = object;
                    List<BmobObject> basketinfo = new ArrayList<BmobObject>();
                    for (BasketInfo basketInfo : list) {
                        BasketInfo basket = new BasketInfo();
                        basket.setObjectId(basketInfo.getObjectId());
                        basketinfo.add(basket);
                    }
                    new BmobObject().deleteBatch(getActivity(), basketinfo,
                            new DeleteListener() {

                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getActivity(), "清空成功",
                                            Toast.LENGTH_SHORT).show();
                                    adapter.setCookBookInfos(new ArrayList<CookBookInfo>());
                                    add_basket_number.setText("已添加 0 个");
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Toast.makeText(getActivity(), "清空失败，请检查网络",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onError(int code, String msg) {

                }
            });
        }
    }
}
