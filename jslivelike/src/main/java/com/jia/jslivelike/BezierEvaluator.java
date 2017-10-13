package com.jia.jslivelike;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Description: 动画估值器，以实现平滑动画
 * Created by jia on 2017/10/13.
 * 人之所以能，是相信能
 *
 * 由于我们view的移动需要控制x y 所以就传入PointF 作为参数
 *
 * TypeEvaluator:在获取动画对象时只需要传入起始和结束值系统就会自动完成值的平滑过渡,这个平滑过渡的完成就是靠TypeEvaluator这个类
 * PointF:点类，与Point一样，区别是其x和y值是float类型
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    // 两个控制点
    private PointF pointF1;
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1,PointF pointF2){
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float time, PointF startValue, PointF endValue) {
        float timeLeft = 1.0f - time;

        //结果
        PointF point = new PointF();

        PointF point0 = (PointF)startValue;//起点
        PointF point3 = (PointF)endValue;//终点

        // 贝塞尔公式
        point.x = timeLeft * timeLeft * timeLeft * (point0.x)
                + 3 * timeLeft * timeLeft * time * (pointF1.x)
                + 3 * timeLeft * time * time * (pointF2.x)
                + time * time * time * (point3.x);

        point.y = timeLeft * timeLeft * timeLeft * (point0.y)
                + 3 * timeLeft * timeLeft * time * (pointF1.y)
                + 3 * timeLeft * time * time * (pointF2.y)
                + time * time * time * (point3.y);

        return point;
    }
}
