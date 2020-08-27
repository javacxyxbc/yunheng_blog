import request from '@/utils/request'
let config = {
  headers: {
    "Content-Type": "multipart/form-data",
  },
};


export function priseArticleById (params) {
  return request({
    url: process.env.WEB_API  + '/articlePage/priseBlogByUid',
    method: 'post',
    params,
    config
  })
}

export function getArticleById (params) {
  return request({
    url: process.env.WEB_API  + '/articlePage/get',
    method: 'get',
    params
  })
}

export function getRecommendBlogList (params) {
  return request({
    url: process.env.WEB_API  + '/articlePage/RecommendArticles',
    method: 'get',
    params
  })
}

export function getRecommendSorts () {
  return request({
    url: process.env.WEB_API  + '/articlePage/RecommendSorts',
    method: 'get'
  })
}
