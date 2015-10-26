package asheiduk.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CardView extends View {
	
	private Paint paint = new Paint();
	private Paint valuePaint = new Paint();
	private Paint namePaint = new Paint();
	
	private RectF frame = new RectF();
	
	// Properties
	private String cardName;
	private int cardValue;
	
	// Berechnete Werte
	private String cardValueString;
	
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
	
	// ========== onSizeChanged ==========
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		// schwarzer Rand, keine Füllung
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3.0f);
		
		// Wert und Name
		valuePaint.setTextAlign(Align.CENTER);
		valuePaint.setTextSize(0.25f * h);
		namePaint.setTextAlign(Align.CENTER);
		namePaint.setTextSize(0.15f * h);
		
		// äußeres Rechteck
		frame.set(
				getPaddingLeft(),
				getPaddingTop(),
				w - getPaddingRight(),
				h - getPaddingBottom());
	}
	
	// ========== onDraw ==========
	
	@Override
	protected void onDraw(Canvas canvas) {
		float centerX = frame.centerX();
		float h = frame.height();
		float top = frame.top;
		
		// Rahmen
		canvas.drawRect(frame, paint);

		// Wert
		if( cardValueString != null ){
			canvas.drawText(cardValueString,
					centerX, 0.45f * h + top,
					valuePaint);
		}
		
		// Name
		if( cardName != null ){
			canvas.drawText(cardName,
					centerX, 0.65f * h + top,
					namePaint);
		}
	}
	
	// ========== Abgeleitete Werte ==========
	
	protected void updateCardValue(){
		cardValueString = Integer.toString(cardValue);
	}
	
	// ========== Properties ==========
	
	public String getCardName() {
		return cardName;
	}
	
	public void setCardName(String cardName) {
		this.cardName = cardName;
		invalidate();
	}
	
	public int getCardValue() {
		return cardValue;
	}
	
	public void setCardValue(int cardValue) {
		this.cardValue = cardValue;
		updateCardValue();
		invalidate();
	}
}
