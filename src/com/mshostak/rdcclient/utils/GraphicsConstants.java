// <editor-fold defaultstate="collapsed" desc="Copyright (c)">
/**
 * Copyright (c) 2008, Maksym Shostak.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the TimingFramework project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
// </editor-fold>
package com.mshostak.rdcclient.utils;

import com.jgoodies.forms.factories.Borders;
import java.awt.Color;
import javax.swing.border.Border;


/** Holds graphics constants. */
public interface GraphicsConstants{

  boolean IN_DEBUG = false;
  boolean CONTENTPANE_GRADIENT_NEEDED = true;
  
  enum ApplicationScreens{
    Connect,
    Logon
  }
  
  int SCREEN_TRANSACTION_DURATION = 600;
  float SCREEN_TRANSACTION_ACCELERATION = 0.2F;
  float SCREEN_TRANSACTION_DECELERATION = 0.4F;
  
  int COMMAND_BUTTON_WIDTH = 50; // in dlu
  //int COMMAND_BUTTON_HEIGHT = 20; // in dlu
  int PREFFERED_FRAME_WIDTH = 400; // in dlu   5 /
  int PREFFERED_FRAME_HEIGHT = 300; // in dlu  4
  
  int LOGO_IMAGE_HEIGHT = PREFFERED_FRAME_HEIGHT / 15;// in dlu
  Border CONTENT_PANE_BORDER = Borders.DLU14_BORDER;
  int BORDERS_THIKNESS = 3; // pixels, TODO: use dlu
  String NIMBUS_COMPONENT_SIZE_VARIANT = "large";
  
  float H1_FONT_INCREMENT = 16.0F;
  final float H2_FONT_INCREMENT = 2.0F;

  float CONTENTPANE_GRADIENT_FRACTIONS[] = new float[]{ 0.0F, 0.17F, 0.84F, 1.0F };
  Color CONTENTPANE_GRADIENT_COLORS[] = new Color[]{ Color.LIGHT_GRAY, 
    Color.WHITE, Color.WHITE, Color.LIGHT_GRAY };
  
  Color BORDER_CENTER_COLOR = new Color( 255, 255, 204, 255 );
  Color BORDER_EDGE_COLOR = new Color( 255, 255, 204, 5 );
  float[] BORDER_GRADIENT_FRACTIONS_1 = new float[]{ 0.01F, 0.4F, 0.99F };
  float[] BORDER_GRADIENT_FRACTIONS_2 = new float[]{ 0.01F, 0.6F, 0.99F };
  Color[] BORDER_GRADIENT_COLORS = new Color[]{ BORDER_EDGE_COLOR,
    BORDER_CENTER_COLOR, BORDER_EDGE_COLOR };


  Color RDC_CLIENT_LABEL_COLOR = Color.DARK_GRAY;
    
  //String LOGO_IMAGE_1_PATH = "/com/mshostak/rdcclient/resources/logo1.png";
  String LOGOFF_IMAGEICON_PATH = "/com/mshostak/rdcclient/resources/Back16.gif";
  String LOGO_IMAGE_2_PATH = "/com/mshostak/rdcclient/resources/logo2.png";
  String PC_IMAGE_PATH = "/com/mshostak/rdcclient/resources/pc.jpg";
    
}