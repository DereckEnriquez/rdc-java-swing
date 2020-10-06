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

import com.mshostak.rdcclient.utils.GraphicsConstants.ApplicationScreens;
import com.mshostak.rdcclient.utils.PanelBordersFactory;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;


/** North content pane. */
public final class NorthPanel implements ContentPanel{

  public NorthPanel(){
    decoratePanel();
  }
  
  
  @Override
  public JPanel getComponent( final ApplicationScreens in_Screen ){
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

  
  private void decoratePanel(){
    final Border decorativeBorder = PanelBordersFactory.getNorthPanelBorder();
    m_ThisPanel.setBorder( decorativeBorder );
  }

  
  private final JPanel m_ThisPanel = new OpaquePanel();

}
