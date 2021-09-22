package com.zxd.etuhttp_compiler

import java.io.BufferedWriter
import java.io.IOException
import javax.annotation.processing.Filer

/**
 * author: zxd
 * created on: 2021/7/9 15:48
 * description:
 */
object ClassHelper {


    @JvmStatic
    fun generatorStaticClass(filer: Filer,isAndroid: Boolean){
        generatorBaseEtuHttp(filer, isAndroid)
        generatorEtuHttpAbstractBodyParam(filer)
        generatorEtuHttpBodyParam(filer)
        generatorEtuHttpFormParam(filer)
        generatorEtuHttpNoBodyParam(filer)
        generatorEtuHttpJsonParam(filer)
        generatorEtuHttpJsonArrayParam(filer)
    }

    private fun generatorEtuHttpJsonArrayParam(filer: Filer) {
        generatorClass(
            filer, "EtuHttpJsonArrayParam", """
                package $etuHttpPackage;

                import com.google.gson.JsonArray;
                import com.google.gson.JsonObject;
                import com.zxd.etuhttp.wrapper.param.JsonArrayParam;

                import java.util.List;
                import java.util.Map;

                /**
                 * author: zxd
                 * created on: 2021/7/28 14:15
                 * description:
                 */
                public class EtuHttpJsonArrayParam extends EtuHttpAbstractBodyParam<JsonArrayParam,EtuHttpJsonArrayParam>{

                    public EtuHttpJsonArrayParam(JsonArrayParam param) {
                        super(param);
                    }

                    public EtuHttpJsonArrayParam add(String key,Object value){
                        param.add(key, value);
                        return this;
                    }

                    public EtuHttpJsonArrayParam add(String key, Object value, boolean isAdd) {
                        if(isAdd) {
                            param.add(key,value);
                        }
                        return this;
                    }

                    public EtuHttpJsonArrayParam addAll(Map<String, ?> map) {
                        param.addAll(map);
                        return this;
                    }

                    public EtuHttpJsonArrayParam add(Object object) {
                        param.add(object);
                        return this;
                    }

                    public EtuHttpJsonArrayParam addAll(List<?> list) {
                        param.addAll(list);
                        return this;
                    }

                    /**
                     * 添加多个对象，将字符串转JsonElement对象,并根据不同类型,执行不同操作,可输入任意非空字符串
                     */
                    public EtuHttpJsonArrayParam addAll(String jsonElement) {
                        param.addAll(jsonElement);
                        return this;
                    }

                    public EtuHttpJsonArrayParam addAll(JsonArray jsonArray) {
                        param.addAll(jsonArray);
                        return this;
                    }

                    /**
                     * 将Json对象里面的key-value逐一取出，添加到Json数组中，成为单独的对象
                     */
                    public EtuHttpJsonArrayParam addAll(JsonObject jsonObject) {
                        param.addAll(jsonObject);
                        return this;
                    }

                    public EtuHttpJsonArrayParam addJsonElement(String jsonElement) {
                        param.addJsonElement(jsonElement);
                        return this;
                    }

                    /**
                     * 添加一个JsonElement对象(Json对象、json数组等)
                     */
                    public EtuHttpJsonArrayParam addJsonElement(String key, String jsonElement) {
                        param.addJsonElement(key, jsonElement);
                        return this;
                    }

                }

            """.trimIndent()
        )
    }

    private fun generatorEtuHttpJsonParam(filer: Filer) {
        generatorClass(
            filer, "EtuHttpJsonParam", """
            package $etuHttpPackage;
          
            
            import com.google.gson.JsonObject;
            import com.zxd.etuhttp.wrapper.param.JsonParam;
            
            import java.util.Map;
            
            /**
             * author: zxd
             * created on: 2021/7/28 14:29
             * description:
             */
            public class EtuHttpJsonParam extends EtuHttpAbstractBodyParam<JsonParam,EtuHttpJsonParam>{
            
                public EtuHttpJsonParam(JsonParam param) {
                    super(param);
                }
            
                public EtuHttpJsonParam add(String key, Object value) {
                    param.add(key,value);
                    return this;
                }
            
                public EtuHttpJsonParam add(String key, Object value, boolean isAdd) {
                    if(isAdd) {
                        param.add(key,value);
                    }
                    return this;
                }
            
                public EtuHttpJsonParam addAll(Map<String, ?> map) {
                    param.addAll(map);
                    return this;
                }
            
                /**
                 * 将Json对象里面的key-value逐一取出，添加到另一个Json对象中，
                 * 输入非Json对象将抛出{@link IllegalStateException}异常
                 */
                public EtuHttpJsonParam addAll(String jsonObject) {
                    param.addAll(jsonObject);
                    return this;
                }
            
                /**
                 * 将Json对象里面的key-value逐一取出，添加到另一个Json对象中
                 */
                public EtuHttpJsonParam addAll(JsonObject jsonObject) {
                    param.addAll(jsonObject);
                    return this;
                }
            
                /**
                 * 添加一个JsonElement对象(Json对象、json数组等)
                 */
                public EtuHttpJsonParam addJsonElement(String key, String jsonElement) {
                    param.addJsonElement(key, jsonElement);
                    return this;
                }
            
            }
               
            """.trimIndent()
        )
    }

