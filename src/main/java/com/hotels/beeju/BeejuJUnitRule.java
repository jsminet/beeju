/**
 * Copyright (C) 2015-2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.beeju;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.thrift.TException;
import org.junit.rules.ExternalResource;

import com.hotels.beeju.core.BeejuCore;

/**
 * Base class for BeeJU JUnit Rules that require a Hive Metastore database configuration pre-set.
 */
abstract class BeejuJUnitRule extends ExternalResource {

  protected BeejuCore core;

  BeejuJUnitRule(String databaseName, Map<String, String> configuration) {
    core = new BeejuCore(databaseName, configuration);
  }

  @Override
  protected void before() throws Throwable {
    createDatabase(databaseName());
  }

  @Override
  protected void after() {
    try {
      core.cleanUp();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * @return {@link com.hotels.beeju.core.BeejuCore#driverClassName()}.
   */
  public String driverClassName() {
    return core.driverClassName();
  }

  /**
   * @return {@link com.hotels.beeju.core.BeejuCore#databaseName()}.
   */
  public String databaseName() {
    return core.databaseName();
  }

  /**
   * @return {@link com.hotels.beeju.core.BeejuCore#connectionURL()}.
   */
  public String connectionURL() {
    return core.connectionURL();
  }

  /**
   * @return {@link com.hotels.beeju.core.BeejuCore#conf()}.
   */
  public HiveConf conf() {
    return core.conf();
  }

  /**
   * @return {@link com.hotels.beeju.core.BeejuCore#newClient()}.
   */
  public HiveMetaStoreClient newClient() {
    return core.newClient();
  }

  /**
   * @return Root of temporary directory
   */
  File tempDir() {
    return core.tempDir().toFile();
  }

  /**
   * Create a new database with the specified name.
   *
   * @param databaseName Database name.
   * @throws TException If an error occurs creating the database.
   */
  void createDatabase(String databaseName) throws TException {
    core.createDatabase(databaseName);
  }
}
