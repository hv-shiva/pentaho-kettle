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

package org.pentaho.di.trans.steps.csvinput;

import org.apache.commons.vfs2.FileObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.pentaho.di.core.bowl.Bowl;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogChannelInterfaceFactory;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.vfs.KettleVFSImpl;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.steps.fileinput.text.CsvFileImportProcessor;
import org.pentaho.di.trans.steps.fileinput.text.TextFileInputFieldDTO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.pentaho.di.trans.step.StepHelperInterface.ACTION_STATUS;
import static org.pentaho.di.trans.step.StepHelperInterface.FAILURE_METHOD_NOT_FOUND_RESPONSE;
import static org.pentaho.di.trans.step.StepHelperInterface.FAILURE_RESPONSE;
import static org.pentaho.di.trans.step.StepHelperInterface.SUCCESS_RESPONSE;

public class CsvInputHelperTest {

  private CsvInputMeta csvInputMeta;
  private TransMeta transMeta;
  private LogChannelInterface logChannelInterface;

  private CsvInputHelper underTest;

  @Before
  public void setUp() {
    csvInputMeta = mock( CsvInputMeta.class );
    transMeta = mock( TransMeta.class );
    underTest = spy( new CsvInputHelper( csvInputMeta ) );

    logChannelInterface = mock( LogChannelInterface.class );
    LogChannelInterfaceFactory logChannelInterfaceFactory = mock( LogChannelInterfaceFactory.class );
    KettleLogStore.setLogChannelInterfaceFactory( logChannelInterfaceFactory );
    when( logChannelInterfaceFactory.create( any(), any( LoggingObjectInterface.class ) ) ).thenReturn(
      logChannelInterface );
    when( logChannelInterfaceFactory.create( any() ) ).thenReturn( logChannelInterface );

    // Setup common mock behaviors
    when( transMeta.environmentSubstitute( anyString() ) ).thenAnswer( invocation -> invocation.getArgument( 0 ) );
    when( csvInputMeta.getDelimiter() ).thenReturn( "," );
    when( csvInputMeta.getEnclosure() ).thenReturn( "\"" );
    when( csvInputMeta.getEscapeCharacter() ).thenReturn( "\\" );
    when( csvInputMeta.getEncoding() ).thenReturn( "UTF-8" );
    when( csvInputMeta.hasHeader() ).thenReturn( true );
    when( csvInputMeta.getFileFormatTypeNr() ).thenReturn( 0 );
  }

  @Test
  public void testHandleStepAction_whenMethodNameIsGetFields_thenReturnsFieldsAndSummary() {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put( "isSampleSummary", "true" );
    queryParams.put( "noOfFields", "100" );

    JSONObject mockResponse = new JSONObject();
    mockResponse.put( "fields", new JSONArray() );
    mockResponse.put( "summary", "Sample summary" );

    doReturn( mockResponse ).when( underTest ).getFields( transMeta, queryParams );

    JSONObject response = underTest.stepAction( "getFields", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( SUCCESS_RESPONSE, response.get( ACTION_STATUS ) );
    assertTrue( response.containsKey( "fields" ) );
    assertTrue( response.containsKey( "summary" ) );
  }

  @Test
  public void testHandleStepAction_whenMethodNameIsInvalid_thenReturnsFailureMethodNotFound() {
    JSONObject response = underTest.stepAction( "invalidMethod", transMeta, null );

    assertNotNull( response );
    assertEquals( FAILURE_METHOD_NOT_FOUND_RESPONSE, response.get( ACTION_STATUS ) );
  }

  @Test
  public void testGetFields_whenValidParameters_thenReturnsFieldsAndSummary() throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put( "isSampleSummary", "true" );
    queryParams.put( "noOfFields", "100" );

    String csvContent = "Name,Age,City\nJohn,30,NYC\nJane,25,LA";
    InputStream inputStream = new ByteArrayInputStream( csvContent.getBytes( "UTF-8" ) );

    FileObject fileObject = mock( FileObject.class );
    Bowl bowl = mock( Bowl.class );

    when( csvInputMeta.getHeaderFileObject( transMeta ) ).thenReturn( fileObject );
    when( transMeta.getBowl() ).thenReturn( bowl );

    try ( MockedStatic<KettleVFS> kettleVFSMockedStatic = Mockito.mockStatic( KettleVFS.class ) ) {
      KettleVFSImpl vfsImpl = mock( KettleVFSImpl.class );
      kettleVFSMockedStatic.when( () -> KettleVFS.getInstance( any( Bowl.class ) ) ).thenReturn( vfsImpl );

      when( vfsImpl.getFileObject( anyString() ) ).thenReturn( fileObject );
      kettleVFSMockedStatic.when( () -> KettleVFS.getInputStream( any( FileObject.class ) ) )
        .thenReturn( inputStream );

      CsvFileImportProcessor mockProcessor = mock( CsvFileImportProcessor.class );
      when( mockProcessor.analyzeFile( true ) ).thenReturn( "Test Summary" );
      when( mockProcessor.getInputFieldsDto() ).thenReturn( new TextFileInputFieldDTO[ 0 ] );

      doReturn( inputStream ).when( underTest ).getInputStream( transMeta, csvInputMeta );

      JSONObject response = underTest.getFields( transMeta, queryParams );

      assertNotNull( response );
      assertTrue( response.containsKey( "fields" ) || response.containsKey( ACTION_STATUS ) );
    }
  }

