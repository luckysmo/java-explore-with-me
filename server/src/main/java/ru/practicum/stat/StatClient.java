package ru.practicum.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.client.BaseClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {
    public static final String APP_NAME = "explore with me";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build());
    }

    public Object save(EventStatDto eventStatDto) {
        return post("/hit", eventStatDto);
    }

    public Object getStats(LocalDateTime start,
                           LocalDateTime end,
                           List<String> uris,
                           boolean unique) throws UnsupportedEncodingException {
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8),
                "uris", uris,
                "unique", unique
        );

        return get("/stats?start=" + parameters.get("start")
                + "&end=" + parameters.get("end")
                + "&uris=" + String.join(", ", uris)
                + "&unique=" + unique, parameters);
    }
}
