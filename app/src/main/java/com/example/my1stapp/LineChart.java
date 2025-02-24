package com.example.my1stapp; // 替换为你的实际包名

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChart extends View {

    private List<Float> dataPoints = new ArrayList<>();
    private Paint paint = new Paint();

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 设置画笔
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints.size() < 2) {
            return;
        }

        float width = getWidth();
        float height = getHeight();

        // 计算位置
        float segmentWidth = width / (dataPoints.size() - 1);

        // 开始绘制
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < dataPoints.size() - 1; i++) {
            float x1 = i * segmentWidth;
            float y1 = height - (dataPoints.get(i) / 100 * height); // 假设数据点的最大值是100
            float x2 = (i + 1) * segmentWidth;
            float y2 = height - (dataPoints.get(i + 1) / 100 * height);

            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

    // 添加数据点
    public void addDataPoint(float value) {
        dataPoints.add(value);
        invalidate(); // 重新绘制
    }
}
