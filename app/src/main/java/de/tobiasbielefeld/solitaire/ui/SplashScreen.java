package de.tobiasbielefeld.solitaire.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;

import de.tobiasbielefeld.solitaire.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity implements NativeAdLoader.Listener {

    private static final int UI_ANIMATION_DELAY = 3000;
    private final Handler mHideHandler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        NativeAdLoader.getInstance().setContext(getApplicationContext());
        NativeAdLoader.getInstance().loadAd(getString(R.string.menu_button_native_placement_id), this);

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, FullScreenNative.class);
                startActivity(intent);
                finish();
            }
        };

        mHideHandler.postDelayed(runnable, UI_ANIMATION_DELAY);
    }

    public void onAdLoaded(Ad ad) {
        mHideHandler.removeCallbacks(runnable);
        Intent intent = new Intent(SplashScreen.this, FullScreenNative.class);
        startActivity(intent);
        finish();
    }

    public void onAdClicked(Ad ad) {}

    public void onError(Ad ad, AdError error) {}
}
