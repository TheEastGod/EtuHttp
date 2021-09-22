package com.zxd.etuhttp.wrapper.param

/**
 * author: zxd
 * created on: 2021/1/29 15:49
 * description:
 */
interface Param<P : Param<P>> : IParam<P>,IHeaders<P>,IRequest{

  companion object{
      @JvmField
      val DATA_DECRYPT: String = "data-decrypt"

      @JvmStatic
      fun get(url: String): NoBodyParam{
         return NoBodyParam(url,Method.GET)
      }

      @JvmStatic
      fun head(url: String): NoBodyParam{
          return NoBodyParam(url,Method.HEAD)
      }

      @JvmStatic
      fun postBody(url: String): BodyParam{
          return BodyParam(url,Method.POST)
      }

      @JvmStatic
      fun putBody(url: String): BodyParam{
          return BodyParam(url,Method.PUT)
      }

      @JvmStatic
      fun patchBody(url: String): BodyParam{
          return BodyParam(url,Method.PATCH)
      }

      @JvmStatic
      fun deleteBody(url: String): BodyParam{
          return BodyParam(url,Method.DELETE)
      }

      /**
       * post请求
       * 参数以{application/x-www-form-urlencoded}形式提交
       * 当带有文件时，自动以{multipart/form-data}形式提交
       * 当调用{@link FormParam#setMultiForm()}方法，强制以{multipart/form-data}形式提交
       */
      @JvmStatic
      fun postForm(url: String): FormParam{
          return FormParam(url,Method.POST)
      }

      /**
       * put请求
       * 参数以{application/x-www-form-urlencoded}形式提交
       * 当带有文件时，自动以{multipart/form-data}形式提交
       * 当调用{@link FormParam#setMultiForm()}方法，强制以{multipart/form-data}形式提交
       *
       * @param url url
       * @return FormParam
       */
      @JvmStatic
      fun putForm(url: String): FormParam{
          return FormParam(url,Method.PUT)
      }

      /**
       * delete 请求
       * 参数以{application/x-www-form-urlencoded}形式提交
       * 当带有文件时，自动以{multipart/form-data}形式提交
       * 当调用{@link FormParam#setMultiForm()}方法，强制以{multipart/form-data}形式提交
       *
       * @param url url
       * @return FormParam
       */
      @JvmStatic
      fun deleteForm(url: String): FormParam{
          return FormParam(url,Method.DELETE)
      }

      /**
       * post请求,参数以{application/json; charset=utf-8}形式提交,提交Json对象
       *
       * @param url url
       * @return JsonParam
       */
      @JvmStatic
      fun postJson(url: String): JsonParam{
          return JsonParam(url,Method.POST)
      }

      /**
       * PUT,参数以{application/json; charset=utf-8}形式提交,提交Json对象
       *
       * @param url url
       * @return JsonParam
       */
      @JvmStatic
      fun putJson(url: String): JsonParam{
          return JsonParam(url,Method.PUT)
      }

      /**
       * patch请求,参数以{application/json; charset=utf-8}形式提交,提交Json对象
       *
       * @param url url
       * @return JsonParam
       */
      @JvmStatic
      fun patchJson(url: String): JsonParam{
          return JsonParam(url,Method.PATCH)
      }
      /**
       * delete请求,参数以{application/json; charset=utf-8}形式提交,提交Json对象
       *
       * @param url url
       * @return JsonParam
       */
      @JvmStatic
      fun deleteJson(url: String): JsonParam{
          return JsonParam(url,Method.DELETE)
      }


      /**
       * post请求,参数以{application/json; charset=utf-8}形式提交,提交Json数组
       *
       * @param url url
       * @return JsonArrayParam
       */
      @JvmStatic
      fun postJsonArray(url: String): JsonArrayParam {
          return JsonArrayParam(url, Method.POST)
      }


      /**
       * put请求,参数以{application/json; charset=utf-8}形式提交,提交Json数组
       *
       * @param url url
       * @return JsonArrayParam
       */
      @JvmStatic
      fun putJsonArray(url: String): JsonArrayParam {
          return JsonArrayParam(url, Method.PUT)
      }

      /**
       * patch请求,参数以{application/json; charset=utf-8}形式提交,提交Json数组
       *
       * @param url url
       * @return JsonArrayParam
       */
      @JvmStatic
      fun patchJsonArray(url: String): JsonArrayParam {
          return JsonArrayParam(url, Method.PATCH)
      }

      /**
       * delete请求,参数以{application/json; charset=utf-8}形式提交,提交Json数组
       *
       * @param url url
       * @return JsonArrayParam
       */
      @JvmStatic
      fun deleteJsonArray(url: String): JsonArrayParam {
          return JsonArrayParam(url, Method.DELETE)
      }

  }

}