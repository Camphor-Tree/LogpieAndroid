package com.logpie.android.ui.layout;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.components.LogpieSystemSetting;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.TimeHelper;
import com.logpie.android.util.LogpieLog;

/**
 * This layout is able to achieve pull-to-refresh.
 * 
 * @author yilei
 * 
 */
public class LogpieRefreshLayout extends LinearLayout implements OnTouchListener
{
    /**
     * Status when pulling down
     */
    public static final int STATUS_PULL_TO_REFRESH = 0;

    /**
     * Status when releasing to refresh
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    /**
     * Status when doing refresh
     */
    public static final int STATUS_REFRESHING = 2;

    /**
     * Status when normal
     */
    public static final int STATUS_REFRESH_FINISHED = 3;

    /**
     * The initial speed should be 0
     */
    public static final int INIT_SCROLL_SPEED = 0;

    /**
     * 初始加速度 The initial accelerate speed;
     */
    public static final int INIT_ACCELERATE_SPEED = 8;

    /**
     * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
     */
    private static final String UPDATED_AT = "updated_at";

    /**
     * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。 The callback to be called when doing
     * refresh.
     */
    public interface PullToRefreshCallback
    {

        /**
         * The callback when doing refresh. Add the detail implementation logic
         * here. Note: this method is called in other thread so it doesn't need
         * start a new thread.
         */
        void onRefresh();
    }

    /**
     * 下拉刷新的回调接口
     */
    private PullToRefreshCallback mRefreshCallback;

    /**
     * 用于存储上次更新时间
     */
    private LogpieSystemSetting mSystemSetting;

    /**
     * 下拉头的View
     */
    private View mHeader;

    /**
     * 需要去下拉刷新的ListView
     */
    private ListView mListView;

    /**
     * 刷新时显示的进度条
     */
    private ProgressBar mProgressBar;

    /**
     * 指示下拉和释放的箭头
     */
    private ImageView mArrow;

    /**
     * 指示下拉和释放的文字描述
     */
    private TextView mDescriptionTextView;

    /**
     * 上次更新时间的文字描述
     */
    private TextView mUpdateAtTextView;

    /**
     * 下拉头的布局参数
     */
    private MarginLayoutParams mHeaderLayoutParams;

    /**
     * 上次更新时间的毫秒值
     */
    private long mLastUpdateTime;

    /**
     * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
     */
    private int mId = -1;

    /**
     * 下拉头的高度
     */
    private int mHideHeaderHeight;

    /**
     * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
     */
    private int currentStatus = STATUS_REFRESH_FINISHED;;

    /**
     * 记录上一次的状态是什么，避免进行重复操作
     */
    private int mLastStatus = currentStatus;

    /**
     * 手指按下时的屏幕纵坐标
     */
    private float mYwhenClickDown;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int mTouchSlop;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean mLoadOnce;

    /**
     * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
     */
    private boolean mAbleToPull;

    private static final String TAG = LogpieRefreshLayout.class.getName();

