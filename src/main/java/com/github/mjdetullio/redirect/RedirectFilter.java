package com.github.mjdetullio.redirect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of a servlet filter that will redirect the accessed URL to a
 * new URL which is read from configuration.
 * 
 * @author Matthew DeTullio
 * @version 1.0
 * @since 2014-01-28
 */
public class RedirectFilter implements Filter {
	private static final String CLASSNAME = RedirectFilter.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private static final String PROPS = "/WEB-INF/application.properties";
	private static final String NEW_BASE_URL = "new.base.url";

	private String baseUrl;

	/**
	 * Redirects all URLs matched by the filter to a new base URL.
	 * 
	 * {@inheritDoc}
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final String methodName = "doFilter";
		LOGGER.entering(CLASSNAME, methodName);

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		// Build new URL
		StringBuilder newUrl = new StringBuilder(baseUrl
				+ request.getPathInfo());

		// Include URL parameters if they're present
		if (null != request.getQueryString()) {
			newUrl.append('?');
			newUrl.append(request.getQueryString());
		}

		LOGGER.info("Redirecting to: " + newUrl.toString());

		((HttpServletResponse) servletResponse).sendRedirect(newUrl.toString());

		LOGGER.exiting(CLASSNAME, methodName);
	}

	/**
	 * Loads application.properties upon filter initialization to retrieve the
	 * new base URL.
	 * 
	 * {@inheritDoc}
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		final String methodName = "init";
		LOGGER.entering(CLASSNAME, methodName);

		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(PROPS);
			properties.load(is);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to read properties from file: "
					+ PROPS, e);
			throw new ServletException(
					"Filter can not be placed into service without the new base URL.",
					e);
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Unable to close file stream: " + PROPS,
						e);
			}
		}

		// Cut off the trailing '/' if it's present
		baseUrl = properties.getProperty(NEW_BASE_URL);
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}

		LOGGER.info("New Base URL: " + baseUrl);

		LOGGER.exiting(CLASSNAME, methodName);
	}

	/**
	 * Sets any saved properties to null upon filter destruction.
	 * 
	 * {@inheritDoc}
	 */
	public void destroy() {
		final String methodName = "destroy";
		LOGGER.entering(CLASSNAME, methodName);

		baseUrl = null;

		LOGGER.exiting(CLASSNAME, methodName);
	}
}