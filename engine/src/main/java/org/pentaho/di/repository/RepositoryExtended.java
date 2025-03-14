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


package org.pentaho.di.repository;

import org.pentaho.di.cluster.ClusterSchema;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.partition.PartitionSchema;

import java.util.List;

/**
 * Additional methods to be added to Repository in next major revision.
 *
 */
@Deprecated
public interface RepositoryExtended extends Repository {
  /**
   * @deprecated more deprecated than the others 
   * Loads the RepositoryDirectoryTree either Eagerly or Lazilly based on the value passed. This value will
   * override the default and any specified setting value for KETTLE_LAZY_REPOSITORY.
   *
   * @param eager
   * @return
   * @throws KettleException
   */
  @Deprecated
  RepositoryDirectoryInterface loadRepositoryDirectoryTree( boolean eager ) throws KettleException;

  /**
   * Loads the RepositoryDirectoryTree, filtering at the server.
   * @param path - relative path  to folder from which we should to download the tree. 
   * Implementation should use "/"  as default path, because it is a root folder of repository
   * @param filter - filter may be a full name or a partial name with one or more wildcard characters ("*"), or a disjunction (using the "|" character to represent logical OR) of these;
   * if null then it should be "*" and return all files and folders from root folder
   * @param depth - 0 fetches just file at path; positive integer n fetches node at path plus n levels of children; 
   * negative integer fetches all children. If n > 0 then only the top level children will be processed
   * @param showHidden - if true we will show hidden files
   * @param includeEmptyFolder include directories without any match
   * @param includeAcls include ACLs 
   * @throws KettleException
   */
  RepositoryDirectoryInterface loadRepositoryDirectoryTree(
      String path,
      String filter,
      int depth,
      boolean showHidden,
      boolean includeEmptyFolder,
      boolean includeAcls )
    throws KettleException;

  /**
   * Move / rename a repository directory
   *
   * @param dirId
   *          The ObjectId of the repository directory to move
   * @param newParent
   *          The RepositoryDirectoryInterface that will be the new parent of the repository directory (May be null if a
   *          move is not desired)
   * @param newName
   *          The new name of the repository directory (May be null if a rename is not desired)
   * @param renameHomeDirectories
   *          true if this is an allowed action
   * @return The ObjectId of the repository directory that was moved
   * @throws KettleException
   */
  ObjectId renameRepositoryDirectory( final ObjectId dirId, final RepositoryDirectoryInterface newParent,
                                             final String newName, final boolean renameHomeDirectories ) throws KettleException;


  /**
   * Delete a repository directory
   *
   * @param dir
   *          The ObjectId of the repository directory to move
   * @param deleteHomeDirectories
   *          true if this is an allowed action
   * @throws KettleException
   */
  void deleteRepositoryDirectory( final RepositoryDirectoryInterface dir, final boolean deleteHomeDirectories )
          throws KettleException;

  List<RepositoryObjectInterface> getChildren( String path, String filter );

  List<DatabaseMeta> getConnections( boolean cached ) throws KettleException;

  List<SlaveServer> getSlaveServers( boolean cached ) throws KettleException;

  List<PartitionSchema> getPartitions( boolean cached ) throws KettleException;

  List<ClusterSchema> getClusters( boolean cached ) throws KettleException;
}
