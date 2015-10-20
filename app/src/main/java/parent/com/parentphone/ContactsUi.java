package parent.com.parentphone;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import parent.com.parentphone.bean.ContactBean;
import parent.com.parentphone.util.Utils;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package PACKAGE_NAME
 * @date 15/9/20下午7:24
 * @Description
 */
public class ContactsUi {
    /**
     * 每行的图片数量
     */
    private static final int ROW_COUNT = 2;
    /**
     * 图片之间的间距，单位是dp
     */
    private static final int IMAGE_PADDING = 4;
    private Activity mActivity;
    private ContactsUiController mController;
    private View mRootView;//根布局
    private RecyclerView mRecyclerView;
    private List<ContactBean> mData;
    private ContactsListAdapter mAdapter;

    public ContactsUi(Activity activity, ContactsUiController controller) {
        this.mActivity = activity;
        this.mController = controller;
        mActivity.setContentView(R.layout.activity_main);
        findView();
    }

    public void findView() {
        mRootView = mActivity.getWindow().getDecorView();
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.list);
    }

    private void initAdapter(List<ContactBean> data) {
        mAdapter = new ContactsListAdapter(mActivity, mController, data);
    }

    private void initRecyclerView() {
        GridLayoutManager lm = new GridLayoutManager(mActivity, ROW_COUNT);
        GridLayoutManager.SpanSizeLookup lookUp = new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };

        lm.setSpanSizeLookup(lookUp);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpacesItemDecoration());
    }

    /**
     * @param data
     */
    public void setDataAndShow(@Nullable List<ContactBean> data) {
        mData = data;
        initAdapter(mData);
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        int padding = Utils.dp2px(mActivity, IMAGE_PADDING);

        public SpacesItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int adapterPosition = parent.getChildAdapterPosition(view);
            switch (adapterPosition % 2) {
                case 0:
                    outRect.left = padding;
                    outRect.right = padding / 2;
                    outRect.bottom = padding / 2;
                    outRect.top = padding / 2;
                    break;
                case 1:
                    outRect.left = padding / 2;
                    outRect.right = padding;
                    outRect.bottom = padding / 2;
                    outRect.top = padding / 2;
                    break;
            }
        }
    }
}
