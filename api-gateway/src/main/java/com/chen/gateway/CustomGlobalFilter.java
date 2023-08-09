package com.chen.gateway;


import com.chen.apiclientsdk.utils.SignUtils;
import com.chen.client.model.entity.InterfaceInfo;
import com.chen.client.model.entity.User;
import com.chen.client.model.service.InnerInterfaceInfoService;
import com.chen.client.model.service.InnerUserInterfaceInfoService;
import com.chen.client.model.service.InnerUserService;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义全局过滤器
 *
 * @author CSY
 * @date 2023/08/06
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "localhost");
    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInfoService userInterfaceInfoService;
    @DubboReference
    private InnerUserService innerUserService;

    @Value("${spring.cloud.gateway.routes[0].uri}")
    private String host;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = host + request.getPath().value();
        log.info("请求地址为：{}", path);
        String method = request.getMethod().toString();
        log.info("请求方法为：{}", method);
        String address = request.getLocalAddress().getHostString();
        log.info("请求地址为：{}", address);
//        黑白名单 这里使用白名单的方式
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(address)) {
            return handleNoAuth(response);
        }
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("noce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        log.info("请求参数为：{}", body);
//      实际情况查数据库的sk 用户鉴权（判断 ak、sk 是否合法）
        User user = innerUserService.getInvokeUser(accessKey);
        String userAccessKey = user.getAccessKey();
        if (!userAccessKey.equals(accessKey)) {
            return handleNoAuth(response);
        }
        if (nonce == null || Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        //时间和当前时间不超过5分钟
        long nowTime = System.currentTimeMillis() / 100;
        // 计算时间差（以毫秒为单位）
        if (timestamp == null) {
            return handleNoAuth(response);
        }
        long diffInMillis = nowTime - Long.parseLong(timestamp);
        long diffInMinutes = diffInMillis / (60 * 1000);
        if (diffInMinutes > 5) {
            return handleNoAuth(response);
        }
        //  实际情况中是从数据库中查出 secretKey
        String secretKey = user.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        //  请求的模拟接口是否存在？
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(path, method);
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //  是否还有调用次数  拿到请求次数  需要 接口id 和 请求人的id
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = user.getId();
        userInterfaceInfoService.verifications(interfaceInfoId, userId);
        //  请求转发，调用模拟接口
        //  响应日志
        log.info("响应码:{}", response.getStatusCode());
        // 调用成功，接口调用次数 + 1
        return handleResponse(exchange, chain, interfaceInfo.getId(), userId);
    }

    /**
     * 处理响应
     *
     * @param exchange        交换
     * @param chain           链
     * @param interfaceInfoId 接口信息id
     * @param userId          用户id
     * @return {@code Mono<Void>}
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invoke
                                        try {
                                            userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;

    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
    public Mono<Void> handleNoCount(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.OK);
        return response.setComplete();
    }
}