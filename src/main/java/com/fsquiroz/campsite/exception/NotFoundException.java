package com.fsquiroz.campsite.exception;

import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AppException {

    private static final String ENTITY_KEY = "entity";
    private static final String ID_KEY = "id";

    private static final String BY_ID = "Entity not found by id";

    private NotFoundException(ErrorCode code, Map<String, Object> meta, String message) {
        super(code, meta, message);
    }

    public static NotFoundException byId(Class<?> clazz, @NotNull Long id) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put(ENTITY_KEY, clazz.getSimpleName());
        meta.put(ID_KEY, id);
        return new NotFoundException(ErrorCode.NF_BY_ID, meta, BY_ID);
    }
}
