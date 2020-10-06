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
import com.mshostak.rdcclient.panels.PcImagePanel;
import com.mshostak.rdcclient.utils.GlobalExceptionHandler;
import com.mshostak.rdcclient.utils.GraphicsHelper;
import com.mshostak.rdcclient.utils.GraphicsHelper.*;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;


/** The application class. Describes life cycle of the application. 
Contains all application actions and simulators.
@author Maksym Shostak. */
public final class RdcClientApplication extends SingleFrameApplication{

  @Action( block = BlockingScope.APPLICATION )
  public Task connect(){
    
    final Task task = new ConnectSimulator();
    task.setInputBlocker( new BusyInputBlocker( task, m_BusyIndicator ) );
    return task;
    //m_MainFrame.changeScreens();
    
  }

  @Action
  public void disconnect(){
    /* Task task = new NetworkActivityTask();
    task.setInputBlocker( new BusyInputBlocker( task ) );
    return task; */
    m_MainView.changeScreens();
  }
  
  @Action( block = BlockingScope.APPLICATION )
  public Task logon(){
    
    final Task task = new LogonSimulator();
    task.setInputBlocker( new BusyInputBlocker( task, m_BusyIndicator ) );
    return task;
   
  }

  @Action( block = BlockingScope.APPLICATION )
  public void showVirtualPcView(){

    if( null == m_VirtualPcView ){
      m_VirtualPcView = new VirtualPcView( m_MainView.getFrame() );
    }
    else{
      // do nothing
    }

    // Minimize main frame
    if( Toolkit.getDefaultToolkit().isFrameStateSupported( Frame.ICONIFIED ) ){
      m_MainView.getFrame().setExtendedState( Frame.ICONIFIED );
    }
    else{
      m_MainView.getFrame().toBack();
    }

    m_VirtualPcView.show();

    // Restore main frame
    if( Toolkit.getDefaultToolkit().isFrameStateSupported( Frame.NORMAL ) ){
      m_MainView.getFrame().setExtendedState( Frame.NORMAL );
    }
    else{
      m_MainView.getFrame().toBack();
    }
    
  }
  
  @Action
  public void exitApplication(){
    exit();
  }

  
  @Override
  protected void initialize( String[] args ){
    
    super.initialize( args );
    
    GraphicsHelper.setLookAndFeel();
    GradientContentPane.loadLogoImages( this.getClass() );
    PcImagePanel.loadPcImage( this.getClass() );
    
  }
  
  @Override
  protected void startup(){
    
    // Set up busy indicator
    m_MainView = new MainView( this );
    m_BusyIndicator = new BusyIndicator();
    final JFrame viewFrame = m_MainView.getFrame();
    viewFrame.setGlassPane( m_BusyIndicator );
    
    m_GlobalExceptionHandler.setOwnerWindow( viewFrame );
    
    show( m_MainView );
    
  }
  
  /** TODO: consider this method.
   * This method is to initialize the specified window by injecting resources.
   * Windows shown in our application come fully initialized from the GUI
   * builder, so this additional configuration is not needed.
  @Override
  protected void configureWindow( java.awt.Window root ){
   
  }*/

  @Override
  protected void shutdown(){
    super.shutdown();
    m_MainView.saveComponentValues();
  }

  
  private final class ConnectSimulator extends NetworkSimulator{

    @Override
    protected void succeeded( Void ignored ){
      //setMessage( "Done" );
      m_MainView.changeScreens();
    }
    
  }
  
  private final class LogonSimulator extends NetworkSimulator{

    private LogonSimulator(){
      super();
      m_MainView.checkUserData();
    }

    @Override
    protected void succeeded( Void ignored ){
      showVirtualPcView();
    }

  }
  
  private class NetworkSimulator extends Task<Void, Void>{
    
    private NetworkSimulator(){
      
      super( RdcClientApplication.this );
      setUserCanCancel( true );
      
      m_Duration = 1000L + Math.round( Math.random() * 1000D );
      
    }

    @Override
    protected Void doInBackground() throws InterruptedException{
      
      final long CYCLE_DURATION = m_Duration / CYCLES_COUNT;
      
      for( int i = 0; i < CYCLES_COUNT; i++ ){
        //setMessage( "Connecting to server... [" + i + "]" );
        Thread.sleep( CYCLE_DURATION );
        //setProgress( i, 0, 49 );
      }
      
      //Thread.sleep( 150L );
      return null;
      
    }

    @Override
    protected void cancelled(){
      setMessage( "Canceled" );
    }

    private final long m_Duration;
    private static final int CYCLES_COUNT = 10;
    
  }

   
  private MainView m_MainView;
  private VirtualPcView m_VirtualPcView;
  private BusyIndicator m_BusyIndicator;
  private static final GlobalExceptionHandler m_GlobalExceptionHandler =
    new GlobalExceptionHandler();

  
  public static void main( String[] args ){
    //  Thread.setDefaultUncaughtExceptionHandler( m_GlobalExceptionHandler );
    launch( RdcClientApplication.class, args );
  }

}


//final JFrame mainFrame = getMainFrame();
//mainFrame.setResizable( false );
// final JComponent mainComponent = createMainComponent();
// mainFrame.add( mainComponent );
//show( mainFrame );
