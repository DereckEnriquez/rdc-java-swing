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
import static com.mshostak.rdcclient.utils.GraphicsConstants.*;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.LayoutMap;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;


/** Right content pane. */
public final class RightPanel implements ContentPanel{

  public RightPanel( final ApplicationContext in_ApplicationContext ){
    initComponents( in_ApplicationContext );
    loadComponentValues( in_ApplicationContext );
    ensureConsistentLabelsColumnWidth( null );
  }

  
  @Override
  public JPanel getComponent( final ApplicationScreens in_Screen ){
    
    if( in_Screen == ApplicationScreens.Connect ){
      buildConnectPanel();
    }
    else if( in_Screen == ApplicationScreens.Logon ){
      buildLogonPanel();
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }
    
    return m_ThisPanel;
    
  }

  @Override
  public JButton getDefaultButton( final ApplicationScreens in_Screen ){
    
    if( ApplicationScreens.Connect == in_Screen ){
      return m_ConnectButton;
    }
    else if( ApplicationScreens.Logon == in_Screen ){
      return m_LogonButton;
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }

  }

  public String getServerIpText(){
    return new String( m_ServerIpField.getText() );
  }

  public String getUsernameText(){
    return new String( m_UsernameField.getText() );
  }
  
  @Override
  public void initEventHandling( final ActionMap in_ActionMap ){

    m_ConnectButton.setAction( in_ActionMap.get( "connect" ) );
    m_ServerIpField.setAction( in_ActionMap.get( "connect" ) );
    m_LogonButton.setAction( in_ActionMap.get( "logon" ) );
    m_UsernameField.setAction( in_ActionMap.get( "logon" ) );
    
  }

  @Override
  public void setFocusOnComponent( final ApplicationScreens in_Screen ){
    
    if( ApplicationScreens.Connect == in_Screen ){
      m_ServerIpField.requestFocusInWindow();
    }
    else if( ApplicationScreens.Logon == in_Screen ){
      m_UsernameField.requestFocusInWindow();
    }
    else{
      throw new AssertionError( "Wrong screen" );
    }
    
  }

  public void saveComponentValues( final ApplicationContext in_ApplicationContext ){

    final LocalStorage localStorage = in_ApplicationContext.getLocalStorage();
    //final SessionStorage sessionStorage = in_ApplicationContext.getSessionStorage();

    final LinkedHashMap<String, String> componentValuesMap =
      new LinkedHashMap<String, String>();
    componentValuesMap.put( m_ServerIpField.getName(), m_ServerIpField.getText() );
    componentValuesMap.put( m_UsernameField.getName(), m_UsernameField.getText() );
    componentValuesMap.put( m_DesktopSizeComboBox.getName(),
      ( String )m_DesktopSizeComboBox.getSelectedItem() );
    
    try{
      localStorage.save( componentValuesMap, USER_SETTING_SFILEPATH );
    }
    catch( IOException ex ){
      Logger.getLogger( RightPanel.class.getName() ).log( Level.SEVERE, null, ex );
    }
    
  }

  private void buildConnectPanel(){

    final FormLayout layout = new FormLayout( COLUMN_SPEC, "0:grow(0.5), pref, 0:grow(0.5)" );
    createThisPanel( layout );

    final CellConstraints cc = new CellConstraints();
    m_ThisPanel.add( m_ServerIpLabel, cc.xy( 1, 2 ) );
    m_ThisPanel.add( m_ServerIpField, cc.xy( 3, 2 ) );

    final ButtonBarBuilder builder = getButtonBarBuilder();
    builder.addGridded( m_ConnectButton );
    m_ThisPanel.add( builder.getPanel(), cc.xy( 5, 2 ) );
    
  }

  private void buildLogonPanel(){

    final FormLayout layout = new FormLayout( COLUMN_SPEC, "0:grow(0.5), pref, $rgap, pref, $rgap, pref, 0:grow(0.5)" );
    createThisPanel( layout );

    final CellConstraints cc = new CellConstraints();
    m_ThisPanel.add( m_UsernameLabel, cc.xy( 1, 2 ) );
    m_ThisPanel.add( m_UsernameField, cc.xy( 3, 2 ) );
    m_ThisPanel.add( m_PasswordLabel, cc.xy( 1, 4 ) );
    m_ThisPanel.add( m_PasswordField, cc.xy( 3, 4 ) );
    m_ThisPanel.add( m_DesktopSizeLabel, cc.xy( 1, 6 ) );
    m_ThisPanel.add( m_DesktopSizeComboBox, cc.xy( 3, 6 ) );

    final ButtonBarBuilder builder = getButtonBarBuilder();
    builder.addGridded( m_LogonButton );
    m_ThisPanel.add( builder.getPanel(), cc.xy( 5, 6 ) );
    
  }
  
  private void createThisPanel( final FormLayout in_Layout ){

    if( IN_DEBUG ){
      m_ThisPanel = new FormDebugPanel( in_Layout );
      FormDebugUtils.dumpAll( m_ThisPanel );
    }
    else{
      m_ThisPanel = new OpaquePanel( in_Layout );
    }

    m_ThisPanel.setBorder( Borders.createEmptyBorder( "0, 7dlu, 0, 0" ) );

  }

