/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package grgr.customer.test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    public static Logger LOG = LoggerFactory.getLogger(Activator.class);

    private String sn;
    private IDao dao;
    private Runner runner;
    private ExecutorService pool;

    public IDao getDao() {
        return dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }

    public void start() {
        sn = FrameworkUtil.getBundle(this.getClass()).getSymbolicName();
        pool = Executors.newCachedThreadPool();
        runner = new Runner();
        new Thread(runner).start();
        LOG.info("Bundle blueprint {} started", sn);
    }

    public void stop() {
        runner.stopped = true;
        pool.shutdownNow();
        LOG.info("Bundle blueprint {} stopped", sn);
    }

    private class Runner implements Runnable {

        public volatile boolean stopped = false;

        @Override
        public void run() {
            while (!stopped) {
                try {
                    File f = new File(System.getProperty("karaf.data"), "btest");
                    if (f.isFile()) {
                        List<String> c = Files.readAllLines(f.toPath());
                        int count = Integer.parseInt(c.get(0));

                        for (int i = 0; i < count; i++) {
                            Client.this.pool.submit(new Task(i + 1));
                        }
                        f.delete();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.info("Bundle {}, runner stopped (interrupt)", Client.this.sn);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            LOG.info("Bundle blueprint {}, runner stopped", Client.this.sn);
        }
    }

    private class Task implements Runnable {

        private final int nr;

        public Task(int nr) {
            this.nr = nr;
        }

        @Override
        public void run() {
            try {
                dao.go(nr);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

    }

}
