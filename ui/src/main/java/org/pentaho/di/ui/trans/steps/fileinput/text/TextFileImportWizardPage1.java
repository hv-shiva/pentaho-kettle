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


package org.pentaho.di.ui.trans.steps.fileinput.text;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.gui.TextFileInputFieldInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.steps.fileinput.text.TextFileInputMeta;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.widget.TableDraw;

public class TextFileImportWizardPage1 extends WizardPage { // implements Listener
  private static Class<?> PKG = TextFileInputMeta.class; // for i18n purposes, needed by Translator2!!

  private TableDraw wTable;
  private FormData fdTable;

  private PropsUI props;
  private List<String> rows;
  private Vector<TextFileInputFieldInterface> fields;

  public TextFileImportWizardPage1( String arg, PropsUI props, List<String> rows,
      Vector<TextFileInputFieldInterface> fields ) {
    super( arg );
    this.props = props;
    this.rows = rows;
    this.fields = fields;

    setTitle( BaseMessages.getString( PKG, "TextFileImportWizardPage1.DialogTitle" ) );
    setDescription( BaseMessages.getString( PKG, "TextFileImportWizardPage1.DialogMessage" ) );
  }

  public void createControl( Composite parent ) {
    // create the composite to hold the widgets
    Composite composite = new Composite( parent, SWT.NONE );
    props.setLook( composite );

    FormLayout compLayout = new FormLayout();
    compLayout.marginHeight = Const.FORM_MARGIN;
    compLayout.marginWidth = Const.FORM_MARGIN;
    composite.setLayout( compLayout );

    MouseAdapter lsMouse = new MouseAdapter() {
      public void mouseDown( MouseEvent e ) {
        int s = getSize();
        // System.out.println("size = "+s);
        setPageComplete( s > 0 );
      }
    };

    wTable = new TableDraw( composite, props, this, fields );
    wTable.setRows( rows );
    props.setLook( wTable );
    wTable.setFields( fields );
    fdTable = new FormData();
    fdTable.left = new FormAttachment( 0, 0 );
    fdTable.right = new FormAttachment( 100, 0 );
    fdTable.top = new FormAttachment( 0, 0 );
    fdTable.bottom = new FormAttachment( 100, 0 );
    wTable.setLayoutData( fdTable );
    wTable.addMouseListener( lsMouse );

    // set the composite as the control for this page
    setControl( composite );
  }

  public void setFields( Vector<TextFileInputFieldInterface> fields ) {
    wTable.setFields( fields );
  }

  public Vector<TextFileInputFieldInterface> getFields() {
    return wTable.getFields();
  }

  public boolean canFlipToNextPage() {
    int size = getSize();
    if ( size > 0 ) {
      setErrorMessage( null );
      return true;
    } else {
      setErrorMessage( BaseMessages.getString( PKG, "TextFileImportWizardPage1.ErrorMarkerNeeded" ) );
      return false;
    }
  }

  public int getSize() {
    return wTable.getFields().size();
  }
}
