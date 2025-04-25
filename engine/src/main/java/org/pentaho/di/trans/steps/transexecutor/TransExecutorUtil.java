package org.pentaho.di.trans.steps.transexecutor;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepUtilInterface;

public class TransExecutorUtil implements StepUtilInterface {

  private final TransExecutorMeta transExecutorMeta;

  public TransExecutorUtil( TransExecutorMeta transExecutorMeta ) {
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

  private JSONObject parametersAction( TransMeta transMeta, TransExecutorMeta transExecutorMeta )
    throws KettleException {
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
      response.put( StepInterface.ACTION_STATUS, StepInterface.FAILURE_RESPONSE );
    }
    return response;
  }

  TransMeta loadExecutorTransMeta( TransMeta transMeta, TransExecutorMeta transExecutorMeta ) throws KettleException {
    return TransExecutorMeta.loadMappingMeta( transExecutorMeta, transExecutorMeta.getRepository(),
      transExecutorMeta.getMetaStore(), transMeta,
      transExecutorMeta.getParameters().isInheritingAllVariables() );
  }
}
