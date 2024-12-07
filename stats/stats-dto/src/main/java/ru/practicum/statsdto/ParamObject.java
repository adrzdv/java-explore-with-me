package ru.practicum.statsdto;

import lombok.*;

import java.util.List;

/**
 * Object for collecting all request parameters in one
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamObject {

    private String start;
    private String end;
    private List<String> uris;
    private Boolean unique;

}
