package com.example.demo;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.example.demo.CloudWatchProperties;

import io.micrometer.cloudwatch.CloudWatchConfig;
import io.micrometer.cloudwatch.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;

@SpringBootApplication
public class M3trics2Application {

  @Bean
  RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.messageConverters(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter()).build();
  }
  
  @Bean
  public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config,
                                                         Clock clock, AmazonCloudWatchAsync client) {
      return new CloudWatchMeterRegistry(config, clock, client);
  }
  
  @Bean
  public Clock micrometerClock() {
      return Clock.SYSTEM;
  }
  
  @Bean
  public AmazonCloudWatchAsync amazonCloudWatchAsync() {
      return AmazonCloudWatchAsyncClient.asyncBuilder().build();
  }
  
  @Bean
  public CloudWatchConfig cloudWatchConfig(CloudWatchProperties properties) {
      return new CloudWatchConfig() {
          @Override
          public String prefix() {
              return null;
          }

          @Override
          public String namespace() {
              return properties.getNamespace();
          }

          @Override
          public Duration step() {
              return properties.getStep();
          }

          @Override
          public boolean enabled() {
              return properties.isEnabled();
          }

          @Override
          public int batchSize() {
              return properties.getBatchSize();
          }

          @Override
          public String get(String s) {
              return null;
          }
      };
  }

  public static void main(String[] args) {
    SpringApplication.run(M3trics2Application.class, args);
  }
}
