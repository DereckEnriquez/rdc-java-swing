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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import org.jdesktop.swingx.graphics.GraphicsUtilities;


/** Provides facilities to work with images. <p> 
Class is utility and not intended for subclassing. */
public final class ImageUtils{

  private ImageUtils(){
  }
  
  
  /** I have taken the liberty of borrowing and modifying a utility from Chris 
  Campbell and Jim Graham, which also exists in some form in the SwingLabs 
  project on http://swinglabs.dev.java.net.
   
   * Convenience method that returns a scaled instance of the
   * provided BufferedImage.
   * 
   * 
   * @param img the original image to be scaled
   * @param targetWidth the desired width of the scaled instance,
   *    in pixels
   * @param targetHeight the desired height of the scaled instance,
   *    in pixels
   * @param hint one of the rendering hints that corresponds to
   *    RenderingHints.KEY_INTERPOLATION (e.g.
   *    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
   *    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
   *    RenderingHints.VALUE_INTERPOLATION_BICUBIC)
   * @param progressiveBilinear if true, this method will use a multi-step
   *    scaling technique that provides higher quality than the usual
   *    one-step technique (only useful in down-scaling cases, where
   *    targetWidth or targetHeight is
   *    smaller than the original dimensions)
   * @return a scaled version of the original BufferedImage
   */
  public static BufferedImage getFasterScaledInstance( BufferedImage img,
    int targetWidth, int targetHeight, Object hint,
    boolean progressiveBilinear ){
    int type = ( img.getTransparency() == Transparency.OPAQUE ) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = img;
    BufferedImage scratchImage = null;
    Graphics2D g2 = null;
    int w, h;
    int prevW = ret.getWidth();
    int prevH = ret.getHeight();
    boolean isTranslucent = img.getTransparency() != Transparency.OPAQUE;

    if( progressiveBilinear ){
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    }
    else{
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do{
      if( progressiveBilinear && w > targetWidth ){
        w /= 2;
        if( w < targetWidth ){
          w = targetWidth;
        }
      }

      if( progressiveBilinear && h > targetHeight ){
        h /= 2;
        if( h < targetHeight ){
          h = targetHeight;
        }
      }

      if( scratchImage == null || isTranslucent ){
        // Use a single scratch buffer for all iterations
        // and then copy to the final, correctly-sized image
        // before returning
        scratchImage = new BufferedImage( w, h, type );
        g2 = scratchImage.createGraphics();
      }
      g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, hint );
      g2.drawImage( ret, 0, 0, w, h, 0, 0, prevW, prevH, null );
      prevW = w;
      prevH = h;

      ret = scratchImage;
    }while( w != targetWidth || h != targetHeight );

    if( g2 != null ){
      g2.dispose();
    }

    // If we used a scratch buffer that is larger than our target size,
    // create an image of the right size and copy the results into it
    if( targetWidth != ret.getWidth() || targetHeight != ret.getHeight() ){
      scratchImage = new BufferedImage( targetWidth, targetHeight, type );
      g2 = scratchImage.createGraphics();
      g2.drawImage( ret, 0, 0, null );
      g2.dispose();
      ret = scratchImage;
    }

    return ret;
  }
  
  public static Image loadImage( final URL in_ImageUrl ){

    // final URL = new URL( (URL)in_ImageUrl.getContent(), null  ); TODO: iomplement defensive copy
    try{
      return GraphicsUtilities.loadCompatibleImage( in_ImageUrl );
    }
    catch( IOException exception ){
      throw new RuntimeException( exception );
    }

  }
  
}
