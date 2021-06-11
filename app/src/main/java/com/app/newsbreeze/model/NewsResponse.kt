package com.app.newsbreeze.model


data class ArticlesList(val source:Source ,val author: String?, val title: String, val description: String?,
                        val url: String?, val urlToImage: String?, val publishedAt: String?, val content: String?)

data class NewsResponse(val status: String?, val totalResults: Int?, val articles: List<ArticlesList>?)

data class Source(val name:String)

