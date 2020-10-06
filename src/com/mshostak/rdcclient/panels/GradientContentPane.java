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
package com.mshostak.rdcclient.panels;

import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import com.jgoodies.forms.util.DefaultUnitConverter;
import com.jgoodies.forms.util.UnitConverter;
import com.mshostak.rdcclient.utils.ImageUtils;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;


/** This class is used as a content pane of the main view.<p>
Class is based #GradientPanel class, that is from StackLayout example writed by
Roman Guy's for "Filthy Rich Clients" book. 
@author Maksym Shostak. */
public final class GradientContentPane extends JPanel{

  public GradientContentPane(){
    this( new BorderLayout() );
  }

  public GradientContentPane( final LayoutManager in_LayoutManager ){
    super( in_LayoutManager );
    //addComponentListener( new GradientCacheManager() );
  }

  
  public static void loadLogoImages( final Class in_Class ){
    {
      //final URL imageUrl1 = in_Class.getResource( LOGO_IMAGE_1_PATH );
      //m_LogoImage1 = ImageUtils.loadImage( imageUrl1 );
    }
    {
      final URL imageUrl2 = in_Class.getResource( LOGO_IMAGE_2_PATH );
      m_LogoImage2 = ImageUtils.loadImage( imageUrl2 );
    }
  }
  
  
  @Override
  public Dimension getPreferredSize(){
    
    if( null == PREFFERED_SIZE ){
      final UnitConverter unitConverter = DefaultUnitConverter.getInstance();
      PREFFERED_SIZE = new Dimension(
        unitConverter.dialogUnitXAsPixel( PREFFERED_FRAME_WIDTH ,this  ),
        unitConverter.dialogUnitYAsPixel( PREFFERED_FRAME_HEIGHT ,this ) );
    }
    else{
      // do nothing
    }

    return PREFFERED_SIZE;

  }

  
  @Override
  protected void paintComponent( final Graphics in_Graphics ){

    //assert( null != m_LogoImage1 );
    assert( null != m_LogoImage2 );
    
    final int CURRENT_WIDTH = getWidth();
    final int CURRENT_HEIGHT = getHeight();
    final boolean SIZE_IS_CHANGED = ( ( CURRENT_WIDTH != m_PreviousWidth ) 
      || ( CURRENT_HEIGHT != m_PreviousHeight ) );
    final boolean IMAGE_IS_EMPTY = ( null == m_GradientImage );

    if( IMAGE_IS_EMPTY || SIZE_IS_CHANGED ){

      // Create the empty image
      m_GradientImage = GraphicsUtilities.createCompatibleImage( CURRENT_WIDTH, 
        CURRENT_HEIGHT ); //createImage( CURRENT_WIDTH, CURRENT_HEIGHT );
      final Graphics2D graphics2D = ( Graphics2D )m_GradientImage.getGraphics();
      
      if( CONTENTPANE_GRADIENT_NEEDED ){
        setRenderingHints( graphics2D );
        paintBackgroundGradient( CURRENT_WIDTH, CURRENT_HEIGHT,graphics2D );
      }
      else{
        graphics2D.setPaint( getBackground() );
        graphics2D.fillRect( 0, 0, CURRENT_WIDTH, CURRENT_HEIGHT );
      }
      
      paintLogoImages( CURRENT_WIDTH, CURRENT_HEIGHT, graphics2D );
      // restore clip if paint something else
      
      graphics2D.dispose();

      // Remember current size
      m_PreviousWidth = CURRENT_WIDTH;
      m_PreviousHeight = CURRENT_HEIGHT;

    }
    else{
      // do nothing
    }

    in_Graphics.drawImage( m_GradientImage, 0, 0, null );

  }

  
  private void paintBackgroundGradient( final int CURRENT_WIDTH,
    final int CURRENT_HEIGHT, final Graphics2D graphics2D ){

    // Paint entire component with the edge paint
    //graphics2D.setColor( CONTENTPANE_EDGE_COLOR );
    //graphics2D.fillRect( 0, 0, CURRENT_WIDTH, CURRENT_HEIGHT );

    /* final Double GRADIENT_CENTER = new Point2D.Double( CURRENT_WIDTH / 2.0,
      CURRENT_HEIGHT / 2.0 );
    final double GRADIENT_RADUIS = Math.sqrt( Math.pow( CURRENT_WIDTH, 2 ) 
      + Math.pow( CURRENT_HEIGHT, 2 ) ); // гіпотенуза
    
    final Paint radialGradient = new RadialGradientPaint( GRADIENT_CENTER,
      Math.round( GRADIENT_RADUIS ), CONTENTPANE_GRADIENT_FRACTIONS, 
      CONTENTPANE_GRADIENT_COLORS, CycleMethod.REFLECT    ); */
    
    final Paint linearGradient = new LinearGradientPaint( 0,
      0, 0, CURRENT_HEIGHT, CONTENTPANE_GRADIENT_FRACTIONS,
      CONTENTPANE_GRADIENT_COLORS, CycleMethod.REPEAT );
    
    graphics2D.setPaint( linearGradient );
    graphics2D.fillRect( 0, 0, CURRENT_WIDTH, CURRENT_HEIGHT );

  }

