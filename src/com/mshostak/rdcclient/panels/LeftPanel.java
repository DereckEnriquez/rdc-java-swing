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

import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mshostak.rdcclient.utils.GraphicsHelper;
import com.mshostak.rdcclient.utils.PanelBordersFactory;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Resource;
import org.jdesktop.application.ResourceMap;


/** Left content pane. */
public final class LeftPanel implements ContentPanel{

  public LeftPanel( final ApplicationContext in_ApplicationContext ){
    
    final ResourceMap resourceMap = in_ApplicationContext.getResourceMap(
      LeftPanel.class );
    resourceMap.injectFields( this );
    initComponents();
    
  }

  
  @Override
  public JPanel getComponent( final ApplicationScreens in_Screen ){
    
    createThisPanel();
    
    if( in_Screen == ApplicationScreens.Connect ){
      m_FirstLineHintLabel.setText( FirstLineConnectHintLabel );
      m_SecondLineHintLabel.setText( SecondLineConnectHintLabel );
    }
    else if( in_Screen == ApplicationScreens.Logon ){
      m_FirstLineHintLabel.setText( FirstLineLogonHintLabel );
      m_SecondLineHintLabel.setText( SecondLineLogonHintLabel );
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }

    final CellConstraints cc = new CellConstraints();
    m_ThisPanel.add( m_RdcClientLabel, cc.xy( 1, 4 ) );
    m_ThisPanel.add( m_WelcomeLabel, cc.xy( 1, 2 ) );
    m_ThisPanel.add( m_FirstLineHintLabel, cc.xy( 1, 6 ) );
    m_ThisPanel.add( m_SecondLineHintLabel, cc.xy( 1, 8 ) );
    
    return m_ThisPanel;
    
  }

  @Override
  public JButton getDefaultButton( ApplicationScreens in_Screen ){
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  @Override
  public void initEventHandling( ActionMap in_ActionMap ){
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  @Override
  public void setFocusOnComponent( ApplicationScreens in_Screen ){
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  
  private void createThisPanel(){

    final FormLayout layout = new FormLayout( "right:pref", 
      "0:grow(0.5), pref, $ugap, pref, $ugap, pref, $rgap, pref, 0:grow(0.5)" );

    if( IN_DEBUG ){
      m_ThisPanel = new FormDebugPanel( layout );
      FormDebugUtils.dumpAll( m_ThisPanel );
    }
    else{
      m_ThisPanel = new OpaquePanel( layout );
    }

    m_ThisPanel.setBorder( Borders.createEmptyBorder( "0, 0, 0, 7dlu" ) );
    decoratePanel();
    
  }

  private void initComponents(){
    
    m_WelcomeLabel = new JLabel();
    m_WelcomeLabel.setName( "WelcomeLabel" );
    
    m_RdcClientLabel = new ShadowedLabel();
    m_RdcClientLabel.setName( "RdcClientLabel" );
    
    m_FirstLineHintLabel = new JLabel();
    m_FirstLineHintLabel.setName( "FirstLineHintLabel" );
    
    m_SecondLineHintLabel = new JLabel();
    m_SecondLineHintLabel.setName( "SecondLineHintLabel" );
    
    GraphicsHelper.setBoldLabelFonts( new JLabel[]{ m_WelcomeLabel, 
      m_FirstLineHintLabel, m_SecondLineHintLabel } );
    GraphicsHelper.setH1LabelFonts( new JLabel[]{ m_RdcClientLabel } );
    
  }
  
  private void decoratePanel(){
    
    final Border decorativeBorder = PanelBordersFactory.getLeftPanelBorder();
    final Border newBorder = BorderFactory.createCompoundBorder(
      decorativeBorder, m_ThisPanel.getBorder() );

    m_ThisPanel.setBorder( newBorder );
    
  }
  
  
  private JPanel m_ThisPanel;
  private JLabel m_WelcomeLabel;
  private JLabel m_RdcClientLabel;
  private JLabel m_FirstLineHintLabel;
  private JLabel m_SecondLineHintLabel;
  
  @Resource String FirstLineConnectHintLabel;
  @Resource String SecondLineConnectHintLabel;
  @Resource String FirstLineLogonHintLabel;
  @Resource String SecondLineLogonHintLabel;
  
}