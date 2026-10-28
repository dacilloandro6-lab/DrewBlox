package com.drewblox.classic;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
    private FrameLayout root;
    private WebView webView;
    private View splash;
    private ProgressBar progress;
    private TextView status;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean worldReady = false;
    private boolean pageFailed = false;

    private static final String ASSET_ORIGIN = "https://appassets.androidplatform.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setNavigationBarColor(Color.rgb(24, 54, 83));
        getWindow().setStatusBarColor(Color.rgb(36, 79, 125));
        buildViews();
        setupWebView();
        // Safety timeout: if the CDN is slow, show the game/error status instead of an endless splash.
        handler.postDelayed(() -> {
            if (!worldReady && !pageFailed) {
                status.setText("Still loading the world… check your internet connection");
                progress.setIndeterminate(true);
            }
        }, 10000);
        webView.loadUrl(ASSET_ORIGIN + "/assets/world/index.html");
    }

    private void buildViews() {
        root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(36, 79, 125));

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(142, 201, 238));
        root.addView(webView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        LinearLayout splashLayout = new LinearLayout(this);
        splashLayout.setOrientation(LinearLayout.VERTICAL);
        splashLayout.setGravity(Gravity.CENTER);
        splashLayout.setPadding(dp(28), dp(22), dp(28), dp(22));
        splashLayout.setBackground(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(104, 171, 218), Color.rgb(36, 79, 125), Color.rgb(24, 54, 83)}));

        TextView logo = new TextView(this);
        logo.setText("DREW");
        logo.setTextColor(Color.WHITE);
        logo.setTextSize(38);
        logo.setTypeface(Typeface.create("sans-serif-black", Typeface.BOLD));
        logo.setGravity(Gravity.CENTER);
        logo.setShadowLayer(3f, 2f, 3f, 0x88000000);

        TextView logoSecond = new TextView(this);
        logoSecond.setText("BLOX");
        logoSecond.setTextColor(Color.rgb(255, 218, 94));
        logoSecond.setTextSize(38);
        logoSecond.setTypeface(Typeface.create("sans-serif-black", Typeface.BOLD));
        logoSecond.setGravity(Gravity.CENTER);
        logoSecond.setShadowLayer(3f, 2f, 3f, 0x88000000);

        LinearLayout logoRow = new LinearLayout(this);
        logoRow.setGravity(Gravity.CENTER);
        logoRow.setOrientation(LinearLayout.HORIZONTAL);
        logoRow.addView(logo, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        logoRow.addView(logoSecond, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Pixel/block motif evokes classic building games without copying another game's logo.
        LinearLayout blocks = new LinearLayout(this);
        blocks.setGravity(Gravity.CENTER);
        blocks.setOrientation(LinearLayout.HORIZONTAL);
        blocks.setPadding(0, dp(16), 0, dp(12));
        int[] colors = {Color.rgb(255, 211, 74), Color.rgb(52, 124, 187), Color.rgb(84, 167, 88)};
        for (int c : colors) {
            View block = new View(this);
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(c);
            shape.setStroke(dp(2), 0xFFEAF3FB);
            block.setBackground(shape);
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(30), dp(30));
            bp.setMargins(dp(4), 0, dp(4), 0);
            blocks.addView(block, bp);
        }

        TextView subtitle = new TextView(this);
        subtitle.setText("A CLASSIC BLOCKY WORLD");
        subtitle.setTextColor(0xFFEAF3FB);
        subtitle.setTextSize(12);
        subtitle.setLetterSpacing(0.14f);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, dp(22));

        status = new TextView(this);
        status.setText("Starting DrewBlox…");
        status.setTextColor(Color.WHITE);
        status.setTextSize(14);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 0, 0, dp(12));

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setIndeterminate(true);
        progress.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.rgb(255, 218, 94)));
        progress.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0x557FFFFFFF));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(dp(220), dp(7));
        pp.setMargins(0, 0, 0, dp(18));

        TextView footer = new TextView(this);
        footer.setText("Loading the baseplate and blocky character");
        footer.setTextColor(0xFFD8E7F4);
        footer.setTextSize(11);
        footer.setGravity(Gravity.CENTER);

        splashLayout.addView(logoRow);
        splashLayout.addView(blocks);
        splashLayout.addView(subtitle);
        splashLayout.addView(status);
        splashLayout.addView(progress, pp);
        splashLayout.addView(footer);

        splash = splashLayout;
        root.addView(splash, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(root);
    }

    private void setupWebView() {
        WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.addJavascriptInterface(new AndroidBridge(), "DrewBloxAndroid");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public android.webkit.WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Keep the app within its local bundled game. External navigations are not allowed.
                return !ASSET_ORIGIN.equals(request.getUrl().getScheme() + "://" + request.getUrl().getHost());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pageFailed) return;
                status.setText("Loading 3D world…");
                progress.setIndeterminate(true);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                if (request.isForMainFrame()) {
                    pageFailed = true;
                    status.setText("Could not open the world. Check your connection and restart DrewBlox.");
                    progress.setIndeterminate(false);
                    progress.setProgress(0);
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void worldReady() {
            handler.post(() -> {
                worldReady = true;
                if (splash != null) {
                    splash.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                        root.removeView(splash);
                        splash = null;
                    }).start();
                }
            });
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (webView != null) {
            webView.removeJavascriptInterface("DrewBloxAndroid");
            webView.destroy();
        }
        super.onDestroy();
    }
}
