package com.trugath.kotlincommits.github.api

import org.json.JSONObject

abstract class JsonObjectResponse(json: String) : JSONObject(json), Response