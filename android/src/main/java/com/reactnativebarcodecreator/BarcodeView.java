package com.reactnativebarcodecreator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.ExceptionsManagerModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class BarcodeView extends androidx.appcompat.widget.AppCompatImageView {
  int width = 100;
  int height = 100;
  String content = "";
  int foregroundColor = 0x000000;
  int background = 0xffffff;
  BarcodeFormat format = BarcodeFormat.QR_CODE;
  ReactContext context;
  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }
  public void setWidth(int width) {
    this.width = dpToPx(width);
    updateQRCodeView();
  }

  public void setHeight(int height) {
    this.height = dpToPx(height);
    updateQRCodeView();
  }

  public void setFormat(BarcodeFormat format) {
    this.format = format;
    updateQRCodeView();
  }

  public void setContent(String content) {
    this.content = content;
    updateQRCodeView();
  }

  public void setForegroundColor(String color) {
    try {
      int c = Color.parseColor(color);
      this.foregroundColor = c;
      updateQRCodeView();
    }catch (Exception e) {
      showException(e);
      e.printStackTrace();
    }
  }

  public void setBackgroundColor(String background) {
    try {
      this.background = Color.parseColor(background);
      updateQRCodeView();
    }catch (Exception e) {
      showException(e);
      e.printStackTrace();
    }
  }

  public void updateQRCodeView() {
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    try {
      BitMatrix bitMatrix = multiFormatWriter.encode(content, format, width, height);
      BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
      Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix, foregroundColor, background);
      setImageBitmap(bitmap);
    } catch (Exception e) {
      showException(e);
      e.printStackTrace();
    }
  }

  private void showException(Exception e) {
    ExceptionsManagerModule manager = context.getNativeModule(ExceptionsManagerModule.class);
    WritableNativeMap map = new WritableNativeMap();
    map.putString("message", e.getMessage());
    manager.reportException(map);
  }

  public BarcodeView(ReactContext context) {
    super(context);
    this.context = context;
  }
}