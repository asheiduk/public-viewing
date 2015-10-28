package asheiduk.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardView = (CardView) findViewById(R.id.card_view);
        cardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CardView cardView = (CardView) v;
        int count = cardView.getSelectedCardCount();
        if (count > CardView.COUNT_INVALID) {
            cardView.setCardCount(count);
        }
    }
}
