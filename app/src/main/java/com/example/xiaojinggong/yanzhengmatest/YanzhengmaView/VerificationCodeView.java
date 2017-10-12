package com.example.xiaojinggong.yanzhengmatest.YanzhengmaView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.xiaojinggong.yanzhengmatest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xiaojinggong on 10/12/17.
 */

public class VerificationCodeView extends View {
    private int mTextSize;
    private int mTextCount;
    //view 高和宽
    private int mWidth;
    private int mHeight;

    private float mTextWidth;
    private int mTextHeight;

    private Paint mTextpaint;
    private Paint mPointPaint;
    private Paint mPathPaint;

    private List<PointF> mPoints = new ArrayList<>();
    private List<Path> mPaths = new ArrayList<>();

    private String mText;

    private Random mrandom=new Random();


    public String getmText() {
        return mText;
    }

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
        mTextSize = array.getDimensionPixelSize(R.styleable.VerificationCodeView_textSize,
                (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mTextCount = array.getInt(R.styleable.VerificationCodeView_textCount, 4);
        array.recycle();//释放资源
        setOnClickListener(new OnClickListener() {//点击变化
            @Override
            public void onClick(View view) {
                mText = getCharAndNum(mTextCount);
                invalidate();
            }
        });


        intiPaint();
    }
    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            mWidth = specSize;
        }else {
            if(specMode == MeasureSpec.AT_MOST){
                if (specMode == MeasureSpec.AT_MOST) {
                    mWidth = Math.min((int) (mTextWidth * 1.8f), specSize);//控制宽度
                }
            }
        }
        //高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else {
            if(specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min((int)(mTextHeight*16f),specSize);//控制高度
            }
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.GREEN);
        initData();
        float charLength = mTextWidth / mTextCount;
        //绘制验证码
        for(int i=1;i<mTextCount;i++){
            int offsetDegree = mrandom.nextInt(15);
            offsetDegree = mrandom.nextInt(2)==1 ? -offsetDegree:offsetDegree;
            canvas.save();
            //旋转画布
            canvas.rotate(offsetDegree, mWidth / 2, mHeight / 2);
            // 给画笔设置随机颜色
            mTextpaint.setARGB(255, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20);
            canvas.drawText(String.valueOf(mText.charAt(i - 1)), (i - 1) * charLength * 1.6f + 30, mHeight * 2 / 3f, mTextpaint);
            //画完一定要把画布转回原来的位置，不然会影响后面的绘制
            canvas.restore();
        }
        //绘制干扰线
        for (Path path : mPaths)
        {
            mPathPaint.setARGB(255, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20);
            canvas.drawPath(path, mPathPaint);
        }
        // 绘制干扰点
        for (PointF pointF : mPoints)
        {
            mPointPaint.setARGB(255, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20, mrandom.nextInt(200) + 20);
            canvas.drawPoint(pointF.x, pointF.y, mPointPaint);
        }
    }

    //初始化画笔
    private void intiPaint(){
        mText = getCharAndNum(mTextCount);
        //初始化验证码画笔
        mTextpaint = new Paint();
        mTextpaint.setStrokeWidth(3);//画笔样式为空心时，设置空心画笔的宽度
        mTextpaint.setTextSize(mTextSize);

        //初始化干扰点画笔
        mPointPaint = new Paint();
        mPointPaint.setStrokeWidth(6);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形

        //初始化干扰线画笔
        mPathPaint = new Paint();
        mPathPaint.setStrokeWidth(5);
        mPathPaint.setColor(Color.GRAY);
        mPathPaint.setStyle(Paint.Style.STROKE); // 设置画笔为空心
        mPathPaint.setStrokeCap(Paint.Cap.ROUND); // 设置断点处为圆形
        // 取得验证码字符串显示的宽度值
        mTextWidth = mTextpaint.measureText(mText);
    }

    //初始化数据
    private void initData(){
        mPoints.clear();
        // 生成干扰点坐标
        for (int i = 0; i < 150; i++) {
            PointF pointF = new PointF(mrandom.nextInt(mWidth) + 10, mrandom.nextInt(mHeight) + 10);
            mPoints.add(pointF);
        }
        mPaths.clear();
        for(int i = 0 ;i<2; i++){
            Path path = new Path();
            int startX = mrandom.nextInt(mWidth / 3) + 10;
            int startY = mrandom.nextInt(mHeight / 3) + 10;
            int endX = mrandom.nextInt(mWidth / 2) + mWidth / 2 - 10;
            int endY = mrandom.nextInt(mHeight / 2) + mHeight / 2 - 10;
            path.moveTo(startX, startY);
            path.quadTo(Math.abs(endX - startX) / 2, Math.abs(endY - startY) / 2, endX, endY);
            mPaths.add(path);
        }

    }

    /**
     * 随机生成字符串和数值
     *
     * @param length length
     * @return String
     */
    public static String getCharAndNum(int length){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
