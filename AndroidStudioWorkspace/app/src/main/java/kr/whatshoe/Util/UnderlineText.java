package kr.whatshoe.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by jaewoo on 2015-10-16.
 */
public class UnderlineText extends EditText {
        private Paint mPaint = new Paint();

        public UnderlineText(Context context) {
            super(context);
            initPaint();
        }

        public UnderlineText(Context context, AttributeSet attrs) {
            super(context, attrs);
            initPaint();
        }

        public UnderlineText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initPaint();
        }

        private void initPaint() {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x80000000);
        }

        @Override protected void onDraw(Canvas canvas) {
            int left = getLeft();
            int right = getRight();
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int height = getHeight();
            int lineHeight = (height-paddingTop-paddingBottom)/3;
            int count = (height-paddingTop-paddingBottom) / lineHeight;

            for (int i = 0; i < 3; i++) {
                int baseline = lineHeight * (i+1) + paddingTop;
                canvas.drawLine(left+paddingLeft, baseline, right-paddingRight, baseline, mPaint);
            }

            super.onDraw(canvas);
        }
    }
