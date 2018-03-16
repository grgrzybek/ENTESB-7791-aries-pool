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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import grgr.customer.User;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    public static Logger LOG = LoggerFactory.getLogger(Activator.class);

    private ServiceTracker<EntityManagerFactory, EntityManagerFactory> emfTracker = null;
    private ServiceTracker<UserTransaction, UserTransaction> utTracker = null;
    private BundleContext context;
    private String sn;
    private EntityManagerFactory emf;
    private UserTransaction ut;
    private Runner runner;
    private ExecutorService pool;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        emfTracker = new ServiceTracker<>(context, EntityManagerFactory.class, new ServiceTrackerCustomizer<EntityManagerFactory, EntityManagerFactory>() {
            @Override
            public EntityManagerFactory addingService(ServiceReference<EntityManagerFactory> reference) {
                emf = context.getService(reference);
                return emf;
            }

            @Override
            public void modifiedService(ServiceReference<EntityManagerFactory> reference, EntityManagerFactory service) {
                emf = context.getService(reference);
            }

            @Override
            public void removedService(ServiceReference<EntityManagerFactory> reference, EntityManagerFactory service) {
                emf = null;
            }
        });
        emfTracker.open();
        utTracker = new ServiceTracker<>(context, UserTransaction.class, new ServiceTrackerCustomizer<UserTransaction, UserTransaction>() {
            @Override
            public UserTransaction addingService(ServiceReference<UserTransaction> reference) {
                ut = context.getService(reference);
                return ut;
            }

            @Override
            public void modifiedService(ServiceReference<UserTransaction> reference, UserTransaction service) {
                ut = context.getService(reference);
            }

            @Override
            public void removedService(ServiceReference<UserTransaction> reference, UserTransaction service) {
                ut = null;
            }
        });
        utTracker.open();

        sn = context.getBundle().getSymbolicName();

        pool = Executors.newCachedThreadPool();
        runner = new Runner();
        new Thread(runner).start();
        LOG.info("Bundle {} started", context.getBundle().getSymbolicName());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        runner.stopped = true;
        emfTracker.close();
        utTracker.close();

        pool.shutdownNow();
        LOG.info("Bundle {} stopped", context.getBundle().getSymbolicName());
    }

    private class Runner implements Runnable {

        public volatile boolean stopped = false;

        @Override
        public void run() {
            while (!stopped) {
                try {
                    File f = new File(System.getProperty("karaf.data"), "test");
                    if (f.isFile()) {
                        List<String> c = Files.readAllLines(f.toPath());
                        int count = Integer.parseInt(c.get(0));

                        for (int i = 0; i < count; i++) {
                            Activator.this.pool.submit(new Task(emf, ut, i + 1));
                        }
                        f.delete();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.info("Bundle {}, runner stopped (interrupt)", Activator.this.sn);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            LOG.info("Bundle {}, runner stopped", Activator.this.sn);
        }
    }

    private static Random RND = new Random();

    private class Task implements Runnable {

        private final EntityManagerFactory emf;
        private final UserTransaction ut;
        private final int nr;

        public Task(EntityManagerFactory emf, UserTransaction ut, int nr) {
            this.emf = emf;
            this.ut = ut;
            this.nr = nr;
        }

        @Override
        public void run() {
            try {
                Thread.currentThread().setName(String.format("EMF-%02d-thread", nr));
                EntityManager em = emf.createEntityManager();
                ut.begin();

                em.joinTransaction();
                long t1 = System.currentTimeMillis();
                List<User> users = em.createQuery("select u from User u", User.class).getResultList();
                for (User u : users) {
//                    LOG.info(String.format(" - %d: %s", u.getId(), u.getName()));
                    u.getName();
                }
                int sleep = (RND.nextInt(5) + 3) * 100;
                LOG.info(String.format(" -A- %03d: %04dms, (sleep = %03d)", this.nr, System.currentTimeMillis() - t1, sleep));
                Thread.sleep(sleep);

                ut.commit();
                em.close();
            } catch (Exception e) {
                try {
                    ut.setRollbackOnly();
                } catch (SystemException e1) {
                    LOG.error(e1.getMessage());
                }
                LOG.error(e.getMessage());
            }
        }

    }

}
