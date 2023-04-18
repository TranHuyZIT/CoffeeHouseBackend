package com.tma.coffeehouse.config.Serializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDeserializer extends JsonDeserializer<Page<?>> implements ContextualDeserializer {
    private static final String CONTENT = "content";
    private static final String NUMBER = "number";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private JavaType valueType;

    @Override
    public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final CollectionType valuesListType = ctxt.getTypeFactory().constructCollectionType(List.class, valueType);

        List<?> list = new ArrayList<>();
        int pageNumber = 0;
        int pageSize = 0;
        long total = 0;
        if (p.isExpectedStartObjectToken()) {
            p.nextToken();
            if (p.hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
                String propName = p.getCurrentName();
                do {
                    p.nextToken();
                    switch (propName) {
                        case CONTENT:
                            list = ctxt.readValue(p, valuesListType);
                            break;
                        case NUMBER:
                            pageNumber = ctxt.readValue(p, Integer.class);
                            break;
                        case SIZE:
                            pageSize = ctxt.readValue(p, Integer.class);
                            break;
                        case TOTAL_ELEMENTS:
                            total = ctxt.readValue(p, Long.class);
                            break;
                        default:
                            p.skipChildren();
                            break;
                    }
                } while (((propName = p.nextFieldName())) != null);
            } else {
                ctxt.handleUnexpectedToken(handledType(), p);
            }
        } else {
            ctxt.handleUnexpectedToken(handledType(), p);
        }

        //Note that Sort field of Page is ignored here.
        //Feel free to add more switch cases above to deserialize it as well.
        return new PageImpl<>(list, PageRequest.of(pageNumber, pageSize), total);
    }

    /**
     * This is the main point here.
     * The PageDeserializer is created for each specific deserialization with concrete generic parameter type of Page.
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        //This is the Page actually
        final JavaType wrapperType = ctxt.getContextualType();
        final PageDeserializer deserializer = new PageDeserializer();
        //This is the parameter of Page
        deserializer.valueType = wrapperType.containedType(0);
        return deserializer;
    }
}