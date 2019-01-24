package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.view.View

sealed class UIState {

    object HomeScreenState: UIState()
    object LibraryScreenState: UIState()
    object InTheatresScreenState: UIState()
    object SearchScreenState: UIState()
    data class DetailsScreenState(val movieId: Int = -1,
                                  val transitionName: String? = null,
                                  val sharedView: View? = null): UIState()
    sealed class AccountScreenState: UIState() {
        object AuthenticatedScreenState: AccountScreenState()
        object UnauthenticatedScreenState: AccountScreenState()
    }
}

sealed class MovieItemType(val name: String) {
    sealed class MovieType(name: String): MovieItemType(name) {
        object Popular: MovieType("popular")
        object TopRated: MovieType("top-rated")
        object SearchResult: MovieType("search-result")
    }
    sealed class LibraryType(name: String): MovieItemType(name) {
        object Favourite: LibraryType("favourite")
        object Watchlisted: LibraryType("watchlisted")
    }
}