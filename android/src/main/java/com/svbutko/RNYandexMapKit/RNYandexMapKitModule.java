
package com.svbutko.RNYandexMapKit;

import android.support.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.yandex.runtime.Error;
import com.yandex.runtime.i18n.I18nManagerFactory;
import com.yandex.runtime.i18n.LocaleUpdateListener;

@ReactModule(name = RNYandexMapKitModule.REACT_CLASS)
public class RNYandexMapKitModule extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "RNYandexMapKit";
  private final ReactApplicationContext reactContext;

  private String apiKey;

  public RNYandexMapKitModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  private LocaleUpdateListener localeUpdateListener = new LocaleUpdateListener() {
    @Override
    public void onLocaleUpdated() {

    }

    @Override
    public void onLocaleUpdateError(@NonNull Error error) {

    }
  };

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactMethod
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  @ReactMethod
  public void setLocale(String locale) {
    I18nManagerFactory.setLocale(locale, localeUpdateListener);
  }

  public String getApiKey() {
    return apiKey;
  }
}