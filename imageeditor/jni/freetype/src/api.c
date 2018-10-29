#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <GLES2/gl2.h>
#include <android/log.h>

#include <ft2build.h>
#include FT_FREETYPE_H

#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG , "FreeTypeNative", __VA_ARGS__)

jintArray loadText(JNIEnv *env, jobject obj,
                   jstring jFontPath,
                   jintArray textArray,
                   jint textSize) {
    FT_Library library;
    FT_Face face;

    FT_Error error;

    int n, num_chars;

    const char *fontPath = (*env)->GetStringUTFChars(env, jFontPath, NULL);
    const int *text = (*env)->GetIntArrayElements(env, textArray, 0);

    num_chars = (*env)->GetArrayLength(env, textArray);

    error = FT_Init_FreeType(&library);              /* initialize library */
    /* error handling omitted */

    error = FT_New_Face(library, fontPath, 0, &face);/* create face object */
    /* error handling omitted */

    FT_Set_Pixel_Sizes(face, 0, textSize);

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    jint resultArray[num_chars * 7];
    int resultIndex = 0;
    for (n = 0; n < num_chars; n++) {
        /* load glyph image into the slot (erase previous one) */
        error = FT_Load_Char(face, text[n], FT_LOAD_RENDER);
        if (error) {
            continue;                 /* ignore errors */
        }

        __android_log_print(ANDROID_LOG_DEBUG, "FreeTypeNative",
                            "bitmap width: %d, bitmap height: %d, bitmap left: %d, bitmap top: %d, glyph advance x: %ld, glyph advance y: %ld.",
                            face->glyph->bitmap.width,
                            face->glyph->bitmap.rows, face->glyph->bitmap_left,
                            face->glyph->bitmap_top, face->glyph->advance.x,
                            face->glyph->advance.x);

        // 生成纹理
        GLuint texture;
        glGenTextures(1, &texture);
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_ALPHA,
                face->glyph->bitmap.width,
                face->glyph->bitmap.rows,
                0,
                GL_ALPHA,
                GL_UNSIGNED_BYTE,
                face->glyph->bitmap.buffer
        );
        // 设置纹理选项
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // 保存纹理id
        resultArray[resultIndex++] = texture;
        resultArray[resultIndex++] = face->glyph->bitmap.width;
        resultArray[resultIndex++] = face->glyph->bitmap.rows;
        resultArray[resultIndex++] = face->glyph->bitmap_left;
        resultArray[resultIndex++] = face->glyph->bitmap_top;
        resultArray[resultIndex++] = face->glyph->advance.x;
        resultArray[resultIndex++] = face->glyph->advance.y;
    }

    FT_Done_Face(face);
    FT_Done_FreeType(library);

    // result array
    jintArray result = (*env)->NewIntArray(env, num_chars * 7);
    (*env)->SetIntArrayRegion(env, result, 0, num_chars * 7, resultArray);
    return result;
}

static JNINativeMethod sMethods[] = {
        /* name, signature, funcPtr */
        {"loadText", "(Ljava/lang/String;[II)[I", (void *) loadText},

};

int register_freetype_native_function(JNIEnv *env) {
    jclass clazz;
    static const char *const kClassName = "com/createchance/imageeditor/freetype/FreeType";

    clazz = (*env)->FindClass(env, kClassName);
    if (clazz == NULL) {
        ALOGD("cannot get class:%s\n", kClassName);
        return -1;
    }

    if ((*env)->RegisterNatives(env, clazz, sMethods, sizeof(sMethods) / sizeof(sMethods[0])) !=
        JNI_OK) {
        ALOGD("register native method failed!\n");
        return -1;
    }

    return 0;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    int status;

    ALOGD("FreeType jni on load.");

    if ((*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    if (register_freetype_native_function(env)) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}

/* EOF */