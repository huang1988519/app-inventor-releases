// Copyright 2008 Google Inc. All Rights Reserved.

package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.widgets.properties.ColorChoicePropertyEditor;

/**
 * Property editor for color RGB values.
 *
 */
public final class YoungAndroidColorChoicePropertyEditor extends ColorChoicePropertyEditor {

  // Colors as defined by the Android runtime.  "Default" is treated specially.
  private static final Color[] YA_COLORS = {
    new Color(MESSAGES.noneColor(), Color.ALPHA_TRANSPARENT, "FFFFFF"),
    new Color(MESSAGES.blackColor(), Color.ALPHA_OPAQUE, "000000"),
    new Color(MESSAGES.blueColor(), Color.ALPHA_OPAQUE, "0000FF"),
    new Color(MESSAGES.cyanColor(), Color.ALPHA_OPAQUE, "00FFFF"),
    new Color(MESSAGES.defaultColor(), Color.ALPHA_TRANSPARENT, "000000"),
    new Color(MESSAGES.darkGrayColor(), Color.ALPHA_OPAQUE, "444444"),
    new Color(MESSAGES.grayColor(), Color.ALPHA_OPAQUE, "888888"),
    new Color(MESSAGES.greenColor(), Color.ALPHA_OPAQUE, "00FF00"),
    new Color(MESSAGES.lightGrayColor(), Color.ALPHA_OPAQUE, "CCCCCC"),
    new Color(MESSAGES.magentaColor(), Color.ALPHA_OPAQUE, "FF00FF"),
    new Color(MESSAGES.orangeColor(), Color.ALPHA_OPAQUE, "FFC800"),
    new Color(MESSAGES.pinkColor(), Color.ALPHA_OPAQUE, "FFAFAF"),
    new Color(MESSAGES.redColor(), Color.ALPHA_OPAQUE, "FF0000"),
    new Color(MESSAGES.whiteColor(), Color.ALPHA_OPAQUE, "FFFFFF"),
    new Color(MESSAGES.yellowColor(), Color.ALPHA_OPAQUE, "FFFF00")
  };

  // The following colors can be generated by the Lego Mindstorms NXT color sensor.
  public static final Color[] NXT_GENERATED_COLORS = {
    new Color(MESSAGES.noneColor(), Color.ALPHA_TRANSPARENT, "FFFFFF"),
    new Color(MESSAGES.redColor(), Color.ALPHA_OPAQUE, "FF0000"),
    new Color(MESSAGES.greenColor(), Color.ALPHA_OPAQUE, "00FF00"),
    new Color(MESSAGES.blueColor(), Color.ALPHA_OPAQUE, "0000FF"),
  };

  /**
   * Creates a new property editor for color RGB values.
   */
  public YoungAndroidColorChoicePropertyEditor() {
    this(YA_COLORS);
  }

  /**
   * Creates a new property editor for a specific array of color RGB values.
   */
  public YoungAndroidColorChoicePropertyEditor(Color[] colors) {
    super(colors, "&H");
  }
}
