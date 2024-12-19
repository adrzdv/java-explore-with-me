package ru.practicum.stats.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsdto.HitObject;
import ru.practicum.statsdto.HitObjectDto;
import ru.practicum.statsdto.ParamObject;
import ru.practicum.stats.service.StatsService;


@RestController
@AllArgsConstructor
public class StatsController {

    private final StatsService service;

    @PostMapping(value = "/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HitObject hit(@RequestBody @Valid HitObject object) {

        return service.hit(object);
    }


    @GetMapping(value = "/stats")
    @ResponseStatus(value = HttpStatus.OK)
    public List<HitObjectDto> getStats(@ModelAttribute ParamObject params) throws BadRequestException {

        return service.viewStats(params);
    }
}
