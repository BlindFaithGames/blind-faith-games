/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_haptimap_offis_openal_OpenAl */

#ifndef _Included_org_haptimap_offis_openal_OpenAl
#define _Included_org_haptimap_offis_openal_OpenAl
#ifdef __cplusplus
extern "C" {
#endif
#undef org_haptimap_offis_openal_OpenAl_SUCCESS
#define org_haptimap_offis_openal_OpenAl_SUCCESS 1L
#undef org_haptimap_offis_openal_OpenAl_ERROR
#define org_haptimap_offis_openal_OpenAl_ERROR 0L
/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    init
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_init
  (JNIEnv *, jclass);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_close
  (JNIEnv *, jclass);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    addBuffer
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_addBuffer
  (JNIEnv *, jclass, jstring);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    releaseBuffer
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_releaseBuffer
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    addSource
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_addSource
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    releaseSource
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_releaseSource
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setPosition
 * Signature: (IFFF)V
 */
JNIEXPORT void JNICALL Java_org_haptimap_offis_openal_OpenAl_setPosition
  (JNIEnv *, jclass, jint, jfloat, jfloat, jfloat);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setPitch
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_org_haptimap_offis_openal_OpenAl_setPitch
  (JNIEnv *, jclass, jint, jfloat);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setGain
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_org_haptimap_offis_openal_OpenAl_setGain
  (JNIEnv *, jclass, jint, jfloat);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setRolloffFactor
 * Signature: (IF)V
 */
JNIEXPORT void JNICALL Java_org_haptimap_offis_openal_OpenAl_setRolloffFactor
  (JNIEnv *, jclass, jint, jfloat);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    play
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_play
  (JNIEnv *, jclass, jint, jboolean);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    stop
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_stop
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setListenerPos
 * Signature: (FFF)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_setListenerPos
  (JNIEnv *, jclass, jfloat, jfloat, jfloat);

/*
 * Class:     org_haptimap_offis_openal_OpenAl
 * Method:    setListenerOrientation
 * Signature: (FFF)I
 */
JNIEXPORT jint JNICALL Java_org_haptimap_offis_openal_OpenAl_setListenerOrientation
  (JNIEnv *, jclass, jfloat, jfloat, jfloat);

#ifdef __cplusplus
}
#endif
#endif
