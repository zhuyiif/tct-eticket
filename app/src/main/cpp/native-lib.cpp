#include <jni.h>
#include <string>
#include <malloc.h>
#include <openssl/ec.h>
#include <openssl/evp.h>
#include <openssl/sm2.h>
#include <crypto/ec/ec_lcl.h>
#include <openssl/aes.h>
#include "utils.h"
#include "sm4.h"

char *path;


EC_KEY *getEcKey() {
    std::string p1 = path;
    p1.append("/private");

    std::string p2 = path;
    p2.append("/public");

    char *privateChar = (char *) malloc(1024);
    memset(privateChar, 0, 1024);
    readBufFromFile((char *) p1.c_str(), privateChar);

    char *publicChar = (char *) malloc(1024);
    memset(publicChar, 0, 1024);
    readBufFromFile((char *) p2.c_str(), publicChar);

    EC_KEY *ec_key = EC_KEY_new();
    ec_key = EC_KEY_new_by_curve_name(NID_sm2p256v1);
    BN_CTX *ctx1 = BN_CTX_new();
    EC_POINT *pubkey_point = EC_POINT_hex2point(ec_key->group, publicChar, NULL, ctx1);
    int iret = EC_KEY_set_public_key(ec_key, pubkey_point);
    BIGNUM *bn_prikey = BN_new();
    iret = BN_hex2bn(&bn_prikey, privateChar);
    iret = EC_KEY_set_private_key(ec_key, bn_prikey);
    p1.clear();
    p2.clear();
    free(publicChar);
    free(privateChar);
    BN_free(bn_prikey);
    EC_POINT_free(pubkey_point);
    return ec_key;

}

EC_KEY *mk_eckey(int nid, const unsigned char *key, int keylen) {
    int ok = 0;
    EC_KEY *k = NULL;
    BIGNUM *priv = NULL;
    EC_POINT *pub = NULL;
    const EC_GROUP *grp;
    k = EC_KEY_new_by_curve_name(nid);
    if (!k) {
        goto err;
    }

    if (!(priv = BN_bin2bn(key, keylen, NULL))) {
        goto err;
    }

    if (!EC_KEY_set_private_key(k, priv)) {
        goto err;
    }
    grp = EC_KEY_get0_group(k);
    pub = EC_POINT_new(grp);
    if (!pub) {
        goto err;
    }

    if (!EC_POINT_mul(grp, pub, priv, NULL, NULL, NULL)) {
        goto err;
    }

    if (!EC_KEY_set_public_key(k, pub)) {
        goto err;
    }
    ok = 1;
    err:
    BN_clear_free(priv);
    EC_POINT_free(pub);
    if (ok) {
        return k;
    }
    EC_KEY_free(k);
    return NULL;
}

