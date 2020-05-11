/*
 * MIT License
 *
 * Copyright (c) 2019 SmashKs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package taiwan.no.one.featDummy.data.local.services.database.v1

import androidx.room.Dao
import androidx.room.Query
import taiwan.no.one.core.data.local.room.BaseDao
import taiwan.no.one.featDummy.data.local.entities.DummyEntity

/**
 * Integrated the base [androidx.room.Room] database operations.
 * Thru [androidx.room.Room] we can just define the interfaces that we want to
 * access for from a local database.
 * Using prefix name (get), (insert), (update), (delete)
 */
@Dao
internal abstract class DummyDao : BaseDao<DummyEntity> {
    @Query("""SELECT * FROM table_dummy""")
    abstract suspend fun getDummies(): List<DummyEntity>
}
