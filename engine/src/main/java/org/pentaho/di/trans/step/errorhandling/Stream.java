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


package org.pentaho.di.trans.step.errorhandling;

import org.pentaho.di.core.Const;
import org.pentaho.di.trans.step.StepMeta;

public class Stream implements StreamInterface {

  private String description;
  private StreamType streamType;
  private StepMeta stepMeta;
  private StreamIcon streamIcon;
  private Object subject;

  /**
   * @param streamType
   * @param stepname
   * @param stepMeta
   * @param description
   */
  public Stream( StreamType streamType, StepMeta stepMeta, String description, StreamIcon streamIcon,
    Object subject ) {
    this.streamType = streamType;
    this.stepMeta = stepMeta;
    this.description = description;
    this.streamIcon = streamIcon;
    this.subject = subject;
  }

  public Stream( StreamInterface stream ) {
    this( stream.getStreamType(), stream.getStepMeta(), stream.getDescription(), stream.getStreamIcon(),
      stream.getSubject() );
  }

  public String toString() {
    if ( stepMeta == null ) {
      return "Stream type " + streamType + Const.CR + description;
    } else {
      return "Stream type " + streamType + " for step '" + stepMeta.getName() + "'" + Const.CR + description;
    }
  }

  @Override
  public boolean equals( Object obj ) {
    if ( !( obj instanceof StreamInterface ) ) {
      return false;
    }
    if ( obj == this ) {
      return true;
    }

    StreamInterface stream = (StreamInterface) obj;

    if ( description.equals( stream.getDescription() ) ) {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return description.hashCode();
  }

  public String getStepname() {
    if ( stepMeta == null ) {
      return null;
    }
    return stepMeta.getName();
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription( String description ) {
    this.description = description;
  }

  /**
   * @return the streamType
   */
  public StreamType getStreamType() {
    return streamType;
  }

  /**
   * @param streamType
   *          the streamType to set
   */
  public void setStreamType( StreamType streamType ) {
    this.streamType = streamType;
  }

  /**
   * @return the stepMeta
   */
  public StepMeta getStepMeta() {
    return stepMeta;
  }

  /**
   * @param stepMeta
   *          the stepMeta to set
   */
  public void setStepMeta( StepMeta stepMeta ) {
    this.stepMeta = stepMeta;
  }

  /**
   * @return the streamIcon
   */
  public StreamIcon getStreamIcon() {
    return streamIcon;
  }

  /**
   * @param streamIcon
   *          the streamIcon to set
   */
  public void setStreamIcon( StreamIcon streamIcon ) {
    this.streamIcon = streamIcon;
  }

  /**
   * @return the subject
   */
  public Object getSubject() {
    return subject;
  }

  /**
   * @param subject
   *          the subject to set
   */
  public void setSubject( Object subject ) {
    this.subject = subject;
  }

}
