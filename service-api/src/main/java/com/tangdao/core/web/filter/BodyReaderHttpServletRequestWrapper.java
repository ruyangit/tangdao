package com.tangdao.core.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月24日
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] requestBody = null;// 用于将流保存下来

	/**
	 * @param request
	 * @throws IOException
	 */
	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// TODO Auto-generated constructor stub
		requestBody = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);

		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public byte[] getRequestBody() {
		return requestBody;
	}

}
