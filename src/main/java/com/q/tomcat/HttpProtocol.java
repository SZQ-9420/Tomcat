package com.q.tomcat;

/**
 * 封装Http切割之后的数据内容
 */
public class HttpProtocol {

    private String requestMethod;
    private String url;
    private String servletUri;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServletUri() {
        return servletUri;
    }

    public void setServletUri(String servletUri) {
        this.servletUri = servletUri;
    }
}