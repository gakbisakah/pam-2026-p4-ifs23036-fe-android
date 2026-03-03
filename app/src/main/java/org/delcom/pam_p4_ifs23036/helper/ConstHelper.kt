package org.delcom.pam_p4_ifs23036.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),

        Movies(path = "movies"),
        MoviesAdd(path = "movies/add"),
        MoviesDetail(path = "movies/{movieId}"),
        MoviesEdit(path = "movies/{movieId}/edit")
    }
}
