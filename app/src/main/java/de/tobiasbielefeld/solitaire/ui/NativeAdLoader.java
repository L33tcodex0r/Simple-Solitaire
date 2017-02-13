package de.tobiasbielefeld.solitaire.ui;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;


public class NativeAdLoader {

    public interface Listener {
        void onError(Ad ad, AdError adError);
        void onAdLoaded(Ad ad);
        void onAdClicked(Ad ad);
    }

    private NativeAd mNativeAd;
    private Listener mListener;
    private boolean isValid;
    private Context mContext;
    private static final String TAG = "NativeAdLoader";

    private static final NativeAdLoader instance = new NativeAdLoader();

    public static NativeAdLoader getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public void loadAd(String placementID, Listener listener) {
        if (mContext == null) {
            Log.e(TAG, "No context set for NativeAdLoader!");
            return;
        }

        isValid = false;
        mNativeAd = new NativeAd(mContext, placementID);
        mListener = listener;

        mNativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Error loading ad!");
                Log.e(TAG, adError.getErrorMessage());

                if (mListener != null) {
                    mListener.onError(ad, adError);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                isValid = true;
                Log.d(TAG, "Ad loaded!");

                if (mListener != null) {
                    mListener.onAdLoaded(ad);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (mListener != null) {
                    mListener.onAdClicked(ad);
                }
            }
        });

        mNativeAd.loadAd();
    }

    public NativeAd getNativeAd() {
        if (!isValid) {
            return null;
        }

        return mNativeAd;
    }
}
