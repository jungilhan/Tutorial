package com.google.api.client.youtube;

import com.google.api.client.util.Key;

public class Thumbnail {
	  // "default" is a Java keyword, so need to specify the JSON key manually
	  @Key("default")
	  String sqDefault;
	  
	  @Key
	  String hqDefault;
}
