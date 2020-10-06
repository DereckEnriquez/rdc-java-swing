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
import com.mshostak.rdcclient.utils.ImageUtils;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.JPanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;


/** A panel that displays screenshot of Virtual PC. */
public class PcImagePanel extends JPanel{

  public static void loadPcImage( final Class in_Class ){
    final URL imageUrl = in_Class.getResource( PC_IMAGE_PATH );
    m_PcImage = ( BufferedImage )ImageUtils.loadImage( imageUrl );
  }

  @Override
  protected void paintComponent( final Graphics in_Graphics ){
    
    //super.paintComponent( in_Graphics );
    assert ( null != m_PcImage );
    
    final int CURRENT_WIDTH = getWidth();
    final int CURRENT_HEIGHT = getHeight();
    
    final boolean IMAGE_IS_EMPTY = ( null == m_ContentImage );
    if( IMAGE_IS_EMPTY ){
      
      m_ContentImage = GraphicsUtilities.createCompatibleImage( CURRENT_WIDTH,
        CURRENT_HEIGHT );
      final Graphics2D graphics2D = ( Graphics2D )m_ContentImage.getGraphics();
      
      m_ContentImage = ImageUtils.getFasterScaledInstance( m_PcImage, 
        CURRENT_WIDTH, CURRENT_HEIGHT, 
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, false );
        
      graphics2D.dispose();
      
    }
    else{
      // do nothing
    }

    in_Graphics.drawImage( m_ContentImage, 0, 0, null );
    
  }
  
  
  private static BufferedImage m_PcImage;
  private Image m_ContentImage;
    
}
