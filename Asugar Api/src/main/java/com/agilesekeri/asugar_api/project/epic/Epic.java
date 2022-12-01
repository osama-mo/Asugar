package com.agilesekeri.asugar_api.project.epic;

import javax.persistence.*;

@Entity
public class Epic {
    @Id
    @SequenceGenerator(
            name = "epic_sequence",
            sequenceName = "epic_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "epic_sequence"
    )
    private Long id;
}