int sm4dec(unsigned char *srcKey ,int len, unsigned char *outKey) {
    unsigned char key[16] = {0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33, 0x33,
                             0x33};
    sm4_context ctx;
    sm4_setkey_dec(&ctx, key);
    sm4_crypt_ecb(&ctx, 0, len, srcKey, outKey);
    return 0;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_aesEnc(JNIEnv *env,
                                                            jclass theClass,
                                                            jbyteArray in_,
                                                            jint length,
                                                            jbyteArray key_) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);
    jbyte *key = env->GetByteArrayElements(key_, NULL);

    int pading = AES_BLOCK_SIZE - length % AES_BLOCK_SIZE;
    int block = length / AES_BLOCK_SIZE;
    int endLen = AES_BLOCK_SIZE - pading;

    unsigned char *p = (unsigned char *) malloc(AES_BLOCK_SIZE + 1);
    memset(p, 0, AES_BLOCK_SIZE + 1);
    memset(p + endLen, pading, (size_t) pading);
    memcpy(p, in + block * AES_BLOCK_SIZE, (size_t) endLen);

    AES_KEY aes_key;
    AES_set_encrypt_key((const unsigned char *) key, 16 * 8, &aes_key);

    unsigned char *out = (unsigned char *) malloc((size_t) (length + pading + 1));
    memset(out, 0, (size_t) (length + pading + 1));

    for (int i = 0; i < block; i++) {
        AES_encrypt((const unsigned char *) (in + (i * AES_BLOCK_SIZE)),
                    out + i * AES_BLOCK_SIZE,
                    &aes_key);
    }
    AES_encrypt(p, out + block * AES_BLOCK_SIZE, &aes_key);

    jbyteArray array = env->NewByteArray(length + pading);
    env->SetByteArrayRegion(array, 0, length + pading, (const jbyte *) out);

    free(p);
    free(out);

    env->ReleaseByteArrayElements(in_, in, 0);
    env->ReleaseByteArrayElements(key_, key, 0);

    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_aesDec(JNIEnv *env,
                                                            jclass theClass,
                                                            jbyteArray in_,
                                                            jint length,
                                                            jbyteArray key_) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);
    jbyte *key = env->GetByteArrayElements(key_, NULL);

    AES_KEY aes_key;
    AES_set_decrypt_key((const unsigned char *) key, 16 * 8, &aes_key);

    unsigned char *out = (unsigned char *) malloc(length);
    memset(out, 0, length);

    for (int i = 0; i < length / 16; i++) {
        AES_decrypt((const unsigned char *) (in + (i * AES_BLOCK_SIZE)),
                    out + i * AES_BLOCK_SIZE,
                    &aes_key);
    }
    //去补位
    int padinglen = out[length - 1];
    memset(out + length - padinglen, 0, padinglen);

    jbyteArray array = env->NewByteArray(length - padinglen);
    env->SetByteArrayRegion(array, 0, length - padinglen, (const jbyte *) out);

    free(out);
    env->ReleaseByteArrayElements(in_, in, 0);
    env->ReleaseByteArrayElements(key_, key, 0);

    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sha1(JNIEnv *env,
                                                          jclass theClass,
                                                          jbyteArray in_,
                                                          jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);

    unsigned char *sha1Msg = (unsigned char *) malloc(SHA_DIGEST_LENGTH + 1);
    memset(sha1Msg, 0, SHA_DIGEST_LENGTH + 1);

    SHA1((const unsigned char *) in, length, sha1Msg);

    jbyteArray array = env->NewByteArray(SHA_DIGEST_LENGTH);
    env->SetByteArrayRegion(array, 0, SHA_DIGEST_LENGTH, (const jbyte *) sha1Msg);

    free(sha1Msg);
    env->ReleaseByteArrayElements(in_, in, 0);

    return array;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_eticket_local_CryptUtils_genSM2KeyPairs(JNIEnv *env,
                                                                    jclass theClass,
                                                                    jstring path_) {

    jboolean result = false;
    const char *p = env->GetStringUTFChars(path_, &result);
    int pLen = env->GetStringUTFLength(path_);

    path = (char *) malloc(pLen + 1);
    memset(path, 0, pLen + 1);
    memcpy(path, p, pLen);

    std::string p1 = path;
    p1.append("/private");

    std::string p2 = path;
    p2.append("/public");

    EC_KEY *ec_key = EC_KEY_new();
    ec_key = EC_KEY_new_by_curve_name(NID_sm2p256v1);
    EC_KEY_generate_key(ec_key);
    const EC_POINT *point = EC_KEY_get0_public_key(ec_key);
    char *publicChar = EC_POINT_point2hex(EC_KEY_get0_group(ec_key),
                                          point,
                                          POINT_CONVERSION_UNCOMPRESSED,
                                          BN_CTX_new());
    const BIGNUM *privateKey = EC_KEY_get0_private_key(ec_key);
    char *privateChar = BN_bn2hex(privateKey);

    int iRet = writeBufToFile((char *) p1.c_str(), privateChar);
    iRet = writeBufToFile((char *) p2.c_str(), publicChar);

    EC_KEY_free(ec_key);
    return 0;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm3(JNIEnv *env,
                                                         jclass theClass,
                                                         jbyteArray in_,
                                                         jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);

    unsigned char *sm3Msg = (unsigned char *) malloc(SM3_DIGEST_LENGTH + 1);
    memset(sm3Msg, 0, SM3_DIGEST_LENGTH + 1);

    sm3((const unsigned char *) in, length, sm3Msg);

    jbyteArray array = env->NewByteArray(SM3_DIGEST_LENGTH);
    env->SetByteArrayRegion(array, 0, SM3_DIGEST_LENGTH, (const jbyte *) sm3Msg);

    free(sm3Msg);
    env->ReleaseByteArrayElements(in_, in, 0);

    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm4Dec(JNIEnv *env,
                                                            jclass theClass,
                                                            jbyteArray in_,
                                                            jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);

    unsigned char out[32] = {0x0};
    sm4dec((unsigned char *)in, length, out);

    jbyteArray array = env->NewByteArray(sizeof(out));
    env->SetByteArrayRegion(array, 0, sizeof(out), (const jbyte *) out);

    env->ReleaseByteArrayElements(in_, in, 0);

    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm2Enc(JNIEnv *env,
                                                            jclass theClass,
                                                            jbyteArray in_,
                                                            jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);

    int iRet = 0;
    EC_KEY *ec_key = getEcKey();
    size_t sm2EncLen = SM2_MAX_PLAINTEXT_LENGTH;

    unsigned char *sm2EncMsg = (unsigned char *) malloc(SM2_MAX_PLAINTEXT_LENGTH);
    memset(sm2EncMsg, 0, SM2_MAX_PLAINTEXT_LENGTH);

    iRet = SM2_encrypt(NID_sm3,
                       (const unsigned char *) in,
                       (size_t) length,
                       sm2EncMsg,
                       &sm2EncLen,
                       ec_key);

    if (!iRet) {
        ERR_load_ERR_strings();
        ERR_load_crypto_strings();

        unsigned long ulErr = ERR_get_error(); // 获取错误号

        const char *pTmp = ERR_reason_error_string(ulErr);
        puts(pTmp);
    }

    jbyteArray array = env->NewByteArray(sm2EncLen);
    env->SetByteArrayRegion(array, 0, sm2EncLen, (const jbyte *) sm2EncMsg);

    free(sm2EncMsg);
    EC_KEY_free(ec_key);
    env->ReleaseByteArrayElements(in_, in, 0);

    return array;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm2Dec(JNIEnv *env,
                                                            jclass theClass,
                                                            jbyteArray in_,
                                                            jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);

    int iRet = 0;
    EC_KEY *ec_key = getEcKey();
    size_t sm2DecLen = 0;

    iRet = SM2_decrypt(NID_sm3,
                       (const unsigned char *) in,
                       (size_t) length,
                       NULL,
                       &sm2DecLen,
                       ec_key);

    unsigned char *sm2DecMsg = (unsigned char *) malloc(sm2DecLen + 1);
    memset(sm2DecMsg, 0, sm2DecLen);

    iRet = SM2_decrypt(NID_sm3,
                       (const unsigned char *) in,
                       (size_t) length,
                       sm2DecMsg,
                       &sm2DecLen,
                       ec_key);

    jbyteArray array = env->NewByteArray(sm2DecLen);
    env->SetByteArrayRegion(array, 0, sm2DecLen, (const jbyte *) sm2DecMsg);

    free(sm2DecMsg);
    EC_KEY_free(ec_key);
    env->ReleaseByteArrayElements(in_, in, 0);

    return array;
}

