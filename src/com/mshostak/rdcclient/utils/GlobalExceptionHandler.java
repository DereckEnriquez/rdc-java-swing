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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import javax.management.RuntimeErrorException;
import javax.swing.JFrame;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;


/** The one and only global exceptions handler. 
@see http://stackoverflow.com/questions/95767/how-can-i-catch-awt-thread-exceptions-in-java 
Not sure, but earlier I wrote that "However it does not handles exceptions from 
Swing Application Framework Tasks". */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler{
  
  public void setOwnerWindow( final JFrame in_OwnerWindow ){
    
    final boolean CORRECT_METHOD_CALL = ( ( null == m_OwnerWindow ) 
      && ( null != in_OwnerWindow ) );
    if( CORRECT_METHOD_CALL ){
      m_OwnerWindow = in_OwnerWindow;
      m_ErrorPaneTitle = m_OwnerWindow.getTitle();
    }
    else{
      throw new AssertionError();
    }
    
    assert( null != m_OwnerWindow );
    
  }
  
  @Override
  public void uncaughtException( final Thread in_Thread, 
    final Throwable in_Throwable ){

    // Don't tell the bad guy to much info. See secure coding guidelines for Java...
    
    final Throwable trowableCopy = new Throwable( in_Throwable );
    final Throwable causedThrowable = findCausedThrowable( trowableCopy );
    final Throwable sanitizedThrowable = sanitizeThrowable( causedThrowable );
    
    displayMessage( sanitizedThrowable );
    
  }

  
  private void displayMessage( final Throwable in_Throwable ){
        
    // TODO: also try ErrorLevel.FATAL and custom ActionMap to handle shutdown.
    
    final ErrorInfo errorInfo = generateErrorInfo( in_Throwable );
    
    JXErrorPane errorPane = new JXErrorPane();
    errorPane.setErrorInfo( errorInfo );
    
    JXErrorPane.showDialog( m_OwnerWindow, errorPane );
    
  }

  private Throwable findCausedThrowable( final Throwable in_Throwable ){ // TODO: choose approach
    
    assert ( null != in_Throwable );

    // Search exceptions cascade until the cause is null. See approach 2 also.
    Throwable throwable = in_Throwable;
    while( true ){

      if( null != throwable.getCause() ){
        throwable = throwable.getCause();
      }
      else{
        return throwable;
      }

    }

  }

  private ErrorInfo generateErrorInfo( final Throwable in_Throwable ){
    
    final String messageBrief = getMessageBrief( in_Throwable );
    final String messageDetails = getMessageDetails( in_Throwable );
    
    return new ErrorInfo( m_ErrorPaneTitle, messageBrief, messageDetails, 
      "Category", in_Throwable, Level.WARNING, null ); //TODO: play with level
    
  }

  private String getMessageBrief( final Throwable in_Throwable ){
    
    if( in_Throwable instanceof SanitizedException ){
      return SANITIZED_MESSAGE_BRIEF;
    }
    else{
      return DEFAULT_MESSAGE_BRIEF;
    }
    
  }

  private String getMessageDetails( final Throwable in_Throwable ){

    final boolean DETAILS_ARE_CORRECT = ( null != in_Throwable.getMessage() ) 
      && ( !in_Throwable.getMessage().isEmpty() );
    
    if( DETAILS_ARE_CORRECT ){
      return in_Throwable.getMessage();
    }
    else{
      return DEFAULT_MESSAGE_DETAILS;
    }

  }
  
  private Throwable sanitizeThrowable( final Throwable in_Throwable ){
    
    if( SANITIZED_EXCEPTIONS.contains( in_Throwable.getClass() ) ){
      return new SanitizedException();
    }
    else{
      return in_Throwable;
    }
    
  }

  
  // TODO: implement in real program such approach, that exceptions message and
  // details will be base on the ID in the exception string (getMessage()), so
  // real text can be taken from resource bundle. See Code Complete.
  private static final String DEFAULT_MESSAGE_BRIEF = "Internal program error " 
    + "occured. Please contact the author.";
  private static final String DEFAULT_MESSAGE_DETAILS = "<html>This is just a prototype, " 
    + "so errors could occur. Anyway, let me know about them by email: " 
    + "mshostak@hotmail.com.</html>";
    /*+ "<p>An error occured on:</p><ul>" 
    + "<li>OS Name: ${os.name}.</li> " 
    + "<li>OS Version: ${os.version}.</li>" 
    + "<li>Java Version: ${java.version}.</li>" 
    + "<li>User Home: ${user.home}.</li> </ul>";*/
  private static final String SANITIZED_MESSAGE_BRIEF = "Exception was sanitized.";
  
  private JFrame m_OwnerWindow;
  private String m_ErrorPaneTitle;
  
  private static final Collection< Class<? extends Exception> > SANITIZED_EXCEPTIONS;

  static{
    SANITIZED_EXCEPTIONS =  new ArrayList< Class<? extends Exception> >( 
      Arrays.asList( SecurityException.class, SQLException.class ) );
  }

}















/*{// Approach 2: search exceptions cascade until it contains the one that needs sanitization
  Throwable throwableCause = in_Throwable;
  boolean needsSanitization = false;
  while( null != throwableCause ){

    if( SANITIZED_EXCEPTIONS.contains( throwableCause.getClass() ) ){
      needsSanitization = true;
      break;
    }
    else{
      // do nothing
    }

    throwableCause = throwableCause.getCause();

  }

} */


/*errorPane.setErrorReporter( new ErrorReporter() {
    
    @Override
    public void reportError( ErrorInfo info ) throws NullPointerException{
    System.out.println( "Exception handled: " 
    + in_Throwable.getClass().getSimpleName() );
    }
    } );*/

    
    /* You should be able to set a key in UIManager that points to your 
    UIDelegate rather than the one specified by the look and feel. Also, you 
    could just call setUI() on the JXErrorPane before showing it to use your 
    own UI delegate.
    
    From ErrorPaneAddon:
    defaults.add(JXErrorPane.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI");
    
    And JXErrorPane.uiClassID = "ErrorPaneUI"
    
    So, if you replace the "ErrorPaneUI" entry in UIDefaults with the class name 
    of the UI you would like to use, then it should use yours.
    
    errorPane.setUI( null ); */