    private fun generatorEtuHttpNoBodyParam(filer: Filer) {
        generatorClass(
            filer, "EtuHttpNoBodyParam", """
                
        package $etuHttpPackage;
        import com.zxd.etuhttp.wrapper.annotations.NonNull;
        import com.zxd.etuhttp.wrapper.param.NoBodyParam;
        
        import java.util.Map;
        
        /**
         * author: zxd
         * created on: 2021/7/28 13:55
         * description:
         */
        public class EtuHttpNoBodyParam extends EtuHttp<NoBodyParam,EtuHttpNoBodyParam> {
        
            public EtuHttpNoBodyParam(NoBodyParam param) {
                super(param);
            }
        
            public EtuHttpNoBodyParam add(String key, Object value) {
                return addQuery(key, value);
            }
        
            public EtuHttpNoBodyParam add(String key, Object value, boolean isAdd) {
                if (isAdd) {
                    addQuery(key, value);
                }
                return this;
            }
        
            public EtuHttpNoBodyParam addAll(Map<String, ?> map) {
                return addAllQuery(map);
            }
        
            public EtuHttpNoBodyParam addEncoded(String key, Object value) {
                return addEncodedQuery(key, value);
            }
        
            public EtuHttpNoBodyParam addAllEncoded(@NonNull Map<String, ?> map) {
                return addAllEncodedQuery(map);
            }
        
            public EtuHttpNoBodyParam set(String key, Object value) {
                return setQuery(key, value);
            }
        
            public EtuHttpNoBodyParam setEncoded(String key, Object value) {
                return setEncodedQuery(key, value);
            }
                    
          """.trimIndent()
        )
    }

