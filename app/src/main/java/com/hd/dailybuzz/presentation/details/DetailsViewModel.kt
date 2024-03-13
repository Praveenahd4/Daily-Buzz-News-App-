package com.hd.dailybuzz.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd.dailybuzz.domain.model.Article
import com.hd.dailybuzz.domain.usecases.news.DeleteArticle
import com.hd.dailybuzz.domain.usecases.news.GetSavedArticle
import com.hd.dailybuzz.domain.usecases.news.NewsUseCases
import com.hd.dailybuzz.domain.usecases.news.UpsertArticle
import com.hd.dailybuzz.util.UIComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
//    private val getSavedArticleUseCase: GetSavedArticle,
//    private val deleteArticleUseCase: DeleteArticle,
//    private val upsertArticleUseCase: UpsertArticle,
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    var sideEffect by mutableStateOf<UIComponent?>(null)
        private set

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    val article = newsUseCases.getArticle(id = event.article.id)
                    if (article == null){
                        upsertArticle(article = event.article)
                    }else{
                        deleteArticle(article = event.article)
                    }
                }
            }
            is DetailsEvent.RemoveSideEffect ->{
                sideEffect = null
            }
        }
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article = article)
        sideEffect = UIComponent.Toast("Article deleted")
    }

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article = article)
        sideEffect = UIComponent.Toast("Article Inserted")
    }

}