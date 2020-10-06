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

import com.jgoodies.looks.Options;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.InputVerifier;
import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.InputBlocker;

/** Provides helper graphics functions.  */
public final class GraphicsHelper{

  private GraphicsHelper(){
  }
  
  
  public static void setBoldLabelFonts( final JLabel in_FontyLabels[] ){

    for( final JLabel fontyLabel: in_FontyLabels ){
      final Font labelFont = fontyLabel.getFont();
      fontyLabel.setFont( labelFont.deriveFont( Font.BOLD ) );
    }

  }
  
  public static void setH1LabelFonts( final JLabel in_FontyLabels[] ){
    
    for( final JLabel fontyLabel: in_FontyLabels ){
      final Font labelFont = fontyLabel.getFont();
      final float NEW_SIZE = (float)labelFont.getSize() + H1_FONT_INCREMENT;
      fontyLabel.setFont( labelFont.deriveFont( Font.BOLD, NEW_SIZE ) );
    }

  }
  
  public static void setH2LabelFonts( final JLabel in_FontyLabels[] ) {

    for( final JLabel fontyLabel: in_FontyLabels ){
      final Font labelFont = fontyLabel.getFont();
      final float NEW_SIZE = ( float )labelFont.getSize() + H2_FONT_INCREMENT;
      fontyLabel.setFont( labelFont.deriveFont( Font.BOLD, NEW_SIZE ) );
    }
    
  }
  
  public static void setLookAndFeel(){

    try{
      
      UIManager.setLookAndFeel( new WindowsLookAndFeel() ); //new NimbusLookAndFeel() );
      Options.setUseSystemFonts( true );
      
      //Options.setUseNarrowButtons( false );
      //Options.setHiResGrayFilterEnabled( true );
      
      // aJComboBox.putClientProperty( Options.COMBO_POPUP_PROTOTYPE_DISPLAY_VALUE_KEY,
        //"The longest text in the combo popup menu");
      
      // Select from end to start on keyboard focus gain.
      // Useful if the lead text shall be visible on focus gain
      // in a short field that often has a quite long text.
      // aTextField.putClientProperty( Options.INVERT_SELECTION_CLIENT_KEY, Boolean.TRUE );
      
      //    Font controlFont = Fonts.WINDOWS_VISTA_96DPI_NORMAL;
      //    FontSet fontSet = FontSets.createDefaultFontSet( controlFont );
      //    FontPolicy fontPolicy = FontPolicies.createFixedPolicy( fontSet );
      //    WindowsLookAndFeel.setFontPolicy( fontPolicy );
    
    }
    catch( Exception exception ){
      exception.printStackTrace();
    }

    /* for( UIManager.LookAndFeelInfo lookAndFeel: UIManager.getInstalledLookAndFeels() ){
    if( NIMBUS_LNF_NAME.equals( lookAndFeel.getName() ) ){
    try{
    UIManager.setLookAndFeel( lookAndFeel.getClassName() );
    }
    catch( Exception exception ){
    exception.printStackTrace();
    }
    }
    } */
    
  }

  // TODO: Change mouse cursor to busy (hour)
  public static final class BusyIndicator extends JComponent implements ActionListener{

    private int frame = -1;  // animation frame index
    private final int nBars = 8;
    private final float barWidth = 6;
    private final float outerRadius = 28;
    private final float innerRadius = 12;
    private final int trailLength = 4;
    private final float barGray = 200f;  // shade of gray, 0-255
    private final Timer timer = new Timer( 65, this ); // 65ms = animation rate

    public BusyIndicator(){

      setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
      MouseInputListener blockMouseEvents = new MouseInputAdapter(){
      };
      addMouseMotionListener( blockMouseEvents );
      addMouseListener( blockMouseEvents );
      InputVerifier retainFocusWhileVisible = new InputVerifier(){

        @Override
        public boolean verify( JComponent c ){
          return !c.isVisible();
        }

      };
      setInputVerifier( retainFocusWhileVisible );
    }

    @Override
    public void actionPerformed( ActionEvent ignored ){
      frame += 1;
      repaint();
    }

    void start(){
      setVisible( true );
      requestFocusInWindow();
      timer.start();
    }

    void stop(){
      setVisible( false );
      timer.stop();
    }