    private fun generatorEtuHttpFormParam(filer: Filer) {
        generatorClass(filer,"EtuHttpFormParam","""
            package $etuHttpPackage;
            
            import android.content.Context;
            import android.net.Uri;

            import com.zxd.etuhttp.wrapper.annotations.NonNull;
            import com.zxd.etuhttp.wrapper.annotations.Nullable;
            import com.zxd.etuhttp.wrapper.entity.UpFile;
            import com.zxd.etuhttp.wrapper.param.FormParam;
            import com.zxd.etuhttp.wrapper.utils.UriUtil;

            import java.io.File;
            import java.util.List;
            import java.util.Map;

            import okhttp3.Headers;
            import okhttp3.MediaType;
            import okhttp3.MultipartBody;
            import okhttp3.RequestBody;

            /**
             * author: zxd
             * created on: 2021/7/27 15:18
             * description:
             */
            public class EtuHttpFormParam extends EtuHttpAbstractBodyParam<FormParam, EtuHttpFormParam> {

                public EtuHttpFormParam(FormParam param) {
                    super(param);
                }

                public EtuHttpFormParam add(String key, Object value) {
                    param.add(key, value);
                    return this;
                }

                public EtuHttpFormParam add(String key, Object value, Boolean isAdd) {
                    if (isAdd) {
                        param.add(key, value);
                    }
                    return this;
                }

                public EtuHttpFormParam addAll(Map<String, ?> map) {
                    param.addAll(map);
                    return this;
                }

                public EtuHttpFormParam addEncoded(String key, Object value) {
                    param.addEncoded(key, value);
                    return this;
                }

                public EtuHttpFormParam addAllEncoded(@NonNull Map<String, ?> map) {
                    param.addAllEncoded(map);
                    return this;
                }

                public EtuHttpFormParam removeAllBody() {
                    param.removeAllBody();
                    return this;
                }

                public EtuHttpFormParam removeAllBody(String key) {
                    param.removeAllBody(key);
                    return this;
                }

                public EtuHttpFormParam set(String key, Object value) {
                    param.set(key, value);
                    return this;
                }

                public EtuHttpFormParam setEncoded(String key, Object value) {
                    param.setEncoded(key, value);
                    return this;
                }

                public EtuHttpFormParam addFile(String key, File file) {
                    param.addFile(key, file);
                    return this;
                }

                public EtuHttpFormParam addFile(String key, String filePath) {
                    param.addFile(key, filePath);
                    return this;
                }

                public EtuHttpFormParam addFile(String key, String fileName, String filePath) {
                    param.addFile(key, fileName, filePath);
                    return this;
                }

                public EtuHttpFormParam addFile(String key, String fileName, File file) {
                    param.addFile(key, fileName, file);
                    return this;
                }

                public EtuHttpFormParam addFile(UpFile file){
                    param.addFile(file);
                    return this;
                }

                public EtuHttpFormParam addFiles(List<? extends UpFile> fileList){
                    param.addFiles(fileList);
                    return this;
                }

                public <T> EtuHttpFormParam addFiles(Map<String,T> fileMap){
                    param.addFiles(fileMap);
                    return this;
                }

                public <T> EtuHttpFormParam addFiles(String key,List<T> fileList){
                    param.addFiles(key, fileList);
                    return this;
                }

                public EtuHttpFormParam addPart(MultipartBody.Part part){
                    param.addPart(part);
                    return this;
                }

                public EtuHttpFormParam addPart(RequestBody requestBody){
                    param.addPart(requestBody);
                    return this;
                }

                public EtuHttpFormParam addPart(Headers headers, RequestBody requestBody) {
                    param.addPart(headers, requestBody);
                    return this;
                }

                public EtuHttpFormParam addFormDataPart(String key, String fileName, RequestBody requestBody) {
                    param.addFormDataPart(key, fileName, requestBody);
                    return this;
                }

                public EtuHttpFormParam addPart(@Nullable MediaType contentType, byte[] content) {
                    param.addPart(contentType, content);
                    return this;
                }

                public EtuHttpFormParam addPart(@Nullable MediaType contentType, byte[] content, int offset,
                                               int byteCount) {
                    param.addPart(contentType, content, offset, byteCount);
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, Uri uri) {
                    param.addPart(UriUtil.asRequestBody(uri, context));
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, String key, Uri uri) {
                    param.addPart(UriUtil.asPart(uri, context, key));
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, String key, String fileName, Uri uri) {
                    param.addPart(UriUtil.asPart(uri, context, key, fileName));
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, Uri uri, @Nullable MediaType contentType) {
                    param.addPart(UriUtil.asRequestBody(uri, context, contentType));
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, String key, Uri uri,
                                               @Nullable MediaType contentType) {
                    param.addPart(UriUtil.asPart(uri, context, key, null, contentType));
                    return this;
                }

                public EtuHttpFormParam addPart(Context context, String key, String filename, Uri uri,
                                               @Nullable MediaType contentType) {
                    param.addPart(UriUtil.asPart(uri, context, key, filename, contentType));
                    return this;
                }


                public EtuHttpFormParam addParts(Context context, Map<String, Uri> uriMap) {
                    for (Map.Entry<String, Uri> entry : uriMap.entrySet()) {
                        addPart(context, entry.getKey(), entry.getValue());
                    }
                    return this;
                }

                public EtuHttpFormParam addParts(Context context, List<Uri> uris) {
                    for (Uri uri : uris) {
                        addPart(context, uri);
                    }
                    return this;
                }

                public EtuHttpFormParam addParts(Context context, String key, List<Uri> uris) {
                    for (Uri uri : uris) {
                        addPart(context, key, uri);
                    }
                    return this;
                }

                public EtuHttpFormParam addParts(Context context, List<Uri> uris,
                                                @Nullable MediaType contentType) {
                    for (Uri uri : uris) {
                        addPart(context, uri, contentType);
                    }
                    return this;
                }

                public EtuHttpFormParam addParts(Context context, String key, List<Uri> uris,
                                                @Nullable MediaType contentType) {
                    for (Uri uri : uris) {
                        addPart(context, key, uri, contentType);
                    }
                    return this;
                }

                //Set content-type to multipart/mixed
                public EtuHttpFormParam setMultiMixed() {
                    param.setMultiMixed();
                    return this;
                }

                //Set content-type to multipart/alternative
                public EtuHttpFormParam setMultiAlternative() {
                    param.setMultiAlternative();
                    return this;
                }

                //Set content-type to multipart/digest
                public EtuHttpFormParam setMultiDigest() {
                    param.setMultiDigest();
                    return this;
                }

                //Set content-type to multipart/parallel
                public EtuHttpFormParam setMultiParallel() {
                    param.setMultiParallel();
                    return this;
                }

                //Set the MIME type
                public EtuHttpFormParam setMultiType(MediaType multiType) {
                    param.setMultiType(multiType);
                    return this;
                }

            }
            
        """.trimIndent())
    }

