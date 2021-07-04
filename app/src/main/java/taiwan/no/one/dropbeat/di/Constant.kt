/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
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

package taiwan.no.one.dropbeat.di

object Constant {
    const val TAG_FEAT_REPO_SHARED_PREFS = "repo shared preferences"

    const val TAG_FEAT_SEARCH_RETROFIT = "retrofit search"
    const val TAG_FEAT_RANKING_RETROFIT = "retrofit ranking"
    const val TAG_FEAT_EXPLORE_RETROFIT = "retrofit explore"
    const val TAG_FEAT_LIBRARY_RETROFIT = "retrofit library"

    const val TAG_WORKER_INIT_DATA = "worker for initializing"
    const val TAG_WORKER_ADD_SONG_TO_DB = "worker for adding a song to the database"
    const val TAG_WORKER_ADD_SONG_TO_PLAYLIST = "worker for adding a song to a playlist"
    const val TAG_WORKER_GET_SONGS_OF_TAG = "worker for getting songs of tag"

    const val TAG_EDGE_FACTORY_NONE = "recyclerview none edge effect"
    const val TAG_EDGE_FACTORY_BOUNCED = "recyclerview bounced edge effect"
}
