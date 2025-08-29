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


package org.pentaho.di.trans.steps.transexecutor;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepHelperInterface;
import org.pentaho.di.trans.step.StepInterface;

public class TransExecutorHelper implements StepHelperInterface {

  private final TransExecutorMeta transExecutorMeta;

  public TransExecutorHelper( TransExecutorMeta transExecutorMeta ) {
    this.transExecutorMeta = transExecutorMeta;
  }

  @Override
  public JSONObject doAction( String fieldName, TransMeta transMeta,
                              Map<String, String> queryParams ) {
    transExecutorMeta.setRepository( transMeta.getRepository() );
    JSONObject response = new JSONObject();
    try {
      switch ( fieldName ) {
        case "parameters":
          response = parametersAction( transMeta, transExecutorMeta );
          break;
        case "isTransValid":
          response = isTransValidAction( transMeta, transExecutorMeta );
          break;
        default:
          response.put( StepInterface.ACTION_STATUS, FAILURE_METHOD_NOT_RESPONSE );
          break;
      }

      response.put( ACTION_STATUS,
        isFailedResponse( response )
          ? FAILURE_RESPONSE
          : SUCCESS_RESPONSE );
    } catch ( Exception e ) {
      response.put( ACTION_STATUS, FAILURE_METHOD_NOT_RESPONSE );
      //getLogChannel().logError( ex.getMessage() );
    }
    return response;
  }

  /**
   * Retrieves the parameters of the transformation and returns them as a JSON object.
   *
   * @param transMeta         The metadata of the current transformation.
   * @param transExecutorMeta The metadata of the transformation executor step.
   * @return A JSON object containing:
   * - "parameters": A JSON array where each element is a JSON object representing a parameter with:
   * - "variable": The name of the parameter.
   * - "field": An empty string (reserved for future use).
   * - "input": The default value or description of the parameter.
   * @throws KettleException If an error occurs while loading the transformation metadata.
   */
  private JSONObject parametersAction( TransMeta transMeta, TransExecutorMeta transExecutorMeta ) {
    JSONObject response = new JSONObject();
    JSONArray parameterArray = new JSONArray();
    try {
      TransMeta inputTransMeta = loadExecutorTransMeta( transMeta, transExecutorMeta );
      if ( inputTransMeta != null ) {
        String[] parameters = inputTransMeta.listParameters();
        for ( int i = 0; i < parameters.length; i++ ) {
          JSONObject parameter = new JSONObject();
          String name = parameters[ i ];
          String desc = inputTransMeta.getParameterDescription( name );
          String str = inputTransMeta.getParameterDefault( name );
          parameter.put( "variable", Const.NVL( name, "" ) );
          parameter.put( "field", "" );
          parameter.put( "input", Const.NVL( str, Const.NVL( desc, "" ) ) );
          parameterArray.add( parameter );
        }
      }
      response.put( "parameters", parameterArray );
    } catch ( Exception e ) {
      //log.logError( e.getMessage() );
      response.put( ACTION_STATUS, FAILURE_RESPONSE );
    }
    return response;
  }

  /**
   * Validates the presence of a transformation by attempting to load its metadata.
   * This method is invoked dynamically using reflection from StepInterface#doAction method.
   *
   * @param queryParams A map of query parameters (not used in this implementation).
   * @return A JSON object containing:
   * - "transPresent": A string ("true" or "false") indicating whether the transformation metadata was successfully
   * loaded.
   * - "errorMessage": An error message if the transformation metadata could not be loaded.
   * @throws KettleException If an error occurs while loading the transformation metadata.
   */
  private JSONObject isTransValidAction( TransMeta transMeta, TransExecutorMeta transExecutorMeta ) {
    JSONObject response = new JSONObject();
    try {
      loadExecutorTransMeta( transMeta, transExecutorMeta );
      response.put( "transPresent", true );
    } catch ( Exception e ) {
      response.put( "transPresent", false );
      response.put( "errorMessage", ExceptionUtils.getRootCauseMessage( e ) );
      //log.logError( e.getMessage() );
    }
    return response;
  }

  TransMeta loadExecutorTransMeta( TransMeta transMeta, TransExecutorMeta transExecutorMeta ) throws KettleException {
    return TransExecutorMeta.loadMappingMeta( transExecutorMeta, transExecutorMeta.getRepository(),
      transExecutorMeta.getMetaStore(), transMeta,
      transExecutorMeta.getParameters().isInheritingAllVariables() );
  }
}
