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


package org.pentaho.di.trans.steps.sort;

import java.text.Collator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.ArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.BooleanLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.IntLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.PrimitiveBooleanArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.PrimitiveIntArrayLoadSaveValidator;
import org.pentaho.di.trans.steps.loadsave.validator.StringLoadSaveValidator;
import org.pentaho.di.trans.steps.mock.StepMockHelper;

public class SortRowsMetaTest {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  /**
   * Replaced previous testRep with load/save tester. Should cover http://jira.pentaho.com/browse/BACKLOG-377
   * @throws KettleException
   */
  @Test
  public void testRoundTrips() throws KettleException {
    List<String> attributes = Arrays.asList( "Directory", "Prefix", "SortSize", "FreeMemoryLimit", "CompressFiles",
      "CompressFilesVariable", "OnlyPassingUniqueRows", "FieldName", "Ascending", "CaseSensitive", "CollatorEnabled",
      "CollatorStrength", "PreSortedField" );

    Map<String, String> getterMap = new HashMap<String, String>();
    Map<String, String> setterMap = new HashMap<String, String>();

    Map<String, FieldLoadSaveValidator<?>> fieldLoadSaveValidatorAttributeMap =
      new HashMap<String, FieldLoadSaveValidator<?>>();
    FieldLoadSaveValidator<String[]> stringArrayLoadSaveValidator =
      new ArrayLoadSaveValidator<String>( new StringLoadSaveValidator(), 25 );
    FieldLoadSaveValidator<boolean[]> booleanArrayLoadSaveValidator =
      new PrimitiveBooleanArrayLoadSaveValidator( new BooleanLoadSaveValidator(), 25 );
    FieldLoadSaveValidator<int[]> intArrayLoadSaveValidator =
      new PrimitiveIntArrayLoadSaveValidator( new IntLoadSaveValidator(), 25 );

    fieldLoadSaveValidatorAttributeMap.put( "FieldName", stringArrayLoadSaveValidator );
    fieldLoadSaveValidatorAttributeMap.put( "Ascending", booleanArrayLoadSaveValidator );
    fieldLoadSaveValidatorAttributeMap.put( "CaseSensitive", booleanArrayLoadSaveValidator );
    fieldLoadSaveValidatorAttributeMap.put( "CollatorEnabled", booleanArrayLoadSaveValidator );
    fieldLoadSaveValidatorAttributeMap.put( "CollatorStrength", intArrayLoadSaveValidator );
    fieldLoadSaveValidatorAttributeMap.put( "PreSortedField", booleanArrayLoadSaveValidator );

    LoadSaveTester<SortRowsMeta> loadSaveTester =
      new LoadSaveTester<>( SortRowsMeta.class, attributes, getterMap, setterMap,
        fieldLoadSaveValidatorAttributeMap, new HashMap<String, FieldLoadSaveValidator<?>>() );

    loadSaveTester.testSerialization();
  }

  @Test
  public void testGetDefaultStrength() {
    SortRowsMeta srm = new SortRowsMeta();
    int usStrength = srm.getDefaultCollationStrength( Locale.US );
    assertEquals( Collator.TERTIARY, usStrength );
    assertEquals( Collator.IDENTICAL, srm.getDefaultCollationStrength( null ) );
  }

  @Test
  public void testPDI16559() throws Exception {
    SortRowsMeta sortRowsReal = new SortRowsMeta();
    SortRowsMeta sortRows = Mockito.spy( sortRowsReal );
    Mockito.doNothing().when( sortRows ).registerUrlWithDirectory();
    sortRows.setDirectory( "/tmp" );
    sortRows.setFieldName( new String[] { "field1", "field2", "field3", "field4", "field5" } );
    sortRows.setAscending( new boolean[] { false, true, false } );
    sortRows.setCaseSensitive( new boolean[] { true, false, true, false } );
    sortRows.setCollatorEnabled( new boolean[] { false, false, true } );
    sortRows.setCollatorStrength( new int[] { 2, 1, 3 } );
    sortRows.setPreSortedField( new boolean[] { true, true, false } );

    try {
      String badXml = sortRows.getXML();
      Assert.fail( "Before calling afterInjectionSynchronization, should have thrown an ArrayIndexOOB" );
    } catch ( Exception expected ) {
      // Do Nothing
    }
    sortRows.afterInjectionSynchronization();
    //run without a exception
    String ktrXml = sortRows.getXML();

    int targetSz = sortRows.getFieldName().length;

    Assert.assertEquals( targetSz, sortRows.getAscending().length );
    Assert.assertEquals( targetSz, sortRows.getCaseSensitive().length );
    Assert.assertEquals( targetSz, sortRows.getCollatorEnabled().length );
    Assert.assertEquals( targetSz, sortRows.getCollatorStrength().length );
    Assert.assertEquals( targetSz, sortRows.getPreSortedField().length );

  }
}
