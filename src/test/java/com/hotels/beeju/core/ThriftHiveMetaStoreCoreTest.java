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
package com.hotels.beeju.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.junit.Test;

public class ThriftHiveMetaStoreCoreTest {

  private final BeejuCore core = new BeejuCore();
  private final ThriftHiveMetaStoreCore thriftHiveMetaStoreCore = new ThriftHiveMetaStoreCore(core);

  @Test
  public void before() throws Exception {
    thriftHiveMetaStoreCore.initialise();
    assertThat(core.conf().getVar(HiveConf.ConfVars.METASTOREURIS),
        is(thriftHiveMetaStoreCore.getThriftConnectionUri()));

    HiveConf conf = new HiveConf(this.getClass());
    conf.setVar(HiveConf.ConfVars.METASTOREURIS, thriftHiveMetaStoreCore.getThriftConnectionUri());
    HiveMetaStoreClient client = new HiveMetaStoreClient(conf);
    List<String> databases = client.getAllDatabases();
    assertThat(databases.size(), is(1));
    assertThat(databases.get(0), is("default"));
  }

}
