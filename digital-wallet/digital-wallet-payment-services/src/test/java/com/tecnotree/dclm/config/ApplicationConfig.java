package com.tecnotree.dclm.config;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@Profile({"test"})
public class ApplicationConfig extends WebMvcConfigurationSupport {


    String isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    SimpleDateFormat isoDate = new SimpleDateFormat(isoFormat);

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                mapper.setSerializationInclusion(Include.NON_EMPTY);
                mapper.setSerializationInclusion(Include.NON_NULL);
                mapper.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
                mapper.setDateFormat(isoDate);
            }
        }
    }


}
