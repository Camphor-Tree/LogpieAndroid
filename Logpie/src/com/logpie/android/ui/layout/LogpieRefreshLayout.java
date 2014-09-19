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
import com.logpie.android.datastorage.LogpieSystemSetting;
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
     * ��ʼ���ٶ� The initial accelerate speed;
     */
    public static final int INIT_ACCELERATE_SPEED = 8;

    /**
     * �ϴθ���ʱ����ַ�����������ΪSharedPreferences�ļ�ֵ
     */
    private static final String UPDATED_AT = "updated_at";

    /**
     * ����ˢ�µļ�������ʹ������ˢ�µĵط�Ӧ��ע��˼���������ȡˢ�»ص��� The callback to be
     * called when doing refresh.
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
     * ����ˢ�µĻص��ӿ�
     */
    private PullToRefreshCallback mRefreshCallback;

    /**
     * ���ڴ洢�ϴθ���ʱ��
     */
    private LogpieSystemSetting mSystemSetting;

    /**
     * ����ͷ��View
     */
    private View mHeader;

    /**
     * ��Ҫȥ����ˢ�µ�ListView
     */
    private ListView mListView;

    /**
     * ˢ��ʱ��ʾ�Ľ����
     */
    private ProgressBar mProgressBar;

    /**
     * ָʾ�������ͷŵļ�ͷ
     */
    private ImageView mArrow;

    /**
     * ָʾ�������ͷŵ���������
     */
    private TextView mDescriptionTextView;

    /**
     * �ϴθ���ʱ�����������
     */
    private TextView mUpdateAtTextView;

    /**
     * ����ͷ�Ĳ��ֲ���
     */
    private MarginLayoutParams mHeaderLayoutParams;

    /**
     * �ϴθ���ʱ��ĺ���ֵ
     */
    private long mLastUpdateTime;

    /**
     * Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ��ʹ��id�������
     */
    private int mId = -1;

    /**
     * ����ͷ�ĸ߶�
     */
    private int mHideHeaderHeight;

    /**
     * ��ǰ����ʲô״̬����ѡֵ��STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING �� STATUS_REFRESH_FINISHED
     */
    private int currentStatus = STATUS_REFRESH_FINISHED;;

    /**
     * ��¼��һ�ε�״̬��ʲô����������ظ�����
     */
    private int mLastStatus = currentStatus;

    /**
     * ��ָ����ʱ����Ļ�����
     */
    private float mYwhenClickDown;

    /**
     * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ��
     */
    private int mTouchSlop;

    /**
     * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
     */
    private boolean mLoadOnce;

    /**
     * ��ǰ�Ƿ����������ֻ��ListView������ͷ��ʱ�����������
     */
    private boolean mAbleToPull;

    private static final String TAG = LogpieRefreshLayout.class.getName();

    public LogpieRefreshLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        layoutInit(context, attrs);
    }

    /**
     * ����һЩ�ؼ��Եĳ�ʼ�����������磺������ͷ����ƫ�ƽ������أ���ListViewע��touch�¼���
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
     * ��ListView������ʱ���ã����д����˸�������ˢ�µľ����߼��� Triggered when the
     * ListView is touched. This method will handle all the detail logic.
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
                // �����ָ���»�״̬����������ͷ����ȫ���صģ������������¼�
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
                    // ͨ��ƫ������ͷ��topMarginֵ����ʵ������Ч��
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
                    // ����ʱ������ͷ�����ˢ��״̬����ȥ��������ˢ�µ�����
                    // If the current status is "Release to refresh", then try
                    // to call refreshTask.
                    // TODO: Add refersh Task
                    LogpieLog.d(TAG, "Start to do refresh task!");
                    new RefreshingTask().execute();
                }
                else if (currentStatus == STATUS_PULL_TO_REFRESH)
                {
                    // ����ʱ���������״̬����ȥ������������ͷ������
                    // If the current status is "Pull to refresh", then try to
                    // just hide the header
                    new HideHeaderTask().execute();
                }
                break;
            }
            // ʱ�̼ǵø�������ͷ�е���Ϣ
            // Always remember to update the header view (switch down the arrow,
            // etc)
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH)
            {
                updateHeaderView();
                // ��ǰ�����������ͷ�״̬��Ҫ��ListViewʧȥ���㣬���򱻵������һ���һֱ����ѡ��״̬
                // If the current status is pull to refresh or release to
                // refresh, need to let ListView loss the focus, otherwise, the
                // item will be always in chosen state.
                mListView.setPressed(false);
                mListView.setFocusable(false);
                mListView.setFocusableInTouchMode(false);
                mLastStatus = currentStatus;
                // ��ǰ�����������ͷ�״̬��ͨ���true���ε�ListView�Ĺ����¼�
                // Should return true to ignore the ListView's scroll event.
                return true;
            }
        }
        return false;
    }

    /**
     * ������ˢ�¿ؼ�ע��һ���������� Set the pullToRefresh callback.
     * 
     * @param listener
     *            ��������ʵ�֡� Implementation of the calbackk
     * @param id
     *            Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ��
     *            �벻ͬ������ע������ˢ�¼�����ʱһ��Ҫ���벻ͬ��id�� To avoid multiple
     *            refresh layout's updateAtTimes' confliction, set the different
     *            id. The id is used to build the key of the storage.
     */
    public void setPullToRefreshCallback(PullToRefreshCallback callback, int id)
    {
        mRefreshCallback = callback;
        mId = id;
    }

    /**
     * �����е�ˢ���߼���ɺ󣬼�¼����һ�£��������ListView��һֱ��������ˢ��״̬��
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
     * ��ݵ�ǰListView�Ĺ���״̬���趨 {@link #ableToPull}
     * ��ֵ��ÿ�ζ���Ҫ��onTouch�е�һ��ִ�У���������жϳ���ǰӦ���ǹ���ListView������Ӧ�ý���
     * ������
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
                // ����׸�Ԫ�ص��ϱ�Ե�����븸����ֵΪ0����˵��ListView���������������ʱӦ����������ˢ��
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
            // ���ListView��û��Ԫ�أ�ҲӦ����������ˢ��
            mAbleToPull = true;
        }
    }

    /**
     * ��������ͷ�е���Ϣ�� Refresh the header information.
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
     * ��ݵ�ǰ��״̬����ת��ͷ�� Based ont the current state to determine how to
     * switch down the arrow.
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
     * ����ˢ�µ������ڴ������л�ȥ�ص�ע�����������ˢ�¼�������
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
     * ʹ��ǰ�߳�˯��ָ���ĺ�����
     * 
     * Make the current thread to sleep, the time unit is milli-second
     * 
     * @param time
     *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
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
