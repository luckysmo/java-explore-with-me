package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventStatDto;
import ru.practicum.dto.EventStatDtoView;
import ru.practicum.repository.EventStatRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.EventStatMapper.eventStatFromDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventStatServiceImpl implements EventStatService {
    private final EventStatRepository repository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(EventStatDto eventStatDto) {
        repository.save(eventStatFromDto(eventStatDto));
    }

    @Override
    public List<EventStatDtoView> getEventStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        String sqlQuery = "SELECT app, uri, count(hits) as hits " +
                "FROM (Select Distinct app, uri, 1 as hits FROM endpoint_hit eh " +
                ") as p " +
                "group by p.app, p.uri";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("start", start);
        paramSource.addValue("end", end);
        paramSource.addValue("uris", uris);

        return jdbcTemplate.query(
                unique ? sqlQuery : sqlQuery.replace("Distinct", ""),
                paramSource,
                (rs, rowNum) -> makeDtoView(rs));
    }

    private EventStatDtoView makeDtoView(ResultSet rs) throws SQLException {
        EventStatDtoView dtoView = new EventStatDtoView();
        dtoView.setApp(rs.getString("app"));
        dtoView.setUri(rs.getString("uri"));
        dtoView.setHits(rs.getLong("hits"));

        return dtoView;
    }
}
