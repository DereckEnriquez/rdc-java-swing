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

import com.mshostak.rdcclient.panels.GradientContentPane;
import com.mshostak.rdcclient.panels.SouthPanel;
import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.mshostak.rdcclient.panels.ContentPanel;
import com.mshostak.rdcclient.panels.LeftPanel;
import com.mshostak.rdcclient.panels.NorthPanel;
import com.mshostak.rdcclient.panels.RightPanel;
import com.mshostak.rdcclient.utils.GraphicsHelper;
import java.awt.KeyboardFocusManager;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.transitions.ScreenTransition;
import org.jdesktop.animation.transitions.TransitionTarget;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;


/** The main application view. It constructs the content pane from other panels
and performs animated transactions between screens. */
final class MainView extends FrameView implements TransitionTarget{

  MainView( final Application in_Application ){
    
    super( in_Application );
    
    m_ThisFrame = getFrame();
    
    initComponents();
    initEventHandling();
    buildView( ApplicationScreens.Connect );
    initAnimatedTransactions();

  }

  
  @Override
  public void setupNextScreen(){
    m_ContentPane.removeAll();
    buildView( m_CurrentScreen );
    getResourceMap().injectComponents( m_ContentPane );
  }

  void changeScreens(){

    if( ApplicationScreens.Connect == m_CurrentScreen ){
      m_CurrentScreen = ApplicationScreens.Logon;
      GraphicsHelper.setServerIpText( m_RightPanel.getServerIpText() );
    }
    else if( ApplicationScreens.Logon == m_CurrentScreen ){
      m_CurrentScreen = ApplicationScreens.Connect;
      GraphicsHelper.setServerIpText( GraphicsHelper.DEFAULT_SERVERIP_LABEL_TEXT );
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }

    m_Transition.start();

  }

  void checkUserData(){
    
    final String userNameText = m_RightPanel.getUsernameText();
    final boolean USERNAME_IS_VALID = ( ( null != userNameText ) 
      && ( !userNameText.trim().isEmpty() ) );
    if( USERNAME_IS_VALID ){
      // do nothing
    }
    else{
      throw new IllegalArgumentException( 
        getResourceMap().getString( "EmptyUserNameMessage" ) );
    }
    
  }

  void saveComponentValues(){
    // TODO: Loading and saving needs refactoring
    m_RightPanel.saveComponentValues( getContext() );
  }
  
  void loadComponentValues(){
    // loads in panel constructors
  }
  
  
  private void buildView( final ApplicationScreens in_Screen ){
    
    if( null == m_ContentPane ){
      createContentPane();
      m_ThisFrame.setContentPane( m_ContentPane );
    }
    else{
      // do nothing
    }
    
    final CellConstraints cc = new CellConstraints();
    m_ContentPane.add( m_NorthPanel.getComponent( in_Screen ), cc.xyw( 1, 1, 2 ) );
    m_ContentPane.add( m_LeftPanel.getComponent( in_Screen ), cc.xy( 1, 2 ) );
    m_ContentPane.add( m_RightPanel.getComponent( in_Screen ), cc.xy( 2, 2 ) );
    m_ContentPane.add( m_SouthPanel.getComponent( in_Screen ), cc.xyw( 1, 3, 2 ) );

    setDefaultButtonAndFocus( in_Screen );
    
    m_CurrentScreen = in_Screen;
    
  }

  private void createContentPane(){

    final FormLayout layout = new FormLayout(
      "right:0:grow(0.5), left:0:grow(0.5)",
      "fill:34dlu, fill:pref:grow, pref" );

    if( IN_DEBUG ){
      m_ContentPane = new FormDebugPanel( layout );
      FormDebugUtils.dumpAll( m_ContentPane );
      JPanel gradientPane = new GradientContentPane( layout );
      m_ThisFrame.setPreferredSize( gradientPane.getPreferredSize() );
    }
    else{
      m_ContentPane = new GradientContentPane( layout );
    }

    m_ContentPane.setBorder( CONTENT_PANE_BORDER );
    
  }

  private void initAnimatedTransactions(){

    assert( null != m_ContentPane );
    
    m_Animator = new Animator( SCREEN_TRANSACTION_DURATION );
    m_Animator.setAcceleration( SCREEN_TRANSACTION_ACCELERATION );
    m_Animator.setDeceleration( SCREEN_TRANSACTION_DECELERATION );
    m_Transition = new ScreenTransition( m_ContentPane, this, m_Animator );
    
  }

  private void initComponents(){

    // Init this Frame
    assert( null != m_ThisFrame );
    m_ThisFrame.setResizable( false );

    // Init components
    m_NorthPanel = new NorthPanel();
    m_LeftPanel = new LeftPanel( getContext() );
    m_RightPanel = new RightPanel( getContext() );
    m_SouthPanel = new SouthPanel();

  }

  private void initEventHandling(){
    final ActionMap actionMap = getContext().getActionMap();
    m_SouthPanel.initEventHandling( actionMap );
    m_RightPanel.initEventHandling( actionMap );
  }

  private void setDefaultButtonAndFocus( final ApplicationScreens in_Screen ){

    final JRootPane rootPane = m_ThisFrame.getRootPane();
    final JButton defaultButton = m_RightPanel.getDefaultButton( in_Screen );
    rootPane.setDefaultButton( defaultButton );
    m_RightPanel.setFocusOnComponent( in_Screen );
        
  }
  
  
  private final JFrame m_ThisFrame;
  private JPanel m_ContentPane;
  private ContentPanel m_NorthPanel;
  private SouthPanel m_SouthPanel;
  private ContentPanel m_LeftPanel;
  private RightPanel m_RightPanel;
  /* TODO: refacttor to reduce member count to max: 7. Maybe, move below members 
  to Effects (Screen) Support helper class. */
  private ApplicationScreens m_CurrentScreen;
  private Animator m_Animator;
  private ScreenTransition m_Transition;

}