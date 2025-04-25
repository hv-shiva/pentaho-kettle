package org.pentaho.di.trans.steps.selectvalues;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepUtilInterface;

public class SelectValuesUtil implements StepUtilInterface {

  @Override
  public JSONObject doAction( String fieldName, TransMeta transMeta,
                              Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    try {
      switch ( fieldName ) {
        case "locales":
          response = localesAction( queryParams );
          break;
        case "timezones":
          response = timezonesAction( queryParams );
          break;
        case "encodings":
          response = encodingsAction( queryParams );
          break;
        default:
          response.put( StepInterface.ACTION_STATUS, StepInterface.FAILURE_METHOD_NOT_RESPONSE );
          break;
      }

      if ( isFailedResponse( response ) ) {
        response.put( StepInterface.ACTION_STATUS, StepInterface.FAILURE_RESPONSE );
      } else if ( response != null ) {
        response.put( StepInterface.ACTION_STATUS, StepInterface.SUCCESS_RESPONSE );
      }
    } catch ( Exception e ) {
      response.put( StepInterface.ACTION_STATUS, StepInterface.FAILURE_METHOD_NOT_RESPONSE );
      //getLogChannel().logError( ex.getMessage() );
    }
    return response;
  }

  public JSONObject localesAction( Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    JSONArray locales = new JSONArray();
    locales.addAll( Arrays.asList( EnvUtil.getLocaleList() ) );
    response.put( "locales", locales );
    return response;
  }

  public JSONObject timezonesAction( Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    JSONArray timezones = new JSONArray();
    timezones.addAll( Arrays.asList( EnvUtil.getTimeZones() ) );
    response.put( "timezones", timezones );
    return response;
  }

  public JSONObject encodingsAction( Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    JSONArray encodings = new JSONArray();
    encodings.addAll( Arrays.asList( getCharsets() ) );
    response.put( "encodings", encodings );
    return response;
  }

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
