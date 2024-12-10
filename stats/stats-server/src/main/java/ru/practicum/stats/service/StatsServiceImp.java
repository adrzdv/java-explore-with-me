package ru.practicum.stats.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectProjection;
import ru.practicum.statsdto.ParamObject;
import ru.practicum.stats.repo.StatsRepository;

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
    public List<HitObjectProjection> viewStats(ParamObject params) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(params.getStart().replace("\"", ""), formatter);
        LocalDateTime end = LocalDateTime.parse(params.getEnd().replace("\"", ""), formatter);


        if (params.getUris() != null && params.getUnique() == null) {

            return repository.getStatsWithUries(params.getUris(), start, end);

        } else if (params.getUnique() != null && params.getUris() == null) {

            if (params.getUnique()) {
                return repository.getUniqueStats(start, end);
            } else {
                return repository.getStats(start, end);
            }

        } else if (params.getUris() != null) {

            if (params.getUnique()) {
                return repository.getUniqueStatsWithUries(params.getUris(), start, end);
            } else {
                return repository.getStatsWithUries(params.getUris(), start, end);
            }

        }

        return repository.getStats(start, end);
    }

}
