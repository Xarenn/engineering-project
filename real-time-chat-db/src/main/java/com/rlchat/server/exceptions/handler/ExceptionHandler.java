package com.rlchat.server.exceptions.handler;

import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.exceptions.ServerException;
import com.rlchat.server.exceptions.util.ErrorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class ExceptionHandler<T> {

    public Mono<ServerResponse> handle(Mono<T> stream) {
        return stream.flatMap(object -> ok().body(Mono.just(object), Object.class))
                .onErrorResume(ServerException.class, exc -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .body(Mono.justOrEmpty(ErrorUtil.errorToMessage(exc)),
                                ErrorUtil.ErrorOutputMessage.class))
                .switchIfEmpty(badRequest().body(Mono.justOrEmpty(new BadRequest()), String.class));
    }

    public Mono<ServerResponse> handle(Flux<T> stream) {
        return Mono.from(stream.flatMap(object -> ok().body(Mono.just(object), Object.class))
                .onErrorResume(ServerException.class, exc -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .body(Mono.justOrEmpty(ErrorUtil.errorToMessage(exc)),
                                ErrorUtil.ErrorOutputMessage.class))
                .switchIfEmpty(badRequest().body(Mono.justOrEmpty(new BadRequest()), String.class)));
    }


}
