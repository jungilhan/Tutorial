/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.youtube;

import android.util.Log;

/**
 * @author Yaniv Inbar
 */
class View {
	private static final String TAG = "Youtube.View";
	
	static void header(String name) {
		Log.d(TAG, "============== " + name + " ==============");
	}

	static void display(Feed<? extends Item> feed) {
		Log.d(TAG, "Showing first " + feed.items.size() + " of "
				+ feed.totalItems + " videos: ");
		for (Item item : feed.items) {
			Log.d(TAG,"-----------------------------------------------");
			display(item);
		}
	}

	static void display(Item item) {
		Log.d(TAG, "Title: " + item.title);
		Log.d(TAG, "Updated: " + item.updated);
		if (item instanceof Video) {
			Video video = (Video) item;
			if (video.description != null) {
				Log.d(TAG, "Description: " + video.description);
			}
			if (!video.tags.isEmpty()) {
				Log.d(TAG, "Tags: " + video.tags);
			}
			if (video.player != null) {
				Log.d(TAG, "Play URL: " + video.player.defaultUrl);
			}
			if (video.thumbnail != null) {
				Log.d(TAG, "SQ thumbnail URL: " + video.thumbnail.sqDefault);
				Log.d(TAG, "HQ thumbnail URL: " + video.thumbnail.hqDefault);
			}
			if (video.likeCount != null) {
				Log.d(TAG, "Like count: " + video.likeCount);
			}
			if (video.viewCount != null) {
				Log.d(TAG, "View count: " + video.viewCount);
			}
		}
	}
}