  private void paintLogoImages( final int PANEL_WIDTH, 
    final int PANEL_HEIGHT, final Graphics2D graphics2D ){
    
    final AlphaComposite composite = AlphaComposite.getInstance( 
      AlphaComposite.SRC_OVER, 0.8F );
    graphics2D.setComposite( composite );
    
    // Calculate new height
    final UnitConverter unitConverter = DefaultUnitConverter.getInstance();
    final int NEW_IMAGE_HEIGHT = unitConverter.dialogUnitYAsPixel( 
      LOGO_IMAGE_HEIGHT, this );
    
    {/* Draw first logo image
      // Scale image 
      final float SCALE_FACTOR = ( float )NEW_IMAGE_HEIGHT / 
        ( float )m_LogoImage1.getHeight( null );
      final int NEW_IMAGE_WIDTH = Math.round( 
        ( float )m_LogoImage1.getWidth( null ) * SCALE_FACTOR );

      m_LogoImage1 = ImageUtils.getFasterScaledInstance( 
        ( BufferedImage )m_LogoImage1, NEW_IMAGE_WIDTH, NEW_IMAGE_HEIGHT, 
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, true  );

      // Calculate image position
      final Insets borderInsets = getBorder().getBorderInsets( this );
      final int IMAGE_LEFT = borderInsets.left;
      final int IMAGE_TOP = borderInsets.top;

      // Draw image
      graphics2D.setClip( IMAGE_LEFT, IMAGE_TOP, NEW_IMAGE_WIDTH, 
        NEW_IMAGE_HEIGHT );
      graphics2D.drawImage( m_LogoImage1, IMAGE_LEFT, IMAGE_TOP, null );*/
    }
      
    {// Draw second logo image
      // Scale image 
      final float SCALE_FACTOR = (float)NEW_IMAGE_HEIGHT /
        (float)m_LogoImage2.getHeight( null );
      final int NEW_IMAGE_WIDTH = Math.round(
        (float)m_LogoImage2.getWidth( null ) * SCALE_FACTOR );

      m_LogoImage2 = ImageUtils.getFasterScaledInstance(
        (BufferedImage)m_LogoImage2, NEW_IMAGE_WIDTH, NEW_IMAGE_HEIGHT,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, true );

      // Calculate image position
      final Insets borderInsets = getBorder().getBorderInsets( this );
      final int IMAGE_LEFT = PANEL_WIDTH - NEW_IMAGE_WIDTH - borderInsets.right;
      final int IMAGE_TOP = borderInsets.top;

      // Draw image
      graphics2D.setClip( IMAGE_LEFT, IMAGE_TOP, NEW_IMAGE_WIDTH,
        NEW_IMAGE_HEIGHT );
      graphics2D.drawImage( m_LogoImage2, IMAGE_LEFT, IMAGE_TOP, null );
    }
    
  }

    
  private void setRenderingHints( final Graphics2D graphics2D ){
    // Already used in getFasterScaledInstance() call 
    // graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION,
      // RenderingHints.VALUE_INTERPOLATION_BILINEAR );
    //graphics2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    //graphics2D.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
    //graphics2D.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
  }

 
  private Image m_GradientImage;
  //private static Image m_LogoImage1;
  private static Image m_LogoImage2;
  
  private static final long serialVersionUID = 1L;
  private Dimension PREFFERED_SIZE = null;
  private int m_PreviousWidth = -1;// obsoete ?
  private int m_PreviousHeight = -1;// obsoete ?
  
  
  /** This listener generated NullPointer exceptions because of image = null and 
  animated transactions. */
  private final class GradientCacheManager implements ComponentListener{

    public void componentResized( ComponentEvent in_Event ){
    }

    public void componentMoved( ComponentEvent in_Event ){
    }

    public void componentShown( ComponentEvent in_Event ){
    }

    public void componentHidden( ComponentEvent in_Event ){
      disposeImageCache();
    }

    
    private void disposeImageCache(){

      synchronized( m_GradientImage ){
        m_GradientImage.flush();
        m_GradientImage = null;
      }

      /*synchronized( m_LogoImage1 ){
        m_LogoImage1.flush();
        m_LogoImage1 = null;
      }*/

    }

  }
  
}
