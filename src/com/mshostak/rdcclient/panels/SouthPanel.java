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

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.factories.Borders;
import com.mshostak.rdcclient.utils.GraphicsHelper;
import com.mshostak.rdcclient.utils.PanelBordersFactory;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


/** South content pane. */
public final class SouthPanel implements ContentPanel{
  
  public SouthPanel(){
    initComponents();
  }
  
  
  @Override
  public JPanel getComponent( final ApplicationScreens in_Screen ){
    
    final ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setBorder( Borders.createEmptyBorder( "14dlu, 0, 0, 0" ) );
    builder.setOpaque( false );
    
    builder.addGridded( m_ExitButton );
    builder.addUnrelatedGap();

    if( in_Screen == ApplicationScreens.Connect ){
    }
    else if( in_Screen == ApplicationScreens.Logon ){
      builder.addGridded( m_DisconnectButton );
      builder.addUnrelatedGap();
      setConnectedToServerLabel();
      builder.addFixed( m_ConnectedToServerLabel );
      builder.addUnrelatedGap();
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }

    builder.addGlue();
    builder.addFixed( m_CopyrightLabel );

    m_ThisPanel = builder.getPanel();
    
    decoratePanel();

    return m_ThisPanel;
      
  }

  @Override
  public JButton getDefaultButton( ApplicationScreens in_Screen ){
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  @Override
  public void initEventHandling( final ActionMap in_ActionMap ){
    m_ExitButton.setAction( in_ActionMap.get( "exitApplication" ) );
    m_DisconnectButton.setAction( in_ActionMap.get( "disconnect" ) );
  }

  @Override
  public void setFocusOnComponent( ApplicationScreens in_Screen ){
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  
  private void decoratePanel(){
    
    final Border decorativeBorder = PanelBordersFactory.getSouthPanelBorder();
    final Border newBorder = BorderFactory.createCompoundBorder( 
      decorativeBorder, m_ThisPanel.getBorder() );
    
    m_ThisPanel.setBorder( newBorder );
    
  }

  private void initComponents(){
    
    m_ExitButton = new JButton();
    m_ExitButton.setName( "ExitButton" );
    m_ExitButton.setVerticalTextPosition( AbstractButton.BOTTOM );
    
    m_DisconnectButton = new JButton();
    m_DisconnectButton.setName( "DisconnectButton" );
    m_DisconnectButton.setVerticalTextPosition( AbstractButton.BOTTOM );
    
    m_ConnectedToServerLabel = new JLabel();
    
    m_CopyrightLabel = new JLabel();
    m_CopyrightLabel.setName( "CopyrightLabel" );
    
    //SIZED_COMPONENTS = new JComponent[]{ m_ExitButton, m_DisconnectButton };
    //GraphicsHelper.setComponentsSizes( SIZED_COMPONENTS );
    GraphicsHelper.setBoldLabelFonts( new JLabel[]{ m_CopyrightLabel } );
        
  }

  private void setConnectedToServerLabel(){

    final String rawServerIpText = GraphicsHelper.getServerIpText();

    if( GraphicsHelper.DEFAULT_SERVERIP_LABEL_TEXT == rawServerIpText ){
      m_ConnectedToServerLabel.setText( "" );
    }
    else{
      m_ConnectedToServerLabel.setText( "Connected to: " + rawServerIpText );
    }
    
  }

  
  private JPanel m_ThisPanel;
  private JButton m_ExitButton;
  private JButton m_DisconnectButton;
  private JLabel m_ConnectedToServerLabel;
  private JLabel m_CopyrightLabel;

  //private JComponent SIZED_COMPONENTS[];

}
