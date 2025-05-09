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


package org.pentaho.di.trans.steps.getxmldata;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.w3c.dom.Node;

/**
 * Describes an XML field and the position in an XML field.
 * 
 * @author Samatar, Brahim
 * @since 20-06-2007
 */
public class GetXMLDataField implements Cloneable {
  private static Class<?> PKG = GetXMLDataMeta.class; // for i18n purposes, needed by Translator2!!

  public static final int RESULT_TYPE_VALUE_OF = 0;
  public static final int RESULT_TYPE_TYPE_SINGLE_NODE = 1;

  public static final String[] ResultTypeCode = { "valueof", "singlenode" };

  public static final String[] ResultTypeDesc = { BaseMessages.getString( PKG, "GetXMLDataField.ResultType.ValueOf" ),
    BaseMessages.getString( PKG, "GetXMLDataField.ResultType.SingleNode" ) };

  public static final int TYPE_TRIM_NONE = 0;
  public static final int TYPE_TRIM_LEFT = 1;
  public static final int TYPE_TRIM_RIGHT = 2;
  public static final int TYPE_TRIM_BOTH = 3;

  public static final int ELEMENT_TYPE_NODE = 0;
  public static final int ELEMENT_TYPE_ATTRIBUT = 1;

  public static final String[] trimTypeCode = { "none", "left", "right", "both" };

  public static final String[] trimTypeDesc = { BaseMessages.getString( PKG, "GetXMLDataField.TrimType.None" ),
    BaseMessages.getString( PKG, "GetXMLDataField.TrimType.Left" ),
    BaseMessages.getString( PKG, "GetXMLDataField.TrimType.Right" ),
    BaseMessages.getString( PKG, "GetXMLDataField.TrimType.Both" ) };

  // //////////////////////////////////////////////////////////////
  //
  // Conversion to be done to go from "attribute" to "attribute"
  // - The output is written as "attribut" but both "attribut" and
  // "attribute" are accepted as input.
  // - When v3.1 is being deprecated all supported versions will
  // support "attribut" and "attribute". Then output "attribute"
  // as all version support it.
  // - In a distant future remove "attribut" all together in v5 or so.
  //
  // TODO Sven Boden
  //
  // //////////////////////////////////////////////////////////////
  public static final String[] ElementTypeCode = { "node", "attribute" };

  public static final String[] ElementOldTypeCode = { "node", "attribut" };

  public static final String[] ElementTypeDesc = { BaseMessages.getString( PKG, "GetXMLDataField.ElementType.Node" ),
    BaseMessages.getString( PKG, "GetXMLDataField.ElementType.Attribute" ) };

  private String name;
  private String xpath;
  private String resolvedXpath;

  private int type;
  private int length;
  private String format;
  private int trimtype;
  private int elementtype;
  private int resulttype;
  private int precision;
  private String currencySymbol;
  private String decimalSymbol;
  private String groupSymbol;
  private boolean repeat;

  public GetXMLDataField( String fieldname ) {
    this.name = fieldname;
    this.xpath = "";
    this.length = -1;
    this.type = ValueMetaInterface.TYPE_STRING;
    this.format = "";
    this.trimtype = TYPE_TRIM_NONE;
    this.elementtype = ELEMENT_TYPE_NODE;
    this.resulttype = RESULT_TYPE_VALUE_OF;
    this.groupSymbol = "";
    this.decimalSymbol = "";
    this.currencySymbol = "";
    this.precision = -1;
    this.repeat = false;
  }

  public GetXMLDataField() {
    this( "" );
  }

  public String getXML() {
    StringBuffer retval = new StringBuffer( 400 );

    retval.append( "      <field>" ).append( Const.CR );
    retval.append( "        " ).append( XMLHandler.addTagValue( "name", getName() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "xpath", getXPath() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "element_type", getElementTypeCode() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "result_type", getResultTypeCode() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "type", getTypeDesc() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "format", getFormat() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "currency", getCurrencySymbol() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "decimal", getDecimalSymbol() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "group", getGroupSymbol() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "length", getLength() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "precision", getPrecision() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "trim_type", getTrimTypeCode() ) );
    retval.append( "        " ).append( XMLHandler.addTagValue( "repeat", isRepeated() ) );

    retval.append( "      </field>" ).append( Const.CR );

    return retval.toString();
  }

  public GetXMLDataField( Node fnode ) throws KettleValueException {
    setName( XMLHandler.getTagValue( fnode, "name" ) );
    setXPath( XMLHandler.getTagValue( fnode, "xpath" ) );
    setElementType( getElementTypeByCode( XMLHandler.getTagValue( fnode, "element_type" ) ) );
    setResultType( getResultTypeByCode( XMLHandler.getTagValue( fnode, "result_type" ) ) );
    setType( ValueMeta.getType( XMLHandler.getTagValue( fnode, "type" ) ) );
    setFormat( XMLHandler.getTagValue( fnode, "format" ) );
    setCurrencySymbol( XMLHandler.getTagValue( fnode, "currency" ) );
    setDecimalSymbol( XMLHandler.getTagValue( fnode, "decimal" ) );
    setGroupSymbol( XMLHandler.getTagValue( fnode, "group" ) );
    setLength( Const.toInt( XMLHandler.getTagValue( fnode, "length" ), -1 ) );
    setPrecision( Const.toInt( XMLHandler.getTagValue( fnode, "precision" ), -1 ) );
    setTrimType( getTrimTypeByCode( XMLHandler.getTagValue( fnode, "trim_type" ) ) );
    setRepeated( !"N".equalsIgnoreCase( XMLHandler.getTagValue( fnode, "repeat" ) ) );
  }

