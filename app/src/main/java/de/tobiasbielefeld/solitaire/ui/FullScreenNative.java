package de.tobiasbielefeld.solitaire.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import de.tobiasbielefeld.solitaire.R;


public class FullScreenNative extends AppCompatActivity {

    private static final int NATIVE_AD_DELAY = 5000;
    private static final String TAG = "FullScreenNative";
    private LinearLayout adView;
    private CountDownTimer countDownTimer;

    private ImageView nativeAdIcon;
    private TextView nativeAdTitle;
    private ImageView nativeAdCover;
    private TextView nativeAdBody;
    private Button nativeAdCallToAction;
    private LinearLayout adChoicesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NativeAd nativeAd = NativeAdLoader.getInstance().getNativeAd();
        if (nativeAd == null) {
            Log.e(TAG, "No ads loaded!");
            Intent intent = new Intent(FullScreenNative.this, Main.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_native_ad);

        // Add the Ad view into the ad container.
        adView = (LinearLayout) findViewById(R.id.native_ad_unit);

        // Create native UI using the ad metadata.
        nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
        nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        nativeAdCover = (ImageView) adView.findViewById(R.id.native_ad_media);
        nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        //nativeAdMedia.setNativeAd(nativeAd);
        NativeAd.Image adCover = nativeAd.getAdCoverImage();
        NativeAd.downloadAndDisplayImage(adCover, nativeAdCover);

        // Add the AdChoices icon
        adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(FullScreenNative.this, nativeAd, true);
        adChoicesContainer.addView(adChoicesView);

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(adView, clickableViews);

        final Button skipButton = (Button) findViewById(R.id.skip_ad);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                FullScreenNative.this.skipThisAdWithAnimation();
            }
        });

        countDownTimer = new CountDownTimer(NATIVE_AD_DELAY, 1000) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                skipButton.setText("Skip | " + seconds);
            }
            @Override
            public void onFinish() {
                skipButton.setText("Skip | 0");
                FullScreenNative.this.skipThisAdWithAnimation();
            }
        };
        countDownTimer.start();
    }

    private void skipThisAdWithAnimation() {
        Intent intent = new Intent(FullScreenNative.this, Main.class);

        Pair<View, String> p1 = Pair.create((View)nativeAdCover, getString(R.string.shared_native_media_view));
        Pair<View, String> p2 = Pair.create((View)nativeAdTitle, getString(R.string.shared_native_title));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(FullScreenNative.this, p1, p2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

        finish();
    }
}