    public LogpieRefreshLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        layoutInit(context, attrs);
    }

    /**
     * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
     * 
     * Do the init operation, such as: hide the Header view, register the
     * onTouchListener()
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed && !mLoadOnce)
        {
            mHideHeaderHeight = -mHeader.getHeight();
            mHeaderLayoutParams = (MarginLayoutParams) mHeader.getLayoutParams();
            mHeaderLayoutParams.topMargin = mHideHeaderHeight;
            // TODO: setLayoutParam here will pollute the log. Because
            // setLayoutParams will actually call into requestLayout()
            // See:
            // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/1.5_r4/android/view/View.java#View.setLayoutParams%28android.view.ViewGroup.LayoutParams%29
            mHeader.setLayoutParams(mHeaderLayoutParams);
            mListView = (ListView) getChildAt(1);
            mListView.setOnTouchListener(this);
            mLoadOnce = true;
        }
    }

    /**
     * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。 Triggered when the ListView is touched.
     * This method will handle all the detail logic.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // Check whether current state is able to pull
        checkIsAbleToPull(event);
        if (mAbleToPull)
        {
            switch (event.getAction())
            {
            case MotionEvent.ACTION_DOWN:
                mYwhenClickDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float yMove = event.getRawY();
                int distance = (int) (yMove - mYwhenClickDown);
                // 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
                // If the finger is dragging down and the header is totally
                // hidden, then just ignore the drag down event
                if (distance <= 0 && mHeaderLayoutParams.topMargin <= mHideHeaderHeight)
                {
                    return false;
                }
                // The the distance doesn't larger than the maximum tolerance
                if (distance < mTouchSlop)
                {
                    return false;
                }
                if (currentStatus != STATUS_REFRESHING)
                {
                    // If the top margin already visible then set the status to
                    // "release to refresh"
                    if (mHeaderLayoutParams.topMargin > 0)
                    {
                        currentStatus = STATUS_RELEASE_TO_REFRESH;
                    }
                    else
                    {
                        // If the top margin still not visible, then should
                        // still showing the "pull to refresh" to let user to
                        // drag down more.
                        currentStatus = STATUS_PULL_TO_REFRESH;
                    }
                    // 通过偏移下拉头的topMargin值，来实现下拉效果
                    // set the drag distance as 1/2 to achieve the dragging
                    // effect.
                    mHeaderLayoutParams.topMargin = (distance / 2) + mHideHeaderHeight;
                    mHeader.setLayoutParams(mHeaderLayoutParams);
                }
                break;
            // When finger release touching
            case MotionEvent.ACTION_UP:
            default:
                if (currentStatus == STATUS_RELEASE_TO_REFRESH)
                {
                    // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                    // If the current status is "Release to refresh", then try
                    // to call refreshTask.
                    // TODO: Add refersh Task
                    LogpieLog.d(TAG, "Start to do refresh task!");
                    new RefreshingTask().execute();
                }
                else if (currentStatus == STATUS_PULL_TO_REFRESH)
                {
                    // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                    // If the current status is "Pull to refresh", then try to
                    // just hide the header
                    new HideHeaderTask().execute();
                }
                break;
            }
            // 时刻记得更新下拉头中的信息
            // Always remember to update the header view (switch down the arrow,
            // etc)
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH)
            {
                updateHeaderView();
                // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
                // If the current status is pull to refresh or release to
                // refresh, need to let ListView loss the focus, otherwise, the
                // item will be always in chosen state.
                mListView.setPressed(false);
                mListView.setFocusable(false);
                mListView.setFocusableInTouchMode(false);
                mLastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                // Should return true to ignore the ListView's scroll event.
                return true;
            }
        }
        return false;
    }

    /**
     * 给下拉刷新控件注册一个监听器。 Set the pullToRefresh callback.
     * 
     * @param listener
     *            监听器的实现。 Implementation of the calbackk
     * @param id
     *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。 To
     *            avoid multiple refresh layout's updateAtTimes' confliction,
     *            set the different id. The id is used to build the key of the
     *            storage.
     */
    public void setPullToRefreshCallback(PullToRefreshCallback callback, int id)
    {
        mRefreshCallback = callback;
        mId = id;
    }

    /**
     * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
     * 
     * When finish the refresh logic, should call this to set back the status
     * and record the last refresh time
     */
    private void finishRefreshing()
    {
        currentStatus = STATUS_REFRESH_FINISHED;
        mSystemSetting.setSystemSetting(UPDATED_AT + mId,
                String.valueOf(System.currentTimeMillis()));
        new HideHeaderTask().execute();
    }

    /**
     * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
     * 
     * According to current ListView's state to set the mAbleToPull. This should
     * be called firstly in callback method onTouch(), then it can judge it
     * should scroll the ListView or should do the "pull to refresh".
     * 
     * @param event
     */
    private void checkIsAbleToPull(MotionEvent event)
    {
        View firstChild = mListView.getChildAt(0);
        if (firstChild != null)
        {
            int firstVisiblePos = mListView.getFirstVisiblePosition();
            if (firstVisiblePos == 0 && firstChild.getTop() == 0)
            {
                if (!mAbleToPull)
                {
                    mYwhenClickDown = event.getRawY();
                }
                // 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
                // If the first element's top margin is 0 distance to the parent
                // layout, then it means that the ListView is scroll to the top.
                // Current state should be able to pull to refresh.
                mAbleToPull = true;
            }
            else
            {
                if (mHeaderLayoutParams.topMargin != mHideHeaderHeight)
                {
                    mHeaderLayoutParams.topMargin = mHideHeaderHeight;
                    mHeader.setLayoutParams(mHeaderLayoutParams);
                }
                mAbleToPull = false;
            }
        }
        else
        {
            // 如果ListView中没有元素，也应该允许下拉刷新
            mAbleToPull = true;
        }
    }

    /**
     * 更新下拉头中的信息。 Refresh the header information.
     */
    private void updateHeaderView()
    {
        if (mLastStatus != currentStatus)
        {
            if (currentStatus == STATUS_PULL_TO_REFRESH)
            {
                mDescriptionTextView.setText(LanguageHelper.getId(
                        LanguageHelper.KEY_PULL_TO_REFRESH, getContext()));
                mArrow.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                rotateArrow();
            }
            else if (currentStatus == STATUS_RELEASE_TO_REFRESH)
            {
                mDescriptionTextView.setText(LanguageHelper.getId(
                        LanguageHelper.KEY_RELEASE_TO_REFRESH, getContext()));
                mArrow.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                rotateArrow();
            }
            else if (currentStatus == STATUS_REFRESHING)
            {
                mDescriptionTextView.setText(LanguageHelper.getId(LanguageHelper.KEY_REFRESHING,
                        getContext()));
                mProgressBar.setVisibility(View.VISIBLE);
                mArrow.clearAnimation();
                mArrow.setVisibility(View.GONE);
            }
            setUpUpdateAtTextView(getContext());
        }
    }

    /**
     * 根据当前的状态来旋转箭头。 Based ont the current state to determine how to switch down
     * the arrow.
     */
    private void rotateArrow()
    {
        float pivotX = mArrow.getWidth() / 2f;
        float pivotY = mArrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULL_TO_REFRESH)
        {
            fromDegrees = 180f;
            toDegrees = 360f;
        }
        else if (currentStatus == STATUS_RELEASE_TO_REFRESH)
        {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        mArrow.startAnimation(animation);
    }

    private void layoutInit(Context context, AttributeSet attrs)
    {
        mSystemSetting = LogpieSystemSetting.getInstance(context);
        // Attach the refresh header into the view.
        mHeader = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null, true);
        mProgressBar = (ProgressBar) mHeader.findViewById(R.id.progress_bar);
        mArrow = (ImageView) mHeader.findViewById(R.id.arrow);
        mDescriptionTextView = (TextView) mHeader.findViewById(R.id.description);
        mUpdateAtTextView = (TextView) mHeader.findViewById(R.id.updated_at);
        setUpUpdateAtTextView(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setOrientation(VERTICAL);
        addView(mHeader, 0);
    }

    private void setUpUpdateAtTextView(Context context)
    {
        long lastUpdateTime;
        String lastUpdateTimeString = LogpieSystemSetting.getInstance(context).getSystemSetting(
                UPDATED_AT + mId);
        // If never update, set default to -1
        if (lastUpdateTimeString == null)
        {
            lastUpdateTime = -1;
        }
        else
        {
            lastUpdateTime = Long.valueOf(lastUpdateTimeString);
        }
        mUpdateAtTextView.setText(TimeHelper.getElapsedTimeString(lastUpdateTime, context));
    }

    class HideHeaderTask extends AsyncTask<Void, Integer, Integer>
    {

        @Override
        protected Integer doInBackground(Void... params)
        {
            int topMargin = mHeaderLayoutParams.topMargin;
            int speed = INIT_SCROLL_SPEED;
            while (true)
            {
                // The increase the speed when approaching the top
                topMargin = topMargin + speed;
                if (topMargin <= mHideHeaderHeight)
                {
                    topMargin = mHideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                // More approaching the top margin, then reduce the
                // accelerate_speed, but the speed should still increase.
                // Note: the speed is negative number
                speed = speed
                        - ((mHideHeaderHeight - mHeaderLayoutParams.topMargin) / mHideHeaderHeight)
                        * INIT_ACCELERATE_SPEED;
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin)
        {
            mHeaderLayoutParams.topMargin = topMargin[0];
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin)
        {
            mHeaderLayoutParams.topMargin = topMargin;
            mHeader.setLayoutParams(mHeaderLayoutParams);
            currentStatus = STATUS_REFRESH_FINISHED;
        }
    }

    /**
     * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
     * 
     * The refreshing task. This task will call the callback registered in this
     * layout.
     */
    class RefreshingTask extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            int topMargin = mHeaderLayoutParams.topMargin;
            // Initial speed
            int speed = INIT_SCROLL_SPEED;
            while (true)
            {
                topMargin = topMargin + speed;
                if (topMargin <= 0)
                {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                // More approaching the top margin, then reduce the
                // accelerate_speed, but the speed should still increase.
                // Note: the speed is negative number
                speed = speed - (mHeaderLayoutParams.topMargin / topMargin) * INIT_ACCELERATE_SPEED;
                sleep(10);
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(0);
            if (mRefreshCallback != null)
            {
                // Call the callback method onRefresh();
                mRefreshCallback.onRefresh();
                // Alway call finish when down with the refresh task.
                // Note: if onRefresh() method is asynchronous, it may have some
                // race condition bug. (Such as: hiding the header too early.)
                // Then should make finisheRefreshing() public, and called when
                // the async-function finish.
                finishRefreshing();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin)
        {
            updateHeaderView();
            mHeaderLayoutParams.topMargin = topMargin[0];
            mHeader.setLayoutParams(mHeaderLayoutParams);
        }

    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     * 
     * Make the current thread to sleep, the time unit is milli-second
     * 
     * @param time
     *            指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(int time)
    {
        try
        {
            Thread.sleep(time);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
