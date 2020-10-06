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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.JLabel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;



/** A label with the drop shadow. */
public final class ShadowedLabel extends JLabel{

  public ShadowedLabel(){

    setForeground( RDC_CLIENT_LABEL_COLOR );

    m_ShadowRenderer = new ShadowRenderer();
    SHADOW_OFFSET = m_ShadowRenderer.getSize();

    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    m_DesktopHints = (Map)toolkit.getDesktopProperty( "awt.font.desktophints" );

  }

  
  /** Draws the shadow centered; increases graphics clip; cached images.  */
  @Override
  protected void paintComponent( final Graphics in_Graphics ){

    final boolean IMAGE_IS_EMPTY = ( null == m_LabelImage );
    if( IMAGE_IS_EMPTY ){

      // Draw image label text
      m_LabelImage = new BufferedImage( getWidth(), getHeight(),
        BufferedImage.TYPE_INT_ARGB );
      m_LabelImage = GraphicsUtilities.createCompatibleImage( m_LabelImage );
      final Graphics2D graphics2d = m_LabelImage.createGraphics();

      graphics2d.setClip( in_Graphics.getClip() );
      graphics2d.setFont( getFont() );
      
      if( null != m_DesktopHints ){
        graphics2d.addRenderingHints( m_DesktopHints );
      }
      else{
        // do nothing
      }

      super.paintComponent( graphics2d );

      graphics2d.dispose();

      // Draw image label shadow
      m_ShadowImage = m_ShadowRenderer.createShadow( m_LabelImage );

    }
    else{
      // do nothing
    }


    // Increase clip to draw the shadow
    final Rectangle oldClip = in_Graphics.getClipBounds();
    final Rectangle newClip = new Rectangle( oldClip.x - SHADOW_OFFSET,
      oldClip.y - SHADOW_OFFSET, oldClip.width + 2 * SHADOW_OFFSET,
      oldClip.height + 2 * SHADOW_OFFSET );
    in_Graphics.setClip( newClip );

    // Draw shadow and label text images
    in_Graphics.drawImage( m_ShadowImage, -SHADOW_OFFSET, -SHADOW_OFFSET, null );
    in_Graphics.drawImage( m_LabelImage, 0, 0, null );

    in_Graphics.setClip( oldClip );

  }

  
  private BufferedImage m_LabelImage;
  private BufferedImage m_ShadowImage;
  private final ShadowRenderer m_ShadowRenderer;
  private final int SHADOW_OFFSET;
  private final Map m_DesktopHints;
  
}

