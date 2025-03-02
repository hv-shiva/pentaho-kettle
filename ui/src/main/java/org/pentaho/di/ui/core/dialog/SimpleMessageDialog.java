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

package org.pentaho.di.ui.core.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.i18n.BaseMessages;

import java.lang.reflect.Field;

/**
 * A simple message dialog containing a title, icon, message and a single button (OK by default) that closes the dialog.
 * The dialog width can be specified and the height is auto-adjusted based on the width.
 */
public class SimpleMessageDialog extends MessageDialog {

  protected static Class<?> PKG = SimpleMessageDialog.class;

  public static final int BUTTON_WIDTH = 65;
  public static final int DEFULT_WIDTH = 450;

  private int width;
  private int buttonWidth;

  /**
   * Creates a new dialog with the button label set to "Ok", dialog width set to {@link #DEFULT_WIDTH} and button width
   * set to {@link #BUTTON_WIDTH}
   *
   * @param parentShell the parent {@link Shell}
   * @param title       the dialog title
   * @param message     the dialog message
   * @param dialogType  the dialog type ({@link MessageDialog#INFORMATION}, {@link MessageDialog#WARNING}, {@link
   *                    MessageDialog#ERROR} etc...)
   */
  public SimpleMessageDialog( final Shell parentShell, final String title, final String message,
                              final int dialogType ) {
    this( parentShell, title, message, dialogType, BaseMessages.getString( PKG, "System.Button.OK" ),
      DEFULT_WIDTH, BUTTON_WIDTH );
  }

  /**
   * Creates a new dialog with the button label set to {@code closeButtonLabel}, dialog width set to {@link
   * #DEFULT_WIDTH} and button width set to {@link #BUTTON_WIDTH}
   *
   * @param parentShell the parent {@link Shell}
   * @param title       the dialog title
   * @param message     the dialog message
   * @param dialogType  the dialog type ({@link MessageDialog#INFORMATION}, {@link MessageDialog#WARNING}, {@link
   *                    MessageDialog#ERROR} etc...)
   * @param buttonLabel the label for the close dialog
   */
  public SimpleMessageDialog( final Shell parentShell, final String title, final String message,
                              final int dialogType, final String buttonLabel ) {
    this( parentShell, title, message, dialogType, buttonLabel, DEFULT_WIDTH, BUTTON_WIDTH );
  }

  /**
   * Creates a new dialog with the specified title, message, dialogType and width.
   *
   * @param parentShell the parent {@link Shell}
   * @param title       the dialog title
   * @param message     the dialog message
   * @param dialogType  the dialog type ({@link MessageDialog#INFORMATION}, {@link MessageDialog#WARNING}, {@link
   *                    MessageDialog#ERROR} etc...)
   * @param buttonLabel the button label
   * @param width       dialog width
   * @param buttonWidth button width
   */
  public SimpleMessageDialog( final Shell parentShell, final String title, final String message, final int dialogType,
                              final String buttonLabel, final int width, final int buttonWidth ) {
    super( parentShell, title, null, message, dialogType, new String[] { buttonLabel }, 0 );
    this.width = width;
    this.buttonWidth = buttonWidth;
  }

  /**
   * Overridden to auto-size the shell according to the selected width.
   */
  @Override
  protected void constrainShellSize() {
    super.constrainShellSize();
    try {
      // the shell property within the Windows class is private - need to access it via reflection
      final Field shellField = Window.class.getDeclaredField( "shell" );
      shellField.setAccessible( true );
      final Shell thisShell = (Shell) shellField.get( this );
      thisShell.pack();
      final int height = thisShell.computeSize( width, SWT.DEFAULT ).y;
      thisShell.setBounds( thisShell.getBounds().x, thisShell.getBounds().y, width + 4, height + 2 );
    } catch ( final Exception e ) {
      // nothing to do
    }
  }

  /**
   * Overridden to make the shell background white.
   *
   * @param shell
   */
  @Override
  protected void configureShell( Shell shell ) {
    super.configureShell( shell );
    shell.setBackground( shell.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    shell.setBackgroundMode( SWT.INHERIT_FORCE );
  }

  /**
   * Overridden to give the button the desired width.
   */
  @Override
  public void create() {
    super.create();
    final Button button = getButton( 0 );
    final int newX = button.getBounds().x + button.getBounds().width - buttonWidth;
    button.setBounds( newX, button.getBounds().y, buttonWidth, button.getBounds().height );
  }
}
