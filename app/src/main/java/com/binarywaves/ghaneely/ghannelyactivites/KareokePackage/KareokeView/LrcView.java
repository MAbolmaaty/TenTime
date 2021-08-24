package com.binarywaves.ghaneely.ghannelyactivites.KareokePackage.KareokeView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import com.binarywaves.ghaneely.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Amany on 22-Aug-17.
 */

public class LrcView extends android.support.v7.widget.AppCompatTextView {

    private static TreeMap<Long, String> dictionnary;
    private static List<String> texts;
    private static int currentLine;
    private static long mCurrentTime = 0L;
    private static long mNextTime = 0L;
    private static int mViewWidth;
    private static float mTextSize;
    private static float mDividerHeight;

    private static Paint mNormalPaint;
    private static Paint mCurrentPaint;
    private static List<Long> mTimes;
    private int normalTextColor;
    private int currentTextColor;
    private Typeface customFont;
    private int mLrcHeight;
    private int mRows;
    private String uploader;
    private String lyrics;

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initViews(attrs);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(attrs);
    }

    private static int drawDividedText(String lrc, Canvas canvas, float y, int breakOffset, Paint paint) {
        int overflow = lrc.length() - mNormalPaint.breakText(lrc, true, mViewWidth * 0.965f, null);
        int contained = lrc.length() - overflow;
        String cutLrc = lrc.substring(0, contained);
        cutLrc = cutLrc.substring(0, overflow > 0 && cutLrc.contains(" ") ? cutLrc.lastIndexOf(" ") : contained);
        float x = (mViewWidth - mNormalPaint.measureText(cutLrc)) / 2;
        canvas.drawText(cutLrc, x, y + breakOffset, paint);

        if (overflow > 0) {
            float lineHeight = mTextSize + mDividerHeight; // todo move to field
            lrc = lrc.substring(cutLrc.length() + 1);
            breakOffset = drawDividedText(lrc, canvas, y + lineHeight, breakOffset, paint) + (int) lineHeight;
        }
        return breakOffset;
    }

    private static Long parseTime(String time) {
        String[] min = time.split(":");
        String[] sec;
        if (!min[1].contains("."))
            min[1] += ".00";
        sec = min[1].split("\\.");
        sec[1] = sec[1].replaceAll("\\D+", "").replaceAll("\r", "").replaceAll("\n", "").trim();
        if (sec[1].length() > 3)
            sec[1] = sec[1].substring(0, 3);

        long minInt = Long.parseLong(min[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long secInt = Long.parseLong(sec[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long milInt = Long.parseLong(sec[1]);

        return minInt * 60 * 1000 + secInt * 1000 + milInt * Double.valueOf(Math.pow(10, 3 - sec[1].length())).longValue();
    }

    private static String[] parseLine(String line) {
        // Matcher matcher = Pattern.compile("\\[.+\\].+").matcher(line);

        if (line.endsWith("["))
            line += " ";
        line = line.replaceAll("\\[", "");
        String[] result = line.split("]");
        try {
            for (int i = 0; i < result.length - 1; ++i)
                result[i] = String.valueOf(parseTime(result[i]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            return null;
        }

        return result;
    }

    public static void setSourceLrc(String lrc) {
        mNextTime = 0;
        mCurrentTime = 0;

        texts = new ArrayList<>();
        mTimes = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new StringReader(lrc));

        String line;
        String[] arr;
        try {
            while (null != (line = reader.readLine())) {
                arr = parseLine(line);
                if (null == arr) {
                    continue;
                }

                if (1 == arr.length) {
                    String last = texts.remove(texts.size() - 1);
                    texts.add(last + arr[0]);
                    continue;
                }
                for (int i = 0; i < arr.length - 1; i++) {
                    mTimes.add(Long.parseLong(arr[i]));
                    texts.add(arr[arr.length - 1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Collections.sort(mTimes);
        dictionnary = new TreeMap<>();
        for (int i = 0; i < mTimes.size(); i++) {

            if (!(dictionnary.isEmpty() && texts.get(i).replaceAll("\\s", "").isEmpty()))
                dictionnary.put(mTimes.get(i), texts.get(i));
        }
        Collections.sort(mTimes);
    }

    private void initViews(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.Lrc);
        mTextSize = ta.getDimension(R.styleable.Lrc_textSize, 35.0f);
        mRows = ta.getInteger(R.styleable.Lrc_rows, 5);
        mDividerHeight = ta.getDimension(R.styleable.Lrc_dividerHeight, 9.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            normalTextColor = ta.getColor(R.styleable.Lrc_normalTextColor,
                    getResources().getColor(R.color.white, getContext().getTheme()));
            currentTextColor = ta.getColor(R.styleable.Lrc_currentTextColor,
                    getResources().getColor(R.color.ghaneely_orange, getContext().getTheme()));

        } else {
            normalTextColor = ta.getColor(R.styleable.Lrc_normalTextColor,
                    getResources().getColor(R.color.white));
            currentTextColor = ta.getColor(R.styleable.Lrc_currentTextColor,
                    getResources().getColor(R.color.ghaneely_orange));
        }


        ta.recycle();

        mLrcHeight = (int) (mTextSize + mDividerHeight) * (mTimes.size() + 1) + 5;

        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mNormalPaint.setTextSize(mTextSize);
        //  Typeface light = LyricsTextFactory.FontCache.get("light", getContext());

        customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/GE_SS_Two_Medium.otf");

        mNormalPaint.setTypeface(customFont);
        mNormalPaint.setColor(normalTextColor);
        mCurrentPaint.setTextSize(mTextSize);
        mCurrentPaint.setTypeface(customFont);
        mCurrentPaint.setColor(currentTextColor);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = getMeasuredWidth();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.makeMeasureSpec(mLrcHeight, MeasureSpec.EXACTLY);
        int measuredWidth = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dictionnary.isEmpty())
            return;

        currentLine = Math.max(mTimes.indexOf(mCurrentTime), 0);

        canvas.save();

        int breakOffset = 0;

        if (currentLine - 1 >= 0) {
            long previousTime = mTimes.get(currentLine - 1);
            String previousLrc = dictionnary.get(previousTime);
            if (previousLrc != null) {
                breakOffset = drawDividedText(previousLrc, canvas, mTextSize + mDividerHeight, breakOffset, mNormalPaint);
            }
        }

        String currentLrc = dictionnary.get(mCurrentTime);
        if (currentLrc == null) {
            mCurrentTime = dictionnary.firstKey();
            currentLrc = dictionnary.get(mCurrentTime);
        }
        breakOffset = drawDividedText(currentLrc, canvas, (mTextSize + mDividerHeight) * 2, breakOffset, mCurrentPaint);

        for (int i = currentLine + 1; i < mTimes.size(); i++) {
            String lrc = dictionnary.get(mTimes.get(i));
            if (lrc == null)
                continue;
            breakOffset = drawDividedText(lrc, canvas,
                    (mTextSize + mDividerHeight) * (2 + i - currentLine), breakOffset, mNormalPaint);
        }

        mLrcHeight = (int) (mTextSize + mDividerHeight) * (mTimes.size() + 3) + breakOffset + 5;
        canvas.clipRect(0, 0, mViewWidth, mLrcHeight);

        canvas.restore();
        requestLayout();
    }

    public synchronized void changeCurrent(long time) {
        if (dictionnary == null || dictionnary.isEmpty())
            return;
        mNextTime = dictionnary.lastKey();
        if (time < mNextTime)
            mNextTime = dictionnary.higherKey(time);
        mCurrentTime = dictionnary.firstKey();
        if (time > mCurrentTime)
            mCurrentTime = dictionnary.floorKey(time);
        postInvalidate();
    }

    public boolean isFinished() {
        return mTimes.isEmpty() || mTimes.get(mTimes.size() - 1) <= mCurrentTime;
    }


}