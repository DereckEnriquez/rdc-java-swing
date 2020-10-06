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

import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.BorderFactory;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


/** Factory that supplies borders for component panels.<p> 
Class is utility and not intended for subclassing.
It contains a lot of repeated code so needs refactoring. */
public final class PanelBordersFactory{

  private PanelBordersFactory(){
  }
  
  
  public static Border getNorthPanelBorder(){
    
    return new MatteBorder( 0, 0, BORDERS_THIKNESS, 0, BORDER_CENTER_COLOR ){

      @Override
      public void paintBorder( final Component c, final Graphics g, final int x,
        final int y, final int width, final int height ){

        final Graphics2D g2d = (Graphics2D)g;
        final Paint oldPaint = g2d.getPaint();
        final boolean SIZE_IS_CHANGED = ( width != m_PreviousWidth );
        final boolean PAINT_IS_EMPTY = ( null == m_BorderPaint );
        if( SIZE_IS_CHANGED || PAINT_IS_EMPTY ){
          m_BorderPaint = new LinearGradientPaint( 0F, 0F, width, 0,
            BORDER_GRADIENT_FRACTIONS_2, BORDER_GRADIENT_COLORS,
            CycleMethod.REPEAT  );
          m_PreviousWidth = width;
        }
        else{
          // do nothing
        }

        g2d.setPaint( m_BorderPaint );
        g.fillRect( getBorderInsets().left, height - getBorderInsets().bottom, 
          width - getBorderInsets().right - getBorderInsets().left, 
          getBorderInsets().bottom );

        g2d.setPaint( oldPaint );

        //System.out.println( "North insets = " + getBorderInsets() );
        
      }

      private Paint m_BorderPaint = null;
      private int m_PreviousWidth = -1;
      
    };
    
  }
  
  public static Border getSouthPanelBorder(){
    
    return new MatteBorder( BORDERS_THIKNESS, 0, 0, 0, BORDER_CENTER_COLOR ){

      @Override
      public void paintBorder( final Component c, final Graphics g, final int x, 
        final int y, final int width, final int height ){
        
        final Graphics2D g2d = ( Graphics2D )g;
        final Paint oldPaint = g2d.getPaint();
        final boolean SIZE_IS_CHANGED = ( width != m_PreviousWidth );        
        final boolean PAINT_IS_EMPTY = ( null == m_BorderPaint );
        if( SIZE_IS_CHANGED || PAINT_IS_EMPTY ){
          m_BorderPaint = new LinearGradientPaint( 0F, 0F, width, 0, 
            BORDER_GRADIENT_FRACTIONS_1, BORDER_GRADIENT_COLORS, 
            CycleMethod.REPEAT  );
          m_PreviousWidth = width;
        }
        else{
          // do nothing
        }
        
        g2d.setPaint( m_BorderPaint );
        g.fillRect( getBorderInsets().left, 0, 
          width - getBorderInsets().right - getBorderInsets().left, 
          getBorderInsets().top );
        
        g2d.setPaint( oldPaint );
        
        //System.out.println( "South insets = " + getBorderInsets() );
        
      }
      
      
      private Paint m_BorderPaint = null;
      private int m_PreviousWidth = -1;
      
    };
    
  }
  
  public static Border getLeftPanelBorder(){

    return new MatteBorder( 0, 0, 0, BORDERS_THIKNESS, BORDER_CENTER_COLOR ){

      @Override
      public void paintBorder( final Component c, final Graphics g, final int x,
        final int y, final int width, final int height ){

        final Graphics2D g2d = (Graphics2D)g;
        final Paint oldPaint = g2d.getPaint();
        final boolean SIZE_IS_CHANGED = ( height != m_PreviousHeight );
        final boolean PAINT_IS_EMPTY = ( null == m_BorderPaint );
        if( SIZE_IS_CHANGED || PAINT_IS_EMPTY ){
          m_BorderPaint = new LinearGradientPaint( 0F, 0F, 0F, height,
            BORDER_GRADIENT_FRACTIONS_1, BORDER_GRADIENT_COLORS,
            CycleMethod.REPEAT );
          m_PreviousHeight = height;
        }
        else{
          // do nothing
        }

        g2d.setPaint( m_BorderPaint );
        g.fillRect( width - getBorderInsets().right, 0,
          getBorderInsets().right,
          height );

        g2d.setPaint( oldPaint );

      //System.out.println( "South insets = " + getBorderInsets() );

      }

      private Paint m_BorderPaint = null;
      private int m_PreviousHeight = -1;
      
    };

  }

}
  
  
