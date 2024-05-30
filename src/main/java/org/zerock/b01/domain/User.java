package org.zerock.b01.domain;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //자동 1씩증가 시퀀스 IDENTITY는 마리아db, ~~ mysql
    private Long id;

    private String mid;

    @Column(length = 20,nullable = false)
    private String mpw;
    @Column(length = 20,nullable = false)
    private String mname;
}
