package pl.mazi.transportdesktopapp.Config;



import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.function.Supplier;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.requestFactory(myRequestFactorySupplier()).build();
    }

    @Bean
    public MyRequestFactorySupplier myRequestFactorySupplier() {
        return new MyRequestFactorySupplier();
    }

    class MyRequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {
        @Override
        public ClientHttpRequestFactory get() {
            HttpComponentsClientHttpRequestFactory requestFactory = null;
            try {
                char[] pass = "pracaDyplomowa".toCharArray();
                KeyStore ks = KeyStore.getInstance("JKS");
                InputStream ksInput = this.getClass().getClassLoader().getResourceAsStream("javaclient.jks");
                ks.load(ksInput, pass);

                SSLContext sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(this.getClass().getClassLoader().getResource("certificate.jks"), pass)
                        .loadKeyMaterial(ks, pass).build();

                SSLConnectionSocketFactory socket =
                        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

                CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(socket).build();

                requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
                requestFactory.setBufferRequestBody(false);
            } catch (Exception e) {
                System.out.println("Exception: {} " + e.getMessage());
            }
            return requestFactory;
        }
    }
}
