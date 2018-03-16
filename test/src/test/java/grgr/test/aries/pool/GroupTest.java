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
package grgr.test.aries.pool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GroupTest {

    @Test
    public void group() throws IOException {
        List<String> lines = Files.readAllLines(new File("/home/ggrzybek/Documents/work/2018-02-22 ENTESB-7791 aries pool/fuse.log_TRACE_geronimo_20180223-narrowed").toPath());
        Map<String, int[]> groups = new HashMap<>();
        final int[] c = new int[] { 0 };
        lines.forEach(l -> {
            c[0]++;
            String[] tab = l.split("\\s*\\|\\s*");
            if (tab.length != 2) {
                System.out.println("?: " + l + ": " + c[0]);
            }
            int[] pair = groups.computeIfAbsent(tab[0], k -> new int[] { 0, 0 });
            pair[tab[1].equals("supplying") ? 0 : 1]++;
        });
        groups.forEach((k, v) -> {
            System.out.println(k + "\t" + v[0] + "\t" + v[1]);
        });
        System.out.println(groups.size());
    }

}
