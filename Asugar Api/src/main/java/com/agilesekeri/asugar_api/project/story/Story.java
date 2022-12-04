package com.agilesekeri.asugar_api.project.story;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Story {
    @Id
    @SequenceGenerator(
            name = "story_sequence",
            sequenceName = "story_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "story_sequence"
    )
    private Long id;
}
