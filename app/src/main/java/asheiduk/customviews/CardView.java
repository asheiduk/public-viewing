package asheiduk.customviews;

import android.content.Context;
import android.content.res.TypedArray;
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
	private Paint boxTextPaint = new Paint();
	private Paint valuePaint = new Paint();
	private Paint namePaint = new Paint();
	
	private RectF frame = new RectF();
	private RectF rover = new RectF();
	
	// Properties
	private String cardName;
	private int cardValue;
	private int cardCountMax;
	
	// Berechnete Werte
	private String[] boxValues;
	private String cardValueString;
	
	// ========== Konstruktoren & Initialisierung ==========
	
	public CardView(Context context) {
		super(context);
		init();
	}
	
	public CardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		obtainStyledAttributes(context, attrs, defStyleAttr, 0);
		init();
	}

	private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.CardView, defStyleAttr, defStyleRes);
		try {
			cardValue = a.getInt(R.styleable.CardView_cardValue, cardValue);
			cardCountMax = a.getInt(R.styleable.CardView_cardCountMax, cardCountMax);
			cardName = a.getString(R.styleable.CardView_cardName);
		} finally {
			a.recycle();
		}
	}
	
	private void init(){
		updateCardValue();
		updateBoxValues();
		
		// schwarzer Rand, keine Füllung
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3.0f);
		
		// Wert und Name
		valuePaint.setTextAlign(Align.CENTER);
		namePaint.setTextAlign(Align.CENTER);
		
		// Text in Kästchen
		boxTextPaint.setStyle(Style.FILL);
		boxTextPaint.setColor(Color.BLACK);
		boxTextPaint.setTextAlign(Align.CENTER);
	}
	
	// ========== onSizeChanged ==========
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		// äußeres Rechteck
		frame.set(
				getPaddingLeft(),
				getPaddingTop(),
				w - getPaddingRight(),
				h - getPaddingBottom());
		
		// Wert und Name
		valuePaint.setTextSize(0.25f * frame.height());
		namePaint.setTextSize(0.15f * frame.height());
		
		// Text in Kästchen
		boxTextPaint.setTextSize(0.1f * frame.height());
		
		// Kästchen
		float boxWidth  = 0.25f * frame.width();
		float boxHeight = 0.17f * frame.height();
		rover.set(
				frame.left,
				frame.top,
				frame.left + boxWidth,
				frame.top  + boxHeight);
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
		
		// Erste Kästchen-Reihe
		rover.offsetTo(frame.left, frame.top);
		int countRow1 = Math.min(4, cardCountMax);
		for(int i=0; i<countRow1; ++i){
			canvas.drawRect(rover, paint);
			String s = boxValues[i];
			canvas.drawText(s,
					rover.centerX(),
					rover.bottom - 0.2f * rover.height(),
					boxTextPaint);
			rover.offset(rover.width(), 0);
		}
	}
	
	// ========== Abgeleitete Werte ==========
	
	protected void updateCardValue(){
		cardValueString = Integer.toString(cardValue);
	}
	
	protected void updateBoxValues(){
		boxValues = new String[cardCountMax];
		for(int i=0; i<cardCountMax; ++i){
			int boxValue = (i+1)*(i+1)*cardValue;
			boxValues[i] = Integer.toString(boxValue);
		}
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
		updateBoxValues();
		invalidate();
	}
	
	public int getCardCountMax() {
		return cardCountMax;
	}
	
	public void setCardCountMax(int cardCount) {
		this.cardCountMax = cardCount;
		updateBoxValues();
		invalidate();
	}
}
