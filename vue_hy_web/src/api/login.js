import request from '@/utils/request'
let config = {
  headers: {
    "Content-Type": "multipart/form-data",
  },
};
export function Login (params) {
  return request({
    url: process.env.WEB_API  + '/login/login',
    method: 'post',
    params,
    config
  })
}

export function Register (params) {
    return request({
      url: process.env.WEB_API  + '/login/register',
      method: 'post',
      params,
      config
    })
  }

  export function logOut (params) {
    return request({
      url: process.env.WEB_API  + '/login/logout',
      method: 'post',
      params
    })
  }
