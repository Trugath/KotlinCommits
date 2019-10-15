package com.trugath.kotlincommits.github.api

import org.json.JSONArray

abstract class JsonArrayResponse(json: String) : JSONArray(json), Response