/**
 * replaced by following
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm2Sign(JNIEnv *env,
                                                             jclass theClass,
                                                             jbyteArray in_,
                                                             jint length) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);
    int iret = -1;

    EC_KEY *ec_key = getEcKey();


    size_t zlen = 0;
    iret = SM2_compute_message_digest(EVP_sm3(),
                                      EVP_sm3(),
                                      (const unsigned char *) in,
                                      length,
                                      SM2_DEFAULT_ID_GMT09,
                                      SM2_DEFAULT_ID_LENGTH,
                                      NULL,
                                      &zlen,
                                      ec_key);
    if (!iret) {
        return NULL;
    }
    unsigned char *z = (unsigned char *) malloc(zlen + 1);
    memset(z, 0, zlen + 1);
    iret = SM2_compute_message_digest(EVP_sm3(),
                                      EVP_sm3(),
                                      (const unsigned char *) in,
                                      length,
                                      SM2_DEFAULT_ID_GMT09,
                                      SM2_DEFAULT_ID_LENGTH,
                                      z,
                                      &zlen,
                                      ec_key);
    if (!iret) {
        return NULL;
    }

    unsigned int signLen = 0;
    iret = SM2_sign(NID_sm3, z, zlen, NULL, &signLen, ec_key);
    if (!iret) {
        return NULL;
    }
    unsigned char *signMsg = (unsigned char *) malloc(signLen + 1);
    memset(signMsg, 0, signLen + 1);
    iret = SM2_sign(NID_sm3, z, zlen, signMsg, &signLen, ec_key);
    if (!iret) {
        return NULL;
    }

    jbyteArray array = env->NewByteArray(signLen);
    env->SetByteArrayRegion(array, 0, signLen, (const jbyte *) signMsg);

    free(signMsg);
    free(z);
    EC_KEY_free(ec_key);
    env->ReleaseByteArrayElements(in_, in, 0);
    return array;
}
**/

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_eticket_local_CryptUtils_sm2Sign(JNIEnv *env,
                                                  jclass theClass,
                                                  jbyteArray in_,
                                                  jint length,
                                                  jbyteArray key_,
                                                  jint keyLen) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);
    jbyte *key = env->GetByteArrayElements(key_, NULL);

    const EVP_MD *id_md = EVP_sm3();
    const EVP_MD *msg_md = EVP_sm3();

    int type = NID_sm2p256v1;
    unsigned char dgst[EVP_MAX_MD_SIZE];
    size_t dgstlen = 32;
    const char *id = SM2_DEFAULT_ID_GMT09;

    ECDSA_SIG *sm2sig = NULL;
    unsigned char sig[256] = {0x0};
    unsigned int siglen = 0;

    const BIGNUM *sig_r;
    const BIGNUM *sig_s;
    const unsigned char *p;
    int ret = 0;

    jbyteArray array;

    EC_KEY *ec_key = mk_eckey(NID_sm2p256v1, (const unsigned char *)key, keyLen);

    if(!SM2_compute_message_digest(id_md, msg_md, (const unsigned char *)in, length, id,
                               strlen(id), dgst, &dgstlen, ec_key)){
        ret = -1;
    }

    if(ret >= 0) {
        siglen = sizeof(sig);
        if (!SM2_sign(type, dgst, dgstlen, sig, &siglen, ec_key)) {
            ret = -2;
        }

        if(ret >= 0) {
            p = sig;
            sm2sig = d2i_ECDSA_SIG(NULL, &p, siglen);

            ECDSA_SIG_get0(sm2sig, &sig_r, &sig_s);

            unsigned char sign[64] = {0x0};
            int signLen = 0;
            siglen = BN_bn2bin(sig_r, sign);
            signLen = siglen;
            siglen = BN_bn2bin(sig_s, sign + siglen);
            signLen += siglen;

            array = env->NewByteArray(signLen);
            env->SetByteArrayRegion(array, 0, signLen, (const jbyte *) sign);

//            array = env->NewByteArray(siglen);
//            env->SetByteArrayRegion(array, 0, siglen, (const jbyte *) sig);
        }
}

    if (sm2sig) {
        ECDSA_SIG_free(sm2sig);
    }
    if (ec_key) {
        EC_KEY_free(ec_key);
    }
    env->ReleaseByteArrayElements(key_, key, 0);
    env->ReleaseByteArrayElements(in_, in, 0);
    if(ret >= 0){
        return array;
    }
    return NULL;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_eticket_local_CryptUtils_sm2Verify(JNIEnv *env,
                                                               jclass theClass,
                                                               jbyteArray in_,
                                                               jint length,
                                                               jbyteArray sign_,
                                                               jint signLen) {
    jbyte *in = env->GetByteArrayElements(in_, NULL);
    jbyte *sign = env->GetByteArrayElements(sign_, NULL);
    int iret = -1;
    EC_KEY *ec_key = getEcKey();

    size_t zlen = 0;
    iret = SM2_compute_message_digest(EVP_sm3(),
                                      EVP_sm3(),
                                      (const unsigned char *) in,
                                      length,
                                      SM2_DEFAULT_ID_GMT09,
                                      SM2_DEFAULT_ID_LENGTH,
                                      NULL,
                                      &zlen,
                                      ec_key);

    unsigned char *z = (unsigned char *) malloc(zlen + 1);
    memset(z, 0, zlen + 1);
    iret = SM2_compute_message_digest(EVP_sm3(),
                                      EVP_sm3(),
                                      (const unsigned char *) in,
                                      length,
                                      SM2_DEFAULT_ID_GMT09,
                                      SM2_DEFAULT_ID_LENGTH,
                                      z,
                                      &zlen,
                                      ec_key);
    if (!iret) {
        return -2;
    }

    iret = SM2_verify(NID_sm3, z, zlen, (const unsigned char *) sign, signLen, ec_key);

    free(z);
    EC_KEY_free(ec_key);
    env->ReleaseByteArrayElements(in_, in, 0);
    env->ReleaseByteArrayElements(sign_, sign, 0);
    return iret;
}
