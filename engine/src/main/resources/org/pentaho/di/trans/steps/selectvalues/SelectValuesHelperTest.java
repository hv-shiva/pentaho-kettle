package org.pentaho.di.trans.steps.selectvalues;


import java.util.Collections;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.pentaho.di.trans.TransMeta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SelectValuesHelperTest {

  @Test
  public void doAction_returnsLocales_whenFieldNameIsLocales() {
    SelectValuesHelper helper = new SelectValuesHelper();
    TransMeta transMeta = mock( TransMeta.class );
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.doAction( "locales", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( "Action successful", response.get( "actionStatus" ) );
    assertTrue( response.containsKey( "locales" ) );
    assertTrue( response.get( "locales" ) instanceof JSONArray );
  }

  @Test
  public void doAction_returnsTimezones_whenFieldNameIsTimezones() {
    SelectValuesHelper helper = new SelectValuesHelper();
    TransMeta transMeta = mock( TransMeta.class );
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.doAction( "timezones", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( "Action successful", response.get( "actionStatus" ) );
    assertTrue( response.containsKey( "timezones" ) );
    assertTrue( response.get( "timezones" ) instanceof JSONArray );
  }

  @Test
  public void doAction_returnsEncodings_whenFieldNameIsEncodings() {
    SelectValuesHelper helper = new SelectValuesHelper();
    TransMeta transMeta = mock( TransMeta.class );
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.doAction( "encodings", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( "Action successful", response.get( "actionStatus" ) );
    assertTrue( response.containsKey( "encodings" ) );
    assertTrue( response.get( "encodings" ) instanceof JSONArray );
  }

  @Test
  public void doAction_returnsFailureResponse_whenFieldNameIsInvalid() {
    SelectValuesHelper helper = new SelectValuesHelper();
    TransMeta transMeta = mock( TransMeta.class );
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.doAction( "invalidField", transMeta, queryParams );

    assertNotNull( response );
    assertEquals( "Action failed with method not found", response.get( "actionStatus" ) );
  }

  @Test
  public void localesAction_returnsNonEmptyLocales() {
    SelectValuesHelper helper = new SelectValuesHelper();
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.localesAction(  );

    assertNotNull( response );
    assertTrue( response.containsKey( "locales" ) );
    assertTrue( response.get( "locales" ) instanceof JSONArray );
    assertFalse( ( (JSONArray) response.get( "locales" ) ).isEmpty() );
  }

  @Test
  public void timezonesAction_returnsNonEmptyTimezones() {
    SelectValuesHelper helper = new SelectValuesHelper();
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.timezonesAction(  );

    assertNotNull( response );
    assertTrue( response.containsKey( "timezones" ) );
    assertTrue( response.get( "timezones" ) instanceof JSONArray );
    assertFalse( ( (JSONArray) response.get( "timezones" ) ).isEmpty() );
  }

  @Test
  public void encodingsAction_returnsNonEmptyEncodings() {
    SelectValuesHelper helper = new SelectValuesHelper();
    Map<String, String> queryParams = Collections.emptyMap();

    JSONObject response = helper.encodingsAction(  );

    assertNotNull( response );
    assertTrue( response.containsKey( "encodings" ) );
    assertTrue( response.get( "encodings" ) instanceof JSONArray );
    assertFalse( ( (JSONArray) response.get( "encodings" ) ).isEmpty() );
  }
}
