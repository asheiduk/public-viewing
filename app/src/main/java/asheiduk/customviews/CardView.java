package asheiduk.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CardView extends View {
	public static final int COUNT_INVALID = -1;
	
	private Paint paint = new Paint();
	private Paint inversePaint = new Paint();
	private Paint boxTextPaint = new Paint();
	private Paint inverseBoxTextPaint = new Paint();
	private Paint valuePaint = new Paint();
	private Paint namePaint = new Paint();
	
	private RectF frame = new RectF();
	private RectF rover = new RectF();
	
	// Properties
	private String cardName;
	private int cardValue;
	private int cardCount;
	private int cardCountMax;
	
	// Berechnete Werte
	private String[] boxValues;
	private RectF[] boxRects;
	private String cardValueString;
	
	// UI-Interaktion State
	private int selectedCardCount = COUNT_INVALID;
	
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
			cardCount = a.getInt(R.styleable.CardView_cardCount, cardCount);
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
		
		// "Aktiv"-Markierung
		inversePaint.set(paint);
		inversePaint.setStyle(Style.FILL_AND_STROKE);
		inverseBoxTextPaint.set(boxTextPaint);
		inverseBoxTextPaint.setColor(Color.WHITE);

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
		inverseBoxTextPaint.setTextSize(0.1f * frame.height());
		
		// Kästchen
		updateBoxRects();
	}

	protected void updateBoxRects() {
		boxRects = new RectF[cardCountMax];
		
		float boxWidth  = 0.25f * frame.width();
		float boxHeight = 0.17f * frame.height();
		rover.set(0, 0, boxWidth, boxHeight);
		
		// Erste Reihe - Oben
		rover.offsetTo(frame.left, frame.top);
		updateBoxRow(0, 4, boxWidth);
		
		// Zweite Reihe - Unten
		rover.offsetTo(frame.left, frame.bottom - boxHeight);
		updateBoxRow(4, 8, boxWidth);
		
		// Dritte Reihe - von rechts nach links
		rover.offset(-boxWidth, -boxHeight);
		updateBoxRow(8,  12, -boxWidth);
	}
	
	private void updateBoxRow(int startBox, int endBox, float offsetX){
		int N = Math.min(endBox, boxRects.length);
		for(int i=startBox; i<N; ++i){
			boxRects[i] = new RectF(rover);
			rover.offset(offsetX, 0);
		}
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
		
		// Kästchen
		for(int i=0; i<cardCountMax; ++i){
			boolean isMarked = i+1 == cardCount;
			
			RectF box = boxRects[i];
			String value = boxValues[i];
			
			canvas.drawRect(box, isMarked ? inversePaint : paint);
			canvas.drawText(value,
					box.centerX(),
					box.bottom - 0.2f * box.height(),
					isMarked ? inverseBoxTextPaint : boxTextPaint);
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
	
	// ========== UI-Interaktionen ==========
	
	protected int getCardCountAt(float x, float y){
		for(int i=0; i<cardCountMax; ++i){
			if( boxRects[i].contains(x, y) )
				return i + 1;
		}
		return 0;
	}
	
	public int getSelectedCardCount(){
		return selectedCardCount;
	}
	
	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				selectedCardCount = COUNT_INVALID;
				break;
			case MotionEvent.ACTION_UP:
				selectedCardCount = getCardCountAt(event.getX(), event.getY());
				break;
		}
		return super.onTouchEvent(event);
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
	
	public int getCardCount() {
		return cardCount;
	}
	
	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
		invalidate();
	}
	
	public int getCardCountMax() {
		return cardCountMax;
	}
	
	public void setCardCountMax(int cardCount) {
		this.cardCountMax = cardCount;
		updateBoxValues();
		updateBoxRects();
		invalidate();
	}
}
