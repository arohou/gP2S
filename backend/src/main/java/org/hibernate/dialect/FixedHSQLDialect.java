/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibernate.dialect;
/**
 * 'Fixed' HSQL Dialect.
 *
 * PEARL: HSQL seems to have a problem with floats.  We remedy this here.
 * See https://stackoverflow.com/q/28480714/773113
 *
 * PEARL: this class must be public, not package-private, and it must have a 
 * public constructor, otherwise hibernate won't be able to instantiate it.
 */
@SuppressWarnings("unused")
public class FixedHSQLDialect extends HSQLDialect
{
    public FixedHSQLDialect()
    {
        registerColumnType( java.sql.Types.FLOAT, "double" );
    }
}
