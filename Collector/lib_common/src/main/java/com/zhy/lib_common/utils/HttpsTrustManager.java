package com.zhy.lib_common.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


/**
 * SSL允许访问所有https地址工具类
 *
 * @author Nielev
 * @version 2016年2月25日  下午2:19:03
 * @company Oray
 */
public class HttpsTrustManager implements X509TrustManager {

    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(
            X509Certificate[] x509Certificates, String s)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        try {
            x509Certificates[0].checkValidity();
        } catch (Exception e) {
            throw new CertificateException("Certificate not valid or trusted.");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }
}