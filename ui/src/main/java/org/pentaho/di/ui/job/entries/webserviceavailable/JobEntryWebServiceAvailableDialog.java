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


package org.pentaho.di.ui.job.entries.webserviceavailable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.webserviceavailable.JobEntryWebServiceAvailable;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * This dialog allows you to edit the webservice available job entry.
 *
 * @author Samatar
 * @since 05-11-2009
 *
 */

public class JobEntryWebServiceAvailableDialog extends JobEntryDialog implements JobEntryDialogInterface {
  private static Class<?> PKG = JobEntryWebServiceAvailable.class; // for i18n purposes, needed by Translator2!!

  private Label wlName;

  private Text wName;

  private FormData fdlName, fdName;

  private Label wlURL;

  private TextVar wURL;

  private FormData fdlURL, fdURL;

  private Label wlConnectTimeOut;

  private TextVar wConnectTimeOut;

  private FormData fdlConnectTimeOut, fdConnectTimeOut;

  private Label wlReadTimeOut;

  private TextVar wReadTimeOut;

  private FormData fdlReadTimeOut, fdReadTimeOut;

  private Button wOK, wCancel;

  private Listener lsOK, lsCancel;

  private JobEntryWebServiceAvailable jobEntry;

  private Shell shell;

  private SelectionAdapter lsDef;

  private boolean changed;

