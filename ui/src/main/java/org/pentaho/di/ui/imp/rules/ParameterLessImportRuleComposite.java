/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.di.ui.imp.rules;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.imp.rule.ImportRuleInterface;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.imp.rule.ImportRuleCompositeInterface;

public class ParameterLessImportRuleComposite implements ImportRuleCompositeInterface {

  private Composite composite;

  public Composite getComposite( Composite parent, ImportRuleInterface importRule ) {
    composite = new Composite( parent, SWT.NONE );
    PropsUI.getInstance().setLook( composite );
    composite.setLayout( new FillLayout() );

    return composite;
  }

  public void setCompositeData( ImportRuleInterface importRule ) {
  }

  public void getCompositeData( ImportRuleInterface importRule ) {
  }
}