    private fun generatorEtuHttpBodyParam(filer: Filer) {
        generatorClass(filer, "EtuHttpBodyParam", """
            package $etuHttpPackage;

            import android.content.Context;
            import android.net.Uri;
            
            import com.zxd.etuhttp.wrapper.annotations.Nullable;
            import com.zxd.etuhttp.wrapper.param.BodyParam;
            
            import java.io.File;
            
            import okhttp3.MediaType;
            import okhttp3.RequestBody;
            import okio.ByteString;
            
            /**
             * author: zxd
             * created on: 2021/7/26 15:59
             * description:
             */
            public class EtuHttpBodyParam extends EtuHttpAbstractBodyParam<BodyParam,EtuHttpBodyParam>{
            
                public EtuHttpBodyParam(BodyParam param) {
                    super(param);
                }
            
                public EtuHttpBodyParam setBody(RequestBody requestBody){
                    param.setBody(requestBody);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(String content, @Nullable MediaType mediaType){
                    param.setBody(content, mediaType);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(ByteString content,@Nullable MediaType mediaType){
                    param.setBody(content, mediaType);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(byte[] content, @Nullable MediaType mediaType){
                    param.setBody(content, mediaType);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(byte[] content, @Nullable MediaType mediaType, int offset, int byteCount){
                    param.setBody(content, mediaType, offset, byteCount);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(File file){
                    param.setBody(file);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(File file, @Nullable MediaType mediaType){
                    param.setBody(file, mediaType);
                    return this;
                }
            
                public EtuHttpBodyParam setBody(Uri uri, Context context){
                    param.setBody(uri, context);
                    return this;
                }
                
                public EtuHttpBodyParam setBody(Uri uri, Context context, @Nullable MediaType contentType){
                    param.setBody(uri, context, contentType);
                    return this;
                }
                
                public <T> EtuHttpBodyParam setBody(T object){
                    param.setJsonBody(object);
                    return this;
                }
            
            }

        """.trimIndent())
    }

    private fun generatorEtuHttpAbstractBodyParam(filer: Filer) {
        if (!isDependenceRxJava()) {
            generatorClass(filer, "EtuHttpAbstractBodyParam", """
                package $etuHttpPackage;
                
                import com.zxd.etuhttp.wrapper.param.AbstractBodyParam;

                /**
                 * author: zxd
                 * created on: 2021/7/26 15:46
                 * description:
                 */
                public class EtuHttpAbstractBodyParam <P extends AbstractBodyParam<P>,R extends EtuHttpAbstractBodyParam<P,R>> extends EtuHttp<P,R>{
                
                    protected EtuHttpAbstractBodyParam(P param){
                        super(param);
                    }
                
                    public final R setUploadMaxLength(long maxLength){
                        param.setUploadMaxLength(maxLength);
                        return (R) this;
                    }
                    
                }
            """.trimIndent())
        }
    }

    private fun generatorBaseEtuHttp(filer: Filer, android: Boolean) {
        if (!isDependenceRxJava()) {
            generatorClass(
                filer, "BaseEtuHttp", """
                package $etuHttpPackage;

                import com.zxd.etuhttp.wrapper.IEtuHttp;

                /**
                * 本类存放asXxx方法，如果依赖了RxJava的话
                * author: zxd
                * created on: 2021/7/26 15:12
                * description:
                */
                public abstract class BaseEtuHttp implements IEtuHttp {
    
    
                }
            """.trimIndent()
            )
        }
    }

    private fun generatorClass(filer: Filer, className: String, content: String) {
        var writer: BufferedWriter? = null
        try {
            val sourceFile = filer.createSourceFile("$etuHttpPackage.$className")
            writer = BufferedWriter(sourceFile.openWriter())
            writer.write(content)
        } catch (e: Exception) {

        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                //Silent
            }
        }
    }
}