  public JobEntryWebServiceAvailableDialog( Shell parent, JobEntryInterface jobEntryInt, Repository rep,
    JobMeta jobMeta ) {
    super( parent, jobEntryInt, rep, jobMeta );
    jobEntry = (JobEntryWebServiceAvailable) jobEntryInt;
    if ( this.jobEntry.getName() == null ) {
      this.jobEntry.setName( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.Name.Default" ) );
    }
  }

  public JobEntryInterface open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, props.getJobsDialogStyle() );
    props.setLook( shell );
    JobDialog.setShellImage( shell, jobEntry );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        jobEntry.setChanged();
      }
    };
    changed = jobEntry.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.Title" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Filename line
    wlName = new Label( shell, SWT.RIGHT );
    wlName.setText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.Name.Label" ) );
    props.setLook( wlName );
    fdlName = new FormData();
    fdlName.left = new FormAttachment( 0, 0 );
    fdlName.right = new FormAttachment( middle, -margin );
    fdlName.top = new FormAttachment( 0, margin );
    wlName.setLayoutData( fdlName );
    wName = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wName );
    wName.addModifyListener( lsMod );
    fdName = new FormData();
    fdName.left = new FormAttachment( middle, 0 );
    fdName.top = new FormAttachment( 0, margin );
    fdName.right = new FormAttachment( 100, 0 );
    wName.setLayoutData( fdName );

    // URL line
    wlURL = new Label( shell, SWT.RIGHT );
    wlURL.setText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.URL.Label" ) );
    props.setLook( wlURL );
    fdlURL = new FormData();
    fdlURL.left = new FormAttachment( 0, 0 );
    fdlURL.top = new FormAttachment( wName, margin );
    fdlURL.right = new FormAttachment( middle, -margin );
    wlURL.setLayoutData( fdlURL );

    wURL = new TextVar( jobMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    props.setLook( wURL );
    wURL.addModifyListener( lsMod );
    fdURL = new FormData();
    fdURL.left = new FormAttachment( middle, 0 );
    fdURL.top = new FormAttachment( wName, margin );
    fdURL.right = new FormAttachment( 100, -margin );
    wURL.setLayoutData( fdURL );

    // Whenever something changes, set the tooltip to the expanded version:
    wURL.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        wURL.setToolTipText( jobMeta.environmentSubstitute( wURL.getText() ) );
      }
    } );

    // connect timeout line
    wlConnectTimeOut = new Label( shell, SWT.RIGHT );
    wlConnectTimeOut.setText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.ConnectTimeOut.Label" ) );
    props.setLook( wlConnectTimeOut );
    fdlConnectTimeOut = new FormData();
    fdlConnectTimeOut.left = new FormAttachment( 0, 0 );
    fdlConnectTimeOut.top = new FormAttachment( wURL, margin );
    fdlConnectTimeOut.right = new FormAttachment( middle, -margin );
    wlConnectTimeOut.setLayoutData( fdlConnectTimeOut );

    wConnectTimeOut = new TextVar( jobMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wConnectTimeOut.setToolTipText( BaseMessages.getString(
      PKG, "JobEntryWebServiceAvailable.ConnectTimeOut.Tooltip" ) );
    props.setLook( wConnectTimeOut );
    wConnectTimeOut.addModifyListener( lsMod );
    fdConnectTimeOut = new FormData();
    fdConnectTimeOut.left = new FormAttachment( middle, 0 );
    fdConnectTimeOut.top = new FormAttachment( wURL, margin );
    fdConnectTimeOut.right = new FormAttachment( 100, -margin );
    wConnectTimeOut.setLayoutData( fdConnectTimeOut );

    // Whenever something changes, set the tooltip to the expanded version:
    wConnectTimeOut.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        wConnectTimeOut.setToolTipText( jobMeta.environmentSubstitute( wConnectTimeOut.getText() ) );
      }
    } );

    // Read timeout line
    wlReadTimeOut = new Label( shell, SWT.RIGHT );
    wlReadTimeOut.setText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.ReadTimeOut.Label" ) );
    props.setLook( wlReadTimeOut );
    fdlReadTimeOut = new FormData();
    fdlReadTimeOut.left = new FormAttachment( 0, 0 );
    fdlReadTimeOut.top = new FormAttachment( wConnectTimeOut, margin );
    fdlReadTimeOut.right = new FormAttachment( middle, -margin );
    wlReadTimeOut.setLayoutData( fdlReadTimeOut );

    wReadTimeOut = new TextVar( jobMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wReadTimeOut.setToolTipText( BaseMessages.getString( PKG, "JobEntryWebServiceAvailable.ReadTimeOut.Tooltip" ) );
    props.setLook( wReadTimeOut );
    wReadTimeOut.addModifyListener( lsMod );
    fdReadTimeOut = new FormData();
    fdReadTimeOut.left = new FormAttachment( middle, 0 );
    fdReadTimeOut.top = new FormAttachment( wConnectTimeOut, margin );
    fdReadTimeOut.right = new FormAttachment( 100, -margin );
    wReadTimeOut.setLayoutData( fdReadTimeOut );

    // Whenever something changes, set the tooltip to the expanded version:
    wReadTimeOut.addModifyListener( new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        wReadTimeOut.setToolTipText( jobMeta.environmentSubstitute( wReadTimeOut.getText() ) );
      }
    } );

    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    FormData fd = new FormData();
    fd.right = new FormAttachment( 50, -10 );
    fd.bottom = new FormAttachment( 100, 0 );
    fd.width = 100;
    wOK.setLayoutData( fd );

    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );
    fd = new FormData();
    fd.left = new FormAttachment( 50, 10 );
    fd.bottom = new FormAttachment( 100, 0 );
    fd.width = 100;
    wCancel.setLayoutData( fd );

    BaseStepDialog.positionBottomButtons( shell, new Button[] { wOK, wCancel }, margin, wReadTimeOut );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wName.addSelectionListener( lsDef );
    wURL.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    getData();

    BaseStepDialog.setSize( shell );

    shell.open();
    props.setDialogSize( shell, "JobEntryWebServiceAvailableDialogSize" );
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return jobEntry;
  }

  public void dispose() {
    WindowProperty winprop = new WindowProperty( shell );
    props.setScreen( winprop );
    shell.dispose();
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    wName.setText( Const.nullToEmpty( jobEntry.getName() ) );
    wURL.setText( Const.nullToEmpty( jobEntry.getURL() ) );
    wConnectTimeOut.setText( Const.NVL( jobEntry.getConnectTimeOut(), "0" ) );
    wReadTimeOut.setText( Const.NVL( jobEntry.getReadTimeOut(), "0" ) );

    wName.selectAll();
    wName.setFocus();
  }

  private void cancel() {
    jobEntry.setChanged( changed );
    jobEntry = null;
    dispose();
  }

  private void ok() {
    if ( Utils.isEmpty( wName.getText() ) ) {
      MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
      mb.setText( BaseMessages.getString( PKG, "System.StepJobEntryNameMissing.Title" ) );
      mb.setMessage( BaseMessages.getString( PKG, "System.JobEntryNameMissing.Msg" ) );
      mb.open();
      return;
    }
    jobEntry.setName( wName.getText() );
    jobEntry.setURL( wURL.getText() );
    jobEntry.setConnectTimeOut( wConnectTimeOut.getText() );
    jobEntry.setReadTimeOut( wReadTimeOut.getText() );
    dispose();
  }

  public boolean evaluates() {
    return true;
  }

  public boolean isUnconditional() {
    return false;
  }

}
