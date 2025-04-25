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


package org.pentaho.di.trans.step;

import java.util.Map;

import org.json.simple.JSONObject;
import org.pentaho.di.trans.TransMeta;

public interface StepUtilInterface {

  String ACTION_STATUS = "actionStatus";
  String SUCCESS_RESPONSE = "Action successful";
  String FAILURE_RESPONSE = "Action failed";
  String FAILURE_METHOD_NOT_RESPONSE = "Action failed with method not found";
  String STATUS = "Status";
  int SUCCESS_STATUS = 1;
  int FAILURE_STATUS = -1;
  int NOT_EXECUTED_STATUS = 0;

  default JSONObject doAction( String fieldName, TransMeta transMeta,
                               Map<String, String> queryParams ) {
    JSONObject response = new JSONObject();
    response.put( ACTION_STATUS, FAILURE_METHOD_NOT_RESPONSE );
    return response;
  }


  default boolean isFailedResponse( JSONObject response ) {
    if ( response != null && response.get( StepInterface.ACTION_STATUS ) != null ) {
      return StepInterface.FAILURE_RESPONSE.equalsIgnoreCase( (String) response.get( StepInterface.ACTION_STATUS ) );
    }

    return false;
  }
}
