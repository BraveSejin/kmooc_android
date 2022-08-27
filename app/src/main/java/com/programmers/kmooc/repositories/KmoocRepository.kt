package com.programmers.kmooc.repositories

import android.util.Log
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.network.HttpClient
import com.programmers.kmooc.utils.DateUtil
import org.json.JSONArray
import org.json.JSONObject

class KmoocRepository {

    /**
     * 국가평생교육진흥원_K-MOOC_강좌정보API
     * https://www.data.go.kr/data/15042355/openapi.do
     */

    private val httpClient = HttpClient("http://apis.data.go.kr/B552881/kmooc")
    private val serviceKey =
        "LwG%2BoHC0C5JRfLyvNtKkR94KYuT2QYNXOT5ONKk65iVxzMXLHF7SMWcuDqKMnT%2BfSMP61nqqh6Nj7cloXRQXLA%3D%3D"

    fun list(completed: (LectureList) -> Unit) {
        httpClient.getJson(
            "/courseList",
            mapOf("serviceKey" to serviceKey, "Mobile" to 1)
        ) { result ->
            result.onSuccess {
                completed(parseLectureList(JSONObject(it)))
            }
        }
    }

    fun next(currentPage: LectureList, completed: (LectureList) -> Unit) {
        val nextPageUrl = currentPage.next
        httpClient.getJson(nextPageUrl, emptyMap()) { result ->
            result.onSuccess {
                completed(parseLectureList(JSONObject(it)))
            }
        }
    }

    fun detail(courseId: String, completed: (Lecture) -> Unit) {
        httpClient.getJson(
            "/courseDetail",
            mapOf("CourseId" to courseId, "serviceKey" to serviceKey)
        ) { result ->
            result.onSuccess {
                completed(parseLecture(JSONObject(it)))
            }
        }
    }

    private fun parseLectureList(jsonObject: JSONObject): LectureList {
        val pagination = jsonObject.getJSONObject("pagination")

        val count = pagination.getInt("count")
        val numPages = pagination.getInt("num_pages")
        val previous = pagination.getString("previous")
        val next = pagination.getString("next")

        val results: JSONArray = jsonObject.getJSONArray("results")
        val list = mutableListOf<Lecture>()
        for (i in 0 until results.length()) {
            list.add(parseLecture(results.getJSONObject(i)))
        }
        return LectureList(
            count = count,
            numPages = numPages,
            previous = previous,
            next = next,
            lectures = list
        )
    }

    private fun parseLecture(jsonObject: JSONObject): Lecture {
        val id = jsonObject.getString("id")
        val number = jsonObject.getString("number")
        val name = jsonObject.getString("name")
        val classfyName = jsonObject.getString("classfy_name")
        val middleClassfyName = jsonObject.getString("middle_classfy_name")

        val image = jsonObject.getJSONObject("media").getJSONObject(("image"))
        val courseImage = image.getString("small")
        val courseImageLarge = image.getString("large")


        val shortDescription = jsonObject.getString("short_description")
        val orgName = jsonObject.getString("org_name")
        val start = DateUtil.parseDate(jsonObject.getString("start"))
        val end = DateUtil.parseDate(jsonObject.getString("end"))
        val teachers = jsonObject.optString("teachers", "no_teacher")
        val overview = jsonObject.optString("overview", "no_overview")


        return Lecture(
            id,
            number,
            name,
            classfyName,
            middleClassfyName,
            courseImage,
            courseImageLarge,
            shortDescription,
            orgName,
            start,
            end,
            teachers,
            overview
        )
    }
}