  public static final int getTrimTypeByCode( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    for ( int i = 0; i < trimTypeCode.length; i++ ) {
      if ( trimTypeCode[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }
    return 0;
  }

  public static final int getElementTypeByCode( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    // / Code to be removed later on as explained in the top of
    // this file.
    // //////////////////////////////////////////////////////////////
    for ( int i = 0; i < ElementOldTypeCode.length; i++ ) {
      if ( ElementOldTypeCode[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }
    // //////////////////////////////////////////////////////////////

    for ( int i = 0; i < ElementTypeCode.length; i++ ) {
      if ( ElementTypeCode[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }

    return 0;
  }

  public static final int getTrimTypeByDesc( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    for ( int i = 0; i < trimTypeDesc.length; i++ ) {
      if ( trimTypeDesc[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }
    return 0;
  }

  public static final int getElementTypeByDesc( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    for ( int i = 0; i < ElementTypeDesc.length; i++ ) {
      if ( ElementTypeDesc[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }
    return 0;
  }

  public static final String getTrimTypeCode( int i ) {
    if ( i < 0 || i >= trimTypeCode.length ) {
      return trimTypeCode[0];
    }
    return trimTypeCode[i];
  }

  public static final String getElementTypeCode( int i ) {
    // To be changed to the new code once all are converted
    if ( i < 0 || i >= ElementOldTypeCode.length ) {
      return ElementOldTypeCode[0];
    }
    return ElementOldTypeCode[i];
  }

  public static final String getTrimTypeDesc( int i ) {
    if ( i < 0 || i >= trimTypeDesc.length ) {
      return trimTypeDesc[0];
    }
    return trimTypeDesc[i];
  }

  public static final String getElementTypeDesc( int i ) {
    if ( i < 0 || i >= ElementTypeDesc.length ) {
      return ElementTypeDesc[0];
    }
    return ElementTypeDesc[i];
  }

  public Object clone() {
    try {
      GetXMLDataField retval = (GetXMLDataField) super.clone();

      return retval;
    } catch ( CloneNotSupportedException e ) {
      return null;
    }
  }

  public int getLength() {
    return length;
  }

  public void setLength( int length ) {
    this.length = length;
  }

  public String getName() {
    return name;
  }

  public String getXPath() {
    return xpath;
  }

  protected String getResolvedXPath() {
    return resolvedXpath;
  }

  public void setXPath( String fieldxpath ) {
    this.xpath = fieldxpath;
  }

  protected void setResolvedXPath( String resolvedXpath ) {
    this.resolvedXpath = resolvedXpath;
  }

  public void setName( String fieldname ) {
    this.name = fieldname;
  }

  public int getType() {
    return type;
  }

  public String getTypeDesc() {
    return ValueMeta.getTypeDesc( type );
  }

  public void setType( int type ) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat( String format ) {
    this.format = format;
  }

  public int getTrimType() {
    return trimtype;
  }

  public int getElementType() {
    return elementtype;
  }

  public String getTrimTypeCode() {
    return getTrimTypeCode( trimtype );
  }

  public String getElementTypeCode() {
    return getElementTypeCode( elementtype );
  }

  public String getTrimTypeDesc() {
    return getTrimTypeDesc( trimtype );
  }

  public String getElementTypeDesc() {
    return getElementTypeDesc( elementtype );
  }

  public void setTrimType( int trimtype ) {
    this.trimtype = trimtype;
  }

  public void setElementType( int element_type ) {
    this.elementtype = element_type;
  }

  public String getGroupSymbol() {
    return groupSymbol;
  }

  public void setGroupSymbol( String group_symbol ) {
    this.groupSymbol = group_symbol;
  }

  public String getDecimalSymbol() {
    return decimalSymbol;
  }

  public void setDecimalSymbol( String decimal_symbol ) {
    this.decimalSymbol = decimal_symbol;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol( String currency_symbol ) {
    this.currencySymbol = currency_symbol;
  }

  public int getPrecision() {
    return precision;
  }

  public void setPrecision( int precision ) {
    this.precision = precision;
  }

  public boolean isRepeated() {
    return repeat;
  }

  public void setRepeated( boolean repeat ) {
    this.repeat = repeat;
  }

  public void flipRepeated() {
    repeat = !repeat;
  }

  public static final int getResultTypeByDesc( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    for ( int i = 0; i < ResultTypeDesc.length; i++ ) {
      if ( ResultTypeDesc[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }
    return 0;
  }

  public String getResultTypeDesc() {
    return getResultTypeDesc( resulttype );
  }

  public static final String getResultTypeDesc( int i ) {
    if ( i < 0 || i >= ResultTypeDesc.length ) {
      return ResultTypeDesc[0];
    }
    return ResultTypeDesc[i];
  }

  public int getResultType() {
    return resulttype;
  }

  public void setResultType( int resulttype ) {
    this.resulttype = resulttype;
  }

  public static final int getResultTypeByCode( String tt ) {
    if ( tt == null ) {
      return 0;
    }

    for ( int i = 0; i < ResultTypeCode.length; i++ ) {
      if ( ResultTypeCode[i].equalsIgnoreCase( tt ) ) {
        return i;
      }
    }

    return 0;
  }

  public static final String getResultTypeCode( int i ) {
    if ( i < 0 || i >= ResultTypeCode.length ) {
      return ResultTypeCode[0];
    }
    return ResultTypeCode[i];
  }

  public String getResultTypeCode() {
    return getResultTypeCode( resulttype );
  }
}
