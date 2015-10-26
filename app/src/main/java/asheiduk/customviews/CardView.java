package asheiduk.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CardView extends View {
	
	// ========== Konstruktoren ==========
	
	public CardView(Context context) {
		super(context);
	}
	
	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	// ========== onDraw ==========
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// schwarzer Rand, keine Füllung
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3.0f);
		
		// Rechteck, 10px nach innen gesetzt
		RectF frame = new RectF();
		frame.set(0.0f, 0.0f, 
				canvas.getWidth(), canvas.getHeight());
		frame.inset(10.0f, 10.0f);
		
		// und los
		canvas.drawRect(frame, paint);
		canvas.drawLine(
				frame.left, frame.top,
				frame.right, frame.bottom,
				paint);
		canvas.drawLine(
				frame.right, frame.top,
				frame.left, frame.bottom,
				paint);
	}
}
