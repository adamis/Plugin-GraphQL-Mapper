package graphql.mapper.request;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class RetryInterceptor implements Interceptor {
	 	private final int maxRetries;
	    private final long delayMs;

	    public RetryInterceptor(int maxRetries, long delayMs) {
	        this.maxRetries = maxRetries;
	        this.delayMs = delayMs;
	    }

	    @Override
	    public Response intercept(Chain chain) throws IOException {
	        Request request = chain.request();
	        IOException lastException = null;

	        for (int i = 0; i < maxRetries; i++) {
	            try {
	                Response response = chain.proceed(request);
	                if (response.isSuccessful()) return response;
	                lastException = new IOException("Response code: " + response.code());
	            } catch (IOException e) {
	                lastException = e;
	            }

	            try {
	                Thread.sleep(delayMs * (i + 1)); // backoff linear
	            } catch (InterruptedException ignored) { }
	        }

	        throw lastException;
	    }
}
