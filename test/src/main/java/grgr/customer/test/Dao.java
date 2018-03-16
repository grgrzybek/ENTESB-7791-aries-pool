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

import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;

import grgr.customer.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dao implements IDao {

    public static Logger LOG = LoggerFactory.getLogger(Activator.class);

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    private static Random RND = new Random();

    @Override
    public void go(int nr) throws Exception {
        Thread.currentThread().setName(String.format("EMF-%02d-thread", nr));
        long t1 = System.currentTimeMillis();
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        for (User u : users) {
//            LOG.info(String.format(" - %d: %s", u.getId(), u.getName()));
            u.getName();
        }
        int sleep = (RND.nextInt(5) + 3) * 100;
        LOG.info(String.format(" -B- %03d: %04dms, (sleep = %03d)", nr, System.currentTimeMillis() - t1, sleep));
        Thread.sleep(sleep);
    }

}
