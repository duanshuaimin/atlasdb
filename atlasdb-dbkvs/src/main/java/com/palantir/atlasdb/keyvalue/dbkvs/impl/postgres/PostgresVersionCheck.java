/*
 * Copyright 2017 Palantir Technologies, Inc. All rights reserved.
 *
 * Licensed under the BSD-3 License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.atlasdb.keyvalue.dbkvs.impl.postgres;

import org.slf4j.Logger;

import com.palantir.util.VersionStrings;

public final class PostgresVersionCheck {
    private static final String MIN_POSTGRES_VERSION = "9.2";

    private PostgresVersionCheck() {}

    public static void checkDatabaseVersion(String version, Logger log) {
        if (!version.matches("^[\\.0-9]+$") || VersionStrings.compareVersions(version, MIN_POSTGRES_VERSION) < 0) {
            log.error("Your key value service currently uses version {} of postgres."
                    + " The minimum supported version is {}."
                    + " If you absolutely need to use an older version of postgres,"
                    + " please contact Palantir support for assistance.", version, MIN_POSTGRES_VERSION);
        } else if (VersionStrings.compareVersions(version, "9.5") >= 0
                && VersionStrings.compareVersions(version, "9.5.2") < 0) {
            throw new DbkvsVersionException(
                    "You are running Postgres " + version + ". Versions 9.5.0 and 9.5.1 contain a known bug "
                            + "that causes incorrect results to be returned for certain queries. "
                            + "Please update your Postgres distribution.");
        }
    }
}