  private void ensureConsistentLabelsColumnWidth( final ApplicationContext in_ApplicationContext ){

    // The mock implementation of the method
    
    //final ResourceMap resourceMap = in_ApplicationContext.getResourceMap(
      //RightPanel.class );
    /* If needed complete the computation of real width.
    final int SERVERIP_LABEL_LENGTH = resourceMap.getString( 
      "ServerIpLabel.text" ).length();
    final int USERNAME_LABEL_LENGTH = resourceMap.getString( 
      "UsernameLabel.text" ).length();
    final int PASSWORD_LABEL_LENGTH = resourceMap.getString( 
      "PasswordLabel.text" ).length();
    final int LABELS_WIDTH = Math.max( SERVERIP_LABEL_LENGTH, 
      Math.max( USERNAME_LABEL_LENGTH, PASSWORD_LABEL_LENGTH ) ); */

    final String longestString = "48dlu";//resourceMap.getString( "???" );
    LayoutMap.getRoot().columnPut( "label", "right:[" + longestString 
      + ", pref]"  );
    
  }

  private ButtonBarBuilder getButtonBarBuilder(){

    final ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.setBorder( Borders.EMPTY_BORDER );
    builder.setOpaque( false );
    return builder;
  }
  
  private void initComponents( final ApplicationContext in_ApplicationContext ){
    
    initConnectScreenComponents();
    initLogonSrceenComponents();
    
    /* SIZED_COMPONENTS = new JComponent[]{ m_ServerIpLabel, m_ServerIpField,
      m_ConnectButton, m_UsernameLabel, m_UsernameField, m_PasswordLabel, 
      m_PasswordField, m_DesktopSizeLabel, m_DesktopSizeComboBox, m_LogonButton };
    GraphicsHelper.setComponentsSizes( SIZED_COMPONENTS ); */
    
  }
 
  private void initConnectScreenComponents(){

    m_ServerIpLabel = new JLabel();
    m_ServerIpLabel.setName( "ServerIpLabel" );

    final MaskFormatter maskFormatter;
    try{
      maskFormatter = new MaskFormatter( "###.###.###.###" );
      maskFormatter.setPlaceholderCharacter( '0' );
    }
    catch( ParseException exception ){
      throw new RuntimeException( exception );
    }

    m_ServerIpField = new JFormattedTextField( maskFormatter );
    m_ServerIpField.setName( "ServerIpField" );
    m_ServerIpField.setColumns( TEXT_FIELDS_COLUMNS );
    m_ServerIpLabel.setLabelFor( m_ServerIpField );

    m_ConnectButton = new JButton();
    m_ConnectButton.setName( "ConectButton" );
    m_ConnectButton.setHorizontalTextPosition( AbstractButton.LEADING );
    m_ConnectButton.setVerticalTextPosition( AbstractButton.BOTTOM );

  }

  private void initLogonSrceenComponents(){

    m_UsernameLabel = new JLabel();
    m_UsernameLabel.setName( "UsernameLabel" );

    m_UsernameField = new JTextField();
    m_UsernameField.setName( "UsernameField" );
    m_UsernameField.setColumns( TEXT_FIELDS_COLUMNS );
    m_UsernameLabel.setLabelFor( m_UsernameField );

    m_PasswordLabel = new JLabel();
    m_PasswordLabel.setName( "PasswordLabel" );

    m_PasswordField = new JPasswordField();
    m_PasswordField.setName( "PasswordField" );
    m_PasswordField.setColumns( TEXT_FIELDS_COLUMNS );
    m_PasswordLabel.setLabelFor( m_PasswordField );

    m_DesktopSizeLabel = new JLabel();
    m_DesktopSizeLabel.setName( "DesktopSizeLabel" );

    m_DesktopSizeComboBox = new JComboBox( DESKTOP_SIZES  );
    m_DesktopSizeComboBox.setName( "DesktopSizeComboBox" );
    m_DesktopSizeLabel.setLabelFor( m_DesktopSizeComboBox );

    m_LogonButton = new JButton();
    m_LogonButton.setName( "LogonButton" );
    m_LogonButton.setHorizontalTextPosition( AbstractButton.LEADING );
    m_LogonButton.setVerticalTextPosition( AbstractButton.BOTTOM );
    
  }

  private void loadComponentValues( final ApplicationContext in_ApplicationContext ){

    final LocalStorage localStorage = in_ApplicationContext.getLocalStorage();
    //final SessionStorage sessionStorage = in_ApplicationContext.getSessionStorage();

    LinkedHashMap<String, String> componentValuesMap = null;
    try{
      componentValuesMap = ( LinkedHashMap<String, String> )localStorage.load( 
        USER_SETTING_SFILEPATH );
    }
    catch( IOException ex ){
      Logger.getLogger( RightPanel.class.getName() ).log( Level.SEVERE, null, ex );
    }
    
    if( null != componentValuesMap ){
      m_ServerIpField.setText( componentValuesMap.get( 
        m_ServerIpField.getName() ) );
      m_UsernameField.setText( componentValuesMap.get( 
        m_UsernameField.getName() ) );
      m_DesktopSizeComboBox.setSelectedItem( componentValuesMap.get(
        m_DesktopSizeComboBox.getName() ) );
    }
    else{
      // do nothing
    }

  }
  
  
  private JPanel m_ThisPanel;
  
  private JLabel m_ServerIpLabel;
  private JTextField m_ServerIpField;
  private JButton m_ConnectButton;
  
  private JLabel m_UsernameLabel;
  private JTextField m_UsernameField;
  private JLabel m_PasswordLabel;
  private JPasswordField m_PasswordField;
  private JLabel m_DesktopSizeLabel;
  private JComboBox m_DesktopSizeComboBox;
  private JButton m_LogonButton;
  
  private static final String USER_SETTING_SFILEPATH = "components.xml";
  //private JComponent SIZED_COMPONENTS[];
  private static final int TEXT_FIELDS_COLUMNS = 10;
  final String[] DESKTOP_SIZES = new String[]{ "1024 by 768", "1280 by 1024", 
    "Full Screen" };
  private static final String COLUMN_SPEC = 
    "$label, $lcgap, [50dlu,pref], $rgap, pref";

}