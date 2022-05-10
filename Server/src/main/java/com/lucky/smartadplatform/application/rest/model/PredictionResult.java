package com.lucky.smartadplatform.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class represents an external API prediction result.
 * 
 * @author lucky
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PredictionResult {

    private String label;

    private Double score;

}
