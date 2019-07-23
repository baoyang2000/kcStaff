package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ForumNote;
import com.ctrl.android.kcetong.model.ForumNoteBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.BaseLinearLayoutManager;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ForumListtAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.android.kcetong.ui.view.DividerDecoration;
import com.ctrl.third.common.library.utils.AnimUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForumeListActivity extends BaseActivity {
    @BindView(R.id.image_right)
    ImageView     imageRight;
    @BindView(R.id.recyclerView_notice_data)
    LRecyclerView recyclerViewNoticeData;
    @BindView(R.id.activity_forume_list)
    LinearLayout  activityForumeList;
    private String TITLE = "";
    private String categoryId_extra;//论坛板块分类id
    private int currentPage=1;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private ForumListtAdapter    Adapter;
    private List<ForumNote> list                  = new ArrayList<>();
    private int             parentRowCountPerPage =0;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forume_list);
        ButterKnife.bind(this);
        TITLE = getIntent().getStringExtra("title");
        categoryId_extra = getIntent().getStringExtra("categoryId");
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(ForumeListActivity.this);
                ForumeListActivity.this.finish();
            }
        });
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    showForumPopWindow(imageRight);
                }else if("1".equals(AppHolder.getInstance().getMemberInfo().getSupers())
                         || "2".equals(AppHolder.getInstance().getMemberInfo().getSupers())){
                    Utils.showShortToast(ForumeListActivity.this, getString(R.string.manager_cannot));
                }

            }
        });
        Adapter=new ForumListtAdapter(this);
        Adapter.setDataList(list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(Adapter);
        recyclerViewNoticeData.setLayoutManager(new BaseLinearLayoutManager(this));
        final DividerDecoration divider = new DividerDecoration.Builder(this).setHeight(R.dimen.default_divider_height).setColorResource(R.color.main_bg).build();
        recyclerViewNoticeData.addItemDecoration(divider);
        recyclerViewNoticeData.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerViewNoticeData.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                String categoryId = categoryId_extra;
                String memberId = "";
                String handleStatus = "";
                String verifyStatus = "";
                getAllForumNoteList(categoryId, memberId, handleStatus, verifyStatus );
            }
        });
        recyclerViewNoticeData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                if (parentRowCountPerPage == 10) {
                    RecyclerViewStateUtils.setFooterViewState(ForumeListActivity.this, recyclerViewNoticeData, 5, LoadingFooter.State.Loading, null);
                    String categoryId = categoryId_extra;
                    String memberId = "";
                    String handleStatus = "";
                    String verifyStatus = "";
                    getAllForumNoteList(categoryId, memberId, handleStatus, verifyStatus);

                } else {
                    RecyclerViewStateUtils.setFooterViewState(ForumeListActivity.this, recyclerViewNoticeData, 10, LoadingFooter.State.Normal, null);
                }
            }
        });
        recyclerViewNoticeData.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ForumeListActivity.this, ForumDetailActivity.class);
                intent.putExtra("title",TITLE);
                intent.putExtra("forumPostId",list.get(position).getForumPostId());
                startActivity(intent);
                AnimUtil.intentSlidIn(ForumeListActivity.this);
            }

            @Override
            public void onItemLongClick(View view, int i) {

            }
        });
        recyclerViewNoticeData.setRefreshing(true);

    }

    //获得所有帖子列表
private void getAllForumNoteList(String categoryId,String memberId,String handleStatus,
        String verifyStatus){
    showProgress(true);
    Map<String,String> map = new HashMap<>();
    map.putAll(ConstantsData.getSystemParams());
    map.put(ConstantsData.METHOD, Url.getForumnoticlistUrl);
    map.put("categoryId", categoryId);
    map.put("memberId", memberId);
    map.put("handleStatus", handleStatus);
    map.put("verifyStatus", verifyStatus);
    map.put("currentPage",  String.valueOf(currentPage));
    map.put("rowCountPerPage", String.valueOf(ConstantsData.ROW_COUNT_PER_PAGE));
    String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
    map.put("sign",sign);
    map.remove(ConstantsData.METHOD);
    RetrofitUtil.Api().getAllForumnitic(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ForumNoteBean>(this) {
        @Override
        public void onResponseCallback(JSONObject response, String resultCode) {
            LLog.d("onResponseCallback: " + response);
            showProgress(false);
        }

        @Override
        public void onNext(ForumNoteBean forumNoteBean) {
            super.onNext(forumNoteBean);
            if(TextUtils.equals("000",forumNoteBean.getCode())){
                if (currentPage == 1) {
                    list.clear();
                }
                parentRowCountPerPage=forumNoteBean.getData().getForumPostList().size();
                list.addAll(forumNoteBean.getData().getForumPostList());
                Adapter.setDataList(list);
                Adapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
            recyclerViewNoticeData.refreshComplete();
            showProgress(false);
        }

        @Override
        public void onNetError(Throwable e) {
            super.onNetError(e);
            if (currentPage > 0) {
                currentPage--;
            }
            recyclerViewNoticeData.refreshComplete();
            showProgress(false);

        }
    });
}
    /**
     * 显示选项
     * */
    private void showForumPopWindow(View view) {
        View     contentView                  = LayoutInflater.from(this).inflate(R.layout.pop_window_forum_write,null);
        TextView popwindow_start_note_btn     =(TextView)contentView.findViewById(R.id.popwindow_start_note_btn);
        TextView popwindow_start_activity_btn =(TextView)contentView.findViewById(R.id.popwindow_start_activity_btn);

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 0);
        //popupWindow.showAsDropDown(view);
        popwindow_start_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MessageUtils.showShortToast(ForumeSsenceActivity.this, "发表帖子");
                Intent intent = new Intent(ForumeListActivity.this, ForumStartNoteActivity.class);
                intent.putExtra("categoryId",categoryId_extra);
                startActivityForResult(intent,5000);
                AnimUtil.intentSlidIn(ForumeListActivity.this);
                popupWindow.dismiss();
            }
        });
        popwindow_start_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MessageUtils.showShortToast(ForumeSsenceActivity.this, "发起活动");
                Intent intent = new Intent(ForumeListActivity.this, ForumStartActActivity.class);
                intent.putExtra("categoryId",categoryId_extra);
                startActivity(intent);
                AnimUtil.intentSlidIn(ForumeListActivity.this);
                popupWindow.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (5001 == resultCode && requestCode == 5000){
            recyclerViewNoticeData.setRefreshing(true);

        }
    }
}