    @Override
    protected void paintComponent( Graphics g ){

      final Graphics2D g2d = (Graphics2D)g.create();
      grayOutTheBackground( g2d );

      RoundRectangle2D bar = new RoundRectangle2D.Float(
        innerRadius, -barWidth / 2, outerRadius, barWidth, barWidth, barWidth );
      // x,         y,          width,       height,   arc width,arc height
      double angle = Math.PI * 2.0 / (double)nBars; // between bars

      g2d.translate( getWidth() / 2, getHeight() / 2 );
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
      for( int i = 0; i < nBars; i++ ){
        // compute bar i's color based on the frame index
        Color barColor = new Color( (int)barGray, (int)barGray, (int)barGray );
        if( frame != -1 ){
          for( int t = 0; t < trailLength; t++ ){
            if( i == ( ( frame - t + nBars ) % nBars ) ){
              float tlf = (float)trailLength;
              float pct = 1.0f - ( ( tlf - t ) / tlf );
              int gray = (int)( ( barGray - ( pct * barGray ) ) + 0.5f );
              barColor = new Color( gray, gray, gray );
            }
          }
        }
        // draw the bar
        g2d.setColor( barColor );
        g2d.fill( bar );
        g2d.rotate( angle );
      }

      g2d.dispose();

    }

    private void grayOutTheBackground( final Graphics2D g2d ){

      // gets the current clipping area
      Rectangle clip = g2d.getClipBounds();

      final Composite oldComposite = g2d.getComposite();
      final Color oldColor = g2d.getColor();

      // sets a 65% translucent composite
      AlphaComposite newComposite = AlphaComposite.SrcOver.derive( 0.65f );
      g2d.setComposite( newComposite );

      // fills the background
      g2d.setColor( getBackground() );
      g2d.fillRect( clip.x, clip.y, clip.width, clip.height );

      // restore graphics
      g2d.setComposite( oldComposite );
      g2d.setColor( oldColor );

    }

  }

  public static final class BusyInputBlocker extends InputBlocker{

    public BusyInputBlocker( final Task in_Task, 
      final BusyIndicator in_Indicator  ){
      
      super( in_Task, Task.BlockingScope.WINDOW, in_Indicator );
      m_Indicator = in_Indicator;
      
    }

    @Override
    protected void block(){
      m_Indicator.start();
    }

    @Override
    protected void unblock(){
      m_Indicator.stop();
    }

    
    final BusyIndicator m_Indicator;
    
  }

  
  public static String getServerIpText(){
    return m_ServerIpText;
  }

  public static void setServerIpText( final String in_ServerIpText ){
    m_ServerIpText = in_ServerIpText;
  }
  
  
  public static final String DEFAULT_SERVERIP_LABEL_TEXT = null;
  private static String m_ServerIpText = DEFAULT_SERVERIP_LABEL_TEXT;// TODO: move to another class or create binding
    
  public static String NIMBUS_LNF_NAME = "Nimbus";
  
}



/* final class MyPlasticFontPolicy implements FontPolicy{

  public FontSet getFontSet( String lafName, UIDefaults table ){
    if( MyFontUtils.IS_TAHOMA_AVAILABE ){
      Font controlFont = MyFonts.getTahoma();
      return FontSets.createDefaultFontSet( controlFont );
    }
    if( MySystemUtils.IS_OS_LINUX_UBUNTU ){
      return MyFontSets.getUbuntuFontSet();
    }
    FontPolicy 
  



default = FontPolicies.getDefaultPlasticFontPolicy().
        return default.getFontSet(lafName, table);
    }
} */



/*
 
Locates the given component on the screen's center.
@param component   the component to be centered.
public static void

locateOnOpticalScreenCenter( final Component in_Component 

){
    
    final Dimension componentSize 

= in_Component.getSize();
    final Dimension screenSize 

= in_Component.getToolkit().getScreenSize();
    in_Component.setLocation

( ( screenSize.width - componentSize.width ) / 2,
      (int)( ( screenSize.height - componentSize.height ) * 0.45 ) );
    
  }

 
 
public static void setComponentsSizes( final JComponent in_SizedComponents[] ){

if( NIMBUS_LNF_NAME.equals( UIManager.getLookAndFeel().getName() ) ){
for( final JComponent component: in_SizedComponents ){
component.putClientProperty( "JComponent.sizeVariant", 
NIMBUS_COMPONENT_SIZE_VARIANT );
}
}
else{

/*final UnitConverter unitConverter = DefaultUnitConverter.getInstance();

for( final JComponent sizedComponent: in_SizedComponents ){

final Dimension PREFFERED_SIZE = new Dimension(
unitConverter.dialogUnitXAsPixel( COMMAND_BUTTON_WIDTH, sizedComponent ),
unitConverter.dialogUnitYAsPixel( COMMAND_BUTTON_HEIGHT, sizedComponent ) );
sizedComponent.setPreferredSize( PREFFERED_SIZE );

}

}

} */
