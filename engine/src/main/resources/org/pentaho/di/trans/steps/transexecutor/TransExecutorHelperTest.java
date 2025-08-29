package org.pentaho.di.trans.steps.transexecutor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.pentaho.di.trans.TransMeta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.pentaho.di.trans.step.StepHelperInterface.ACTION_STATUS;
import static org.pentaho.di.trans.step.StepHelperInterface.FAILURE_METHOD_NOT_RESPONSE;

public class TransExecutorHelperTest {

  @Test
  public void doAction_returnsParameters_whenFieldNameIsParameters() throws KettleException  {
    TransExecutorMeta transExecutorMeta = mock( TransExecutorMeta.class );
    TransExecutorHelper helper = new TransExecutorHelper( transExecutorMeta );
    TransMeta transMeta = mock( TransMeta.class );
    TransExecutorParameters parameters = mock( TransExecutorParameters.class );
    when( transExecutorMeta.getParameters() ).thenReturn( parameters );
    try ( MockedStatic<TransExecutorMeta> mocked = Mockito.mockStatic(TransExecutorMeta.class)) {
      mocked.when(() -> TransExecutorMeta.loadMappingMeta(
        Mockito.any(TransExecutorMeta.class),
        Mockito.any(),
        Mockito.any(),
        Mockito.any(),
        Mockito.anyBoolean()
      )).thenReturn(transMeta);
      when( transMeta.listParameters() ).thenReturn( new String[] { "param1", "param2" } );
      when( transMeta.getParameterDescription( "param1" ) ).thenReturn( "desc1" );
      when( transMeta.getParameterDescription( "param2" ) ).thenReturn( "desc2" );
      when( transMeta.getParameterDefault( "param1" ) ).thenReturn( "default1" );
      when( transMeta.getParameterDefault( "param2" ) ).thenReturn( "default2" );

    JSONObject response = helper.doAction( "parameters", transMeta, null );

    assertNotNull( response );
    assertTrue( response.containsKey( "parameters" ) );
    assertTrue( response.get( "parameters" ) instanceof JSONArray );
    }
  }

  @Test
  public void doAction_returnsTransValid_whenFieldNameIsIsTransValid() {
    TransExecutorMeta transExecutorMeta = mock( TransExecutorMeta.class );
    TransExecutorHelper helper = new TransExecutorHelper( transExecutorMeta );
    TransMeta transMeta = mock( TransMeta.class );

    JSONObject response = helper.doAction( "isTransValid", transMeta, null );

    assertNotNull( response );
    assertTrue( response.containsKey( "transPresent" ) );
    assertTrue( response.get( "transPresent" ) instanceof Boolean );
  }

  @Test
  public void doAction_returnsFailureResponse_whenFieldNameIsInvalid() {
    TransExecutorMeta transExecutorMeta = mock( TransExecutorMeta.class );
    TransExecutorHelper helper = new TransExecutorHelper( transExecutorMeta );
    TransMeta transMeta = mock( TransMeta.class );

    JSONObject response = helper.doAction( "invalidField", transMeta, null );

    assertNotNull( response );
    assertEquals( FAILURE_METHOD_NOT_RESPONSE, response.get( ACTION_STATUS ) );
  }

  @Test
  public void parametersAction_returnsNonEmptyParameters() throws Exception {
    TransExecutorMeta transExecutorMeta = mock( TransExecutorMeta.class );
    TransExecutorHelper helper = new TransExecutorHelper( transExecutorMeta );
    TransMeta transMeta = mock( TransMeta.class );
    when( transExecutorMeta.getRepository() ).thenReturn( null );

    JSONObject response = helper.doAction( "parameters", transMeta, null );

    assertNotNull( response );
    assertTrue( response.containsKey( "parameters" ) );
    assertTrue( response.get( "parameters" ) instanceof JSONArray );
    assertFalse( ( (JSONArray) response.get( "parameters" ) ).isEmpty() );
  }

  @Test
  public void isTransValidAction_returnsFalse_whenTransMetaIsInvalid() throws Exception {
    TransExecutorMeta transExecutorMeta = mock( TransExecutorMeta.class );
    TransExecutorHelper helper = new TransExecutorHelper( transExecutorMeta );
    TransMeta transMeta = mock( TransMeta.class );
    doThrow( new Exception( "Invalid transformation" ) ).when( transExecutorMeta ).getRepository();

    JSONObject response = helper.doAction( "isTransValid", transMeta, null );

    assertNotNull( response );
    assertFalse( (Boolean) response.get( "transPresent" ) );
    assertTrue( response.containsKey( "errorMessage" ) );
  }
}