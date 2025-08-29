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


package org.pentaho.di.trans.steps.selectvalues;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepHelperInterface;
import org.pentaho.di.trans.step.StepInterface;

public class SelectValuesHelper implements StepHelperInterface {

  /**
   * Executes an action based on the provided field name and query parameters.
   *
   * @param fieldName   The name of the field to determine the action to execute.
   * @param transMeta   The transformation metadata (not used in this implementation).
   * @param queryParams A map of query parameters (not used in this implementation).
   * @return A JSON object containing the result of the action and its status.
   */
  @Override
  public JSONObject doAction( String fieldName, TransMeta transMeta,
                              Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    try {
      switch ( fieldName ) {
        case "locales":
          response = localesAction();
          break;
        case "timezones":
          response = timezonesAction();
          break;
        case "encodings":
          response = encodingsAction();
          break;
        default:
          response.put( ACTION_STATUS, FAILURE_METHOD_NOT_RESPONSE );
          break;
      }
      if (!response.containsKey(ACTION_STATUS)) {
        if ( isFailedResponse( response ) ) {
          response.put( ACTION_STATUS, FAILURE_RESPONSE );
        } else if ( response != null ) {
          response.put( ACTION_STATUS, SUCCESS_RESPONSE );
        }
      }
    } catch ( Exception e ) {
      response.put( ACTION_STATUS, FAILURE_METHOD_NOT_RESPONSE );
      //getLogChannel().logError( ex.getMessage() );
    }
    return response;
  }

  /**
   * Retrieves the list of available locales and returns them as a JSON object.
   *
   * @param queryParams A map of query parameters (not used in this implementation).
   * @return A JSON object containing the list of locales.
   */
  public JSONObject localesAction() {
    JSONObject response = new JSONObject();
    JSONArray locales = new JSONArray();
    locales.addAll( Arrays.asList( EnvUtil.getLocaleList() ) );
    response.put( "locales", locales );
    return response;
  }

  /**
   * Retrieves the list of available time zones and returns them as a JSON object.
   *
   * @param queryParams A map of query parameters (not used in this implementation).
   * @return A JSON object containing the list of time zones.
   */
  public JSONObject timezonesAction() {
    JSONObject response = new JSONObject();
    JSONArray timezones = new JSONArray();
    timezones.addAll( Arrays.asList( EnvUtil.getTimeZones() ) );
    response.put( "timezones", timezones );
    return response;
  }

  /**
   * Retrieves the list of available character encodings and returns them as a JSON object.
   *
   * @param queryParams A map of query parameters (not used in this implementation).
   * @return A JSON object containing the list of character encodings.
   */
  public JSONObject encodingsAction() {
    JSONObject response = new JSONObject();
    JSONArray encodings = new JSONArray();
    encodings.addAll( Arrays.asList( getCharsets() ) );
    response.put( "encodings", encodings );
    return response;
  }

  /**
   * Retrieves the list of available character sets.
   *
   * @return An array of character set display names.
   */
  public String[] getCharsets() {
    Collection<Charset> charsetCol = Charset.availableCharsets().values();
    String[] charsets = new String[ charsetCol.size() ];
    int i = 0;
    for ( Charset charset : charsetCol ) {
      charsets[ i++ ] = charset.displayName();
    }
    return charsets;
  }

}
