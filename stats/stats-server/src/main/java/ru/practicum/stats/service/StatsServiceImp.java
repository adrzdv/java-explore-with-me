package ru.practicum.stats.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectDto;
import ru.practicum.statsdto.HitObjectProjection;
import ru.practicum.statsdto.ParamObject;
import ru.practicum.stats.repo.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImp implements StatsService {

    private final StatsRepository repository;

    @Override
    @Transactional
    public HitObject hit(HitObject object) {

        return repository.save(object);

    }

    @Override
    public List<HitObjectDto> viewStats(ParamObject params) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String decodedStart = URLDecoder.decode(params.getStart(), StandardCharsets.UTF_8);
        String decodedEnd = URLDecoder.decode(params.getEnd(), StandardCharsets.UTF_8);

        LocalDateTime start = LocalDateTime.parse(decodedStart, formatter);
        LocalDateTime end = LocalDateTime.parse(decodedEnd, formatter);
        List<HitObjectProjection> temp;

        if (params.getUris() != null && params.getUnique() == null) {

            temp = repository.getStatsWithUries(params.getUris(), start, end);
            return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();


        } else if (params.getUnique() != null && params.getUris() == null) {

            if (params.getUnique()) {

                temp = repository.getUniqueStats(start, end);
                return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();

            } else {

                temp = repository.getStats(start, end);
                return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();

            }

        } else if (params.getUris() != null) {

            if (params.getUnique()) {

                temp = repository.getUniqueStatsWithUries(params.getUris(), start, end);
                return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();

            } else {

                temp = repository.getStatsWithUries(params.getUris(), start, end);
                return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();

            }

        }

        temp = repository.getStats(start, end);
        return temp.stream().map(StatsServiceImp::fromProjectionToDto).toList();

    }

    private static HitObjectDto fromProjectionToDto(HitObjectProjection projection) {
        return new HitObjectDto(projection.getApp(), projection.getUri(), projection.getHits());
    }

}
