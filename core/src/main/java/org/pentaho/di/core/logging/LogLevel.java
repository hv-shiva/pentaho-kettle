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


package org.pentaho.di.core.logging;

import org.pentaho.di.i18n.BaseMessages;

public enum LogLevel {

  NOTHING( 0, "Nothing" ), ERROR( 1, "Error" ), MINIMAL( 2, "Minimal" ), BASIC( 3, "Basic" ), DETAILED(
    4, "Detailed" ), DEBUG( 5, "Debug" ), ROWLEVEL( 6, "Rowlevel" );

  /** for i18n purposes, needed by Translator2!! */
  private static Class<?> PKG = LogLevel.class;

  public static final String[] logLevelDescriptions = {
    BaseMessages.getString( PKG, "LogWriter.Level.Nothing.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Error.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Minimal.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Basic.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Detailed.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Debug.LongDesc" ),
    BaseMessages.getString( PKG, "LogWriter.Level.Rowlevel.LongDesc" ), };

  private int level;
  private String code;

  private LogLevel( int level, String code ) {
    this.level = level;
    this.code = code;
  }

  public int getLevel() {
    return level;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return logLevelDescriptions[level];
  }

  /**
   * Return the log level for a certain log level code
   *
   * @param code
   *          the code to look for
   * @return the log level or BASIC if nothing matches.
   */
  public static LogLevel getLogLevelForCode( String code ) {
    for ( LogLevel logLevel : values() ) {
      if ( logLevel.getCode().equals( code ) ) {
        return logLevel;
      }
    }
    return BASIC;
  }

  /**
   * @param filterLogLevel
   *          the filter log level
   * @return true if the log level is visible compared to the filter log level specified
   */
  public boolean isVisible( LogLevel filterLogLevel ) {
    return filterLogLevel != null && getLevel() <= filterLogLevel.getLevel();
  }

  /**
   * @return true if this level is Error or lower
   */
  public boolean isError() {
    return this == ERROR;
  }

  /**
   * @return True if this level is Minimal or lower (which is nothing)
   */
  public boolean isNothing() {
    return this.level >= NOTHING.level;
  }

  /**
   * @return True if this level is Minimal
   */
  public boolean isMinimal() {
    return this.level >= MINIMAL.level;
  }

  /**
   * @return True if this level is Basic
   */
  public boolean isBasic() {
    return this.level >= BASIC.level;
  }

  /**
   * @return True if this level is Detailed
   */
  public boolean isDetailed() {
    return this.level >= DETAILED.level;
  }

  /**
   * @return True if this level is Debug
   */
  public boolean isDebug() {
    return this.level >= DEBUG.level;
  }

  /**
   * @return True if this level is Row level
   */
  public boolean isRowlevel() {
    return this.level >= ROWLEVEL.level;
  }

  /**
   * @return An array of log level descriptions, sorted by level (0==Nothing, 6=Row Level)
   */
  public static String[] getLogLevelDescriptions() {
    return logLevelDescriptions;
  }

  /**
   * @return An array of log level codes, sorted by level (0==Nothing, 6=Row Level)
   */
  public static String[] logLogLevelCodes() {
    String[] codes = new String[values().length];
    for ( int i = 0; i < codes.length; i++ ) {
      codes[i] = values()[i].getCode();
    }
    return codes;
  }
}
