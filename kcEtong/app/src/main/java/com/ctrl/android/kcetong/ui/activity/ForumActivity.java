package com.ctrl.android.kcetong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.ForumCategory;
import com.ctrl.android.kcetong.model.ForumCategoryBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.util.Utils;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.ForumCategaryAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.ctrl.third.common.library.utils.AnimUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @项目名称: 诚信行<br>
 * @类描述: 论坛<br>
 * @创建人： whs <br>
 * @创建时间： 2017/1/20 15:15 <br>
 * @修改人： <br>
 * @修改时间: 2017/1/20 15:15 <br>
 */
public class ForumActivity extends BaseActivity {

    @BindView(R.id.listView)
    ListView     listView;
    @BindView(R.id.activity_forum)
    LinearLayout activityForum;

    private String TITLE = StrConstant.COMMUNITY_FORUM_TITLE;
    private String               communityId;
    private List<ForumCategory>  listForumCategory;
    private ForumCategaryAdapter forumCategaryAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        toolbarBaseSetting(TITLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(ForumActivity.this);
                ForumActivity.this.finish();
            }
        });
        communityId = AppHolder.getInstance().getHouse().getCommunityId();//社区id 具体问题具体分析
        GetallForumAreaList(communityId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        communityId = AppHolder.getInstance().getHouse().getCommunityId();//社区id 具体问题具体分析
        GetallForumAreaList(communityId);
    }

    private void GetallForumAreaList(String communityId){
        Map<String,String> map = new HashMap<>();
        map.putAll(ConstantsData.getSystemParams());
        map.put(ConstantsData.METHOD, Url.getForumDateUrl);
        map.put("communityId",communityId);
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign",sign);
        map.remove(ConstantsData.METHOD);
        RetrofitUtil.Api().getForumlist(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ForumCategoryBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ForumCategoryBean forumCategoryBean) {
                super.onNext(forumCategoryBean);
                if(TextUtils.equals("000",forumCategoryBean.getCode())){
                    listForumCategory=forumCategoryBean.getData().getForumCategoryList();
                    forumCategaryAdapter = new ForumCategaryAdapter(ForumActivity.this);
                    forumCategaryAdapter.setList(listForumCategory);
                    listView.setAdapter(forumCategaryAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //MessageUtils.showShortToast(ForumActivity.this,listForumCategory.get(position).getName());
                            Intent intent = new Intent(ForumActivity.this, ForumeListActivity.class);
                            intent.putExtra("title",listForumCategory.get(position).getName());
                            intent.putExtra("categoryId",listForumCategory.get(position).getForumCategoryId());
                            startActivity(intent);
                            AnimUtil.intentSlidIn(ForumActivity.this);
                        }
                    });
                }else if(TextUtils.equals("002", forumCategoryBean.getCode())){
                    Utils.toastError(ForumActivity.this, StrConstant.NODATA);
                }
            }
        });


    }

}
