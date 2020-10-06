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
package com.mshostak.rdcclient;

import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.factories.Borders;
import com.mshostak.rdcclient.panels.PcImagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;


/** The mock Virtual PC view. */
public class VirtualPcView{

  public VirtualPcView( final Window in_Owner ){
    
    m_ThisView = new JDialog( in_Owner, ModalityType.APPLICATION_MODAL );
    
    initComponents();
    buildView();
    initEventHandling();
    
  }

  
  public void show(){
    
    // Set additional states
    //m_ThisView.pack();
    //m_ThisView.toFront();
    //assert ( m_ViewWindow.isAlwaysOnTopSupported() );
    //m_ViewWindow.setAlwaysOnTop( true );
    
    m_ThisView.setVisible( true );
    
  }

  public void hide(){
    m_ThisView.setVisible( false );
  }

  
  // TODO: reuse view class from UM.
  private void buildView(){
    
    m_ThisView.setLayout( new BorderLayout() );
    
    final ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setBorder( Borders.DIALOG_BORDER );
    
    builder.addGridded( m_LogOffButton );
    builder.addUnrelatedGap();
    builder.addGlue();
    
    final JPanel buttonPanel = builder.getPanel();
    m_ThisView.add( buttonPanel, BorderLayout.SOUTH );
    m_ThisView.add( m_ImagePanel, BorderLayout.CENTER );

  }

  private void initComponents(){
    
    // Init this view
    m_ThisView.setTitle( "Virtual PC 1 : The RDC Client" );
    m_ThisView.getContentPane().setBackground( Color.BLACK );
    m_ThisView.setResizable( false );
    m_ThisView.setUndecorated( true );
    
    // Set the window size to full screen size and show it
    final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    m_ThisView.setBounds( new Rectangle( SCREEN_SIZE ) );
    m_ThisView.setPreferredSize( SCREEN_SIZE );
    
    m_ImagePanel = new PcImagePanel();
        
    m_LogOffButton = new JButton();
    m_LogOffButton.setVerticalTextPosition( AbstractButton.BOTTOM );
    
  }

  private void initEventHandling(){
    m_LogOffButton.setAction( new LogOffAction( "Log off" ) );
    m_ThisView.getRootPane().setDefaultButton( m_LogOffButton );
  }

  
  private final JDialog m_ThisView;
  private JButton m_LogOffButton;
  private JPanel m_ImagePanel;
  
  
  private class LogOffAction extends AbstractAction{

    public LogOffAction( String name ){
      super( name );
      putValue( Action.MNEMONIC_KEY, KeyEvent.VK_F );
      final URL imgURL = VirtualPcView.class.getResource( LOGOFF_IMAGEICON_PATH );
      final ImageIcon imageIcon = new ImageIcon( imgURL );
      putValue( Action.LARGE_ICON_KEY, imageIcon );
    }

    @Override
    public void actionPerformed( ActionEvent e ){
      hide();
    }
    
  }
  
}
