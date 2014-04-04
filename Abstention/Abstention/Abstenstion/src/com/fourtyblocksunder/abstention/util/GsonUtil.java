package com.fourtyblocksunder.abstention.util;

import com.google.gson.Gson;

public class GsonUtil {
	
	/**
	 * Our main Gson instance to read / write JSON data.
	 */
	private static Gson gson = null;

	/**
	 * Returns the global Gson instance.
	 * 
	 * @return
	 */
	public static Gson getGson() {
		return gson;
	}
	
	public static void setGson(Gson g)
	{
		gson = g;
	}

}
