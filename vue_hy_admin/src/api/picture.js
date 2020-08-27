import request from '@/utils/request'

export function getPictureList(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/getList',
    method: 'post',
    data: params
  })
}
export function uploadPic(params) {
  let config = {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  };
  return request({
    url: process.env.PICTURE_API + '/file/uploadPic',
    method: 'post',
    data: params,
    config:config
  })
}
export function addPicture(params) {
  let config = {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  };
  return request({
    url: process.env.ADMIN_API + '/picture/add',
    method: 'post',
    data: params,
    config:config
  })
}

export function editPicture(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/edit',
    method: 'post',
    data: params
  })
}

export function deletePicture(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/delete',
    method: 'post',
    data: params
  })
}

export function setCover(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/setCover',
    method: 'post',
    data: params
  })
}