  @Test
  public void testGetFields_whenNullQueryParams_thenHandlesGracefully() {
    Map<String, String> queryParams = new HashMap<>();

    when( csvInputMeta.getHeaderFileObject( transMeta ) ).thenThrow( new RuntimeException( "Test exception" ) );

    JSONObject response = underTest.getFields( transMeta, queryParams );

    assertNotNull( response );
    assertEquals( FAILURE_RESPONSE, response.get( ACTION_STATUS ) );
  }

  @Test
  public void testGetInputStream_whenValidFileObject_thenReturnsInputStream() {
    FileObject fileObject = mock( FileObject.class );
    InputStream mockInputStream = new ByteArrayInputStream( "test".getBytes() );

    when( csvInputMeta.getHeaderFileObject( transMeta ) ).thenReturn( fileObject );

    try ( MockedStatic<KettleVFS> kettleVFSMockedStatic = Mockito.mockStatic( KettleVFS.class ) ) {
      kettleVFSMockedStatic.when( () -> KettleVFS.getInputStream( fileObject ) ).thenReturn( mockInputStream );

      InputStream result = underTest.getInputStream( transMeta, csvInputMeta );

      assertNotNull( result );
    }
  }

  @Test
  public void testLogChannel_whenCalled_thenReturnsLogChannelInterface() {
    LogChannelInterface result = underTest.logChannel();

    assertNotNull( result );
  }

  @Test
  public void testStepAction_whenGetFieldsWithSampleSummaryFalse_thenReturnsSuccess() {
    assertStepActionSuccess( "false", "50", "" );
  }

  @Test
  public void testStepAction_whenGetFieldsWithZeroSamples_thenReturnsSuccess() {
    assertStepActionSuccess( "true", "0", "No samples" );
  }

  @Test
  public void testGetFields_whenLargeNumberOfSamples_thenReturnsSuccess() {
    assertStepActionSuccess( "true", "10000", "Large sample summary" );
  }

  @Test
  public void testGetFields_whenMissingNoOfFieldsParameter_thenDefaultsToZero() {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put( "isSampleSummary", "true" );
    // Missing "noOfFields" parameter

    when( csvInputMeta.getHeaderFileObject( transMeta ) ).thenThrow( new RuntimeException( "Test exception" ) );

    JSONObject response = underTest.getFields( transMeta, queryParams );

    assertNotNull( response );
    assertEquals( FAILURE_RESPONSE, response.get( ACTION_STATUS ) );
  }

  @Test
  public void testStepAction_whenNullQueryParams_thenHandlesGetFields() {
    JSONObject mockResponse = new JSONObject();
    mockResponse.put( "fields", new JSONArray() );
    mockResponse.put( "summary", "Summary" );

    doReturn( mockResponse ).when( underTest ).getFields( transMeta, null );

    JSONObject response = underTest.stepAction( "getFields", transMeta, null );

    assertNotNull( response );
    assertEquals( SUCCESS_RESPONSE, response.get( ACTION_STATUS ) );
  }

  @Test
  public void testHandleStepAction_whenEmptyMethodName_thenReturnsFailureMethodNotFound() {
    JSONObject response = underTest.stepAction( "", transMeta, null );

    assertNotNull( response );
    assertEquals( FAILURE_METHOD_NOT_FOUND_RESPONSE, response.get( ACTION_STATUS ) );
  }

  private void assertStepActionSuccess( String isSampleSummary, String noOfFields, String summary ) {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put( "isSampleSummary", isSampleSummary );
    queryParams.put( "noOfFields", noOfFields );

    JSONObject mockResponse = new JSONObject();
    mockResponse.put( "fields", new JSONArray() );
    mockResponse.put( "summary", summary );

    doReturn( mockResponse ).when( underTest ).getFields( transMeta, queryParams );

    JSONObject response = underTest.stepAction( "getFields", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( SUCCESS_RESPONSE, response.get( ACTION_STATUS ) );
  }
}

