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
	
	private Paint paint = new Paint();
	private RectF frame = new RectF();
	
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
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3.0f);
		
		// äußeres Rechteck
		frame.set(
			getPaddingLeft(),
			getPaddingTop(),
			getWidth() - getPaddingRight(),
			getHeight() - getPaddingBottom());

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
