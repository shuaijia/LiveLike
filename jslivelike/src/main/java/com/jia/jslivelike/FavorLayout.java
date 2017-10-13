package com.jia.jslivelike;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Description:贾帅
 * Created by jia on 2017/10/13.
 * 人之所以能，是相信能
 * <p/>
 * 关键技术：
 * 自定义view的一些基础方法以及一些注意点
 * 随机数的使用
 * 插补器的使用
 * 属性动画的高级用法
 * 贝塞尔曲线在Android中的实现以及应用
 * <p/>
 * 特点：
 * 爱心出现在底部并且水平居中
 * 爱心的颜色/类型 随机
 * 爱心进入时候有一个缩放的动画
 * 缩放完毕后,开始变速向上移动,并且伴随alpha渐变效果
 * 爱心移动的轨迹光滑,是个曲线
 */
public class FavorLayout extends RelativeLayout {

    private static final String TAG = "FavorLayout";

    // 实现随机效果
    private Random random = new Random();

    // 爱心高度
    private int iHeight = 120;
    // 爱心宽度
    private int iWidth = 120;
    // FavorLayout高度
    private int mHeight;
    // FavorLayout宽度
    private int mWidth;

    // 来控制子view的位置
    private LayoutParams lp;

    private Drawable aLove;
    private Drawable bLove;
    private Drawable cLove;
    private Drawable dLove;
    private Drawable eLove;

    private Drawable[] loves;

    // 为了实现 变速效果 挑选了几种插补器
    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速

    // 在init中初始化
    private Interpolator[] interpolators;


    public FavorLayout(Context context) {
        super(context);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件宽高（应在onMeasure方法中获取）
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void init() {
        //底部 并且 水平居中
        lp = new LayoutParams(iWidth, iHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE); //这里的TRUE 要注意 不是true
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        //初始化显示的图片
        loves = new Drawable[5];
        aLove = getResources().getDrawable(R.mipmap.love_a);
        bLove = getResources().getDrawable(R.mipmap.love_b);
        cLove = getResources().getDrawable(R.mipmap.love_c);
        dLove = getResources().getDrawable(R.mipmap.love_d);
        eLove = getResources().getDrawable(R.mipmap.love_e);

        //赋值给loves
        loves[0] = aLove;
        loves[1] = bLove;
        loves[2] = cLove;
        loves[3] = dLove;
        loves[4] = eLove;

        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
    }

    /**
     * 点赞
     * 对外暴露的方法
     */
    public void addFavor() {
        ImageView imageView = new ImageView(getContext());
        // 随机选一个
        imageView.setImageDrawable(loves[random.nextInt(loves.length)]);
        // 设置底部 水平居中
        imageView.setLayoutParams(lp);

        addView(imageView);
        Log.e(TAG, "addFavor: " + "add后子view数:" + getChildCount());

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    /**
     * 设置动画
     *
     * @param target
     * @return
     */
    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);//实现随机变速
        finalSet.setTarget(target);
        return finalSet;
    }


    /**
     * 设置初始动画
     * 渐变 并且横纵向放大
     *
     * @param target
     * @return
     */
    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    /**
     * 获取一条路径的两个控制点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        //减去100 是为了控制 x轴活动范围
        pointF.x = random.nextInt((mWidth - 100));
        //再Y轴上 为了确保第二个控制点 在第一个点之上,我把Y分成了上下两半
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    /**
     * 获取贝塞尔曲线动画
     *
     * @param target
     * @return
     */
    private ValueAnimator getBezierValueAnimator(View target) {

        //初始化一个BezierEvaluator
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        // 起点固定，终点随机
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - iWidth) / 2, mHeight - iHeight), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    /**
     * 设置item的大小
     *
     * @param iWidth
     * @param iHeight
     */
    public void setFavorItemSize(int iWidth, int iHeight) {
        this.iWidth = iWidth;
        this.iHeight = iHeight;
    }

    /**
     * 设置点赞效果集合
     *
     * @param items
     */
    public void setFavorItem(Drawable[] items) {
        this.loves = items;
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
            Log.v(TAG, "removeView后子view数:" + getChildCount());
        }
    }


}
