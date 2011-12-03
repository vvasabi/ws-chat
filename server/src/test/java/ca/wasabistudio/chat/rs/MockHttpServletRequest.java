package ca.wasabistudio.chat.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class MockHttpServletRequest implements HttpServletRequest {

	private final HttpSession session;

	public MockHttpServletRequest(HttpSession session) {
		this.session = session;
	}

	@Override
	public AsyncContext getAsyncContext() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Object getAttribute(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public int getContentLength() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public String getContentType() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getLocalAddr() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getLocalName() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public int getLocalPort() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public Locale getLocale() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getParameter(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String[] getParameterValues(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getProtocol() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRealPath(String path) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRemoteHost() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public int getRemotePort() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getScheme() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getServerName() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public int getServerPort() {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isSecure() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public void removeAttribute(String key) {
		// NOT IMPLEMENTED

	}

	@Override
	public void setAttribute(String key, Object value) {
		// NOT IMPLEMENTED

	}

	@Override
	public void setCharacterEncoding(String encoding)
			throws UnsupportedEncodingException {
		// NOT IMPLEMENTED

	}

	@Override
	public AsyncContext startAsync() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest request,
			ServletResponse response) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public String getAuthType() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getContextPath() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public long getDateHeader(String key) {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public String getHeader(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String key) {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public int getIntHeader(String key) {
		// NOT IMPLEMENTED
		return 0;
	}

	@Override
	public String getMethod() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, IllegalStateException,
			ServletException {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException,
			IllegalStateException, ServletException {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getPathInfo() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getPathTranslated() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getQueryString() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRemoteUser() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRequestURI() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public String getServletPath() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (create) {
			return null;
		}
		return session;
	}

	@Override
	public Principal getUserPrincipal() {
		// NOT IMPLEMENTED
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		// NOT IMPLEMENTED
		return false;
	}

	@Override
	public void login(String username, String password)
			throws ServletException {
		// NOT IMPLEMENTED
	}

	@Override
	public void logout() throws ServletException {
		// NOT IMPLEMENTED
	}

}
