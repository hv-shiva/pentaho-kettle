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


package org.pentaho.di.trans.steps.validator;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.injection.BaseMetadataInjectionTest;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class ValidatorMetaInjectionTest extends BaseMetadataInjectionTest<ValidatorMeta> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();
  @Before
  public void setup() {
    setup( new ValidatorMeta() );
  }

  @Test
  public void test() throws Exception {
    check( "VALIDATE_ALL", new BooleanGetter() {
      public boolean get() {
        return meta.isValidatingAll();
      }
    } );
    check( "CONCATENATE_ERRORS", new BooleanGetter() {
      public boolean get() {
        return meta.isConcatenatingErrors();
      }
    } );
    check( "CONCATENATION_SEPARATOR", new StringGetter() {
      public String get() {
        return meta.getConcatenationSeparator();
      }
    } );

    check( "NAME", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getName();
      }
    } );
    check( "FIELD_NAME", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getFieldName();
      }
    } );
    check( "MAX_LENGTH", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getMaximumLength();
      }
    } );
    check( "MIN_LENGTH", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getMinimumLength();
      }
    } );
    check( "NULL_ALLOWED", new BooleanGetter() {
      public boolean get() {
        return meta.getValidations().get( 0 ).isNullAllowed();
      }
    } );
    check( "ONLY_NULL_ALLOWED", new BooleanGetter() {
      public boolean get() {
        return meta.getValidations().get( 0 ).isOnlyNullAllowed();
      }
    } );
    check( "ONLY_NUMERIC_ALLOWED", new BooleanGetter() {
      public boolean get() {
        return meta.getValidations().get( 0 ).isOnlyNumericAllowed();
      }
    } );

    skipPropertyTest( "DATA_TYPE" );

    check( "DATA_TYPE_VERIFIED", new BooleanGetter() {
      public boolean get() {
        return meta.getValidations().get( 0 ).isDataTypeVerified();
      }
    } );
    check( "CONVERSION_MASK", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getConversionMask();
      }
    } );
    check( "DECIMAL_SYMBOL", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getDecimalSymbol();
      }
    } );
    check( "GROUPING_SYMBOL", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getGroupingSymbol();
      }
    } );
    check( "MIN_VALUE", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getMinimumValue();
      }
    } );
    check( "MAX_VALUE", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getMaximumValue();
      }
    } );
    check( "SOURCING_VALUES", new BooleanGetter() {
      public boolean get() {
        return meta.getValidations().get( 0 ).isSourcingValues();
      }
    } );
    check( "SOURCING_STEP_NAME", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getSourcingStepName();
      }
    } );
    check( "SOURCING_FIELD", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getSourcingField();
      }
    } );
    check( "START_STRING", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getStartString();
      }
    } );
    check( "START_STRING_NOT_ALLOWED", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getStartStringNotAllowed();
      }
    } );
    check( "END_STRING", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getEndString();
      }
    } );
    check( "END_STRING_NOT_ALLOWED", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getEndStringNotAllowed();
      }
    } );
    check( "REGULAR_EXPRESSION_EXPECTED", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getRegularExpression();
      }
    } );
    check( "REGULAR_EXPRESSION_NOT_ALLOWED", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getRegularExpressionNotAllowed();
      }
    } );
    check( "ERROR_CODE", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getErrorCode();
      }
    } );
    check( "ERROR_CODE_DESCRIPTION", new StringGetter() {
      public String get() {
        return meta.getValidations().get( 0 ).getErrorDescription();
      }
    } );
  }
}
