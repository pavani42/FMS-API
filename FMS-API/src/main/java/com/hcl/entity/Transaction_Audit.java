package com.hcl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Transaction_Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String iMessage;
    private LocalDateTime messgInTime;
    private StatusMessage status;
    private ReasonCodes reasonCode;
    private LocalDateTime messOutTime;
}
