/*
 * *
 *  * Created by Kolawole Omirin
 *  * Copyright (c) 2024 . All rights reserved.
 *  * Last modified 8/7/24, 2:45 AM
 *
 */
package com.opxl.noblepay.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "web_requests")
@AllArgsConstructor
@NoArgsConstructor
public class WebPayRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;
    private int amount;
    @Lob
    @Column(name = "auth_data")
    private String authData;
    private  String customerId;
    private String transactionRef;
    private String currency;
    private String callbackUrl;
    private String paymentId;
    private String responseCode;
    private String responseMessage;
    private String retrievalReferenceNumber;
    private String terminalId;
    private String token;
    private String stan;
    private String panLast4Digits;
    private String bankCode;
    private String tokenExpiryDate;
    private String transactionIdentifier;
    private String cardType;
    private String md;
    private String transactionId;

    @Type(JsonType.class)
    @Column(name="json")
    private  String deviceInformation;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime updatedOn;


    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @PrePersist
    private  void onPersist(){ setDateCreated( LocalDateTime.now());}


    @PrePersist
    private  void onUpdated(){ setUpdatedOn( LocalDateTime.now());}
}
